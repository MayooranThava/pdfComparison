package pdf

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import com.testautomationguru.utility.PDFUtil
import com.testautomationguru.utility.CompareMode

import internal.GlobalVariable
import java.io.File;
import java.io.FileWriter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.ss.usermodel.Cell;

import java.io.File;
import java.io.IOException;
public class Comparison {

	static XSSFWorkbook createExcel() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		return workbook
	}

	static boolean addRecord(ArrayList<String> record, XSSFSheet dataSheet,int rowNum) {
		XSSFRow dataRow;
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put(rowNum.toString(), record)
		Set<String> keyid = data.keySet();
		for (String key: keyid) {
			dataRow = dataSheet.createRow(rowNum);
			Object[] objectArr = data.get(key);
			int cellid = 0;

			for (Object obj : objectArr) {
				Cell cell = dataRow.createCell(cellid++)
				cell.setCellValue((String)obj)
			}
		}


		return true
	}

	static boolean publishExcel(XSSFWorkbook workbook, String fileNamePath) {
		FileOutputStream out = new FileOutputStream(new File(fileNamePath));
		workbook.write(out);
		out.close();
		return true;
	}



	static boolean comparePDF(String firstPDFPath, String secondPDFPath, String highlightedJPGPath) {

		PDFUtil pdfUtil = new PDFUtil();
		pdfUtil.setCompareMode(CompareMode.VISUAL_MODE);
		pdfUtil.highlightPdfDifference(true);
		pdfUtil.setImageDestinationPath(highlightedJPGPath);
		println("Here is where the image is saved:"+ highlightedJPGPath)
		return pdfUtil.compare(firstPDFPath, secondPDFPath);
	}



	static boolean imageToPixel(String highlightedJPGPath, int threshold) {
		// Threshold == 10 -1 -1 -1 -1 -1
		//Reading the image
		File file= new File(highlightedJPGPath);
		BufferedImage img = ImageIO.read(file);
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				//Retrieving contents of a pixel
				int pixel = img.getRGB(x,y);
				//Creating a Color object from pixel value
				Color color = new Color(pixel, true);
				//Retrieving the R G B values
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				if (red && blue == 255 && green == 0) {
					threshold -= 1;
				}
				if (threshold == 0) {
					println("Mismatch in header")
					return false
				}


			}
		}
		return true;

	}

	static String[] subdirectories(String folderPath) {
		File directoryPath = new File(folderPath);
		String[] contents = directoryPath.list();
		return contents



	}

}

