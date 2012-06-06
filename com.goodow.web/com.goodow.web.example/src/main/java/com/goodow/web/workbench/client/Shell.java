package com.goodow.web.workbench.client;

import java.util.logging.Logger;

import com.goodow.web.layout.client.ui.FlexPanel;
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
  FlexPanel root;

  private final Provider<FlexPanel> provider;

  @Inject
  public Shell(final Provider<FlexPanel> provider) {
    this.provider = provider;
    initWidget(uiBinder.createAndBindUi(this));
    logger.finest("Shell is initialized.");
  }

  @UiFactory
  FlexPanel createPanel() {
    FlexPanel panel = provider.get();
    return panel;
  }
}
