/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2006, 2007, 2008, 2009, 2010, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.cs.taglib.tag.contact;

import com.zimbra.common.mailbox.ContactConstants;
import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.mailbox.Contact;
import com.zimbra.cs.taglib.tag.ZimbraSimpleTag;

import javax.servlet.jsp.JspTagException;
import java.util.HashMap;
import java.util.Map;

public class ContactOpTag extends ZimbraSimpleTag {

    protected boolean mForce;
    protected Map<String, String> mAttrs = new HashMap<String,String>();
    protected Map<String, String> mMembers = new HashMap<String,String>();

    public void setForce(boolean force) { mForce = force; }

    public void addAttr(String name, String value) throws JspTagException {
        if (!mForce) {
            try {
                ContactConstants.Attr.fromString(name); // make sure it is a known attr name
            } catch (ServiceException e) {
                throw new JspTagException(e);
            }
        }
        mAttrs.put(name, value);
    }
    
    public void addMembers(String id, String type) throws JspTagException {
        mMembers.put(id, type);
    }

    protected boolean allFieldsEmpty() {
        for (Map.Entry<String,String> entry : mAttrs.entrySet()) {
            if (entry.getValue() != null && entry.getValue().trim().length() > 0 && !entry.getKey().equalsIgnoreCase(ContactConstants.A_fileAs)){
                return false;
            }
        }
        return true;
    }
}
