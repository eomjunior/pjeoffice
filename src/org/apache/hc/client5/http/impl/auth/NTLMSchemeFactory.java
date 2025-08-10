/*    */ package org.apache.hc.client5.http.impl.auth;
/*    */ 
/*    */ import org.apache.hc.client5.http.auth.AuthScheme;
/*    */ import org.apache.hc.client5.http.auth.AuthSchemeFactory;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ public class NTLMSchemeFactory
/*    */   implements AuthSchemeFactory
/*    */ {
/* 49 */   public static final NTLMSchemeFactory INSTANCE = new NTLMSchemeFactory();
/*    */ 
/*    */   
/*    */   public AuthScheme create(HttpContext context) {
/* 53 */     return new NTLMScheme();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/NTLMSchemeFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */