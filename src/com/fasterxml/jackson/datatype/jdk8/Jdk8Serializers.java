/*    */ package com.fasterxml.jackson.datatype.jdk8;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.BeanDescription;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.MapperFeature;
/*    */ import com.fasterxml.jackson.databind.SerializationConfig;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import com.fasterxml.jackson.databind.ser.Serializers;
/*    */ import com.fasterxml.jackson.databind.type.ReferenceType;
/*    */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*    */ import java.io.Serializable;
/*    */ import java.util.Optional;
/*    */ import java.util.OptionalDouble;
/*    */ import java.util.OptionalInt;
/*    */ import java.util.OptionalLong;
/*    */ import java.util.stream.DoubleStream;
/*    */ import java.util.stream.IntStream;
/*    */ import java.util.stream.LongStream;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class Jdk8Serializers
/*    */   extends Serializers.Base
/*    */   implements Serializable {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public JsonSerializer<?> findReferenceSerializer(SerializationConfig config, ReferenceType refType, BeanDescription beanDesc, TypeSerializer contentTypeSerializer, JsonSerializer<Object> contentValueSerializer) {
/* 28 */     Class<?> raw = refType.getRawClass();
/* 29 */     if (Optional.class.isAssignableFrom(raw)) {
/*    */       
/* 31 */       boolean staticTyping = (contentTypeSerializer == null && config.isEnabled(MapperFeature.USE_STATIC_TYPING));
/* 32 */       return (JsonSerializer<?>)new OptionalSerializer(refType, staticTyping, contentTypeSerializer, contentValueSerializer);
/*    */     } 
/*    */     
/* 35 */     if (OptionalInt.class.isAssignableFrom(raw)) {
/* 36 */       return (JsonSerializer<?>)OptionalIntSerializer.INSTANCE;
/*    */     }
/* 38 */     if (OptionalLong.class.isAssignableFrom(raw)) {
/* 39 */       return (JsonSerializer<?>)OptionalLongSerializer.INSTANCE;
/*    */     }
/* 41 */     if (OptionalDouble.class.isAssignableFrom(raw)) {
/* 42 */       return (JsonSerializer<?>)OptionalDoubleSerializer.INSTANCE;
/*    */     }
/* 44 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) {
/* 51 */     Class<?> raw = type.getRawClass();
/* 52 */     if (LongStream.class.isAssignableFrom(raw)) {
/* 53 */       return (JsonSerializer<?>)LongStreamSerializer.INSTANCE;
/*    */     }
/* 55 */     if (IntStream.class.isAssignableFrom(raw)) {
/* 56 */       return (JsonSerializer<?>)IntStreamSerializer.INSTANCE;
/*    */     }
/* 58 */     if (DoubleStream.class.isAssignableFrom(raw)) {
/* 59 */       return (JsonSerializer<?>)DoubleStreamSerializer.INSTANCE;
/*    */     }
/* 61 */     if (Stream.class.isAssignableFrom(raw)) {
/* 62 */       JavaType[] params = config.getTypeFactory().findTypeParameters(type, Stream.class);
/* 63 */       JavaType vt = (params == null || params.length != 1) ? TypeFactory.unknownType() : params[0];
/* 64 */       return (JsonSerializer<?>)new StreamSerializer(config.getTypeFactory().constructParametricType(Stream.class, new JavaType[] { vt }), vt);
/*    */     } 
/* 66 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/Jdk8Serializers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */