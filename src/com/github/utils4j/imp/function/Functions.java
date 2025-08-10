/*    */ package com.github.utils4j.imp.function;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
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
/*    */ public final class Functions
/*    */ {
/*    */   public static final Runnable EMPTY_RUNNABLE = () -> {
/*    */     
/*    */     };
/*    */   
/*    */   public static <T, R> Function<T, R> toFunction(Consumer<T> consumer) {
/* 39 */     return toFunction(consumer, null);
/*    */   }
/*    */   
/*    */   public static <T, R> Function<T, R> toFunction(Consumer<T> consumer, R r) {
/* 43 */     return t -> {
/*    */         consumer.accept(t);
/*    */         return r;
/*    */       };
/*    */   }
/*    */   
/*    */   public static <T> IProvider<T> nullProvider(IExecutable<?> executable) {
/* 50 */     return () -> {
/*    */         executable.execute();
/*    */         return null;
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/function/Functions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */