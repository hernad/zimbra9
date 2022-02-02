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

import com.zimbra.client.ZContact;
import com.zimbra.client.ZMailbox;
import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.taglib.tag.ZimbraSimpleTag;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;
import java.util.Map;

public class GetGroupContactsTag extends ZimbraSimpleTag {

    private String var;
    private String id;
    private boolean json;

    public void setVar(String var) {
        this.var = var;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setJson(boolean json) {
        this.json = json;
    }

    public void doTag() throws JspException, IOException {
        JspContext jctxt = getJspContext();
        try {
            ZMailbox mbox = getMailbox();
            ZContact group = mbox.getContact(id);
            if (json) {
                JSONArray jsonArray = new JSONArray();
                Map<String, ZContact> members = group.getMembers();
                String addr = null;
                for (ZContact contact : members.values()) {
                    if(!contact.isTypeI()) {
                        Map<String, String> attrs = contact.getAttrs();
                        addr = attrs.get("email");
                    }
                    else {
                        addr = contact.getId();
                    }
                    if (addr != null) {
                        jsonArray.put(addr);
                    }
                }
                JSONObject top = new JSONObject();
                top.put("Result", jsonArray);
                top.write(jctxt.getOut());
            }
        } catch (JSONException e) {
            throw new JspTagException(e);
        } catch (ServiceException e) {
            throw new JspTagException(e);
        }
    }
}
