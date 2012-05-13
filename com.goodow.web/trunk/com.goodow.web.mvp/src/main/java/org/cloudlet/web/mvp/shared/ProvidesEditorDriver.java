package org.cloudlet.web.mvp.shared;

import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;

import org.cloudlet.web.mvp.shared.rpc.BaseEditor;
import org.cloudlet.web.service.shared.rpc.BaseEntityProxy;

public interface ProvidesEditorDriver<T extends BaseEntityProxy> {
  // <E extends BaseEditor<? super T>> RequestFactoryEditorDriver<T, E> provideEditorDriver();
  <E extends BaseEditor<? super T>> RequestFactoryEditorDriver<T, E> provideEditorDriver();
}
