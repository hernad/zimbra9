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
package com.zimbra.cs.taglib.tag;

import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.taglib.bean.ZMessageComposeBean;
import com.zimbra.client.ZDateTime;
import com.zimbra.client.ZMailbox;
import com.zimbra.client.ZMailbox.ReplyVerb;
import com.zimbra.client.ZMailbox.ZOutgoingMessage;
import com.zimbra.client.ZMailbox.ZSendInviteReplyResult;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

public class SendInviteReplyTag extends ZimbraSimpleTag {

    private String mVar;
    
    private ZMessageComposeBean mCompose;

    public void setCompose(ZMessageComposeBean compose) { mCompose = compose; }

    public void setVar(String var) { this.mVar = var; }

    public void doTag() throws JspException, IOException {
        JspContext jctxt = getJspContext();
        try {
            ZMailbox mbox = getMailbox();

            ZOutgoingMessage m = mCompose.toOutgoingMessage(mbox);

            ZDateTime instance = mCompose.getInviteReplyInst() == 0 ? null :  new ZDateTime(mCompose.getInviteReplyInst(), mCompose.getInviteReplyAllDay(), mbox.getPrefs().getTimeZone());

            String compNum = mCompose.getCompNum();
            if (compNum != null && compNum.length()==0)
                compNum = "0";

            String instCompNum = mCompose.getInstanceCompNum();
            if (instCompNum != null && instCompNum.length()==0)
                instCompNum = null;


            if (instance != null && instCompNum != null)
                compNum = instCompNum;

            ZSendInviteReplyResult response = mbox.sendInviteReply(mCompose.getMessageId(), compNum, ReplyVerb.fromString(mCompose.getInviteReplyVerb()), true, null, instance, m);
            jctxt.setAttribute(mVar, response, PageContext.PAGE_SCOPE);

        } catch (ServiceException e) {
            throw new JspTagException(e.getMessage(), e);
        }
    }
}
