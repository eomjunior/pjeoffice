/*    */ package io.reactivex.disposables;
/*    */ 
/*    */ import io.reactivex.annotations.NonNull;
/*    */ import org.reactivestreams.Subscription;
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
/*    */ final class SubscriptionDisposable
/*    */   extends ReferenceDisposable<Subscription>
/*    */ {
/*    */   private static final long serialVersionUID = -707001650852963139L;
/*    */   
/*    */   SubscriptionDisposable(Subscription value) {
/* 26 */     super(value);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onDisposed(@NonNull Subscription value) {
/* 31 */     value.cancel();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/disposables/SubscriptionDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */