/*    */ package org.apache.hc.client5.http;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SystemDefaultDnsResolver
/*    */   implements DnsResolver
/*    */ {
/* 39 */   public static final SystemDefaultDnsResolver INSTANCE = new SystemDefaultDnsResolver();
/*    */ 
/*    */   
/*    */   public InetAddress[] resolve(String host) throws UnknownHostException {
/* 43 */     return InetAddress.getAllByName(host);
/*    */   }
/*    */ 
/*    */   
/*    */   public String resolveCanonicalHostname(String host) throws UnknownHostException {
/* 48 */     if (host == null) {
/* 49 */       return null;
/*    */     }
/* 51 */     InetAddress in = InetAddress.getByName(host);
/* 52 */     String canonicalServer = in.getCanonicalHostName();
/* 53 */     if (in.getHostAddress().contentEquals(canonicalServer)) {
/* 54 */       return host;
/*    */     }
/* 56 */     return canonicalServer;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/SystemDefaultDnsResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */