package elpisclothing.StepsDefinitions;

import cucumber.api.java.en.*;
import elpisclothing.pageObjects.Home_PageObject;
import net.thucydides.core.annotations.Steps;

@SuppressWarnings({ "deprecation" })
public class Home_PageDefinitions {
	@Steps
	Home_PageObject home;

	@Given("^HomePage: mở trang$")
	public void m_trang_httpselpisclothingvn() {
		home.openPageEpisclothing();
	}

}

