/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.appserver.integration.tests.jaggery;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.wso2.appserver.integration.common.utils.WebAppTypes;
import org.wso2.appserver.integration.tests.jaggery.utils.JaggeryTestUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.appserver.integration.common.utils.ASIntegrationTest;
import org.wso2.carbon.automation.engine.context.TestUserMode;

import java.io.BufferedReader;
import java.net.URL;
import java.net.URLConnection;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * This class sends requests to response.jag and validates the response
 */
public class ResponseObjectTestCase extends ASIntegrationTest {

    private static final Log log = LogFactory.getLog(ResponseObjectTestCase.class);
    private TestUserMode userMode;

    @BeforeClass(alwaysRun = true)
    public void init() throws Exception {
        super.init(userMode);
    }

    @Factory(dataProvider = "userModeProvider")
    public ResponseObjectTestCase(TestUserMode userMode) {
        this.userMode = userMode;
    }

    @DataProvider
    private static TestUserMode[][] userModeProvider() {
        return new TestUserMode[][]{
                new TestUserMode[]{TestUserMode.SUPER_TENANT_ADMIN},
                new TestUserMode[]{TestUserMode.TENANT_USER},
        };
    }

    @Test(groups = {"wso2.as"}, description = "Test response object")
    public void testResponse() throws Exception {

        String response = null;
        URL jaggeryURL = new URL(getWebAppURL(WebAppTypes.JAGGERY) + "/testapp/response.jag");
        URLConnection jaggeryServerConnection = JaggeryTestUtil.openConnection(jaggeryURL);
        assertNotNull(jaggeryServerConnection, "Connection establishment failure");

        BufferedReader in = JaggeryTestUtil.inputReader(jaggeryServerConnection);
        assertNotNull(in, "Input stream failure");

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response = inputLine;
        }

        in.close();
        log.info("Response: " + response);
        assertNotNull(response, "Result cannot be null");
    }

    @Test(groups = {"wso2.as"}, description = "Test response object member",
            dependsOnMethods = "testResponse")
    public void testReadResponse() throws Exception {

        String response = null;
        URL jaggeryURL = new URL(webAppURL + "/testapp/response.jag?action=member");
        URLConnection jaggeryServerConnection = JaggeryTestUtil.openConnection(jaggeryURL);
        assertNotNull(jaggeryServerConnection, "Connection establishment failure");

        BufferedReader in = JaggeryTestUtil.inputReader(jaggeryServerConnection);
        assertNotNull(in, "Input stream failure");

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response = inputLine;
        }

        in.close();
        log.info("Response: " + response);
        assertEquals(response, "My response content, status : 0, contentType : text/html");
    }

    @Test(groups = {"wso2.as"}, description = "Test response object operation",
            dependsOnMethods = "testReadResponse")
    public void testReadResponseOperation() throws Exception {

        String response = null;
        URL jaggeryURL = new URL(webAppURL + "/testapp/response.jag?action=operation");
        URLConnection jaggeryServerConnection = JaggeryTestUtil.openConnection(jaggeryURL);
        assertNotNull(jaggeryServerConnection, "Connection establishment failure");

        BufferedReader in = JaggeryTestUtil.inputReader(jaggeryServerConnection);
        assertNotNull(in, "Input stream failure");

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response = inputLine;
        }

        in.close();
        log.info("Response: " + response);
        assertEquals(response, null);
    }

    @Test(groups = {"wso2.as"}, description = "Test response object redirect operation",
            dependsOnMethods = "testReadResponseOperation")
    public void testReadResponseRedirect() throws Exception {

        String response = null;
        URL jaggeryURL = new URL(webAppURL + "/testapp/response.jag?action=redirect");
        URLConnection jaggeryServerConnection = JaggeryTestUtil.openConnection(jaggeryURL);
        assertNotNull(jaggeryServerConnection, "Connection establishment failure");

        BufferedReader in = JaggeryTestUtil.inputReader(jaggeryServerConnection);
        assertNotNull(in, "Input stream failure");

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response = inputLine;
        }

        in.close();
        log.info("Response: " + response);
        assertEquals(response, "0");
    }
}
