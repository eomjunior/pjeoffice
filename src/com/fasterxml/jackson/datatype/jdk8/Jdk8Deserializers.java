/*    */ package com.fasterxml.jackson.datatype.jdk8;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.BeanDescription;
/*    */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import com.fasterxml.jackson.databind.deser.Deserializers;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*    */ import com.fasterxml.jackson.databind.type.ReferenceType;
/*    */ import java.io.Serializable;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalDouble;
/*    */ import java.util.OptionalInt;
/*    */ import java.util.OptionalLong;
/*    */ 
/*    */ public class Jdk8Deserializers
/*    */   extends Deserializers.Base implements Serializable {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public JsonDeserializer<?> findReferenceDeserializer(ReferenceType refType, DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer contentTypeDeserializer, JsonDeserializer<?> contentDeserializer) {
/* 21 */     if (refType.hasRawClass(Optional.class)) {
/* 22 */       return (JsonDeserializer<?>)new OptionalDeserializer((JavaType)refType, null, contentTypeDeserializer, contentDeserializer);
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 28 */     if (refType.hasRawClass(OptionalInt.class)) {
/* 29 */       return (JsonDeserializer<?>)OptionalIntDeserializer.INSTANCE;
/*    */     }
/* 31 */     if (refType.hasRawClass(OptionalLong.class)) {
/* 32 */       return (JsonDeserializer<?>)OptionalLongDeserializer.INSTANCE;
/*    */     }
/* 34 */     if (refType.hasRawClass(OptionalDouble.class)) {
/* 35 */       return (JsonDeserializer<?>)OptionalDoubleDeserializer.INSTANCE;
/*    */     }
/* 37 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/Jdk8Deserializers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */