pwd
svn rm https://goodow.googlecode.com/svn/com.goodow.web/trunk/com.goodow.web.mobile/web/ -m "" --username ${username_subversion_mobile} --password ${password_subversion_mobile} --force
svn import mobile https://goodow.googlecode.com/svn/com.goodow.web/trunk/com.goodow.web.mobile/web/ -m "" --username ${username_subversion_mobile} --password ${password_subversion_mobile} --force
rm -rf mobile