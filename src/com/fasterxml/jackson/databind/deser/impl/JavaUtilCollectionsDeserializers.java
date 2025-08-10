/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ public abstract class JavaUtilCollectionsDeserializers
/*     */ {
/*     */   private static final int TYPE_SINGLETON_SET = 1;
/*     */   private static final int TYPE_SINGLETON_LIST = 2;
/*     */   private static final int TYPE_SINGLETON_MAP = 3;
/*     */   private static final int TYPE_UNMODIFIABLE_SET = 4;
/*     */   private static final int TYPE_UNMODIFIABLE_LIST = 5;
/*     */   private static final int TYPE_UNMODIFIABLE_MAP = 6;
/*     */   private static final int TYPE_SYNC_SET = 7;
/*     */   private static final int TYPE_SYNC_COLLECTION = 8;
/*     */   private static final int TYPE_SYNC_LIST = 9;
/*     */   private static final int TYPE_SYNC_MAP = 10;
/*     */   public static final int TYPE_AS_LIST = 11;
/*     */   private static final String PREFIX_JAVA_UTIL_COLLECTIONS = "java.util.Collections$";
/*     */   private static final String PREFIX_JAVA_UTIL_ARRAYS = "java.util.Arrays$";
/*     */   private static final String PREFIX_JAVA_UTIL_IMMUTABLE_COLL = "java.util.ImmutableCollections$";
/*     */   
/*     */   public static JsonDeserializer<?> findForCollection(DeserializationContext ctxt, JavaType type) throws JsonMappingException {
/*  45 */     String clsName = type.getRawClass().getName();
/*  46 */     if (!clsName.startsWith("java.util.")) {
/*  47 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  51 */     String localName = _findUtilCollectionsTypeName(clsName);
/*  52 */     if (localName != null) {
/*  53 */       JavaUtilCollectionsConverter conv = null;
/*     */       
/*     */       String name;
/*  56 */       if ((name = _findUnmodifiableTypeName(localName)) != null) {
/*  57 */         if (name.endsWith("Set")) {
/*  58 */           conv = converter(4, type, Set.class);
/*  59 */         } else if (name.endsWith("List")) {
/*  60 */           conv = converter(5, type, List.class);
/*     */         } 
/*  62 */       } else if ((name = _findSingletonTypeName(localName)) != null) {
/*  63 */         if (name.endsWith("Set")) {
/*  64 */           conv = converter(1, type, Set.class);
/*  65 */         } else if (name.endsWith("List")) {
/*  66 */           conv = converter(2, type, List.class);
/*     */         } 
/*  68 */       } else if ((name = _findSyncTypeName(localName)) != null) {
/*     */         
/*  70 */         if (name.endsWith("Set")) {
/*  71 */           conv = converter(7, type, Set.class);
/*  72 */         } else if (name.endsWith("List")) {
/*  73 */           conv = converter(9, type, List.class);
/*  74 */         } else if (name.endsWith("Collection")) {
/*  75 */           conv = converter(8, type, Collection.class);
/*     */         } 
/*     */       } 
/*     */       
/*  79 */       return (conv == null) ? null : (JsonDeserializer<?>)new StdDelegatingDeserializer(conv);
/*     */     } 
/*  81 */     if ((localName = _findUtilArrayTypeName(clsName)) != null) {
/*     */       
/*  83 */       if (localName.contains("List"))
/*     */       {
/*     */ 
/*     */         
/*  87 */         return (JsonDeserializer<?>)new StdDelegatingDeserializer(
/*  88 */             converter(11, type, List.class));
/*     */       }
/*  90 */       return null;
/*     */     } 
/*     */     
/*  93 */     if ((localName = _findUtilCollectionsImmutableTypeName(clsName)) != null) {
/*     */       
/*  95 */       if (localName.contains("List")) {
/*  96 */         return (JsonDeserializer<?>)new StdDelegatingDeserializer(
/*  97 */             converter(11, type, List.class));
/*     */       }
/*  99 */       if (localName.contains("Set")) {
/* 100 */         return (JsonDeserializer<?>)new StdDelegatingDeserializer(
/* 101 */             converter(4, type, Set.class));
/*     */       }
/* 103 */       return null;
/*     */     } 
/*     */     
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonDeserializer<?> findForMap(DeserializationContext ctxt, JavaType type) throws JsonMappingException {
/* 113 */     String clsName = type.getRawClass().getName();
/*     */     
/* 115 */     JavaUtilCollectionsConverter conv = null;
/*     */     String localName;
/* 117 */     if ((localName = _findUtilCollectionsTypeName(clsName)) != null) {
/*     */       String name;
/*     */       
/* 120 */       if ((name = _findUnmodifiableTypeName(localName)) != null) {
/* 121 */         if (name.contains("Map")) {
/* 122 */           conv = converter(6, type, Map.class);
/*     */         }
/* 124 */       } else if ((name = _findSingletonTypeName(localName)) != null) {
/* 125 */         if (name.contains("Map")) {
/* 126 */           conv = converter(3, type, Map.class);
/*     */         }
/* 128 */       } else if ((name = _findSyncTypeName(localName)) != null) {
/*     */         
/* 130 */         if (name.contains("Map")) {
/* 131 */           conv = converter(10, type, Map.class);
/*     */         }
/*     */       } 
/* 134 */     } else if ((localName = _findUtilCollectionsImmutableTypeName(clsName)) != null && 
/* 135 */       localName.contains("Map")) {
/* 136 */       conv = converter(6, type, Map.class);
/*     */     } 
/*     */     
/* 139 */     return (conv == null) ? null : (JsonDeserializer<?>)new StdDelegatingDeserializer(conv);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static JavaUtilCollectionsConverter converter(int kind, JavaType concreteType, Class<?> rawSuper) {
/* 145 */     return new JavaUtilCollectionsConverter(kind, concreteType.findSuperType(rawSuper));
/*     */   }
/*     */   
/*     */   private static String _findUtilArrayTypeName(String clsName) {
/* 149 */     if (clsName.startsWith("java.util.Arrays$")) {
/* 150 */       return clsName.substring("java.util.Arrays$".length());
/*     */     }
/* 152 */     return null;
/*     */   }
/*     */   
/*     */   private static String _findUtilCollectionsTypeName(String clsName) {
/* 156 */     if (clsName.startsWith("java.util.Collections$")) {
/* 157 */       return clsName.substring("java.util.Collections$".length());
/*     */     }
/* 159 */     return null;
/*     */   }
/*     */   
/*     */   private static String _findUtilCollectionsImmutableTypeName(String clsName) {
/* 163 */     if (clsName.startsWith("java.util.ImmutableCollections$")) {
/* 164 */       return clsName.substring("java.util.ImmutableCollections$".length());
/*     */     }
/* 166 */     return null;
/*     */   }
/*     */   
/*     */   private static String _findSingletonTypeName(String localName) {
/* 170 */     return localName.startsWith("Singleton") ? localName.substring(9) : null;
/*     */   }
/*     */   
/*     */   private static String _findSyncTypeName(String localName) {
/* 174 */     return localName.startsWith("Synchronized") ? localName.substring(12) : null;
/*     */   }
/*     */   
/*     */   private static String _findUnmodifiableTypeName(String localName) {
/* 178 */     return localName.startsWith("Unmodifiable") ? localName.substring(12) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class JavaUtilCollectionsConverter
/*     */     implements Converter<Object, Object>
/*     */   {
/*     */     private final JavaType _inputType;
/*     */ 
/*     */     
/*     */     private final int _kind;
/*     */ 
/*     */     
/*     */     JavaUtilCollectionsConverter(int kind, JavaType inputType) {
/* 193 */       this._inputType = inputType;
/* 194 */       this._kind = kind; } public Object convert(Object value) {
/*     */       Set<?> set;
/*     */       List<?> list;
/*     */       Map<?, ?> map;
/*     */       Map.Entry<?, ?> entry;
/* 199 */       if (value == null) {
/* 200 */         return null;
/*     */       }
/*     */       
/* 203 */       switch (this._kind) {
/*     */         
/*     */         case 1:
/* 206 */           set = (Set)value;
/* 207 */           _checkSingleton(set.size());
/* 208 */           return Collections.singleton(set.iterator().next());
/*     */ 
/*     */         
/*     */         case 2:
/* 212 */           list = (List)value;
/* 213 */           _checkSingleton(list.size());
/* 214 */           return Collections.singletonList(list.get(0));
/*     */ 
/*     */         
/*     */         case 3:
/* 218 */           map = (Map<?, ?>)value;
/* 219 */           _checkSingleton(map.size());
/* 220 */           entry = map.entrySet().iterator().next();
/* 221 */           return Collections.singletonMap(entry.getKey(), entry.getValue());
/*     */ 
/*     */         
/*     */         case 4:
/* 225 */           return Collections.unmodifiableSet((Set)value);
/*     */         case 5:
/* 227 */           return Collections.unmodifiableList((List)value);
/*     */         case 6:
/* 229 */           return Collections.unmodifiableMap((Map<?, ?>)value);
/*     */         
/*     */         case 7:
/* 232 */           return Collections.synchronizedSet((Set)value);
/*     */         case 9:
/* 234 */           return Collections.synchronizedList((List)value);
/*     */         case 8:
/* 236 */           return Collections.synchronizedCollection((Collection)value);
/*     */         case 10:
/* 238 */           return Collections.synchronizedMap((Map<?, ?>)value);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 243 */       return value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JavaType getInputType(TypeFactory typeFactory) {
/* 249 */       return this._inputType;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JavaType getOutputType(TypeFactory typeFactory) {
/* 255 */       return this._inputType;
/*     */     }
/*     */     
/*     */     private void _checkSingleton(int size) {
/* 259 */       if (size != 1)
/*     */       {
/* 261 */         throw new IllegalArgumentException("Can not deserialize Singleton container from " + size + " entries");
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/deser/impl/JavaUtilCollectionsDeserializers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */