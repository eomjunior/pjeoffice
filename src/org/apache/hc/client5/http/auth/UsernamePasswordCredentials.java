/*    */ package org.apache.hc.client5.http.auth;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.security.Principal;
/*    */ import java.util.Objects;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
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
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*    */ public class UsernamePasswordCredentials
/*    */   implements Credentials, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 243343858802739403L;
/*    */   private final BasicUserPrincipal principal;
/*    */   private final char[] password;
/*    */   
/*    */   public UsernamePasswordCredentials(String userName, char[] password) {
/* 59 */     Args.notNull(userName, "Username");
/* 60 */     this.principal = new BasicUserPrincipal(userName);
/* 61 */     this.password = password;
/*    */   }
/*    */ 
/*    */   
/*    */   public Principal getUserPrincipal() {
/* 66 */     return this.principal;
/*    */   }
/*    */   
/*    */   public String getUserName() {
/* 70 */     return this.principal.getName();
/*    */   }
/*    */ 
/*    */   
/*    */   public char[] getPassword() {
/* 75 */     return this.password;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 80 */     return this.principal.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 85 */     if (this == o) {
/* 86 */       return true;
/*    */     }
/* 88 */     if (o instanceof UsernamePasswordCredentials) {
/* 89 */       UsernamePasswordCredentials that = (UsernamePasswordCredentials)o;
/* 90 */       return Objects.equals(this.principal, that.principal);
/*    */     } 
/* 92 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 97 */     return this.principal.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/auth/UsernamePasswordCredentials.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */