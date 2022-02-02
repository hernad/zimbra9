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
package com.zimbra.cs.taglib.tag;

import com.zimbra.common.service.ServiceException;
import com.zimbra.client.ZMailbox;
import com.zimbra.soap.mail.message.CheckSpellingResponse;
import com.zimbra.soap.mail.type.Misspelling;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.List;

public class CheckSpellingTag extends ZimbraSimpleTag {

    private String mText;

    public void setText(String text) { this.mText = text; }

    public void doTag() throws JspException, IOException {
        JspContext jctxt = getJspContext();
        try {
            ZMailbox mbox = getMailbox();
			String trimmed = mText.trim().replaceAll("\\u00A0"," ").replaceAll("\\s\\s+"," ");
			CheckSpellingResponse res = mbox.checkSpelling(trimmed);
			JspWriter out = jctxt.getOut();
			out.print("{\"available\":");
			out.print(res.isAvailable() ? "true" : "false");
			out.println(",\"data\":[");
			boolean firstMisspelling = true;
			for (Misspelling misspelling : res.getMisspelledWords()) {
				if (!firstMisspelling) {
					out.print(',');
				}
				firstMisspelling = false;
				out.print("{\"word\":\"");
				out.print(misspelling.getWord());
				out.print("\",\"suggestions\":[");
				List<String> suggestions = misspelling.getSuggestionsList();
				for (int i = 0, count = suggestions.size(); i < count && i < 5; i++) {
					if (i > 0) {
						out.print(',');
					}
					out.print('"');
					out.print(suggestions.get(i));
					out.print('"');
				}
				out.println("]}");
			}
			out.println("]}");
		} catch (ServiceException e) {
            throw new JspTagException(e);
        }
    }
}
