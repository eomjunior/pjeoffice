/*    */ package com.fasterxml.jackson.databind.deser;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.util.AccessPattern;
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
/*    */ public interface NullValueProvider
/*    */ {
/*    */   Object getNullValue(DeserializationContext paramDeserializationContext) throws JsonMappingException;
/*    */   
/*    */   AccessPattern getNullAccessPattern();
/*    */   
/*    */   default Object getAbsentValue(DeserializationContext ctxt) throws JsonMappingException {
/* 52 */     return getNullValue(ctxt);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/deser/NullValueProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */