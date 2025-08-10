/*    */ package org.apache.hc.client5.http.classic;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.client5.http.HttpRoute;
/*    */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*    */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*    */ import org.apache.hc.core5.http.HttpException;
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
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public interface ExecChain
/*    */ {
/*    */   ClassicHttpResponse proceed(ClassicHttpRequest paramClassicHttpRequest, Scope paramScope) throws IOException, HttpException;
/*    */   
/*    */   public static final class Scope
/*    */   {
/*    */     public final String exchangeId;
/*    */     public final HttpRoute route;
/*    */     public final ClassicHttpRequest originalRequest;
/*    */     public final ExecRuntime execRuntime;
/*    */     public final HttpClientContext clientContext;
/*    */     
/*    */     public Scope(String exchangeId, HttpRoute route, ClassicHttpRequest originalRequest, ExecRuntime execRuntime, HttpClientContext clientContext) {
/* 58 */       this.exchangeId = (String)Args.notNull(exchangeId, "Exchange id");
/* 59 */       this.route = (HttpRoute)Args.notNull(route, "Route");
/* 60 */       this.originalRequest = (ClassicHttpRequest)Args.notNull(originalRequest, "Original request");
/* 61 */       this.execRuntime = (ExecRuntime)Args.notNull(execRuntime, "Exec runtime");
/* 62 */       this.clientContext = (clientContext != null) ? clientContext : HttpClientContext.create();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/classic/ExecChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */