/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.Module;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
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
/*    */ public class JsonTools
/*    */ {
/*    */   private static class JsonToolsHolder
/*    */   {
/* 38 */     static final ObjectMapper MAPPER = new ObjectMapper();
/*    */     static {
/* 40 */       MAPPER.registerModule((Module)new Jdk8Module());
/*    */     }
/*    */   }
/*    */   
/*    */   public static ObjectMapper mapper() {
/* 45 */     return JsonToolsHolder.MAPPER;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/JsonTools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */