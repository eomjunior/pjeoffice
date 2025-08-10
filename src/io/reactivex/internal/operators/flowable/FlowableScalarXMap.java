/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.ScalarSubscription;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Callable;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.reactivestreams.Subscriber;
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
/*     */ public final class FlowableScalarXMap
/*     */ {
/*     */   private FlowableScalarXMap() {
/*  34 */     throw new IllegalStateException("No instances!");
/*     */   }
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
/*     */   public static <T, R> boolean tryScalarXMapSubscribe(Publisher<T> source, Subscriber<? super R> subscriber, Function<? super T, ? extends Publisher<? extends R>> mapper) {
/*  50 */     if (source instanceof Callable) {
/*     */       T t;
/*     */       Publisher<? extends R> r;
/*     */       try {
/*  54 */         t = ((Callable<T>)source).call();
/*  55 */       } catch (Throwable ex) {
/*  56 */         Exceptions.throwIfFatal(ex);
/*  57 */         EmptySubscription.error(ex, subscriber);
/*  58 */         return true;
/*     */       } 
/*     */       
/*  61 */       if (t == null) {
/*  62 */         EmptySubscription.complete(subscriber);
/*  63 */         return true;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  69 */         r = (Publisher<? extends R>)ObjectHelper.requireNonNull(mapper.apply(t), "The mapper returned a null Publisher");
/*  70 */       } catch (Throwable ex) {
/*  71 */         Exceptions.throwIfFatal(ex);
/*  72 */         EmptySubscription.error(ex, subscriber);
/*  73 */         return true;
/*     */       } 
/*     */       
/*  76 */       if (r instanceof Callable) {
/*     */         R u;
/*     */         
/*     */         try {
/*  80 */           u = ((Callable)r).call();
/*  81 */         } catch (Throwable ex) {
/*  82 */           Exceptions.throwIfFatal(ex);
/*  83 */           EmptySubscription.error(ex, subscriber);
/*  84 */           return true;
/*     */         } 
/*     */         
/*  87 */         if (u == null) {
/*  88 */           EmptySubscription.complete(subscriber);
/*  89 */           return true;
/*     */         } 
/*  91 */         subscriber.onSubscribe((Subscription)new ScalarSubscription(subscriber, u));
/*     */       } else {
/*  93 */         r.subscribe(subscriber);
/*     */       } 
/*     */       
/*  96 */       return true;
/*     */     } 
/*  98 */     return false;
/*     */   }
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
/*     */   public static <T, U> Flowable<U> scalarXMap(T value, Function<? super T, ? extends Publisher<? extends U>> mapper) {
/* 112 */     return RxJavaPlugins.onAssembly(new ScalarXMapFlowable<T, U>(value, mapper));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ScalarXMapFlowable<T, R>
/*     */     extends Flowable<R>
/*     */   {
/*     */     final T value;
/*     */ 
/*     */     
/*     */     final Function<? super T, ? extends Publisher<? extends R>> mapper;
/*     */ 
/*     */ 
/*     */     
/*     */     ScalarXMapFlowable(T value, Function<? super T, ? extends Publisher<? extends R>> mapper) {
/* 129 */       this.value = value;
/* 130 */       this.mapper = mapper;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void subscribeActual(Subscriber<? super R> s) {
/*     */       Publisher<? extends R> other;
/*     */       try {
/* 138 */         other = (Publisher<? extends R>)ObjectHelper.requireNonNull(this.mapper.apply(this.value), "The mapper returned a null Publisher");
/* 139 */       } catch (Throwable e) {
/* 140 */         EmptySubscription.error(e, s);
/*     */         return;
/*     */       } 
/* 143 */       if (other instanceof Callable) {
/*     */         R u;
/*     */         
/*     */         try {
/* 147 */           u = ((Callable)other).call();
/* 148 */         } catch (Throwable ex) {
/* 149 */           Exceptions.throwIfFatal(ex);
/* 150 */           EmptySubscription.error(ex, s);
/*     */           
/*     */           return;
/*     */         } 
/* 154 */         if (u == null) {
/* 155 */           EmptySubscription.complete(s);
/*     */           return;
/*     */         } 
/* 158 */         s.onSubscribe((Subscription)new ScalarSubscription(s, u));
/*     */       } else {
/* 160 */         other.subscribe(s);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableScalarXMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */