/*    */ package com.github.progress4j.imp;
/*    */ 
/*    */ import com.github.progress4j.IContainerProgressView;
/*    */ import com.github.progress4j.IProgress;
/*    */ import com.github.progress4j.IProgressView;
/*    */ import com.github.progress4j.IStageEvent;
/*    */ import com.github.progress4j.IStepEvent;
/*    */ import com.github.utils4j.imp.Dates;
/*    */ import com.github.utils4j.imp.Ids;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import java.awt.Container;
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
/*    */ abstract class ContainerProgressView<T extends Container>
/*    */   extends ProgressWrapper
/*    */   implements IContainerProgressView<T>
/*    */ {
/*    */   private Disposable stepToken;
/*    */   private Disposable stageToken;
/*    */   private Disposable disposeToken;
/*    */   
/*    */   protected ContainerProgressView() {
/* 45 */     this(Ids.next());
/*    */   }
/*    */   
/*    */   protected ContainerProgressView(String name) {
/* 49 */     super(new DefaultProgress("requisição: " + Dates.timeNow() + " id: " + name));
/*    */   }
/*    */ 
/*    */   
/*    */   public final void dispose() {
/* 54 */     super.dispose();
/* 55 */     disposeTokens();
/* 56 */     doDispose();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doDispose() {}
/*    */ 
/*    */   
/*    */   public final IProgressView reset() {
/* 64 */     super.reset();
/* 65 */     interrupt();
/* 66 */     disposeTokens();
/* 67 */     bind();
/* 68 */     undisplay();
/* 69 */     return (IProgressView)this;
/*    */   }
/*    */   
/*    */   private void disposeTokens() {
/* 73 */     if (this.stepToken != null) {
/* 74 */       this.stepToken.dispose();
/*    */     }
/* 76 */     if (this.stageToken != null) {
/* 77 */       this.stageToken.dispose();
/*    */     }
/* 79 */     if (this.disposeToken != null)
/* 80 */       this.disposeToken.dispose(); 
/*    */   }
/*    */   
/*    */   protected final void bind() {
/* 84 */     bind(Thread.currentThread());
/* 85 */     this.stepToken = stepObservable().subscribe(this::stepToken);
/* 86 */     this.stageToken = stageObservable().subscribe(this::stageToken);
/* 87 */     this.disposeToken = disposeObservable().subscribe(p -> disposeTokens());
/*    */   }
/*    */   
/*    */   protected abstract void bind(Thread paramThread);
/*    */   
/*    */   protected abstract void stepToken(IStepEvent paramIStepEvent);
/*    */   
/*    */   protected abstract void stageToken(IStageEvent paramIStageEvent);
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ContainerProgressView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */