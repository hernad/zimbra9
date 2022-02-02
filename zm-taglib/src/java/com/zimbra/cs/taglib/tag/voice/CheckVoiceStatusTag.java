/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2007, 2009, 2010, 2011, 2013, 2014, 2016 Synacor, Inc.
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

package com.zimbra.cs.taglib.tag.voice;

import com.zimbra.cs.taglib.tag.ZimbraSimpleTag;
import com.zimbra.client.ZMailbox;
import com.zimbra.client.ZPhoneAccount;
import com.zimbra.common.service.ServiceException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;
import java.util.List;

public class CheckVoiceStatusTag extends ZimbraSimpleTag {
	private String mVar;

	public void setVar(String var) { mVar = var; }

	public void doTag() throws JspException, IOException {
		try {
			ZMailbox mbox = getMailbox();
			List<ZPhoneAccount> accounts = mbox.getAllPhoneAccounts();
			Boolean ok = accounts.size() > 0;
			getJspContext().setAttribute(mVar, ok, PageContext.PAGE_SCOPE);
		} catch (ServiceException e) {
			getJspContext().setAttribute(mVar, Boolean.FALSE, PageContext.PAGE_SCOPE);
			throw new JspTagException(e);
		}
	}
}