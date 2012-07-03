package com.goodow.web.core.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public final class Shell extends Composite {

  interface ShellBinder extends UiBinder<Widget, Shell> {
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  private static ShellBinder uiBinder = GWT.create(ShellBinder.class);

  @UiField
  View root;

  private final Provider<View> provider;

  @Inject
  public Shell(final Provider<View> provider) {
    this.provider = provider;
    initWidget(uiBinder.createAndBindUi(this));
    logger.finest("Shell is initialized.");
  }

  @UiFactory
  View createPanel() {
    View panel = provider.get();
    return panel;
  }
}
