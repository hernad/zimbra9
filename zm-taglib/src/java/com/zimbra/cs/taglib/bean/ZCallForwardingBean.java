/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2007, 2009, 2010, 2011, 2013, 2014, 2016 Synacor, Inc.
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

package com.zimbra.cs.taglib.bean;

import com.zimbra.client.ZCallFeature;
import com.zimbra.client.ZPhone;
import com.zimbra.common.soap.VoiceConstants;
import com.zimbra.common.service.ServiceException;

public class ZCallForwardingBean extends ZCallFeatureBean {
    public ZCallForwardingBean(ZCallFeature feature) {
        super(feature);
    }

    public void setForwardTo(String phone) {
        String name = ZPhone.getNonFullName(phone);
        getFeature().setData(VoiceConstants.A_FORWARD_TO, name);
    }
    
    public int getNumberOfRings() {
	String rings = getFeature().getData(VoiceConstants.A_NUM_RING_CYCLES);
	try {
	    return Integer.parseInt(rings);
	} catch (NumberFormatException ex) {
	    return -1;
	}
    }
    
    public void setNumberOfRings(int rings) {
	getFeature().setData(VoiceConstants.A_NUM_RING_CYCLES, Integer.toString(rings));
    }

    public String getForwardTo() throws ServiceException {
		String name = getFeature().getData(VoiceConstants.A_FORWARD_TO);
		if (name == null) {
			name = "";
		}
		return ZPhone.getDisplay(name);
    }
}
