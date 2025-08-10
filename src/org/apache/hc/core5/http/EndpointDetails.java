/*     */ package org.apache.hc.core5.http;
/*     */ 
/*     */ import java.net.SocketAddress;
/*     */ import org.apache.hc.core5.net.InetAddressUtils;
/*     */ import org.apache.hc.core5.util.Timeout;
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
/*     */ public abstract class EndpointDetails
/*     */   implements HttpConnectionMetrics
/*     */ {
/*     */   private final SocketAddress remoteAddress;
/*     */   private final SocketAddress localAddress;
/*     */   private final Timeout socketTimeout;
/*     */   
/*     */   protected EndpointDetails(SocketAddress remoteAddress, SocketAddress localAddress, Timeout socketTimeout) {
/*  47 */     this.remoteAddress = remoteAddress;
/*  48 */     this.localAddress = localAddress;
/*  49 */     this.socketTimeout = socketTimeout;
/*     */   }
/*     */   
/*     */   public SocketAddress getRemoteAddress() {
/*  53 */     return this.remoteAddress;
/*     */   }
/*     */   
/*     */   public SocketAddress getLocalAddress() {
/*  57 */     return this.localAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract long getRequestCount();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract long getResponseCount();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract long getSentBytesCount();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract long getReceivedBytesCount();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Timeout getSocketTimeout() {
/*  94 */     return this.socketTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 100 */     StringBuilder buffer = new StringBuilder(90);
/* 101 */     InetAddressUtils.formatAddress(buffer, this.localAddress);
/* 102 */     buffer.append("<->");
/* 103 */     InetAddressUtils.formatAddress(buffer, this.remoteAddress);
/* 104 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/EndpointDetails.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */