/*    */ package com.github.utils4j.imp.function;
/*    */ 
/*    */ import java.util.function.Predicate;
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
/*    */ public final class Predicates
/*    */ {
/*    */   public static final <T> Predicate<T> notNull() {
/* 35 */     return t -> (t != null);
/*    */   }
/*    */   
/*    */   public static final <T> Predicate<T> all() {
/* 39 */     return t -> true;
/*    */   }
/*    */   
/*    */   public static final <T> Predicate<T> denyAll() {
/* 43 */     return t -> false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/function/Predicates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */