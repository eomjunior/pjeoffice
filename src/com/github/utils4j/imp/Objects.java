/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.github.utils4j.IConstants;
/*    */ import java.nio.charset.Charset;
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
/*    */ public abstract class Objects
/*    */ {
/*    */   public static Object getOfDefault(Object input, Object defaultIfNull) {
/* 40 */     return (input == null) ? defaultIfNull : input;
/*    */   }
/*    */   
/*    */   public static String toString(Object input, String defaultIfNull) {
/* 44 */     return (input != null) ? input.toString() : defaultIfNull;
/*    */   }
/*    */   
/*    */   public static String toString(char[] input, String defaultIfNull) {
/* 48 */     return (input != null) ? new String(input) : defaultIfNull;
/*    */   }
/*    */   
/*    */   public static String iso2utf8(char[] input, String defaultIfNull) {
/* 52 */     return (input != null) ? iso2utf8(input) : defaultIfNull;
/*    */   }
/*    */   
/*    */   public static String iso2utf8(char[] input) {
/* 56 */     return fromIso(input, IConstants.UTF_8);
/*    */   }
/*    */   
/*    */   public static String fromIso(char[] input, Charset outputCharset) {
/* 60 */     return new String((new String(input)).getBytes(IConstants.ISO_8859_1), outputCharset);
/*    */   }
/*    */   
/*    */   public static String toString(char[] input, Charset inputCharset, Charset outputCharset) {
/* 64 */     return new String((new String(input)).getBytes(inputCharset), outputCharset);
/*    */   }
/*    */   
/*    */   public static String toJson(Object instance) throws JsonProcessingException {
/* 68 */     return JsonTools.mapper().writeValueAsString(instance);
/*    */   }
/*    */   
/*    */   public static Object[] arrayOf(Object... objects) {
/* 72 */     return objects;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Objects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */