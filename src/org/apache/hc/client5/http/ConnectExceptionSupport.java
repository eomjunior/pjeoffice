/*    */ package org.apache.hc.client5.http;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetAddress;
/*    */ import java.util.Arrays;
/*    */ import org.apache.hc.core5.annotation.Internal;
/*    */ import org.apache.hc.core5.net.NamedEndpoint;
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
/*    */ @Internal
/*    */ public final class ConnectExceptionSupport
/*    */ {
/*    */   public static ConnectTimeoutException createConnectTimeoutException(IOException cause, NamedEndpoint namedEndpoint, InetAddress... remoteAddresses) {
/* 51 */     String message = "Connect to " + ((namedEndpoint != null) ? (String)namedEndpoint : "remote endpoint") + ((remoteAddresses != null && remoteAddresses.length > 0) ? (" " + Arrays.<InetAddress>asList(remoteAddresses)) : "") + ((cause != null && cause.getMessage() != null) ? (" failed: " + cause.getMessage()) : " timed out");
/* 52 */     return new ConnectTimeoutException(message, namedEndpoint);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static HttpHostConnectException createHttpHostConnectException(IOException cause, NamedEndpoint namedEndpoint, InetAddress... remoteAddresses) {
/* 62 */     String message = "Connect to " + ((namedEndpoint != null) ? (String)namedEndpoint : "remote endpoint") + ((remoteAddresses != null && remoteAddresses.length > 0) ? (" " + Arrays.<InetAddress>asList(remoteAddresses)) : "") + ((cause != null && cause.getMessage() != null) ? (" failed: " + cause.getMessage()) : " refused");
/* 63 */     return new HttpHostConnectException(message, namedEndpoint);
/*    */   }
/*    */ 
/*    */   
/*    */   public static IOException enhance(IOException cause, NamedEndpoint namedEndpoint, InetAddress... remoteAddresses) {
/* 68 */     if (cause instanceof java.net.SocketTimeoutException) {
/* 69 */       IOException ex = createConnectTimeoutException(cause, namedEndpoint, remoteAddresses);
/* 70 */       ex.setStackTrace(cause.getStackTrace());
/* 71 */       return ex;
/* 72 */     }  if (cause instanceof java.net.ConnectException) {
/* 73 */       if ("Connection timed out".equals(cause.getMessage())) {
/* 74 */         IOException iOException = createConnectTimeoutException(cause, namedEndpoint, remoteAddresses);
/* 75 */         iOException.initCause(cause);
/* 76 */         return iOException;
/*    */       } 
/* 78 */       IOException ex = createHttpHostConnectException(cause, namedEndpoint, remoteAddresses);
/* 79 */       ex.setStackTrace(cause.getStackTrace());
/* 80 */       return ex;
/*    */     } 
/* 82 */     return cause;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/ConnectExceptionSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */