pwd
svn rm https://goodow.googlecode.com/svn/com.goodow.web/trunk/com.goodow.web.mobile/ -m "" --username ${username.subversion.mobile} --password ${password.subversion.mobile} --force
svn import mobile https://goodow.googlecode.com/svn/com.goodow.web/trunk/com.goodow.web.mobile/ -m "" --username ${username.subversion.mobile} --password ${password.subversion.mobile} --force
rm -rf mobile