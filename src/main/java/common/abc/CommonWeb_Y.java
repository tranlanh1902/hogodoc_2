package common.abc;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import io.cucumber.datatable.DataTable;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.thucydides.core.webdriver.WebDriverFacade;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.gargoylesoftware.htmlunit.ScriptException;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
/**
 * @author Lanhtt10
 */
public class CommonWeb_Y extends PageObject {
	static WebElement element;
	static WebDriverWait waitExplicit;
	static JavascriptExecutor javascriptExecutor;
	static Actions action;
	static String osName = System.getProperty("os.name");
	static String workingDr = System.getProperty("user.dir");
	static String platWin = "windows", platMac = "mac";

	static String timeOutShort = "time.short.timeout", timeOutLong = "time.long.timeout";

	public WebDriver getWebDriver() {
		return (WebDriver) ((WebDriverFacade) getDriver()).getProxiedDriver();
	}

	/*
	 * ========================================================================== *.FEATURE ==========================================================================
	 */
	// get giá trị từng colum Table trong file *.feature
	/**
	 * @param table      : tên bảng Example
	 * @param columnName : tên cột của bảng Examples
	 */
	public String getValueColumnDataTableBDD(DataTable table, String columnName) {
		String valueColumn = null;
		String valueColumnRe = null;
		List<Map<String, String>> asMaps = table.asMaps(String.class, String.class);
		for (Map<String, String> featureMap : asMaps) {
			valueColumn = featureMap.get(columnName);
			valueColumnRe = valueColumn.replaceAll("\"", "");
		}
		return valueColumnRe;
	}

	/*
	 * ========================================================================== SESSION ==========================================================================
	 */
	// trả về source của page
	public String getSourceCodePage() {
		return getDriver().getPageSource();
	}

	/* ==== OCR ==== */
	// get OCR với ảnh chụp toàn màn hình
	/**
	 * @param language : tên ngôn ngữ cần OCR từ file ảnh lấy từ danh sách file src/test/resources/OCR/tessdata (VD: "eng", "vie",....)
	 */
	public String getScreenshotOCR(String language) {
		// String language = lấy trong list danh sách file tessdata (VD:"eng" hoặc "vie"
		// ......);

		String pathImg = getPathCaptureScreenshot();

		ITesseract instance = new Tesseract();
		if (osName.toLowerCase().contains(platWin)) {
			instance.setDatapath(String.format(workingDr + "\\%s", "src\\test\\resources\\OCR\\tessdata"));
		} else if (osName.toLowerCase().contains(platMac)) {
			instance.setDatapath(String.format(workingDr + "/%s", "src/test/resources/OCR/tessdata"));
		} else {
			System.out.println("not found OS get pathFile downloaded");
		}

		instance.setLanguage(language);
		String text = null;
		try {
			return text = instance.doOCR(new File(pathImg));

		} catch (TesseractException e) {
			e.printStackTrace();
		}

		return text;
	}

	// get OCR với ảnh chụp element
	/**
	 * @param language : tên ngôn ngữ cần OCR từ file ảnh lấy từ danh sách file src/test/resources/OCR/tessdata (VD: "eng", "vie",....)
	 * @param xpath    : xpath element cần OCR
	 * @param values   : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public String getElementOCR(String language, String xpath, String... values) {
		// String language = lấy trong list danh sách file tessdata (VD:"eng" hoặc "vie"
		// ......);

		String pathImg = getPathCaptureElementshot(xpath, values);

		ITesseract instance = new Tesseract();
		if (osName.toLowerCase().contains(platWin)) {
			instance.setDatapath(String.format(workingDr + "\\%s", "src\\test\\resources\\OCR\\tessdata"));
		} else if (osName.toLowerCase().contains(platMac)) {
			instance.setDatapath(String.format(workingDr + "/%s", "src/test/resources/OCR/tessdata"));
		} else {
			System.out.println("not found OS get pathFile downloaded");
		}

		instance.setLanguage(language);
		String text = null;
		try {
			return text = instance.doOCR(new File(pathImg));

		} catch (TesseractException e) {
			e.printStackTrace();
		}

		return text;
	}

	// chụp ảnh toàn màn hình
	public String getPathCaptureScreenshot() {
		deleteAllFileInFolderScreenshort();
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
			File source = ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.FILE);
			String path = null;

			if (osName.toLowerCase().contains(platWin)) {
				path = String.format(workingDr + "\\%s", "src\\test\\resources\\OCR\\screenshot\\img_" + formater.format(calendar.getTime()) + ".png");
			} else if (osName.toLowerCase().contains(platMac)) {
				path = String.format(workingDr + "/%s", "src/test/resources/OCR/screenshot/img_" + formater.format(calendar.getTime()) + ".png");
			} else {
				System.out.println("not found OS get pathFile downloaded");
			}

			FileUtils.copyFile(source, new File(path));
			return path;
		} catch (IOException e) {
			System.out.println("Exception while taking screenshot: " + e.getMessage());
			return e.getMessage();
		}

	}

	// chụp ảnh đối với element
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public String getPathCaptureElementshot(String xpath, String... values) {
		deleteAllFileInFolderScreenshort();
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");

			File source = getElement(xpath, values).getScreenshotAs(OutputType.FILE);

			String path = null;

			if (osName.toLowerCase().contains(platWin)) {
				path = String.format(workingDr + "\\%s", "src\\test\\resources\\OCR\\screenshot\\img_" + formater.format(calendar.getTime()) + ".png");
			} else if (osName.toLowerCase().contains(platMac)) {
				path = String.format(workingDr + "/%s", "src/test/resources/OCR/screenshot/img_" + formater.format(calendar.getTime()) + ".png");
			} else {
				System.out.println("not found OS get pathFile downloaded");
			}

			FileUtils.copyFile(source, new File(path));
			return path;
		} catch (IOException e) {
			System.out.println("Exception while taking screenshot: " + e.getMessage());
			return e.getMessage();
		}

	}

	public void deleteAllFileInFolderScreenshort() {
		String path = null;
		try {
			if (osName.toLowerCase().contains(platWin)) {
				path = String.format(workingDr + "\\%s", "src\\test\\resources\\OCR\\screenshot");
			} else if (osName.toLowerCase().contains(platMac)) {
				path = String.format(workingDr + "/%s", "src/test/resources/OCR/screenshot");
			} else {
				System.out.println("not found OS get pathFile downloaded");
			}

			File file = new File(path);
			File[] listOfFiles = file.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					new File(listOfFiles[i].toString()).delete();
				}
			}
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}

	/*
	 * ========================================================================== ELEMENT ==========================================================================
	 */
	/* ===== element ===== */
	// trả về element
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public WebElementFacade getElement(String xpath, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible();
	}

	/* ===== List element ===== */
	// trả về list element
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public List<WebElementFacade> getListElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return findAll(xpath);
	}

	// trả về số lượng element
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public int countNumberElement(String xpath, String... values) {
		return getListElement(xpath, values).size();
	}

	/* ===== Atrributes ===== */
	// trả về text element
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public String getText(String xpath, String... values) {
		highlightElementJS(xpath, values);

		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible().getText();
	}

	// trả về tag name
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public String getTagName(String xpath, String... values) {
		highlightElementJS(xpath, values);

		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible().getTagName();
	}

	// trả về giá trị attribute element
	/**
	 * @param xpath         : xpath element cần lấy
	 * @param attributeName : tên attribute của element (VD: id, name, class, style...)
	 * @param values        : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public String getAttributeValue(String xpath, String attributeName, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible().getAttribute(attributeName);
	}

	// trả về toạ độ(x,y) element
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public Point getLocationElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible().getLocation();
	}

	// trả về toạ độ X element
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public int getLocation_X_Element(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible().getLocation().x;
	}

	// trả về toạ độ Y element
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public int getLocation_Y_Element(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible().getLocation().y;
	}

	// trả về size(width, height) element
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public Dimension getSizeElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible().getSize();
	}

	
	// trả về width element
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public int getWidthElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible().getSize().getWidth();
	}

	// trả về height element
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public int getHeightElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).waitUntilVisible().getSize().getHeight();
	}

	// verify element chứa text
	/**
	 * @param xpath            : xpath element cần lấy
	 * @param containTextValue : giá trị text của element mong muốn
	 * @param values           : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void verifyContainTextValueElement(String xpath, String containTextValue, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).waitUntilVisible().shouldContainText(containTextValue);
	}

	/* ==== kiểm tra trạng thái element ==== */
	// is Enable
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public boolean isControlEnable(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).isEnabled();
	}

	// is Clickable
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public boolean isControlClickable(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).isClickable();
	}

	// is Disabled
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public boolean isControlDisabled(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).isDisabled();
	}

	// is Visible
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public boolean isControlVisible(String xpath, String... values) {
		// element có trong Dom + có trên UI
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).isVisible();
	}

	// is Present
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public boolean isControlPresent(String xpath, String... values) {
		// element có/ko có trên UI + có trong Dom
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).isPresent();
	}

	/* ==== verify trạng thái element ==== */
	// verify enabled element
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void verifyEnabledElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).shouldBeEnabled();
	}

	// verify visible element
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void verifyVisibleElement(String xpath, String... values) {
		// element có trong Dom + có trên UI
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).shouldBeVisible();
	}

	// verify invisible element
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void verifyInvisibleElement(String xpath, String... values) {
		// element có/ko có trong Dom + ko có trên UI
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).shouldNotBeVisible();
	}

	// verify present element
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void verifyPresentElement(String xpath, String... values) {
		// element có/ko có trên UI + có trong Dom
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).shouldBePresent();
	}

	// click element
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void clickToElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).waitUntilVisible().click();
	}

	/* ===== checkbox/radio button ===== */
	// is Selected
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public boolean isControlSelected(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		return element(xpath).isSelected();
	}

	/* ===== toast message ===== */
	// verify element chứa text đối với toast mesage
	/**
	 * @param xpath            : xpath element cần lấy
	 * @param containTextValue : giá trị text của element mong muốn
	 * @param values           : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void verifyContainTextValueToastMessage(String xpath, String containTextValue, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).shouldContainText(containTextValue);
	}

	/* ===== Textbox/textarea ===== */
	// clear
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void clearToElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).waitUntilVisible().clear();
	}

	// sendkey
	/**
	 * @param xpath           : xpath element cần lấy
	 * @param valueToSendkey: giá trị muốn senkey vào element
	 * @param values          : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void sendkeyToElement(String xpath, String valueToSendkey, String... values) {
		// Trường hợp senkey nhiều dòng thì xuống dòng textare là dấu \n (VD: Cộng hoà
		// xã hội chủ nghĩa VN\nĐộc lập - tự do - hạnh phúc")
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).waitUntilVisible().sendKeys(valueToSendkey);
	}

	// sendkey and Enter
	/**
	 * @param xpath           : xpath element cần lấy
	 * @param valueToSendkey: giá trị muốn senkey vào element
	 * @param values          : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void sendkeyAndEnterToElement(String xpath, String valueToSendkey, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element(xpath).waitUntilVisible().typeAndEnter(valueToSendkey);
	}

	// clear sau đó sendKey
	/**
	 * @param xpath          : xpath element cần lấy
	 * @param valueToSendkey : giá trị muốn senkey vào element
	 * @param values         : tham số dynamic trong String xpath (có thể có hoặc không)
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public void clearBeforeSendKeyToElement(String xpath, String valueToSendkey, String... values) throws InterruptedException, NumberFormatException, IOException {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);

		element(xpath).waitUntilVisible().clear();
		Thread.sleep(Integer.valueOf(getProperties(timeOutShort)));
		element(xpath).waitUntilVisible().sendKeys(valueToSendkey);
	}

	// sendkey đối với textbox chỉ là số (type="number")
	/**
	 * @param xpath          : xpath element cần lấy
	 * @param numberExpected : số muốn chọn
	 * @param values         : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void sendkeyNumberToElement(String xpath, int numberExpected, String... values) {
		highlightElementJS(xpath, values);

		String valueNumberTxt = getAttributeValue(xpath, "value", values);
		int countCurrent = Integer.parseInt(valueNumberTxt);

		int downOrUp;
		int diff = numberExpected - countCurrent;

		if (diff > 0) {
			downOrUp = 1;
		} else if (diff < 0) {
			diff = -1 * diff;
			downOrUp = -1;
		} else {
			downOrUp = 0;
		}

		for (int i = 0; i < diff; i++) {
			if (downOrUp == 1) {
				sendKeyBoardToElement(xpath, Keys.ARROW_UP, values);
			} else if (downOrUp == -1) {
				sendKeyBoardToElement(xpath, Keys.ARROW_DOWN, values);

			} else {
				break;
			}
		}
	}

	/* ===== Dropdown ===== */
	/* == Default Dropdownlist == */
	// trả về danh sách các giá trị trong Defaul dropdownlist
	/**
	 * @param xpath  : xpath element cần lấy (element có tagname là <select> )
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public List<String> getListSelectOptionInDefaultDropdown(String xpath, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		highlightElementJS(xpath);
		return element(xpath).getSelectOptions();
	}

	// trả về số lượng các giá trị trong Default Dropdownlist
	/**
	 * @param xpath  : xpath element cần lấy (element có tagname là <select> )
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public int countSelectOptionInDefaultDropdown(String xpath, String... values) {
		return getListSelectOptionInDefaultDropdown(xpath, values).size();
	}

	/* Single Default Dropdowlist */
	// Chọn 1 giá trị trong Default Single Dropdownlist
	/**
	 * @param xpath           : xpath element cần lấy (element có tagname là <select> )
	 * @param valueItemChoose : giá trị có trong dropdown muốn chọn
	 * @param values          : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void selectItemInSingleDefaultDropdown(String xpath, String valueItemChoose, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		highlightElementJS(xpath);
		element(xpath).deselectByVisibleText(valueItemChoose);
	}

	// trả về giá đã chọn trong Single Default Dropdowlist
	/**
	 * @param xpath  : xpath element cần lấy (element có tagname là <select> )
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public String getSelectedItemInSingleDefaultDropDown(String xpath, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		highlightElementJS(xpath);
		return element(xpath).getSelectedVisibleTextValue();
	}

	/* Multiple Default Dropdowlist */
	// chọn nhiều giá trị trong Multiple Defautl Dropdownlist
	/**
	 * @param xpath             : xpath element cần lấy (element có tagname là <select> )
	 * @param expectedValueItem : Array các giá trị có trong dropdown muốn chọn
	 * @param values            : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void selectItemInMultipleDefaultDropdown(String xpath, String[] expectedValueItem, String... values) {
		// String expectValue[] = { "Unit","Desktop"};
		xpath = String.format(xpath, (Object[]) values);
		highlightElementJS(xpath);

		List<String> valueOption = element(xpath).waitUntilVisible().getSelectOptions();

		Select select = new Select(element(xpath));
		for (String b : valueOption) {
			for (String a : expectedValueItem) {
				if (b.equals(a)) {
					select.selectByVisibleText(a);
				}
			}
		}
	}

	// trả về giá trị vừa chọn trong Multiple Default Dropdownlist
	/**
	 * @param xpath             : xpath element cần lấy (element có tagname là <select> )
	 * @param expectedValueItem : Array các giá trị có trong dropdown muốn chọn
	 * @param values            : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public List<String> getSelectedItemInMultipleDefaultDropDown(String xpath, String[] expectedValueItem, String... values) {
		// String expectValue[] = { "Unit","Desktop"};
		xpath = String.format(xpath, (Object[]) values);
		highlightElementJS(xpath);

		List<String> actualValueItem = new ArrayList<String>();
		Select select = new Select(element(xpath));
		List<WebElement> itemSelected = select.getAllSelectedOptions();
		for (WebElement a : itemSelected) {
			actualValueItem.add(a.getText());
		}
		return actualValueItem;
	}

	/* ==Custom Dropdowlist == */
	// Chọn 1 giá trị trong Single Custom dropdownlist
	/**
	 * @param xpathParent       : xpath cha dropdown, click vào xổ ra các option
	 * @param xpathAllItem      : xpath con chứa các giá trị option
	 * @param expectedValueItem : giá trị trong dropdown muốn chọn
	 */
	public void selectItemInSingleCustomDropdown(String xpathParent, String xpathAllItem, String expectedValueItem) throws InterruptedException {
		// 1. Click vào dropDown và cho nó xổ hết các giá trị ra
		element(xpathParent).waitUntilVisible().click();

		// 2. Chờ và hiển thị cho tất cả các giá trị dropdown được load ra
		element(xpathAllItem).waitUntilPresent();
		List<WebElement> allIteams = getDriver().findElements(By.xpath(xpathAllItem));

		for (WebElement childElement : allIteams) {
			if (childElement.getText().equals(expectedValueItem)) {
				// 3. Scoll đến giá trị muốn chọn
				scrollToElementJS1(childElement);
				Thread.sleep(2000);

				// 4. Click vào item cần chọn
				childElement.click();
				Thread.sleep(2000);
				break;
			}
		}
	}

	// Chọn nhiều giá trị trong Multiple Custom dropdownlist
	/**
	 * @param xpathParent       : xpath cha dropdown, click vào xổ ra các option
	 * @param xpathAllItem      : xpath con chứa các giá trị option
	 * @param expectedValueItem : Array các giá trị trong dropdown muốn chọn
	 */
	public void selectItemInMultipleCustomDropdown(String xpathParent, String xpathAllItem, String[] expectedValueItem) throws InterruptedException {
		// String expectValue[] = { "Unit","Desktop"};

		// 1. Click vào dropDown và cho nó xổ hết các giá trị ra
		element(xpathParent).waitUntilVisible().click();

		// 2. Chờ và hiển thị cho tất cả các giá trị dropdown được load ra
		element(xpathAllItem).waitUntilPresent();
		List<WebElement> allIteams = getDriver().findElements(By.xpath(xpathAllItem));

		// 3. Chọn những element theo String mong muốn chọn
		for (WebElement childElement : allIteams) {
			for (String item : expectedValueItem) {
				if (childElement.getText().equals(item)) {
					// 3. Scoll đến giá trị muốn chọn
					scrollToElementJS1(childElement);
					Thread.sleep(2000);
					// 4. Click vào item cần chọn
					childElement.click();
				}
			}

		}
	}

	/* ==== Alert ==== */
	// click OK button của alert
	public void clickToAcceptAlertBtn() throws NumberFormatException, IOException {
		waitForAlertPresence();
		getAlert().accept();
	}

	// click Cancle button của alert
	public void clickToCancleAlertBtn() throws NumberFormatException, IOException {
		waitForAlertPresence();
		getAlert().dismiss();
	}

	// get message của aleart
	public String getTextInAleart() throws NumberFormatException, IOException {
		waitForAlertPresence();
		return getAlert().getText();
	}

	// senkey to aleart
	/**
	 * @param valueToSendkey : giá trị muốn senkey vào element
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public void sendKeysToAleart(String valueToSendkey) throws NumberFormatException, IOException {
		waitForAlertPresence();
		getAlert().sendKeys(valueToSendkey);
	}

	// is alert present
	public boolean isAlertPresent() {
		try {
			getDriver().switchTo().alert();
			return true;
		} catch (NoAlertPresentException Ex) {
			return false;
		}
	}

	// wait cho đến khai alert present
	public void waitForAlertPresence() throws NumberFormatException, IOException {
		waitExplicit = new WebDriverWait(getDriver(), Integer.valueOf(getProperties(timeOutLong)));
		waitExplicit.until(ExpectedConditions.alertIsPresent());
	}

	/* ===== Data Picker ===== */
	/**
	 * @param xpath         : xpath element cần lấy
	 * @param dateToSendKey : giá trị format date muốn senkey vào element
	 * @param values        : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void sendkeyToDataPicker(String xpath, String dateToSendKey, String... values) {
		removeAttributeJS(xpath, "type", values);
		sendkeyToElement(xpath, dateToSendKey, values);
	}

	/* ===== Upload ===== */
	// - C1: Sendkey (Dùng trong tất cả hệ điều hành,browser)
	/**
	 * @param fileName           : Array các file muốn upload
	 * @param xpathAddFileButton : xpath Browse button
	 * @param values             : tham số dynamic trong String xpathAddFileButton (có thể có hoặc không)
	 */
	public void uploadFileBySendkeysFile(String fileName[], String xpathAddFileButton, String... values) {
		// String fileName[] = { "add.pdf","edit.pdf"};
		for (String a : fileName) {
			String pathFile = null;
			if (osName.toLowerCase().contains(platWin)) {
				pathFile = "\\src\\test\\resources\\upload\\";
			} else if (osName.toLowerCase().contains(platMac)) {
				pathFile = "/src/test/resources/upload/";
			} else {
				System.out.println("not found OS get pathFile");
			}
			pathFile = workingDr + pathFile + a;
			sendkeyToElement(xpathAddFileButton, pathFile, values);
		}
	}

	
	// - C2: Java robot (Dùng trong tất cả hệ điều hành,browser)
	/**
	 * @param fileName           : Array các file muốn upload
	 * @param xpathAddFileButton : xpath Browse button
	 * @param values             : tham số dynamic trong String xpathAddFileButton (có thể có hoặc không)
	 */
	public void uploadByJavaRobot(String[] fileName, String xpathBrowserButton, String... value) throws AWTException, InterruptedException, ScriptException, IOException {
		for (String a : fileName) {
			highlightElementJS(xpathBrowserButton, value);
			String pathFile = null;

			if (osName.toLowerCase().contains(platWin)) {
				pathFile = "\\src\\test\\resources\\upload\\";
			} else if (osName.toLowerCase().contains(platMac)) {
				pathFile = "/src/test/resources/upload/";
			} else {
				System.out.println("not found OS get pathFile upload");
			}

			pathFile = workingDr + pathFile + a;

			clickToElement(xpathBrowserButton);

			StringSelection stringSelection = new StringSelection(pathFile);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

			Robot robot = new Robot();
			Thread.sleep(5000);

			if (osName.toLowerCase().contains(platWin)) {
				robot.keyPress(KeyEvent.VK_CONTROL);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_CONTROL);
				robot.keyRelease(KeyEvent.VK_V);
				Thread.sleep(5000);

			} else if (osName.toLowerCase().contains(platMac)) {
				Runtime runtime = Runtime.getRuntime();
				if (getDriver().toString().toLowerCase().contains("chrome")) {
					System.out.println("chrome");
					String[] args = { "osascript", "-e", "tell app \"Chrome\" to activate" };
					runtime.exec(args);
				} else if (getDriver().toString().toLowerCase().contains("firefox")) {
					System.out.println("firefox");
					String[] args = { "osascript", "-e", "tell app \"Firefox\" to activate" };
					runtime.exec(args);
				} else {
					System.out.println("not found browser in" + osName);
				}

				Thread.sleep(5000);
				// Open go to window
				robot.keyPress(KeyEvent.VK_META);
				robot.keyPress(KeyEvent.VK_SHIFT);
				robot.keyPress(KeyEvent.VK_G);
				robot.keyRelease(KeyEvent.VK_META);
				robot.keyRelease(KeyEvent.VK_SHIFT);
				robot.keyRelease(KeyEvent.VK_G);
				Thread.sleep(5000);

				// Paste the clipboard value
				robot.keyPress(KeyEvent.VK_META);
				robot.keyPress(KeyEvent.VK_V);
				robot.keyRelease(KeyEvent.VK_META);
				robot.keyRelease(KeyEvent.VK_V);
				Thread.sleep(5000);

				// Press Enter key to close the Goto window and Upload window
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				Thread.sleep(10000);

			} else {
				System.out.println("not found OS action robot");
			}

			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		}
	}

	/* ===== Download ===== */
	// download với file có tên cố định
	/**
	 * @param fullNameFileDownload : tên đầy đủ của file luôn cố định tải về (VD: 'hoso.pdf')
	 * @param xpath                : xpath element cần click vào để download được file
	 * @param values               : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void downloadFileFullName(String fullNameFileDownload, String xpath, String... values) throws Exception {
		// String fullNameFile = "abc.xlsx"
		// 1. xoá toàn bộ file trong thư muc
		deleteAllFileInFolder();

		// 2.click element để download
		scrollToElementJS(xpath, values);
		clickToElement(xpath, values);

		// 3. đợi đến khi file được tải về
		waitForDownloadFileFullnameCompleted(fullNameFileDownload);

		// 4.đếm số lượng file trong thư mục sau khi tải về và verify
		int countFileBeforeDelete = countFilesInDirectory();
		Assert.assertEquals(countFileBeforeDelete, 1);

		// 5.xoá file tải về
		deleteFileFullName(fullNameFileDownload);

		// 6.đếm số lượng file trong thư mục sau khi xoá và verify
		int countFileAfterDelete = countFilesInDirectory();
		Assert.assertEquals(countFileAfterDelete, 0);
	}

	// dowload với file có tên chỉ có 1 phần cố định
	/**
	 * @param containNameFile : tên 1 phần tên file cố định khi tải về (VD: '.pdf')
	 * @param xpath           : xpath element cần click vào để download được file
	 * @param values          : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void downloadFileContainName(String containNameFile, String xpath, String... values) throws Exception {
		// String containNameFile =".xlsx"
		// 1. xoá toàn bộ file trong thư muc
		deleteAllFileInFolder();

		// 2.click element để download
		scrollToElementJS(xpath, values);
		clickToElement(xpath, values);

		// 3. đợi đến khi file được tải về
		waitForDownloadFileContainsNameCompleted(containNameFile);

		// 4.đếm số lượng file trong thư mục sau khi tải về và verify
		int countFileBeforeDelete = countFilesInDirectory();
		Assert.assertEquals(countFileBeforeDelete, 1);

		// 5.xoá file tải về
		deleteFileContainName(containNameFile);

		// 6.đếm số lượng file trong thư mục sau khi xoá và verify
		int countFileAfterDelete = countFilesInDirectory();
		Assert.assertEquals(countFileAfterDelete, 0);
	}

	// verify file được tải về
	public void waitForDownloadFileFullnameCompleted(String fileName) throws Exception {
		int i = 0;
		while (i < 30) {
			boolean exist = isFileExists(fileName);
			if (exist == true) {
				i = 30;
			}
			Thread.sleep(500);
			i = i + 1;
		}
	}

	public void waitForDownloadFileContainsNameCompleted(String fileName) throws Exception {
		int i = 0;
		while (i < 30) {
			boolean exist = isFileContain(fileName);
			if (exist == true) {
				i = 30;
			}
			Thread.sleep(500);
			i = i + 1;
		}
	}

	// đếm số file tải về
	public int countFilesInDirectory() {
		String pathFolderDownload = getPathContainDownload();
		File file = new File(pathFolderDownload);
		int i = 0;
		for (File listOfFiles : file.listFiles()) {
			if (listOfFiles.isFile()) {
				i++;
			}
		}
		return i;
	}

	// đường dẫn downloaded
	public String getPathContainDownload() {
		String path = null;
		if (osName.toLowerCase().contains(platWin)) {
			path = String.format(workingDr + "\\%s", "src\\test\\resources\\downloaded\\");

		} else if (osName.toLowerCase().contains(platMac)) {
			path = String.format(workingDr + "/%s", "src/test/resources/downloaded/");

		} else {
			System.out.println("not found OS get pathFile downloaded");
		}
		return path;
	}

	// xoá file
	public void deleteFileFullName(String fileName) {
		if (isFileExists(fileName)) {
			deleteFullName(fileName);
		}
	}

	public void deleteFullName(String fileName) {
		try {
			if (isFileExists(fileName)) {
				String pathFolderDownload = getPathContainDownload();
				File files = new File(pathFolderDownload + fileName);
				files.delete();
			}
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}

	public void deleteFileContainName(String fileName) {
		deleteContainName(fileName);
	}

	public void deleteContainName(String fileName) {
		try {
			String files;
			String pathFolderDownload = getPathContainDownload();
			File file = new File(pathFolderDownload);
			File[] listOfFiles = file.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					files = listOfFiles[i].getName();
					if (files.contains(fileName)) {
						new File(listOfFiles[i].toString()).delete();
					}
				}
			}
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}

	public void deleteAllFileInFolder() {
		try {
			String pathFolderDownload = getPathContainDownload();
			File file = new File(pathFolderDownload);
			File[] listOfFiles = file.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					new File(listOfFiles[i].toString()).delete();
				}
			}
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}

	// kiểm tra file có tồn tại không
	public boolean isFileExists(String file) {
		try {
			String pathFolderDownload = getPathContainDownload();
			File files = new File(pathFolderDownload + file);
			boolean exists = files.exists();
			return exists;
		} catch (Exception e) {
			System.out.print(e.getMessage());
			return false;
		}

	}

	public boolean isFileContain(String fileName) {
		try {
			boolean flag = false;
			String pathFolderDownload = getPathContainDownload();
			File dir = new File(pathFolderDownload);
			File[] files = dir.listFiles();
			if (files == null || files.length == 0) {
				flag = false;
			}
			for (int i = 1; i < files.length; i++) {
				if (files[i].getName().contains(fileName)) {
					flag = true;
				}
			}
			return flag;
		} catch (Exception e) {
			System.out.print(e.getMessage());
			return false;
		}
	}

	/* ==== Table ==== */
	// trả về danh sách giá trị của 1 danh sách element (thường dùng khi lấy giá trị
	// 1 colum table chức năng tìm kiếm)
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public List<String> getListText(String xpath, String... values) {
		List<String> lstValue = new ArrayList<String>();

		xpath = String.format(xpath, (Object[]) values);

		List<WebElementFacade> lstElement = findAll(xpath);
		int size = lstElement.size();
		if (size > 0) {
			highlightElementJS(xpath, values);
			for (WebElementFacade a : lstElement) {
				lstValue.add(a.waitUntilVisible().getText());
			}
			lstValue.removeAll(Collections.singleton(null));
			lstValue.removeAll(Collections.singleton(" "));
		}
		System.out.println("UI= " + lstValue);
		return lstValue;
	}

	/*
	 * ========================================================================== TƯƠNG TÁC: CHUỘT VÀ BÀN PHÍM ==========================================================================
	 */
	/* ==== Mouse ==== */
	// click
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void clickMouseToElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element = element(xpath);

		action = new Actions(getDriver());
		action.click(element).perform();
	}

	// Double Click
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void doubleClickMouseToEment(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element = element(xpath);

		action = new Actions(getDriver());
		action.doubleClick(element).perform();
	}

	// Hover
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void hoverMouseToElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element = element(xpath);

		action = new Actions(getDriver());
		action.moveToElement(element).perform();
	}

	// click chuột phải
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void rightMouseClickToElement(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element = element(xpath);

		action = new Actions(getDriver());
		action.contextClick(element).perform();
	}

	// drapAndDrop (kéo và thả )
	/**
	 * @param xpathSource : xpath điểm bắt đầu kéo
	 * @param xpathTarget : xpath điểm kết thúc thả
	 */
	public void dragAndDropMouse(String xpathSource, String xpathTarget) {
		WebElementFacade elementSource = element(xpathSource);
		WebElementFacade elementTarget = element(xpathTarget);

		highlightElement1(elementSource);
		highlightElement1(elementTarget);

		action = new Actions(getDriver());
		action.dragAndDrop(elementSource, elementTarget).perform();
	}

	// ClickAndHold (click và giữ: chọn nhiều item liên tiếp)
	/**
	 * @param xpathAllItem : xpath chứa toàn bộ element muốn chọn
	 * @param indexFrom    : index của element bắt đầu muốn chọn trong list element có xpath là xpathAllItem
	 * @param indexTo      : index của element kết thúc muốn chọn trong list element có xpath là xpathAllItem
	 * @param values       : tham số dynamic trong String xpathAllItem (có thể có hoặc không)
	 */
	public void clickAndHoldMouseFromToElement(String xpathAllItem, int indexFrom, int indexTo, String... values) {
		// Chọn nhiều item trong cùng 1 list (VD: chọn từ 1-> 4)
		xpathAllItem = String.format(xpathAllItem, (Object[]) values);
		List<WebElementFacade> lst = findAll(xpathAllItem);

		action = new Actions(getDriver());
		action.clickAndHold(lst.get(indexFrom)).moveToElement(lst.get(indexTo)).build().perform();
	}

	// ClickAndHold (click và giữ: chọn nhiều item không liên tiếp)
	/**
	 * @param xpathAllItem : xpath chứa toàn bộ element muốn chọn
	 * @param index        : Array các index của các element muốn chọn trong list element xpath là xpathAllItem
	 * @param values       : tham số dynamic trong String xpathAllItem (có thể có hoặc không)
	 */
	public void clickAndHoldMouseElement(String xpathAllItem, int[] index, String... values) {
		// Chọn nhiều item trong cùng 1 list (VD: chọn từ 1-> 3->5->7)
		xpathAllItem = String.format(xpathAllItem, (Object[]) values);
		List<WebElementFacade> lst = findAll(xpathAllItem);

		action = new Actions(getDriver());

		if (osName.toLowerCase().contains(platWin)) {
			action.keyDown(Keys.CONTROL).perform();
		} else if (osName.toLowerCase().contains(platMac)) {
			action.keyDown(Keys.META).perform();
		} else {
			System.out.println("not found platform click clickAndHoldMouseElement");
		}

		for (int x : index) {
			lst.get(x).click();
		}
	}

	/*
	 * ==== Keyboard: https://artoftesting.com/press-enter-tab-space-arrow-function-keys-in- selenium-webdriver-with-java====
	 */
	// Nhả phím
	/**
	 * @param key : key enum's value (VD:key=Keys.CONTROL)
	 */
	public void keyPressUp(Keys key) {
		// key=Keys.CONTROL
		action = new Actions(getDriver());
		action.keyUp(key).perform();
	}

	// Nhấn phím
	/**
	 * @param key : key enum's value (VD:key=Keys.CONTROL)
	 */
	public void keyPressDown(Keys key) {
		// key=Keys.CONTROL
		action = new Actions(getDriver());
		action.keyDown(key).perform();
	}

	// Sendkey Board
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param key    : key enum's value (VD:key=Keys.CONTROL) muốn senkey vào element
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void sendKeyBoardToElement(String xpath, Keys key, String... values) {
		// key=Keys.CONTROL
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element = element(xpath);

		action = new Actions(getDriver());
		action.sendKeys(element, key).perform();
		// element(xpath).sendKeys(key);
	}

	/*
	 * ========================================================================== WEB ==========================================================================
	 */
	/* ==== Window/Tab ==== */
	// switch tới Window/Tab theo ID (chỉ dùng cho n=2 tab)
	public void switchToWindowByID() {
		String parentID = getDriver().getWindowHandle();

		Set<String> allWindows = getDriver().getWindowHandles();
		for (String runWindow : allWindows) {
			if (!runWindow.equals(parentID)) {// nếu ID nào # id parent thì switch qua
				getDriver().switchTo().window(runWindow);
				break;
			}
		}
	}

	// switch tới Window/Tab by String (chỉ dùng cho n>2 tab)
	/**
	 * @param titleExpect : title của Window/Tab con muốn switch
	 */
	public void switchToWindowByString(String titleExpect) {
		Set<String> allWindows = getDriver().getWindowHandles();
		for (String runWindow : allWindows) {
			getDriver().switchTo().window(runWindow);
			String titleCurent = getDriver().getTitle();
			if (titleCurent.equals(titleExpect)) {
				break;
			}
		}
	}

	// kiểm tra đã đóng tất cả các tab trừ parent, và trở về tab parent
	public void closeWindownWithoutParent() {
		String parentID = getDriver().getWindowHandle();

		Set<String> allWindows = getDriver().getWindowHandles();
		for (String runWindow : allWindows) {
			if (!runWindow.equals(parentID)) {
				getDriver().switchTo().window(runWindow);
				getDriver().close();
			}
		}

		getDriver().switchTo().window(parentID);
//		if (getDriver().getWindowHandles().size() == 1) {
//			return true;
//		} else {
//			return false;
//		}
	}

	// trả về title của page
	public String getTitlePage() {
		return getTitle();
	}

	// trả về size(width,height) của page
	public Dimension getWindowSize() {
		return getDriver().manage().window().getSize();
	}

	// set size(height,width) cho page
	/**
	 * @param width  : chiều rộng muốn set
	 * @param height : chiều cao muốn set
	 */
	public void setWindowSize(int width, int height) {
		getDriver().manage().window().setSize(new Dimension(width, height));
	}

	// trả về toạ độ (x,y) của page
	/**
	 * @return Point
	 */
	public Point getWindownPosition() {
		return getDriver().manage().window().getPosition();
	}

	// set toạ độ (x,y) của page
	/**
	 * @param x : toạ độ x muốn set
	 * @param y : toạ độ y muốn set
	 */
	public void setWindownPosition(int x, int y) {
		getDriver().manage().window().setPosition(new Point(x, y));
	}

	// set toạ độ (x,y) của page
	public void getMaximizeWindow() {
		getDriver().manage().window().maximize();
	}

	/*
	 * ==== Popup/Dialog chỉ xuất hiện lần đầu khi mở app hoặc có thể xuất hiện hoặc không xuất hiện ====
	 */
	// đóng popup/dialog nếu xuất hiện
	/**
	 * @param milisecond     : thời gian (mili giây) tối đa chờ popup xuất hiện
	 * @param xpathIconClose : xpath của button icon Close
	 * @param values         : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void closePopupIfDisplayed(long milisecond, String xpathIconClose, String... values) {
		long startime = System.currentTimeMillis();
		long duration = 0;

		while (milisecond > duration) {
			List<WebElementFacade> Lst = getListElement(xpathIconClose, values);
			if (Lst.size() > 0) {
				clickToElement(xpathIconClose, values);
				break;
			}
			long endTime = System.currentTimeMillis();
			duration = endTime - startime;
		}
	}

	// kiểm tra popup có xuất hiện không ?
	/**
	 * @param xpath  : xpath của popup
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public boolean isPopupDisplayed(String xpath, String... values) {
		boolean a = false;
		List<WebElementFacade> lst = getListElement(xpath, values);
		if (lst.size() > 0) {
			a = true;
		}
		return a;
	}

	/* ==== Navigation ===== */
	// mở URL được cấu hình ở file serenity.properties
	public void openPage() {
		open();
	}

	// trả về url của page đang mở
	public String getCurrentPageURL() {
		return getDriver().getCurrentUrl();
	}

	// back lại page trước đó
	public void backToPreviousPage() {
		getDriver().navigate().back();
	}

	// next page đã đã mở
	public void fowardToNextPage() {
		getDriver().navigate().forward();
	}

	// refresh lại page đang mở
	public void refreshCurrentPage() {
		getDriver().navigate().refresh();
	}

	/* ==== Storage ==== */
	// trả về danh sách cookie: name - value
	public Set<Cookie> getAllCookies() {
		Set<Cookie> set = getDriver().manage().getCookies();
//		for (Cookie key : set) {
//			System.out.println(key);
//		}
		return set;
	}

	// set cookie
	/**
	 * @param cookieName  : tên cookie cần set
	 * @param cookievalue : giá trị cần set cho String cookieName
	 */
	public void setCookie(String cookieName, String cookievalue) {
		getDriver().manage().addCookie(new Cookie(cookieName, cookievalue));
	}

	// delete cookie
	/**
	 * @param cookieName : tên cookie muốn xoá
	 */
	public void deleteCookie(String cookieName) {
		getDriver().manage().deleteCookieNamed(cookieName);
	}

	// delte all cookies
	public void deleteAllCookie() {
		getDriver().manage().deleteAllCookies();
	}

	/* ===== Iframe/Frame ===== */
	// switch to frame
	/**
	 * @param xpath  : xpath frame/iframe cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void switchToFrame(String xpath, String... values) {
		highlightElementJS(xpath, values);
		xpath = String.format(xpath, (Object[]) values);
		element = element(xpath);
		getDriver().switchTo().frame(element);
	}

	// switch to top windows
	public void backToTopParentFrame() {
		getDriver().switchTo().defaultContent();
		// getDriver().switchTo().parentFrame();
	}


	/* ===== QR code ===== */
	public String decodeQR (String xpath, String values) throws NotFoundException, IOException {
		Result result = null;
		
		String qrCodeURL = getAttributeValue(xpath, "src", values);
		URL url = new URL(qrCodeURL);
		// Pass the URL class object to store the file as image
		BufferedImage qrCodeImage = ImageIO.read(url);

		// Process the image
		LuminanceSource luminanceSource = new BufferedImageLuminanceSource(qrCodeImage);
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
		// To Capture details of QR code
		result = new MultiFormatReader().decode(binaryBitmap);
		System.out.println(result.getText());
		return result.getText();
	}
	
	
	/* ========================================================================== JAVASCRIPT ========================================================================== */
	//html5: get validation message
	public String getHtml5ValidationMessage(String xpath, String ...values) {
		xpath = String.format(xpath, (Object[]) values);
		JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
		
		element = element(xpath);
		return (String) jsExecutor.executeScript("return arguments[0].validationMessage;", element);
	}
	// Refresh Browser
	public Object refreshPageJS() {
		javascriptExecutor = (JavascriptExecutor) getDriver();
		return javascriptExecutor.executeScript("history.go(0)");
	}

	// Get Title Browser
	public String getTitlePageJS() {
		javascriptExecutor = (JavascriptExecutor) getDriver();
		return (String) javascriptExecutor.executeScript("return document.title");
	}

	// Get Url Browser
	public String getURLPageJS() {
		javascriptExecutor = (JavascriptExecutor) getDriver();
		return (String) javascriptExecutor.executeScript("return document.URL");
	}

	// Get Domain
	public String getDomainJS() {
		javascriptExecutor = (JavascriptExecutor) getDriver();
		return (String) javascriptExecutor.executeScript("return document.domain");
	}

	// Click Element
	/**
	 * @param xpath  : xpath element cần lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */

	public Object clicktoElementJS(String xpath, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		javascriptExecutor = (JavascriptExecutor) getDriver();

		element = element(xpath);
		return javascriptExecutor.executeScript("arguments[0].click();", element);
	}

	// Senkey element
	/**
	 * @param xpath          : xpath element cần lấy
	 * @param valueToSendkey : giá trị muốn senkey vào element
	 * @param values         : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public Object sendkeyToElementJS(String xpath, String valueToSenKey, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		highlightElementJS(xpath);

		javascriptExecutor = (JavascriptExecutor) getDriver();
		element = element(xpath);
		return javascriptExecutor.executeScript("arguments[0].setAttribute('value', '" + valueToSenKey + "')", element);
	}

	// getInnerText
	public String getInnerTextJS() {
		javascriptExecutor = (JavascriptExecutor) getDriver();
		return javascriptExecutor.executeScript("return document.documentElement.innerText;").toString();
	}

	// Scoll to Pixel
	/**
	 * @param pixel : pixel muốn scroll tới
	 */
	public Object scrollToPixelJS(float pixel) {
		javascriptExecutor = (JavascriptExecutor) getDriver();
		return javascriptExecutor.executeScript("window.scrollBy(0," + pixel + ")");
	}

	// Scoll to bottom Page
	public Object scrollToBottomPageJS() {
		javascriptExecutor = (JavascriptExecutor) getDriver();
		return javascriptExecutor.executeScript("window.scrollBy(0, document.body.scrollHeight)");
	}

	// Scoll to Element
	/**
	 * @param xpath  : xpath element muốn scroll tới
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public Object scrollToElementJS(String xpath, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		javascriptExecutor = (JavascriptExecutor) getDriver();
		element = element(xpath);
		return javascriptExecutor.executeScript("arguments[0]. scrollIntoView(true);", element);
	}

	public Object scrollToElementJS1(WebElement element) {
		javascriptExecutor = (JavascriptExecutor) getDriver();
		return javascriptExecutor.executeScript("arguments[0]. scrollIntoView(true);", element);
	}

	// Remove attribute
	/**
	 * @param xpath     : xpath element muốn lấy
	 * @param attribute : tên attribute của element muốn remove
	 * @param values    : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public Object removeAttributeJS(String xpath, String attribute, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		javascriptExecutor = (JavascriptExecutor) getDriver();
		element = element(xpath);
		return javascriptExecutor.executeScript("arguments[0].removeAttribute('" + attribute + "');", element);
	}

	// Set attribute
	/**
	 * @param xpath     : xpath element muốn lấy
	 * @param attribute : tên attribute của element muốn set
	 * @param values    : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public Object setAttributeJS(String xpath, String attribute, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		javascriptExecutor = (JavascriptExecutor) getDriver();
		element = element(xpath);
		return javascriptExecutor.executeScript("arguments[0].setAttribute('" + attribute + "');", element);
	}

	// Highlight
	/**
	 * @param xpath  : xpath element muốn lấy
	 * @param values : tham số dynamic trong String xpath (có thể có hoặc không)
	 */
	public void highlightElementJS(String xpath, String... values) {
		xpath = String.format(xpath, (Object[]) values);
		javascriptExecutor = (JavascriptExecutor) getDriver();
		element = element(xpath).waitUntilVisible();
		String originalStyle = element.getAttribute("style");
		javascriptExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 5px solid red; border-style: dashed;");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		javascriptExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
	}

	public void highlightElement1(WebElement element) {
		javascriptExecutor = (JavascriptExecutor) getDriver();

		String originalStyle = element.getAttribute("style");
		javascriptExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 5px solid red; border-style: dashed;");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		javascriptExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
	}

	/*
	 * ========================================================================== .PROPERTIES ==========================================================================
	 */

	// get giá trị của từng thông số cấu hình file serenity.properties
	/**
	 * @param property : tên thông số cấu hình (VD: appium.platformName/platformVersion.....)
	 */
	public static String getProperties(String property) throws IOException {
		String path = null;
		String fileName = "serenity.properties";
		if (osName.toLowerCase().contains(platWin)) {
			path = String.format(workingDr + "\\%s", fileName);

		} else if (osName.toLowerCase().contains(platMac)) {
			path = String.format(workingDr + "/%s", fileName);

		} else {
			System.out.println("not found osName: get properties");
		}

		FileInputStream a = new FileInputStream(path);

		Properties p = new Properties();
		p.load(a);
		return p.getProperty(property);
	}

	/* ======================= Dự án ======================== */
	public void dynamciChooseValueAuto(String xpathAuto, String xpathResultValue, String... valueSenkey) {
		highlightElementJS(xpathAuto);
		element(xpathAuto).sendKeys(valueSenkey);

		highlightElementJS(xpathResultValue, valueSenkey);
		element(String.format(xpathResultValue, (Object[]) valueSenkey)).waitUntilVisible().click();
	}

	public int getRowResulSearchUi(String xpathRecord) {
		int recordNumberInt = 0;

		List<WebElementFacade> lstRecord = findAll(xpathRecord);
		int sizeRecord = lstRecord.size();

		if (sizeRecord > 0) {
			highlightElementJS(xpathRecord);
			String recordResultString = element(xpathRecord).getText();
			String[] recordResultStringArray = recordResultString.split("\\s");

			String recordNumberString = recordResultStringArray[2];

			// String recordNumberStringRep = recordNumberString.replaceAll(",", "");
			recordNumberInt = Integer.parseInt(recordNumberString);
		}

		System.out.println("row UI =" + recordNumberInt);
		return recordNumberInt;
	}

}
