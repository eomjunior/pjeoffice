/*    */ package org.apache.hc.client5.http.auth;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.security.Principal;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.ietf.jgss.GSSCredential;
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
/*    */ public class KerberosCredentials
/*    */   implements Credentials, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 487421613855550713L;
/*    */   private final GSSCredential gssCredential;
/*    */   
/*    */   public KerberosCredentials(GSSCredential gssCredential) {
/* 55 */     this.gssCredential = gssCredential;
/*    */   }
/*    */   
/*    */   public GSSCredential getGSSCredential() {
/* 59 */     return this.gssCredential;
/*    */   }
/*    */ 
/*    */   
/*    */   public Principal getUserPrincipal() {
/* 64 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public char[] getPassword() {
/* 69 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/auth/KerberosCredentials.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */