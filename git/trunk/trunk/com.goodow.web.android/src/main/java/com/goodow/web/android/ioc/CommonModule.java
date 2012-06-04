package com.goodow.web.android.ioc;

import com.goodow.web.android.persist.DatabasePersistor;
import com.goodow.web.android.persist.Persistor;

import roboguice.config.AbstractAndroidModule;

public class CommonModule extends AbstractAndroidModule {

  @Override
  protected void configure() {
    bind(Persistor.class).to(DatabasePersistor.class);
  }

}
