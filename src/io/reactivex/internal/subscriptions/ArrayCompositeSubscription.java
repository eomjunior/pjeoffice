/*     */ package io.reactivex.internal.subscriptions;
/*     */ 
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import org.reactivestreams.Subscription;
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
/*     */ public final class ArrayCompositeSubscription
/*     */   extends AtomicReferenceArray<Subscription>
/*     */   implements Disposable
/*     */ {
/*     */   private static final long serialVersionUID = 2746389416410565408L;
/*     */   
/*     */   public ArrayCompositeSubscription(int capacity) {
/*  34 */     super(capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setResource(int index, Subscription resource) {
/*     */     while (true) {
/*  45 */       Subscription o = get(index);
/*  46 */       if (o == SubscriptionHelper.CANCELLED) {
/*  47 */         if (resource != null) {
/*  48 */           resource.cancel();
/*     */         }
/*  50 */         return false;
/*     */       } 
/*  52 */       if (compareAndSet(index, o, resource)) {
/*  53 */         if (o != null) {
/*  54 */           o.cancel();
/*     */         }
/*  56 */         return true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Subscription replaceResource(int index, Subscription resource) {
/*     */     while (true) {
/*  69 */       Subscription o = get(index);
/*  70 */       if (o == SubscriptionHelper.CANCELLED) {
/*  71 */         if (resource != null) {
/*  72 */           resource.cancel();
/*     */         }
/*  74 */         return null;
/*     */       } 
/*  76 */       if (compareAndSet(index, o, resource)) {
/*  77 */         return o;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/*  84 */     if (get(0) != SubscriptionHelper.CANCELLED) {
/*  85 */       int s = length();
/*  86 */       for (int i = 0; i < s; i++) {
/*  87 */         Subscription o = get(i);
/*  88 */         if (o != SubscriptionHelper.CANCELLED) {
/*  89 */           o = getAndSet(i, SubscriptionHelper.CANCELLED);
/*  90 */           if (o != SubscriptionHelper.CANCELLED && o != null) {
/*  91 */             o.cancel();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDisposed() {
/* 100 */     return (get(0) == SubscriptionHelper.CANCELLED);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscriptions/ArrayCompositeSubscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */