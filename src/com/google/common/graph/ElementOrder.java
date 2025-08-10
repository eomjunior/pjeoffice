/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ @Immutable
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @Beta
/*     */ public final class ElementOrder<T>
/*     */ {
/*     */   private final Type type;
/*     */   @CheckForNull
/*     */   private final Comparator<T> comparator;
/*     */   
/*     */   public enum Type
/*     */   {
/*  69 */     UNORDERED,
/*  70 */     STABLE,
/*  71 */     INSERTION,
/*  72 */     SORTED;
/*     */   }
/*     */   
/*     */   private ElementOrder(Type type, @CheckForNull Comparator<T> comparator) {
/*  76 */     this.type = (Type)Preconditions.checkNotNull(type);
/*  77 */     this.comparator = comparator;
/*  78 */     Preconditions.checkState((((type == Type.SORTED) ? true : false) == ((comparator != null) ? true : false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <S> ElementOrder<S> unordered() {
/*  83 */     return new ElementOrder<>(Type.UNORDERED, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <S> ElementOrder<S> stable() {
/* 123 */     return new ElementOrder<>(Type.STABLE, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <S> ElementOrder<S> insertion() {
/* 128 */     return new ElementOrder<>(Type.INSERTION, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <S extends Comparable<? super S>> ElementOrder<S> natural() {
/* 135 */     return new ElementOrder<>(Type.SORTED, (Comparator<S>)Ordering.natural());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <S> ElementOrder<S> sorted(Comparator<S> comparator) {
/* 143 */     return new ElementOrder<>(Type.SORTED, (Comparator<S>)Preconditions.checkNotNull(comparator));
/*     */   }
/*     */ 
/*     */   
/*     */   public Type type() {
/* 148 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<T> comparator() {
/* 157 */     if (this.comparator != null) {
/* 158 */       return this.comparator;
/*     */     }
/* 160 */     throw new UnsupportedOperationException("This ordering does not define a comparator.");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@CheckForNull Object obj) {
/* 165 */     if (obj == this) {
/* 166 */       return true;
/*     */     }
/* 168 */     if (!(obj instanceof ElementOrder)) {
/* 169 */       return false;
/*     */     }
/*     */     
/* 172 */     ElementOrder<?> other = (ElementOrder)obj;
/* 173 */     return (this.type == other.type && Objects.equal(this.comparator, other.comparator));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 178 */     return Objects.hashCode(new Object[] { this.type, this.comparator });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 183 */     MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper(this).add("type", this.type);
/* 184 */     if (this.comparator != null) {
/* 185 */       helper.add("comparator", this.comparator);
/*     */     }
/* 187 */     return helper.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   <K extends T, V> Map<K, V> createMap(int expectedSize) {
/* 192 */     switch (this.type) {
/*     */       case UNORDERED:
/* 194 */         return Maps.newHashMapWithExpectedSize(expectedSize);
/*     */       case INSERTION:
/*     */       case STABLE:
/* 197 */         return Maps.newLinkedHashMapWithExpectedSize(expectedSize);
/*     */       case SORTED:
/* 199 */         return Maps.newTreeMap(comparator());
/*     */     } 
/* 201 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   <T1 extends T> ElementOrder<T1> cast() {
/* 207 */     return this;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/graph/ElementOrder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */