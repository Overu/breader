package com.goodow.web.ui.client.activities;

import com.goodow.web.ui.client.places.MyPlace;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;

public class AnimationSelectedEvent extends Event<AnimationSelectedEvent.Handler> {

  public interface Handler {
    void onAnimationSelected(AnimationSelectedEvent event);
  }

  private static final Type<AnimationSelectedEvent.Handler> TYPE =
      new Type<AnimationSelectedEvent.Handler>();

  public static void fire(final EventBus eventBus, final MyPlace place) {
    eventBus.fireEvent(new AnimationSelectedEvent(place));
  }

  public static Type<AnimationSelectedEvent.Handler> getType() {
    return TYPE;
  }

  private final MyPlace animation;

  protected AnimationSelectedEvent(final MyPlace animation) {
    this.animation = animation;

  }

  public MyPlace getAnimation() {
    return animation;
  }

  @Override
  public com.google.web.bindery.event.shared.Event.Type<Handler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(final Handler handler) {
    handler.onAnimationSelected(this);

  }

}
