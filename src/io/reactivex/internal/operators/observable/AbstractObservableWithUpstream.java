/*    */ package io.reactivex.internal.operators.observable;
/*    */ 
/*    */ import io.reactivex.Observable;
/*    */ import io.reactivex.ObservableSource;
/*    */ import io.reactivex.internal.fuseable.HasUpstreamObservableSource;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class AbstractObservableWithUpstream<T, U>
/*    */   extends Observable<U>
/*    */   implements HasUpstreamObservableSource<T>
/*    */ {
/*    */   protected final ObservableSource<T> source;
/*    */   
/*    */   AbstractObservableWithUpstream(ObservableSource<T> source) {
/* 35 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public final ObservableSource<T> source() {
/* 40 */     return this.source;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/AbstractObservableWithUpstream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */