/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class TypeResolver
/*     */ {
/*     */   private final TypeTable typeTable;
/*     */   
/*     */   public TypeResolver() {
/*  59 */     this.typeTable = new TypeTable();
/*     */   }
/*     */   
/*     */   private TypeResolver(TypeTable typeTable) {
/*  63 */     this.typeTable = typeTable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeResolver covariantly(Type contextType) {
/*  74 */     return (new TypeResolver()).where((Map<TypeVariableKey, ? extends Type>)TypeMappingIntrospector.getTypeMappings(contextType));
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
/*     */   static TypeResolver invariantly(Type contextType) {
/*  89 */     Type invariantContext = WildcardCapturer.INSTANCE.capture(contextType);
/*  90 */     return (new TypeResolver()).where((Map<TypeVariableKey, ? extends Type>)TypeMappingIntrospector.getTypeMappings(invariantContext));
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
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeResolver where(Type formal, Type actual) {
/* 113 */     Map<TypeVariableKey, Type> mappings = Maps.newHashMap();
/* 114 */     populateTypeMappings(mappings, (Type)Preconditions.checkNotNull(formal), (Type)Preconditions.checkNotNull(actual));
/* 115 */     return where(mappings);
/*     */   }
/*     */ 
/*     */   
/*     */   TypeResolver where(Map<TypeVariableKey, ? extends Type> mappings) {
/* 120 */     return new TypeResolver(this.typeTable.where(mappings));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void populateTypeMappings(final Map<TypeVariableKey, Type> mappings, Type from, final Type to) {
/* 125 */     if (from.equals(to)) {
/*     */       return;
/*     */     }
/* 128 */     (new TypeVisitor()
/*     */       {
/*     */         void visitTypeVariable(TypeVariable<?> typeVariable) {
/* 131 */           mappings.put(new TypeResolver.TypeVariableKey(typeVariable), to);
/*     */         }
/*     */ 
/*     */         
/*     */         void visitWildcardType(WildcardType fromWildcardType) {
/* 136 */           if (!(to instanceof WildcardType)) {
/*     */             return;
/*     */           }
/* 139 */           WildcardType toWildcardType = (WildcardType)to;
/* 140 */           Type[] fromUpperBounds = fromWildcardType.getUpperBounds();
/* 141 */           Type[] toUpperBounds = toWildcardType.getUpperBounds();
/* 142 */           Type[] fromLowerBounds = fromWildcardType.getLowerBounds();
/* 143 */           Type[] toLowerBounds = toWildcardType.getLowerBounds();
/* 144 */           Preconditions.checkArgument((fromUpperBounds.length == toUpperBounds.length && fromLowerBounds.length == toLowerBounds.length), "Incompatible type: %s vs. %s", fromWildcardType, to);
/*     */ 
/*     */           
/*     */           int i;
/*     */ 
/*     */           
/* 150 */           for (i = 0; i < fromUpperBounds.length; i++) {
/* 151 */             TypeResolver.populateTypeMappings(mappings, fromUpperBounds[i], toUpperBounds[i]);
/*     */           }
/* 153 */           for (i = 0; i < fromLowerBounds.length; i++) {
/* 154 */             TypeResolver.populateTypeMappings(mappings, fromLowerBounds[i], toLowerBounds[i]);
/*     */           }
/*     */         }
/*     */ 
/*     */         
/*     */         void visitParameterizedType(ParameterizedType fromParameterizedType) {
/* 160 */           if (to instanceof WildcardType) {
/*     */             return;
/*     */           }
/* 163 */           ParameterizedType toParameterizedType = (ParameterizedType)TypeResolver.expectArgument((Class)ParameterizedType.class, to);
/* 164 */           if (fromParameterizedType.getOwnerType() != null && toParameterizedType
/* 165 */             .getOwnerType() != null) {
/* 166 */             TypeResolver.populateTypeMappings(mappings, fromParameterizedType
/* 167 */                 .getOwnerType(), toParameterizedType.getOwnerType());
/*     */           }
/* 169 */           Preconditions.checkArgument(fromParameterizedType
/* 170 */               .getRawType().equals(toParameterizedType.getRawType()), "Inconsistent raw type: %s vs. %s", fromParameterizedType, to);
/*     */ 
/*     */ 
/*     */           
/* 174 */           Type[] fromArgs = fromParameterizedType.getActualTypeArguments();
/* 175 */           Type[] toArgs = toParameterizedType.getActualTypeArguments();
/* 176 */           Preconditions.checkArgument((fromArgs.length == toArgs.length), "%s not compatible with %s", fromParameterizedType, toParameterizedType);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 181 */           for (int i = 0; i < fromArgs.length; i++) {
/* 182 */             TypeResolver.populateTypeMappings(mappings, fromArgs[i], toArgs[i]);
/*     */           }
/*     */         }
/*     */ 
/*     */         
/*     */         void visitGenericArrayType(GenericArrayType fromArrayType) {
/* 188 */           if (to instanceof WildcardType) {
/*     */             return;
/*     */           }
/* 191 */           Type componentType = Types.getComponentType(to);
/* 192 */           Preconditions.checkArgument((componentType != null), "%s is not an array type.", to);
/* 193 */           TypeResolver.populateTypeMappings(mappings, fromArrayType.getGenericComponentType(), componentType);
/*     */         }
/*     */ 
/*     */         
/*     */         void visitClass(Class<?> fromClass) {
/* 198 */           if (to instanceof WildcardType) {
/*     */             return;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 204 */           throw new IllegalArgumentException("No type mapping from " + fromClass + " to " + to);
/*     */         }
/* 206 */       }).visit(new Type[] { from });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type resolveType(Type type) {
/* 214 */     Preconditions.checkNotNull(type);
/* 215 */     if (type instanceof TypeVariable)
/* 216 */       return this.typeTable.resolve((TypeVariable)type); 
/* 217 */     if (type instanceof ParameterizedType)
/* 218 */       return resolveParameterizedType((ParameterizedType)type); 
/* 219 */     if (type instanceof GenericArrayType)
/* 220 */       return resolveGenericArrayType((GenericArrayType)type); 
/* 221 */     if (type instanceof WildcardType) {
/* 222 */       return resolveWildcardType((WildcardType)type);
/*     */     }
/*     */     
/* 225 */     return type;
/*     */   }
/*     */ 
/*     */   
/*     */   Type[] resolveTypesInPlace(Type[] types) {
/* 230 */     for (int i = 0; i < types.length; i++) {
/* 231 */       types[i] = resolveType(types[i]);
/*     */     }
/* 233 */     return types;
/*     */   }
/*     */   
/*     */   private Type[] resolveTypes(Type[] types) {
/* 237 */     Type[] result = new Type[types.length];
/* 238 */     for (int i = 0; i < types.length; i++) {
/* 239 */       result[i] = resolveType(types[i]);
/*     */     }
/* 241 */     return result;
/*     */   }
/*     */   
/*     */   private WildcardType resolveWildcardType(WildcardType type) {
/* 245 */     Type[] lowerBounds = type.getLowerBounds();
/* 246 */     Type[] upperBounds = type.getUpperBounds();
/* 247 */     return new Types.WildcardTypeImpl(resolveTypes(lowerBounds), resolveTypes(upperBounds));
/*     */   }
/*     */   
/*     */   private Type resolveGenericArrayType(GenericArrayType type) {
/* 251 */     Type componentType = type.getGenericComponentType();
/* 252 */     Type resolvedComponentType = resolveType(componentType);
/* 253 */     return Types.newArrayType(resolvedComponentType);
/*     */   }
/*     */   
/*     */   private ParameterizedType resolveParameterizedType(ParameterizedType type) {
/* 257 */     Type owner = type.getOwnerType();
/* 258 */     Type resolvedOwner = (owner == null) ? null : resolveType(owner);
/* 259 */     Type resolvedRawType = resolveType(type.getRawType());
/*     */     
/* 261 */     Type[] args = type.getActualTypeArguments();
/* 262 */     Type[] resolvedArgs = resolveTypes(args);
/* 263 */     return Types.newParameterizedTypeWithOwner(resolvedOwner, (Class)resolvedRawType, resolvedArgs);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> T expectArgument(Class<T> type, Object arg) {
/*     */     try {
/* 269 */       return type.cast(arg);
/* 270 */     } catch (ClassCastException e) {
/* 271 */       throw new IllegalArgumentException(arg + " is not a " + type.getSimpleName());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class TypeTable
/*     */   {
/*     */     private final ImmutableMap<TypeResolver.TypeVariableKey, Type> map;
/*     */     
/*     */     TypeTable() {
/* 280 */       this.map = ImmutableMap.of();
/*     */     }
/*     */     
/*     */     private TypeTable(ImmutableMap<TypeResolver.TypeVariableKey, Type> map) {
/* 284 */       this.map = map;
/*     */     }
/*     */ 
/*     */     
/*     */     final TypeTable where(Map<TypeResolver.TypeVariableKey, ? extends Type> mappings) {
/* 289 */       ImmutableMap.Builder<TypeResolver.TypeVariableKey, Type> builder = ImmutableMap.builder();
/* 290 */       builder.putAll((Map)this.map);
/* 291 */       for (Map.Entry<TypeResolver.TypeVariableKey, ? extends Type> mapping : mappings.entrySet()) {
/* 292 */         TypeResolver.TypeVariableKey variable = mapping.getKey();
/* 293 */         Type type = mapping.getValue();
/* 294 */         Preconditions.checkArgument(!variable.equalsType(type), "Type variable %s bound to itself", variable);
/* 295 */         builder.put(variable, type);
/*     */       } 
/* 297 */       return new TypeTable(builder.buildOrThrow());
/*     */     }
/*     */     
/*     */     final Type resolve(final TypeVariable<?> var) {
/* 301 */       final TypeTable unguarded = this;
/* 302 */       TypeTable guarded = new TypeTable(this)
/*     */         {
/*     */           public Type resolveInternal(TypeVariable<?> intermediateVar, TypeResolver.TypeTable forDependent)
/*     */           {
/* 306 */             if (intermediateVar.getGenericDeclaration().equals(var.getGenericDeclaration())) {
/* 307 */               return intermediateVar;
/*     */             }
/* 309 */             return unguarded.resolveInternal(intermediateVar, forDependent);
/*     */           }
/*     */         };
/* 312 */       return resolveInternal(var, guarded);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Type resolveInternal(TypeVariable<?> var, TypeTable forDependants) {
/* 324 */       Type type = (Type)this.map.get(new TypeResolver.TypeVariableKey(var));
/* 325 */       if (type == null) {
/* 326 */         Type[] bounds = var.getBounds();
/* 327 */         if (bounds.length == 0) {
/* 328 */           return var;
/*     */         }
/* 330 */         Type[] resolvedBounds = (new TypeResolver(forDependants)).resolveTypes(bounds);
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
/* 359 */         if (Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY && 
/* 360 */           Arrays.equals((Object[])bounds, (Object[])resolvedBounds)) {
/* 361 */           return var;
/*     */         }
/* 363 */         return Types.newArtificialTypeVariable((GenericDeclaration)var
/* 364 */             .getGenericDeclaration(), var.getName(), resolvedBounds);
/*     */       } 
/*     */       
/* 367 */       return (new TypeResolver(forDependants)).resolveType(type);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TypeMappingIntrospector
/*     */     extends TypeVisitor {
/* 373 */     private final Map<TypeResolver.TypeVariableKey, Type> mappings = Maps.newHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static ImmutableMap<TypeResolver.TypeVariableKey, Type> getTypeMappings(Type contextType) {
/* 380 */       Preconditions.checkNotNull(contextType);
/* 381 */       TypeMappingIntrospector introspector = new TypeMappingIntrospector();
/* 382 */       introspector.visit(new Type[] { contextType });
/* 383 */       return ImmutableMap.copyOf(introspector.mappings);
/*     */     }
/*     */ 
/*     */     
/*     */     void visitClass(Class<?> clazz) {
/* 388 */       visit(new Type[] { clazz.getGenericSuperclass() });
/* 389 */       visit(clazz.getGenericInterfaces());
/*     */     }
/*     */ 
/*     */     
/*     */     void visitParameterizedType(ParameterizedType parameterizedType) {
/* 394 */       Class<?> rawClass = (Class)parameterizedType.getRawType();
/* 395 */       TypeVariable[] arrayOfTypeVariable = (TypeVariable[])rawClass.getTypeParameters();
/* 396 */       Type[] typeArgs = parameterizedType.getActualTypeArguments();
/* 397 */       Preconditions.checkState((arrayOfTypeVariable.length == typeArgs.length));
/* 398 */       for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/* 399 */         map(new TypeResolver.TypeVariableKey(arrayOfTypeVariable[i]), typeArgs[i]);
/*     */       }
/* 401 */       visit(new Type[] { rawClass });
/* 402 */       visit(new Type[] { parameterizedType.getOwnerType() });
/*     */     }
/*     */ 
/*     */     
/*     */     void visitTypeVariable(TypeVariable<?> t) {
/* 407 */       visit(t.getBounds());
/*     */     }
/*     */ 
/*     */     
/*     */     void visitWildcardType(WildcardType t) {
/* 412 */       visit(t.getUpperBounds());
/*     */     }
/*     */     
/*     */     private void map(TypeResolver.TypeVariableKey var, Type arg) {
/* 416 */       if (this.mappings.containsKey(var)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 425 */       for (Type t = arg; t != null; t = this.mappings.get(TypeResolver.TypeVariableKey.forLookup(t))) {
/* 426 */         if (var.equalsType(t)) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 431 */           for (Type x = arg; x != null; x = this.mappings.remove(TypeResolver.TypeVariableKey.forLookup(x)));
/*     */           return;
/*     */         } 
/*     */       } 
/* 435 */       this.mappings.put(var, arg);
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
/*     */   private static class WildcardCapturer
/*     */   {
/* 448 */     static final WildcardCapturer INSTANCE = new WildcardCapturer();
/*     */     
/*     */     private final AtomicInteger id;
/*     */     
/*     */     private WildcardCapturer() {
/* 453 */       this(new AtomicInteger());
/*     */     }
/*     */     
/*     */     private WildcardCapturer(AtomicInteger id) {
/* 457 */       this.id = id;
/*     */     }
/*     */     
/*     */     final Type capture(Type type) {
/* 461 */       Preconditions.checkNotNull(type);
/* 462 */       if (type instanceof Class) {
/* 463 */         return type;
/*     */       }
/* 465 */       if (type instanceof TypeVariable) {
/* 466 */         return type;
/*     */       }
/* 468 */       if (type instanceof GenericArrayType) {
/* 469 */         GenericArrayType arrayType = (GenericArrayType)type;
/* 470 */         return Types.newArrayType(
/* 471 */             notForTypeVariable().capture(arrayType.getGenericComponentType()));
/*     */       } 
/* 473 */       if (type instanceof ParameterizedType) {
/* 474 */         ParameterizedType parameterizedType = (ParameterizedType)type;
/* 475 */         Class<?> rawType = (Class)parameterizedType.getRawType();
/* 476 */         TypeVariable[] arrayOfTypeVariable = (TypeVariable[])rawType.getTypeParameters();
/* 477 */         Type[] typeArgs = parameterizedType.getActualTypeArguments();
/* 478 */         for (int i = 0; i < typeArgs.length; i++) {
/* 479 */           typeArgs[i] = forTypeVariable(arrayOfTypeVariable[i]).capture(typeArgs[i]);
/*     */         }
/* 481 */         return Types.newParameterizedTypeWithOwner(
/* 482 */             notForTypeVariable().captureNullable(parameterizedType.getOwnerType()), rawType, typeArgs);
/*     */       } 
/*     */ 
/*     */       
/* 486 */       if (type instanceof WildcardType) {
/* 487 */         WildcardType wildcardType = (WildcardType)type;
/* 488 */         Type[] lowerBounds = wildcardType.getLowerBounds();
/* 489 */         if (lowerBounds.length == 0) {
/* 490 */           return captureAsTypeVariable(wildcardType.getUpperBounds());
/*     */         }
/*     */         
/* 493 */         return type;
/*     */       } 
/*     */       
/* 496 */       throw new AssertionError("must have been one of the known types");
/*     */     }
/*     */ 
/*     */     
/*     */     TypeVariable<?> captureAsTypeVariable(Type[] upperBounds) {
/* 501 */       String name = "capture#" + this.id.incrementAndGet() + "-of ? extends " + Joiner.on('&').join((Object[])upperBounds);
/* 502 */       return Types.newArtificialTypeVariable(WildcardCapturer.class, name, upperBounds);
/*     */     }
/*     */     
/*     */     private WildcardCapturer forTypeVariable(final TypeVariable<?> typeParam) {
/* 506 */       return new WildcardCapturer(this, this.id)
/*     */         {
/*     */           TypeVariable<?> captureAsTypeVariable(Type[] upperBounds) {
/* 509 */             Set<Type> combined = new LinkedHashSet<>(Arrays.asList(upperBounds));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 516 */             combined.addAll(Arrays.asList(typeParam.getBounds()));
/* 517 */             if (combined.size() > 1) {
/* 518 */               combined.remove(Object.class);
/*     */             }
/* 520 */             return super.captureAsTypeVariable(combined.<Type>toArray(new Type[0]));
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     private WildcardCapturer notForTypeVariable() {
/* 526 */       return new WildcardCapturer(this.id);
/*     */     }
/*     */     
/*     */     @CheckForNull
/*     */     private Type captureNullable(@CheckForNull Type type) {
/* 531 */       if (type == null) {
/* 532 */         return null;
/*     */       }
/* 534 */       return capture(type);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class TypeVariableKey
/*     */   {
/*     */     private final TypeVariable<?> var;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     TypeVariableKey(TypeVariable<?> var) {
/* 555 */       this.var = (TypeVariable)Preconditions.checkNotNull(var);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 560 */       return Objects.hashCode(new Object[] { this.var.getGenericDeclaration(), this.var.getName() });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 565 */       if (obj instanceof TypeVariableKey) {
/* 566 */         TypeVariableKey that = (TypeVariableKey)obj;
/* 567 */         return equalsTypeVariable(that.var);
/*     */       } 
/* 569 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 575 */       return this.var.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     static TypeVariableKey forLookup(Type t) {
/* 581 */       if (t instanceof TypeVariable) {
/* 582 */         return new TypeVariableKey((TypeVariable)t);
/*     */       }
/* 584 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean equalsType(Type type) {
/* 593 */       if (type instanceof TypeVariable) {
/* 594 */         return equalsTypeVariable((TypeVariable)type);
/*     */       }
/* 596 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean equalsTypeVariable(TypeVariable<?> that) {
/* 601 */       return (this.var.getGenericDeclaration().equals(that.getGenericDeclaration()) && this.var
/* 602 */         .getName().equals(that.getName()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/reflect/TypeResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */