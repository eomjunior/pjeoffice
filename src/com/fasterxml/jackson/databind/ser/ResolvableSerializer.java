package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;

public interface ResolvableSerializer {
  void resolve(SerializerProvider paramSerializerProvider) throws JsonMappingException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/ser/ResolvableSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */