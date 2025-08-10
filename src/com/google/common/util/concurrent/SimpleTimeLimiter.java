/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ObjectArrays;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import javax.annotation.CheckForNull;
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
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public final class SimpleTimeLimiter
/*     */   implements TimeLimiter
/*     */ {
/*     */   private final ExecutorService executor;
/*     */   
/*     */   private SimpleTimeLimiter(ExecutorService executor) {
/*  57 */     this.executor = (ExecutorService)Preconditions.checkNotNull(executor);
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
/*     */   public static SimpleTimeLimiter create(ExecutorService executor) {
/*  72 */     return new SimpleTimeLimiter(executor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T newProxy(final T target, Class<T> interfaceType, final long timeoutDuration, final TimeUnit timeoutUnit) {
/*  78 */     Preconditions.checkNotNull(target);
/*  79 */     Preconditions.checkNotNull(interfaceType);
/*  80 */     Preconditions.checkNotNull(timeoutUnit);
/*  81 */     checkPositiveTimeout(timeoutDuration);
/*  82 */     Preconditions.checkArgument(interfaceType.isInterface(), "interfaceType must be an interface type");
/*     */     
/*  84 */     final Set<Method> interruptibleMethods = findInterruptibleMethods(interfaceType);
/*     */     
/*  86 */     InvocationHandler handler = new InvocationHandler()
/*     */       {
/*     */         
/*     */         @CheckForNull
/*     */         public Object invoke(Object obj, Method method, @CheckForNull Object[] args) throws Throwable
/*     */         {
/*  92 */           Callable<Object> callable = () -> {
/*     */               
/*     */               try {
/*     */                 return method.invoke(target, args);
/*  96 */               } catch (InvocationTargetException e) {
/*     */                 throw SimpleTimeLimiter.throwCause(e, false);
/*     */               } 
/*     */             };
/* 100 */           return SimpleTimeLimiter.this.callWithTimeout((Callable)callable, timeoutDuration, timeoutUnit, interruptibleMethods
/* 101 */               .contains(method));
/*     */         }
/*     */       };
/* 104 */     return newProxy(interfaceType, handler);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> T newProxy(Class<T> interfaceType, InvocationHandler handler) {
/* 110 */     Object object = Proxy.newProxyInstance(interfaceType
/* 111 */         .getClassLoader(), new Class[] { interfaceType }, handler);
/* 112 */     return interfaceType.cast(object);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   private <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit, boolean amInterruptible) throws Exception {
/* 119 */     Preconditions.checkNotNull(callable);
/* 120 */     Preconditions.checkNotNull(timeoutUnit);
/* 121 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 123 */     Future<T> future = this.executor.submit(callable);
/*     */     
/*     */     try {
/* 126 */       return amInterruptible ? 
/* 127 */         future.get(timeoutDuration, timeoutUnit) : 
/* 128 */         Uninterruptibles.<T>getUninterruptibly(future, timeoutDuration, timeoutUnit);
/* 129 */     } catch (InterruptedException e) {
/* 130 */       future.cancel(true);
/* 131 */       throw e;
/* 132 */     } catch (ExecutionException e) {
/* 133 */       throw throwCause(e, true);
/* 134 */     } catch (TimeoutException e) {
/* 135 */       future.cancel(true);
/* 136 */       throw new UncheckedTimeoutException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException, InterruptedException, ExecutionException {
/* 146 */     Preconditions.checkNotNull(callable);
/* 147 */     Preconditions.checkNotNull(timeoutUnit);
/* 148 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 150 */     Future<T> future = this.executor.submit(callable);
/*     */     
/*     */     try {
/* 153 */       return future.get(timeoutDuration, timeoutUnit);
/* 154 */     } catch (InterruptedException|TimeoutException e) {
/* 155 */       future.cancel(true);
/* 156 */       throw e;
/* 157 */     } catch (ExecutionException e) {
/* 158 */       wrapAndThrowExecutionExceptionOrError(e.getCause());
/* 159 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T callUninterruptiblyWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException, ExecutionException {
/* 169 */     Preconditions.checkNotNull(callable);
/* 170 */     Preconditions.checkNotNull(timeoutUnit);
/* 171 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 173 */     Future<T> future = this.executor.submit(callable);
/*     */     
/*     */     try {
/* 176 */       return Uninterruptibles.getUninterruptibly(future, timeoutDuration, timeoutUnit);
/* 177 */     } catch (TimeoutException e) {
/* 178 */       future.cancel(true);
/* 179 */       throw e;
/* 180 */     } catch (ExecutionException e) {
/* 181 */       wrapAndThrowExecutionExceptionOrError(e.getCause());
/* 182 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void runWithTimeout(Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException, InterruptedException {
/* 189 */     Preconditions.checkNotNull(runnable);
/* 190 */     Preconditions.checkNotNull(timeoutUnit);
/* 191 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 193 */     Future<?> future = this.executor.submit(runnable);
/*     */     
/*     */     try {
/* 196 */       future.get(timeoutDuration, timeoutUnit);
/* 197 */     } catch (InterruptedException|TimeoutException e) {
/* 198 */       future.cancel(true);
/* 199 */       throw e;
/* 200 */     } catch (ExecutionException e) {
/* 201 */       wrapAndThrowRuntimeExecutionExceptionOrError(e.getCause());
/* 202 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void runUninterruptiblyWithTimeout(Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException {
/* 209 */     Preconditions.checkNotNull(runnable);
/* 210 */     Preconditions.checkNotNull(timeoutUnit);
/* 211 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 213 */     Future<?> future = this.executor.submit(runnable);
/*     */     
/*     */     try {
/* 216 */       Uninterruptibles.getUninterruptibly(future, timeoutDuration, timeoutUnit);
/* 217 */     } catch (TimeoutException e) {
/* 218 */       future.cancel(true);
/* 219 */       throw e;
/* 220 */     } catch (ExecutionException e) {
/* 221 */       wrapAndThrowRuntimeExecutionExceptionOrError(e.getCause());
/* 222 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Exception throwCause(Exception e, boolean combineStackTraces) throws Exception {
/* 227 */     Throwable cause = e.getCause();
/* 228 */     if (cause == null) {
/* 229 */       throw e;
/*     */     }
/* 231 */     if (combineStackTraces) {
/*     */       
/* 233 */       StackTraceElement[] combined = (StackTraceElement[])ObjectArrays.concat((Object[])cause.getStackTrace(), (Object[])e.getStackTrace(), StackTraceElement.class);
/* 234 */       cause.setStackTrace(combined);
/*     */     } 
/* 236 */     if (cause instanceof Exception) {
/* 237 */       throw (Exception)cause;
/*     */     }
/* 239 */     if (cause instanceof Error) {
/* 240 */       throw (Error)cause;
/*     */     }
/*     */     
/* 243 */     throw e;
/*     */   }
/*     */   
/*     */   private static Set<Method> findInterruptibleMethods(Class<?> interfaceType) {
/* 247 */     Set<Method> set = Sets.newHashSet();
/* 248 */     for (Method m : interfaceType.getMethods()) {
/* 249 */       if (declaresInterruptedEx(m)) {
/* 250 */         set.add(m);
/*     */       }
/*     */     } 
/* 253 */     return set;
/*     */   }
/*     */   
/*     */   private static boolean declaresInterruptedEx(Method method) {
/* 257 */     for (Class<?> exType : method.getExceptionTypes()) {
/*     */       
/* 259 */       if (exType == InterruptedException.class) {
/* 260 */         return true;
/*     */       }
/*     */     } 
/* 263 */     return false;
/*     */   }
/*     */   
/*     */   private void wrapAndThrowExecutionExceptionOrError(Throwable cause) throws ExecutionException {
/* 267 */     if (cause instanceof Error)
/* 268 */       throw new ExecutionError((Error)cause); 
/* 269 */     if (cause instanceof RuntimeException) {
/* 270 */       throw new UncheckedExecutionException(cause);
/*     */     }
/* 272 */     throw new ExecutionException(cause);
/*     */   }
/*     */ 
/*     */   
/*     */   private void wrapAndThrowRuntimeExecutionExceptionOrError(Throwable cause) {
/* 277 */     if (cause instanceof Error) {
/* 278 */       throw new ExecutionError((Error)cause);
/*     */     }
/* 280 */     throw new UncheckedExecutionException(cause);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void checkPositiveTimeout(long timeoutDuration) {
/* 285 */     Preconditions.checkArgument((timeoutDuration > 0L), "timeout must be positive: %s", timeoutDuration);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/SimpleTimeLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */