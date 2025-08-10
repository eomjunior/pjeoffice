/*    */ package org.apache.hc.core5.http2;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public class H2ConnectionException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = -2014204317155428658L;
/*    */   private final int code;
/*    */   
/*    */   public H2ConnectionException(H2Error error, String message) {
/* 46 */     super(message);
/* 47 */     Args.notNull(error, "H2 Error code");
/* 48 */     this.code = error.getCode();
/*    */   }
/*    */   
/*    */   public H2ConnectionException(int code, String message) {
/* 52 */     super(message);
/* 53 */     this.code = code;
/*    */   }
/*    */   
/*    */   public int getCode() {
/* 57 */     return this.code;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/H2ConnectionException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */