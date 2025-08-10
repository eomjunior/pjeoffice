/*    */ package org.apache.hc.core5.http.io.ssl;
/*    */ 
/*    */ import javax.net.ssl.SSLParameters;
/*    */ import org.apache.hc.core5.function.Callback;
/*    */ import org.apache.hc.core5.http.ssl.TLS;
/*    */ import org.apache.hc.core5.http.ssl.TlsCiphers;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class DefaultTlsSetupHandler
/*    */   implements Callback<SSLParameters>
/*    */ {
/*    */   public void execute(SSLParameters sslParameters) {
/* 45 */     sslParameters.setProtocols(TLS.excludeWeak(sslParameters.getProtocols()));
/* 46 */     sslParameters.setCipherSuites(TlsCiphers.excludeWeak(sslParameters.getCipherSuites()));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/ssl/DefaultTlsSetupHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */