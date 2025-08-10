/*    */ package com.fasterxml.jackson.databind.jsontype.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AsPropertyTypeSerializer
/*    */   extends AsArrayTypeSerializer
/*    */ {
/*    */   protected final String _typePropertyName;
/*    */   
/*    */   public AsPropertyTypeSerializer(TypeIdResolver idRes, BeanProperty property, String propName) {
/* 22 */     super(idRes, property);
/* 23 */     this._typePropertyName = propName;
/*    */   }
/*    */ 
/*    */   
/*    */   public AsPropertyTypeSerializer forProperty(BeanProperty prop) {
/* 28 */     return (this._property == prop) ? this : 
/* 29 */       new AsPropertyTypeSerializer(this._idResolver, prop, this._typePropertyName);
/*    */   }
/*    */   
/*    */   public String getPropertyName() {
/* 33 */     return this._typePropertyName;
/*    */   }
/*    */   public JsonTypeInfo.As getTypeInclusion() {
/* 36 */     return JsonTypeInfo.As.PROPERTY;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/jsontype/impl/AsPropertyTypeSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */