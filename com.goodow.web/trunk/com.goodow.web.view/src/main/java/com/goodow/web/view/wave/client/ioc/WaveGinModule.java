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
package com.goodow.web.view.wave.client.ioc;

import com.goodow.web.view.wave.client.shell.WaveShell;
import com.goodow.web.view.wave.client.shell.WaveShellResources.CellListResources;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.user.cellview.client.CellList;
import com.google.inject.Singleton;

import java.util.logging.Logger;

public final class WaveGinModule extends AbstractGinModule {

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  protected void configure() {
    bind(CellList.Resources.class).to(CellListResources.class).in(Singleton.class);
    bind(WaveShell.class).asEagerSingleton();
  }

}
