/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
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

import com.zimbra.cs.taglib.tag.ZimbraSimpleTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

public class MemberTag extends ZimbraSimpleTag {

    private String mType;
    private String mId;

    public void setValue(String value) { mId = value; }
    public void setName(String name) { mType = name; }

    public void doTag() throws JspException {
        ContactOpTag op = (ContactOpTag) findAncestorWithClass(this, ContactOpTag.class);
        if (op == null)
            throw new JspTagException("The field tag must be used within a create/modify contact tag");
        op.addMembers(mId, mType) ;
    }

}
