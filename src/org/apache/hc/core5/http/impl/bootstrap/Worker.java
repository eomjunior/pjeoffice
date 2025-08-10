/*    */ package org.apache.hc.core5.http.impl.bootstrap;
/*    */ 
/*    */ import org.apache.hc.core5.http.ExceptionListener;
/*    */ import org.apache.hc.core5.http.HttpConnection;
/*    */ import org.apache.hc.core5.http.impl.io.HttpService;
/*    */ import org.apache.hc.core5.http.io.HttpServerConnection;
/*    */ import org.apache.hc.core5.http.protocol.BasicHttpContext;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
/*    */ import org.apache.hc.core5.http.protocol.HttpCoreContext;
/*    */ import org.apache.hc.core5.io.CloseMode;
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
/*    */ class Worker
/*    */   implements Runnable
/*    */ {
/*    */   private final HttpService httpservice;
/*    */   private final HttpServerConnection conn;
/*    */   private final ExceptionListener exceptionListener;
/*    */   
/*    */   Worker(HttpService httpservice, HttpServerConnection conn, ExceptionListener exceptionListener) {
/* 47 */     this.httpservice = httpservice;
/* 48 */     this.conn = conn;
/* 49 */     this.exceptionListener = exceptionListener;
/*    */   }
/*    */   
/*    */   public HttpServerConnection getConnection() {
/* 53 */     return this.conn;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/*    */     try {
/* 59 */       BasicHttpContext localContext = new BasicHttpContext();
/* 60 */       HttpCoreContext context = HttpCoreContext.adapt((HttpContext)localContext);
/* 61 */       while (!Thread.interrupted() && this.conn.isOpen()) {
/* 62 */         this.httpservice.handleRequest(this.conn, (HttpContext)context);
/* 63 */         localContext.clear();
/*    */       } 
/* 65 */       this.conn.close();
/* 66 */     } catch (Exception ex) {
/* 67 */       this.exceptionListener.onError((HttpConnection)this.conn, ex);
/*    */     } finally {
/* 69 */       this.conn.close(CloseMode.IMMEDIATE);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/bootstrap/Worker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */