/*    */ package org.apache.hc.core5.reactor;
/*    */ 
/*    */ import java.nio.channels.SocketChannel;
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
/*    */ final class ChannelEntry
/*    */ {
/*    */   final SocketChannel channel;
/*    */   final Object attachment;
/*    */   
/*    */   public ChannelEntry(SocketChannel channel, Object attachment) {
/* 39 */     this.channel = channel;
/* 40 */     this.attachment = attachment;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 45 */     return "[channel=" + this.channel + ", attachment=" + this.attachment + ']';
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/ChannelEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */