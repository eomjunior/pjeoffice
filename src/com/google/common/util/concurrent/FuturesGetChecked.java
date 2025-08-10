/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CopyOnWriteArraySet;
/*     */ import java.util.concurrent.ExecutionException;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ final class FuturesGetChecked
/*     */ {
/*     */   private static final Ordering<List<Class<?>>> ORDERING_BY_CONSTRUCTOR_PARAMETER_LIST;
/*     */   private static final Ordering<Constructor<?>> WITH_STRING_PARAM_THEN_WITH_THROWABLE_PARAM;
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass) throws X {
/*  50 */     return getChecked(bestGetCheckedTypeValidator(), future, exceptionClass);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   @VisibleForTesting
/*     */   static <V, X extends Exception> V getChecked(GetCheckedTypeValidator validator, Future<V> future, Class<X> exceptionClass) throws X {
/*  59 */     validator.validateClass(exceptionClass);
/*     */     try {
/*  61 */       return future.get();
/*  62 */     } catch (InterruptedException e) {
/*  63 */       Thread.currentThread().interrupt();
/*  64 */       throw newWithCause(exceptionClass, e);
/*  65 */     } catch (ExecutionException e) {
/*  66 */       wrapAndThrowExceptionOrError(e.getCause(), exceptionClass);
/*  67 */       throw (X)new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass, long timeout, TimeUnit unit) throws X {
/*  77 */     bestGetCheckedTypeValidator().validateClass(exceptionClass);
/*     */     try {
/*  79 */       return future.get(timeout, unit);
/*  80 */     } catch (InterruptedException e) {
/*  81 */       Thread.currentThread().interrupt();
/*  82 */       throw newWithCause(exceptionClass, e);
/*  83 */     } catch (TimeoutException e) {
/*  84 */       throw newWithCause(exceptionClass, e);
/*  85 */     } catch (ExecutionException e) {
/*  86 */       wrapAndThrowExceptionOrError(e.getCause(), exceptionClass);
/*  87 */       throw (X)new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static GetCheckedTypeValidator bestGetCheckedTypeValidator() {
/*  97 */     return GetCheckedTypeValidatorHolder.BEST_VALIDATOR;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static GetCheckedTypeValidator weakSetValidator() {
/* 102 */     return GetCheckedTypeValidatorHolder.WeakSetValidator.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static GetCheckedTypeValidator classValueValidator() {
/* 108 */     return GetCheckedTypeValidatorHolder.ClassValueValidator.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class GetCheckedTypeValidatorHolder
/*     */   {
/* 119 */     static final String CLASS_VALUE_VALIDATOR_NAME = GetCheckedTypeValidatorHolder.class
/* 120 */       .getName() + "$ClassValueValidator";
/*     */     
/* 122 */     static final FuturesGetChecked.GetCheckedTypeValidator BEST_VALIDATOR = getBestValidator();
/*     */     
/*     */     enum ClassValueValidator
/*     */       implements FuturesGetChecked.GetCheckedTypeValidator {
/* 126 */       INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 132 */       private static final ClassValue<Boolean> isValidClass = new ClassValue<Boolean>()
/*     */         {
/*     */           protected Boolean computeValue(Class<?> type)
/*     */           {
/* 136 */             FuturesGetChecked.checkExceptionClassValidity(type.asSubclass(Exception.class));
/* 137 */             return Boolean.valueOf(true);
/*     */           }
/*     */         }; static {
/*     */       
/*     */       }
/*     */       public void validateClass(Class<? extends Exception> exceptionClass) {
/* 143 */         isValidClass.get(exceptionClass);
/*     */       }
/*     */     }
/*     */     
/*     */     enum WeakSetValidator implements FuturesGetChecked.GetCheckedTypeValidator {
/* 148 */       INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 158 */       private static final Set<WeakReference<Class<? extends Exception>>> validClasses = new CopyOnWriteArraySet<>();
/*     */       static {
/*     */       
/*     */       }
/*     */       public void validateClass(Class<? extends Exception> exceptionClass) {
/* 163 */         for (WeakReference<Class<? extends Exception>> knownGood : validClasses) {
/* 164 */           if (exceptionClass.equals(knownGood.get())) {
/*     */             return;
/*     */           }
/*     */         } 
/*     */         
/* 169 */         FuturesGetChecked.checkExceptionClassValidity(exceptionClass);
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
/* 180 */         if (validClasses.size() > 1000) {
/* 181 */           validClasses.clear();
/*     */         }
/*     */         
/* 184 */         validClasses.add(new WeakReference<>(exceptionClass));
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static FuturesGetChecked.GetCheckedTypeValidator getBestValidator() {
/*     */       try {
/* 195 */         Class<? extends Enum> theClass = Class.forName(CLASS_VALUE_VALIDATOR_NAME).asSubclass(Enum.class);
/* 196 */         return (FuturesGetChecked.GetCheckedTypeValidator)((Enum[])theClass.getEnumConstants())[0];
/* 197 */       } catch (ClassNotFoundException|RuntimeException|Error t) {
/*     */ 
/*     */         
/* 200 */         return FuturesGetChecked.weakSetValidator();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <X extends Exception> void wrapAndThrowExceptionOrError(Throwable cause, Class<X> exceptionClass) throws X {
/* 208 */     if (cause instanceof Error) {
/* 209 */       throw (X)new ExecutionError((Error)cause);
/*     */     }
/* 211 */     if (cause instanceof RuntimeException) {
/* 212 */       throw (X)new UncheckedExecutionException(cause);
/*     */     }
/* 214 */     throw newWithCause(exceptionClass, cause);
/*     */   }
/*     */   enum ClassValueValidator implements GetCheckedTypeValidator {
/*     */     INSTANCE;
/*     */     private static final ClassValue<Boolean> isValidClass = new ClassValue<Boolean>() { protected Boolean computeValue(Class<?> type) { FuturesGetChecked.checkExceptionClassValidity(type.asSubclass(Exception.class)); return Boolean.valueOf(true); } }
/*     */     ; static {  } public void validateClass(Class<? extends Exception> exceptionClass) { isValidClass.get(exceptionClass); } } enum WeakSetValidator implements GetCheckedTypeValidator {
/*     */     INSTANCE; private static final Set<WeakReference<Class<? extends Exception>>> validClasses = new CopyOnWriteArraySet<>(); static {  } public void validateClass(Class<? extends Exception> exceptionClass) { for (WeakReference<Class<? extends Exception>> knownGood : validClasses) { if (exceptionClass.equals(knownGood.get()))
/*     */           return;  }
/*     */        FuturesGetChecked.checkExceptionClassValidity(exceptionClass); if (validClasses.size() > 1000)
/*     */         validClasses.clear();  validClasses.add(new WeakReference<>(exceptionClass)); }
/*     */   } private static boolean hasConstructorUsableByGetChecked(Class<? extends Exception> exceptionClass) { try {
/* 225 */       Exception unused = newWithCause((Class)exceptionClass, new Exception());
/* 226 */       return true;
/* 227 */     } catch (Throwable t) {
/* 228 */       return false;
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <X extends Exception> X newWithCause(Class<X> exceptionClass, Throwable cause) {
/* 235 */     List<Constructor<X>> constructors = (List)Arrays.asList(exceptionClass.getConstructors());
/* 236 */     for (Constructor<X> constructor : preferringStringsThenThrowables(constructors)) {
/* 237 */       Exception exception = newFromConstructor(constructor, cause);
/* 238 */       if (exception != null) {
/* 239 */         if (exception.getCause() == null) {
/* 240 */           exception.initCause(cause);
/*     */         }
/* 242 */         return (X)exception;
/*     */       } 
/*     */     } 
/* 245 */     throw new IllegalArgumentException("No appropriate constructor for exception of type " + exceptionClass + " in response to chained exception", cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <X extends Exception> List<Constructor<X>> preferringStringsThenThrowables(List<Constructor<X>> constructors) {
/* 254 */     return WITH_STRING_PARAM_THEN_WITH_THROWABLE_PARAM.sortedCopy(constructors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 264 */     ORDERING_BY_CONSTRUCTOR_PARAMETER_LIST = Ordering.natural().onResultOf(params -> Boolean.valueOf(params.contains(String.class))).compound((Comparator)Ordering.natural().onResultOf(params -> Boolean.valueOf(params.contains(Throwable.class)))).reverse();
/*     */     
/* 266 */     WITH_STRING_PARAM_THEN_WITH_THROWABLE_PARAM = ORDERING_BY_CONSTRUCTOR_PARAMETER_LIST.onResultOf(constructor -> Arrays.asList(constructor.getParameterTypes()));
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   private static <X> X newFromConstructor(Constructor<X> constructor, Throwable cause) {
/* 271 */     Class<?>[] paramTypes = constructor.getParameterTypes();
/* 272 */     Object[] params = new Object[paramTypes.length];
/* 273 */     for (int i = 0; i < paramTypes.length; i++) {
/* 274 */       Class<?> paramType = paramTypes[i];
/* 275 */       if (paramType.equals(String.class)) {
/* 276 */         params[i] = cause.toString();
/* 277 */       } else if (paramType.equals(Throwable.class)) {
/* 278 */         params[i] = cause;
/*     */       } else {
/* 280 */         return null;
/*     */       } 
/*     */     } 
/*     */     try {
/* 284 */       return constructor.newInstance(params);
/* 285 */     } catch (IllegalArgumentException|InstantiationException|IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/*     */ 
/*     */ 
/*     */       
/* 289 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static boolean isCheckedException(Class<? extends Exception> type) {
/* 295 */     return !RuntimeException.class.isAssignableFrom(type);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static void checkExceptionClassValidity(Class<? extends Exception> exceptionClass) {
/* 300 */     Preconditions.checkArgument(
/* 301 */         isCheckedException(exceptionClass), "Futures.getChecked exception type (%s) must not be a RuntimeException", exceptionClass);
/*     */ 
/*     */     
/* 304 */     Preconditions.checkArgument(
/* 305 */         hasConstructorUsableByGetChecked(exceptionClass), "Futures.getChecked exception type (%s) must be an accessible class with an accessible constructor whose parameters (if any) must be of type String and/or Throwable", exceptionClass);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static interface GetCheckedTypeValidator {
/*     */     void validateClass(Class<? extends Exception> param1Class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/FuturesGetChecked.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */