/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2008, 2009, 2010, 2013, 2014, 2016 Synacor, Inc.
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

package com.zimbra.cs.taglib.tag.i18n;

import java.io.*;
import java.util.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

public class TimeZoneTag extends BodyTagSupport {

	//
	// Data
	//

	protected TimeZone value;

	//
	// Public methods
	//

	public TimeZone getTimeZone() {
		return this.value;
	}

	// properties

	public void setValue(Object value) {
		if (value instanceof String) {
			this.value = TimeZone.getTimeZone(String.valueOf(value));
		}
		else {
			this.value = (TimeZone)value;
		}
	}

	//
	// TagSupport methods
	//

	public int doStartTag() throws JspException {
		// NOTE: This is to keep compatibility with JSTL
		if (this.value == null) {
			this.value = TimeZone.getTimeZone("GMT");
		}
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		this.value = null;
		return EVAL_PAGE;
	}

} // class TimeZoneTag