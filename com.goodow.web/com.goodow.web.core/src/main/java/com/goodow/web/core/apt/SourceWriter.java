package com.goodow.web.core.apt;

import java.io.PrintWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class SourceWriter {

  private int indentLevel = 0;
  private String indentPrefix = "";
  private boolean needsIndent;
  private final PrintWriter out;

  private Set<String> imports;

  private String className;

  public SourceWriter(final String className, final Writer writer) {
    this.className = className;
    out = new PrintWriter(writer);
    imports = new HashSet<String>();
    imports.add("java.lang.*");
    int index = className.lastIndexOf(".");
    if (index > 0) {
      String pkgName = className.substring(0, index);
      imports.add(pkgName + ".*");
      print("package ").print(pkgName).println(";");
      println();
    }
  }

  public void annotation(final Class<? extends Annotation> annotationClass) {
    print("@").type(annotationClass).println();
  }

  public SourceWriter beginJavaDocComment() {
    println("/**");
    indent();
    indentPrefix = " * ";
    return this;
  }

  public void close() {
    out.close();
  }

  public SourceWriter endJavaDocComment() {
    out.println("*/");
    outdent();
    indentPrefix = "";
    return this;
  }

  public void importClass(final Class<?> clazz) {
    importClass(clazz.getName());
  }

  public void importClass(final String className) {
    printImport(className);
  }

  public void importPackage(final Package pkg) {
    importPackage(pkg.getName());
  }

  public void importPackage(final String pkgName) {
    printImport(pkgName + ".*");
  }

  public void indent() {
    indentLevel++;
  }

  public SourceWriter indentln(final String s) {
    indent();
    println(s);
    outdent();
    return this;
  }

  public SourceWriter indentln(final String s, final Object... args) {
    indentln(String.format(s, args));
    return this;
  }

  public boolean isImported(final String qualifiedName) {
    if (imports.contains(qualifiedName)) {
      return true;
    }
    int index = qualifiedName.lastIndexOf(".");
    if (index > 0) {
      String wild = qualifiedName.substring(0, index) + ".*";
      if (imports.contains(wild)) {
        return true;
      }
    }
    return false;
  }

  public void outdent() {
    indentLevel = Math.max(indentLevel - 1, 0);
  }

  public SourceWriter print(final Object o) {
    return print(o.toString());
  }

  public SourceWriter print(final String s) {
    maybeIndent();
    out.print(s);
    return this;
  }

  public SourceWriter print(final String s, final Object... args) {
    print(String.format(s, args));
    return this;
  }

  public void printImport(final String qualifiedName) {
    if (imports.add(qualifiedName)) {
      print("import ").print(qualifiedName).println(";");
    }
  }

  public void println() {
    maybeIndent();
    // Unix-style line endings for consistent behavior across platforms.
    out.print('\n');
    needsIndent = true;
  }

  public void println(final String s) {
    print(s);
    println();
  }

  public void println(final String s, final Object... args) {
    println(String.format(s, args));
  }

  public SourceWriter type(final Class<?> clazz) {
    return type(clazz.getName());
  }

  public SourceWriter type(final String qualifiedName) {
    if (isImported(qualifiedName)) {
      int index = qualifiedName.lastIndexOf(".");
      String simpleName = index > 0 ? qualifiedName.substring(index + 1) : qualifiedName;
      try {
        Class.forName("java.lang." + simpleName);
        print(qualifiedName);
      } catch (ClassNotFoundException e) {
        print(simpleName);
      }
    } else {
      print(qualifiedName);
    }
    return this;
  }

  private void maybeIndent() {
    if (needsIndent) {
      needsIndent = false;
      for (int i = 0; i < indentLevel; i++) {
        out.print("  ");
        out.print(indentPrefix);
      }
    }
  }
}
