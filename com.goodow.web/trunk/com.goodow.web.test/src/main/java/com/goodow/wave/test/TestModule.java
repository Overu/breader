/*
 * Copyright 2012 Goodow.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.goodow.wave.test;

import com.google.appengine.api.rdbms.dev.LocalRdbmsService.ServerType;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalRdbmsServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.testing.TearDown;
import com.google.common.testing.TearDownAccepter;
import com.google.guiceberry.GuiceBerryEnvMain;
import com.google.guiceberry.TestWrapper;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.UnitOfWork;

import java.net.URL;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public final class TestModule extends AbstractModule {

  private static final class PersistServiceStarter implements GuiceBerryEnvMain {

    @Inject
    private LocalServiceTestHelper helper;
    @Inject
    private PersistService persistService;

    @Override
    public void run() {

      helper.setUp();
      // Starting a server should never be done in a @Provides method
      // (or inside Provider's get).
      persistService.start();
      helper.tearDown();
    }
  }

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configure() {
    LogManager logManager = LogManager.getLogManager();
    try {
      URL url = LoggingUtil.searchLoggingFile();
      logManager.readConfiguration(url.openStream());
      logger.config("Config logging use " + url);
    } catch (Exception e) {
      System.err.println("TestingModule: Load logging configuration failed");
      System.err.println("" + e);
    }
    // super.configure();

    bind(GuiceBerryEnvMain.class).to(PersistServiceStarter.class);
  }

  @Provides
  @Singleton
  LocalServiceTestHelper localServiceTestHelperProvider() {
    LocalRdbmsServiceTestConfig localRdbmsServiceTestConfig = new LocalRdbmsServiceTestConfig();
    localRdbmsServiceTestConfig.setServerType(ServerType.HOSTED);
    LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig(),
            localRdbmsServiceTestConfig);
    return helper;
  }

  @Provides
  TestWrapper testWrapperProvider(final TearDownAccepter tearDownAccepter,
      final LocalServiceTestHelper helper, final UnitOfWork unitOfWork) {

    return new TestWrapper() {

      @Override
      public void toRunBeforeTest() {
        tearDownAccepter.addTearDown(new TearDown() {

          @Override
          public void tearDown() throws Exception {
            unitOfWork.end();
            helper.tearDown();
          }
        });
        helper.setUp();
        unitOfWork.begin();
      }
    };
  }
}
