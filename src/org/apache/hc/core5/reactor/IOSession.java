/*    */ package org.apache.hc.core5.reactor;
/*    */ 
/*    */ import java.net.SocketAddress;
/*    */ import java.nio.channels.ByteChannel;
/*    */ import java.util.concurrent.locks.Lock;
/*    */ import org.apache.hc.core5.annotation.Internal;
/*    */ import org.apache.hc.core5.http.SocketModalCloseable;
/*    */ import org.apache.hc.core5.util.Identifiable;
/*    */ import org.apache.hc.core5.util.Timeout;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Internal
/*    */ public interface IOSession
/*    */   extends ByteChannel, SocketModalCloseable, Identifiable
/*    */ {
/*    */   IOEventHandler getHandler();
/*    */   
/*    */   void upgrade(IOEventHandler paramIOEventHandler);
/*    */   
/*    */   Lock getLock();
/*    */   
/*    */   void enqueue(Command paramCommand, Command.Priority paramPriority);
/*    */   
/*    */   boolean hasCommands();
/*    */   
/*    */   Command poll();
/*    */   
/*    */   ByteChannel channel();
/*    */   
/*    */   SocketAddress getRemoteAddress();
/*    */   
/*    */   SocketAddress getLocalAddress();
/*    */   
/*    */   int getEventMask();
/*    */   
/*    */   void setEventMask(int paramInt);
/*    */   
/*    */   void setEvent(int paramInt);
/*    */   
/*    */   void clearEvent(int paramInt);
/*    */   
/*    */   void close();
/*    */   
/*    */   Status getStatus();
/*    */   
/*    */   Timeout getSocketTimeout();
/*    */   
/*    */   void setSocketTimeout(Timeout paramTimeout);
/*    */   
/*    */   long getLastReadTime();
/*    */   
/*    */   long getLastWriteTime();
/*    */   
/*    */   long getLastEventTime();
/*    */   
/*    */   void updateReadTime();
/*    */   
/*    */   void updateWriteTime();
/*    */   
/*    */   public enum Status
/*    */   {
/* 64 */     ACTIVE,
/* 65 */     CLOSING,
/* 66 */     CLOSED;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/IOSession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */