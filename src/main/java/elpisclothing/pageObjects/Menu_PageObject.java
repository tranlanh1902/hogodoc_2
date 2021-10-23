package elpisclothing.pageObjects;

import common.abc.CommonWeb_Y;
import elpisclothing.pageUIs.Menu_PageUI;

public class Menu_PageObject extends CommonWeb_Y {

	public void clickToLoginHyperlink() {
		clickToElement(Menu_PageUI.hyperlink_LOGIN);
	}


}
