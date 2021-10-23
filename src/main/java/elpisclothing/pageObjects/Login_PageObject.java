package elpisclothing.pageObjects;

import common.abc.CommonWeb_Y;
import elpisclothing.pageUIs.Login_PageUI;

public class Login_PageObject extends CommonWeb_Y {


	public void inputToEmailTxt(String emailSenkey) {
		sendkeyToElement(Login_PageUI.txt_EMAIL, emailSenkey);
	}

	public void inputToPassTxt(String passSenkey) {
		sendkeyToElement(Login_PageUI.txt_PASSWORD, passSenkey);

	}

	public void clickToDanNhapBtn() {

		clickToElement(Login_PageUI.btn_LOGIN);

	}

}
