/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
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
/*    */ @JacksonStdImpl
/*    */ public class ToStringSerializer
/*    */   extends ToStringSerializerBase
/*    */ {
/* 20 */   public static final ToStringSerializer instance = new ToStringSerializer();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ToStringSerializer() {
/* 30 */     super(Object.class);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ToStringSerializer(Class<?> handledType) {
/* 38 */     super(handledType);
/*    */   }
/*    */ 
/*    */   
/*    */   public final String valueToString(Object value) {
/* 43 */     return value.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/ser/std/ToStringSerializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */