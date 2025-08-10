/*    */ package org.apache.hc.client5.http.impl.async;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.client5.http.async.AsyncExecCallback;
/*    */ import org.apache.hc.client5.http.async.AsyncExecChain;
/*    */ import org.apache.hc.client5.http.async.AsyncExecChainHandler;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
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
/*    */ class AsyncExecChainElement
/*    */ {
/*    */   private final AsyncExecChainHandler handler;
/*    */   private final AsyncExecChainElement next;
/*    */   
/*    */   AsyncExecChainElement(AsyncExecChainHandler handler, AsyncExecChainElement next) {
/* 45 */     this.handler = handler;
/* 46 */     this.next = next;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute(HttpRequest request, AsyncEntityProducer entityProducer, AsyncExecChain.Scope scope, AsyncExecCallback asyncExecCallback) throws HttpException, IOException {
/* 54 */     this.handler.execute(request, entityProducer, scope, (this.next != null) ? this.next::execute : null, asyncExecCallback);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 59 */     return "{handler=" + this.handler
/* 60 */       .getClass() + ", next=" + ((this.next != null) ? (String)this.next.handler
/* 61 */       .getClass() : "null") + '}';
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/AsyncExecChainElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */