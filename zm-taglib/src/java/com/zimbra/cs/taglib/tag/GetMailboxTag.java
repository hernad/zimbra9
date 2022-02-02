/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2006, 2007, 2008, 2009, 2010, 2013, 2014, 2016 Synacor, Inc.
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

import com.zimbra.common.auth.ZAuthToken;
import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.taglib.bean.ZMailboxBean;
import com.zimbra.cs.taglib.ZJspSession;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;

public class GetMailboxTag extends ZimbraSimpleTag {
    
    private String mVar;
    private boolean mRefreshAccount;
    private String mRestAuthToken;
    private boolean mCsrfEnabled;
    private ZAuthToken mRestAuthTokenObject;
    private String mRestTargetAccountId;
    
    public void setVar(String var) { this.mVar = var; }
    public void setRefreshaccount(boolean refresh) { this.mRefreshAccount = refresh; }
    public void setRestauthtoken(String authToken) { this.mRestAuthToken = authToken; }
    public void setCsrfenabled(boolean csrfEnabled) { this.mCsrfEnabled = csrfEnabled; }
    public void setRestauthtokenobject(ZAuthToken authToken) { this.mRestAuthTokenObject = authToken; }
    public void setResttargetaccountid(String targetId) { this.mRestTargetAccountId = targetId; }

    public void doTag() throws JspException, IOException {
        try {
            JspContext ctxt = getJspContext();
            
            if (mRestAuthTokenObject != null) {
                ctxt.setAttribute(mVar, new ZMailboxBean(ZJspSession.getRestMailbox((PageContext)ctxt, mRestAuthTokenObject, mRestTargetAccountId)),  PageContext.REQUEST_SCOPE);
            } else if (mRestAuthToken != null && mRestAuthToken.length() > 0) {
                ctxt.setAttribute(mVar, new ZMailboxBean(ZJspSession.getRestMailbox((PageContext)ctxt, mRestAuthToken, mCsrfEnabled, mRestTargetAccountId)),  PageContext.REQUEST_SCOPE);
            } else {
                ZMailboxBean bean = (ZMailboxBean) ctxt.getAttribute(mVar, PageContext.REQUEST_SCOPE);
                if ( bean == null) {
                    bean = new ZMailboxBean(getMailbox());
                    ctxt.setAttribute(mVar, bean,  PageContext.REQUEST_SCOPE);
                }
                if (mRefreshAccount)
                    bean.getAccountInfoReload();
            }
        } catch (ServiceException e) {
            throw new JspTagException(e.getMessage(), e);
        }
    }
}
