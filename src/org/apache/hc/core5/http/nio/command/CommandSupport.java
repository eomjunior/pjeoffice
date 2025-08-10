/*    */ package org.apache.hc.core5.http.nio.command;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Internal;
/*    */ import org.apache.hc.core5.http.RequestNotExecutedException;
/*    */ import org.apache.hc.core5.reactor.Command;
/*    */ import org.apache.hc.core5.reactor.IOSession;
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
/*    */ @Internal
/*    */ public final class CommandSupport
/*    */ {
/*    */   public static void failCommands(IOSession ioSession, Exception ex) {
/* 48 */     Args.notNull(ioSession, "I/O session");
/*    */     Command command;
/* 50 */     while ((command = ioSession.poll()) != null) {
/* 51 */       if (command instanceof ExecutableCommand) {
/* 52 */         ((ExecutableCommand)command).failed(ex); continue;
/*    */       } 
/* 54 */       command.cancel();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void cancelCommands(IOSession ioSession) {
/* 63 */     Args.notNull(ioSession, "I/O session");
/*    */     Command command;
/* 65 */     while ((command = ioSession.poll()) != null) {
/* 66 */       if (command instanceof ExecutableCommand) {
/* 67 */         if (!ioSession.isOpen()) {
/* 68 */           ((ExecutableCommand)command).failed((Exception)new RequestNotExecutedException()); continue;
/*    */         } 
/* 70 */         command.cancel();
/*    */         continue;
/*    */       } 
/* 73 */       command.cancel();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/command/CommandSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */