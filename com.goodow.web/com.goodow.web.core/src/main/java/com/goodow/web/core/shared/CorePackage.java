package com.goodow.web.core.shared;


@com.google.inject.Singleton
public class CorePackage extends BaseCorePackage {
  public static class PropertyAccessor<T extends Property> extends com.goodow.web.core.shared.CorePackage.TypedElementAccessor<T> {
    @java.lang.Override
    public Object getProperty(T entity, Property property) {
      if ("declaringType".equals(property.getName())) {
        return entity.getDeclaringType();
      } else if ("containment".equals(property.getName())) {
        return entity.isContainment();
      } else {
        return super.getProperty(entity, property);
      }
    }
    @java.lang.Override
    public void setProperty(T entity, Property property, Object value) {
      if ("declaringType".equals(property.getName())) {
        entity.setDeclaringType((EntityType) value);
      } else if ("containment".equals(property.getName())) {
        entity.setContainment((java.lang.Boolean) value);
      } else {
        super.setProperty(entity, property, value);
      }
    }
  }
  public static class ValueTypeAccessor<T extends ValueType> extends com.goodow.web.core.shared.CorePackage.TypeAccessor<T> {
    @java.lang.Override
    public Object getProperty(T entity, Property property) {
      return super.getProperty(entity, property);
    }
    @java.lang.Override
    public void setProperty(T entity, Property property, Object value) {
      super.setProperty(entity, property, value);
    }
  }
  public static class TypeAccessor<T extends Type> extends com.goodow.web.core.shared.CorePackage.NamedElementAccessor<T> {
    @java.lang.Override
    public Object getProperty(T entity, Property property) {
      if ("package".equals(property.getName())) {
        return entity.getPackage();
      } else {
        return super.getProperty(entity, property);
      }
    }
    @java.lang.Override
    public void setProperty(T entity, Property property, Object value) {
      if ("package".equals(property.getName())) {
        entity.setPackage((com.goodow.web.core.shared.Package) value);
      } else {
        super.setProperty(entity, property, value);
      }
    }
  }
  public static class ParameterAccessor<T extends Parameter> extends com.goodow.web.core.shared.CorePackage.TypedElementAccessor<T> {
    @java.lang.Override
    public Object getProperty(T entity, Property property) {
      if ("operation".equals(property.getName())) {
        return entity.getOperation();
      } else {
        return super.getProperty(entity, property);
      }
    }
    @java.lang.Override
    public void setProperty(T entity, Property property, Object value) {
      if ("operation".equals(property.getName())) {
        entity.setOperation((Operation) value);
      } else {
        super.setProperty(entity, property, value);
      }
    }
  }
  public static class EntityAccessor<T extends Entity> implements Accessor<T> {
    @java.lang.Override
    public Object getProperty(T entity, Property property) {
      if ("id".equals(property.getName())) {
        return entity.getId();
      } else if ("version".equals(property.getName())) {
        return entity.getVersion();
      } else {
        return null;
      }
    }
    @java.lang.Override
    public void setProperty(T entity, Property property, Object value) {
      if ("id".equals(property.getName())) {
        entity.setId((java.lang.String) value);
      } else if ("version".equals(property.getName())) {
        entity.setVersion((java.lang.Long) value);
      } else {
        
      }
    }
  }
  public static class FactoryAccessor<T extends Factory> extends com.goodow.web.core.shared.CorePackage.NamedElementAccessor<T> {
    @java.lang.Override
    public Object getProperty(T entity, Property property) {
      return super.getProperty(entity, property);
    }
    @java.lang.Override
    public void setProperty(T entity, Property property, Object value) {
      super.setProperty(entity, property, value);
    }
  }
  public static class PackageAccessor<T extends com.goodow.web.core.shared.Package> extends com.goodow.web.core.shared.CorePackage.NamedElementAccessor<T> {
    @java.lang.Override
    public Object getProperty(T entity, Property property) {
      return super.getProperty(entity, property);
    }
    @java.lang.Override
    public void setProperty(T entity, Property property, Object value) {
      super.setProperty(entity, property, value);
    }
  }
  public static class NamedElementAccessor<T extends NamedElement> extends com.goodow.web.core.shared.CorePackage.ContentAccessor<T> {
    @java.lang.Override
    public Object getProperty(T entity, Property property) {
      if ("name".equals(property.getName())) {
        return entity.getName();
      } else {
        return super.getProperty(entity, property);
      }
    }
    @java.lang.Override
    public void setProperty(T entity, Property property, Object value) {
      if ("name".equals(property.getName())) {
        entity.setName((java.lang.String) value);
      } else {
        super.setProperty(entity, property, value);
      }
    }
  }
  public static class EntityTypeAccessor<T extends EntityType> extends com.goodow.web.core.shared.CorePackage.TypeAccessor<T> {
    @java.lang.Override
    public Object getProperty(T entity, Property property) {
      if ("abstract".equals(property.getName())) {
        return entity.isAbstract();
      } else if ("superType".equals(property.getName())) {
        return entity.getSuperType();
      } else {
        return super.getProperty(entity, property);
      }
    }
    @java.lang.Override
    public void setProperty(T entity, Property property, Object value) {
      if ("abstract".equals(property.getName())) {
        entity.setAbstract((java.lang.Boolean) value);
      } else if ("superType".equals(property.getName())) {
        entity.setSuperType((EntityType) value);
      } else {
        super.setProperty(entity, property, value);
      }
    }
  }
  public static class OperationAccessor<T extends Operation> extends com.goodow.web.core.shared.CorePackage.TypedElementAccessor<T> {
    @java.lang.Override
    public Object getProperty(T entity, Property property) {
      if ("path".equals(property.getName())) {
        return entity.getPath();
      } else if ("httpMethod".equals(property.getName())) {
        return entity.getHttpMethod();
      } else if ("declaringType".equals(property.getName())) {
        return entity.getDeclaringType();
      } else {
        return super.getProperty(entity, property);
      }
    }
    @java.lang.Override
    public void setProperty(T entity, Property property, Object value) {
      if ("path".equals(property.getName())) {
        entity.setPath((java.lang.String) value);
      } else if ("httpMethod".equals(property.getName())) {
        entity.setHttpMethod((HttpMethod) value);
      } else if ("declaringType".equals(property.getName())) {
        entity.setDeclaringType((EntityType) value);
      } else {
        super.setProperty(entity, property, value);
      }
    }
  }
  public static class TypedElementAccessor<T extends TypedElement> extends com.goodow.web.core.shared.CorePackage.NamedElementAccessor<T> {
    @java.lang.Override
    public Object getProperty(T entity, Property property) {
      if ("type".equals(property.getName())) {
        return entity.getType();
      } else if ("many".equals(property.getName())) {
        return entity.isMany();
      } else {
        return super.getProperty(entity, property);
      }
    }
    @java.lang.Override
    public void setProperty(T entity, Property property, Object value) {
      if ("type".equals(property.getName())) {
        entity.setType((Type) value);
      } else if ("many".equals(property.getName())) {
        entity.setMany((java.lang.Boolean) value);
      } else {
        super.setProperty(entity, property, value);
      }
    }
  }
  public static class UserAccessor<T extends User> extends com.goodow.web.core.shared.CorePackage.ContentAccessor<T> {
    @java.lang.Override
    public Object getProperty(T entity, Property property) {
      if ("userName".equals(property.getName())) {
        return entity.getUserName();
      } else if ("password".equals(property.getName())) {
        return entity.getPassword();
      } else if ("passwordSalt".equals(property.getName())) {
        return entity.getPasswordSalt();
      } else {
        return super.getProperty(entity, property);
      }
    }
    @java.lang.Override
    public void setProperty(T entity, Property property, Object value) {
      if ("userName".equals(property.getName())) {
        entity.setUserName((java.lang.String) value);
      } else if ("password".equals(property.getName())) {
        entity.setPassword((java.lang.String) value);
      } else if ("passwordSalt".equals(property.getName())) {
        entity.setPasswordSalt((java.lang.String) value);
      } else {
        super.setProperty(entity, property, value);
      }
    }
  }
  public static class GroupAccessor<T extends Group> extends com.goodow.web.core.shared.CorePackage.ContentAccessor<T> {
    @java.lang.Override
    public Object getProperty(T entity, Property property) {
      if ("name".equals(property.getName())) {
        return entity.getName();
      } else {
        return super.getProperty(entity, property);
      }
    }
    @java.lang.Override
    public void setProperty(T entity, Property property, Object value) {
      if ("name".equals(property.getName())) {
        entity.setName((java.lang.String) value);
      } else {
        super.setProperty(entity, property, value);
      }
    }
  }
  public static class ContentAccessor<T extends Content> extends com.goodow.web.core.shared.CorePackage.EntityAccessor<T> {
    @java.lang.Override
    public Object getProperty(T entity, Property property) {
      if ("owner".equals(property.getName())) {
        return entity.getOwner();
      } else if ("ownerGroup".equals(property.getName())) {
        return entity.getOwnerGroup();
      } else {
        return super.getProperty(entity, property);
      }
    }
    @java.lang.Override
    public void setProperty(T entity, Property property, Object value) {
      if ("owner".equals(property.getName())) {
        entity.setOwner((User) value);
      } else if ("ownerGroup".equals(property.getName())) {
        entity.setOwnerGroup((Group) value);
      } else {
        super.setProperty(entity, property, value);
      }
    }
  }
  public static final EntityInfo Property = new EntityInfo(Property.class, CoreFactory.Property, null);
  public static final EntityInfo ValueType = new EntityInfo(ValueType.class, CoreFactory.ValueType, null);
  public static final EntityInfo Type = new EntityInfo(Type.class, CoreFactory.Type, null);
  public static final EntityInfo Parameter = new EntityInfo(Parameter.class, CoreFactory.Parameter, null);
  public static final EntityInfo Entity = new EntityInfo(Entity.class, null, Service.class);
  public static final EntityInfo Factory = new EntityInfo(Factory.class, CoreFactory.Factory, null);
  public static final EntityInfo Package = new EntityInfo(com.goodow.web.core.shared.Package.class, CoreFactory.Package, null);
  public static final EntityInfo NamedElement = new EntityInfo(NamedElement.class, null, null);
  public static final EntityInfo EntityType = new EntityInfo(EntityType.class, CoreFactory.EntityType, null);
  public static final EntityInfo Operation = new EntityInfo(Operation.class, CoreFactory.Operation, null);
  public static final EntityInfo TypedElement = new EntityInfo(TypedElement.class, null, null);
  public static final EntityInfo User = new EntityInfo(User.class, CoreFactory.User, UserService.class);
  public static final EntityInfo Group = new EntityInfo(Group.class, CoreFactory.Group, null);
  public static final EntityInfo Content = new EntityInfo(Content.class, CoreFactory.Content, ContentService.class);
  public static final EnumInfo<GroupPermission> GroupPermission = new EnumInfo<GroupPermission>(GroupPermission.class);
  public static final EnumInfo<HttpStatus> HttpStatus = new EnumInfo<HttpStatus>(HttpStatus.class);
  public static final EnumInfo<ContentPermission> ContentPermission = new EnumInfo<ContentPermission>(ContentPermission.class);
  public static final EnumInfo<MemberPermission> MemberPermission = new EnumInfo<MemberPermission>(MemberPermission.class);
  public static final EnumInfo<CoreRole> CoreRole = new EnumInfo<CoreRole>(CoreRole.class);
  public static final EnumInfo<HttpMethod> HttpMethod = new EnumInfo<HttpMethod>(HttpMethod.class);
  public static final OperationInfo<User> UserService_findUserByUsername = new OperationInfo<User>("findUserByUsername");
  public static final OperationInfo<java.lang.Void> UserService_updatePassword = new OperationInfo<java.lang.Void>("updatePassword");
  public static final OperationInfo<java.lang.Void> ContentService_put = new OperationInfo<java.lang.Void>("put");
  public static final OperationInfo<java.lang.Void> ContentService_remove = new OperationInfo<java.lang.Void>("remove");
  public CorePackage() {
    setName("com.goodow.web.core.shared");
    addEntityTypes(Property.as(), ValueType.as(), Type.as(), Parameter.as(), Entity.as(), Factory.as(), Package.as(), NamedElement.as(), EntityType.as(), Operation.as(), TypedElement.as(), User.as(), Group.as(), Content.as());
    addValueTypes(BOOLEAN.as(), Boolean.as(), INT.as(), Integer.as(), LONG.as(), Long.as(), String.as(), Role.as(), GroupPermission.as(), HttpStatus.as(), ContentPermission.as(), MemberPermission.as(), CoreRole.as(), HttpMethod.as());
    Property.as().setSuperType(com.goodow.web.core.shared.CorePackage.TypedElement.as());
    Property.as().setAccessor(new PropertyAccessor());
    addProperty(Property.as(), "declaringType", CorePackage.EntityType.as(), false);
    addProperty(Property.as(), "containment", CorePackage.BOOLEAN.as(), false);
    
    ValueType.as().setSuperType(com.goodow.web.core.shared.CorePackage.Type.as());
    ValueType.as().setAccessor(new ValueTypeAccessor());
    
    Type.as().setSuperType(com.goodow.web.core.shared.CorePackage.NamedElement.as());
    Type.as().setAccessor(new TypeAccessor());
    addProperty(Type.as(), "_package", CorePackage.Package.as(), false);
    
    Parameter.as().setSuperType(com.goodow.web.core.shared.CorePackage.TypedElement.as());
    Parameter.as().setAccessor(new ParameterAccessor());
    addProperty(Parameter.as(), "operation", CorePackage.Operation.as(), false);
    
    Entity.as().setAccessor(new EntityAccessor());
    addProperty(Entity.as(), "id", CorePackage.String.as(), false);
    addProperty(Entity.as(), "version", CorePackage.Long.as(), false);
    
    Factory.as().setSuperType(com.goodow.web.core.shared.CorePackage.NamedElement.as());
    Factory.as().setAccessor(new FactoryAccessor());
    
    Package.as().setSuperType(com.goodow.web.core.shared.CorePackage.NamedElement.as());
    Package.as().setAccessor(new PackageAccessor());
    
    NamedElement.as().setSuperType(com.goodow.web.core.shared.CorePackage.Content.as());
    NamedElement.as().setAccessor(new NamedElementAccessor());
    addProperty(NamedElement.as(), "name", CorePackage.String.as(), false);
    
    EntityType.as().setSuperType(com.goodow.web.core.shared.CorePackage.Type.as());
    EntityType.as().setAccessor(new EntityTypeAccessor());
    addProperty(EntityType.as(), "_abstract", CorePackage.BOOLEAN.as(), false);
    addProperty(EntityType.as(), "superType", CorePackage.EntityType.as(), false);
    
    Operation.as().setSuperType(com.goodow.web.core.shared.CorePackage.TypedElement.as());
    Operation.as().setAccessor(new OperationAccessor());
    addProperty(Operation.as(), "path", CorePackage.String.as(), false);
    addProperty(Operation.as(), "httpMethod", CorePackage.HttpMethod.as(), false);
    addProperty(Operation.as(), "declaringType", CorePackage.EntityType.as(), false);
    
    TypedElement.as().setSuperType(com.goodow.web.core.shared.CorePackage.NamedElement.as());
    TypedElement.as().setAccessor(new TypedElementAccessor());
    addProperty(TypedElement.as(), "type", CorePackage.Type.as(), false);
    addProperty(TypedElement.as(), "many", CorePackage.BOOLEAN.as(), false);
    
    User.as().setSuperType(com.goodow.web.core.shared.CorePackage.Content.as());
    User.as().setAccessor(new UserAccessor());
    addProperty(User.as(), "userName", CorePackage.String.as(), false);
    addProperty(User.as(), "password", CorePackage.String.as(), false);
    addProperty(User.as(), "passwordSalt", CorePackage.String.as(), false);
    
    addOperations(User.as(), UserService_findUserByUsername, UserService_updatePassword);
    addParameter(UserService_findUserByUsername.as(), "userName", CorePackage.String.as());
    
    addParameter(UserService_updatePassword.as(), "userName", CorePackage.String.as());
    addParameter(UserService_updatePassword.as(), "newPwd", CorePackage.String.as());
    
    Group.as().setSuperType(com.goodow.web.core.shared.CorePackage.Content.as());
    Group.as().setAccessor(new GroupAccessor());
    addProperty(Group.as(), "name", CorePackage.String.as(), false);
    
    Content.as().setSuperType(com.goodow.web.core.shared.CorePackage.Entity.as());
    Content.as().setAccessor(new ContentAccessor());
    addProperty(Content.as(), "owner", CorePackage.User.as(), false);
    addProperty(Content.as(), "ownerGroup", CorePackage.Group.as(), false);
    
    addOperations(Content.as(), ContentService_put, ContentService_remove);
    addParameter(ContentService_put.as(), "content", null);
    
    addParameter(ContentService_remove.as(), "content", null);
    
  }
}
