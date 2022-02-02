/*
 * ***** BEGIN LICENSE BLOCK *****
 *
 * Zimbra Collaboration Suite Web Client
 * Copyright (C) 2014, 2016 Synacor, Inc.
 *
 * The contents of this file are subject to the Common Public Attribution License Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at: https://www.zimbra.com/license
 * The License is based on the Mozilla Public License Version 1.1 but Sections 14 and 15
 * have been added to cover use of software over a computer network and provide for limited attribution
 * for the Original Developer. In addition, Exhibit A has been modified to be consistent with Exhibit B.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * See the License for the specific language governing rights and limitations under the License.
 * The Original Code is Zimbra Open Source Web Client.
 * The Initial Developer of the Original Code is Zimbra, Inc.  All rights to the Original Code were
 * transferred by Zimbra, Inc. to Synacor, Inc. on September 14, 2015.
 *
 * All portions of the code are Copyright (C) 2014, 2016 Synacor, Inc. All Rights Reserved.
 *
 * ***** END LICENSE BLOCK *****
 */
//	WebHelp 5.10.002
var WH_MSG_RESIZEPANE		=0x0100;
var WH_MSG_SHOWPANE			=0x0101;
var WH_MSG_HIDEPANE			=0x0102;
var WH_MSG_SYNCTOC			=0x0103;
var WH_MSG_NEXT				=0x0104;
var WH_MSG_PREV				=0x0105;
var WH_MSG_NOSEARCHINPUT	=0x0106;
var WH_MSG_NOSYNC			=0x0107;
var WH_MSG_ENABLEWEBSEARCH 	=0x0108;

var WH_MSG_ISPANEVISIBLE	=0x0109;
var WH_MSG_PANESTATUE		=0x010a;

var	WH_MSG_SYNCINFO			=0x010b;
var WH_MSG_PANEINFO			=0x010c;
var WH_MSG_WEBSEARCH		=0x010d;

var WH_MSG_SEARCHINDEXKEY 	=0x0201;

var WH_MSG_SEARCHFTSKEY 	=0x020a;

var WH_MSG_PROJECTREADY		=0x0301;
var WH_MSG_GETPROJINFO 		=0x0302;

var WH_MSG_SHOWTOC 			=0x0401;
var WH_MSG_SHOWIDX 			=0x0402;
var WH_MSG_SHOWFTS 			=0x0403;
var WH_MSG_SHOWGLO 			=0x0404;

var WH_MSG_SHOWGLODEF 		=0x0500;

var WH_MSG_GETTOCPATHS 		=0x0600;
var WH_MSG_GETAVIAVENUES 	=0x0601;
var WH_MSG_GETCURRENTAVENUE =0x0602;
var WH_MSG_GETPANEINFO		=0x0603;
var WH_MSG_AVENUEINFO		=0x0604;

var WH_MSG_GETSTARTFRAME 	=0x0701;
var WH_MSG_GETDEFAULTTOPIC	=0x0702;

var WH_MSG_SEARCHTHIS		=0x0801;
var WH_MSG_GETSEARCHS		=0x0802;

var WH_MSG_ISINFRAMESET		=0x0900;

var WH_MSG_TOOLBARORDER		=0x0a00;
var WH_MSG_MINIBARORDER		=0x0a01;
var WH_MSG_ISSYNCSSUPPORT	=0x0a02;
var WH_MSG_ISSEARCHSUPPORT	=0x0a03;
var WH_MSG_GETPANETYPE		=0x0a04;
var WH_MSG_BACKUPSEARCH		=0x0a05;
var WH_MSG_GETPANES			=0x0a06;
var WH_MSG_INITSEARCHSTRING =0x0a07;
var WH_MSG_RELOADNS6		=0x0a08;
var WH_MSG_ISAVENUESUPPORT	=0x0a09;

var WH_MSG_GETCMD			=0x0b00;
var	WH_MSG_GETPANE			=0x0b01;
var WH_MSG_GETDEFPANE		=0x0b02;
var WH_MSG_HILITESEARCH 	=0x0b03;
var WH_MSG_GETSEARCHSTR		=0x0b04;
var WH_MSG_SETSYNSTR		=0x0b05;
var WH_MSG_GETMAXRSLT		=0x0b06;
var WH_MSG_SETNUMRSLT		=0x0b07;
var WH_MSG_GETNUMRSLT		=0x0b08;

function whMessage(nMessageId,wSender,nVersion,oParam)
{
	this.nMessageId=nMessageId;
	this.wSender=wSender;
	this.nVersion=nVersion;
	this.oParam=oParam;
}
var gbWhMsg=true;