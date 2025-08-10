/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.observers.QueueDrainObserver;
/*     */ import io.reactivex.internal.queue.MpscLinkedQueue;
/*     */ import io.reactivex.internal.util.ObservableQueueDrain;
/*     */ import io.reactivex.internal.util.QueueDrainHelper;
/*     */ import io.reactivex.observers.DisposableObserver;
/*     */ import io.reactivex.observers.SerializedObserver;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.Callable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableBufferExactBoundary<T, U extends Collection<? super T>, B>
/*     */   extends AbstractObservableWithUpstream<T, U>
/*     */ {
/*     */   final ObservableSource<B> boundary;
/*     */   final Callable<U> bufferSupplier;
/*     */   
/*     */   public ObservableBufferExactBoundary(ObservableSource<T> source, ObservableSource<B> boundary, Callable<U> bufferSupplier) {
/*  35 */     super(source);
/*  36 */     this.boundary = boundary;
/*  37 */     this.bufferSupplier = bufferSupplier;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super U> t) {
/*  42 */     this.source.subscribe(new BufferExactBoundaryObserver<Object, U, B>((Observer<? super U>)new SerializedObserver(t), this.bufferSupplier, this.boundary));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BufferExactBoundaryObserver<T, U extends Collection<? super T>, B>
/*     */     extends QueueDrainObserver<T, U, U>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Callable<U> bufferSupplier;
/*     */     
/*     */     final ObservableSource<B> boundary;
/*     */     
/*     */     Disposable upstream;
/*     */     Disposable other;
/*     */     U buffer;
/*     */     
/*     */     BufferExactBoundaryObserver(Observer<? super U> actual, Callable<U> bufferSupplier, ObservableSource<B> boundary) {
/*  59 */       super(actual, (SimplePlainQueue)new MpscLinkedQueue());
/*  60 */       this.bufferSupplier = bufferSupplier;
/*  61 */       this.boundary = boundary;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  66 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  67 */         Collection collection; this.upstream = d;
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/*  72 */           collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
/*  73 */         } catch (Throwable e) {
/*  74 */           Exceptions.throwIfFatal(e);
/*  75 */           this.cancelled = true;
/*  76 */           d.dispose();
/*  77 */           EmptyDisposable.error(e, this.downstream);
/*     */           
/*     */           return;
/*     */         } 
/*  81 */         this.buffer = (U)collection;
/*     */         
/*  83 */         ObservableBufferExactBoundary.BufferBoundaryObserver<T, U, B> bs = new ObservableBufferExactBoundary.BufferBoundaryObserver<T, U, B>(this);
/*  84 */         this.other = (Disposable)bs;
/*     */         
/*  86 */         this.downstream.onSubscribe(this);
/*     */         
/*  88 */         if (!this.cancelled) {
/*  89 */           this.boundary.subscribe((Observer)bs);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  96 */       synchronized (this) {
/*  97 */         U b = this.buffer;
/*  98 */         if (b == null) {
/*     */           return;
/*     */         }
/* 101 */         b.add(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 107 */       dispose();
/* 108 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*     */       U b;
/* 114 */       synchronized (this) {
/* 115 */         b = this.buffer;
/* 116 */         if (b == null) {
/*     */           return;
/*     */         }
/* 119 */         this.buffer = null;
/*     */       } 
/* 121 */       this.queue.offer(b);
/* 122 */       this.done = true;
/* 123 */       if (enter()) {
/* 124 */         QueueDrainHelper.drainLoop(this.queue, this.downstream, false, this, (ObservableQueueDrain)this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 130 */       if (!this.cancelled) {
/* 131 */         this.cancelled = true;
/* 132 */         this.other.dispose();
/* 133 */         this.upstream.dispose();
/*     */         
/* 135 */         if (enter()) {
/* 136 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 143 */       return this.cancelled;
/*     */     }
/*     */ 
/*     */     
/*     */     void next() {
/*     */       Collection collection;
/*     */       U b;
/*     */       try {
/* 151 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
/* 152 */       } catch (Throwable e) {
/* 153 */         Exceptions.throwIfFatal(e);
/* 154 */         dispose();
/* 155 */         this.downstream.onError(e);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 160 */       synchronized (this) {
/* 161 */         b = this.buffer;
/* 162 */         if (b == null) {
/*     */           return;
/*     */         }
/* 165 */         this.buffer = (U)collection;
/*     */       } 
/*     */       
/* 168 */       fastPathEmit(b, false, this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Observer<? super U> a, U v) {
/* 173 */       this.downstream.onNext(v);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class BufferBoundaryObserver<T, U extends Collection<? super T>, B>
/*     */     extends DisposableObserver<B>
/*     */   {
/*     */     final ObservableBufferExactBoundary.BufferExactBoundaryObserver<T, U, B> parent;
/*     */     
/*     */     BufferBoundaryObserver(ObservableBufferExactBoundary.BufferExactBoundaryObserver<T, U, B> parent) {
/* 183 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(B t) {
/* 188 */       this.parent.next();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 193 */       this.parent.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 198 */       this.parent.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableBufferExactBoundary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */