package com.retech.reader.web.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

import com.retech.reader.web.server.domain.Comment;

import org.cloudlet.web.service.server.NoLocator;
import org.cloudlet.web.service.shared.rpc.BaseEntityProxy;

import java.util.Date;

@ProxyFor(value = Comment.class, locator = NoLocator.class)
public interface CommentProxy extends BaseEntityProxy {

  Date getDate();

  String getMac();

  String getText();

}
