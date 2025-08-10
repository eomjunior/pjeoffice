/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MaybeSwitchIfEmpty<T>
/*     */   extends AbstractMaybeWithUpstream<T, T>
/*     */ {
/*     */   final MaybeSource<? extends T> other;
/*     */   
/*     */   public MaybeSwitchIfEmpty(MaybeSource<T> source, MaybeSource<? extends T> other) {
/*  32 */     super(source);
/*  33 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  38 */     this.source.subscribe(new SwitchIfEmptyMaybeObserver<T>(observer, this.other));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SwitchIfEmptyMaybeObserver<T>
/*     */     extends AtomicReference<Disposable>
/*     */     implements MaybeObserver<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -2223459372976438024L;
/*     */     
/*     */     final MaybeObserver<? super T> downstream;
/*     */     final MaybeSource<? extends T> other;
/*     */     
/*     */     SwitchIfEmptyMaybeObserver(MaybeObserver<? super T> actual, MaybeSource<? extends T> other) {
/*  52 */       this.downstream = actual;
/*  53 */       this.other = other;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  58 */       DisposableHelper.dispose(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/*  63 */       return DisposableHelper.isDisposed(get());
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  68 */       if (DisposableHelper.setOnce(this, d)) {
/*  69 */         this.downstream.onSubscribe(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSuccess(T value) {
/*  75 */       this.downstream.onSuccess(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  80 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  85 */       Disposable d = get();
/*  86 */       if (d != DisposableHelper.DISPOSED && 
/*  87 */         compareAndSet(d, null)) {
/*  88 */         this.other.subscribe(new OtherMaybeObserver<T>(this.downstream, this));
/*     */       }
/*     */     }
/*     */     
/*     */     static final class OtherMaybeObserver<T>
/*     */       implements MaybeObserver<T>
/*     */     {
/*     */       final MaybeObserver<? super T> downstream;
/*     */       final AtomicReference<Disposable> parent;
/*     */       
/*     */       OtherMaybeObserver(MaybeObserver<? super T> actual, AtomicReference<Disposable> parent) {
/*  99 */         this.downstream = actual;
/* 100 */         this.parent = parent;
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSubscribe(Disposable d) {
/* 105 */         DisposableHelper.setOnce(this.parent, d);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onSuccess(T value) {
/* 110 */         this.downstream.onSuccess(value);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onError(Throwable e) {
/* 115 */         this.downstream.onError(e);
/*     */       }
/*     */ 
/*     */       
/*     */       public void onComplete() {
/* 120 */         this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeSwitchIfEmpty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */