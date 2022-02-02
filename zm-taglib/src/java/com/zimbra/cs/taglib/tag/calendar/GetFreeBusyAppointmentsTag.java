/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2013, 2014, 2016 Synacor, Inc.
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
import com.zimbra.cs.taglib.bean.ZApptSummariesBean;
import com.zimbra.cs.taglib.bean.ZMailboxBean;
import com.zimbra.cs.taglib.tag.ZimbraSimpleTag;
import com.zimbra.client.ZAppointmentHit;
import com.zimbra.client.ZMailbox;
import com.zimbra.client.ZMailbox.ZGetFreeBusyResult;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetFreeBusyAppointmentsTag extends ZimbraSimpleTag {

    private String mVar;
    private String mEmail;
    private String mVarException;
    private long mStart;
    private long mEnd;
    private int mFolderId;
    private ZMailboxBean mMailbox;

    public void setVar(String var) { this.mVar = var; }
    public void setEmail(String email) { this.mEmail = email; }
    public void setVarexception(String varException) { this.mVarException = varException; }

    public void setStart(long start) { this.mStart = start; }
    public void setEnd(long end) { this.mEnd = end; }
    public void setFolderid(int folderid) { mFolderId = folderid; }
    public void setBox(ZMailboxBean mailbox) { this.mMailbox = mailbox; }

    public void doTag() throws JspException, IOException {
        JspContext jctxt = getJspContext();
        try {
            ZMailbox mbox = mMailbox != null ? mMailbox.getMailbox() :  getMailbox();

            List<ZGetFreeBusyResult> result =
                mbox.getFreeBusy(mEmail, mStart, mEnd, mFolderId);
            List<ZAppointmentHit>appts = mbox.createAppointmentHits(result.get(0).getTimeSlots());
            jctxt.setAttribute(mVar, new ZApptSummariesBean(appts),  PageContext.PAGE_SCOPE);
        } catch (ServiceException e) {
            if (mVarException != null) {
                jctxt.setAttribute(mVarException, e,  PageContext.PAGE_SCOPE);
                jctxt.setAttribute(mVar, new ZApptSummariesBean(new ArrayList<ZAppointmentHit>()),  PageContext.PAGE_SCOPE);
            } else {
                throw new JspTagException(e);
            }
        }
    }
}
