/*    */ package com.itextpdf.text.log;
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
/*    */ public class CounterFactory
/*    */ {
/* 58 */   private static CounterFactory myself = new CounterFactory();
/*    */ 
/*    */ 
/*    */   
/* 62 */   private Counter counter = new DefaultCounter();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CounterFactory getInstance() {
/* 69 */     return myself;
/*    */   }
/*    */ 
/*    */   
/*    */   public static Counter getCounter(Class<?> klass) {
/* 74 */     return myself.counter.getCounter(klass);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Counter getCounter() {
/* 81 */     return this.counter;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCounter(Counter counter) {
/* 88 */     this.counter = counter;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/log/CounterFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */