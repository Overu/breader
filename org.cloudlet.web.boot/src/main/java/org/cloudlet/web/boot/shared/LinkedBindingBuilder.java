package org.cloudlet.web.boot.shared;

import com.google.gwt.inject.client.AsyncProvider;

public interface LinkedBindingBuilder<T> {
  // void to(Class<? extends T> implementation);

  void toAsyncProvider(AsyncProvider<? extends T> provider);

  void toInstance(T instance);
}
