/*    */ package org.apache.hc.client5.http.impl.auth;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.apache.hc.client5.http.auth.AuthScope;
/*    */ import org.apache.hc.client5.http.auth.Credentials;
/*    */ import org.apache.hc.client5.http.auth.CredentialsStore;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
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
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.SAFE)
/*    */ public class BasicCredentialsProvider
/*    */   implements CredentialsStore
/*    */ {
/* 54 */   private final ConcurrentHashMap<AuthScope, Credentials> credMap = new ConcurrentHashMap<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCredentials(AuthScope authScope, Credentials credentials) {
/* 61 */     Args.notNull(authScope, "Authentication scope");
/* 62 */     this.credMap.put(authScope, credentials);
/*    */   }
/*    */ 
/*    */   
/*    */   public Credentials getCredentials(AuthScope authScope, HttpContext context) {
/* 67 */     return CredentialsMatcher.matchCredentials(this.credMap, authScope);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 72 */     this.credMap.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 77 */     return this.credMap.keySet().toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/BasicCredentialsProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */