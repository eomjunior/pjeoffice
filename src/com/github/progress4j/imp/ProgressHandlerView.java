/*     */ package com.github.progress4j.imp;
/*     */ 
/*     */ import com.github.progress4j.IProgressHandler;
/*     */ import com.github.progress4j.IStageEvent;
/*     */ import com.github.progress4j.IStepEvent;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Ids;
/*     */ import io.reactivex.Observable;
/*     */ import java.awt.Container;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class ProgressHandlerView<T extends Container>
/*     */   extends ContainerProgressView<T>
/*     */ {
/*     */   private final IProgressHandler<T> handler;
/*     */   
/*     */   protected ProgressHandlerView(IProgressHandler<T> handler) {
/*  44 */     this(Ids.next(), handler);
/*     */   }
/*     */   
/*     */   protected ProgressHandlerView(String name, IProgressHandler<T> handler) {
/*  48 */     super(name);
/*  49 */     this.handler = (IProgressHandler<T>)Args.requireNonNull(handler, "handler is null");
/*  50 */     bind();
/*     */   }
/*     */ 
/*     */   
/*     */   public final T asContainer() {
/*  55 */     return (T)this.handler.asContainer();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<Boolean> detailStatus() {
/*  60 */     return this.handler.detailStatus();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void showSteps(boolean visible) {
/*  65 */     this.handler.showSteps(visible);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isStepsVisible() {
/*  70 */     return this.handler.isStepsVisible();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Observable<Boolean> cancelClick() {
/*  75 */     return this.handler.cancelClick();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isCanceled() {
/*  80 */     return this.handler.isCanceled();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doDispose() {
/*  85 */     this.handler.dispose();
/*  86 */     super.doDispose();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void interrupt() {
/*  91 */     this.handler.cancel();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void cancelCode(Runnable cancelCode) throws InterruptedException {
/*  96 */     this.handler.cancelCode(cancelCode);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void stepToken(IStepEvent event) {
/* 101 */     this.handler.stepToken(event);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void stageToken(IStageEvent event) {
/* 106 */     this.handler.stageToken(event);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void bind(Thread thread) {
/* 111 */     this.handler.bind(thread);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isFrom(Thread thread) {
/* 116 */     return this.handler.isFrom(thread);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void cancel() {
/* 121 */     this.handler.cancel();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/progress4j/imp/ProgressHandlerView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */