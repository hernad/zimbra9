/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2009, 2010, 2013, 2014, 2016 Synacor, Inc.
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zimbra.cs.taglib.tag.voice;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import com.zimbra.cs.taglib.tag.ZimbraSimpleTag;
import com.zimbra.common.service.ServiceException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

/**
 *
 * @author lars
 */
public class ListObjectTag extends ZimbraSimpleTag {

  /**
   * @param args the command line arguments
   */
  private String mVar;
  private int mScope = PageContext.PAGE_SCOPE;
  private String mCsep;
  private String mStrArr;
  private HashMap<String, ArrayList> mMap;
  private String mPhone;
  private String mAdd;
  private String mRemove;
  private Boolean mClear = Boolean.FALSE;

  public void setVar(String var) {
    mVar = var;
  }

  public void setScope(String scope) {
    if (scope.equalsIgnoreCase("APPLICATION")) {
      mScope = PageContext.APPLICATION_SCOPE;
    } else if (scope.equalsIgnoreCase("SESSION")) {
      mScope = PageContext.SESSION_SCOPE;
    } else if (scope.equalsIgnoreCase("REQUEST")) {
      mScope = PageContext.REQUEST_SCOPE;
    } else {
      mScope = PageContext.PAGE_SCOPE;
    }
  }

  public void setCsep(String csep) {
    mCsep = csep;
  }
  public void setStrArr(String strArr) {
    mStrArr = strArr;
  }

  public void setMap(HashMap<String, ArrayList> map) {
    mMap = map;
  }

  public void setPhone(String phone) {
    mPhone = phone;
  }

  public void setAdd(String add) {
    mAdd = add;
  }

  public void setRemove(String remove) {
    mRemove = remove;
  }

  public void setClear(boolean clear) {
    mClear = new Boolean(clear);
  }

  private String getCsepString(ArrayList<String> list) {
    Iterator<String> lIter = list.iterator();
    StringBuilder sb = new StringBuilder();
    while (lIter.hasNext()) {
      sb.append(lIter.next());
      if (lIter.hasNext()) {
        sb.append(",");
      }
    }
    return sb.toString();
  }

  private String[] getStringArray(ArrayList<String> list) {
    return list.toArray(new String[list.size()]);
  }

  public void doTag() throws IOException {
      if (mMap == null) {
        mMap = new HashMap<String, ArrayList>();
      }
      if (mPhone != null) {
        ArrayList<String> list = null;
        if (mMap.containsKey(mPhone)) {
          try {
            list = (ArrayList<String>) mMap.get(mPhone);
          } catch (Exception ex) {}
        }
        if (list == null) {
          list = new ArrayList<String>();
          mMap.put(mPhone, list);
        }

        if (mAdd != null) {
          String[] items = mAdd.split(",");
          for (int i = 0; i < items.length; i++) {
			if (!list.contains(items[i]))
              list.add(items[i]);
          }
        }

        if (mRemove != null) {
          String[] items = mRemove.split(",");
          for (int i = 0; i < items.length; i++) {
            int position = list.indexOf(items[i]);
            if (position >= 0 && position < list.size())
              list.remove(position);
          }
        }

        if (mClear != null && mClear.booleanValue()) {
          list.clear();
        }

        if (mCsep != null) {
          getJspContext().setAttribute(mCsep, getCsepString(list), mScope);
        }
        if (mStrArr != null) {
          getJspContext().setAttribute(mStrArr, getStringArray(list), mScope);
        }
      }
      if (mVar != null) {
        getJspContext().setAttribute(mVar, mMap, mScope);
      }
      
  }
}

