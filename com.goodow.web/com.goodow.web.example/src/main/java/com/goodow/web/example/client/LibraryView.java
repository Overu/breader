package com.goodow.web.example.client;

import com.goodow.web.layout.client.ui.View;
import com.goodow.web.layout.client.ui.WidgetRegistry;
import com.google.inject.Inject;
import com.google.inject.Provider;


public class LibraryView extends View {

  @Inject
  public LibraryView(WidgetRegistry registry, Provider<View> view) {

    super(registry);

    setLabel("图书馆");

    View view1 = view.get();
    view1.setLabel("同步视图");
    view1.setWidgetId("view1");

    View view2 = view.get();
    view2.setLabel("异步视图");
    view2.setWidgetId("view2");

    add(view1);
    add(view2);
  }

}
