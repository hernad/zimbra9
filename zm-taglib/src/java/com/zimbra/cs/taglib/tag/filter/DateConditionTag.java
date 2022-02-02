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
package com.zimbra.cs.taglib.tag.filter;

import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.taglib.tag.ZimbraSimpleTag;
import com.zimbra.cs.taglib.bean.ZTagLibException;
import com.zimbra.client.ZFilterCondition.DateOp;
import com.zimbra.client.ZFilterCondition.ZDateCondition;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConditionTag extends ZimbraSimpleTag {

    private DateOp mOp;
    private String mValue;


    public void setValue(String value) { mValue = value; }
    public void setOp(String op) throws ServiceException { mOp = DateOp.fromString(op); }

    public void doTag() throws JspException {
        try {
            FilterRuleTag rule = (FilterRuleTag) findAncestorWithClass(this, FilterRuleTag.class);
            if (rule == null)
                throw new JspTagException("The dateCondition tag must be used within a filterRule tag");
            if (mValue == null || mValue.equals("")) {
                mValue = new SimpleDateFormat("yyyyMMdd").format(new Date());
            }
            rule.addCondition(new ZDateCondition(mOp, mValue));
        } catch (ServiceException e) {
            throw new JspTagException(ZTagLibException.INVALID_FILTER_DATE(e.getMessage(), e));
        }
    }

}
