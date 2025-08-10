/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.AbstractList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Throwables
/*     */ {
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   private static final String JAVA_LANG_ACCESS_CLASSNAME = "sun.misc.JavaLangAccess";
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   @VisibleForTesting
/*     */   static final String SHARED_SECRETS_CLASSNAME = "sun.misc.SharedSecrets";
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <X extends Throwable> void throwIfInstanceOf(Throwable throwable, Class<X> declaredType) throws X {
/*  77 */     Preconditions.checkNotNull(throwable);
/*  78 */     if (declaredType.isInstance(throwable)) {
/*  79 */       throw (X)declaredType.cast(throwable);
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <X extends Throwable> void propagateIfInstanceOf(@CheckForNull Throwable throwable, Class<X> declaredType) throws X {
/* 107 */     if (throwable != null) {
/* 108 */       throwIfInstanceOf(throwable, declaredType);
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void throwIfUnchecked(Throwable throwable) {
/* 132 */     Preconditions.checkNotNull(throwable);
/* 133 */     if (throwable instanceof RuntimeException) {
/* 134 */       throw (RuntimeException)throwable;
/*     */     }
/* 136 */     if (throwable instanceof Error) {
/* 137 */       throw (Error)throwable;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static void propagateIfPossible(@CheckForNull Throwable throwable) {
/* 152 */     if (throwable != null) {
/* 153 */       throwIfUnchecked(throwable);
/*     */     }
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <X extends Throwable> void propagateIfPossible(@CheckForNull Throwable throwable, Class<X> declaredType) throws X {
/* 171 */     propagateIfInstanceOf(throwable, declaredType);
/* 172 */     propagateIfPossible(throwable);
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
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <X1 extends Throwable, X2 extends Throwable> void propagateIfPossible(@CheckForNull Throwable throwable, Class<X1> declaredType1, Class<X2> declaredType2) throws X1, X2 {
/* 191 */     Preconditions.checkNotNull(declaredType2);
/* 192 */     propagateIfInstanceOf(throwable, declaredType1);
/* 193 */     propagateIfPossible(throwable, declaredType2);
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static RuntimeException propagate(Throwable throwable) {
/* 230 */     throwIfUnchecked(throwable);
/* 231 */     throw new RuntimeException(throwable);
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
/*     */   public static Throwable getRootCause(Throwable throwable) {
/* 247 */     Throwable slowPointer = throwable;
/* 248 */     boolean advanceSlowPointer = false;
/*     */     
/*     */     Throwable cause;
/* 251 */     while ((cause = throwable.getCause()) != null) {
/* 252 */       throwable = cause;
/*     */       
/* 254 */       if (throwable == slowPointer) {
/* 255 */         throw new IllegalArgumentException("Loop in causal chain detected.", throwable);
/*     */       }
/* 257 */       if (advanceSlowPointer) {
/* 258 */         slowPointer = slowPointer.getCause();
/*     */       }
/* 260 */       advanceSlowPointer = !advanceSlowPointer;
/*     */     } 
/* 262 */     return throwable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Throwable> getCausalChain(Throwable throwable) {
/* 282 */     Preconditions.checkNotNull(throwable);
/* 283 */     List<Throwable> causes = new ArrayList<>(4);
/* 284 */     causes.add(throwable);
/*     */ 
/*     */ 
/*     */     
/* 288 */     Throwable slowPointer = throwable;
/* 289 */     boolean advanceSlowPointer = false;
/*     */     
/*     */     Throwable cause;
/* 292 */     while ((cause = throwable.getCause()) != null) {
/* 293 */       throwable = cause;
/* 294 */       causes.add(throwable);
/*     */       
/* 296 */       if (throwable == slowPointer) {
/* 297 */         throw new IllegalArgumentException("Loop in causal chain detected.", throwable);
/*     */       }
/* 299 */       if (advanceSlowPointer) {
/* 300 */         slowPointer = slowPointer.getCause();
/*     */       }
/* 302 */       advanceSlowPointer = !advanceSlowPointer;
/*     */     } 
/* 304 */     return Collections.unmodifiableList(causes);
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
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static <X extends Throwable> X getCauseAs(Throwable throwable, Class<X> expectedCauseType) {
/*     */     try {
/* 326 */       return expectedCauseType.cast(throwable.getCause());
/* 327 */     } catch (ClassCastException e) {
/* 328 */       e.initCause(throwable);
/* 329 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static String getStackTraceAsString(Throwable throwable) {
/* 341 */     StringWriter stringWriter = new StringWriter();
/* 342 */     throwable.printStackTrace(new PrintWriter(stringWriter));
/* 343 */     return stringWriter.toString();
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
/*     */   @Deprecated
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static List<StackTraceElement> lazyStackTrace(Throwable throwable) {
/* 380 */     return lazyStackTraceIsLazy() ? 
/* 381 */       jlaStackTrace(throwable) : 
/* 382 */       Collections.<StackTraceElement>unmodifiableList(Arrays.asList(throwable.getStackTrace()));
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
/*     */   @Deprecated
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static boolean lazyStackTraceIsLazy() {
/* 397 */     return (getStackTraceElementMethod != null && getStackTraceDepthMethod != null);
/*     */   }
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   private static List<StackTraceElement> jlaStackTrace(final Throwable t) {
/* 403 */     Preconditions.checkNotNull(t);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 410 */     return new AbstractList<StackTraceElement>()
/*     */       {
/*     */ 
/*     */ 
/*     */         
/*     */         public StackTraceElement get(int n)
/*     */         {
/* 417 */           return 
/* 418 */             (StackTraceElement)Throwables.invokeAccessibleNonThrowingMethod(
/* 419 */               Objects.<Method>requireNonNull(Throwables.getStackTraceElementMethod), Objects.requireNonNull(Throwables.jla), new Object[] { this.val$t, Integer.valueOf(n) });
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 424 */           return (
/* 425 */             (Integer)Throwables.invokeAccessibleNonThrowingMethod(
/* 426 */               Objects.<Method>requireNonNull(Throwables.getStackTraceDepthMethod), Objects.requireNonNull(Throwables.jla), new Object[] { this.val$t })).intValue();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   private static Object invokeAccessibleNonThrowingMethod(Method method, Object receiver, Object... params) {
/*     */     try {
/* 436 */       return method.invoke(receiver, params);
/* 437 */     } catch (IllegalAccessException e) {
/* 438 */       throw new RuntimeException(e);
/* 439 */     } catch (InvocationTargetException e) {
/* 440 */       throw propagate(e.getCause());
/*     */     } 
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
/*     */   @CheckForNull
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/* 458 */   private static final Object jla = getJLA();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/* 467 */   private static final Method getStackTraceElementMethod = (jla == null) ? null : getGetMethod();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/* 476 */   private static final Method getStackTraceDepthMethod = (jla == null) ? null : getSizeMethod(jla);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   private static Object getJLA() {
/*     */     try {
/* 491 */       Class<?> sharedSecrets = Class.forName("sun.misc.SharedSecrets", false, null);
/* 492 */       Method langAccess = sharedSecrets.getMethod("getJavaLangAccess", new Class[0]);
/* 493 */       return langAccess.invoke(null, new Object[0]);
/* 494 */     } catch (ThreadDeath death) {
/* 495 */       throw death;
/* 496 */     } catch (Throwable t) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 501 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   private static Method getGetMethod() {
/* 513 */     return getJlaMethod("getStackTraceElement", new Class[] { Throwable.class, int.class });
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
/*     */   @CheckForNull
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   private static Method getSizeMethod(Object jla) {
/*     */     try {
/* 530 */       Method getStackTraceDepth = getJlaMethod("getStackTraceDepth", new Class[] { Throwable.class });
/* 531 */       if (getStackTraceDepth == null) {
/* 532 */         return null;
/*     */       }
/* 534 */       getStackTraceDepth.invoke(jla, new Object[] { new Throwable() });
/* 535 */       return getStackTraceDepth;
/* 536 */     } catch (UnsupportedOperationException|IllegalAccessException|InvocationTargetException e) {
/* 537 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   private static Method getJlaMethod(String name, Class<?>... parameterTypes) throws ThreadDeath {
/*     */     try {
/* 546 */       return Class.forName("sun.misc.JavaLangAccess", false, null).getMethod(name, parameterTypes);
/* 547 */     } catch (ThreadDeath death) {
/* 548 */       throw death;
/* 549 */     } catch (Throwable t) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 554 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Throwables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */