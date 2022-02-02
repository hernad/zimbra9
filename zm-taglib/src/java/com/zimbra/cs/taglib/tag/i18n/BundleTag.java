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

import java.util.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

public class BundleTag extends TagSupport {

	//
	// Data
	//

	protected ResourceBundle bundle;

	protected String basename;
	protected String prefix;
	protected boolean force;

	//
	// Public methods
	//

	public ResourceBundle getBundle() {
		return this.bundle;
	}

	public String getPrefix() {
		return this.prefix;
	}

	// properties

	public void setBasename(String basename) {
		this.basename = basename;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setForce(boolean force) {
		this.force = force;
	}

	//
	// TagSupport methods
	//

	public int doStartTag() throws JspException {
		PageContext pageContext = super.pageContext;
		String basename = I18nUtil.makeBasename(pageContext, this.basename);
		this.bundle = I18nUtil.findBundle(pageContext, null, -1, basename);
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		this.basename = null;
		this.prefix = null;
		this.bundle = null;
		this.force = false;

		return EVAL_PAGE;
	}

} // class BundleTag