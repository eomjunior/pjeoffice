/*    */ package org.apache.hc.core5.http;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
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
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public interface ExceptionListener
/*    */ {
/* 38 */   public static final ExceptionListener NO_OP = new ExceptionListener()
/*    */     {
/*    */       public void onError(Exception ex) {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/*    */       public void onError(HttpConnection connection, Exception ex) {}
/*    */     };
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public static final ExceptionListener STD_ERR = new ExceptionListener()
/*    */     {
/*    */       public void onError(Exception ex)
/*    */       {
/* 56 */         ex.printStackTrace();
/*    */       }
/*    */ 
/*    */       
/*    */       public void onError(HttpConnection connection, Exception ex) {
/* 61 */         ex.printStackTrace();
/*    */       }
/*    */     };
/*    */   
/*    */   void onError(Exception paramException);
/*    */   
/*    */   void onError(HttpConnection paramHttpConnection, Exception paramException);
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/ExceptionListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */