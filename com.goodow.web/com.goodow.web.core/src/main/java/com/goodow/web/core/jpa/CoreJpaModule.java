package com.goodow.web.core.jpa;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

import java.util.logging.Logger;

import org.aopalliance.intercept.MethodInterceptor;

import com.goodow.web.core.shared.CategoryService;
import com.goodow.web.core.shared.GroupService;
import com.goodow.web.core.shared.ResourceService;
import com.goodow.web.core.shared.UserService;
import com.goodow.web.core.shared.WebContentService;
import com.google.inject.Singleton;
import com.google.inject.persist.finder.Finder;
import com.google.inject.persist.jpa.JpaPersistModule;

@Singleton
public class CoreJpaModule extends JpaModule {

	private final Logger logger = Logger.getLogger(getClass().getName());

	@java.lang.Override
	protected void configure() {

		logger.finest("Install JpaServiceModule begin");
		bind(ResourceService.class).to(JpaResourceService.class);
		bind(CategoryService.class).to(JpaCategoryService.class);
		bind(UserService.class).to(JpaUserService.class);
		bind(GroupService.class).to(JpaGroupService.class);
		bind(WebContentService.class).to(JpaWebContentService.class);

		install(new JpaPersistModule("persist.jpaUnit")); // TODO read from
															// config;

		requestStaticInjection(InjectionListener.class);

		MethodInterceptor finderInterceptor = new JpaFinderProxy();
		requestInjection(finderInterceptor);
		bindInterceptor(any(), annotatedWith(Finder.class), finderInterceptor);
		logger.finest("Install JpaServiceModule end");
	}
}