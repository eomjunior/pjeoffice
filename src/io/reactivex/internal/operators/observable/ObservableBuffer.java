/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class ObservableBuffer<T, U extends Collection<? super T>>
/*     */   extends AbstractObservableWithUpstream<T, U>
/*     */ {
/*     */   final int count;
/*     */   final int skip;
/*     */   final Callable<U> bufferSupplier;
/*     */   
/*     */   public ObservableBuffer(ObservableSource<T> source, int count, int skip, Callable<U> bufferSupplier) {
/*  33 */     super(source);
/*  34 */     this.count = count;
/*  35 */     this.skip = skip;
/*  36 */     this.bufferSupplier = bufferSupplier;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super U> t) {
/*  41 */     if (this.skip == this.count) {
/*  42 */       BufferExactObserver<T, U> bes = new BufferExactObserver<T, U>(t, this.count, this.bufferSupplier);
/*  43 */       if (bes.createBuffer()) {
/*  44 */         this.source.subscribe(bes);
/*     */       }
/*     */     } else {
/*  47 */       this.source.subscribe(new BufferSkipObserver<Object, U>(t, this.count, this.skip, this.bufferSupplier));
/*     */     } 
/*     */   }
/*     */   
/*     */   static final class BufferExactObserver<T, U extends Collection<? super T>>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final Observer<? super U> downstream;
/*     */     final int count;
/*     */     final Callable<U> bufferSupplier;
/*     */     U buffer;
/*     */     int size;
/*     */     Disposable upstream;
/*     */     
/*     */     BufferExactObserver(Observer<? super U> actual, int count, Callable<U> bufferSupplier) {
/*  62 */       this.downstream = actual;
/*  63 */       this.count = count;
/*  64 */       this.bufferSupplier = bufferSupplier;
/*     */     }
/*     */     
/*     */     boolean createBuffer() {
/*     */       Collection collection;
/*     */       try {
/*  70 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "Empty buffer supplied");
/*  71 */       } catch (Throwable t) {
/*  72 */         Exceptions.throwIfFatal(t);
/*  73 */         this.buffer = null;
/*  74 */         if (this.upstream == null) {
/*  75 */           EmptyDisposable.error(t, this.downstream);
/*     */         } else {
/*  77 */           this.upstream.dispose();
/*  78 */           this.downstream.onError(t);
/*     */         } 
/*  80 */         return false;
/*     */       } 
/*     */       
/*  83 */       this.buffer = (U)collection;
/*     */       
/*  85 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/*  90 */       if (DisposableHelper.validate(this.upstream, d)) {
/*  91 */         this.upstream = d;
/*  92 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/*  98 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 103 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 108 */       U b = this.buffer;
/*     */       
/* 110 */       b.add(t);
/*     */       
/* 112 */       if (b != null && ++this.size >= this.count) {
/* 113 */         this.downstream.onNext(b);
/*     */         
/* 115 */         this.size = 0;
/* 116 */         createBuffer();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 123 */       this.buffer = null;
/* 124 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 129 */       U b = this.buffer;
/* 130 */       if (b != null) {
/* 131 */         this.buffer = null;
/* 132 */         if (!b.isEmpty()) {
/* 133 */           this.downstream.onNext(b);
/*     */         }
/* 135 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BufferSkipObserver<T, U extends Collection<? super T>>
/*     */     extends AtomicBoolean
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -8223395059921494546L;
/*     */     
/*     */     final Observer<? super U> downstream;
/*     */     final int count;
/*     */     final int skip;
/*     */     final Callable<U> bufferSupplier;
/*     */     Disposable upstream;
/*     */     final ArrayDeque<U> buffers;
/*     */     long index;
/*     */     
/*     */     BufferSkipObserver(Observer<? super U> actual, int count, int skip, Callable<U> bufferSupplier) {
/* 156 */       this.downstream = actual;
/* 157 */       this.count = count;
/* 158 */       this.skip = skip;
/* 159 */       this.bufferSupplier = bufferSupplier;
/* 160 */       this.buffers = new ArrayDeque<U>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 165 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 166 */         this.upstream = d;
/* 167 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 173 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 178 */       return this.upstream.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 183 */       if (this.index++ % this.skip == 0L) {
/*     */         Collection collection;
/*     */         
/*     */         try {
/* 187 */           collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.");
/* 188 */         } catch (Throwable e) {
/* 189 */           this.buffers.clear();
/* 190 */           this.upstream.dispose();
/* 191 */           this.downstream.onError(e);
/*     */           
/*     */           return;
/*     */         } 
/* 195 */         this.buffers.offer((U)collection);
/*     */       } 
/*     */       
/* 198 */       Iterator<U> it = this.buffers.iterator();
/* 199 */       while (it.hasNext()) {
/* 200 */         Collection<T> collection = (Collection)it.next();
/* 201 */         collection.add(t);
/* 202 */         if (this.count <= collection.size()) {
/* 203 */           it.remove();
/*     */           
/* 205 */           this.downstream.onNext(collection);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 212 */       this.buffers.clear();
/* 213 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 218 */       while (!this.buffers.isEmpty()) {
/* 219 */         this.downstream.onNext(this.buffers.poll());
/*     */       }
/* 221 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */