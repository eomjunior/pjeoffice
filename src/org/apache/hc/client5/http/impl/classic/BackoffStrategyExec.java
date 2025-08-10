/*    */ package org.apache.hc.client5.http.impl.classic;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.client5.http.HttpRoute;
/*    */ import org.apache.hc.client5.http.classic.BackoffManager;
/*    */ import org.apache.hc.client5.http.classic.ConnectionBackoffStrategy;
/*    */ import org.apache.hc.client5.http.classic.ExecChain;
/*    */ import org.apache.hc.client5.http.classic.ExecChainHandler;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.Experimental;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*    */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpResponse;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ @Experimental
/*    */ public final class BackoffStrategyExec
/*    */   implements ExecChainHandler
/*    */ {
/*    */   private final ConnectionBackoffStrategy connectionBackoffStrategy;
/*    */   private final BackoffManager backoffManager;
/*    */   
/*    */   public BackoffStrategyExec(ConnectionBackoffStrategy connectionBackoffStrategy, BackoffManager backoffManager) {
/* 67 */     Args.notNull(connectionBackoffStrategy, "Connection backoff strategy");
/* 68 */     Args.notNull(backoffManager, "Backoff manager");
/* 69 */     this.connectionBackoffStrategy = connectionBackoffStrategy;
/* 70 */     this.backoffManager = backoffManager;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClassicHttpResponse execute(ClassicHttpRequest request, ExecChain.Scope scope, ExecChain chain) throws IOException, HttpException {
/*    */     ClassicHttpResponse response;
/* 78 */     Args.notNull(request, "HTTP request");
/* 79 */     Args.notNull(scope, "Scope");
/* 80 */     HttpRoute route = scope.route;
/*    */ 
/*    */     
/*    */     try {
/* 84 */       response = chain.proceed(request, scope);
/* 85 */     } catch (IOException|HttpException ex) {
/* 86 */       if (this.connectionBackoffStrategy.shouldBackoff(ex)) {
/* 87 */         this.backoffManager.backOff(route);
/*    */       }
/* 89 */       throw ex;
/*    */     } 
/* 91 */     if (this.connectionBackoffStrategy.shouldBackoff((HttpResponse)response)) {
/* 92 */       this.backoffManager.backOff(route);
/*    */     } else {
/* 94 */       this.backoffManager.probe(route);
/*    */     } 
/* 96 */     return response;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/BackoffStrategyExec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */