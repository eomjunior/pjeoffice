/*    */ package org.apache.hc.client5.http.impl.auth;
/*    */ 
/*    */ import org.apache.hc.client5.http.auth.AuthenticationException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NTLMEngineException
/*    */   extends AuthenticationException
/*    */ {
/*    */   private static final long serialVersionUID = 6027981323731768824L;
/*    */   
/*    */   public NTLMEngineException() {}
/*    */   
/*    */   public NTLMEngineException(String message) {
/* 50 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NTLMEngineException(String message, Throwable cause) {
/* 61 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/NTLMEngineException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */