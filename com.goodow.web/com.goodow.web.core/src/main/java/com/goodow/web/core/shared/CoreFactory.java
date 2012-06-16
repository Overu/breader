package com.goodow.web.core.shared;

import com.google.inject.*;

@Singleton
public class CoreFactory extends Factory {
  
  @Inject
  public static Provider<Property> Property;
  
  @Inject
  public static Provider<ValueType> ValueType;
  
  @Inject
  public static Provider<Type> Type;
  
  @Inject
  public static Provider<Parameter> Parameter;
  
  @Inject
  public static Provider<Factory> Factory;
  
  @Inject
  public static Provider<Package> Package;
  
  @Inject
  public static Provider<EntityType> EntityType;
  
  @Inject
  public static Provider<Operation> Operation;
  
  @Inject
  public static Provider<User> User;
  
  @Inject
  public static Provider<Group> Group;
  
  @Inject
  public static Provider<Content> Content;
}