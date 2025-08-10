/*    */ package io.reactivex.internal.operators.completable;
/*    */ 
/*    */ import io.reactivex.Completable;
/*    */ import io.reactivex.CompletableObserver;
/*    */ import io.reactivex.CompletableSource;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.exceptions.CompositeException;
/*    */ import io.reactivex.exceptions.Exceptions;
/*    */ import io.reactivex.functions.Function;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.functions.ObjectHelper;
/*    */ import java.util.concurrent.atomic.AtomicReference;
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
/*    */ public final class CompletableResumeNext
/*    */   extends Completable
/*    */ {
/*    */   final CompletableSource source;
/*    */   final Function<? super Throwable, ? extends CompletableSource> errorMapper;
/*    */   
/*    */   public CompletableResumeNext(CompletableSource source, Function<? super Throwable, ? extends CompletableSource> errorMapper) {
/* 33 */     this.source = source;
/* 34 */     this.errorMapper = errorMapper;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(CompletableObserver observer) {
/* 39 */     ResumeNextObserver parent = new ResumeNextObserver(observer, this.errorMapper);
/* 40 */     observer.onSubscribe(parent);
/* 41 */     this.source.subscribe(parent);
/*    */   }
/*    */ 
/*    */   
/*    */   static final class ResumeNextObserver
/*    */     extends AtomicReference<Disposable>
/*    */     implements CompletableObserver, Disposable
/*    */   {
/*    */     private static final long serialVersionUID = 5018523762564524046L;
/*    */     
/*    */     final CompletableObserver downstream;
/*    */     
/*    */     final Function<? super Throwable, ? extends CompletableSource> errorMapper;
/*    */     boolean once;
/*    */     
/*    */     ResumeNextObserver(CompletableObserver observer, Function<? super Throwable, ? extends CompletableSource> errorMapper) {
/* 57 */       this.downstream = observer;
/* 58 */       this.errorMapper = errorMapper;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 63 */       DisposableHelper.replace(this, d);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 68 */       this.downstream.onComplete();
/*    */     }
/*    */     
/*    */     public void onError(Throwable e) {
/*    */       CompletableSource c;
/* 73 */       if (this.once) {
/* 74 */         this.downstream.onError(e);
/*    */         return;
/*    */       } 
/* 77 */       this.once = true;
/*    */ 
/*    */ 
/*    */       
/*    */       try {
/* 82 */         c = (CompletableSource)ObjectHelper.requireNonNull(this.errorMapper.apply(e), "The errorMapper returned a null CompletableSource");
/* 83 */       } catch (Throwable ex) {
/* 84 */         Exceptions.throwIfFatal(ex);
/* 85 */         this.downstream.onError((Throwable)new CompositeException(new Throwable[] { e, ex }));
/*    */         
/*    */         return;
/*    */       } 
/* 89 */       c.subscribe(this);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 94 */       return DisposableHelper.isDisposed(get());
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 99 */       DisposableHelper.dispose(this);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/completable/CompletableResumeNext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */