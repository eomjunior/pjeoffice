/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.security.AccessControlException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ final class Types
/*     */ {
/*  59 */   private static final Joiner COMMA_JOINER = Joiner.on(", ").useForNull("null");
/*     */ 
/*     */   
/*     */   static Type newArrayType(Type componentType) {
/*  63 */     if (componentType instanceof WildcardType) {
/*  64 */       WildcardType wildcard = (WildcardType)componentType;
/*  65 */       Type[] lowerBounds = wildcard.getLowerBounds();
/*  66 */       Preconditions.checkArgument((lowerBounds.length <= 1), "Wildcard cannot have more than one lower bounds.");
/*  67 */       if (lowerBounds.length == 1) {
/*  68 */         return supertypeOf(newArrayType(lowerBounds[0]));
/*     */       }
/*  70 */       Type[] upperBounds = wildcard.getUpperBounds();
/*  71 */       Preconditions.checkArgument((upperBounds.length == 1), "Wildcard should have only one upper bound.");
/*  72 */       return subtypeOf(newArrayType(upperBounds[0]));
/*     */     } 
/*     */     
/*  75 */     return JavaVersion.CURRENT.newArrayType(componentType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ParameterizedType newParameterizedTypeWithOwner(@CheckForNull Type ownerType, Class<?> rawType, Type... arguments) {
/*  84 */     if (ownerType == null) {
/*  85 */       return newParameterizedType(rawType, arguments);
/*     */     }
/*     */     
/*  88 */     Preconditions.checkNotNull(arguments);
/*  89 */     Preconditions.checkArgument((rawType.getEnclosingClass() != null), "Owner type for unenclosed %s", rawType);
/*  90 */     return new ParameterizedTypeImpl(ownerType, rawType, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParameterizedType newParameterizedType(Class<?> rawType, Type... arguments) {
/*  95 */     return new ParameterizedTypeImpl(ClassOwnership.JVM_BEHAVIOR
/*  96 */         .getOwnerType(rawType), rawType, arguments);
/*     */   }
/*     */   
/*     */   private enum ClassOwnership
/*     */   {
/* 101 */     OWNED_BY_ENCLOSING_CLASS
/*     */     {
/*     */       @CheckForNull
/*     */       Class<?> getOwnerType(Class<?> rawType) {
/* 105 */         return rawType.getEnclosingClass();
/*     */       }
/*     */     },
/* 108 */     LOCAL_CLASS_HAS_NO_OWNER
/*     */     {
/*     */       @CheckForNull
/*     */       Class<?> getOwnerType(Class<?> rawType) {
/* 112 */         if (rawType.isLocalClass()) {
/* 113 */           return null;
/*     */         }
/* 115 */         return rawType.getEnclosingClass();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     static final ClassOwnership JVM_BEHAVIOR = detectJvmBehavior();
/*     */ 
/*     */     
/*     */     private static ClassOwnership detectJvmBehavior() {
/* 127 */       Class<?> subclass = (new LocalClass<String>() {  }).getClass();
/*     */ 
/*     */       
/* 130 */       ParameterizedType parameterizedType = Objects.<ParameterizedType>requireNonNull((ParameterizedType)subclass.getGenericSuperclass());
/* 131 */       for (ClassOwnership behavior : values()) {
/* 132 */         if (behavior.getOwnerType(LocalClass.class) == parameterizedType.getOwnerType()) {
/* 133 */           return behavior;
/*     */         }
/*     */       } 
/* 136 */       throw new AssertionError();
/*     */     }
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     @CheckForNull
/*     */     abstract Class<?> getOwnerType(Class<?> param1Class); }
/*     */   
/*     */   static <D extends GenericDeclaration> TypeVariable<D> newArtificialTypeVariable(D declaration, String name, Type... bounds) {
/* 147 */     (new Type[1])[0] = Object.class; return newTypeVariableImpl(declaration, name, (bounds.length == 0) ? new Type[1] : bounds);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static WildcardType subtypeOf(Type upperBound) {
/* 153 */     return new WildcardTypeImpl(new Type[0], new Type[] { upperBound });
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static WildcardType supertypeOf(Type lowerBound) {
/* 159 */     return new WildcardTypeImpl(new Type[] { lowerBound }, new Type[] { Object.class });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String toString(Type type) {
/* 168 */     return (type instanceof Class) ? ((Class)type).getName() : type.toString();
/*     */   }
/*     */   
/*     */   @CheckForNull
/*     */   static Type getComponentType(Type type) {
/* 173 */     Preconditions.checkNotNull(type);
/* 174 */     final AtomicReference<Type> result = new AtomicReference<>();
/* 175 */     (new TypeVisitor()
/*     */       {
/*     */         void visitTypeVariable(TypeVariable<?> t) {
/* 178 */           result.set(Types.subtypeOfComponentType(t.getBounds()));
/*     */         }
/*     */ 
/*     */         
/*     */         void visitWildcardType(WildcardType t) {
/* 183 */           result.set(Types.subtypeOfComponentType(t.getUpperBounds()));
/*     */         }
/*     */ 
/*     */         
/*     */         void visitGenericArrayType(GenericArrayType t) {
/* 188 */           result.set(t.getGenericComponentType());
/*     */         }
/*     */ 
/*     */         
/*     */         void visitClass(Class<?> t) {
/* 193 */           result.set(t.getComponentType());
/*     */         }
/* 195 */       }).visit(new Type[] { type });
/* 196 */     return result.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   private static Type subtypeOfComponentType(Type[] bounds) {
/* 205 */     for (Type bound : bounds) {
/* 206 */       Type componentType = getComponentType(bound);
/* 207 */       if (componentType != null) {
/*     */ 
/*     */         
/* 210 */         if (componentType instanceof Class) {
/* 211 */           Class<?> componentClass = (Class)componentType;
/* 212 */           if (componentClass.isPrimitive()) {
/* 213 */             return componentClass;
/*     */           }
/*     */         } 
/* 216 */         return subtypeOf(componentType);
/*     */       } 
/*     */     } 
/* 219 */     return null;
/*     */   }
/*     */   
/*     */   private static final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
/*     */     private final Type componentType;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     GenericArrayTypeImpl(Type componentType) {
/* 227 */       this.componentType = Types.JavaVersion.CURRENT.usedInGenericType(componentType);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type getGenericComponentType() {
/* 232 */       return this.componentType;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 237 */       return Types.toString(this.componentType) + "[]";
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 242 */       return this.componentType.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 247 */       if (obj instanceof GenericArrayType) {
/* 248 */         GenericArrayType that = (GenericArrayType)obj;
/* 249 */         return Objects.equal(getGenericComponentType(), that.getGenericComponentType());
/*     */       } 
/* 251 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ParameterizedTypeImpl
/*     */     implements ParameterizedType, Serializable {
/*     */     @CheckForNull
/*     */     private final Type ownerType;
/*     */     private final ImmutableList<Type> argumentsList;
/*     */     private final Class<?> rawType;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ParameterizedTypeImpl(@CheckForNull Type ownerType, Class<?> rawType, Type[] typeArguments) {
/* 264 */       Preconditions.checkNotNull(rawType);
/* 265 */       Preconditions.checkArgument((typeArguments.length == (rawType.getTypeParameters()).length));
/* 266 */       Types.disallowPrimitiveType(typeArguments, "type parameter");
/* 267 */       this.ownerType = ownerType;
/* 268 */       this.rawType = rawType;
/* 269 */       this.argumentsList = Types.JavaVersion.CURRENT.usedInGenericType(typeArguments);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type[] getActualTypeArguments() {
/* 274 */       return Types.toArray((Collection<Type>)this.argumentsList);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type getRawType() {
/* 279 */       return this.rawType;
/*     */     }
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public Type getOwnerType() {
/* 285 */       return this.ownerType;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 290 */       StringBuilder builder = new StringBuilder();
/* 291 */       if (this.ownerType != null && Types.JavaVersion.CURRENT.jdkTypeDuplicatesOwnerName()) {
/* 292 */         builder.append(Types.JavaVersion.CURRENT.typeName(this.ownerType)).append('.');
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 297 */       Objects.requireNonNull(Types.JavaVersion.CURRENT); return builder.append(this.rawType.getName()).append('<').append(Types.COMMA_JOINER.join(Iterables.transform((Iterable)this.argumentsList, Types.JavaVersion.CURRENT::typeName)))
/* 298 */         .append('>')
/* 299 */         .toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 304 */       return ((this.ownerType == null) ? 0 : this.ownerType.hashCode()) ^ this.argumentsList
/* 305 */         .hashCode() ^ this.rawType
/* 306 */         .hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object other) {
/* 311 */       if (!(other instanceof ParameterizedType)) {
/* 312 */         return false;
/*     */       }
/* 314 */       ParameterizedType that = (ParameterizedType)other;
/* 315 */       return (getRawType().equals(that.getRawType()) && 
/* 316 */         Objects.equal(getOwnerType(), that.getOwnerType()) && 
/* 317 */         Arrays.equals((Object[])getActualTypeArguments(), (Object[])that.getActualTypeArguments()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <D extends GenericDeclaration> TypeVariable<D> newTypeVariableImpl(D genericDeclaration, String name, Type[] bounds) {
/* 325 */     TypeVariableImpl<D> typeVariableImpl = new TypeVariableImpl<>(genericDeclaration, name, bounds);
/*     */ 
/*     */     
/* 328 */     TypeVariable<D> typeVariable = Reflection.<TypeVariable<D>>newProxy((Class)TypeVariable.class, new TypeVariableInvocationHandler(typeVariableImpl));
/*     */     
/* 330 */     return typeVariable;
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
/*     */   private static final class TypeVariableInvocationHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     private static final ImmutableMap<String, Method> typeVariableMethods;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Types.TypeVariableImpl<?> typeVariableImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 363 */       ImmutableMap.Builder<String, Method> builder = ImmutableMap.builder();
/* 364 */       for (Method method : Types.TypeVariableImpl.class.getMethods()) {
/* 365 */         if (method.getDeclaringClass().equals(Types.TypeVariableImpl.class)) {
/*     */           try {
/* 367 */             method.setAccessible(true);
/* 368 */           } catch (AccessControlException accessControlException) {}
/*     */ 
/*     */ 
/*     */           
/* 372 */           builder.put(method.getName(), method);
/*     */         } 
/*     */       } 
/* 375 */       typeVariableMethods = builder.buildKeepingLast();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     TypeVariableInvocationHandler(Types.TypeVariableImpl<?> typeVariableImpl) {
/* 381 */       this.typeVariableImpl = typeVariableImpl;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     public Object invoke(Object proxy, Method method, @CheckForNull Object[] args) throws Throwable {
/* 388 */       String methodName = method.getName();
/* 389 */       Method typeVariableMethod = (Method)typeVariableMethods.get(methodName);
/* 390 */       if (typeVariableMethod == null) {
/* 391 */         throw new UnsupportedOperationException(methodName);
/*     */       }
/*     */       try {
/* 394 */         return typeVariableMethod.invoke(this.typeVariableImpl, args);
/* 395 */       } catch (InvocationTargetException e) {
/* 396 */         throw e.getCause();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class TypeVariableImpl<D extends GenericDeclaration>
/*     */   {
/*     */     private final D genericDeclaration;
/*     */     private final String name;
/*     */     private final ImmutableList<Type> bounds;
/*     */     
/*     */     TypeVariableImpl(D genericDeclaration, String name, Type[] bounds) {
/* 409 */       Types.disallowPrimitiveType(bounds, "bound for type variable");
/* 410 */       this.genericDeclaration = (D)Preconditions.checkNotNull(genericDeclaration);
/* 411 */       this.name = (String)Preconditions.checkNotNull(name);
/* 412 */       this.bounds = ImmutableList.copyOf((Object[])bounds);
/*     */     }
/*     */     
/*     */     public Type[] getBounds() {
/* 416 */       return Types.toArray((Collection<Type>)this.bounds);
/*     */     }
/*     */     
/*     */     public D getGenericDeclaration() {
/* 420 */       return this.genericDeclaration;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 424 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getTypeName() {
/* 428 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 433 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 438 */       return this.genericDeclaration.hashCode() ^ this.name.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 443 */       if (Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY) {
/*     */         
/* 445 */         if (obj != null && 
/* 446 */           Proxy.isProxyClass(obj.getClass()) && 
/* 447 */           Proxy.getInvocationHandler(obj) instanceof Types.TypeVariableInvocationHandler) {
/*     */           
/* 449 */           Types.TypeVariableInvocationHandler typeVariableInvocationHandler = (Types.TypeVariableInvocationHandler)Proxy.getInvocationHandler(obj);
/* 450 */           TypeVariableImpl<?> that = typeVariableInvocationHandler.typeVariableImpl;
/* 451 */           return (this.name.equals(that.getName()) && this.genericDeclaration
/* 452 */             .equals(that.getGenericDeclaration()) && this.bounds
/* 453 */             .equals(that.bounds));
/*     */         } 
/* 455 */         return false;
/*     */       } 
/*     */       
/* 458 */       if (obj instanceof TypeVariable) {
/* 459 */         TypeVariable<?> that = (TypeVariable)obj;
/* 460 */         return (this.name.equals(that.getName()) && this.genericDeclaration
/* 461 */           .equals(that.getGenericDeclaration()));
/*     */       } 
/* 463 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class WildcardTypeImpl
/*     */     implements WildcardType, Serializable {
/*     */     private final ImmutableList<Type> lowerBounds;
/*     */     private final ImmutableList<Type> upperBounds;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     WildcardTypeImpl(Type[] lowerBounds, Type[] upperBounds) {
/* 474 */       Types.disallowPrimitiveType(lowerBounds, "lower bound for wildcard");
/* 475 */       Types.disallowPrimitiveType(upperBounds, "upper bound for wildcard");
/* 476 */       this.lowerBounds = Types.JavaVersion.CURRENT.usedInGenericType(lowerBounds);
/* 477 */       this.upperBounds = Types.JavaVersion.CURRENT.usedInGenericType(upperBounds);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type[] getLowerBounds() {
/* 482 */       return Types.toArray((Collection<Type>)this.lowerBounds);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type[] getUpperBounds() {
/* 487 */       return Types.toArray((Collection<Type>)this.upperBounds);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object obj) {
/* 492 */       if (obj instanceof WildcardType) {
/* 493 */         WildcardType that = (WildcardType)obj;
/* 494 */         return (this.lowerBounds.equals(Arrays.asList(that.getLowerBounds())) && this.upperBounds
/* 495 */           .equals(Arrays.asList(that.getUpperBounds())));
/*     */       } 
/* 497 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 502 */       return this.lowerBounds.hashCode() ^ this.upperBounds.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 507 */       StringBuilder builder = new StringBuilder("?");
/* 508 */       for (UnmodifiableIterator<Type> unmodifiableIterator = this.lowerBounds.iterator(); unmodifiableIterator.hasNext(); ) { Type lowerBound = unmodifiableIterator.next();
/* 509 */         builder.append(" super ").append(Types.JavaVersion.CURRENT.typeName(lowerBound)); }
/*     */       
/* 511 */       for (Type upperBound : Types.filterUpperBounds((Iterable<Type>)this.upperBounds)) {
/* 512 */         builder.append(" extends ").append(Types.JavaVersion.CURRENT.typeName(upperBound));
/*     */       }
/* 514 */       return builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Type[] toArray(Collection<Type> types) {
/* 521 */     return types.<Type>toArray(new Type[0]);
/*     */   }
/*     */   
/*     */   private static Iterable<Type> filterUpperBounds(Iterable<Type> bounds) {
/* 525 */     return Iterables.filter(bounds, Predicates.not(Predicates.equalTo(Object.class)));
/*     */   }
/*     */   
/*     */   private static void disallowPrimitiveType(Type[] types, String usedAs) {
/* 529 */     for (Type type : types) {
/* 530 */       if (type instanceof Class) {
/* 531 */         Class<?> cls = (Class)type;
/* 532 */         Preconditions.checkArgument(!cls.isPrimitive(), "Primitive type '%s' used as %s", cls, usedAs);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Class<?> getArrayClass(Class<?> componentType) {
/* 542 */     return Array.newInstance(componentType, 0).getClass();
/*     */   }
/*     */   
/*     */   enum JavaVersion
/*     */   {
/* 547 */     JAVA6
/*     */     {
/*     */       GenericArrayType newArrayType(Type componentType) {
/* 550 */         return new Types.GenericArrayTypeImpl(componentType);
/*     */       }
/*     */ 
/*     */       
/*     */       Type usedInGenericType(Type type) {
/* 555 */         Preconditions.checkNotNull(type);
/* 556 */         if (type instanceof Class) {
/* 557 */           Class<?> cls = (Class)type;
/* 558 */           if (cls.isArray()) {
/* 559 */             return new Types.GenericArrayTypeImpl(cls.getComponentType());
/*     */           }
/*     */         } 
/* 562 */         return type;
/*     */       }
/*     */     },
/* 565 */     JAVA7
/*     */     {
/*     */       Type newArrayType(Type componentType) {
/* 568 */         if (componentType instanceof Class) {
/* 569 */           return Types.getArrayClass((Class)componentType);
/*     */         }
/* 571 */         return new Types.GenericArrayTypeImpl(componentType);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       Type usedInGenericType(Type type) {
/* 577 */         return (Type)Preconditions.checkNotNull(type);
/*     */       }
/*     */     },
/* 580 */     JAVA8
/*     */     {
/*     */       Type newArrayType(Type componentType) {
/* 583 */         return JAVA7.newArrayType(componentType);
/*     */       }
/*     */ 
/*     */       
/*     */       Type usedInGenericType(Type type) {
/* 588 */         return JAVA7.usedInGenericType(type);
/*     */       }
/*     */ 
/*     */       
/*     */       String typeName(Type type) {
/*     */         try {
/* 594 */           Method getTypeName = Type.class.getMethod("getTypeName", new Class[0]);
/* 595 */           return (String)getTypeName.invoke(type, new Object[0]);
/* 596 */         } catch (NoSuchMethodException e) {
/* 597 */           throw new AssertionError("Type.getTypeName should be available in Java 8");
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 603 */         catch (InvocationTargetException e) {
/* 604 */           throw new RuntimeException(e);
/* 605 */         } catch (IllegalAccessException e) {
/* 606 */           throw new RuntimeException(e);
/*     */         } 
/*     */       }
/*     */     },
/* 610 */     JAVA9
/*     */     {
/*     */       Type newArrayType(Type componentType) {
/* 613 */         return JAVA8.newArrayType(componentType);
/*     */       }
/*     */ 
/*     */       
/*     */       Type usedInGenericType(Type type) {
/* 618 */         return JAVA8.usedInGenericType(type);
/*     */       }
/*     */ 
/*     */       
/*     */       String typeName(Type type) {
/* 623 */         return JAVA8.typeName(type);
/*     */       }
/*     */ 
/*     */       
/*     */       boolean jdkTypeDuplicatesOwnerName() {
/* 628 */         return false;
/*     */       }
/*     */     };
/*     */     
/*     */     static final JavaVersion CURRENT;
/*     */     
/*     */     static {
/* 635 */       if (AnnotatedElement.class.isAssignableFrom(TypeVariable.class)) {
/* 636 */         if ((new TypeCapture<Map.Entry<String, int[][]>>() {  }).capture()
/* 637 */           .toString()
/* 638 */           .contains("java.util.Map.java.util.Map")) {
/* 639 */           CURRENT = JAVA8;
/*     */         } else {
/* 641 */           CURRENT = JAVA9;
/*     */         } 
/* 643 */       } else if ((new TypeCapture<int[]>() {  }).capture() instanceof Class) {
/* 644 */         CURRENT = JAVA7;
/*     */       } else {
/* 646 */         CURRENT = JAVA6;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final ImmutableList<Type> usedInGenericType(Type[] types) {
/* 655 */       ImmutableList.Builder<Type> builder = ImmutableList.builder();
/* 656 */       for (Type type : types) {
/* 657 */         builder.add(usedInGenericType(type));
/*     */       }
/* 659 */       return builder.build();
/*     */     }
/*     */     
/*     */     String typeName(Type type) {
/* 663 */       return Types.toString(type);
/*     */     }
/*     */     
/*     */     boolean jdkTypeDuplicatesOwnerName() {
/* 667 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     abstract Type newArrayType(Type param1Type);
/*     */ 
/*     */ 
/*     */     
/*     */     abstract Type usedInGenericType(Type param1Type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class NativeTypeVariableEquals<X>
/*     */   {
/* 683 */     static final boolean NATIVE_TYPE_VARIABLE_ONLY = !NativeTypeVariableEquals.class.getTypeParameters()[0].equals(
/* 684 */         Types.newArtificialTypeVariable(NativeTypeVariableEquals.class, "X", new Type[0]));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/reflect/Types.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */