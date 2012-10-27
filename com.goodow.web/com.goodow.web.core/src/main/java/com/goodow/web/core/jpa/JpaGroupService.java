package com.goodow.web.core.jpa;

import java.util.List;

import com.goodow.web.core.shared.Group;
import com.goodow.web.core.shared.GroupService;
import com.goodow.web.core.shared.UserService;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class JpaGroupService extends JpaWebContentService<Group> implements
		GroupService {

	public String getIt() {
		return "Got it!";
	}

	@Override
	public List<Group> postIt(List<Group> entity) {
		return entity;
	}

	@Override
	public Group getById(String id) {
		Group g = new Group();
		g.setId(id);
		g.setName(id);
		return g;
	}

	@Override
	public String getByName(String name) {
		return name;
	}

	@Inject
	Provider<UserService> userServiceProvider;

	@Override
	public UserService getUserService(String groupId) {
		Group g = getById(groupId);
		UserService service = userServiceProvider.get();
		service.setContainer(g);
		return service;
	}
}
