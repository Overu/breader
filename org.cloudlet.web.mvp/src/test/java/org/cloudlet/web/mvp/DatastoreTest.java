package org.cloudlet.web.mvp;

import com.google.inject.Inject;

import org.cloudlet.web.mvp.server.tree.domain.TreeNode;
import org.cloudlet.web.mvp.server.tree.service.TreeService;
import org.cloudlet.web.test.BaseTest;
import org.junit.Test;

import java.util.List;

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
