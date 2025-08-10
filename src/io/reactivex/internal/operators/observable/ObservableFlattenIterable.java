/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ 
/*     */ public final class ObservableFlattenIterable<T, R>
/*     */   extends AbstractObservableWithUpstream<T, R>
/*     */ {
/*     */   final Function<? super T, ? extends Iterable<? extends R>> mapper;
/*     */   
/*     */   public ObservableFlattenIterable(ObservableSource<T> source, Function<? super T, ? extends Iterable<? extends R>> mapper) {
/*  38 */     super(source);
/*  39 */     this.mapper = mapper;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super R> observer) {
/*  44 */     this.source.subscribe(new FlattenIterableObserver<T, R>(observer, this.mapper));
/*     */   }
/*     */   
/*     */   static final class FlattenIterableObserver<T, R>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Observer<? super R> downstream;
/*     */     final Function<? super T, ? extends Iterable<? extends R>> mapper;
/*     */     Disposable upstream;
/*     */     
/*     */     FlattenIterableObserver(Observer<? super R> actual, Function<? super T, ? extends Iterable<? extends R>> mapper) {
/*  55 */       this.downstream = actual;
/*  56 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  61 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  62 */         this.upstream = d;
/*     */         
/*  64 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T value) {
/*     */       Iterator<? extends R> it;
/*  70 */       if (this.upstream == DisposableHelper.DISPOSED) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  77 */         it = ((Iterable<? extends R>)this.mapper.apply(value)).iterator();
/*  78 */       } catch (Throwable ex) {
/*  79 */         Exceptions.throwIfFatal(ex);
/*  80 */         this.upstream.dispose();
/*  81 */         onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/*  85 */       Observer<? super R> a = this.downstream;
/*     */       
/*     */       while (true) {
/*     */         boolean b;
/*     */         
/*     */         try {
/*  91 */           b = it.hasNext();
/*  92 */         } catch (Throwable ex) {
/*  93 */           Exceptions.throwIfFatal(ex);
/*  94 */           this.upstream.dispose();
/*  95 */           onError(ex);
/*     */           
/*     */           return;
/*     */         } 
/*  99 */         if (b) {
/*     */           R v;
/*     */           
/*     */           try {
/* 103 */             v = (R)ObjectHelper.requireNonNull(it.next(), "The iterator returned a null value");
/* 104 */           } catch (Throwable ex) {
/* 105 */             Exceptions.throwIfFatal(ex);
/* 106 */             this.upstream.dispose();
/* 107 */             onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 111 */           a.onNext(v);
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 120 */       if (this.upstream == DisposableHelper.DISPOSED) {
/* 121 */         RxJavaPlugins.onError(e);
/*     */         return;
/*     */       } 
/* 124 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 125 */       this.downstream.onError(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 130 */       if (this.upstream == DisposableHelper.DISPOSED) {
/*     */         return;
/*     */       }
/* 133 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/* 134 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 139 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 144 */       this.upstream.dispose();
/* 145 */       this.upstream = (Disposable)DisposableHelper.DISPOSED;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableFlattenIterable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */