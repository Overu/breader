package com.goodow.web.core.jpa;

import com.goodow.web.core.shared.Group;
import com.goodow.web.core.shared.GroupService;
import com.goodow.web.core.shared.UserService;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class JpaGroupService extends JpaWebContentService<Group> implements
		GroupService {

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

}
