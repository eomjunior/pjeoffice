/*    */ package io.reactivex.internal.operators.maybe;
/*    */ 
/*    */ import io.reactivex.Maybe;
/*    */ import io.reactivex.MaybeSource;
/*    */ import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;
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
/*    */ abstract class AbstractMaybeWithUpstream<T, R>
/*    */   extends Maybe<R>
/*    */   implements HasUpstreamMaybeSource<T>
/*    */ {
/*    */   protected final MaybeSource<T> source;
/*    */   
/*    */   AbstractMaybeWithUpstream(MaybeSource<T> source) {
/* 30 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   public final MaybeSource<T> source() {
/* 35 */     return this.source;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/maybe/AbstractMaybeWithUpstream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */