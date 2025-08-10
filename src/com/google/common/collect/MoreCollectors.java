/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collector;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public final class MoreCollectors
/*     */ {
/*  47 */   private static final Collector<Object, ?, Optional<Object>> TO_OPTIONAL = Collector.of(ToOptionalState::new, ToOptionalState::add, ToOptionalState::combine, ToOptionalState::getOptional, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Collector<T, ?, Optional<T>> toOptional() {
/*  64 */     return (Collector)TO_OPTIONAL;
/*     */   }
/*     */   
/*  67 */   private static final Object NULL_PLACEHOLDER = new Object();
/*     */   
/*     */   static {
/*  70 */     ONLY_ELEMENT = Collector.of(ToOptionalState::new, (state, o) -> state.add((o == null) ? NULL_PLACEHOLDER : o), ToOptionalState::combine, state -> {
/*     */           Object result = state.getElement();
/*     */           return (result == NULL_PLACEHOLDER) ? null : result;
/*     */         }new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Collector<Object, ?, Object> ONLY_ELEMENT;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Collector<T, ?, T> onlyElement() {
/*  87 */     return (Collector)ONLY_ELEMENT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ToOptionalState
/*     */   {
/*     */     static final int MAX_EXTRAS = 4;
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/* 101 */     Object element = null;
/* 102 */     List<Object> extras = Collections.emptyList();
/*     */ 
/*     */ 
/*     */     
/*     */     IllegalArgumentException multiples(boolean overflow) {
/* 107 */       StringBuilder sb = (new StringBuilder()).append("expected one element but was: <").append(this.element);
/* 108 */       for (Object o : this.extras) {
/* 109 */         sb.append(", ").append(o);
/*     */       }
/* 111 */       if (overflow) {
/* 112 */         sb.append(", ...");
/*     */       }
/* 114 */       sb.append('>');
/* 115 */       throw new IllegalArgumentException(sb.toString());
/*     */     }
/*     */     
/*     */     void add(Object o) {
/* 119 */       Preconditions.checkNotNull(o);
/* 120 */       if (this.element == null) {
/* 121 */         this.element = o;
/* 122 */       } else if (this.extras.isEmpty()) {
/*     */         
/* 124 */         this.extras = new ArrayList(4);
/* 125 */         this.extras.add(o);
/* 126 */       } else if (this.extras.size() < 4) {
/* 127 */         this.extras.add(o);
/*     */       } else {
/* 129 */         throw multiples(true);
/*     */       } 
/*     */     }
/*     */     
/*     */     ToOptionalState combine(ToOptionalState other) {
/* 134 */       if (this.element == null)
/* 135 */         return other; 
/* 136 */       if (other.element == null) {
/* 137 */         return this;
/*     */       }
/* 139 */       if (this.extras.isEmpty())
/*     */       {
/* 141 */         this.extras = new ArrayList();
/*     */       }
/* 143 */       this.extras.add(other.element);
/* 144 */       this.extras.addAll(other.extras);
/* 145 */       if (this.extras.size() > 4) {
/* 146 */         this.extras.subList(4, this.extras.size()).clear();
/* 147 */         throw multiples(true);
/*     */       } 
/* 149 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     Optional<Object> getOptional() {
/* 154 */       if (this.extras.isEmpty()) {
/* 155 */         return Optional.ofNullable(this.element);
/*     */       }
/* 157 */       throw multiples(false);
/*     */     }
/*     */ 
/*     */     
/*     */     Object getElement() {
/* 162 */       if (this.element == null)
/* 163 */         throw new NoSuchElementException(); 
/* 164 */       if (this.extras.isEmpty()) {
/* 165 */         return this.element;
/*     */       }
/* 167 */       throw multiples(false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/MoreCollectors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */