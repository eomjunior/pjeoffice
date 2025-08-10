/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
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
/*    */ public abstract class Ids
/*    */ {
/* 35 */   private static final AtomicLong next = new AtomicLong(System.nanoTime());
/*    */   
/*    */   public static String next() {
/* 38 */     return next("");
/*    */   }
/*    */   
/*    */   public static String next(String prefix) {
/* 42 */     return next(prefix, "");
/*    */   }
/*    */   
/*    */   public static String next(String prefix, String suffix) {
/* 46 */     return prefix + Long.toString(next.getAndIncrement()) + suffix;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Ids.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */