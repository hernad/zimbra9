/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2006, 2007, 2009, 2010, 2013, 2014, 2016 Synacor, Inc.
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

import com.zimbra.cs.taglib.bean.NextPrevItemBean;
import com.zimbra.cs.taglib.bean.ZSearchResultBean;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

public class ComputeNextPrevItemTag extends ZimbraSimpleTag {


    private String mVar;
    private int mIndex;
    private ZSearchResultBean mSearchResult;

    public void setVar(String var) { this.mVar = var; }

    public void setSearchResult(ZSearchResultBean result) {this.mSearchResult = result; }

    public void setIndex(int index) {this.mIndex = index; }

    public void doTag() throws JspException, IOException {

        PageContext pageContext = (PageContext) getJspContext();

        int prevIndex = mIndex;
        int prevOffset = mSearchResult.getOffset();
        int nextIndex = mIndex;
        int nextOffset = mSearchResult.getOffset();
        boolean hasPrev = true;
        boolean hasNext = true;

        if (mIndex > 0) {
            prevIndex = mIndex - 1;
        } else if (mSearchResult.getOffset() > 0) {
            prevOffset = mSearchResult.getPrevOffset();
            prevIndex = mSearchResult.getLimit()-1;
        } else {
            hasPrev = false;
        }
        
        if (mIndex < mSearchResult.getHits().size() -1) {
            nextIndex = mIndex + 1;
        } else if (mSearchResult.getHasMore()) {
            nextOffset = mSearchResult.getNextOffset();
            nextIndex = 0;
        } else {
            hasNext = false;
        }

        NextPrevItemBean result = new NextPrevItemBean(hasPrev, prevIndex, prevOffset, hasNext, nextIndex, nextOffset);
        pageContext.setAttribute(mVar, result, PageContext.REQUEST_SCOPE);

    }
}
