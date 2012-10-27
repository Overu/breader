package com.goodow.web.core.jpa;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.persistence.TypedQuery;

import com.goodow.web.core.shared.WebContent;
import com.goodow.web.core.shared.WebContentService;
import com.google.inject.persist.Transactional;

public class JpaWebContentService<E extends WebContent> extends
		JpaWebService<E> implements WebContentService<E> {

	private static final Logger logger = Logger
			.getLogger(JpaWebContentService.class.getName());

	private WebContent container;

	public WebContent getContainer() {
		return container;
	}

	public void setContainer(WebContent container) {
		this.container = container;
	}

	@Override
	public E getById(final String id) {
		return em.get().find(getObjectType(), id);
	}

	@Override
	public List<E> find(final WebContent container) {
		String hsql = "select e from " + getObjectType().getName()
				+ " e where e.container=:container";
		TypedQuery<E> query = em.get().createQuery(hsql, getObjectType());
		query.setParameter("container", container);
		return query.getResultList();
	}

	@Override
	@Transactional
	public void remove(final E domain) {
		em.get().remove(domain);
	}

	@Override
	public E save(final E entity) {
		if (entity.getId() == null) {
			entity.setId(UUID.randomUUID().toString());
		}
		em.get().persist(entity);
		return entity;
	}

}
