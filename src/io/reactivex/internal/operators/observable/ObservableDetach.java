/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.Observer;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.disposables.DisposableHelper;
/*    */ import io.reactivex.internal.util.EmptyComponent;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ObservableDetach<T>
/*    */   extends AbstractObservableWithUpstream<T, T>
/*    */ {
/*    */   public ObservableDetach(ObservableSource<T> source) {
/* 30 */     super(source);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Observer<? super T> observer) {
/* 35 */     this.source.subscribe(new DetachObserver<T>(observer));
/*    */   }
/*    */   
/*    */   static final class DetachObserver<T>
/*    */     implements Observer<T>, Disposable
/*    */   {
/*    */     Observer<? super T> downstream;
/*    */     Disposable upstream;
/*    */     
/*    */     DetachObserver(Observer<? super T> downstream) {
/* 45 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 50 */       Disposable d = this.upstream;
/* 51 */       this.upstream = (Disposable)EmptyComponent.INSTANCE;
/* 52 */       this.downstream = EmptyComponent.asObserver();
/* 53 */       d.dispose();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 58 */       return this.upstream.isDisposed();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Disposable d) {
/* 63 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 64 */         this.upstream = d;
/*    */         
/* 66 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 72 */       this.downstream.onNext(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 77 */       Observer<? super T> a = this.downstream;
/* 78 */       this.upstream = (Disposable)EmptyComponent.INSTANCE;
/* 79 */       this.downstream = EmptyComponent.asObserver();
/* 80 */       a.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 85 */       Observer<? super T> a = this.downstream;
/* 86 */       this.upstream = (Disposable)EmptyComponent.INSTANCE;
/* 87 */       this.downstream = EmptyComponent.asObserver();
/* 88 */       a.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableDetach.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */