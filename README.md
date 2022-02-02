= bring.out zimbra 9.0.0 build

== new build

	cd zm-build

       
        [zm-build]$ git branch -l
         * (HEAD detached at 9.0.0.p23)
         develop
        [zm-build]$ export BUILD_NO=33001
        [zm-build]$ ./build.pl --build-no=$BUILD_NO --build-ts=`date +'%Y%m%d%H%M%S'` --build-release=BOUT --build-release-no=9.0.0 --build-release-candidate=GA --build-type=FOSS --build-thirdparty-server=files.zimbra.com --no-interactive



== NOTES

before git commit:

	tar cvfz git_repos.tar.gz */.git
	rm -rf */.git



