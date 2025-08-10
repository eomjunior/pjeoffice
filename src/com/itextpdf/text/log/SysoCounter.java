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
/*    */ public class SysoCounter
/*    */   implements Counter
/*    */ {
/*    */   protected String name;
/*    */   
/*    */   public SysoCounter() {
/* 56 */     this.name = "iText";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected SysoCounter(Class<?> klass) {
/* 64 */     this.name = klass.getName();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Counter getCounter(Class<?> klass) {
/* 71 */     return new SysoCounter(klass);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void read(long l) {
/* 78 */     System.out.println(String.format("[%s] %s bytes read", new Object[] { this.name, Long.valueOf(l) }));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void written(long l) {
/* 85 */     System.out.println(String.format("[%s] %s bytes written", new Object[] { this.name, Long.valueOf(l) }));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/log/SysoCounter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */