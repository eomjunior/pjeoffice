/*    */ package org.apache.hc.core5.http.nio.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.nio.AsyncDataConsumer;
/*    */ import org.apache.hc.core5.http.nio.AsyncFilterChain;
/*    */ import org.apache.hc.core5.http.nio.AsyncFilterHandler;
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
/*    */ public final class AsyncServerFilterChainElement
/*    */ {
/*    */   private final AsyncFilterHandler handler;
/*    */   private final AsyncServerFilterChainElement next;
/*    */   private final AsyncFilterChain filterChain;
/*    */   
/*    */   public AsyncServerFilterChainElement(AsyncFilterHandler handler, AsyncServerFilterChainElement next) {
/* 52 */     this.handler = handler;
/* 53 */     this.next = next;
/* 54 */     this.filterChain = (next != null) ? next::handle : null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AsyncDataConsumer handle(HttpRequest request, EntityDetails entityDetails, HttpContext context, AsyncFilterChain.ResponseTrigger responseTrigger) throws HttpException, IOException {
/* 62 */     return this.handler.handle(request, entityDetails, context, responseTrigger, this.filterChain);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 67 */     return "{handler=" + this.handler
/* 68 */       .getClass() + ", next=" + ((this.next != null) ? (String)this.next.handler
/* 69 */       .getClass() : "null") + '}';
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/AsyncServerFilterChainElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */