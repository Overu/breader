package com.goodow.web.core.shared;

import javax.ws.rs.Path;

@Path("users")
public interface UserService extends WebContentService<User> {

	User findUserByUsername(final String userName);

	void updatePassword(final String userName, final String newPwd);
}
