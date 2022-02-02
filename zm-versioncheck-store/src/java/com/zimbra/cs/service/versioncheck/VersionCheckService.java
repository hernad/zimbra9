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
package com.zimbra.cs.service.versioncheck;

import com.zimbra.soap.DocumentDispatcher;
import com.zimbra.soap.DocumentService;

import com.zimbra.common.soap.AdminConstants;
/**
 * @author Greg Solovyev
 */
public class VersionCheckService implements DocumentService {
    public void registerHandlers(DocumentDispatcher dispatcher) {
        dispatcher.registerHandler(AdminConstants.VC_REQUEST, new VersionCheck());
    }
}
