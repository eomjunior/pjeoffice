/*    */ package org.apache.hc.core5.reactor;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Internal;
/*    */ import org.apache.hc.core5.http.HttpHost;
/*    */ import org.apache.hc.core5.net.NamedEndpoint;
/*    */ import org.apache.hc.core5.net.Ports;
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
/*    */ @Internal
/*    */ public final class EndpointParameters
/*    */   implements NamedEndpoint
/*    */ {
/*    */   private final String scheme;
/*    */   private final String hostName;
/*    */   private final int port;
/*    */   private final Object attachment;
/*    */   
/*    */   public EndpointParameters(String scheme, String hostName, int port, Object attachment) {
/* 49 */     this.scheme = (String)Args.notBlank(scheme, "Protocol scheme");
/* 50 */     this.hostName = (String)Args.notBlank(hostName, "Endpoint name");
/* 51 */     this.port = Ports.checkWithDefault(port);
/* 52 */     this.attachment = attachment;
/*    */   }
/*    */   
/*    */   public EndpointParameters(HttpHost host, Object attachment) {
/* 56 */     Args.notNull(host, "HTTP host");
/* 57 */     this.scheme = host.getSchemeName();
/* 58 */     this.hostName = host.getHostName();
/* 59 */     this.port = host.getPort();
/* 60 */     this.attachment = attachment;
/*    */   }
/*    */   
/*    */   public String getScheme() {
/* 64 */     return this.scheme;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getHostName() {
/* 69 */     return this.hostName;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getPort() {
/* 74 */     return this.port;
/*    */   }
/*    */   
/*    */   public Object getAttachment() {
/* 78 */     return this.attachment;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 83 */     return "EndpointParameters{scheme='" + this.scheme + '\'' + ", name='" + this.hostName + '\'' + ", port=" + this.port + ", attachment=" + this.attachment + '}';
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/EndpointParameters.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */