package com.zimbra.qa.unittest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.zimbra.common.httpclient.HttpClientUtil;
import com.zimbra.common.service.ServiceException;
import com.zimbra.common.soap.AdminConstants;
import com.zimbra.common.util.ZimbraHttpConnectionManager;
import com.zimbra.common.util.ZimbraLog;
import com.zimbra.cs.account.Account;
import com.zimbra.cs.account.AuthToken;
import com.zimbra.cs.account.Provisioning;
import com.zimbra.cs.account.SearchDirectoryOptions;
import com.zimbra.cs.account.SearchDirectoryOptions.ObjectType;
import com.zimbra.cs.account.ZimbraAuthToken;
import com.zimbra.cs.account.accesscontrol.generated.RightConsts;
import com.zimbra.cs.account.soap.SoapProvisioning;
import com.zimbra.cs.service.AuthProvider;
import com.zimbra.soap.admin.message.CreateAccountRequest;
import com.zimbra.soap.admin.message.CreateAccountResponse;
import com.zimbra.soap.admin.message.GrantRightRequest;
import com.zimbra.soap.admin.message.GrantRightResponse;
import com.zimbra.soap.admin.type.Attr;
import com.zimbra.soap.admin.type.CacheEntryType;
import com.zimbra.soap.admin.type.EffectiveRightsTargetSelector;
import com.zimbra.soap.admin.type.GranteeSelector;
import com.zimbra.soap.admin.type.GranteeSelector.GranteeBy;
import com.zimbra.soap.admin.type.RightModifierInfo;
import com.zimbra.soap.type.GranteeType;
import com.zimbra.soap.type.TargetBy;
import com.zimbra.soap.type.TargetType;

import junit.framework.TestCase;

public class TestSearchResultsDownload extends TestCase {
    private static final String USER_PREFIX = TestSearchResultsDownload.class.getSimpleName().toLowerCase() + "_";
    private static final int NUM_ACCOUNTS = 10;

    // Reference: com.zimbra.qa.unittest.TestDomainAdmin
    private final static String ADMINISTRATOR_DOMAIN = "testadmin.domain";
    private final static String DOMADMIN = "domadmin@" + ADMINISTRATOR_DOMAIN;

    TestDomainAdmin testDomainAdmin;
    @Override
    public void setUp() throws Exception {
        cleanup();
        TestJaxbProvisioning.ensureDomainExists(ADMINISTRATOR_DOMAIN);
        createAdminConsoleStyleDomainAdmin(DOMADMIN);
        for(int i = 0; i < NUM_ACCOUNTS; i++) {
            TestUtil.createAccount(USER_PREFIX + i);
            TestUtil.createAccount(USER_PREFIX + i + "@" + ADMINISTRATOR_DOMAIN);
        }
    }

    @Override
    public void tearDown() throws Exception {
        cleanup();
    }

    private void cleanup() throws Exception {
        for(int i = 0; i < NUM_ACCOUNTS; i++) {
            if(TestUtil.accountExists(USER_PREFIX + i)) {
                TestUtil.deleteAccount(USER_PREFIX + i);
                TestUtil.deleteAccount(USER_PREFIX + i + "@" + ADMINISTRATOR_DOMAIN);
            }
        }
        TestUtil.deleteAccount(DOMADMIN);
        TestJaxbProvisioning.deleteDomainIfExists(ADMINISTRATOR_DOMAIN);
    }

    @Test
    public void testMissingTypes() throws Exception {
        AuthToken at = AuthProvider.getAdminAuthToken();
        at.setCsrfTokenEnabled(true);
        int port = 7071;
        try {
            port = Provisioning.getInstance().getLocalServer().getIntAttr(Provisioning.A_zimbraAdminPort, 0);
        } catch (ServiceException e) {
            ZimbraLog.test.error("Unable to get admin SOAP port", e);
        }
        String host =  Provisioning.getInstance().getLocalServer().getName();
        String searchDownloadURL = "https://" + host + ":" + port + "/service/extension/com_zimbra_bulkprovision/search_results_download";
        HttpClientBuilder eve = ZimbraHttpConnectionManager.getInternalHttpConnMgr().newHttpClient();
        BasicCookieStore state = new BasicCookieStore();
        eve.setDefaultCookieStore(state);
        at.encode(state, true, host);
        HttpGet get = new HttpGet(searchDownloadURL);
        int statusCode = HttpClientUtil.executeMethod(eve.build(), get).getStatusLine().getStatusCode();
        assertEquals("Should be getting status code 400. Getting status code " + statusCode, HttpStatus.SC_BAD_REQUEST,statusCode);
    }

    @Test
    public void testDownloadAllTypes() throws Exception {
        int count = downloadAllTypes(true);
        assertTrue("Result set should have more than " + (NUM_ACCOUNTS * 2) + " entries", count > NUM_ACCOUNTS * 2);
    }

    @Test
    public void testDownloadAccounts() throws Exception {
        int count = downloadAccounts(true);
        assertTrue("Result set should have more than " + (NUM_ACCOUNTS * 2) + " entries", count > NUM_ACCOUNTS * 2);
    }

    @Test
    public void testDownloadByQuery() throws Exception {
        List<String> expectedResults = new ArrayList<String>();
        StringBuffer sb = new StringBuffer();
        generateQuery(true, sb, expectedResults);
        int count = downloadByQuery(true, sb, expectedResults);
        assertEquals("Result set should have " + (NUM_ACCOUNTS * 2) + " entries", NUM_ACCOUNTS * 2, count);
    }

    @Test
    public void testDelegatedAdminDownloadAllTypes() throws Exception {
        int count = downloadAllTypes(false);
        assertEquals("Result set should have entries of " + NUM_ACCOUNTS + " accounts, a delegated admin and a domain", NUM_ACCOUNTS + 2, count);
    }

    @Test
    public void testDelegatedAdminDownloadAccounts() throws Exception {
        int count = downloadAccounts(false);
        assertEquals("Result set should have entries of " + NUM_ACCOUNTS + " accounts and a delegated admin", NUM_ACCOUNTS + 1, count);
    }

    @Test
    public void testDelegatedAdminDownloadByQuery() throws Exception {
        List<String> expectedResults = new ArrayList<String>();
        StringBuffer sb = new StringBuffer();
        generateQuery(false, sb, expectedResults);
        int count = downloadByQuery(false, sb, expectedResults);
        assertEquals("Result set should have " + NUM_ACCOUNTS + " entries", NUM_ACCOUNTS, count);
    }

    private int downloadAllTypes(boolean isGlobalAdmin) throws Exception {
        return downloadCSV(new HashSet<ObjectType>(Arrays.asList(ObjectType.values())),
                new HashSet<String>(Arrays.asList(AdminConstants.E_CALENDAR_RESOURCE,
                        AdminConstants.E_ACCOUNT,
                        AdminConstants.E_DL,
                        AdminConstants.E_ALIAS,
                        AdminConstants.E_COS,
                        AdminConstants.E_DOMAIN
                        )), null, null, isGlobalAdmin);
    }

    private int downloadAccounts(boolean isGlobalAdmin) throws Exception {
        return downloadCSV(new HashSet<ObjectType>(Arrays.asList(ObjectType.accounts)),
                new HashSet<String>(Arrays.asList(AdminConstants.E_ACCOUNT)), null, null, isGlobalAdmin);
    }

    private int downloadByQuery(boolean isGlobalAdmin, StringBuffer sb, List<String> expectedResults) throws Exception {
        return downloadCSV(new HashSet<ObjectType>(Arrays.asList(ObjectType.accounts)),
                new HashSet<String>(Arrays.asList(AdminConstants.E_ACCOUNT,
                        AdminConstants.E_DL,
                        AdminConstants.E_ALIAS)), URLEncoder.encode(sb.toString(), "UTF-8"), expectedResults, isGlobalAdmin);
    }

    private void generateQuery(boolean isGlobalAdmin, StringBuffer sb, List<String> expectedResults) throws Exception {
        sb.append("(|");
        for (int i = 0; i < NUM_ACCOUNTS; i++) {
            sb.append("(mail=");
            sb.append(USER_PREFIX + i);
            sb.append("@");
            sb.append(TestUtil.getDomain());
            sb.append(")");
            if (isGlobalAdmin) {
                expectedResults.add(USER_PREFIX + i + "@" + TestUtil.getDomain());
            }
        }
        for (int i = 0; i < NUM_ACCOUNTS; i++) {
            sb.append("(mail=");
            sb.append(USER_PREFIX + i);
            sb.append("@");
            sb.append(ADMINISTRATOR_DOMAIN);
            sb.append(")");
            expectedResults.add(USER_PREFIX + i + "@" + ADMINISTRATOR_DOMAIN);
        }
        sb.append(")");
    }

    private int downloadCSV(Set<SearchDirectoryOptions.ObjectType> types, Set<String> typeIDs, String query, List<String> expectedResults, boolean isGlobalAdmin) throws Exception {
        AuthToken at;
        if (isGlobalAdmin) {
            at = AuthProvider.getAdminAuthToken();
        } else {
            SoapProvisioning sp = SoapProvisioning.getAdminInstance(true);
            sp.soapZimbraAdminAuthenticate();
            Account acct = sp.getAccount(DOMADMIN, true);
            at = new ZimbraAuthToken(acct, true, null);
        }
        at.setCsrfTokenEnabled(true);
        int port = 7071;
        try {
            port = Provisioning.getInstance().getLocalServer().getIntAttr(Provisioning.A_zimbraAdminPort, 0);
        } catch (ServiceException e) {
            ZimbraLog.test.error("Unable to get admin SOAP port", e);
        }
        String host =  Provisioning.getInstance().getLocalServer().getName();
        String searchDownloadURL = "https://" + host + ":" + port +
            "/service/extension/com_zimbra_bulkprovision/search_results_download?types=" + ObjectType.toCSVString(types);
        if(query != null) {
            searchDownloadURL += "&q=" + query;
        }
        HttpClientBuilder eve = ZimbraHttpConnectionManager.getInternalHttpConnMgr().newHttpClient();
        BasicCookieStore state = new BasicCookieStore();
        eve.setDefaultCookieStore(state);
        at.encode(state, true, host);
        HttpGet get = new HttpGet(searchDownloadURL);
        HttpResponse response = HttpClientUtil.executeMethod(eve.build(), get);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals("The GET request should succeed. Getting status code " + statusCode, HttpStatus.SC_OK,statusCode);
        CSVParser parser = null;
        InputStream in = null;
        int recordCount;
        try {
            in = response.getEntity().getContent();
            parser = new CSVParser(new InputStreamReader(in), CSVFormat.DEFAULT);
            List<CSVRecord> records = parser.getRecords();
            recordCount = records.size();
            assertNotNull("Result set should not be NULL", records);
            assertFalse("Result set should not be empty", records.isEmpty());
            int numFound = 0;
            assertTrue("Result set should have more than one entry", records.size() > 1);
            for(CSVRecord record : records) {
                assertTrue("each record should have at least 3 fields", record.size() > 2);
                String name = record.get(0);
                assertNotNull("1st field should not be NULL", name);
                assertNotNull("2d field should not be NULL", record.get(1));
                String val = record.get(2);
                assertNotNull("3rd field should not be NULL", val);
                assertTrue("Record has invalid type " + val, typeIDs.contains(val));
                if(expectedResults != null) {
                    assertTrue("Unexpected record in result set: " + name, expectedResults.contains(name));
                    numFound++;
                }
            }
            if(expectedResults != null) {
                assertEquals("Unexpected number of results in record set", expectedResults.size(), numFound);
            }
        } finally  {
            try {
                if(parser != null) {
                    parser.close();
                }
                if(in != null) {
                    in.close();
                }
            } catch (IOException e) {
                //ignore
            }
        }
        return recordCount;
    }

    private String createAdminConsoleStyleDomainAdmin(String domAdminName) throws ServiceException {
        List<Attr> attrs = Lists.newArrayList();
        attrs.add(new Attr(Provisioning.A_zimbraIsDelegatedAdminAccount, "TRUE"));
        attrs.add(new Attr(Provisioning.A_zimbraAdminConsoleUIComponents, "accountListView"));
        attrs.add(new Attr(Provisioning.A_zimbraAdminConsoleUIComponents, "downloadsView"));
        attrs.add(new Attr(Provisioning.A_zimbraAdminConsoleUIComponents, "DLListView"));
        attrs.add(new Attr(Provisioning.A_zimbraAdminConsoleUIComponents, "aliasListView"));
        attrs.add(new Attr(Provisioning.A_zimbraAdminConsoleUIComponents, "resourceListView"));
        attrs.add(new Attr(Provisioning.A_zimbraAdminConsoleUIComponents, "saveSearch"));
        SoapProvisioning adminSoapProv = TestUtil.newSoapProvisioning();
        CreateAccountRequest caReq = new CreateAccountRequest(domAdminName, TestUtil.DEFAULT_PASSWORD, attrs);
        CreateAccountResponse caResp = adminSoapProv.invokeJaxb(caReq);
        assertNotNull("CreateAccountResponse for " + domAdminName, caResp);

        grantRight(adminSoapProv, TargetType.domain, ADMINISTRATOR_DOMAIN, domAdminName,
                RightConsts.RT_domainAdminConsoleRights);
        grantRight(adminSoapProv, TargetType.global, "globalacltarget", domAdminName,
                RightConsts.RT_domainAdminZimletRights);
        grantRight(adminSoapProv, TargetType.global, "globalacltarget", domAdminName,
                RightConsts.RT_adminLoginCalendarResourceAs);

        adminSoapProv.flushCache(CacheEntryType.acl, null);
        return caResp.getAccount().getId();
    }

    private void grantRight(SoapProvisioning soapProv, TargetType targetType, String targetName,
            GranteeType granteeType, String granteeName, String rightName)
    throws ServiceException {
        GranteeSelector grantee;
        EffectiveRightsTargetSelector target;
        RightModifierInfo right;
        GrantRightResponse grResp;

        grantee = new GranteeSelector(granteeType, GranteeBy.name, granteeName);
        target = new EffectiveRightsTargetSelector(targetType, TargetBy.name, targetName);
        right = new RightModifierInfo(rightName);
        grResp = soapProv.invokeJaxb(new GrantRightRequest(target, grantee, right));
        assertNotNull("GrantRightResponse for " + right.getValue(), grResp);
    }

    private void grantRight(SoapProvisioning soapProv, TargetType targetType, String targetName, String granteeName,
            String rightName)
    throws ServiceException {
        grantRight(soapProv, targetType, targetName, GranteeType.usr, granteeName, rightName);
    }
}
