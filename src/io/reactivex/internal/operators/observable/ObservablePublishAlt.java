/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.ResettableConnectable;
/*     */ import io.reactivex.internal.fuseable.HasUpstreamObservableSource;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.observables.ConnectableObservable;
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
/*     */ public final class ObservablePublishAlt<T>
/*     */   extends ConnectableObservable<T>
/*     */   implements HasUpstreamObservableSource<T>, ResettableConnectable
/*     */ {
/*     */   final ObservableSource<T> source;
/*     */   final AtomicReference<PublishConnection<T>> current;
/*     */   
/*     */   public ObservablePublishAlt(ObservableSource<T> source) {
/*  47 */     this.source = source;
/*  48 */     this.current = new AtomicReference<PublishConnection<T>>();
/*     */   }
/*     */   
/*     */   public void connect(Consumer<? super Disposable> connection) {
/*     */     PublishConnection<T> conn;
/*  53 */     boolean doConnect = false;
/*     */ 
/*     */     
/*     */     while (true) {
/*  57 */       conn = this.current.get();
/*     */       
/*  59 */       if (conn == null || conn.isDisposed()) {
/*  60 */         PublishConnection<T> fresh = new PublishConnection<T>(this.current);
/*  61 */         if (!this.current.compareAndSet(conn, fresh)) {
/*     */           continue;
/*     */         }
/*  64 */         conn = fresh;
/*     */       }  break;
/*     */     } 
/*  67 */     doConnect = (!conn.connect.get() && conn.connect.compareAndSet(false, true));
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  72 */       connection.accept(conn);
/*  73 */     } catch (Throwable ex) {
/*  74 */       Exceptions.throwIfFatal(ex);
/*  75 */       throw ExceptionHelper.wrapOrThrow(ex);
/*     */     } 
/*     */     
/*  78 */     if (doConnect) {
/*  79 */       this.source.subscribe(conn);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/*     */     PublishConnection<T> conn;
/*     */     while (true) {
/*  88 */       conn = this.current.get();
/*     */       
/*  90 */       if (conn == null) {
/*  91 */         PublishConnection<T> fresh = new PublishConnection<T>(this.current);
/*  92 */         if (!this.current.compareAndSet(conn, fresh)) {
/*     */           continue;
/*     */         }
/*  95 */         conn = fresh;
/*     */       } 
/*     */       
/*     */       break;
/*     */     } 
/* 100 */     InnerDisposable<T> inner = new InnerDisposable<T>(observer, conn);
/* 101 */     observer.onSubscribe(inner);
/* 102 */     if (conn.add(inner)) {
/* 103 */       if (inner.isDisposed()) {
/* 104 */         conn.remove(inner);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 109 */     Throwable error = conn.error;
/* 110 */     if (error != null) {
/* 111 */       observer.onError(error);
/*     */     } else {
/* 113 */       observer.onComplete();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetIf(Disposable connection) {
/* 120 */     this.current.compareAndSet((PublishConnection<T>)connection, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public ObservableSource<T> source() {
/* 125 */     return this.source;
/*     */   }
/*     */ 
/*     */   
/*     */   static final class PublishConnection<T>
/*     */     extends AtomicReference<InnerDisposable<T>[]>
/*     */     implements Observer<T>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -3251430252873581268L;
/*     */     
/*     */     final AtomicBoolean connect;
/*     */     
/*     */     final AtomicReference<PublishConnection<T>> current;
/*     */     
/*     */     final AtomicReference<Disposable> upstream;
/*     */     
/* 141 */     static final ObservablePublishAlt.InnerDisposable[] EMPTY = new ObservablePublishAlt.InnerDisposable[0];
/*     */ 
/*     */     
/* 144 */     static final ObservablePublishAlt.InnerDisposable[] TERMINATED = new ObservablePublishAlt.InnerDisposable[0];
/*     */     
/*     */     Throwable error;
/*     */ 
/*     */     
/*     */     PublishConnection(AtomicReference<PublishConnection<T>> current) {
/* 150 */       this.connect = new AtomicBoolean();
/* 151 */       this.current = current;
/* 152 */       this.upstream = new AtomicReference<Disposable>();
/* 153 */       lazySet((ObservablePublishAlt.InnerDisposable<T>[])EMPTY);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 159 */       getAndSet((ObservablePublishAlt.InnerDisposable<T>[])TERMINATED);
/* 160 */       this.current.compareAndSet(this, null);
/* 161 */       DisposableHelper.dispose(this.upstream);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 166 */       return (get() == TERMINATED);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 171 */       DisposableHelper.setOnce(this.upstream, d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 176 */       for (ObservablePublishAlt.InnerDisposable<T> inner : (ObservablePublishAlt.InnerDisposable[])get()) {
/* 177 */         inner.downstream.onNext(t);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 184 */       this.error = e;
/* 185 */       this.upstream.lazySet(DisposableHelper.DISPOSED);
/* 186 */       for (ObservablePublishAlt.InnerDisposable<T> inner : (ObservablePublishAlt.InnerDisposable[])getAndSet((ObservablePublishAlt.InnerDisposable<T>[])TERMINATED)) {
/* 187 */         inner.downstream.onError(e);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 194 */       this.upstream.lazySet(DisposableHelper.DISPOSED);
/* 195 */       for (ObservablePublishAlt.InnerDisposable<T> inner : (ObservablePublishAlt.InnerDisposable[])getAndSet((ObservablePublishAlt.InnerDisposable<T>[])TERMINATED)) {
/* 196 */         inner.downstream.onComplete();
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean add(ObservablePublishAlt.InnerDisposable<T> inner) {
/*     */       while (true) {
/* 202 */         ObservablePublishAlt.InnerDisposable[] arrayOfInnerDisposable1 = (ObservablePublishAlt.InnerDisposable[])get();
/* 203 */         if (arrayOfInnerDisposable1 == TERMINATED) {
/* 204 */           return false;
/*     */         }
/* 206 */         int n = arrayOfInnerDisposable1.length;
/*     */         
/* 208 */         ObservablePublishAlt.InnerDisposable[] arrayOfInnerDisposable2 = new ObservablePublishAlt.InnerDisposable[n + 1];
/* 209 */         System.arraycopy(arrayOfInnerDisposable1, 0, arrayOfInnerDisposable2, 0, n);
/* 210 */         arrayOfInnerDisposable2[n] = inner;
/* 211 */         if (compareAndSet((ObservablePublishAlt.InnerDisposable<T>[])arrayOfInnerDisposable1, (ObservablePublishAlt.InnerDisposable<T>[])arrayOfInnerDisposable2))
/* 212 */           return true; 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove(ObservablePublishAlt.InnerDisposable<T> inner) {
/*     */       ObservablePublishAlt.InnerDisposable[] arrayOfInnerDisposable1;
/*     */       ObservablePublishAlt.InnerDisposable[] arrayOfInnerDisposable2;
/*     */       do {
/* 220 */         arrayOfInnerDisposable1 = (ObservablePublishAlt.InnerDisposable[])get();
/* 221 */         int n = arrayOfInnerDisposable1.length;
/* 222 */         if (n == 0) {
/*     */           return;
/*     */         }
/*     */         
/* 226 */         int j = -1;
/* 227 */         for (int i = 0; i < n; i++) {
/* 228 */           if (arrayOfInnerDisposable1[i] == inner) {
/* 229 */             j = i;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 234 */         if (j < 0) {
/*     */           return;
/*     */         }
/* 237 */         arrayOfInnerDisposable2 = EMPTY;
/* 238 */         if (n == 1)
/* 239 */           continue;  arrayOfInnerDisposable2 = new ObservablePublishAlt.InnerDisposable[n - 1];
/* 240 */         System.arraycopy(arrayOfInnerDisposable1, 0, arrayOfInnerDisposable2, 0, j);
/* 241 */         System.arraycopy(arrayOfInnerDisposable1, j + 1, arrayOfInnerDisposable2, j, n - j - 1);
/*     */       }
/* 243 */       while (!compareAndSet((ObservablePublishAlt.InnerDisposable<T>[])arrayOfInnerDisposable1, (ObservablePublishAlt.InnerDisposable<T>[])arrayOfInnerDisposable2));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class InnerDisposable<T>
/*     */     extends AtomicReference<PublishConnection<T>>
/*     */     implements Disposable
/*     */   {
/*     */     private static final long serialVersionUID = 7463222674719692880L;
/*     */ 
/*     */ 
/*     */     
/*     */     final Observer<? super T> downstream;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     InnerDisposable(Observer<? super T> downstream, ObservablePublishAlt.PublishConnection<T> parent) {
/* 265 */       this.downstream = downstream;
/* 266 */       lazySet(parent);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 271 */       ObservablePublishAlt.PublishConnection<T> p = getAndSet(null);
/* 272 */       if (p != null) {
/* 273 */         p.remove(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 279 */       return (get() == null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservablePublishAlt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */