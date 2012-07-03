package com.goodow.web.core.shared;

import com.google.gwt.place.shared.Place;

import com.googlecode.mgwt.mvp.client.Animation;

public class MyPlace extends Place {

  private String uri;

  private Animation animation;

  private String title;

  public MyPlace(final Animation animation, final String title, final String uri) {
    this.animation = animation;
    this.title = title;
    this.uri = uri;
  }

  @Override
  public boolean equals(final Object other) {

    if (other == this) {
      return true;
    }
    if (other instanceof MyPlace) {
      MyPlace that = (MyPlace) other;
      return this.uri.equals(that.uri);
    }
    return false;
  }

  /**
   * @return the animation
   */
  public Animation getAnimation() {
    return animation;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @return the uri
   */
  public String getUri() {
    return uri;
  }

  @Override
  public int hashCode() {
    return 3;
  }

  /**
   * @param animation the animation to set
   */
  public void setAnimation(final Animation animation) {
    this.animation = animation;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(final String title) {
    this.title = title;
  }

  /**
   * @param uri the uri to set
   */
  public void setUri(final String uri) {
    this.uri = uri;
  }
}
