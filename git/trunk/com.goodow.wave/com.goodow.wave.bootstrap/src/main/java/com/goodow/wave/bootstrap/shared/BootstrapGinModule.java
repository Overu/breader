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
package com.goodow.wave.bootstrap.shared;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

import java.util.logging.Logger;

public final class BootstrapGinModule extends AbstractGinModule {
  private static final Logger logger = Logger.getLogger(BootstrapGinModule.class.getName());

  @Override
  protected void configure() {
    logger.finest("config start");
    bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
  }
}
