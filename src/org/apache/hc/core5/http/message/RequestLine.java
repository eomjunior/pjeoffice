/*    */ package org.apache.hc.core5.http.message;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpVersion;
/*    */ import org.apache.hc.core5.http.ProtocolVersion;
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
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*    */ public final class RequestLine
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 2810581718468737193L;
/*    */   private final ProtocolVersion protoversion;
/*    */   private final String method;
/*    */   private final String uri;
/*    */   
/*    */   public RequestLine(HttpRequest request) {
/* 55 */     Args.notNull(request, "Request");
/* 56 */     this.method = request.getMethod();
/* 57 */     this.uri = request.getRequestUri();
/* 58 */     this.protoversion = (request.getVersion() != null) ? request.getVersion() : (ProtocolVersion)HttpVersion.HTTP_1_1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RequestLine(String method, String uri, ProtocolVersion version) {
/* 65 */     this.method = (String)Args.notNull(method, "Method");
/* 66 */     this.uri = (String)Args.notNull(uri, "URI");
/* 67 */     this.protoversion = (version != null) ? version : (ProtocolVersion)HttpVersion.HTTP_1_1;
/*    */   }
/*    */   
/*    */   public String getMethod() {
/* 71 */     return this.method;
/*    */   }
/*    */   
/*    */   public ProtocolVersion getProtocolVersion() {
/* 75 */     return this.protoversion;
/*    */   }
/*    */   
/*    */   public String getUri() {
/* 79 */     return this.uri;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 84 */     StringBuilder buf = new StringBuilder();
/* 85 */     buf.append(this.method).append(" ").append(this.uri).append(" ").append(this.protoversion);
/* 86 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/RequestLine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */