/*    */ package com.github.utils4j.imp.function;
/*    */ 
/*    */ import java.util.function.Consumer;
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
/*    */ public final class SwitchConsumer<T>
/*    */ {
/*    */   private Predicate<T> conditionalConsumer;
/*    */   
/*    */   private static <T> Predicate<T> testAndConsume(Predicate<T> pred, Consumer<T> cons) {
/* 36 */     return t -> {
/*    */         boolean result = pred.test(t);
/*    */         if (result) {
/*    */           cons.accept(t);
/*    */         }
/*    */         return result;
/*    */       };
/*    */   }
/*    */   
/*    */   private SwitchConsumer(Predicate<T> pred) {
/* 46 */     this.conditionalConsumer = pred;
/*    */   }
/*    */   
/*    */   public static <C> SwitchConsumer<C> inCase(Predicate<C> pred, Consumer<C> cons) {
/* 50 */     return new SwitchConsumer<>(testAndConsume(pred, cons));
/*    */   }
/*    */   
/*    */   public SwitchConsumer<T> elseIf(Predicate<T> pred, Consumer<T> cons) {
/* 54 */     return new SwitchConsumer(this.conditionalConsumer.or(testAndConsume(pred, cons)));
/*    */   }
/*    */   
/*    */   public Consumer<T> elseDefault(Consumer<T> cons) {
/* 58 */     return testAndConsume(this.conditionalConsumer.negate(), cons)::test;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/function/SwitchConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */