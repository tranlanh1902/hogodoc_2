package elpisclothing.StepsDefinitions;

import cucumber.api.java.en.*;
import elpisclothing.pageObjects.Login_PageObject;
import io.cucumber.datatable.DataTable;

import net.thucydides.core.annotations.Steps;

@SuppressWarnings({ "deprecation" })
public class Login_PageDefinitions {
	@Steps
	Login_PageObject login;

	
	///
	@Given("^LoginPage: nhập thông tin đăng nhập$")
	public void dangnhappage_nhp_thng_tin_ng_nhp_thnh_cng(DataTable table) {
		String emailSenkey = login.getValueColumnDataTableBDD(table, "email");
		String passSenkey = login.getValueColumnDataTableBDD(table, "matKhau");

		if (emailSenkey.trim().length() > 0) {
			login.inputToEmailTxt(emailSenkey);
		}

		if (passSenkey.trim().length() > 0) {
			login.inputToPassTxt(passSenkey);
		}
	}

	///
	@And("^LoginPage: nhấn button Đăng nhập$")
	public void dangnhappage_nhn_button_ng_nhp() {
		login.clickToDanNhapBtn();
	}

}
