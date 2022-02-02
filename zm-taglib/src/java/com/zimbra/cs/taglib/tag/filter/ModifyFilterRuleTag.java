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

import com.zimbra.cs.taglib.tag.ZimbraSimpleTag;
import com.zimbra.cs.taglib.bean.ZTagLibException;
import com.zimbra.client.ZFilterRule;
import com.zimbra.client.ZMailbox;
import com.zimbra.client.ZFilterRules;
import com.zimbra.common.service.ServiceException;
import com.zimbra.common.util.StringUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class ModifyFilterRuleTag extends ZimbraSimpleTag {

    private ZFilterRule mRule;
    private String mOriginalName;

    public void setRule(ZFilterRule rule) { mRule = rule; }
    public void setOriginalname(String originalName) { mOriginalName = originalName; }

    public void doTag() throws JspException, IOException {
        try {
            if (mOriginalName == null)
                mOriginalName = mRule.getName();

            ZMailbox mbox = getMailbox();
            ZFilterRules rules = mbox.getIncomingFilterRules(true);
            List<ZFilterRule> newRules = new ArrayList<ZFilterRule>();
            boolean origFound = false;

            for (ZFilterRule rule: rules.getRules()) {
                String ruleName = StringUtil.escapeHtml(rule.getName());
                if (ruleName.equalsIgnoreCase(mOriginalName)) {
                    newRules.add(mRule);
                    origFound = true;
                } else if (rule.getName().equalsIgnoreCase(mRule.getName())) {
                    throw ZTagLibException.FILTER_EXISTS("filter with name "+mRule.getName()+" already exists", null);
                } else {
                    newRules.add(rule);
                }
            }
            if (!origFound) {
                throw ZTagLibException.NO_SUCH_FILTER_EXISTS("filter with name "+mRule.getName()+" doesn't exist", null);                
            }
            mbox.saveIncomingFilterRules(new ZFilterRules(newRules));
        } catch (ServiceException e) {
            throw new JspTagException(e);
        }
    }
}
