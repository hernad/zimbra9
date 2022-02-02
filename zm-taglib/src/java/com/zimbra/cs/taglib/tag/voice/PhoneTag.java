/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2009, 2010, 2013, 2014, 2016 Synacor, Inc.
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

import com.zimbra.common.service.ServiceException;
//import com.zimbra.cs.account.Provisioning;

import com.zimbra.cs.taglib.bean.ZPhoneBean;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

public class PhoneTag extends ZimbraSimpleTag {

    protected String mVar;
    protected String mName;
    protected String mDisplayVar;
    protected String mErrorVar;
    
    public void setVar(String var) {
	    this.mVar = var;
    }
        
    public void setName(String name) {
	    this.mName = name;
    }
    
    public String getName() {
	    return this.mName;
    }
    
    public void setDisplayVar(String displayVar) {
	    this.mDisplayVar = displayVar;
    }

    public void setErrorVar(String errorVar) {
	    this.mErrorVar = errorVar;
    }
    
    public void doTag() throws JspTagException {
	try {
	    ZPhoneBean phone = new ZPhoneBean(this.mName);
	    
	    String validity = phone.getValidity();
	    if (mErrorVar!=null) getJspContext().setAttribute(mErrorVar, validity, PageContext.PAGE_SCOPE);
	    if (mDisplayVar!=null) getJspContext().setAttribute(mDisplayVar, phone.getDisplay(), PageContext.PAGE_SCOPE);
	    if (mVar!=null) getJspContext().setAttribute(mVar, validity.equals(ZPhoneBean.VALID), PageContext.PAGE_SCOPE);
	} catch (ServiceException e) {
	    throw new JspTagException(e);
	}
    }
}
