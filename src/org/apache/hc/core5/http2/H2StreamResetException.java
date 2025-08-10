/*    */ package org.apache.hc.core5.http2;
/*    */ 
/*    */ import org.apache.hc.core5.http.HttpStreamResetException;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ public class H2StreamResetException
/*    */   extends HttpStreamResetException
/*    */ {
/*    */   private static final long serialVersionUID = 4280996898701236013L;
/*    */   private final int code;
/*    */   
/*    */   public H2StreamResetException(H2Error error, String message) {
/* 49 */     super(message);
/* 50 */     Args.notNull(error, "H2 Error code");
/* 51 */     this.code = error.getCode();
/*    */   }
/*    */   
/*    */   public H2StreamResetException(int code, String message) {
/* 55 */     super(message);
/* 56 */     this.code = code;
/*    */   }
/*    */   
/*    */   public int getCode() {
/* 60 */     return this.code;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/H2StreamResetException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */