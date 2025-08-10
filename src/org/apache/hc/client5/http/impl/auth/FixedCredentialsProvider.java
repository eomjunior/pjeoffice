/*    */ package org.apache.hc.client5.http.impl.auth;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.apache.hc.client5.http.auth.AuthScope;
/*    */ import org.apache.hc.client5.http.auth.Credentials;
/*    */ import org.apache.hc.client5.http.auth.CredentialsProvider;
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
/*    */ final class FixedCredentialsProvider
/*    */   implements CredentialsProvider
/*    */ {
/*    */   private final Map<AuthScope, Credentials> credMap;
/*    */   
/*    */   public FixedCredentialsProvider(Map<AuthScope, Credentials> credMap) {
/* 44 */     this.credMap = Collections.unmodifiableMap(new HashMap<>(credMap));
/*    */   }
/*    */ 
/*    */   
/*    */   public Credentials getCredentials(AuthScope authScope, HttpContext context) {
/* 49 */     return CredentialsMatcher.matchCredentials(this.credMap, authScope);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return this.credMap.keySet().toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/FixedCredentialsProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */