/*    */ package com.google.j2objc.annotations;
/*    */ 
/*    */ import java.lang.annotation.Documented;
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
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
/*    */ @Documented
/*    */ @Target({ElementType.TYPE, ElementType.PACKAGE})
/*    */ @Retention(RetentionPolicy.CLASS)
/*    */ public @interface ReflectionSupport
/*    */ {
/*    */   Level value();
/*    */   
/*    */   public enum Level
/*    */   {
/* 42 */     NATIVE_ONLY,
/*    */ 
/*    */ 
/*    */     
/* 46 */     FULL;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/j2objc/annotations/ReflectionSupport.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */