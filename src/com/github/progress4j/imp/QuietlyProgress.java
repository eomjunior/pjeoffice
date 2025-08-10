/*    */ package com.github.progress4j.imp;
/*    */ 
/*    */ import com.github.progress4j.IProgress;
/*    */ import com.github.progress4j.IQuietlyProgress;
/*    */ import com.github.progress4j.IStage;
/*    */ import com.github.utils4j.imp.Throwables;
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
/*    */ public class QuietlyProgress
/*    */   extends ProgressWrapper
/*    */   implements IQuietlyProgress
/*    */ {
/*    */   public static IQuietlyProgress wrap(IProgress progress) {
/* 39 */     return new QuietlyProgress(progress);
/*    */   }
/*    */   
/*    */   private QuietlyProgress(IProgress progress) {
/* 43 */     super(progress);
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin(IStage stage) {
/* 48 */     Throwables.quietly(() -> this.progress.begin(stage));
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin(IStage stage, int total) {
/* 53 */     Throwables.quietly(() -> this.progress.begin(stage, total));
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin(String stage) {
/* 58 */     Throwables.quietly(() -> this.progress.begin(stage));
/*    */   }
/*    */ 
/*    */   
/*    */   public void begin(String stage, int total) {
/* 63 */     Throwables.quietly(() -> this.progress.begin(stage, total));
/*    */   }
/*    */ 
/*    */   
/*    */   public void step(String mensagem, Object... params) {
/* 68 */     Throwables.quietly(() -> this.progress.step(mensagem, params));
/*    */   }
/*    */ 
/*    */   
/*    */   public void skip(long steps) {
/* 73 */     Throwables.quietly(() -> this.progress.skip(steps));
/*    */   }
/*    */ 
/*    */   
/*    */   public void info(String mensagem, Object... params) {
/* 78 */     Throwables.quietly(() -> this.progress.info(mensagem, params));
/*    */   }
/*    */ 
/*    */   
/*    */   public void end() {
/* 83 */     Throwables.quietly(() -> this.progress.end());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/QuietlyProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */