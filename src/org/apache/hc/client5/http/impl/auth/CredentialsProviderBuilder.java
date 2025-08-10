/*    */ package org.apache.hc.client5.http.impl.auth;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.apache.hc.client5.http.auth.AuthScope;
/*    */ import org.apache.hc.client5.http.auth.Credentials;
/*    */ import org.apache.hc.client5.http.auth.CredentialsProvider;
/*    */ import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
/*    */ import org.apache.hc.core5.http.HttpHost;
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
/*    */ public final class CredentialsProviderBuilder
/*    */ {
/*    */   private final Map<AuthScope, Credentials> credMap;
/*    */   
/*    */   public static CredentialsProviderBuilder create() {
/* 49 */     return new CredentialsProviderBuilder();
/*    */   }
/*    */ 
/*    */   
/*    */   public CredentialsProviderBuilder() {
/* 54 */     this.credMap = new HashMap<>();
/*    */   }
/*    */   
/*    */   public CredentialsProviderBuilder add(AuthScope authScope, Credentials credentials) {
/* 58 */     Args.notNull(authScope, "Host");
/* 59 */     this.credMap.put(authScope, credentials);
/* 60 */     return this;
/*    */   }
/*    */   
/*    */   public CredentialsProviderBuilder add(AuthScope authScope, String username, char[] password) {
/* 64 */     Args.notNull(authScope, "Host");
/* 65 */     this.credMap.put(authScope, new UsernamePasswordCredentials(username, password));
/* 66 */     return this;
/*    */   }
/*    */   
/*    */   public CredentialsProviderBuilder add(HttpHost httpHost, Credentials credentials) {
/* 70 */     Args.notNull(httpHost, "Host");
/* 71 */     this.credMap.put(new AuthScope(httpHost), credentials);
/* 72 */     return this;
/*    */   }
/*    */   
/*    */   public CredentialsProviderBuilder add(HttpHost httpHost, String username, char[] password) {
/* 76 */     Args.notNull(httpHost, "Host");
/* 77 */     this.credMap.put(new AuthScope(httpHost), new UsernamePasswordCredentials(username, password));
/* 78 */     return this;
/*    */   }
/*    */   
/*    */   public CredentialsProvider build() {
/* 82 */     if (this.credMap.size() == 0)
/* 83 */       return (CredentialsProvider)new BasicCredentialsProvider(); 
/* 84 */     if (this.credMap.size() == 1) {
/* 85 */       Map.Entry<AuthScope, Credentials> entry = this.credMap.entrySet().iterator().next();
/* 86 */       return new SingleCredentialsProvider(entry.getKey(), entry.getValue());
/*    */     } 
/* 88 */     return new FixedCredentialsProvider(this.credMap);
/*    */   }
/*    */ 
/*    */   
/*    */   static class Entry
/*    */   {
/*    */     final AuthScope authScope;
/*    */     final Credentials credentials;
/*    */     
/*    */     Entry(AuthScope authScope, Credentials credentials) {
/* 98 */       this.authScope = authScope;
/* 99 */       this.credentials = credentials;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/CredentialsProviderBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */