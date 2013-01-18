package com.goodow.web.core.shared;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class WebPlatform {

	@Inject
	private static WebPlatform instance;

//	@Inject
//	private Injector injector;
//	
//	public Injector getInjector() {
//		return injector;
//	}

	public static WebPlatform getInstance() {
		return instance;
	}

	Map<String, Package> packages = new HashMap<String, Package>();

	public ObjectType getObjectType(final String fullName) {
		WebType type = getType(fullName);
		return (ObjectType) type;
	}

	public Operation getOperation(final String fullName) {
		int index = fullName.lastIndexOf(".");
		String typeName = fullName.substring(0, index);
		ObjectType type = getObjectType(typeName);
		if (type != null) {
			String simpleName = fullName.substring(index + 1);
			return type.getOperation(simpleName);
		}
		return null;

	}

	public Package getPackage(final String name) {
		return packages.get(name);
	}

	public Map<String, Package> getPackages() {
		return packages;
	}

	public WebType getType(final String fullName) {
		int index = fullName.lastIndexOf(".");
		String pkgName = fullName.substring(0, index);
		Package pkg = getPackage(pkgName);
		if (pkg != null) {
			String simpleName = fullName.substring(index + 1);
			return pkg.getType(simpleName);
		}
		return null;
	}

}
