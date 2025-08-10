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
/*    */ @Experimental
/*    */ public class KerberosScheme
/*    */   extends GGSSchemeBase
/*    */ {
/*    */   private static final String KERBEROS_OID = "1.2.840.113554.1.2.2";
/*    */   
/*    */   public KerberosScheme(KerberosConfig config, DnsResolver dnsResolver) {
/* 54 */     super(config, dnsResolver);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public KerberosScheme() {}
/*    */ 
/*    */   
/*    */   public String getName() {
/* 63 */     return "Kerberos";
/*    */   }
/*    */ 
/*    */   
/*    */   protected byte[] generateToken(byte[] input, String serviceName, String authServer) throws GSSException {
/* 68 */     return generateGSSToken(input, new Oid("1.2.840.113554.1.2.2"), serviceName, authServer);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isConnectionBased() {
/* 73 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/KerberosScheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */