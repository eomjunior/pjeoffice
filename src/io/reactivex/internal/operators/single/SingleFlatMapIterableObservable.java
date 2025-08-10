/*     */ package io.reactivex.internal.operators.single;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.SingleSource;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.observers.BasicIntQueueDisposable;
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
/*     */ public final class SingleFlatMapIterableObservable<T, R>
/*     */   extends Observable<R>
/*     */ {
/*     */   final SingleSource<T> source;
/*     */   final Function<? super T, ? extends Iterable<? extends R>> mapper;
/*     */   
/*     */   public SingleFlatMapIterableObservable(SingleSource<T> source, Function<? super T, ? extends Iterable<? extends R>> mapper) {
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
/*     */     extends BasicIntQueueDisposable<R>
/*     */     implements SingleObserver<T>
/*     */   {
/*     */     private static final long serialVersionUID = -8938804753851907758L;
/*     */     
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
/*  70 */       this.downstream = actual;
/*  71 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  76 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  77 */         this.upstream = d;
/*     */         
/*  79 */         this.downstream.onSubscribe((Disposable)this);
/*     */       } 
/*     */     }
/*     */     public void onSuccess(T value) {
/*     */       Iterator<? extends R> iterator;
/*     */       boolean has;
/*  85 */       Observer<? super R> a = this.downstream;
/*     */ 
/*     */       
/*     */       try {
/*  89 */         iterator = ((Iterable<? extends R>)this.mapper.apply(value)).iterator();
/*     */         
/*  91 */         has = iterator.hasNext();
/*  92 */       } catch (Throwable ex) {
/*  93 */         Exceptions.throwIfFatal(ex);
/*  94 */         this.downstream.onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/*  98 */       if (!has) {
/*  99 */         a.onComplete();
/*     */         
/*     */         return;
/*     */       } 
/* 103 */       if (this.outputFused) {
/* 104 */         this.it = iterator;
/* 105 */         a.onNext(null);
/* 106 */         a.onComplete();
/*     */       } else {
/*     */         while (true) {
/* 109 */           R v; boolean b; if (this.cancelled) {
/*     */             return;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 116 */             v = iterator.next();
/* 117 */           } catch (Throwable ex) {
/* 118 */             Exceptions.throwIfFatal(ex);
/* 119 */             a.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 123 */           a.onNext(v);
/*     */           
/* 125 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/* 132 */             b = iterator.hasNext();
/* 133 */           } catch (Throwable ex) {
/* 134 */             Exceptions.throwIfFatal(ex);
/* 135 */             a.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 139 */           if (!b) {
/* 140 */             a.onComplete();
/*     */             return;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 149 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 150 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 155 */       this.cancelled = true;
/* 156 */       this.upstream.dispose();
/* 157 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 162 */       return this.cancelled;
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int mode) {
/* 167 */       if ((mode & 0x2) != 0) {
/* 168 */         this.outputFused = true;
/* 169 */         return 2;
/*     */       } 
/* 171 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 176 */       this.it = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 181 */       return (this.it == null);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public R poll() throws Exception {
/* 187 */       Iterator<? extends R> iterator = this.it;
/*     */       
/* 189 */       if (iterator != null) {
/* 190 */         R v = (R)ObjectHelper.requireNonNull(iterator.next(), "The iterator returned a null value");
/* 191 */         if (!iterator.hasNext()) {
/* 192 */           this.it = null;
/*     */         }
/* 194 */         return v;
/*     */       } 
/* 196 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/single/SingleFlatMapIterableObservable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */