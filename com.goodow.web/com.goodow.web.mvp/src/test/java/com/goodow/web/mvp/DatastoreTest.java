package com.goodow.web.mvp;

import java.util.List;

import org.junit.Test;

import com.goodow.wave.test.BaseTest;
import com.goodow.web.mvp.jpa.TreeService;
import com.goodow.web.mvp.shared.TreeNode;
import com.google.inject.Inject;

public class DatastoreTest extends BaseTest {

	@Inject
	private TreeService service;

	@Test
	public void testQuery() {
		TreeNode node = new TreeNode();
		node.setName("n1");
		node.setPath("p1");
		service.put(node);

		List<TreeNode> list = service.find(0, 10, "id");
		list.size();
	}

	@Test
	public void testQuery2() {
		testQuery();
	}
}
