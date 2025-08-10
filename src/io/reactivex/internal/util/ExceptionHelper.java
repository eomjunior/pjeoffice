/*     */ package io.reactivex.internal.util;
/*     */ 
/*     */ import io.reactivex.exceptions.CompositeException;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public final class ExceptionHelper
/*     */ {
/*     */   private ExceptionHelper() {
/*  29 */     throw new IllegalStateException("No instances!");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RuntimeException wrapOrThrow(Throwable error) {
/*  40 */     if (error instanceof Error) {
/*  41 */       throw (Error)error;
/*     */     }
/*  43 */     if (error instanceof RuntimeException) {
/*  44 */       return (RuntimeException)error;
/*     */     }
/*  46 */     return new RuntimeException(error);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   public static final Throwable TERMINATED = new Termination();
/*     */   public static <T> boolean addThrowable(AtomicReference<Throwable> field, Throwable exception) {
/*     */     while (true) {
/*     */       CompositeException compositeException;
/*  57 */       Throwable current = field.get();
/*     */       
/*  59 */       if (current == TERMINATED) {
/*  60 */         return false;
/*     */       }
/*     */ 
/*     */       
/*  64 */       if (current == null) {
/*  65 */         Throwable update = exception;
/*     */       } else {
/*  67 */         compositeException = new CompositeException(new Throwable[] { current, exception });
/*     */       } 
/*     */       
/*  70 */       if (field.compareAndSet(current, compositeException)) {
/*  71 */         return true;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <T> Throwable terminate(AtomicReference<Throwable> field) {
/*  77 */     Throwable current = field.get();
/*  78 */     if (current != TERMINATED) {
/*  79 */       current = field.getAndSet(TERMINATED);
/*     */     }
/*  81 */     return current;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Throwable> flatten(Throwable t) {
/*  90 */     List<Throwable> list = new ArrayList<Throwable>();
/*  91 */     ArrayDeque<Throwable> deque = new ArrayDeque<Throwable>();
/*  92 */     deque.offer(t);
/*     */     
/*  94 */     while (!deque.isEmpty()) {
/*  95 */       Throwable e = deque.removeFirst();
/*  96 */       if (e instanceof CompositeException) {
/*  97 */         CompositeException ce = (CompositeException)e;
/*  98 */         List<Throwable> exceptions = ce.getExceptions();
/*  99 */         for (int i = exceptions.size() - 1; i >= 0; i--)
/* 100 */           deque.offerFirst(exceptions.get(i)); 
/*     */         continue;
/*     */       } 
/* 103 */       list.add(e);
/*     */     } 
/*     */ 
/*     */     
/* 107 */     return list;
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
/*     */   public static <E extends Throwable> Exception throwIfThrowable(Throwable e) throws E {
/* 119 */     if (e instanceof Exception) {
/* 120 */       return (Exception)e;
/*     */     }
/* 122 */     throw (E)e;
/*     */   }
/*     */   
/*     */   public static String timeoutMessage(long timeout, TimeUnit unit) {
/* 126 */     return "The source did not signal an event for " + timeout + " " + unit
/*     */ 
/*     */       
/* 129 */       .toString().toLowerCase() + " and has been terminated.";
/*     */   }
/*     */   
/*     */   static final class Termination
/*     */     extends Throwable
/*     */   {
/*     */     private static final long serialVersionUID = -4649703670690200604L;
/*     */     
/*     */     Termination() {
/* 138 */       super("No further exceptions");
/*     */     }
/*     */ 
/*     */     
/*     */     public Throwable fillInStackTrace() {
/* 143 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/util/ExceptionHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */