/*    */ package org.apache.hc.client5.http.impl.auth;
/*    */ 
/*    */ import org.apache.hc.client5.http.auth.AuthScope;
/*    */ import org.apache.hc.client5.http.auth.Credentials;
/*    */ import org.apache.hc.client5.http.auth.CredentialsProvider;
/*    */ import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
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
/*    */ final class SingleCredentialsProvider
/*    */   implements CredentialsProvider
/*    */ {
/*    */   private final AuthScope authScope;
/*    */   private final Credentials credentials;
/*    */   
/*    */   public SingleCredentialsProvider(AuthScope authScope, Credentials credentials) {
/* 43 */     this.authScope = (AuthScope)Args.notNull(authScope, "Auth scope");
/* 44 */     this.credentials = credentials;
/*    */   }
/*    */   
/*    */   public SingleCredentialsProvider(AuthScope authScope, String username, char[] password) {
/* 48 */     this(authScope, (Credentials)new UsernamePasswordCredentials(username, password));
/*    */   }
/*    */ 
/*    */   
/*    */   public Credentials getCredentials(AuthScope authScope, HttpContext context) {
/* 53 */     return (this.authScope.match(authScope) >= 0) ? this.credentials : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     return this.authScope.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/SingleCredentialsProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */