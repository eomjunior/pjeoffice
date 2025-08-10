/*     */ package org.apache.hc.client5.http.impl;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.hc.client5.http.DnsResolver;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public class InMemoryDnsResolver
/*     */   implements DnsResolver
/*     */ {
/*  51 */   private static final Logger LOG = LoggerFactory.getLogger(InMemoryDnsResolver.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   private final Map<String, InetAddress[]> dnsMap = (Map)new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(String host, InetAddress... ips) {
/*  78 */     Args.notNull(host, "Host name");
/*  79 */     Args.notNull(ips, "Array of IP addresses");
/*  80 */     this.dnsMap.put(host, ips);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InetAddress[] resolve(String host) throws UnknownHostException {
/*  88 */     InetAddress[] resolvedAddresses = this.dnsMap.get(host);
/*  89 */     if (LOG.isInfoEnabled()) {
/*  90 */       LOG.info("Resolving {} to {}", host, Arrays.deepToString((Object[])resolvedAddresses));
/*     */     }
/*  92 */     if (resolvedAddresses == null) {
/*  93 */       throw new UnknownHostException(host + " cannot be resolved");
/*     */     }
/*  95 */     return resolvedAddresses;
/*     */   }
/*     */ 
/*     */   
/*     */   public String resolveCanonicalHostname(String host) throws UnknownHostException {
/* 100 */     InetAddress[] resolvedAddresses = resolve(host);
/* 101 */     if (resolvedAddresses.length > 0) {
/* 102 */       return resolvedAddresses[0].getCanonicalHostName();
/*     */     }
/* 104 */     return host;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/InMemoryDnsResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */