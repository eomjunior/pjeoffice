/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Set;
/*     */ import java.util.function.ObjIntConsumer;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public final class LinkedHashMultiset<E>
/*     */   extends AbstractMapBasedMultiset<E>
/*     */ {
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <E> LinkedHashMultiset<E> create() {
/*  49 */     return new LinkedHashMultiset<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> LinkedHashMultiset<E> create(int distinctElements) {
/*  60 */     return new LinkedHashMultiset<>(distinctElements);
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
/*     */   public static <E> LinkedHashMultiset<E> create(Iterable<? extends E> elements) {
/*  72 */     LinkedHashMultiset<E> multiset = create(Multisets.inferDistinctElements(elements));
/*  73 */     Iterables.addAll(multiset, elements);
/*  74 */     return multiset;
/*     */   }
/*     */   
/*     */   private LinkedHashMultiset() {
/*  78 */     super(new LinkedHashMap<>());
/*     */   }
/*     */   
/*     */   private LinkedHashMultiset(int distinctElements) {
/*  82 */     super(Maps.newLinkedHashMapWithExpectedSize(distinctElements));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/*  92 */     stream.defaultWriteObject();
/*  93 */     Serialization.writeMultiset(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   @J2ktIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  99 */     stream.defaultReadObject();
/* 100 */     int distinctElements = Serialization.readCount(stream);
/* 101 */     setBackingMap(new LinkedHashMap<>());
/* 102 */     Serialization.populateMultiset(this, stream, distinctElements);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/LinkedHashMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */