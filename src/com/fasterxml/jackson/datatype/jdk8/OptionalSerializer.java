/*    */ package com.fasterxml.jackson.datatype.jdk8;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import com.fasterxml.jackson.databind.ser.std.ReferenceTypeSerializer;
/*    */ import com.fasterxml.jackson.databind.type.ReferenceType;
/*    */ import com.fasterxml.jackson.databind.util.NameTransformer;
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
/*    */ public class OptionalSerializer
/*    */   extends ReferenceTypeSerializer<Optional<?>>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   protected OptionalSerializer(ReferenceType fullType, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> ser) {
/* 25 */     super(fullType, staticTyping, vts, ser);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected OptionalSerializer(OptionalSerializer base, BeanProperty property, TypeSerializer vts, JsonSerializer<?> valueSer, NameTransformer unwrapper, Object suppressableValue, boolean suppressNulls) {
/* 32 */     super(base, property, vts, valueSer, unwrapper, suppressableValue, suppressNulls);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ReferenceTypeSerializer<Optional<?>> withResolved(BeanProperty prop, TypeSerializer vts, JsonSerializer<?> valueSer, NameTransformer unwrapper) {
/* 41 */     return new OptionalSerializer(this, prop, vts, valueSer, unwrapper, this._suppressableValue, this._suppressNulls);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ReferenceTypeSerializer<Optional<?>> withContentInclusion(Object suppressableValue, boolean suppressNulls) {
/* 49 */     return new OptionalSerializer(this, this._property, this._valueTypeSerializer, this._valueSerializer, this._unwrapper, suppressableValue, suppressNulls);
/*    */   }
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
/*    */   protected boolean _isValuePresent(Optional<?> value) {
/* 62 */     return value.isPresent();
/*    */   }
/*    */ 
/*    */   
/*    */   protected Object _getReferenced(Optional<?> value) {
/* 67 */     return value.get();
/*    */   }
/*    */ 
/*    */   
/*    */   protected Object _getReferencedIfPresent(Optional<?> value) {
/* 72 */     return value.isPresent() ? value.get() : null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/OptionalSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */