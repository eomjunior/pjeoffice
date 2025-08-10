/*    */ package org.apache.hc.core5.http2.nio.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import org.apache.hc.core5.function.Callback;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http2.nio.AsyncPingHandler;
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
/*    */ public class BasicPingHandler
/*    */   implements AsyncPingHandler
/*    */ {
/* 44 */   private static final byte[] PING_MESSAGE = new byte[] { 42, 42, 112, 105, 110, 103, 42, 42 };
/*    */   
/*    */   private final Callback<Boolean> callback;
/*    */   
/*    */   public BasicPingHandler(Callback<Boolean> callback) {
/* 49 */     this.callback = (Callback<Boolean>)Args.notNull(callback, "Callback");
/*    */   }
/*    */ 
/*    */   
/*    */   public ByteBuffer getData() {
/* 54 */     return ByteBuffer.wrap(PING_MESSAGE);
/*    */   }
/*    */ 
/*    */   
/*    */   public void consumeResponse(ByteBuffer feedback) throws HttpException, IOException {
/* 59 */     boolean result = true;
/* 60 */     for (int i = 0; i < PING_MESSAGE.length; i++) {
/* 61 */       if (!feedback.hasRemaining() || PING_MESSAGE[i] != feedback.get()) {
/* 62 */         result = false;
/*    */         break;
/*    */       } 
/*    */     } 
/* 66 */     this.callback.execute(Boolean.valueOf(result));
/*    */   }
/*    */ 
/*    */   
/*    */   public void failed(Exception cause) {
/* 71 */     this.callback.execute(Boolean.FALSE);
/*    */   }
/*    */ 
/*    */   
/*    */   public void cancel() {
/* 76 */     this.callback.execute(Boolean.FALSE);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/nio/support/BasicPingHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */