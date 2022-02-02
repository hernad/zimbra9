/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2009, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.cs.client.soap;
import com.zimbra.cs.versioncheck.VersionUpdate;
import java.util.List;
import java.util.ArrayList;
/**
 * @author Greg Solovyev
 */
public class LmcVersionCheckResponse extends LmcSoapResponse {
	private List <VersionUpdate> updates;
	private boolean status;
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public LmcVersionCheckResponse() {
		updates = new ArrayList();
	}
	
	public void addUpdate(VersionUpdate upd) {
		updates.add(upd);
	}
	
	public List <VersionUpdate> getUpdates() {
		return updates;
	}
}
