/*    */ package org.apache.hc.core5.http;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TruncatedChunkException
/*    */   extends MalformedChunkCodingException
/*    */ {
/*    */   private static final long serialVersionUID = -23506263930279460L;
/*    */   
/*    */   public TruncatedChunkException(String message) {
/* 45 */     super(HttpException.clean(message));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TruncatedChunkException(String format, Object... args) {
/* 57 */     super(format, args);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/TruncatedChunkException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */