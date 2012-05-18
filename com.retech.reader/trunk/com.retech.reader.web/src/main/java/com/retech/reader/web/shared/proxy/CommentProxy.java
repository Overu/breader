package com.retech.reader.web.shared.proxy;

import com.goodow.wave.server.requestfactory.RequestFactoryLocator;

import com.google.web.bindery.requestfactory.shared.ProxyFor;

import com.retech.reader.web.server.domain.Comment;

import org.cloudlet.web.service.shared.rpc.BaseEntityProxy;

import java.util.Date;

@ProxyFor(value = Comment.class, locator = RequestFactoryLocator.class)
public interface CommentProxy extends BaseEntityProxy {

  Date getDate();

  String getMac();

  String getText();

}
