package com.goodow.web.core.apt;

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.type.WildcardType;

public class WebVistor implements TypeVisitor<Void, Void> {

  @Override
  public Void visit(final TypeMirror t) {
    System.out.println("visit 1");
    return null;
  }

  @Override
  public Void visit(final TypeMirror t, final Void p) {
    System.out.println("visit 2");
    return null;
  }

  @Override
  public Void visitArray(final ArrayType t, final Void p) {
    System.out.println("visit 3");
    return null;
  }

  @Override
  public Void visitDeclared(final DeclaredType t, final Void p) {
    System.out.println("visit 4");
    return null;
  }

  @Override
  public Void visitError(final ErrorType t, final Void p) {
    System.out.println("visit 5");
    return null;
  }

  @Override
  public Void visitExecutable(final ExecutableType t, final Void p) {
    System.out.println("visit 6");
    return null;
  }

  @Override
  public Void visitNoType(final NoType t, final Void p) {
    System.out.println("visit 7");
    return null;
  }

  @Override
  public Void visitNull(final NullType t, final Void p) {
    System.out.println("visit 8");
    return null;
  }

  @Override
  public Void visitPrimitive(final PrimitiveType t, final Void p) {
    System.out.println("visit 9");
    return null;
  }

  @Override
  public Void visitTypeVariable(final TypeVariable t, final Void p) {
    System.out.println("visit 10");
    return null;
  }

  @Override
  public Void visitUnknown(final TypeMirror t, final Void p) {
    System.out.println("visit 11");
    return null;
  }

  @Override
  public Void visitWildcard(final WildcardType t, final Void p) {
    System.out.println("visit 12");
    return null;
  }

}
