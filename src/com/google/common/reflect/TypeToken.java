/*      */ package com.google.common.reflect;
/*      */ 
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Joiner;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.collect.FluentIterable;
/*      */ import com.google.common.collect.ForwardingSet;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Ordering;
/*      */ import com.google.common.collect.UnmodifiableIterator;
/*      */ import com.google.common.primitives.Primitives;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.GenericArrayType;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Type;
/*      */ import java.lang.reflect.TypeVariable;
/*      */ import java.lang.reflect.WildcardType;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import javax.annotation.CheckForNull;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @ElementTypesAreNonnullByDefault
/*      */ public abstract class TypeToken<T>
/*      */   extends TypeCapture<T>
/*      */   implements Serializable
/*      */ {
/*      */   private final Type runtimeType;
/*      */   @LazyInit
/*      */   @CheckForNull
/*      */   private transient TypeResolver invariantTypeResolver;
/*      */   @LazyInit
/*      */   @CheckForNull
/*      */   private transient TypeResolver covariantTypeResolver;
/*      */   private static final long serialVersionUID = 3637540370352322684L;
/*      */   
/*      */   protected TypeToken() {
/*  125 */     this.runtimeType = capture();
/*  126 */     Preconditions.checkState(!(this.runtimeType instanceof TypeVariable), "Cannot construct a TypeToken for a type variable.\nYou probably meant to call new TypeToken<%s>(getClass()) that can resolve the type variable for you.\nIf you do need to create a TypeToken of a type variable, please use TypeToken.of() instead.", this.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected TypeToken(Class<?> declaringClass) {
/*  156 */     Type captured = capture();
/*  157 */     if (captured instanceof Class) {
/*  158 */       this.runtimeType = captured;
/*      */     } else {
/*  160 */       this.runtimeType = TypeResolver.covariantly(declaringClass).resolveType(captured);
/*      */     } 
/*      */   }
/*      */   
/*      */   private TypeToken(Type type) {
/*  165 */     this.runtimeType = (Type)Preconditions.checkNotNull(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public static <T> TypeToken<T> of(Class<T> type) {
/*  170 */     return new SimpleTypeToken<>(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public static TypeToken<?> of(Type type) {
/*  175 */     return new SimpleTypeToken(type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Class<? super T> getRawType() {
/*  195 */     Class<?> rawType = (Class)getRawTypes().iterator().next();
/*      */     
/*  197 */     Class<? super T> result = (Class)rawType;
/*  198 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Type getType() {
/*  203 */     return this.runtimeType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final <X> TypeToken<T> where(TypeParameter<X> typeParam, TypeToken<X> typeArg) {
/*  238 */     TypeResolver resolver = (new TypeResolver()).where(
/*  239 */         (Map<TypeResolver.TypeVariableKey, ? extends Type>)ImmutableMap.of(new TypeResolver.TypeVariableKey(typeParam.typeVariable), typeArg.runtimeType));
/*      */ 
/*      */     
/*  242 */     return new SimpleTypeToken<>(resolver.resolveType(this.runtimeType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final <X> TypeToken<T> where(TypeParameter<X> typeParam, Class<X> typeArg) {
/*  268 */     return where(typeParam, of(typeArg));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<?> resolveType(Type type) {
/*  281 */     Preconditions.checkNotNull(type);
/*      */ 
/*      */     
/*  284 */     return of(getInvariantTypeResolver().resolveType(type));
/*      */   }
/*      */   
/*      */   private TypeToken<?> resolveSupertype(Type type) {
/*  288 */     TypeToken<?> supertype = of(getCovariantTypeResolver().resolveType(type));
/*      */     
/*  290 */     supertype.covariantTypeResolver = this.covariantTypeResolver;
/*  291 */     supertype.invariantTypeResolver = this.invariantTypeResolver;
/*  292 */     return supertype;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   final TypeToken<? super T> getGenericSuperclass() {
/*  309 */     if (this.runtimeType instanceof TypeVariable)
/*      */     {
/*  311 */       return boundAsSuperclass(((TypeVariable)this.runtimeType).getBounds()[0]);
/*      */     }
/*  313 */     if (this.runtimeType instanceof WildcardType)
/*      */     {
/*  315 */       return boundAsSuperclass(((WildcardType)this.runtimeType).getUpperBounds()[0]);
/*      */     }
/*  317 */     Type superclass = getRawType().getGenericSuperclass();
/*  318 */     if (superclass == null) {
/*  319 */       return null;
/*      */     }
/*      */     
/*  322 */     TypeToken<? super T> superToken = (TypeToken)resolveSupertype(superclass);
/*  323 */     return superToken;
/*      */   }
/*      */   
/*      */   @CheckForNull
/*      */   private TypeToken<? super T> boundAsSuperclass(Type bound) {
/*  328 */     TypeToken<?> token = of(bound);
/*  329 */     if (token.getRawType().isInterface()) {
/*  330 */       return null;
/*      */     }
/*      */     
/*  333 */     TypeToken<? super T> superclass = (TypeToken)token;
/*  334 */     return superclass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final ImmutableList<TypeToken<? super T>> getGenericInterfaces() {
/*  350 */     if (this.runtimeType instanceof TypeVariable) {
/*  351 */       return boundsAsInterfaces(((TypeVariable)this.runtimeType).getBounds());
/*      */     }
/*  353 */     if (this.runtimeType instanceof WildcardType) {
/*  354 */       return boundsAsInterfaces(((WildcardType)this.runtimeType).getUpperBounds());
/*      */     }
/*  356 */     ImmutableList.Builder<TypeToken<? super T>> builder = ImmutableList.builder();
/*  357 */     for (Type interfaceType : getRawType().getGenericInterfaces()) {
/*      */ 
/*      */       
/*  360 */       TypeToken<? super T> resolvedInterface = (TypeToken)resolveSupertype(interfaceType);
/*  361 */       builder.add(resolvedInterface);
/*      */     } 
/*  363 */     return builder.build();
/*      */   }
/*      */   
/*      */   private ImmutableList<TypeToken<? super T>> boundsAsInterfaces(Type[] bounds) {
/*  367 */     ImmutableList.Builder<TypeToken<? super T>> builder = ImmutableList.builder();
/*  368 */     for (Type bound : bounds) {
/*      */       
/*  370 */       TypeToken<? super T> boundType = (TypeToken)of(bound);
/*  371 */       if (boundType.getRawType().isInterface()) {
/*  372 */         builder.add(boundType);
/*      */       }
/*      */     } 
/*  375 */     return builder.build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeSet getTypes() {
/*  390 */     return new TypeSet();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<? super T> getSupertype(Class<? super T> superclass) {
/*  399 */     Preconditions.checkArgument(
/*  400 */         someRawTypeIsSubclassOf(superclass), "%s is not a super class of %s", superclass, this);
/*      */ 
/*      */ 
/*      */     
/*  404 */     if (this.runtimeType instanceof TypeVariable) {
/*  405 */       return getSupertypeFromUpperBounds(superclass, ((TypeVariable)this.runtimeType).getBounds());
/*      */     }
/*  407 */     if (this.runtimeType instanceof WildcardType) {
/*  408 */       return getSupertypeFromUpperBounds(superclass, ((WildcardType)this.runtimeType).getUpperBounds());
/*      */     }
/*  410 */     if (superclass.isArray()) {
/*  411 */       return getArraySupertype(superclass);
/*      */     }
/*      */ 
/*      */     
/*  415 */     TypeToken<? super T> supertype = (TypeToken)resolveSupertype((toGenericType((Class)superclass)).runtimeType);
/*  416 */     return supertype;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<? extends T> getSubtype(Class<?> subclass) {
/*  425 */     Preconditions.checkArgument(!(this.runtimeType instanceof TypeVariable), "Cannot get subtype of type variable <%s>", this);
/*      */     
/*  427 */     if (this.runtimeType instanceof WildcardType) {
/*  428 */       return getSubtypeFromLowerBounds(subclass, ((WildcardType)this.runtimeType).getLowerBounds());
/*      */     }
/*      */     
/*  431 */     if (isArray()) {
/*  432 */       return getArraySubtype(subclass);
/*      */     }
/*      */     
/*  435 */     Preconditions.checkArgument(
/*  436 */         getRawType().isAssignableFrom(subclass), "%s isn't a subclass of %s", subclass, this);
/*  437 */     Type resolvedTypeArgs = resolveTypeArgsForSubclass(subclass);
/*      */     
/*  439 */     TypeToken<? extends T> subtype = (TypeToken)of(resolvedTypeArgs);
/*  440 */     Preconditions.checkArgument(subtype
/*  441 */         .isSubtypeOf(this), "%s does not appear to be a subtype of %s", subtype, this);
/*  442 */     return subtype;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSupertypeOf(TypeToken<?> type) {
/*  454 */     return type.isSubtypeOf(getType());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSupertypeOf(Type type) {
/*  466 */     return of(type).isSubtypeOf(getType());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSubtypeOf(TypeToken<?> type) {
/*  478 */     return isSubtypeOf(type.getType());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSubtypeOf(Type supertype) {
/*  490 */     Preconditions.checkNotNull(supertype);
/*  491 */     if (supertype instanceof WildcardType)
/*      */     {
/*      */ 
/*      */       
/*  495 */       return any(((WildcardType)supertype).getLowerBounds()).isSupertypeOf(this.runtimeType);
/*      */     }
/*      */ 
/*      */     
/*  499 */     if (this.runtimeType instanceof WildcardType)
/*      */     {
/*  501 */       return any(((WildcardType)this.runtimeType).getUpperBounds()).isSubtypeOf(supertype);
/*      */     }
/*      */ 
/*      */     
/*  505 */     if (this.runtimeType instanceof TypeVariable) {
/*  506 */       return (this.runtimeType.equals(supertype) || 
/*  507 */         any(((TypeVariable)this.runtimeType).getBounds()).isSubtypeOf(supertype));
/*      */     }
/*  509 */     if (this.runtimeType instanceof GenericArrayType) {
/*  510 */       return of(supertype).isSupertypeOfArray((GenericArrayType)this.runtimeType);
/*      */     }
/*      */     
/*  513 */     if (supertype instanceof Class)
/*  514 */       return someRawTypeIsSubclassOf((Class)supertype); 
/*  515 */     if (supertype instanceof ParameterizedType)
/*  516 */       return isSubtypeOfParameterizedType((ParameterizedType)supertype); 
/*  517 */     if (supertype instanceof GenericArrayType) {
/*  518 */       return isSubtypeOfArrayType((GenericArrayType)supertype);
/*      */     }
/*  520 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isArray() {
/*  529 */     return (getComponentType() != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isPrimitive() {
/*  538 */     return (this.runtimeType instanceof Class && ((Class)this.runtimeType).isPrimitive());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<T> wrap() {
/*  548 */     if (isPrimitive()) {
/*      */       
/*  550 */       Class<T> type = (Class<T>)this.runtimeType;
/*  551 */       return of(Primitives.wrap(type));
/*      */     } 
/*  553 */     return this;
/*      */   }
/*      */   
/*      */   private boolean isWrapper() {
/*  557 */     return Primitives.allWrapperTypes().contains(this.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<T> unwrap() {
/*  567 */     if (isWrapper()) {
/*      */       
/*  569 */       Class<T> type = (Class<T>)this.runtimeType;
/*  570 */       return of(Primitives.unwrap(type));
/*      */     } 
/*  572 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   public final TypeToken<?> getComponentType() {
/*  581 */     Type componentType = Types.getComponentType(this.runtimeType);
/*  582 */     if (componentType == null) {
/*  583 */       return null;
/*      */     }
/*  585 */     return of(componentType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Invokable<T, Object> method(Method method) {
/*  594 */     Preconditions.checkArgument(
/*  595 */         someRawTypeIsSubclassOf(method.getDeclaringClass()), "%s not declared by %s", method, this);
/*      */ 
/*      */ 
/*      */     
/*  599 */     return new Invokable.MethodInvokable<T>(method)
/*      */       {
/*      */         Type getGenericReturnType() {
/*  602 */           return TypeToken.this.getCovariantTypeResolver().resolveType(super.getGenericReturnType());
/*      */         }
/*      */ 
/*      */         
/*      */         Type[] getGenericParameterTypes() {
/*  607 */           return TypeToken.this.getInvariantTypeResolver().resolveTypesInPlace(super.getGenericParameterTypes());
/*      */         }
/*      */ 
/*      */         
/*      */         Type[] getGenericExceptionTypes() {
/*  612 */           return TypeToken.this.getCovariantTypeResolver().resolveTypesInPlace(super.getGenericExceptionTypes());
/*      */         }
/*      */ 
/*      */         
/*      */         public TypeToken<T> getOwnerType() {
/*  617 */           return TypeToken.this;
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  622 */           return getOwnerType() + "." + super.toString();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Invokable<T, T> constructor(Constructor<?> constructor) {
/*  633 */     Preconditions.checkArgument(
/*  634 */         (constructor.getDeclaringClass() == getRawType()), "%s not declared by %s", constructor, 
/*      */ 
/*      */         
/*  637 */         getRawType());
/*  638 */     return new Invokable.ConstructorInvokable<T>(constructor)
/*      */       {
/*      */         Type getGenericReturnType() {
/*  641 */           return TypeToken.this.getCovariantTypeResolver().resolveType(super.getGenericReturnType());
/*      */         }
/*      */ 
/*      */         
/*      */         Type[] getGenericParameterTypes() {
/*  646 */           return TypeToken.this.getInvariantTypeResolver().resolveTypesInPlace(super.getGenericParameterTypes());
/*      */         }
/*      */ 
/*      */         
/*      */         Type[] getGenericExceptionTypes() {
/*  651 */           return TypeToken.this.getCovariantTypeResolver().resolveTypesInPlace(super.getGenericExceptionTypes());
/*      */         }
/*      */ 
/*      */         
/*      */         public TypeToken<T> getOwnerType() {
/*  656 */           return TypeToken.this;
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  661 */           return getOwnerType() + "(" + Joiner.on(", ").join((Object[])getGenericParameterTypes()) + ")";
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public class TypeSet
/*      */     extends ForwardingSet<TypeToken<? super T>>
/*      */     implements Serializable
/*      */   {
/*      */     @CheckForNull
/*      */     private transient ImmutableSet<TypeToken<? super T>> types;
/*      */ 
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */     
/*      */     public TypeSet interfaces() {
/*  680 */       return new TypeToken.InterfaceSet(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeSet classes() {
/*  685 */       return new TypeToken.ClassSet();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<TypeToken<? super T>> delegate() {
/*  690 */       ImmutableSet<TypeToken<? super T>> filteredTypes = this.types;
/*  691 */       if (filteredTypes == null) {
/*      */ 
/*      */ 
/*      */         
/*  695 */         ImmutableList<TypeToken<? super T>> collectedTypes = (ImmutableList)TypeToken.TypeCollector.FOR_GENERIC_TYPE.collectTypes(TypeToken.this);
/*  696 */         return 
/*      */ 
/*      */           
/*  699 */           (Set<TypeToken<? super T>>)(this.types = FluentIterable.from((Iterable)collectedTypes).filter(TypeToken.TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet());
/*      */       } 
/*  701 */       return (Set<TypeToken<? super T>>)filteredTypes;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Class<? super T>> rawTypes() {
/*  710 */       ImmutableList<Class<? super T>> collectedTypes = (ImmutableList)TypeToken.TypeCollector.FOR_RAW_TYPE.collectTypes((Iterable<? extends Class<?>>)TypeToken.this.getRawTypes());
/*  711 */       return (Set<Class<? super T>>)ImmutableSet.copyOf((Collection)collectedTypes);
/*      */     }
/*      */   }
/*      */   
/*      */   private final class InterfaceSet
/*      */     extends TypeSet {
/*      */     private final transient TypeToken<T>.TypeSet allTypes;
/*      */     @CheckForNull
/*      */     private transient ImmutableSet<TypeToken<? super T>> interfaces;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     InterfaceSet(TypeToken<T>.TypeSet allTypes) {
/*  723 */       this.allTypes = allTypes;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<TypeToken<? super T>> delegate() {
/*  728 */       ImmutableSet<TypeToken<? super T>> result = this.interfaces;
/*  729 */       if (result == null) {
/*  730 */         return 
/*  731 */           (Set<TypeToken<? super T>>)(this.interfaces = FluentIterable.from((Iterable)this.allTypes).filter(TypeToken.TypeFilter.INTERFACE_ONLY).toSet());
/*      */       }
/*  733 */       return (Set<TypeToken<? super T>>)result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet interfaces() {
/*  739 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Class<? super T>> rawTypes() {
/*  747 */       ImmutableList<Class<? super T>> collectedTypes = (ImmutableList)TypeToken.TypeCollector.FOR_RAW_TYPE.collectTypes((Iterable<? extends Class<?>>)TypeToken.this.getRawTypes());
/*  748 */       return (Set<Class<? super T>>)FluentIterable.from((Iterable)collectedTypes).filter(Class::isInterface).toSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet classes() {
/*  753 */       throw new UnsupportedOperationException("interfaces().classes() not supported.");
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/*  757 */       return TypeToken.this.getTypes().interfaces();
/*      */     }
/*      */   }
/*      */   
/*      */   private final class ClassSet extends TypeSet {
/*      */     @CheckForNull
/*      */     private transient ImmutableSet<TypeToken<? super T>> classes;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     private ClassSet() {}
/*      */     
/*      */     protected Set<TypeToken<? super T>> delegate() {
/*  769 */       ImmutableSet<TypeToken<? super T>> result = this.classes;
/*  770 */       if (result == null) {
/*      */ 
/*      */ 
/*      */         
/*  774 */         ImmutableList<TypeToken<? super T>> collectedTypes = TypeToken.TypeCollector.FOR_GENERIC_TYPE.classesOnly().collectTypes(TypeToken.this);
/*  775 */         return 
/*      */ 
/*      */           
/*  778 */           (Set<TypeToken<? super T>>)(this.classes = FluentIterable.from((Iterable)collectedTypes).filter(TypeToken.TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet());
/*      */       } 
/*  780 */       return (Set<TypeToken<? super T>>)result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet classes() {
/*  786 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Class<? super T>> rawTypes() {
/*  794 */       ImmutableList<Class<? super T>> collectedTypes = TypeToken.TypeCollector.FOR_RAW_TYPE.classesOnly().collectTypes((Iterable<? extends Class<? super T>>)TypeToken.this.getRawTypes());
/*  795 */       return (Set<Class<? super T>>)ImmutableSet.copyOf((Collection)collectedTypes);
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet interfaces() {
/*  800 */       throw new UnsupportedOperationException("classes().interfaces() not supported.");
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/*  804 */       return TypeToken.this.getTypes().classes();
/*      */     }
/*      */   }
/*      */   
/*      */   private enum TypeFilter
/*      */     implements Predicate<TypeToken<?>>
/*      */   {
/*  811 */     IGNORE_TYPE_VARIABLE_OR_WILDCARD
/*      */     {
/*      */       public boolean apply(TypeToken<?> type) {
/*  814 */         return (!(type.runtimeType instanceof TypeVariable) && 
/*  815 */           !(type.runtimeType instanceof WildcardType));
/*      */       }
/*      */     },
/*  818 */     INTERFACE_ONLY
/*      */     {
/*      */       public boolean apply(TypeToken<?> type) {
/*  821 */         return type.getRawType().isInterface();
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(@CheckForNull Object o) {
/*  831 */     if (o instanceof TypeToken) {
/*  832 */       TypeToken<?> that = (TypeToken)o;
/*  833 */       return this.runtimeType.equals(that.runtimeType);
/*      */     } 
/*  835 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  840 */     return this.runtimeType.hashCode();
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*  845 */     return Types.toString(this.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object writeReplace() {
/*  852 */     return of((new TypeResolver()).resolveType(this.runtimeType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   final TypeToken<T> rejectTypeVariables() {
/*  861 */     (new TypeVisitor()
/*      */       {
/*      */         void visitTypeVariable(TypeVariable<?> type) {
/*  864 */           throw new IllegalArgumentException(TypeToken.this
/*  865 */               .runtimeType + "contains a type variable and is not safe for the operation");
/*      */         }
/*      */ 
/*      */         
/*      */         void visitWildcardType(WildcardType type) {
/*  870 */           visit(type.getLowerBounds());
/*  871 */           visit(type.getUpperBounds());
/*      */         }
/*      */ 
/*      */         
/*      */         void visitParameterizedType(ParameterizedType type) {
/*  876 */           visit(type.getActualTypeArguments());
/*  877 */           visit(new Type[] { type.getOwnerType() });
/*      */         }
/*      */ 
/*      */         
/*      */         void visitGenericArrayType(GenericArrayType type) {
/*  882 */           visit(new Type[] { type.getGenericComponentType() });
/*      */         }
/*  884 */       }).visit(new Type[] { this.runtimeType });
/*  885 */     return this;
/*      */   }
/*      */   
/*      */   private boolean someRawTypeIsSubclassOf(Class<?> superclass) {
/*  889 */     for (UnmodifiableIterator<Class<?>> unmodifiableIterator = getRawTypes().iterator(); unmodifiableIterator.hasNext(); ) { Class<?> rawType = unmodifiableIterator.next();
/*  890 */       if (superclass.isAssignableFrom(rawType)) {
/*  891 */         return true;
/*      */       } }
/*      */     
/*  894 */     return false;
/*      */   }
/*      */   
/*      */   private boolean isSubtypeOfParameterizedType(ParameterizedType supertype) {
/*  898 */     Class<?> matchedClass = of(supertype).getRawType();
/*  899 */     if (!someRawTypeIsSubclassOf(matchedClass)) {
/*  900 */       return false;
/*      */     }
/*  902 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])matchedClass.getTypeParameters();
/*  903 */     Type[] supertypeArgs = supertype.getActualTypeArguments();
/*  904 */     for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/*  905 */       Type subtypeParam = getCovariantTypeResolver().resolveType(arrayOfTypeVariable[i]);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  910 */       if (!of(subtypeParam).is(supertypeArgs[i], arrayOfTypeVariable[i])) {
/*  911 */         return false;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  917 */     return (Modifier.isStatic(((Class)supertype.getRawType()).getModifiers()) || supertype
/*  918 */       .getOwnerType() == null || 
/*  919 */       isOwnedBySubtypeOf(supertype.getOwnerType()));
/*      */   }
/*      */   
/*      */   private boolean isSubtypeOfArrayType(GenericArrayType supertype) {
/*  923 */     if (this.runtimeType instanceof Class) {
/*  924 */       Class<?> fromClass = (Class)this.runtimeType;
/*  925 */       if (!fromClass.isArray()) {
/*  926 */         return false;
/*      */       }
/*  928 */       return of(fromClass.getComponentType()).isSubtypeOf(supertype.getGenericComponentType());
/*  929 */     }  if (this.runtimeType instanceof GenericArrayType) {
/*  930 */       GenericArrayType fromArrayType = (GenericArrayType)this.runtimeType;
/*  931 */       return of(fromArrayType.getGenericComponentType())
/*  932 */         .isSubtypeOf(supertype.getGenericComponentType());
/*      */     } 
/*  934 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isSupertypeOfArray(GenericArrayType subtype) {
/*  939 */     if (this.runtimeType instanceof Class) {
/*  940 */       Class<?> thisClass = (Class)this.runtimeType;
/*  941 */       if (!thisClass.isArray()) {
/*  942 */         return thisClass.isAssignableFrom(Object[].class);
/*      */       }
/*  944 */       return of(subtype.getGenericComponentType()).isSubtypeOf(thisClass.getComponentType());
/*  945 */     }  if (this.runtimeType instanceof GenericArrayType) {
/*  946 */       return of(subtype.getGenericComponentType())
/*  947 */         .isSubtypeOf(((GenericArrayType)this.runtimeType).getGenericComponentType());
/*      */     }
/*  949 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean is(Type formalType, TypeVariable<?> declaration) {
/*  980 */     if (this.runtimeType.equals(formalType)) {
/*  981 */       return true;
/*      */     }
/*  983 */     if (formalType instanceof WildcardType) {
/*  984 */       WildcardType your = canonicalizeWildcardType(declaration, (WildcardType)formalType);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  990 */       return (every(your.getUpperBounds()).isSupertypeOf(this.runtimeType) && 
/*  991 */         every(your.getLowerBounds()).isSubtypeOf(this.runtimeType));
/*      */     } 
/*  993 */     return canonicalizeWildcardsInType(this.runtimeType).equals(canonicalizeWildcardsInType(formalType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Type canonicalizeTypeArg(TypeVariable<?> declaration, Type typeArg) {
/* 1015 */     return (typeArg instanceof WildcardType) ? 
/* 1016 */       canonicalizeWildcardType(declaration, (WildcardType)typeArg) : 
/* 1017 */       canonicalizeWildcardsInType(typeArg);
/*      */   }
/*      */   
/*      */   private static Type canonicalizeWildcardsInType(Type type) {
/* 1021 */     if (type instanceof ParameterizedType) {
/* 1022 */       return canonicalizeWildcardsInParameterizedType((ParameterizedType)type);
/*      */     }
/* 1024 */     if (type instanceof GenericArrayType) {
/* 1025 */       return Types.newArrayType(
/* 1026 */           canonicalizeWildcardsInType(((GenericArrayType)type).getGenericComponentType()));
/*      */     }
/* 1028 */     return type;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static WildcardType canonicalizeWildcardType(TypeVariable<?> declaration, WildcardType type) {
/* 1036 */     Type[] declared = declaration.getBounds();
/* 1037 */     List<Type> upperBounds = new ArrayList<>();
/* 1038 */     for (Type bound : type.getUpperBounds()) {
/* 1039 */       if (!any(declared).isSubtypeOf(bound)) {
/* 1040 */         upperBounds.add(canonicalizeWildcardsInType(bound));
/*      */       }
/*      */     } 
/* 1043 */     return new Types.WildcardTypeImpl(type.getLowerBounds(), upperBounds.<Type>toArray(new Type[0]));
/*      */   }
/*      */ 
/*      */   
/*      */   private static ParameterizedType canonicalizeWildcardsInParameterizedType(ParameterizedType type) {
/* 1048 */     Class<?> rawType = (Class)type.getRawType();
/* 1049 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])rawType.getTypeParameters();
/* 1050 */     Type[] typeArgs = type.getActualTypeArguments();
/* 1051 */     for (int i = 0; i < typeArgs.length; i++) {
/* 1052 */       typeArgs[i] = canonicalizeTypeArg(arrayOfTypeVariable[i], typeArgs[i]);
/*      */     }
/* 1054 */     return Types.newParameterizedTypeWithOwner(type.getOwnerType(), rawType, typeArgs);
/*      */   }
/*      */ 
/*      */   
/*      */   private static Bounds every(Type[] bounds) {
/* 1059 */     return new Bounds(bounds, false);
/*      */   }
/*      */ 
/*      */   
/*      */   private static Bounds any(Type[] bounds) {
/* 1064 */     return new Bounds(bounds, true);
/*      */   }
/*      */   
/*      */   private static class Bounds {
/*      */     private final Type[] bounds;
/*      */     private final boolean target;
/*      */     
/*      */     Bounds(Type[] bounds, boolean target) {
/* 1072 */       this.bounds = bounds;
/* 1073 */       this.target = target;
/*      */     }
/*      */     
/*      */     boolean isSubtypeOf(Type supertype) {
/* 1077 */       for (Type bound : this.bounds) {
/* 1078 */         if (TypeToken.of(bound).isSubtypeOf(supertype) == this.target) {
/* 1079 */           return this.target;
/*      */         }
/*      */       } 
/* 1082 */       return !this.target;
/*      */     }
/*      */     
/*      */     boolean isSupertypeOf(Type subtype) {
/* 1086 */       TypeToken<?> type = TypeToken.of(subtype);
/* 1087 */       for (Type bound : this.bounds) {
/* 1088 */         if (type.isSubtypeOf(bound) == this.target) {
/* 1089 */           return this.target;
/*      */         }
/*      */       } 
/* 1092 */       return !this.target;
/*      */     }
/*      */   }
/*      */   
/*      */   private ImmutableSet<Class<? super T>> getRawTypes() {
/* 1097 */     final ImmutableSet.Builder<Class<?>> builder = ImmutableSet.builder();
/* 1098 */     (new TypeVisitor(this)
/*      */       {
/*      */         void visitTypeVariable(TypeVariable<?> t) {
/* 1101 */           visit(t.getBounds());
/*      */         }
/*      */ 
/*      */         
/*      */         void visitWildcardType(WildcardType t) {
/* 1106 */           visit(t.getUpperBounds());
/*      */         }
/*      */ 
/*      */         
/*      */         void visitParameterizedType(ParameterizedType t) {
/* 1111 */           builder.add(t.getRawType());
/*      */         }
/*      */ 
/*      */         
/*      */         void visitClass(Class<?> t) {
/* 1116 */           builder.add(t);
/*      */         }
/*      */ 
/*      */         
/*      */         void visitGenericArrayType(GenericArrayType t) {
/* 1121 */           builder.add(Types.getArrayClass(TypeToken.of(t.getGenericComponentType()).getRawType()));
/*      */         }
/* 1123 */       }).visit(new Type[] { this.runtimeType });
/*      */ 
/*      */     
/* 1126 */     ImmutableSet<Class<? super T>> result = builder.build();
/* 1127 */     return result;
/*      */   }
/*      */   
/*      */   private boolean isOwnedBySubtypeOf(Type supertype) {
/* 1131 */     for (TypeToken<?> type : (Iterable<TypeToken<?>>)getTypes()) {
/* 1132 */       Type ownerType = type.getOwnerTypeIfPresent();
/* 1133 */       if (ownerType != null && of(ownerType).isSubtypeOf(supertype)) {
/* 1134 */         return true;
/*      */       }
/*      */     } 
/* 1137 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CheckForNull
/*      */   private Type getOwnerTypeIfPresent() {
/* 1146 */     if (this.runtimeType instanceof ParameterizedType)
/* 1147 */       return ((ParameterizedType)this.runtimeType).getOwnerType(); 
/* 1148 */     if (this.runtimeType instanceof Class) {
/* 1149 */       return ((Class)this.runtimeType).getEnclosingClass();
/*      */     }
/* 1151 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   static <T> TypeToken<? extends T> toGenericType(Class<T> cls) {
/* 1164 */     if (cls.isArray()) {
/*      */       
/* 1166 */       Type arrayOfGenericType = Types.newArrayType(
/*      */           
/* 1168 */           (toGenericType((Class)cls.getComponentType())).runtimeType);
/*      */       
/* 1170 */       TypeToken<? extends T> result = (TypeToken)of(arrayOfGenericType);
/* 1171 */       return result;
/*      */     } 
/* 1173 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])cls.getTypeParameters();
/*      */ 
/*      */ 
/*      */     
/* 1177 */     Type ownerType = (cls.isMemberClass() && !Modifier.isStatic(cls.getModifiers())) ? (toGenericType((Class)cls.getEnclosingClass())).runtimeType : null;
/*      */     
/* 1179 */     if (arrayOfTypeVariable.length > 0 || (ownerType != null && ownerType != cls.getEnclosingClass())) {
/*      */ 
/*      */ 
/*      */       
/* 1183 */       TypeToken<? extends T> type = (TypeToken)of(Types.newParameterizedTypeWithOwner(ownerType, cls, (Type[])arrayOfTypeVariable));
/* 1184 */       return type;
/*      */     } 
/* 1186 */     return of(cls);
/*      */   }
/*      */ 
/*      */   
/*      */   private TypeResolver getCovariantTypeResolver() {
/* 1191 */     TypeResolver resolver = this.covariantTypeResolver;
/* 1192 */     if (resolver == null) {
/* 1193 */       resolver = this.covariantTypeResolver = TypeResolver.covariantly(this.runtimeType);
/*      */     }
/* 1195 */     return resolver;
/*      */   }
/*      */   
/*      */   private TypeResolver getInvariantTypeResolver() {
/* 1199 */     TypeResolver resolver = this.invariantTypeResolver;
/* 1200 */     if (resolver == null) {
/* 1201 */       resolver = this.invariantTypeResolver = TypeResolver.invariantly(this.runtimeType);
/*      */     }
/* 1203 */     return resolver;
/*      */   }
/*      */ 
/*      */   
/*      */   private TypeToken<? super T> getSupertypeFromUpperBounds(Class<? super T> supertype, Type[] upperBounds) {
/* 1208 */     for (Type upperBound : upperBounds) {
/*      */       
/* 1210 */       TypeToken<? super T> bound = (TypeToken)of(upperBound);
/* 1211 */       if (bound.isSubtypeOf(supertype)) {
/*      */         
/* 1213 */         TypeToken<? super T> result = bound.getSupertype(supertype);
/* 1214 */         return result;
/*      */       } 
/*      */     } 
/* 1217 */     throw new IllegalArgumentException(supertype + " isn't a super type of " + this);
/*      */   }
/*      */   
/*      */   private TypeToken<? extends T> getSubtypeFromLowerBounds(Class<?> subclass, Type[] lowerBounds) {
/* 1221 */     if (lowerBounds.length > 0) {
/*      */       
/* 1223 */       TypeToken<? extends T> bound = (TypeToken)of(lowerBounds[0]);
/*      */       
/* 1225 */       return bound.getSubtype(subclass);
/*      */     } 
/* 1227 */     throw new IllegalArgumentException(subclass + " isn't a subclass of " + this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private TypeToken<? super T> getArraySupertype(Class<? super T> supertype) {
/* 1234 */     TypeToken<?> componentType = getComponentType();
/*      */     
/* 1236 */     if (componentType == null) {
/* 1237 */       throw new IllegalArgumentException(supertype + " isn't a super type of " + this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1246 */     TypeToken<?> componentSupertype = componentType.getSupertype(Objects.<Class<?>>requireNonNull(supertype.getComponentType()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1251 */     TypeToken<? super T> result = (TypeToken)of(newArrayClassOrGenericArrayType(componentSupertype.runtimeType));
/* 1252 */     return result;
/*      */   }
/*      */   
/*      */   private TypeToken<? extends T> getArraySubtype(Class<?> subclass) {
/* 1256 */     Class<?> subclassComponentType = subclass.getComponentType();
/* 1257 */     if (subclassComponentType == null) {
/* 1258 */       throw new IllegalArgumentException(subclass + " does not appear to be a subtype of " + this);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1263 */     TypeToken<?> componentSubtype = ((TypeToken)Objects.<TypeToken>requireNonNull(getComponentType())).getSubtype(subclassComponentType);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1268 */     TypeToken<? extends T> result = (TypeToken)of(newArrayClassOrGenericArrayType(componentSubtype.runtimeType));
/* 1269 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Type resolveTypeArgsForSubclass(Class<?> subclass) {
/* 1277 */     if (this.runtimeType instanceof Class && ((subclass
/* 1278 */       .getTypeParameters()).length == 0 || (
/* 1279 */       getRawType().getTypeParameters()).length != 0))
/*      */     {
/* 1281 */       return subclass;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1290 */     TypeToken<?> genericSubtype = toGenericType(subclass);
/*      */ 
/*      */     
/* 1293 */     Type supertypeWithArgsFromSubtype = (genericSubtype.getSupertype(getRawType())).runtimeType;
/* 1294 */     return (new TypeResolver())
/* 1295 */       .where(supertypeWithArgsFromSubtype, this.runtimeType)
/* 1296 */       .resolveType(genericSubtype.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Type newArrayClassOrGenericArrayType(Type componentType) {
/* 1304 */     return Types.JavaVersion.JAVA7.newArrayType(componentType);
/*      */   }
/*      */   
/*      */   private static final class SimpleTypeToken<T> extends TypeToken<T> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SimpleTypeToken(Type type) {
/* 1310 */       super(type);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static abstract class TypeCollector<K>
/*      */   {
/*      */     private TypeCollector() {}
/*      */ 
/*      */ 
/*      */     
/* 1323 */     static final TypeCollector<TypeToken<?>> FOR_GENERIC_TYPE = new TypeCollector<TypeToken<?>>()
/*      */       {
/*      */         Class<?> getRawType(TypeToken<?> type)
/*      */         {
/* 1327 */           return type.getRawType();
/*      */         }
/*      */ 
/*      */         
/*      */         Iterable<? extends TypeToken<?>> getInterfaces(TypeToken<?> type) {
/* 1332 */           return (Iterable<? extends TypeToken<?>>)type.getGenericInterfaces();
/*      */         }
/*      */ 
/*      */         
/*      */         @CheckForNull
/*      */         TypeToken<?> getSuperclass(TypeToken<?> type) {
/* 1338 */           return type.getGenericSuperclass();
/*      */         }
/*      */       };
/*      */     
/* 1342 */     static final TypeCollector<Class<?>> FOR_RAW_TYPE = new TypeCollector<Class<?>>()
/*      */       {
/*      */         Class<?> getRawType(Class<?> type)
/*      */         {
/* 1346 */           return type;
/*      */         }
/*      */ 
/*      */         
/*      */         Iterable<? extends Class<?>> getInterfaces(Class<?> type) {
/* 1351 */           return Arrays.asList(type.getInterfaces());
/*      */         }
/*      */ 
/*      */         
/*      */         @CheckForNull
/*      */         Class<?> getSuperclass(Class<?> type) {
/* 1357 */           return type.getSuperclass();
/*      */         }
/*      */       };
/*      */ 
/*      */     
/*      */     final TypeCollector<K> classesOnly() {
/* 1363 */       return new ForwardingTypeCollector<K>(this, this)
/*      */         {
/*      */           Iterable<? extends K> getInterfaces(K type) {
/* 1366 */             return (Iterable<? extends K>)ImmutableSet.of();
/*      */           }
/*      */ 
/*      */           
/*      */           ImmutableList<K> collectTypes(Iterable<? extends K> types) {
/* 1371 */             ImmutableList.Builder<K> builder = ImmutableList.builder();
/* 1372 */             for (K type : types) {
/* 1373 */               if (!getRawType(type).isInterface()) {
/* 1374 */                 builder.add(type);
/*      */               }
/*      */             } 
/* 1377 */             return super.collectTypes((Iterable<? extends K>)builder.build());
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     final ImmutableList<K> collectTypes(K type) {
/* 1383 */       return collectTypes((Iterable<? extends K>)ImmutableList.of(type));
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableList<K> collectTypes(Iterable<? extends K> types) {
/* 1388 */       Map<K, Integer> map = Maps.newHashMap();
/* 1389 */       for (K type : types) {
/* 1390 */         collectTypes(type, map);
/*      */       }
/* 1392 */       return sortKeysByValue(map, (Comparator<? super Integer>)Ordering.natural().reverse());
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     private int collectTypes(K type, Map<? super K, Integer> map) {
/* 1398 */       Integer existing = map.get(type);
/* 1399 */       if (existing != null)
/*      */       {
/* 1401 */         return existing.intValue();
/*      */       }
/*      */       
/* 1404 */       int aboveMe = getRawType(type).isInterface() ? 1 : 0;
/* 1405 */       for (K interfaceType : getInterfaces(type)) {
/* 1406 */         aboveMe = Math.max(aboveMe, collectTypes(interfaceType, map));
/*      */       }
/* 1408 */       K superclass = getSuperclass(type);
/* 1409 */       if (superclass != null) {
/* 1410 */         aboveMe = Math.max(aboveMe, collectTypes(superclass, map));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1417 */       map.put(type, Integer.valueOf(aboveMe + 1));
/* 1418 */       return aboveMe + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     private static <K, V> ImmutableList<K> sortKeysByValue(final Map<K, V> map, final Comparator<? super V> valueComparator) {
/* 1423 */       Ordering<K> keyOrdering = new Ordering<K>()
/*      */         {
/*      */           
/*      */           public int compare(K left, K right)
/*      */           {
/* 1428 */             return valueComparator.compare(
/* 1429 */                 Objects.requireNonNull(map.get(left)), Objects.requireNonNull(map.get(right)));
/*      */           }
/*      */         };
/* 1432 */       return keyOrdering.immutableSortedCopy(map.keySet());
/*      */     }
/*      */     
/*      */     abstract Class<?> getRawType(K param1K);
/*      */     
/*      */     abstract Iterable<? extends K> getInterfaces(K param1K);
/*      */     
/*      */     @CheckForNull
/*      */     abstract K getSuperclass(K param1K);
/*      */     
/*      */     private static class ForwardingTypeCollector<K>
/*      */       extends TypeCollector<K> {
/*      */       private final TypeToken.TypeCollector<K> delegate;
/*      */       
/*      */       ForwardingTypeCollector(TypeToken.TypeCollector<K> delegate) {
/* 1447 */         this.delegate = delegate;
/*      */       }
/*      */ 
/*      */       
/*      */       Class<?> getRawType(K type) {
/* 1452 */         return this.delegate.getRawType(type);
/*      */       }
/*      */ 
/*      */       
/*      */       Iterable<? extends K> getInterfaces(K type) {
/* 1457 */         return this.delegate.getInterfaces(type);
/*      */       }
/*      */ 
/*      */       
/*      */       @CheckForNull
/*      */       K getSuperclass(K type) {
/* 1463 */         return this.delegate.getSuperclass(type);
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/reflect/TypeToken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */