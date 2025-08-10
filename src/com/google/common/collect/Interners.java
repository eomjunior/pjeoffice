/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Equivalence;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public final class Interners
/*     */ {
/*     */   public static class InternerBuilder
/*     */   {
/*  46 */     private final MapMaker mapMaker = new MapMaker();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean strong = true;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public InternerBuilder strong() {
/*  57 */       this.strong = true;
/*  58 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GwtIncompatible("java.lang.ref.WeakReference")
/*     */     public InternerBuilder weak() {
/*  68 */       this.strong = false;
/*  69 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public InternerBuilder concurrencyLevel(int concurrencyLevel) {
/*  78 */       this.mapMaker.concurrencyLevel(concurrencyLevel);
/*  79 */       return this;
/*     */     }
/*     */     
/*     */     public <E> Interner<E> build() {
/*  83 */       if (!this.strong) {
/*  84 */         this.mapMaker.weakKeys();
/*     */       }
/*  86 */       return new Interners.InternerImpl<>(this.mapMaker);
/*     */     }
/*     */     
/*     */     private InternerBuilder() {} }
/*     */   
/*     */   public static InternerBuilder newBuilder() {
/*  92 */     return new InternerBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Interner<E> newStrongInterner() {
/* 101 */     return newBuilder().strong().build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible("java.lang.ref.WeakReference")
/*     */   public static <E> Interner<E> newWeakInterner() {
/* 112 */     return newBuilder().weak().build();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class InternerImpl<E> implements Interner<E> {
/*     */     @VisibleForTesting
/*     */     final MapMakerInternalMap<E, MapMaker.Dummy, ?, ?> map;
/*     */     
/*     */     private InternerImpl(MapMaker mapMaker) {
/* 121 */       this
/* 122 */         .map = MapMakerInternalMap.createWithDummyValues(mapMaker.keyEquivalence(Equivalence.equals()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public E intern(E sample) {
/*     */       while (true) {
/* 130 */         MapMakerInternalMap.InternalEntry entry = (MapMakerInternalMap.InternalEntry)this.map.getEntry(sample);
/* 131 */         if (entry != null) {
/* 132 */           Object canonical = entry.getKey();
/* 133 */           if (canonical != null) {
/*     */ 
/*     */             
/* 136 */             E result = (E)canonical;
/* 137 */             return result;
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 142 */         MapMaker.Dummy sneaky = this.map.putIfAbsent(sample, MapMaker.Dummy.VALUE);
/* 143 */         if (sneaky == null) {
/* 144 */           return sample;
/*     */         }
/*     */       } 
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
/*     */   public static <E> Function<E, E> asFunction(Interner<E> interner) {
/* 163 */     return new InternerFunction<>((Interner<E>)Preconditions.checkNotNull(interner));
/*     */   }
/*     */   
/*     */   private static class InternerFunction<E>
/*     */     implements Function<E, E> {
/*     */     private final Interner<E> interner;
/*     */     
/*     */     public InternerFunction(Interner<E> interner) {
/* 171 */       this.interner = interner;
/*     */     }
/*     */ 
/*     */     
/*     */     public E apply(E input) {
/* 176 */       return this.interner.intern(input);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 181 */       return this.interner.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object other) {
/* 186 */       if (other instanceof InternerFunction) {
/* 187 */         InternerFunction<?> that = (InternerFunction)other;
/* 188 */         return this.interner.equals(that.interner);
/*     */       } 
/*     */       
/* 191 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/Interners.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */