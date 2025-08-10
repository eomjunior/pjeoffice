/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import java.io.Serializable;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
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
/*     */ @GwtIncompatible
/*     */ @J2ktIncompatible
/*     */ public final class Enums
/*     */ {
/*     */   @GwtIncompatible
/*     */   public static Field getField(Enum<?> enumValue) {
/*  53 */     Class<?> clazz = enumValue.getDeclaringClass();
/*     */     try {
/*  55 */       return clazz.getDeclaredField(enumValue.name());
/*  56 */     } catch (NoSuchFieldException impossible) {
/*  57 */       throw new AssertionError(impossible);
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
/*     */   public static <T extends Enum<T>> Optional<T> getIfPresent(Class<T> enumClass, String value) {
/*  70 */     Preconditions.checkNotNull(enumClass);
/*  71 */     Preconditions.checkNotNull(value);
/*  72 */     return Platform.getEnumIfPresent(enumClass, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*  77 */   private static final Map<Class<? extends Enum<?>>, Map<String, WeakReference<? extends Enum<?>>>> enumConstantCache = new WeakHashMap<>();
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> populateCache(Class<T> enumClass) {
/*  82 */     Map<String, WeakReference<? extends Enum<?>>> result = new HashMap<>();
/*  83 */     for (Enum<?> enum_ : EnumSet.<T>allOf(enumClass)) {
/*  84 */       result.put(enum_.name(), new WeakReference<>(enum_));
/*     */     }
/*  86 */     enumConstantCache.put(enumClass, result);
/*  87 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static <T extends Enum<T>> Map<String, WeakReference<? extends Enum<?>>> getEnumConstants(Class<T> enumClass) {
/*  93 */     synchronized (enumConstantCache) {
/*  94 */       Map<String, WeakReference<? extends Enum<?>>> constants = enumConstantCache.get(enumClass);
/*  95 */       if (constants == null) {
/*  96 */         constants = populateCache(enumClass);
/*     */       }
/*  98 */       return constants;
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
/*     */   @GwtIncompatible
/*     */   public static <T extends Enum<T>> Converter<String, T> stringConverter(Class<T> enumClass) {
/* 112 */     return new StringConverter<>(enumClass);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static final class StringConverter<T extends Enum<T>>
/*     */     extends Converter<String, T> implements Serializable {
/*     */     private final Class<T> enumClass;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     StringConverter(Class<T> enumClass) {
/* 122 */       this.enumClass = Preconditions.<Class<T>>checkNotNull(enumClass);
/*     */     }
/*     */ 
/*     */     
/*     */     protected T doForward(String value) {
/* 127 */       return Enum.valueOf(this.enumClass, value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(T enumValue) {
/* 132 */       return enumValue.name();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object object) {
/* 137 */       if (object instanceof StringConverter) {
/* 138 */         StringConverter<?> that = (StringConverter)object;
/* 139 */         return this.enumClass.equals(that.enumClass);
/*     */       } 
/* 141 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 146 */       return this.enumClass.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 151 */       return "Enums.stringConverter(" + this.enumClass.getName() + ".class)";
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/Enums.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */