/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.wso2.carbon.greg.store.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.automation.extensions.selenium.BrowserManager;
import org.wso2.carbon.greg.store.StoreHomePage;
import org.wso2.carbon.greg.store.StoreLoginPage;
import org.wso2.carbon.greg.store.exceptions.StoreTestException;
import org.wso2.greg.integration.common.ui.page.LoginPage;
import org.wso2.greg.integration.common.ui.page.lifecycle.LifeCycleHomePage;
import org.wso2.greg.integration.common.ui.page.util.UIElementMapper;
import org.wso2.greg.integration.common.utils.GREGIntegrationUIBaseTest;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.testng.AssertJUnit.assertEquals;

public class REGISTRY3905_Testcase extends GREGIntegrationUIBaseTest {

    private WebDriver driver;
    private String baseUrl;
    private UIElementMapper uiElementMapper;
    private StoreHomePage storeHomePage;

    @BeforeClass(alwaysRun = true, description = "Basic setup and moving to store")
    public void setUp() throws Exception {
        super.init();
        driver = BrowserManager.getWebDriver();
        driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
        baseUrl = getStoreBaseUrl();
        uiElementMapper = UIElementMapper.getInstance();
    }

    @Test(description = "This method tests login functionality of the Store home page")
    public void testLoginStore() throws StoreTestException, XPathExpressionException, IOException {
        driver.get(baseUrl);
        storeHomePage = new StoreHomePage(driver);

        StoreLoginPage storeLoginPage = storeHomePage.moveToRegister();
        storeLoginPage.Register("sample", "sample", "sample@wso2.com", "FName", "LName");

        storeLoginPage = storeHomePage.moveToLoginPage();
        storeLoginPage.Login("sample", "sample");

        WebElement signedInUser = driver
                .findElement(By.id(uiElementMapper.getElement("store.homepage.loggedinuser.id")));
        assertEquals("Signed is user is different from context username", signedInUser.getText().trim(),
                     "sample");
        log.info("Successfully logged in");

        driver.get(getLoginURL());
        LoginPage test = new LoginPage(driver);
        test.loginAs("sample","sample");

        driver.get(getLoginURL() + "admin/index.jsp?loginStatus=true");

        driver.findElement(By.linkText("Sign-out")).click();
    }

}
