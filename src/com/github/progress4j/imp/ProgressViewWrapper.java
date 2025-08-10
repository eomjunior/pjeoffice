/*    */ package com.github.progress4j.imp;
/*    */ 
/*    */ import com.github.progress4j.IProgress;
/*    */ import com.github.progress4j.IProgressView;
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
/*    */ public class ProgressViewWrapper
/*    */   extends ProgressWrapper
/*    */   implements IProgressView
/*    */ {
/*    */   protected ProgressViewWrapper(IProgressView progress) {
/* 35 */     super((IProgress)progress);
/*    */   }
/*    */   
/*    */   private IProgressView view() {
/* 39 */     return (IProgressView)this.progress;
/*    */   }
/*    */ 
/*    */   
/*    */   public void display() {
/* 44 */     view().display();
/*    */   }
/*    */ 
/*    */   
/*    */   public void undisplay() {
/* 49 */     view().undisplay();
/*    */   }
/*    */ 
/*    */   
/*    */   public IProgressView reset() {
/* 54 */     view().reset();
/* 55 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void cancelCode(Runnable code) throws InterruptedException {
/* 60 */     view().cancelCode(code);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFrom(Thread thread) {
/* 65 */     return view().isFrom(thread);
/*    */   }
/*    */ 
/*    */   
/*    */   public void cancel() {
/* 70 */     view().cancel();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ProgressViewWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */