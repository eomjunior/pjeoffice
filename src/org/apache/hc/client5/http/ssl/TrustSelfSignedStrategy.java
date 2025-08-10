/*    */ package org.apache.hc.client5.http.ssl;
/*    */ 
/*    */ import java.security.cert.CertificateException;
/*    */ import java.security.cert.X509Certificate;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.ssl.TrustStrategy;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ public class TrustSelfSignedStrategy
/*    */   implements TrustStrategy
/*    */ {
/* 45 */   public static final TrustSelfSignedStrategy INSTANCE = new TrustSelfSignedStrategy();
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
/* 50 */     return (chain.length == 1);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/ssl/TrustSelfSignedStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */