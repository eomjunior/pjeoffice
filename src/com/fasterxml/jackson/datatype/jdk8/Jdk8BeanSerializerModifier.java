/*    */ package com.fasterxml.jackson.datatype.jdk8;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.BeanDescription;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.SerializationConfig;
/*    */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*    */ import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalDouble;
/*    */ import java.util.OptionalInt;
/*    */ import java.util.OptionalLong;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Jdk8BeanSerializerModifier
/*    */   extends BeanSerializerModifier
/*    */ {
/*    */   public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
/* 26 */     for (int i = 0; i < beanProperties.size(); i++) {
/* 27 */       Object empty; BeanPropertyWriter writer = beanProperties.get(i);
/* 28 */       JavaType type = writer.getType();
/*    */ 
/*    */       
/* 31 */       if (type.isTypeOrSubTypeOf(Optional.class)) {
/* 32 */         empty = Optional.empty();
/* 33 */       } else if (type.hasRawClass(OptionalLong.class)) {
/* 34 */         empty = OptionalLong.empty();
/* 35 */       } else if (type.hasRawClass(OptionalInt.class)) {
/* 36 */         empty = OptionalInt.empty();
/* 37 */       } else if (type.hasRawClass(OptionalDouble.class)) {
/* 38 */         empty = OptionalDouble.empty();
/*    */       } else {
/*    */         continue;
/*    */       } 
/* 42 */       beanProperties.set(i, new Jdk8OptionalBeanPropertyWriter(writer, empty)); continue;
/*    */     } 
/* 44 */     return beanProperties;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/Jdk8BeanSerializerModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */