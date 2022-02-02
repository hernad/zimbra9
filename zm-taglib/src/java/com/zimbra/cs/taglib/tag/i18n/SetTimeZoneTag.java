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
import java.util.TimeZone;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

public class SetTimeZoneTag extends SimpleTagSupport  {

	//
	// Data
	//

	protected TimeZone value;
	protected String var = I18nUtil.DEFAULT_TIMEZONE_VAR;
	protected int scope = I18nUtil.DEFAULT_SCOPE_VALUE;

	//
	// Public methods
	//

	public void setValue(Object value) {
		if (value instanceof String) {
			this.value = TimeZone.getTimeZone(String.valueOf(value));
		}
		else {
			this.value = (TimeZone)value;
		}
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void setScope(String scope) {
		this.scope = I18nUtil.getScope(scope);
	}

	//
	// SimpleTag methods
	//

	public void doTag() throws JspException, IOException {
		// NOTE: This is to keep compatibility with JSTL
		if (this.value == null) {
			this.value = TimeZone.getTimeZone("GMT");
		}
		PageContext pageContext = (PageContext)getJspContext();
		pageContext.setAttribute(this.var, this.value, this.scope);
		// clear state
		this.value = null;
		this.var = I18nUtil.DEFAULT_TIMEZONE_VAR;
		this.scope = I18nUtil.DEFAULT_SCOPE_VALUE;
	}

} // class SetTimeZoneTag