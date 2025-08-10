/*    */ package org.apache.hc.core5.http2.nio.command;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Internal;
/*    */ import org.apache.hc.core5.http2.nio.AsyncPingHandler;
/*    */ import org.apache.hc.core5.reactor.Command;
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
/*    */ @Internal
/*    */ public final class PingCommand
/*    */   implements Command
/*    */ {
/*    */   private final AsyncPingHandler handler;
/*    */   
/*    */   public PingCommand(AsyncPingHandler handler) {
/* 46 */     this.handler = (AsyncPingHandler)Args.notNull(handler, "Handler");
/*    */   }
/*    */   
/*    */   public AsyncPingHandler getHandler() {
/* 50 */     return this.handler;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean cancel() {
/* 55 */     this.handler.cancel();
/* 56 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/nio/command/PingCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */