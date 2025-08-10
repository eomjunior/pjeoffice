/*    */ package org.apache.hc.core5.http.nio.command;
/*    */ 
/*    */ import org.apache.hc.core5.function.Callback;
/*    */ import org.apache.hc.core5.io.CloseMode;
/*    */ import org.apache.hc.core5.reactor.Command;
/*    */ import org.apache.hc.core5.reactor.IOSession;
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
/*    */ public final class ShutdownCommand
/*    */   implements Command
/*    */ {
/* 44 */   public static final ShutdownCommand GRACEFUL = new ShutdownCommand(CloseMode.GRACEFUL);
/* 45 */   public static final ShutdownCommand IMMEDIATE = new ShutdownCommand(CloseMode.IMMEDIATE);
/*    */   
/* 47 */   public static final Callback<IOSession> GRACEFUL_IMMEDIATE_CALLBACK = createIOSessionCallback(Command.Priority.IMMEDIATE);
/* 48 */   public static final Callback<IOSession> GRACEFUL_NORMAL_CALLBACK = createIOSessionCallback(Command.Priority.NORMAL);
/*    */   
/*    */   private static Callback<IOSession> createIOSessionCallback(Command.Priority priority) {
/* 51 */     return session -> session.enqueue(GRACEFUL, priority);
/*    */   }
/*    */   
/*    */   private final CloseMode type;
/*    */   
/*    */   public ShutdownCommand(CloseMode type) {
/* 57 */     this.type = type;
/*    */   }
/*    */   
/*    */   public CloseMode getType() {
/* 61 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean cancel() {
/* 66 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     return "Shutdown: " + this.type;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/command/ShutdownCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */