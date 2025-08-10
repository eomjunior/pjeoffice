/*    */ package com.fasterxml.jackson.databind.jsonFormatVisitors;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
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
/*    */ public interface JsonMapFormatVisitor
/*    */   extends JsonFormatVisitorWithSerializerProvider
/*    */ {
/*    */   void keyFormat(JsonFormatVisitable paramJsonFormatVisitable, JavaType paramJavaType) throws JsonMappingException;
/*    */   
/*    */   void valueFormat(JsonFormatVisitable paramJsonFormatVisitable, JavaType paramJavaType) throws JsonMappingException;
/*    */   
/*    */   public static class Base
/*    */     implements JsonMapFormatVisitor
/*    */   {
/*    */     protected SerializerProvider _provider;
/*    */     
/*    */     public Base() {}
/*    */     
/*    */     public Base(SerializerProvider p) {
/* 32 */       this._provider = p;
/*    */     }
/*    */     public SerializerProvider getProvider() {
/* 35 */       return this._provider;
/*    */     }
/*    */     public void setProvider(SerializerProvider p) {
/* 38 */       this._provider = p;
/*    */     }
/*    */     
/*    */     public void keyFormat(JsonFormatVisitable handler, JavaType keyType) throws JsonMappingException {}
/*    */     
/*    */     public void valueFormat(JsonFormatVisitable handler, JavaType valueType) throws JsonMappingException {}
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/jsonFormatVisitors/JsonMapFormatVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */