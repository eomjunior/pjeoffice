/*    */ package org.apache.hc.core5.http.nio.entity;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.List;
/*    */ import org.apache.hc.core5.http.Header;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.nio.AsyncDataConsumer;
/*    */ import org.apache.hc.core5.http.nio.CapacityChannel;
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
/*    */ public abstract class AbstractBinDataConsumer
/*    */   implements AsyncDataConsumer
/*    */ {
/* 45 */   private static final ByteBuffer EMPTY = ByteBuffer.wrap(new byte[0]);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract int capacityIncrement();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract void data(ByteBuffer paramByteBuffer, boolean paramBoolean) throws IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract void completed() throws IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 70 */     capacityChannel.update(capacityIncrement());
/*    */   }
/*    */ 
/*    */   
/*    */   public final void consume(ByteBuffer src) throws IOException {
/* 75 */     data(src, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 80 */     data(EMPTY, true);
/* 81 */     completed();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/entity/AbstractBinDataConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */