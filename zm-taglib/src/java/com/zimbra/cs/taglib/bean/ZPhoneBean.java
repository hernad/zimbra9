/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2009, 2010, 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.cs.taglib.bean;

import com.zimbra.client.ZPhone;
import com.zimbra.common.service.ServiceException;

public class ZPhoneBean {

    public static final String INVALID_PHNUM_OWN_PHONE_NUMBER = ZPhone.INVALID_PHNUM_OWN_PHONE_NUMBER;
    public static final String INVALID_PHNUM_INTERNATIONAL_NUMBER = ZPhone.INVALID_PHNUM_INTERNATIONAL_NUMBER;
    public static final String INVALID_PHNUM_BAD_NPA = ZPhone.INVALID_PHNUM_BAD_NPA;
    public static final String INVALID_PHNUM_BAD_LINE = ZPhone.INVALID_PHNUM_BAD_LINE;
    public static final String INVALID_PHNUM_EMERGENCY_ASSISTANCE = ZPhone.INVALID_PHNUM_EMERGENCY_ASSISTANCE;
    public static final String INVALID_PHNUM_DIRECTORY_ASSISTANCE = ZPhone.INVALID_PHNUM_DIRECTORY_ASSISTANCE;
    public static final String INVALID_PHNUM_BAD_FORMAT = ZPhone.INVALID_PHNUM_BAD_FORMAT;
    public static final String VALID = ZPhone.VALID;

    private ZPhone mPhone;

    public ZPhoneBean(ZPhone phone) {
        mPhone = phone;
    }
    public ZPhoneBean(String name) throws ServiceException {
	this(new ZPhone(name));
    }

    public String getName() {
	return mPhone.getName();
    }

    public String getDisplay() {
	return mPhone.getDisplay();
    }
    
    public String getValidity() {
	return mPhone.getValidity();
    }
}
