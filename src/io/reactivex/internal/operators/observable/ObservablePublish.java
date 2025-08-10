/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.fuseable.HasUpstreamObservableSource;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.observables.ConnectableObservable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservablePublish<T>
/*     */   extends ConnectableObservable<T>
/*     */   implements HasUpstreamObservableSource<T>, ObservablePublishClassic<T>
/*     */ {
/*     */   final ObservableSource<T> source;
/*     */   final AtomicReference<PublishObserver<T>> current;
/*     */   final ObservableSource<T> onSubscribe;
/*     */   
/*     */   public static <T> ConnectableObservable<T> create(ObservableSource<T> source) {
/*  50 */     AtomicReference<PublishObserver<T>> curr = new AtomicReference<PublishObserver<T>>();
/*  51 */     ObservableSource<T> onSubscribe = new PublishSource<T>(curr);
/*  52 */     return RxJavaPlugins.onAssembly(new ObservablePublish<T>(onSubscribe, source, curr));
/*     */   }
/*     */ 
/*     */   
/*     */   private ObservablePublish(ObservableSource<T> onSubscribe, ObservableSource<T> source, AtomicReference<PublishObserver<T>> current) {
/*  57 */     this.onSubscribe = onSubscribe;
/*  58 */     this.source = source;
/*  59 */     this.current = current;
/*     */   }
/*     */ 
/*     */   
/*     */   public ObservableSource<T> source() {
/*  64 */     return this.source;
/*     */   }
/*     */ 
/*     */   
/*     */   public ObservableSource<T> publishSource() {
/*  69 */     return this.source;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/*  74 */     this.onSubscribe.subscribe(observer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect(Consumer<? super Disposable> connection) {
/*     */     PublishObserver<T> ps;
/*     */     while (true) {
/*  84 */       ps = this.current.get();
/*     */       
/*  86 */       if (ps == null || ps.isDisposed()) {
/*     */         
/*  88 */         PublishObserver<T> u = new PublishObserver<T>(this.current);
/*     */         
/*  90 */         if (!this.current.compareAndSet(ps, u)) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/*  95 */         ps = u;
/*     */       } 
/*     */       break;
/*     */     } 
/*  99 */     boolean doConnect = (!ps.shouldConnect.get() && ps.shouldConnect.compareAndSet(false, true));
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
/*     */     try {
/* 116 */       connection.accept(ps);
/* 117 */     } catch (Throwable ex) {
/* 118 */       Exceptions.throwIfFatal(ex);
/* 119 */       throw ExceptionHelper.wrapOrThrow(ex);
/*     */     } 
/* 121 */     if (doConnect) {
/* 122 */       this.source.subscribe(ps);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class PublishObserver<T>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     final AtomicReference<PublishObserver<T>> current;
/*     */     
/* 133 */     static final ObservablePublish.InnerDisposable[] EMPTY = new ObservablePublish.InnerDisposable[0];
/*     */     
/* 135 */     static final ObservablePublish.InnerDisposable[] TERMINATED = new ObservablePublish.InnerDisposable[0];
/*     */ 
/*     */ 
/*     */     
/*     */     final AtomicReference<ObservablePublish.InnerDisposable<T>[]> observers;
/*     */ 
/*     */     
/*     */     final AtomicBoolean shouldConnect;
/*     */ 
/*     */     
/* 145 */     final AtomicReference<Disposable> upstream = new AtomicReference<Disposable>();
/*     */ 
/*     */     
/*     */     PublishObserver(AtomicReference<PublishObserver<T>> current) {
/* 149 */       this.observers = new AtomicReference(EMPTY);
/* 150 */       this.current = current;
/* 151 */       this.shouldConnect = new AtomicBoolean();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 157 */       ObservablePublish.InnerDisposable[] ps = (ObservablePublish.InnerDisposable[])this.observers.getAndSet(TERMINATED);
/* 158 */       if (ps != TERMINATED) {
/* 159 */         this.current.compareAndSet(this, null);
/*     */         
/* 161 */         DisposableHelper.dispose(this.upstream);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 167 */       return (this.observers.get() == TERMINATED);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 172 */       DisposableHelper.setOnce(this.upstream, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 177 */       for (ObservablePublish.InnerDisposable<T> inner : (ObservablePublish.InnerDisposable[])this.observers.get()) {
/* 178 */         inner.child.onNext(t);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 185 */       this.current.compareAndSet(this, null);
/* 186 */       ObservablePublish.InnerDisposable[] arrayOfInnerDisposable = (ObservablePublish.InnerDisposable[])this.observers.getAndSet(TERMINATED);
/* 187 */       if (arrayOfInnerDisposable.length != 0) {
/* 188 */         for (ObservablePublish.InnerDisposable<T> inner : arrayOfInnerDisposable) {
/* 189 */           inner.child.onError(e);
/*     */         }
/*     */       } else {
/* 192 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 199 */       this.current.compareAndSet(this, null);
/* 200 */       for (ObservablePublish.InnerDisposable<T> inner : (ObservablePublish.InnerDisposable[])this.observers.getAndSet(TERMINATED)) {
/* 201 */         inner.child.onComplete();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean add(ObservablePublish.InnerDisposable<T> producer) {
/*     */       while (true) {
/* 215 */         ObservablePublish.InnerDisposable[] arrayOfInnerDisposable1 = (ObservablePublish.InnerDisposable[])this.observers.get();
/*     */ 
/*     */         
/* 218 */         if (arrayOfInnerDisposable1 == TERMINATED) {
/* 219 */           return false;
/*     */         }
/*     */         
/* 222 */         int len = arrayOfInnerDisposable1.length;
/*     */         
/* 224 */         ObservablePublish.InnerDisposable[] arrayOfInnerDisposable2 = new ObservablePublish.InnerDisposable[len + 1];
/* 225 */         System.arraycopy(arrayOfInnerDisposable1, 0, arrayOfInnerDisposable2, 0, len);
/* 226 */         arrayOfInnerDisposable2[len] = producer;
/*     */         
/* 228 */         if (this.observers.compareAndSet(arrayOfInnerDisposable1, arrayOfInnerDisposable2)) {
/* 229 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void remove(ObservablePublish.InnerDisposable<T> producer) {
/*     */       ObservablePublish.InnerDisposable[] arrayOfInnerDisposable1;
/*     */       ObservablePublish.InnerDisposable[] arrayOfInnerDisposable2;
/*     */       do {
/* 245 */         arrayOfInnerDisposable1 = (ObservablePublish.InnerDisposable[])this.observers.get();
/*     */         
/* 247 */         int len = arrayOfInnerDisposable1.length;
/* 248 */         if (len == 0) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 253 */         int j = -1;
/* 254 */         for (int i = 0; i < len; i++) {
/* 255 */           if (arrayOfInnerDisposable1[i].equals(producer)) {
/* 256 */             j = i;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 261 */         if (j < 0) {
/*     */           return;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 268 */         if (len == 1) {
/* 269 */           arrayOfInnerDisposable2 = EMPTY;
/*     */         } else {
/*     */           
/* 272 */           arrayOfInnerDisposable2 = new ObservablePublish.InnerDisposable[len - 1];
/*     */           
/* 274 */           System.arraycopy(arrayOfInnerDisposable1, 0, arrayOfInnerDisposable2, 0, j);
/*     */           
/* 276 */           System.arraycopy(arrayOfInnerDisposable1, j + 1, arrayOfInnerDisposable2, j, len - j - 1);
/*     */         }
/*     */       
/* 279 */       } while (!this.observers.compareAndSet(arrayOfInnerDisposable1, arrayOfInnerDisposable2));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class InnerDisposable<T>
/*     */     extends AtomicReference<Object>
/*     */     implements Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -1100270633763673112L;
/*     */ 
/*     */ 
/*     */     
/*     */     final Observer<? super T> child;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     InnerDisposable(Observer<? super T> child) {
/* 301 */       this.child = child;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 306 */       return (get() == this);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 312 */       Object o = getAndSet(this);
/* 313 */       if (o != null && o != this) {
/* 314 */         ((ObservablePublish.PublishObserver<T>)o).remove(this);
/*     */       }
/*     */     }
/*     */     
/*     */     void setParent(ObservablePublish.PublishObserver<T> p) {
/* 319 */       if (!compareAndSet(null, p))
/* 320 */         p.remove(this); 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class PublishSource<T>
/*     */     implements ObservableSource<T> {
/*     */     private final AtomicReference<ObservablePublish.PublishObserver<T>> curr;
/*     */     
/*     */     PublishSource(AtomicReference<ObservablePublish.PublishObserver<T>> curr) {
/* 329 */       this.curr = curr;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void subscribe(Observer<? super T> child) {
/* 335 */       ObservablePublish.InnerDisposable<T> inner = new ObservablePublish.InnerDisposable<T>(child);
/* 336 */       child.onSubscribe(inner);
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 341 */         ObservablePublish.PublishObserver<T> r = this.curr.get();
/*     */         
/* 343 */         if (r == null || r.isDisposed()) {
/*     */           
/* 345 */           ObservablePublish.PublishObserver<T> u = new ObservablePublish.PublishObserver<T>(this.curr);
/*     */           
/* 347 */           if (!this.curr.compareAndSet(r, u)) {
/*     */             continue;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 353 */           r = u;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 360 */         if (r.add(inner)) {
/* 361 */           inner.setParent(r);
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservablePublish.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */