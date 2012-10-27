package com.goodow.web.core.jaxb;

import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;

import com.goodow.wave.test.BaseTest;
import com.goodow.web.core.shared.Feed;
import com.goodow.web.core.shared.GroupService;
import com.goodow.web.core.shared.User;
import com.google.inject.Inject;

public class LibraryTest extends BaseTest {

	@Inject
	GroupService groupService;

	@Test
	public void testJAXB() throws JAXBException {
		Feed<User> users = groupService.getUserService("g1").getFeed();
		JAXBContext jc = JAXBContext.newInstance(Feed.class, User.class);
		Marshaller marshaller = jc.createMarshaller();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		marshaller.marshal(users, os);
		System.out.println(os.toString());
	}
}
