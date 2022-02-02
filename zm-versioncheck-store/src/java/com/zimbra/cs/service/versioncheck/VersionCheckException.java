/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2009, 2012, 2013, 2014, 2016 Synacor, Inc.
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

import com.zimbra.common.service.ServiceException;


public class VersionCheckException extends ServiceException {
	public static final String INVALID_VC_RESPONSE     = "versioncheck.INVALID_VC_RESPONSE";
	public static final String FAILED_TO_GET_RESPONSE     = "versioncheck.FAILED_TO_GET_RESPONSE";
	public static final String EMPTY_VC_RESPONSE = "versioncheck.EMPTY_VC_RESPONSE";
   
	private VersionCheckException(String message, String code, boolean isReceiversFault, Throwable cause) {
        super(message, code, isReceiversFault, cause);
    }

    public static VersionCheckException INVALID_VC_RESPONSE(String msg, Throwable cause) {
        return new VersionCheckException("Received invalid response from the update script: " + msg, INVALID_VC_RESPONSE, SENDERS_FAULT, cause);
    }	
    
    public static VersionCheckException FAILED_TO_GET_RESPONSE(String msg, Throwable cause) {
        return new VersionCheckException("Failed to get version updates from " + msg, FAILED_TO_GET_RESPONSE, SENDERS_FAULT, cause);
    }
    
    public static VersionCheckException EMPTY_VC_RESPONSE(Throwable cause) {
        return new VersionCheckException("Response from update script is empty", EMPTY_VC_RESPONSE, SENDERS_FAULT, cause);
    }
}
