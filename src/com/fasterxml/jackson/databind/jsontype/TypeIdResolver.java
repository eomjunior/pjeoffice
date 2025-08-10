package com.fasterxml.jackson.databind.jsontype;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import java.io.IOException;

public interface TypeIdResolver {
  void init(JavaType paramJavaType);
  
  String idFromValue(Object paramObject);
  
  String idFromValueAndType(Object paramObject, Class<?> paramClass);
  
  String idFromBaseType();
  
  JavaType typeFromId(DatabindContext paramDatabindContext, String paramString) throws IOException;
  
  String getDescForKnownTypeIds();
  
  JsonTypeInfo.Id getMechanism();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/jsontype/TypeIdResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */