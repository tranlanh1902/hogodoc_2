@login 
Feature: 01_Đăng nhập 
Background: 
	Given HomePage: mở trang 
	And Menu: nhấn hyperlink Tài khoản 
	
@sucessLogin 
Scenario: 01_Đăng nhập thành công 
	Given LoginPage: nhập thông tin đăng nhập 
		| case                         | email                   | matKhau       | 
		| Nhập [Email],[Mật khẩu] đúng | tranlanh1902@gmail.com | 123456 | 
	When LoginPage: nhấn button Đăng nhập 