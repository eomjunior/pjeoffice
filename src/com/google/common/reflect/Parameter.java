/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.FluentIterable;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.AnnotatedType;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ public final class Parameter
/*     */   implements AnnotatedElement
/*     */ {
/*     */   private final Invokable<?, ?> declaration;
/*     */   private final int position;
/*     */   private final TypeToken<?> type;
/*     */   private final ImmutableList<Annotation> annotations;
/*     */   private final Object annotatedType;
/*     */   
/*     */   Parameter(Invokable<?, ?> declaration, int position, TypeToken<?> type, Annotation[] annotations, Object annotatedType) {
/*  57 */     this.declaration = declaration;
/*  58 */     this.position = position;
/*  59 */     this.type = type;
/*  60 */     this.annotations = ImmutableList.copyOf((Object[])annotations);
/*  61 */     this.annotatedType = annotatedType;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeToken<?> getType() {
/*  66 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public Invokable<?, ?> getDeclaringInvokable() {
/*  71 */     return this.declaration;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
/*  76 */     return (getAnnotation(annotationType) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
/*  82 */     Preconditions.checkNotNull(annotationType);
/*  83 */     for (UnmodifiableIterator<Annotation> unmodifiableIterator = this.annotations.iterator(); unmodifiableIterator.hasNext(); ) { Annotation annotation = unmodifiableIterator.next();
/*  84 */       if (annotationType.isInstance(annotation)) {
/*  85 */         return annotationType.cast(annotation);
/*     */       } }
/*     */     
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Annotation[] getAnnotations() {
/*  93 */     return getDeclaredAnnotations();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A[] getAnnotationsByType(Class<A> annotationType) {
/* 101 */     return getDeclaredAnnotationsByType(annotationType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Annotation[] getDeclaredAnnotations() {
/* 107 */     return (Annotation[])this.annotations.toArray((Object[])new Annotation[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public <A extends Annotation> A getDeclaredAnnotation(Class<A> annotationType) {
/* 116 */     Preconditions.checkNotNull(annotationType);
/* 117 */     return (A)FluentIterable.from((Iterable)this.annotations).filter(annotationType).first().orNull();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A[] getDeclaredAnnotationsByType(Class<A> annotationType) {
/* 126 */     Annotation[] arrayOfAnnotation = (Annotation[])FluentIterable.from((Iterable)this.annotations).filter(annotationType).toArray(annotationType);
/*     */     
/* 128 */     return (A[])arrayOfAnnotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AnnotatedType getAnnotatedType() {
/* 139 */     return Objects.<AnnotatedType>requireNonNull((AnnotatedType)this.annotatedType);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object obj) {
/* 144 */     if (obj instanceof Parameter) {
/* 145 */       Parameter that = (Parameter)obj;
/* 146 */       return (this.position == that.position && this.declaration.equals(that.declaration));
/*     */     } 
/* 148 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 153 */     return this.position;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 158 */     return this.type + " arg" + this.position;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/reflect/Parameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */