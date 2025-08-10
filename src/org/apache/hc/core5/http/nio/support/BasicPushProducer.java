/*    */ package org.apache.hc.core5.http.nio.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpResponse;
/*    */ import org.apache.hc.core5.http.message.BasicHttpResponse;
/*    */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*    */ import org.apache.hc.core5.http.nio.AsyncPushProducer;
/*    */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*    */ import org.apache.hc.core5.http.nio.ResponseChannel;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
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
/*    */ public class BasicPushProducer
/*    */   implements AsyncPushProducer
/*    */ {
/*    */   private final HttpResponse response;
/*    */   private final AsyncEntityProducer dataProducer;
/*    */   
/*    */   public BasicPushProducer(HttpResponse response, AsyncEntityProducer dataProducer) {
/* 54 */     this.response = (HttpResponse)Args.notNull(response, "Response");
/* 55 */     this.dataProducer = (AsyncEntityProducer)Args.notNull(dataProducer, "Entity producer");
/*    */   }
/*    */   
/*    */   public BasicPushProducer(int code, AsyncEntityProducer dataProducer) {
/* 59 */     this((HttpResponse)new BasicHttpResponse(code), dataProducer);
/*    */   }
/*    */   
/*    */   public BasicPushProducer(AsyncEntityProducer dataProducer) {
/* 63 */     this(200, dataProducer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void produceResponse(ResponseChannel channel, HttpContext httpContext) throws HttpException, IOException {
/* 68 */     channel.sendResponse(this.response, (EntityDetails)this.dataProducer, httpContext);
/*    */   }
/*    */ 
/*    */   
/*    */   public int available() {
/* 73 */     return (this.dataProducer != null) ? this.dataProducer.available() : 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void produce(DataStreamChannel channel) throws IOException {
/* 78 */     if (this.dataProducer != null) {
/* 79 */       this.dataProducer.produce(channel);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void failed(Exception cause) {
/* 85 */     releaseResources();
/*    */   }
/*    */ 
/*    */   
/*    */   public void releaseResources() {
/* 90 */     if (this.dataProducer != null)
/* 91 */       this.dataProducer.releaseResources(); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/BasicPushProducer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */