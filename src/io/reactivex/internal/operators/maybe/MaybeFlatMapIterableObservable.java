/*     */ package io.reactivex.internal.operators.maybe;
/*     */ 
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.MaybeSource;
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.observers.BasicQueueDisposable;
/*     */ import java.util.Iterator;
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
/*     */ public final class MaybeFlatMapIterableObservable<T, R>
/*     */   extends Observable<R>
/*     */ {
/*     */   final MaybeSource<T> source;
/*     */   final Function<? super T, ? extends Iterable<? extends R>> mapper;
/*     */   
/*     */   public MaybeFlatMapIterableObservable(MaybeSource<T> source, Function<? super T, ? extends Iterable<? extends R>> mapper) {
/*  41 */     this.source = source;
/*  42 */     this.mapper = mapper;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super R> observer) {
/*  47 */     this.source.subscribe(new FlatMapIterableObserver<T, R>(observer, this.mapper));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class FlatMapIterableObserver<T, R>
/*     */     extends BasicQueueDisposable<R>
/*     */     implements MaybeObserver<T>
/*     */   {
/*     */     final Observer<? super R> downstream;
/*     */     
/*     */     final Function<? super T, ? extends Iterable<? extends R>> mapper;
/*     */     
/*     */     Disposable upstream;
/*     */     
/*     */     volatile Iterator<? extends R> it;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     boolean outputFused;
/*     */     
/*     */     FlatMapIterableObserver(Observer<? super R> actual, Function<? super T, ? extends Iterable<? extends R>> mapper) {
/*  68 */       this.downstream = actual;
/*  69 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  74 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  75 */         this.upstream = d;
/*     */         
/*  77 */         this.downstream.onSubscribe((Disposable)this);
/*     */       } 
/*     */     }
/*     */     public void onSuccess(T value) {
/*     */       Iterator<? extends R> iterator;
/*     */       boolean has;
/*  83 */       Observer<? super R> a = this.downstream;
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  88 */         iterator = ((Iterable<? extends R>)this.mapper.apply(value)).iterator();
/*     */         
/*  90 */         has = iterator.hasNext();
/*  91 */       } catch (Throwable ex) {
/*  92 */         Exceptions.throwIfFatal(ex);
/*  93 */         a.onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/*  97 */       if (!has) {
/*  98 */         a.onComplete();
/*     */         
/*     */         return;
/*     */       } 
/* 102 */       this.it = iterator;
/*     */       
/* 104 */       if (this.outputFused) {
/* 105 */         a.onNext(null);
/* 106 */         a.onComplete(); return;
/*     */       } 
/*     */       while (true) {
/*     */         R v;
/*     */         boolean b;
/* 111 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 118 */           v = iterator.next();
/* 119 */         } catch (Throwable ex) {
/* 120 */           Exceptions.throwIfFatal(ex);
/* 121 */           a.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 125 */         a.onNext(v);
/*     */         
/* 127 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 134 */           b = iterator.hasNext();
/* 135 */         } catch (Throwable ex) {
/* 136 */           Exceptions.throwIfFatal(ex);
/* 137 */           a.onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/* 141 */         if (!b) {
/* 142 */           a.onComplete();
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 150 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 151 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 156 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 161 */       this.cancelled = true;
/* 162 */       this.upstream.dispose();
/* 163 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 168 */       return this.cancelled;
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 173 */       if ((mode & 0x2) != 0) {
/* 174 */         this.outputFused = true;
/* 175 */         return 2;
/*     */       } 
/* 177 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 182 */       this.it = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 187 */       return (this.it == null);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public R poll() throws Exception {
/* 193 */       Iterator<? extends R> iterator = this.it;
/*     */       
/* 195 */       if (iterator != null) {
/* 196 */         R v = (R)ObjectHelper.requireNonNull(iterator.next(), "The iterator returned a null value");
/* 197 */         if (!iterator.hasNext()) {
/* 198 */           this.it = null;
/*     */         }
/* 200 */         return v;
/*     */       } 
/* 202 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/MaybeFlatMapIterableObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */