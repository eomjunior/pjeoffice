/*    */ package org.apache.hc.client5.http.impl.auth;
/*    */ 
/*    */ import org.apache.hc.client5.http.DnsResolver;
/*    */ import org.apache.hc.client5.http.SystemDefaultDnsResolver;
/*    */ import org.apache.hc.client5.http.auth.AuthScheme;
/*    */ import org.apache.hc.client5.http.auth.AuthSchemeFactory;
/*    */ import org.apache.hc.client5.http.auth.KerberosConfig;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.Experimental;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ @Experimental
/*    */ public class SPNegoSchemeFactory
/*    */   implements AuthSchemeFactory
/*    */ {
/* 56 */   public static final SPNegoSchemeFactory DEFAULT = new SPNegoSchemeFactory(KerberosConfig.DEFAULT, (DnsResolver)SystemDefaultDnsResolver.INSTANCE);
/*    */ 
/*    */   
/*    */   private final KerberosConfig config;
/*    */ 
/*    */   
/*    */   private final DnsResolver dnsResolver;
/*    */ 
/*    */ 
/*    */   
/*    */   public SPNegoSchemeFactory(KerberosConfig config, DnsResolver dnsResolver) {
/* 67 */     this.config = config;
/* 68 */     this.dnsResolver = dnsResolver;
/*    */   }
/*    */ 
/*    */   
/*    */   public AuthScheme create(HttpContext context) {
/* 73 */     return new SPNegoScheme(this.config, this.dnsResolver);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/SPNegoSchemeFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */