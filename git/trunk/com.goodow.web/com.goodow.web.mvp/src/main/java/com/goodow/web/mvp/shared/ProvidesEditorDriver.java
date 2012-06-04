package com.goodow.web.mvp.shared;

import com.goodow.web.mvp.shared.rpc.BaseEditor;
import com.goodow.web.service.shared.rpc.BaseEntityProxy;

import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;


public interface ProvidesEditorDriver<T extends BaseEntityProxy> {
  // <E extends BaseEditor<? super T>> RequestFactoryEditorDriver<T, E> provideEditorDriver();
  <E extends BaseEditor<? super T>> RequestFactoryEditorDriver<T, E> provideEditorDriver();
}
