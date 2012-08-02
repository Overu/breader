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
package com.goodow.web.reader.client;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class HeadTypeEvent extends GwtEvent<HeadTypeEvent.HeadTypeHandle> {

  public enum HeadType {
    MOUSEOVER, MOUSEOUT, CLICK
  }

  public interface HeadTypeHandle extends EventHandler {
    public void onHeadTypeHandle(HeadTypeEvent headTypeEvent);
  }

  public static final Type<HeadTypeEvent.HeadTypeHandle> TYPE =
      new Type<HeadTypeEvent.HeadTypeHandle>();

  private final HeadType headType;

  private final TopBarButton topBarButton;

  public HeadTypeEvent(final HeadType headType, final TopBarButton topBarButton) {
    this.headType = headType;
    this.topBarButton = topBarButton;
  }

  @Override
  public Type<HeadTypeHandle> getAssociatedType() {
    return TYPE;
  }

  public HeadType getHeadType() {
    return headType;
  }

  public TopBarButton getTopBarButton() {
    return topBarButton;
  }

  @Override
  protected void dispatch(final HeadTypeHandle handler) {
    handler.onHeadTypeHandle(this);
  }

}
