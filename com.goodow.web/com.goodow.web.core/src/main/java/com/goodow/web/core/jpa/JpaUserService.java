package com.goodow.web.core.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.goodow.web.core.shared.Feed;
import com.goodow.web.core.shared.User;
import com.goodow.web.core.shared.UserService;

public class JpaUserService extends JpaWebContentService<User> implements
		UserService {

	@Override
	public User findUserByUsername(final String userName) {
		User toRtn = null;
		try {
			toRtn = em
					.get()
					.createQuery(
							"select u from com.goodow.web.security.server.domain.User u where u.userName = :userName",
							User.class).setParameter("userName", userName)
					.getSingleResult();
		} catch (NoResultException e) {
		}
		return toRtn;
	}

	@Override
	@RequiresAuthentication
	public void updatePassword(final String userName, final String newPwd) {
		User user = findUserByUsername(userName);
		if (user == null) {
			throw new UnknownAccountException("找不到用户名: " + userName);
		}
		String hashedPwd = new SimpleHash(JpaRealm.ALGORITHM_NAME,
				newPwd.toCharArray(), ByteSource.Util.bytes(user.getPhone()))
				.toHex();
		user.setEmail(hashedPwd);
		em.get().persist(user);
	}

	@Override
	public Feed<User> getFeed() {
		Feed<User> feed = new Feed<User>();
		List<User> users = new ArrayList<User>();
		User u1 = new User();
		u1.setId("jack");
		u1.setName("Jack");
		u1.setEmail("jack@gmail.com");
		u1.setPhone("+86-13911231234");
		users.add(u1);
		User u2 = new User();
		u2.setId("rose");
		u2.setName("Rose");
		u2.setEmail("rose@gmail.com");
		u2.setPhone("13812834321");
		users.add(u2);
		feed.setEntries(users);
		feed.setTotalResults(20);
		return feed;
	}
}
