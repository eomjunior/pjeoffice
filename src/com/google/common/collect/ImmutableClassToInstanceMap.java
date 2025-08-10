/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Primitives;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.DoNotCall;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.io.Serializable;
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
/*     */ @Immutable(containerOf = {"B"})
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtIncompatible
/*     */ public final class ImmutableClassToInstanceMap<B>
/*     */   extends ForwardingMap<Class<? extends B>, B>
/*     */   implements ClassToInstanceMap<B>, Serializable
/*     */ {
/*  46 */   private static final ImmutableClassToInstanceMap<Object> EMPTY = new ImmutableClassToInstanceMap(
/*  47 */       ImmutableMap.of());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ImmutableMap<Class<? extends B>, B> delegate;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <B> ImmutableClassToInstanceMap<B> of() {
/*  58 */     return (ImmutableClassToInstanceMap)EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <B, T extends B> ImmutableClassToInstanceMap<B> of(Class<T> type, T value) {
/*  67 */     ImmutableMap<Class<? extends B>, B> map = ImmutableMap.of(type, (B)value);
/*  68 */     return new ImmutableClassToInstanceMap<>(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <B> Builder<B> builder() {
/*  76 */     return new Builder<>();
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
/*     */   public static final class Builder<B>
/*     */   {
/*  97 */     private final ImmutableMap.Builder<Class<? extends B>, B> mapBuilder = ImmutableMap.builder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public <T extends B> Builder<B> put(Class<T> key, T value) {
/* 105 */       this.mapBuilder.put(key, (B)value);
/* 106 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public <T extends B> Builder<B> putAll(Map<? extends Class<? extends T>, ? extends T> map) {
/* 118 */       for (Map.Entry<? extends Class<? extends T>, ? extends T> entry : map.entrySet()) {
/* 119 */         Class<? extends T> type = entry.getKey();
/* 120 */         T value = entry.getValue();
/* 121 */         this.mapBuilder.put(type, cast((Class)type, value));
/*     */       } 
/* 123 */       return this;
/*     */     }
/*     */     
/*     */     private static <T> T cast(Class<T> type, Object value) {
/* 127 */       return Primitives.wrap(type).cast(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableClassToInstanceMap<B> build() {
/* 137 */       ImmutableMap<Class<? extends B>, B> map = this.mapBuilder.buildOrThrow();
/* 138 */       if (map.isEmpty()) {
/* 139 */         return ImmutableClassToInstanceMap.of();
/*     */       }
/* 141 */       return new ImmutableClassToInstanceMap<>(map);
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
/*     */   public static <B, S extends B> ImmutableClassToInstanceMap<B> copyOf(Map<? extends Class<? extends S>, ? extends S> map) {
/* 159 */     if (map instanceof ImmutableClassToInstanceMap) {
/*     */       
/* 161 */       Map<? extends Class<? extends S>, ? extends S> rawMap = map;
/*     */       
/* 163 */       ImmutableClassToInstanceMap<B> cast = (ImmutableClassToInstanceMap)rawMap;
/* 164 */       return cast;
/*     */     } 
/* 166 */     return (new Builder<>()).<S>putAll(map).build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ImmutableClassToInstanceMap(ImmutableMap<Class<? extends B>, B> delegate) {
/* 172 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<Class<? extends B>, B> delegate() {
/* 177 */     return this.delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public <T extends B> T getInstance(Class<T> type) {
/* 184 */     return (T)this.delegate.get(Preconditions.checkNotNull(type));
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
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   @DoNotCall("Always throws UnsupportedOperationException")
/*     */   public <T extends B> T putInstance(Class<T> type, T value) {
/* 199 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 203 */     return isEmpty() ? of() : this;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ImmutableClassToInstanceMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */