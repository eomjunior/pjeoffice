/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.AnnotatedType;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ public abstract class Invokable<T, R>
/*     */   implements AnnotatedElement, Member
/*     */ {
/*     */   private final AccessibleObject accessibleObject;
/*     */   private final Member member;
/*     */   
/*     */   <M extends AccessibleObject & Member> Invokable(M member) {
/*  71 */     Preconditions.checkNotNull(member);
/*  72 */     this.accessibleObject = (AccessibleObject)member;
/*  73 */     this.member = (Member)member;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Invokable<?, Object> from(Method method) {
/*  78 */     return new MethodInvokable(method);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> Invokable<T, T> from(Constructor<T> constructor) {
/*  83 */     return new ConstructorInvokable<>(constructor);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
/*  88 */     return this.accessibleObject.isAnnotationPresent(annotationClass);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public final <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
/*  94 */     return this.accessibleObject.getAnnotation(annotationClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Annotation[] getAnnotations() {
/*  99 */     return this.accessibleObject.getAnnotations();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Annotation[] getDeclaredAnnotations() {
/* 104 */     return this.accessibleObject.getDeclaredAnnotations();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract TypeVariable<?>[] getTypeParameters();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setAccessible(boolean flag) {
/* 116 */     this.accessibleObject.setAccessible(flag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean trySetAccessible() {
/*     */     try {
/* 125 */       this.accessibleObject.setAccessible(true);
/* 126 */       return true;
/* 127 */     } catch (Exception e) {
/* 128 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isAccessible() {
/* 134 */     return this.accessibleObject.isAccessible();
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/* 139 */     return this.member.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getModifiers() {
/* 144 */     return this.member.getModifiers();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isSynthetic() {
/* 149 */     return this.member.isSynthetic();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isPublic() {
/* 154 */     return Modifier.isPublic(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isProtected() {
/* 159 */     return Modifier.isProtected(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isPackagePrivate() {
/* 164 */     return (!isPrivate() && !isPublic() && !isProtected());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isPrivate() {
/* 169 */     return Modifier.isPrivate(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isStatic() {
/* 174 */     return Modifier.isStatic(getModifiers());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isFinal() {
/* 185 */     return Modifier.isFinal(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isAbstract() {
/* 190 */     return Modifier.isAbstract(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isNative() {
/* 195 */     return Modifier.isNative(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isSynchronized() {
/* 200 */     return Modifier.isSynchronized(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean isVolatile() {
/* 205 */     return Modifier.isVolatile(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean isTransient() {
/* 210 */     return Modifier.isTransient(getModifiers());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object obj) {
/* 215 */     if (obj instanceof Invokable) {
/* 216 */       Invokable<?, ?> that = (Invokable<?, ?>)obj;
/* 217 */       return (getOwnerType().equals(that.getOwnerType()) && this.member.equals(that.member));
/*     */     } 
/* 219 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 224 */     return this.member.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 229 */     return this.member.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isOverridable();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isVarArgs();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public final R invoke(@CheckForNull T receiver, Object... args) throws InvocationTargetException, IllegalAccessException {
/* 260 */     return (R)invokeInternal(receiver, (Object[])Preconditions.checkNotNull(args));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final TypeToken<? extends R> getReturnType() {
/* 267 */     return (TypeToken)TypeToken.of(getGenericReturnType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @IgnoreJRERequirement
/*     */   public final ImmutableList<Parameter> getParameters() {
/* 277 */     Type[] parameterTypes = getGenericParameterTypes();
/* 278 */     Annotation[][] annotations = getParameterAnnotations();
/*     */     
/* 280 */     Object[] annotatedTypes = ANNOTATED_TYPE_EXISTS ? (Object[])getAnnotatedParameterTypes() : new Object[parameterTypes.length];
/* 281 */     ImmutableList.Builder<Parameter> builder = ImmutableList.builder();
/* 282 */     for (int i = 0; i < parameterTypes.length; i++) {
/* 283 */       builder.add(new Parameter(this, i, 
/*     */             
/* 285 */             TypeToken.of(parameterTypes[i]), annotations[i], annotatedTypes[i]));
/*     */     }
/* 287 */     return builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public final ImmutableList<TypeToken<? extends Throwable>> getExceptionTypes() {
/* 292 */     ImmutableList.Builder<TypeToken<? extends Throwable>> builder = ImmutableList.builder();
/* 293 */     for (Type type : getGenericExceptionTypes()) {
/*     */ 
/*     */ 
/*     */       
/* 297 */       TypeToken<? extends Throwable> exceptionType = (TypeToken)TypeToken.of(type);
/* 298 */       builder.add(exceptionType);
/*     */     } 
/* 300 */     return builder.build();
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
/*     */   public final <R1 extends R> Invokable<T, R1> returning(Class<R1> returnType) {
/* 312 */     return returning(TypeToken.of(returnType));
/*     */   }
/*     */ 
/*     */   
/*     */   public final <R1 extends R> Invokable<T, R1> returning(TypeToken<R1> returnType) {
/* 317 */     if (!returnType.isSupertypeOf(getReturnType())) {
/* 318 */       throw new IllegalArgumentException("Invokable is known to return " + 
/* 319 */           getReturnType() + ", not " + returnType);
/*     */     }
/*     */     
/* 322 */     Invokable<T, R1> specialized = this;
/* 323 */     return specialized;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Class<? super T> getDeclaringClass() {
/* 329 */     return (Class)this.member.getDeclaringClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeToken<T> getOwnerType() {
/* 336 */     return TypeToken.of((Class)getDeclaringClass());
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
/*     */   static class MethodInvokable<T>
/*     */     extends Invokable<T, Object>
/*     */   {
/*     */     final Method method;
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
/*     */     MethodInvokable(Method method) {
/* 368 */       super(method);
/* 369 */       this.method = method;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     final Object invokeInternal(@CheckForNull Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException {
/* 376 */       return this.method.invoke(receiver, args);
/*     */     }
/*     */ 
/*     */     
/*     */     Type getGenericReturnType() {
/* 381 */       return this.method.getGenericReturnType();
/*     */     }
/*     */ 
/*     */     
/*     */     Type[] getGenericParameterTypes() {
/* 386 */       return this.method.getGenericParameterTypes();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     AnnotatedType[] getAnnotatedParameterTypes() {
/* 392 */       return this.method.getAnnotatedParameterTypes();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public AnnotatedType getAnnotatedReturnType() {
/* 398 */       return this.method.getAnnotatedReturnType();
/*     */     }
/*     */ 
/*     */     
/*     */     Type[] getGenericExceptionTypes() {
/* 403 */       return this.method.getGenericExceptionTypes();
/*     */     }
/*     */ 
/*     */     
/*     */     final Annotation[][] getParameterAnnotations() {
/* 408 */       return this.method.getParameterAnnotations();
/*     */     }
/*     */ 
/*     */     
/*     */     public final TypeVariable<?>[] getTypeParameters() {
/* 413 */       return (TypeVariable<?>[])this.method.getTypeParameters();
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isOverridable() {
/* 418 */       return (!isFinal() && 
/* 419 */         !isPrivate() && 
/* 420 */         !isStatic() && 
/* 421 */         !Modifier.isFinal(getDeclaringClass().getModifiers()));
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isVarArgs() {
/* 426 */       return this.method.isVarArgs();
/*     */     }
/*     */   }
/*     */   
/*     */   static class ConstructorInvokable<T>
/*     */     extends Invokable<T, T> {
/*     */     final Constructor<?> constructor;
/*     */     
/*     */     ConstructorInvokable(Constructor<?> constructor) {
/* 435 */       super(constructor);
/* 436 */       this.constructor = constructor;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     final Object invokeInternal(@CheckForNull Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException {
/*     */       try {
/* 443 */         return this.constructor.newInstance(args);
/* 444 */       } catch (InstantiationException e) {
/* 445 */         throw new RuntimeException(this.constructor + " failed.", e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Type getGenericReturnType() {
/* 455 */       Class<?> declaringClass = getDeclaringClass();
/* 456 */       TypeVariable[] arrayOfTypeVariable = (TypeVariable[])declaringClass.getTypeParameters();
/* 457 */       if (arrayOfTypeVariable.length > 0) {
/* 458 */         return Types.newParameterizedType(declaringClass, (Type[])arrayOfTypeVariable);
/*     */       }
/* 460 */       return declaringClass;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Type[] getGenericParameterTypes() {
/* 466 */       Type[] types = this.constructor.getGenericParameterTypes();
/* 467 */       if (types.length > 0 && mayNeedHiddenThis()) {
/* 468 */         Class<?>[] rawParamTypes = this.constructor.getParameterTypes();
/* 469 */         if (types.length == rawParamTypes.length && rawParamTypes[0] == 
/* 470 */           getDeclaringClass().getEnclosingClass())
/*     */         {
/* 472 */           return Arrays.<Type>copyOfRange(types, 1, types.length);
/*     */         }
/*     */       } 
/* 475 */       return types;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     AnnotatedType[] getAnnotatedParameterTypes() {
/* 481 */       return this.constructor.getAnnotatedParameterTypes();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public AnnotatedType getAnnotatedReturnType() {
/* 487 */       return this.constructor.getAnnotatedReturnType();
/*     */     }
/*     */ 
/*     */     
/*     */     Type[] getGenericExceptionTypes() {
/* 492 */       return this.constructor.getGenericExceptionTypes();
/*     */     }
/*     */ 
/*     */     
/*     */     final Annotation[][] getParameterAnnotations() {
/* 497 */       return this.constructor.getParameterAnnotations();
/*     */     }
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
/*     */     public final TypeVariable<?>[] getTypeParameters() {
/* 511 */       TypeVariable[] arrayOfTypeVariable1 = (TypeVariable[])getDeclaringClass().getTypeParameters();
/* 512 */       TypeVariable[] arrayOfTypeVariable2 = (TypeVariable[])this.constructor.getTypeParameters();
/* 513 */       TypeVariable[] arrayOfTypeVariable3 = new TypeVariable[arrayOfTypeVariable1.length + arrayOfTypeVariable2.length];
/*     */       
/* 515 */       System.arraycopy(arrayOfTypeVariable1, 0, arrayOfTypeVariable3, 0, arrayOfTypeVariable1.length);
/* 516 */       System.arraycopy(arrayOfTypeVariable2, 0, arrayOfTypeVariable3, arrayOfTypeVariable1.length, arrayOfTypeVariable2.length);
/*     */       
/* 518 */       return (TypeVariable<?>[])arrayOfTypeVariable3;
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isOverridable() {
/* 523 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isVarArgs() {
/* 528 */       return this.constructor.isVarArgs();
/*     */     }
/*     */     
/*     */     private boolean mayNeedHiddenThis() {
/* 532 */       Class<?> declaringClass = this.constructor.getDeclaringClass();
/* 533 */       if (declaringClass.getEnclosingConstructor() != null)
/*     */       {
/* 535 */         return true;
/*     */       }
/* 537 */       Method enclosingMethod = declaringClass.getEnclosingMethod();
/* 538 */       if (enclosingMethod != null)
/*     */       {
/* 540 */         return !Modifier.isStatic(enclosingMethod.getModifiers());
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 548 */       return (declaringClass.getEnclosingClass() != null && 
/* 549 */         !Modifier.isStatic(declaringClass.getModifiers()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 554 */   private static final boolean ANNOTATED_TYPE_EXISTS = initAnnotatedTypeExists();
/*     */   
/*     */   private static boolean initAnnotatedTypeExists() {
/*     */     try {
/* 558 */       Class.forName("java.lang.reflect.AnnotatedType");
/* 559 */     } catch (ClassNotFoundException e) {
/* 560 */       return false;
/*     */     } 
/* 562 */     return true;
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   abstract Object invokeInternal(@CheckForNull Object paramObject, Object[] paramArrayOfObject) throws InvocationTargetException, IllegalAccessException;
/*     */   
/*     */   abstract Type[] getGenericParameterTypes();
/*     */   
/*     */   abstract AnnotatedType[] getAnnotatedParameterTypes();
/*     */   
/*     */   abstract Type[] getGenericExceptionTypes();
/*     */   
/*     */   abstract Annotation[][] getParameterAnnotations();
/*     */   
/*     */   abstract Type getGenericReturnType();
/*     */   
/*     */   public abstract AnnotatedType getAnnotatedReturnType();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/reflect/Invokable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */