Step to build deployable zimlet extension package(.zip).
--------------
1. git clone ssh://git@stash.corp.synacor.com:7999/zimbra/zm-zcs.git ../zm-zcs

2. ant package-zimlet / ant dist-zimlet


Step to build and deploy zimlet extension.

1. mkdir ~/.zcs-deps

2. Ensure that the following jars are present under ~/.zcs-deps dir:

	* ant-contrib-1.0b1.jar (You can get this from perforce ZimbraCommon/jars)
	* zimbracommon.jar (you can build this from zm-common repo)
	* zimbrasoap.jar (you can build this from zm-soap repo)
	* zimbraclient.jar (you can build this from zm-client repo)
	* zimbrastore.jar (you can build this from zm-store repo)	

3. git clone ssh://git@stash.corp.synacor.com:7999/zimbra/zm-zcs.git ../zm-zcs

4. git clone https://github.com/Zimbra/zimbra-package-stub.git ../zimbra-package-stub

5. ant deploy