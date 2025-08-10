/*    */ package com.fasterxml.jackson.datatype.jdk8;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
/*    */ 
/*    */ 
/*    */ public abstract class BaseScalarOptionalDeserializer<T>
/*    */   extends StdScalarDeserializer<T>
/*    */ {
/*    */   protected final T _empty;
/*    */   
/*    */   protected BaseScalarOptionalDeserializer(Class<T> cls, T empty) {
/* 13 */     super(cls);
/* 14 */     this._empty = empty;
/*    */   }
/*    */ 
/*    */   
/*    */   public T getNullValue(DeserializationContext ctxt) {
/* 19 */     return this._empty;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/datatype/jdk8/BaseScalarOptionalDeserializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */