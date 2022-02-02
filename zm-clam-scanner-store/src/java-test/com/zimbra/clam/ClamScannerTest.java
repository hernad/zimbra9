/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2014, 2016 Synacor, Inc.
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
package com.zimbra.clam;

import java.net.MalformedURLException;

import org.junit.Assert;
import org.junit.Test;

public class ClamScannerTest {

    public void parseGoodUrl(String url, String expectedHost, int expectedPort) {
        ClamScanner scanner = new ClamScanner();
        try {
            scanner.setURL(url);
        } catch (MalformedURLException e) {
            Assert.fail("failed to parse [" + url + "]");
        }
        Assert.assertTrue(scanner.isEnabled());
        Assert.assertEquals(expectedHost, scanner.getClamdHost());
        Assert.assertEquals(expectedPort, scanner.getClamdPort());
    }

    public void parseBadUrl(String url) {
        ClamScanner scanner = new ClamScanner();
        try {
            scanner.setURL(url);
            Assert.fail("expected parse error from url [" + url +"]");
        } catch (MalformedURLException e) {
            //expected - bad url
        } catch (IllegalStateException e) {
            //expected - acceptable OK url, but no port or not clam
        }
    }

    public void createAndParseGoodUrl(String host, int port) {
        parseGoodUrl("clam://" + host + ":" + port, host, port);
    }


    @Test
    public void localhostPort() {
        createAndParseGoodUrl("localhost", 12345);
    }

    @Test
    public void hostnamePort() {
        createAndParseGoodUrl("clam.example.com", 3452);
    }

    @Test
    public void ipv4() {
        createAndParseGoodUrl("129.151.2.13", 9213);
    }

    @Test
    public void ipv6() {
        parseGoodUrl("clam://[2001:db8:85a3:8d3:1319:8a2e:370:7348]:443", "2001:db8:85a3:8d3:1319:8a2e:370:7348", 443);
    }

    @Test
    public void trailingSlashes() {
        parseGoodUrl("clam://localhost:12345/", "localhost", 12345);
        parseGoodUrl("clam://192.151.121.1:999/", "192.151.121.1", 999);
        parseGoodUrl("clam://clam.example.com:4212/", "clam.example.com", 4212);
        parseGoodUrl("clam://[2001:db8:85a3:8d3:1319:8a2e:370:7348]:876", "2001:db8:85a3:8d3:1319:8a2e:370:7348", 876);
        parseGoodUrl("clam://10.137.245.108:3310/", "10.137.245.108", 3310);
        parseGoodUrl("clam://zqa-363.eng.zimbra.com:3310/", "zqa-363.eng.zimbra.com", 3310);
        parseGoodUrl("clam://[fe80::250:56ff:fea5:151e]:3310/", "fe80::250:56ff:fea5:151e", 3310);
        parseGoodUrl("clam://localhost:3310/", "localhost", 3310);
    }

    @Test
    public void badUrls() {
        parseBadUrl("foo");
        parseBadUrl("foo://localhost:1231");
        parseBadUrl("clam://localhost:99999"); //out of port range
        parseBadUrl("clam:///"); //host/ip required
        parseBadUrl("clam://localhost/"); //port required
        parseBadUrl("clam://2001:db8:85a3:8d3:1319:8a2e:370:7348:1234"); //ipv6 brackets required
    }
}
