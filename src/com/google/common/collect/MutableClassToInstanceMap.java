/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Primitives;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public final class MutableClassToInstanceMap<B>
/*     */   extends ForwardingMap<Class<? extends B>, B>
/*     */   implements ClassToInstanceMap<B>, Serializable
/*     */ {
/*     */   private final Map<Class<? extends B>, B> delegate;
/*     */   
/*     */   public static <B> MutableClassToInstanceMap<B> create() {
/*  62 */     return new MutableClassToInstanceMap<>(new HashMap<>());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <B> MutableClassToInstanceMap<B> create(Map<Class<? extends B>, B> backingMap) {
/*  72 */     return new MutableClassToInstanceMap<>(backingMap);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private MutableClassToInstanceMap(Map<Class<? extends B>, B> delegate) {
/*  78 */     this.delegate = (Map<Class<? extends B>, B>)Preconditions.checkNotNull(delegate);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<Class<? extends B>, B> delegate() {
/*  83 */     return this.delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <B> Map.Entry<Class<? extends B>, B> checkedEntry(final Map.Entry<Class<? extends B>, B> entry) {
/*  91 */     return new ForwardingMapEntry<Class<? extends B>, B>()
/*     */       {
/*     */         protected Map.Entry<Class<? extends B>, B> delegate() {
/*  94 */           return entry;
/*     */         }
/*     */ 
/*     */         
/*     */         @ParametricNullness
/*     */         public B setValue(@ParametricNullness B value) {
/* 100 */           MutableClassToInstanceMap.cast((Class)getKey(), value);
/* 101 */           return super.setValue(value);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<Class<? extends B>, B>> entrySet() {
/* 108 */     return new ForwardingSet<Map.Entry<Class<? extends B>, B>>()
/*     */       {
/*     */         protected Set<Map.Entry<Class<? extends B>, B>> delegate()
/*     */         {
/* 112 */           return MutableClassToInstanceMap.this.delegate().entrySet();
/*     */         }
/*     */ 
/*     */         
/*     */         public Spliterator<Map.Entry<Class<? extends B>, B>> spliterator() {
/* 117 */           return CollectSpliterators.map(
/* 118 */               delegate().spliterator(), x$0 -> MutableClassToInstanceMap.checkedEntry(x$0));
/*     */         }
/*     */ 
/*     */         
/*     */         public Iterator<Map.Entry<Class<? extends B>, B>> iterator() {
/* 123 */           return new TransformedIterator<Map.Entry<Class<? extends B>, B>, Map.Entry<Class<? extends B>, B>>(this, 
/*     */               
/* 125 */               delegate().iterator())
/*     */             {
/*     */               Map.Entry<Class<? extends B>, B> transform(Map.Entry<Class<? extends B>, B> from)
/*     */               {
/* 129 */                 return MutableClassToInstanceMap.checkedEntry(from);
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public Object[] toArray() {
/* 143 */           Object[] result = standardToArray();
/* 144 */           return result;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public <T> T[] toArray(T[] array) {
/* 150 */           return (T[])standardToArray((Object[])array);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public B put(Class<? extends B> key, @ParametricNullness B value) {
/* 159 */     cast(key, value);
/* 160 */     return super.put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends Class<? extends B>, ? extends B> map) {
/* 165 */     Map<Class<? extends B>, B> copy = new LinkedHashMap<>(map);
/* 166 */     for (Map.Entry<? extends Class<? extends B>, B> entry : copy.entrySet()) {
/* 167 */       cast(entry.getKey(), entry.getValue());
/*     */     }
/* 169 */     super.putAll(copy);
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   public <T extends B> T putInstance(Class<T> type, @ParametricNullness T value) {
/* 176 */     return cast(type, put(type, (B)value));
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   public <T extends B> T getInstance(Class<T> type) {
/* 182 */     return cast(type, get(type));
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   @CanIgnoreReturnValue
/*     */   private static <T> T cast(Class<T> type, @CheckForNull Object value) {
/* 188 */     return Primitives.wrap(type).cast(value);
/*     */   }
/*     */   
/*     */   private Object writeReplace() {
/* 192 */     return new SerializedForm<>(delegate());
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 196 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */   
/*     */   private static final class SerializedForm<B> implements Serializable {
/*     */     private final Map<Class<? extends B>, B> backingMap;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(Map<Class<? extends B>, B> backingMap) {
/* 204 */       this.backingMap = backingMap;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 208 */       return MutableClassToInstanceMap.create(this.backingMap);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/MutableClassToInstanceMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */