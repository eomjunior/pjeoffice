package com.fasterxml.jackson.databind.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.util.Converter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonDeserialize {
  Class<? extends JsonDeserializer> using() default JsonDeserializer.None.class;
  
  Class<? extends JsonDeserializer> contentUsing() default JsonDeserializer.None.class;
  
  Class<? extends KeyDeserializer> keyUsing() default KeyDeserializer.None.class;
  
  Class<?> builder() default Void.class;
  
  Class<? extends Converter> converter() default Converter.None.class;
  
  Class<? extends Converter> contentConverter() default Converter.None.class;
  
  Class<?> as() default Void.class;
  
  Class<?> keyAs() default Void.class;
  
  Class<?> contentAs() default Void.class;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/annotation/JsonDeserialize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */