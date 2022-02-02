/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2009, 2011, 2013, 2014, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.cs.versioncheck;

import com.zimbra.common.service.ServiceException;
import com.zimbra.common.util.ZimbraLog;
import com.zimbra.cs.extension.ZimbraExtension;
import com.zimbra.cs.service.versioncheck.VersionCheckService;
import com.zimbra.qa.unittest.TestVersionCheck;
import com.zimbra.qa.unittest.ZimbraSuite;
import com.zimbra.soap.SoapServlet;

/**
 * @author Greg Solovyev
 */
public class VersionCheckExtension implements ZimbraExtension {
    public static final String EXTENSION_NAME_VERSIONCHECK = "versioncheck";

    @Override
    public void init() throws ServiceException {
        SoapServlet.addService("AdminServlet", new VersionCheckService());
        try {
            ZimbraSuite.addTest(TestVersionCheck.class);
        } catch (NoClassDefFoundError e) {
            // Expected in production, because JUnit is not available.
            ZimbraLog.test.debug("Unable to load ZimbraAdminVersionCheck unit tests.", e);
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public String getName() {
        return EXTENSION_NAME_VERSIONCHECK;
    }

}
