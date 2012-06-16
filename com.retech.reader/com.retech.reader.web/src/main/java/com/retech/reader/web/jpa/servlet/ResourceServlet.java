package com.retech.reader.web.jpa.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import com.retech.reader.web.jpa.JpaResourceService;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class ResourceServlet extends HttpServlet {

	/**
	 * service
	 */
	private final JpaResourceService resourceService;

	@Inject
	ResourceServlet(final JpaResourceService resourceService) {
		this.resourceService = resourceService;
	}

	@Override
	protected void doGet(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException,
			IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		resp.setContentType("text/html; charset=utf-8");
		PrintWriter writer = resp.getWriter();
		writer.print(resourceService.getDataString(Long.parseLong(req
				.getParameter("id"))));
		writer.flush();
	}

}
