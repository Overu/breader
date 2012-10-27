package com.goodow.web.core.shared;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("groups")
public interface GroupService extends WebContentService<Group> {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	String getIt();

	@POST
	@Consumes({ MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML })
	List<Group> postIt(List<Group> entity);

	@Path("{id}")
	@GET
	@Consumes(MediaType.WILDCARD)
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Override
	Group getById(@PathParam("id") String groupId);

	@Path("query")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	String getByName(@QueryParam("name") String name);

	@Path("{id}/users")
	UserService getUserService(@PathParam("id") String groupId);

}
