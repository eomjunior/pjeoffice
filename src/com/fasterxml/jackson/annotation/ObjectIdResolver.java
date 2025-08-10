package com.fasterxml.jackson.annotation;

public interface ObjectIdResolver {
  void bindItem(ObjectIdGenerator.IdKey paramIdKey, Object paramObject);
  
  Object resolveId(ObjectIdGenerator.IdKey paramIdKey);
  
  ObjectIdResolver newForDeserialization(Object paramObject);
  
  boolean canUseFor(ObjectIdResolver paramObjectIdResolver);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/annotation/ObjectIdResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */