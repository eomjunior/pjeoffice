/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import com.github.utils4j.imp.function.Predicates;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Containers
/*     */ {
/*     */   public static boolean isEmpty(Collection<?> c) {
/*  52 */     return (c == null || c.size() == 0);
/*     */   }
/*     */   
/*     */   public static <T, C extends Collection<T>> void ifNotEmpty(C c, Consumer<C> consumer) {
/*  56 */     if (!isEmpty((Collection<?>)c)) consumer.accept(c); 
/*     */   }
/*     */   
/*     */   public static boolean isEmpty(Map<?, ?> m) {
/*  60 */     return (m == null || m.isEmpty());
/*     */   }
/*     */   
/*     */   public static boolean isEmpty(Set<?> set) {
/*  64 */     return (set == null || set.isEmpty());
/*     */   }
/*     */   
/*     */   public static <T> List<T> toList(Set<T> set) {
/*  68 */     return isEmpty(set) ? Collections.<T>emptyList() : new ArrayList<>(set);
/*     */   }
/*     */   
/*     */   public static <T> List<T> toUnmodifiableList(Set<T> set) {
/*  72 */     return isEmpty(set) ? Collections.<T>emptyList() : Collections.<T>unmodifiableList(new ArrayList<>(set));
/*     */   }
/*     */   
/*     */   public static String firstText(Collection<String> values) {
/*  76 */     if (values == null || values.isEmpty())
/*  77 */       return ""; 
/*  78 */     return Strings.text(values.iterator().next());
/*     */   }
/*     */   
/*     */   public static boolean isEmpty(Enumeration<?> e) {
/*  82 */     return (e == null || !e.hasMoreElements());
/*     */   }
/*     */   
/*     */   public static <T> boolean isEmpty(T[] value) {
/*  86 */     return (value == null || value.length == 0);
/*     */   }
/*     */   
/*     */   public static boolean isEmpty(byte[] value) {
/*  90 */     return (value == null || value.length == 0);
/*     */   }
/*     */   
/*     */   public static boolean isEmpty(int[] value) {
/*  94 */     return (value == null || value.length == 0);
/*     */   }
/*     */   
/*     */   public static boolean isEmpty(float[] value) {
/*  98 */     return (value == null || value.length == 0);
/*     */   }
/*     */   
/*     */   public static boolean isEmpty(double[] value) {
/* 102 */     return (value == null || value.length == 0);
/*     */   }
/*     */   
/*     */   public static boolean isEmpty(long[] value) {
/* 106 */     return (value == null || value.length == 0);
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public static <T> List<T> arrayList(T... array) {
/* 111 */     return isEmpty(array) ? new ArrayList<>() : arrayList(Predicates.all(), array);
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public static <T> List<T> arrayList(Predicate<T> filter, T... array) {
/* 116 */     return (List<T>)Arrays.<T>asList(array).stream().filter(filter).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public static String[] arrayOf(Collection<String> container) {
/* 120 */     if (container == null)
/* 121 */       return Strings.emptyArray(); 
/* 122 */     return container.<String>toArray(new String[container.size()]);
/*     */   }
/*     */   
/*     */   public static <T> List<T> nonNull(List<T> list) {
/* 126 */     return (list == null) ? Collections.<T>emptyList() : list;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Containers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */