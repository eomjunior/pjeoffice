/*    */ package org.apache.hc.client5.http.impl.auth;
/*    */ 
/*    */ import org.apache.hc.client5.http.DnsResolver;
/*    */ import org.apache.hc.client5.http.auth.KerberosConfig;
/*    */ import org.apache.hc.core5.annotation.Experimental;
/*    */ import org.ietf.jgss.GSSException;
/*    */ import org.ietf.jgss.Oid;
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
/*    */ @Experimental
/*    */ public class SPNegoScheme
/*    */   extends GGSSchemeBase
/*    */ {
/*    */   private static final String SPNEGO_OID = "1.3.6.1.5.5.2";
/*    */   
/*    */   public SPNegoScheme(KerberosConfig config, DnsResolver dnsResolver) {
/* 55 */     super(config, dnsResolver);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public SPNegoScheme() {}
/*    */ 
/*    */   
/*    */   public String getName() {
/* 64 */     return "Negotiate";
/*    */   }
/*    */ 
/*    */   
/*    */   protected byte[] generateToken(byte[] input, String serviceName, String authServer) throws GSSException {
/* 69 */     return generateGSSToken(input, new Oid("1.3.6.1.5.5.2"), serviceName, authServer);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isConnectionBased() {
/* 74 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/SPNegoScheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */