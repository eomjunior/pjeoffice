/*    */ package org.apache.hc.core5.http.impl;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Internal;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpResponse;
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
/*    */ 
/*    */ 
/*    */ @Internal
/*    */ public class ServerSupport
/*    */ {
/*    */   public static void validateResponse(HttpResponse response, EntityDetails responseEntityDetails) throws HttpException {
/* 52 */     int status = response.getCode();
/* 53 */     switch (status) {
/*    */       case 204:
/*    */       case 304:
/* 56 */         if (responseEntityDetails != null)
/* 57 */           throw new HttpException("Response " + status + " must not enclose an entity"); 
/*    */         break;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static String toErrorMessage(Exception ex) {
/* 63 */     String message = ex.getMessage();
/* 64 */     return (message != null) ? message : ex.toString();
/*    */   }
/*    */   
/*    */   public static int toStatusCode(Exception ex) {
/*    */     int code;
/* 69 */     if (ex instanceof org.apache.hc.core5.http.MethodNotSupportedException) {
/* 70 */       code = 501;
/* 71 */     } else if (ex instanceof org.apache.hc.core5.http.UnsupportedHttpVersionException) {
/* 72 */       code = 505;
/* 73 */     } else if (ex instanceof org.apache.hc.core5.http.NotImplementedException) {
/* 74 */       code = 501;
/* 75 */     } else if (ex instanceof org.apache.hc.core5.http.RequestHeaderFieldsTooLargeException) {
/* 76 */       code = 431;
/* 77 */     } else if (ex instanceof org.apache.hc.core5.http.MisdirectedRequestException) {
/* 78 */       code = 421;
/* 79 */     } else if (ex instanceof org.apache.hc.core5.http.ProtocolException) {
/* 80 */       code = 400;
/*    */     } else {
/* 82 */       code = 500;
/*    */     } 
/* 84 */     return code;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/ServerSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */