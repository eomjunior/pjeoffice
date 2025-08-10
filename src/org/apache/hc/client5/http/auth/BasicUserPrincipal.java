/*    */ package org.apache.hc.client5.http.auth;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.security.Principal;
/*    */ import java.util.Objects;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.util.Args;
/*    */ import org.apache.hc.core5.util.LangUtils;
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
/*    */ public final class BasicUserPrincipal
/*    */   implements Principal, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -2266305184969850467L;
/*    */   private final String username;
/*    */   
/*    */   public BasicUserPrincipal(String username) {
/* 52 */     Args.notNull(username, "User name");
/* 53 */     this.username = username;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 58 */     return this.username;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 63 */     int hash = 17;
/* 64 */     hash = LangUtils.hashCode(hash, this.username);
/* 65 */     return hash;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 70 */     if (this == o) {
/* 71 */       return true;
/*    */     }
/* 73 */     if (o instanceof BasicUserPrincipal) {
/* 74 */       BasicUserPrincipal that = (BasicUserPrincipal)o;
/* 75 */       return Objects.equals(this.username, that.username);
/*    */     } 
/* 77 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 82 */     StringBuilder buffer = new StringBuilder();
/* 83 */     buffer.append("[principal: ");
/* 84 */     buffer.append(this.username);
/* 85 */     buffer.append("]");
/* 86 */     return buffer.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/auth/BasicUserPrincipal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */