/*    */ package org.apache.hc.core5.http2.frame;
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
/*    */ public interface StreamIdGenerator
/*    */ {
/* 41 */   public static final StreamIdGenerator ODD = new StreamIdGenerator()
/*    */     {
/*    */       public int generate(int previousMax)
/*    */       {
/* 45 */         int i = previousMax + 1;
/* 46 */         if (i % 2 == 0) {
/* 47 */           i++;
/*    */         }
/* 49 */         return i;
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean isSameSide(int streamId) {
/* 54 */         return ((streamId & 0x1) == 1);
/*    */       }
/*    */     };
/*    */ 
/*    */   
/* 59 */   public static final StreamIdGenerator EVEN = new StreamIdGenerator()
/*    */     {
/*    */       public int generate(int previousMax)
/*    */       {
/* 63 */         int i = previousMax + 1;
/* 64 */         if (i % 2 == 1) {
/* 65 */           i++;
/*    */         }
/* 67 */         return i;
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean isSameSide(int streamId) {
/* 72 */         return ((streamId & 0x1) == 0);
/*    */       }
/*    */     };
/*    */   
/*    */   int generate(int paramInt);
/*    */   
/*    */   boolean isSameSide(int paramInt);
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/frame/StreamIdGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */