/*    */ package org.apache.hc.core5.http2.hpack;
/*    */ 
/*    */ import org.apache.hc.core5.http.HttpException;
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
/*    */ public class HPackException
/*    */   extends HttpException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public HPackException(String message) {
/* 41 */     super(message);
/*    */   }
/*    */   
/*    */   public HPackException(String message, Exception cause) {
/* 45 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/hpack/HPackException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */