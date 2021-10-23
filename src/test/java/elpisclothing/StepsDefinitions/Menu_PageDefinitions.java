package elpisclothing.StepsDefinitions;
import cucumber.api.java.en.*;
import elpisclothing.pageObjects.Menu_PageObject;
import net.thucydides.core.annotations.Steps;

@SuppressWarnings({ "deprecation" })
public class Menu_PageDefinitions {
	@Steps
	Menu_PageObject menu;

	////
	@When("^Menu: nhấn hyperlink Tài khoản$")
	public void nhn_hyperlink_ng_nhp() {
		menu.clickToLoginHyperlink();
	}

}
