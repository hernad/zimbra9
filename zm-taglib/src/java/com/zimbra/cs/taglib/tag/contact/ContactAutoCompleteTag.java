/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2007, 2008, 2009, 2010, 2011, 2013, 2014, 2015, 2016 Synacor, Inc.
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

import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.taglib.tag.ZimbraSimpleTag;
import com.zimbra.client.ZAutoCompleteMatch;
import com.zimbra.client.ZMailbox;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContactAutoCompleteTag extends ZimbraSimpleTag {

    private String mVar;
    private String mQuery;
    private int mLimit;
    private boolean mJSON;

    public void setVar(String var) { this.mVar = var; }
    public void setQuery(String query) { this.mQuery = query.toLowerCase(); }
    public void setLimit(int limit) { this.mLimit = limit; }
    public void setJson(boolean json) { this.mJSON = json; }

    public void doTag() throws JspException, IOException {
        JspContext jctxt = getJspContext();
        try {
            ZMailbox mbox = getMailbox();

            List<ZAutoCompleteMatch> matches = mbox.autoComplete(mQuery, mLimit);
            if (mJSON)
            	toJSON(jctxt.getOut(), matches);
        } catch (JSONException e) {
            throw new JspTagException(e);
        } catch (ServiceException e) {
            throw new JspTagException(e);
        }
    }
    public void toJSON(JspWriter out, Collection<ZAutoCompleteMatch> matches) throws JSONException, IOException {
    	JSONArray jsonArray = new JSONArray();
    	for (ZAutoCompleteMatch match : matches) {
    		jsonArray.put(match.toZJSONObject().getJSONObject());
    	}
    	JSONObject top = new JSONObject();
    	top.put("Result", jsonArray);
    	top.write(out);
    }
}
