/*    */ package org.apache.tools.ant.util.java15;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Proxy;
/*    */ import java.net.ProxySelector;
/*    */ import java.net.SocketAddress;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import org.apache.tools.ant.BuildException;
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
/*    */ public class ProxyDiagnostics
/*    */ {
/*    */   private URI destURI;
/*    */   public static final String DEFAULT_DESTINATION = "https://ant.apache.org/";
/*    */   
/*    */   public ProxyDiagnostics(String destination) {
/*    */     try {
/* 53 */       this.destURI = new URI(destination);
/* 54 */     } catch (URISyntaxException e) {
/* 55 */       throw new BuildException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ProxyDiagnostics() {
/* 64 */     this("https://ant.apache.org/");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 73 */     ProxySelector selector = ProxySelector.getDefault();
/* 74 */     StringBuilder result = new StringBuilder();
/* 75 */     for (Proxy proxy : selector.select(this.destURI)) {
/* 76 */       SocketAddress address = proxy.address();
/* 77 */       if (address == null) {
/* 78 */         result.append("Direct connection\n");
/*    */         continue;
/*    */       } 
/* 81 */       result.append(proxy);
/* 82 */       if (address instanceof InetSocketAddress) {
/* 83 */         InetSocketAddress ina = (InetSocketAddress)address;
/* 84 */         result.append(' ');
/* 85 */         result.append(ina.getHostName());
/* 86 */         result.append(':');
/* 87 */         result.append(ina.getPort());
/* 88 */         if (ina.isUnresolved()) {
/* 89 */           result.append(" [unresolved]");
/*    */         } else {
/* 91 */           InetAddress addr = ina.getAddress();
/* 92 */           result.append(" [");
/* 93 */           result.append(addr.getHostAddress());
/* 94 */           result.append(']');
/*    */         } 
/*    */       } 
/* 97 */       result.append('\n');
/*    */     } 
/* 99 */     return result.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/java15/ProxyDiagnostics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */