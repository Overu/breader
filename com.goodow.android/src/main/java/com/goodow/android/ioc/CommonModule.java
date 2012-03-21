package com.goodow.android.ioc;

import com.goodow.android.persist.DatabasePersistor;
import com.goodow.android.persist.Persistor;

import roboguice.config.AbstractAndroidModule;

public class CommonModule extends AbstractAndroidModule {

  @Override
  protected void configure() {
    bind(Persistor.class).to(DatabasePersistor.class);
  }

}
