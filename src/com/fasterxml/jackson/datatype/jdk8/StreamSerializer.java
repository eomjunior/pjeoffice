/*    */ package com.fasterxml.jackson.datatype.jdk8;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.MapperFeature;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*    */ import com.fasterxml.jackson.databind.ser.std.StdSerializer;
/*    */ import java.io.IOException;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StreamSerializer
/*    */   extends StdSerializer<Stream<?>>
/*    */   implements ContextualSerializer
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final JavaType elemType;
/*    */   private final transient JsonSerializer<Object> elemSerializer;
/*    */   
/*    */   public StreamSerializer(JavaType streamType, JavaType elemType) {
/* 36 */     this(streamType, elemType, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StreamSerializer(JavaType streamType, JavaType elemType, JsonSerializer<Object> elemSerializer) {
/* 47 */     super(streamType);
/* 48 */     this.elemType = elemType;
/* 49 */     this.elemSerializer = elemSerializer;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
/* 55 */     if (!this.elemType.hasRawClass(Object.class) && (provider
/* 56 */       .isEnabled(MapperFeature.USE_STATIC_TYPING) || this.elemType.isFinal())) {
/* 57 */       return (JsonSerializer<?>)new StreamSerializer(provider
/* 58 */           .getTypeFactory().constructParametricType(Stream.class, new JavaType[] { this.elemType }), this.elemType, provider
/*    */           
/* 60 */           .findPrimaryPropertySerializer(this.elemType, property));
/*    */     }
/* 62 */     return (JsonSerializer<?>)this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void serialize(Stream<?> stream, JsonGenerator jgen, SerializerProvider provider) throws IOException {
/* 68 */     try (Stream<?> s = stream) {
/* 69 */       jgen.writeStartArray();
/*    */       
/* 71 */       s.forEachOrdered(elem -> {
/*    */             try {
/*    */               if (this.elemSerializer == null) {
/*    */                 provider.defaultSerializeValue(elem, jgen);
/*    */               } else {
/*    */                 this.elemSerializer.serialize(elem, jgen, provider);
/*    */               } 
/* 78 */             } catch (IOException e) {
/*    */               throw new WrappedIOException(e);
/*    */             } 
/*    */           });
/*    */       
/* 83 */       jgen.writeEndArray();
/* 84 */     } catch (WrappedIOException e) {
/* 85 */       throw e.getCause();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/StreamSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */