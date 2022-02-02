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
import com.zimbra.common.util.StringUtil;
import com.zimbra.cs.taglib.bean.ZTagLibException;
import com.zimbra.cs.taglib.tag.ZimbraSimpleTag;
import com.zimbra.client.ZFilterRule;
import com.zimbra.client.ZFilterRules;
import com.zimbra.client.ZMailbox;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;
import java.util.List;

public class MoveFilterRuleTag extends ZimbraSimpleTag {

    private String mName;
    private boolean mUp;

    public void setName(String name) { mName = name; }
    public void setDirection(String direction) throws JspTagException {
        boolean up = direction.equalsIgnoreCase("up");
        boolean down = direction.equalsIgnoreCase("down");
        if (!(up || down))
            throw new JspTagException("moveFilterRule direction must be up or down", null);
        mUp = up;
    }

    public void doTag() throws JspException, IOException {
        try {
            ZMailbox mbox = getMailbox();
            ZFilterRules zrules = mbox.getIncomingFilterRules(true);
            List<ZFilterRule> rules = zrules.getRules();
            int index = -1;
            for (int i=0; i < rules.size(); i++) {
                String ruleName = StringUtil.escapeHtml(rules.get(i).getName());
                if (ruleName.equalsIgnoreCase(mName)) {
                    index = i;
                    break;
                }
            }
            if (index == -1)
                throw ZTagLibException.NO_SUCH_FILTER_EXISTS("filter with name "+mName+" doesn't exist", null);

            if ((index == 0 && mUp) || (index == rules.size()-1 && !mUp))
                return;
            
            ZFilterRule rule = rules.get(index);
            rules.remove(index);
            if (mUp) {
                rules.add(index - 1, rule);
            } else {
                index++;
                if (index > rules.size())
                    rules.add(rule);
                else
                    rules.add(index, rule);
            }
            mbox.saveIncomingFilterRules(new ZFilterRules(rules));
        } catch (ServiceException e) {
            throw new JspTagException(e);
        }
    }
}
