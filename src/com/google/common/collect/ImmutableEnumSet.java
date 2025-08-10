/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class ImmutableEnumSet<E extends Enum<E>>
/*     */   extends ImmutableSet<E>
/*     */ {
/*     */   private final transient EnumSet<E> delegate;
/*     */   @LazyInit
/*     */   private transient int hashCode;
/*     */   
/*     */   static <E extends Enum<E>> ImmutableSet<E> asImmutable(EnumSet<E> set) {
/*  41 */     switch (set.size()) {
/*     */       case 0:
/*  43 */         return ImmutableSet.of();
/*     */       case 1:
/*  45 */         return ImmutableSet.of((E)Iterables.<Enum>getOnlyElement(set));
/*     */     } 
/*  47 */     return new ImmutableEnumSet<>(set);
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
/*     */   private ImmutableEnumSet(EnumSet<E> delegate) {
/*  62 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/*  67 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  72 */     return Iterators.unmodifiableIterator(this.delegate.iterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/*  77 */     return this.delegate.spliterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super E> action) {
/*  82 */     this.delegate.forEach(action);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  87 */     return this.delegate.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@CheckForNull Object object) {
/*  92 */     return this.delegate.contains(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> collection) {
/*  97 */     if (collection instanceof ImmutableEnumSet) {
/*  98 */       collection = ((ImmutableEnumSet)collection).delegate;
/*     */     }
/* 100 */     return this.delegate.containsAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 105 */     return this.delegate.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object<E> object) {
/* 110 */     if (object == this) {
/* 111 */       return true;
/*     */     }
/* 113 */     if (object instanceof ImmutableEnumSet) {
/* 114 */       object = (Object<E>)((ImmutableEnumSet)object).delegate;
/*     */     }
/* 116 */     return this.delegate.equals(object);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isHashCodeFast() {
/* 121 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 128 */     int result = this.hashCode;
/* 129 */     return (result == 0) ? (this.hashCode = this.delegate.hashCode()) : result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 134 */     return this.delegate.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @J2ktIncompatible
/*     */   Object writeReplace() {
/* 141 */     return new EnumSerializedForm<>(this.delegate);
/*     */   }
/*     */   
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 146 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */   
/*     */   @J2ktIncompatible
/*     */   private static class EnumSerializedForm<E extends Enum<E>>
/*     */     implements Serializable
/*     */   {
/*     */     final EnumSet<E> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     EnumSerializedForm(EnumSet<E> delegate) {
/* 157 */       this.delegate = delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     Object readResolve() {
/* 162 */       return new ImmutableEnumSet<>(this.delegate.clone());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableEnumSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */