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

package com.zimbra.cs.taglib.tag.calendar;

import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.taglib.bean.ZMessageComposeBean;
import com.zimbra.cs.taglib.bean.ZMessageBean;
import com.zimbra.cs.taglib.tag.ZimbraSimpleTag;
import com.zimbra.client.ZInvite;
import com.zimbra.client.ZMailbox;
import com.zimbra.client.ZFolder;
import com.zimbra.client.ZDateTime;
import com.zimbra.client.ZInvite.ZComponent;
import com.zimbra.client.ZMailbox.ZAppointmentResult;
import com.zimbra.client.ZMailbox.ZOutgoingMessage;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

public class SaveAppointmentTag extends ZimbraSimpleTag {

    private String mVar;

    private ZMessageComposeBean mCompose;
    private ZMessageBean mMessage;

    public void setCompose(ZMessageComposeBean compose) { mCompose = compose; }
    public void setMessage(ZMessageBean message) { mMessage = message; }

    public void setVar(String var) { this.mVar = var; }

    public void doTag() throws JspException, IOException {
        JspContext jctxt = getJspContext();
        PageContext pc = (PageContext) jctxt;

        
        try {
            ZMailbox mbox = getMailbox();

            String folderId = mCompose.getApptFolderId();

            ZInvite inv = mCompose.toInviteObject(mbox, mMessage, folderId);

            ZInvite previousInv = mMessage != null ? mMessage.getInvite() : null;
            ZComponent prevComp = previousInv != null ? previousInv.getComponent() : null;
            String compNum = prevComp != null ? prevComp.getComponentNumber() : "0";

            if (inv.getComponent().getAttendees().size() > 0) {
                String key;
                if (mMessage != null) {
                    key = (mCompose.getUseInstance()) ? "apptInstanceModified" : "apptModified";
                } else {
                    key = "apptNew";
                }
                mCompose.setInviteBlurb(mbox, pc, inv, previousInv, key);
            }

            ZDateTime exceptionId = prevComp != null && prevComp.isException() ? prevComp.getStart() : null;

            ZOutgoingMessage m = mCompose.toOutgoingMessage(mbox);

            if (folderId == null || folderId.length() == 0)
                folderId = ZFolder.ID_CALENDAR;

            ZAppointmentResult response;

            if (mMessage != null) {
                if (mCompose.getUseInstance()) {
                    if (mCompose.getExceptionInviteId() != null && mCompose.getExceptionInviteId().length() > 0) {
                        response = mbox.modifyAppointment(mCompose.getExceptionInviteId(), compNum, exceptionId , m, inv);
                    } else {
                        exceptionId = new ZDateTime(mCompose.getInstanceStartTime(), mCompose.getAllDay(), mbox.getPrefs().getTimeZone());
                        response = mbox.createAppointmentException(mCompose.getInviteId(), compNum, exceptionId, m, inv, null);
                    }
                } else {
                    response = mbox.modifyAppointment(mCompose.getInviteId(), compNum, exceptionId, m, inv);
                }

            } else {
                response = mbox.createAppointment(folderId, null, m, inv, null);
            }

            jctxt.setAttribute(mVar, response, PageContext.PAGE_SCOPE);

        } catch (ServiceException e) {
            throw new JspTagException(e.getMessage(), e);
        }
    }
}
