/*    */ package org.apache.hc.core5.http.nio.ssl;
/*    */ 
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.SocketAddress;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public final class FixedPortStrategy
/*    */   implements SecurePortStrategy
/*    */ {
/*    */   private final int[] securePorts;
/*    */   
/*    */   public FixedPortStrategy(int... securePorts) {
/* 48 */     this.securePorts = (int[])Args.notNull(securePorts, "Secure ports");
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSecure(SocketAddress localAddress) {
/* 53 */     int port = ((InetSocketAddress)localAddress).getPort();
/* 54 */     for (int securePort : this.securePorts) {
/* 55 */       if (port == securePort) {
/* 56 */         return true;
/*    */       }
/*    */     } 
/* 59 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/ssl/FixedPortStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */