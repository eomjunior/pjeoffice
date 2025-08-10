/*    */ package org.apache.hc.client5.http.auth;
/*    */ 
/*    */ import org.apache.hc.core5.http.ProtocolException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MalformedChallengeException
/*    */   extends ProtocolException
/*    */ {
/*    */   private static final long serialVersionUID = 814586927989932284L;
/*    */   
/*    */   public MalformedChallengeException() {}
/*    */   
/*    */   public MalformedChallengeException(String message) {
/* 54 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MalformedChallengeException(String message, Throwable cause) {
/* 65 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/auth/MalformedChallengeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */