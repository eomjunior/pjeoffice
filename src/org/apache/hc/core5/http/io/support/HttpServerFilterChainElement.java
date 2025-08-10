/*    */ package org.apache.hc.core5.http.io.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.io.HttpFilterChain;
/*    */ import org.apache.hc.core5.http.io.HttpFilterHandler;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
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
/*    */ public final class HttpServerFilterChainElement
/*    */ {
/*    */   private final HttpFilterHandler handler;
/*    */   private final HttpServerFilterChainElement next;
/*    */   private final HttpFilterChain filterChain;
/*    */   
/*    */   public HttpServerFilterChainElement(HttpFilterHandler handler, HttpServerFilterChainElement next) {
/* 50 */     this.handler = handler;
/* 51 */     this.next = next;
/* 52 */     this.filterChain = (next != null) ? next::handle : null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(ClassicHttpRequest request, HttpFilterChain.ResponseTrigger responseTrigger, HttpContext context) throws IOException, HttpException {
/* 59 */     this.handler.handle(request, responseTrigger, context, this.filterChain);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 64 */     return "{handler=" + this.handler
/* 65 */       .getClass() + ", next=" + ((this.next != null) ? (String)this.next.handler
/* 66 */       .getClass() : "null") + '}';
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/support/HttpServerFilterChainElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */