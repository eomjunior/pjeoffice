/*    */ package org.apache.hc.client5.http;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ import org.apache.hc.core5.http.HttpHost;
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
/*    */ public interface RouteInfo
/*    */ {
/*    */   HttpHost getTargetHost();
/*    */   
/*    */   InetAddress getLocalAddress();
/*    */   
/*    */   int getHopCount();
/*    */   
/*    */   HttpHost getHopTarget(int paramInt);
/*    */   
/*    */   HttpHost getProxyHost();
/*    */   
/*    */   TunnelType getTunnelType();
/*    */   
/*    */   boolean isTunnelled();
/*    */   
/*    */   LayerType getLayerType();
/*    */   
/*    */   boolean isLayered();
/*    */   
/*    */   boolean isSecure();
/*    */   
/*    */   public enum TunnelType
/*    */   {
/* 49 */     PLAIN, TUNNELLED;
/*    */   }
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
/*    */   public enum LayerType
/*    */   {
/* 65 */     PLAIN, LAYERED;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/RouteInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */