package com.goodow.web.core.shared;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("users")
public interface UserService extends WebContentService<User> {

	@GET
	@Produces({ MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_JSON })
	public Feed<User> getFeed();

	User findUserByUsername(final String userName);

	void updatePassword(final String userName, final String newPwd);
}
