package org.cloudlet.web.dev.rebind;

import com.google.gwt.core.ext.linker.Shardable;

/**
 * A custom linker that generates an app cache manifest with the files generated by the GWT compiler
 * and the static files used by this application.
 * <p>
 * Before using this approach with production code be sure that you understand the limitations of
 * {@link BaseAppCacheLinker}, namely that it sends all permutations to the client.
 * 
 * @see BaseAppCacheLinker
 */
@Shardable
public class AppCacheLinker extends BaseAppCacheLinker {
  @Override
  protected String[] otherCachedFiles() {
    return new String[] {"/index.html", "/favicon.ico", "/icon.png", "/cordova-1.5.0.js",};
  }
}