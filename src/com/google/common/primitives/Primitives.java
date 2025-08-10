/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Primitives
/*     */ {
/*     */   private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPE;
/*     */   private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPE;
/*     */   
/*     */   static {
/*  46 */     Map<Class<?>, Class<?>> primToWrap = new LinkedHashMap<>(16);
/*  47 */     Map<Class<?>, Class<?>> wrapToPrim = new LinkedHashMap<>(16);
/*     */     
/*  49 */     add(primToWrap, wrapToPrim, boolean.class, Boolean.class);
/*  50 */     add(primToWrap, wrapToPrim, byte.class, Byte.class);
/*  51 */     add(primToWrap, wrapToPrim, char.class, Character.class);
/*  52 */     add(primToWrap, wrapToPrim, double.class, Double.class);
/*  53 */     add(primToWrap, wrapToPrim, float.class, Float.class);
/*  54 */     add(primToWrap, wrapToPrim, int.class, Integer.class);
/*  55 */     add(primToWrap, wrapToPrim, long.class, Long.class);
/*  56 */     add(primToWrap, wrapToPrim, short.class, Short.class);
/*  57 */     add(primToWrap, wrapToPrim, void.class, Void.class);
/*     */     
/*  59 */     PRIMITIVE_TO_WRAPPER_TYPE = Collections.unmodifiableMap(primToWrap);
/*  60 */     WRAPPER_TO_PRIMITIVE_TYPE = Collections.unmodifiableMap(wrapToPrim);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void add(Map<Class<?>, Class<?>> forward, Map<Class<?>, Class<?>> backward, Class<?> key, Class<?> value) {
/*  68 */     forward.put(key, value);
/*  69 */     backward.put(value, key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<Class<?>> allPrimitiveTypes() {
/*  80 */     return PRIMITIVE_TO_WRAPPER_TYPE.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<Class<?>> allWrapperTypes() {
/*  89 */     return WRAPPER_TO_PRIMITIVE_TYPE.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isWrapperType(Class<?> type) {
/*  99 */     return WRAPPER_TO_PRIMITIVE_TYPE.containsKey(Preconditions.checkNotNull(type));
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
/*     */   public static <T> Class<T> wrap(Class<T> type) {
/* 113 */     Preconditions.checkNotNull(type);
/*     */ 
/*     */ 
/*     */     
/* 117 */     Class<T> wrapped = (Class<T>)PRIMITIVE_TO_WRAPPER_TYPE.get(type);
/* 118 */     return (wrapped == null) ? type : wrapped;
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
/*     */   public static <T> Class<T> unwrap(Class<T> type) {
/* 132 */     Preconditions.checkNotNull(type);
/*     */ 
/*     */ 
/*     */     
/* 136 */     Class<T> unwrapped = (Class<T>)WRAPPER_TO_PRIMITIVE_TYPE.get(type);
/* 137 */     return (unwrapped == null) ? type : unwrapped;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/primitives/Primitives.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */