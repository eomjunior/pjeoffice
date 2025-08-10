/*     */ package io.reactivex.internal.operators.completable;
/*     */ 
/*     */ import io.reactivex.Completable;
/*     */ import io.reactivex.CompletableObserver;
/*     */ import io.reactivex.CompletableSource;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public final class CompletableAndThenCompletable
/*     */   extends Completable
/*     */ {
/*     */   final CompletableSource source;
/*     */   final CompletableSource next;
/*     */   
/*     */   public CompletableAndThenCompletable(CompletableSource source, CompletableSource next) {
/*  29 */     this.source = source;
/*  30 */     this.next = next;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(CompletableObserver observer) {
/*  35 */     this.source.subscribe(new SourceObserver(observer, this.next));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SourceObserver
/*     */     extends AtomicReference<Disposable>
/*     */     implements CompletableObserver, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -4101678820158072998L;
/*     */     
/*     */     final CompletableObserver actualObserver;
/*     */     final CompletableSource next;
/*     */     
/*     */     SourceObserver(CompletableObserver actualObserver, CompletableSource next) {
/*  49 */       this.actualObserver = actualObserver;
/*  50 */       this.next = next;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  55 */       if (DisposableHelper.setOnce(this, d)) {
/*  56 */         this.actualObserver.onSubscribe(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  62 */       this.actualObserver.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  67 */       this.next.subscribe(new CompletableAndThenCompletable.NextObserver(this, this.actualObserver));
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  72 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  77 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */   }
/*     */   
/*     */   static final class NextObserver
/*     */     implements CompletableObserver
/*     */   {
/*     */     final AtomicReference<Disposable> parent;
/*     */     final CompletableObserver downstream;
/*     */     
/*     */     NextObserver(AtomicReference<Disposable> parent, CompletableObserver downstream) {
/*  88 */       this.parent = parent;
/*  89 */       this.downstream = downstream;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  94 */       DisposableHelper.replace(this.parent, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  99 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 104 */       this.downstream.onError(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableAndThenCompletable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */