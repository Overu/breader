package com.goodow.web.service.shared;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;

@ProxyFor(value = SortPredicateProxy.class)
public interface SortPredicateProxy extends ValueProxy {

  /**
   * SortDirection controls the order of a sort.
   */
  public enum SortDirection {
    ASC, DESC
  }

  String SORT = " order by :sort ";

  /**
   * Gets the direction of the sort.
   */
  public SortDirection getDirection();

  /**
   * Gets the name of the property to sort on.
   */
  public String getPropertyName();

}
