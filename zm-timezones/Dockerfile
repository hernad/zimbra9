# 
# ***** BEGIN LICENSE BLOCK *****
# Zimbra Collaboration Suite Server
# Copyright (C) 2020 Synacor, Inc.
#
# This program is free software: you can redistribute it and/or modify it under
# the terms of the GNU General Public License as published by the Free Software Foundation,
# version 2 of the License.
#
# This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
# without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
# See the GNU General Public License for more details.
# You should have received a copy of the GNU General Public License along with this program.
# If not, see <https://www.gnu.org/licenses/>.
# ***** END LICENSE BLOCK *****
# 

FROM busybox

COPY build/js/AjxTimezoneData.js          /opt/zimbra/jetty_base/webapps/zimbra/js/ajax/util/AjxTimezoneData.js
COPY build/js/AjxTimezoneData.js          /opt/zimbra/jetty_base/webapps/zimbraAdmin/js/ajax/util/AjxTimezoneData.js
COPY build/messages/*                     /opt/zimbra/jetty_base/webapps/zimbra/WEB-INF/classes/messages/
COPY build/messages/*                     /opt/zimbra/jetty_base/webapps/zimbraAdmin/WEB-INF/classes/messages/
COPY conf/timezones.ics                   /opt/zimbra/conf/timezones.ics