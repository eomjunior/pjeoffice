/*    */ package com.github.utils4j.gui.imp;
/*    */ 
/*    */ import com.github.utils4j.imp.Throwables;
/*    */ import java.util.concurrent.FutureTask;
/*    */ import javafx.application.Platform;
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
/*    */ public final class JfxTools
/*    */ {
/*    */   public static void runLatter(Runnable runnable) {
/* 41 */     Platform.runLater(runnable);
/*    */   }
/*    */   
/*    */   public static void runAndWait(Runnable runnable) {
/* 45 */     Throwables.runtime(() -> {
/*    */           if (Platform.isFxApplicationThread()) {
/*    */             runnable.run();
/*    */           } else {
/*    */             FutureTask<Object> futureTask = new FutureTask(runnable, null);
/*    */             Platform.runLater(futureTask);
/*    */             futureTask.get();
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/JfxTools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */