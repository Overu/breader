package com.goodow.wave.server.media;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.goodow.wave.test.BaseTest;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class AttachmentTest extends BaseTest {
	@Inject
	Provider<EntityManager> ems;

	@Test
	public void test() {
		AttachmentMetadata attachmentMetadata = new AttachmentMetadata("i1",
				null);
		ems.get().persist(attachmentMetadata);
	}
}
