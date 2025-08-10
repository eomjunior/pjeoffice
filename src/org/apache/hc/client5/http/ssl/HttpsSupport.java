/*    */ package org.apache.hc.client5.http.ssl;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import javax.net.ssl.HostnameVerifier;
/*    */ import org.apache.hc.client5.http.psl.PublicSuffixMatcherLoader;
/*    */ import org.apache.hc.core5.util.TextUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class HttpsSupport
/*    */ {
/*    */   private static String[] split(String s) {
/* 46 */     if (TextUtils.isBlank(s)) {
/* 47 */       return null;
/*    */     }
/* 49 */     return s.split(" *, *");
/*    */   }
/*    */   
/*    */   private static String getProperty(String key) {
/* 53 */     return AccessController.<String>doPrivileged(() -> System.getProperty(key));
/*    */   }
/*    */   
/*    */   public static String[] getSystemProtocols() {
/* 57 */     return split(getProperty("https.protocols"));
/*    */   }
/*    */   
/*    */   public static String[] getSystemCipherSuits() {
/* 61 */     return split(getProperty("https.cipherSuites"));
/*    */   }
/*    */   
/*    */   public static HostnameVerifier getDefaultHostnameVerifier() {
/* 65 */     return new DefaultHostnameVerifier(PublicSuffixMatcherLoader.getDefault());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/ssl/HttpsSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */