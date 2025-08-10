/*    */ package com.fasterxml.jackson.datatype.jdk8;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*    */ import com.fasterxml.jackson.databind.deser.std.ReferenceTypeDeserializer;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*    */ import java.util.Optional;
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
/*    */ final class OptionalDeserializer
/*    */   extends ReferenceTypeDeserializer<Optional<?>>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public OptionalDeserializer(JavaType fullType, ValueInstantiator inst, TypeDeserializer typeDeser, JsonDeserializer<?> deser) {
/* 27 */     super(fullType, inst, typeDeser, deser);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public OptionalDeserializer withResolved(TypeDeserializer typeDeser, JsonDeserializer<?> valueDeser) {
/* 38 */     return new OptionalDeserializer(this._fullType, this._valueInstantiator, typeDeser, valueDeser);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Optional<?> getNullValue(DeserializationContext ctxt) throws JsonMappingException {
/* 45 */     return Optional.ofNullable(this._valueDeserializer.getNullValue(ctxt));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getEmptyValue(DeserializationContext ctxt) throws JsonMappingException {
/* 53 */     return getNullValue(ctxt);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<?> referenceValue(Object contents) {
/* 58 */     return Optional.ofNullable(contents);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getReferenced(Optional<?> reference) {
/* 65 */     return reference.orElse(null);
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<?> updateReference(Optional<?> reference, Object contents) {
/* 70 */     return Optional.ofNullable(contents);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/OptionalDeserializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */