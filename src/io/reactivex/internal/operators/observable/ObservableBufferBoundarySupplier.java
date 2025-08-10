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
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public final class ObservableBufferBoundarySupplier<T, U extends Collection<? super T>, B>
/*     */   extends AbstractObservableWithUpstream<T, U>
/*     */ {
/*     */   final Callable<? extends ObservableSource<B>> boundarySupplier;
/*     */   final Callable<U> bufferSupplier;
/*     */   
/*     */   public ObservableBufferBoundarySupplier(ObservableSource<T> source, Callable<? extends ObservableSource<B>> boundarySupplier, Callable<U> bufferSupplier) {
/*  37 */     super(source);
/*  38 */     this.boundarySupplier = boundarySupplier;
/*  39 */     this.bufferSupplier = bufferSupplier;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super U> t) {
/*  44 */     this.source.subscribe(new BufferBoundarySupplierObserver<Object, U, B>((Observer<? super U>)new SerializedObserver(t), this.bufferSupplier, this.boundarySupplier));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BufferBoundarySupplierObserver<T, U extends Collection<? super T>, B>
/*     */     extends QueueDrainObserver<T, U, U>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Callable<U> bufferSupplier;
/*     */     final Callable<? extends ObservableSource<B>> boundarySupplier;
/*     */     Disposable upstream;
/*  55 */     final AtomicReference<Disposable> other = new AtomicReference<Disposable>();
/*     */     
/*     */     U buffer;
/*     */ 
/*     */     
/*     */     BufferBoundarySupplierObserver(Observer<? super U> actual, Callable<U> bufferSupplier, Callable<? extends ObservableSource<B>> boundarySupplier) {
/*  61 */       super(actual, (SimplePlainQueue)new MpscLinkedQueue());
/*  62 */       this.bufferSupplier = bufferSupplier;
/*  63 */       this.boundarySupplier = boundarySupplier;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  68 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  69 */         Collection collection; ObservableSource<B> boundary; this.upstream = d;
/*     */         
/*  71 */         Observer<? super U> actual = this.downstream;
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/*  76 */           collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
/*  77 */         } catch (Throwable e) {
/*  78 */           Exceptions.throwIfFatal(e);
/*  79 */           this.cancelled = true;
/*  80 */           d.dispose();
/*  81 */           EmptyDisposable.error(e, actual);
/*     */           
/*     */           return;
/*     */         } 
/*  85 */         this.buffer = (U)collection;
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/*  90 */           boundary = (ObservableSource<B>)ObjectHelper.requireNonNull(this.boundarySupplier.call(), "The boundary ObservableSource supplied is null");
/*  91 */         } catch (Throwable ex) {
/*  92 */           Exceptions.throwIfFatal(ex);
/*  93 */           this.cancelled = true;
/*  94 */           d.dispose();
/*  95 */           EmptyDisposable.error(ex, actual);
/*     */           
/*     */           return;
/*     */         } 
/*  99 */         ObservableBufferBoundarySupplier.BufferBoundaryObserver<T, U, B> bs = new ObservableBufferBoundarySupplier.BufferBoundaryObserver<T, U, B>(this);
/* 100 */         this.other.set(bs);
/*     */         
/* 102 */         actual.onSubscribe(this);
/*     */         
/* 104 */         if (!this.cancelled) {
/* 105 */           boundary.subscribe((Observer)bs);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 112 */       synchronized (this) {
/* 113 */         U b = this.buffer;
/* 114 */         if (b == null) {
/*     */           return;
/*     */         }
/* 117 */         b.add(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 123 */       dispose();
/* 124 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*     */       U b;
/* 130 */       synchronized (this) {
/* 131 */         b = this.buffer;
/* 132 */         if (b == null) {
/*     */           return;
/*     */         }
/* 135 */         this.buffer = null;
/*     */       } 
/* 137 */       this.queue.offer(b);
/* 138 */       this.done = true;
/* 139 */       if (enter()) {
/* 140 */         QueueDrainHelper.drainLoop(this.queue, this.downstream, false, this, (ObservableQueueDrain)this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 146 */       if (!this.cancelled) {
/* 147 */         this.cancelled = true;
/* 148 */         this.upstream.dispose();
/* 149 */         disposeOther();
/*     */         
/* 151 */         if (enter()) {
/* 152 */           this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 159 */       return this.cancelled;
/*     */     }
/*     */     
/*     */     void disposeOther() {
/* 163 */       DisposableHelper.dispose(this.other);
/*     */     }
/*     */ 
/*     */     
/*     */     void next() {
/*     */       Collection collection;
/*     */       ObservableSource<B> boundary;
/*     */       try {
/* 171 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
/* 172 */       } catch (Throwable e) {
/* 173 */         Exceptions.throwIfFatal(e);
/* 174 */         dispose();
/* 175 */         this.downstream.onError(e);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*     */       try {
/* 182 */         boundary = (ObservableSource<B>)ObjectHelper.requireNonNull(this.boundarySupplier.call(), "The boundary ObservableSource supplied is null");
/* 183 */       } catch (Throwable ex) {
/* 184 */         Exceptions.throwIfFatal(ex);
/* 185 */         this.cancelled = true;
/* 186 */         this.upstream.dispose();
/* 187 */         this.downstream.onError(ex);
/*     */         
/*     */         return;
/*     */       } 
/* 191 */       ObservableBufferBoundarySupplier.BufferBoundaryObserver<T, U, B> bs = new ObservableBufferBoundarySupplier.BufferBoundaryObserver<T, U, B>(this);
/*     */       
/* 193 */       if (DisposableHelper.replace(this.other, (Disposable)bs)) {
/*     */         U b;
/* 195 */         synchronized (this) {
/* 196 */           b = this.buffer;
/* 197 */           if (b == null) {
/*     */             return;
/*     */           }
/* 200 */           this.buffer = (U)collection;
/*     */         } 
/*     */         
/* 203 */         boundary.subscribe((Observer)bs);
/*     */         
/* 205 */         fastPathEmit(b, false, this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Observer<? super U> a, U v) {
/* 211 */       this.downstream.onNext(v);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BufferBoundaryObserver<T, U extends Collection<? super T>, B>
/*     */     extends DisposableObserver<B>
/*     */   {
/*     */     final ObservableBufferBoundarySupplier.BufferBoundarySupplierObserver<T, U, B> parent;
/*     */     boolean once;
/*     */     
/*     */     BufferBoundaryObserver(ObservableBufferBoundarySupplier.BufferBoundarySupplierObserver<T, U, B> parent) {
/* 223 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(B t) {
/* 228 */       if (this.once) {
/*     */         return;
/*     */       }
/* 231 */       this.once = true;
/* 232 */       dispose();
/* 233 */       this.parent.next();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 238 */       if (this.once) {
/* 239 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 242 */       this.once = true;
/* 243 */       this.parent.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 248 */       if (this.once) {
/*     */         return;
/*     */       }
/* 251 */       this.once = true;
/* 252 */       this.parent.next();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableBufferBoundarySupplier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */