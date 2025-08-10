/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @GwtIncompatible
/*     */ @J2ktIncompatible
/*     */ final class Serialization
/*     */ {
/*     */   static int readCount(ObjectInputStream stream) throws IOException {
/*  51 */     return stream.readInt();
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
/*     */   static <K, V> void writeMap(Map<K, V> map, ObjectOutputStream stream) throws IOException {
/*  63 */     stream.writeInt(map.size());
/*  64 */     for (Map.Entry<K, V> entry : map.entrySet()) {
/*  65 */       stream.writeObject(entry.getKey());
/*  66 */       stream.writeObject(entry.getValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> void populateMap(Map<K, V> map, ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  76 */     int size = stream.readInt();
/*  77 */     populateMap(map, stream, size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> void populateMap(Map<K, V> map, ObjectInputStream stream, int size) throws IOException, ClassNotFoundException {
/*  87 */     for (int i = 0; i < size; i++) {
/*     */       
/*  89 */       K key = (K)stream.readObject();
/*     */       
/*  91 */       V value = (V)stream.readObject();
/*  92 */       map.put(key, value);
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
/*     */   static <E> void writeMultiset(Multiset<E> multiset, ObjectOutputStream stream) throws IOException {
/* 105 */     int entryCount = multiset.entrySet().size();
/* 106 */     stream.writeInt(entryCount);
/* 107 */     for (Multiset.Entry<E> entry : multiset.entrySet()) {
/* 108 */       stream.writeObject(entry.getElement());
/* 109 */       stream.writeInt(entry.getCount());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> void populateMultiset(Multiset<E> multiset, ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 119 */     int distinctElements = stream.readInt();
/* 120 */     populateMultiset(multiset, stream, distinctElements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> void populateMultiset(Multiset<E> multiset, ObjectInputStream stream, int distinctElements) throws IOException, ClassNotFoundException {
/* 131 */     for (int i = 0; i < distinctElements; i++) {
/*     */       
/* 133 */       E element = (E)stream.readObject();
/* 134 */       int count = stream.readInt();
/* 135 */       multiset.add(element, count);
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
/*     */   static <K, V> void writeMultimap(Multimap<K, V> multimap, ObjectOutputStream stream) throws IOException {
/* 149 */     stream.writeInt(multimap.asMap().size());
/* 150 */     for (Map.Entry<K, Collection<V>> entry : (Iterable<Map.Entry<K, Collection<V>>>)multimap.asMap().entrySet()) {
/* 151 */       stream.writeObject(entry.getKey());
/* 152 */       stream.writeInt(((Collection)entry.getValue()).size());
/* 153 */       for (V value : entry.getValue()) {
/* 154 */         stream.writeObject(value);
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
/*     */   static <K, V> void populateMultimap(Multimap<K, V> multimap, ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 166 */     int distinctKeys = stream.readInt();
/* 167 */     populateMultimap(multimap, stream, distinctKeys);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> void populateMultimap(Multimap<K, V> multimap, ObjectInputStream stream, int distinctKeys) throws IOException, ClassNotFoundException {
/* 178 */     for (int i = 0; i < distinctKeys; i++) {
/*     */       
/* 180 */       K key = (K)stream.readObject();
/* 181 */       Collection<V> values = multimap.get(key);
/* 182 */       int valueCount = stream.readInt();
/* 183 */       for (int j = 0; j < valueCount; j++) {
/*     */         
/* 185 */         V value = (V)stream.readObject();
/* 186 */         values.add(value);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> FieldSetter<T> getFieldSetter(Class<T> clazz, String fieldName) {
/*     */     try {
/* 194 */       Field field = clazz.getDeclaredField(fieldName);
/* 195 */       return new FieldSetter<>(field);
/* 196 */     } catch (NoSuchFieldException e) {
/* 197 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   static final class FieldSetter<T>
/*     */   {
/*     */     private final Field field;
/*     */     
/*     */     private FieldSetter(Field field) {
/* 206 */       this.field = field;
/* 207 */       field.setAccessible(true);
/*     */     }
/*     */     
/*     */     void set(T instance, Object value) {
/*     */       try {
/* 212 */         this.field.set(instance, value);
/* 213 */       } catch (IllegalAccessException impossible) {
/* 214 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */     
/*     */     void set(T instance, int value) {
/*     */       try {
/* 220 */         this.field.set(instance, Integer.valueOf(value));
/* 221 */       } catch (IllegalAccessException impossible) {
/* 222 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/Serialization.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */