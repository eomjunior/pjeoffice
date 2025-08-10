/*     */ package com.google.common.eventbus;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.cache.CacheBuilder;
/*     */ import com.google.common.cache.CacheLoader;
/*     */ import com.google.common.cache.LoadingCache;
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.primitives.Primitives;
/*     */ import com.google.common.reflect.TypeToken;
/*     */ import com.google.common.util.concurrent.UncheckedExecutionException;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.CopyOnWriteArraySet;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ final class SubscriberRegistry
/*     */ {
/*  66 */   private final ConcurrentMap<Class<?>, CopyOnWriteArraySet<Subscriber>> subscribers = Maps.newConcurrentMap();
/*     */   
/*     */   @Weak
/*     */   private final EventBus bus;
/*     */   
/*     */   SubscriberRegistry(EventBus bus) {
/*  72 */     this.bus = (EventBus)Preconditions.checkNotNull(bus);
/*     */   }
/*     */ 
/*     */   
/*     */   void register(Object listener) {
/*  77 */     Multimap<Class<?>, Subscriber> listenerMethods = findAllSubscribers(listener);
/*     */     
/*  79 */     for (Map.Entry<Class<?>, Collection<Subscriber>> entry : (Iterable<Map.Entry<Class<?>, Collection<Subscriber>>>)listenerMethods.asMap().entrySet()) {
/*  80 */       Class<?> eventType = entry.getKey();
/*  81 */       Collection<Subscriber> eventMethodsInListener = entry.getValue();
/*     */       
/*  83 */       CopyOnWriteArraySet<Subscriber> eventSubscribers = this.subscribers.get(eventType);
/*     */       
/*  85 */       if (eventSubscribers == null) {
/*  86 */         CopyOnWriteArraySet<Subscriber> newSet = new CopyOnWriteArraySet<>();
/*     */         
/*  88 */         eventSubscribers = (CopyOnWriteArraySet<Subscriber>)MoreObjects.firstNonNull(this.subscribers.putIfAbsent(eventType, newSet), newSet);
/*     */       } 
/*     */       
/*  91 */       eventSubscribers.addAll(eventMethodsInListener);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void unregister(Object listener) {
/*  97 */     Multimap<Class<?>, Subscriber> listenerMethods = findAllSubscribers(listener);
/*     */     
/*  99 */     for (Map.Entry<Class<?>, Collection<Subscriber>> entry : (Iterable<Map.Entry<Class<?>, Collection<Subscriber>>>)listenerMethods.asMap().entrySet()) {
/* 100 */       Class<?> eventType = entry.getKey();
/* 101 */       Collection<Subscriber> listenerMethodsForType = entry.getValue();
/*     */       
/* 103 */       CopyOnWriteArraySet<Subscriber> currentSubscribers = this.subscribers.get(eventType);
/* 104 */       if (currentSubscribers == null || !currentSubscribers.removeAll(listenerMethodsForType))
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 109 */         throw new IllegalArgumentException("missing event subscriber for an annotated method. Is " + listener + " registered?");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   Set<Subscriber> getSubscribersForTesting(Class<?> eventType) {
/* 120 */     return (Set<Subscriber>)MoreObjects.firstNonNull(this.subscribers.get(eventType), ImmutableSet.of());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<Subscriber> getSubscribers(Object event) {
/* 128 */     ImmutableSet<Class<?>> eventTypes = flattenHierarchy(event.getClass());
/*     */ 
/*     */     
/* 131 */     List<Iterator<Subscriber>> subscriberIterators = Lists.newArrayListWithCapacity(eventTypes.size());
/*     */     
/* 133 */     for (UnmodifiableIterator<Class<?>> unmodifiableIterator = eventTypes.iterator(); unmodifiableIterator.hasNext(); ) { Class<?> eventType = unmodifiableIterator.next();
/* 134 */       CopyOnWriteArraySet<Subscriber> eventSubscribers = this.subscribers.get(eventType);
/* 135 */       if (eventSubscribers != null)
/*     */       {
/* 137 */         subscriberIterators.add(eventSubscribers.iterator());
/*     */       } }
/*     */ 
/*     */     
/* 141 */     return Iterators.concat(subscriberIterators.iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   private static final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache = CacheBuilder.newBuilder()
/* 152 */     .weakKeys()
/* 153 */     .build(new CacheLoader<Class<?>, ImmutableList<Method>>()
/*     */       {
/*     */         public ImmutableList<Method> load(Class<?> concreteClass) throws Exception
/*     */         {
/* 157 */           return SubscriberRegistry.getAnnotatedMethodsNotCached(concreteClass);
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Multimap<Class<?>, Subscriber> findAllSubscribers(Object listener) {
/* 165 */     HashMultimap hashMultimap = HashMultimap.create();
/* 166 */     Class<?> clazz = listener.getClass();
/* 167 */     for (UnmodifiableIterator<Method> unmodifiableIterator = getAnnotatedMethods(clazz).iterator(); unmodifiableIterator.hasNext(); ) { Method method = unmodifiableIterator.next();
/* 168 */       Class<?>[] parameterTypes = method.getParameterTypes();
/* 169 */       Class<?> eventType = parameterTypes[0];
/* 170 */       hashMultimap.put(eventType, Subscriber.create(this.bus, listener, method)); }
/*     */     
/* 172 */     return (Multimap<Class<?>, Subscriber>)hashMultimap;
/*     */   }
/*     */   
/*     */   private static ImmutableList<Method> getAnnotatedMethods(Class<?> clazz) {
/*     */     try {
/* 177 */       return (ImmutableList<Method>)subscriberMethodsCache.getUnchecked(clazz);
/* 178 */     } catch (UncheckedExecutionException e) {
/* 179 */       Throwables.throwIfUnchecked(e.getCause());
/* 180 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static ImmutableList<Method> getAnnotatedMethodsNotCached(Class<?> clazz) {
/* 185 */     Set<? extends Class<?>> supertypes = TypeToken.of(clazz).getTypes().rawTypes();
/* 186 */     Map<MethodIdentifier, Method> identifiers = Maps.newHashMap();
/* 187 */     for (Class<?> supertype : supertypes) {
/* 188 */       for (Method method : supertype.getDeclaredMethods()) {
/* 189 */         if (method.isAnnotationPresent((Class)Subscribe.class) && !method.isSynthetic()) {
/*     */           
/* 191 */           Class<?>[] parameterTypes = method.getParameterTypes();
/* 192 */           Preconditions.checkArgument((parameterTypes.length == 1), "Method %s has @Subscribe annotation but has %s parameters. Subscriber methods must have exactly 1 parameter.", method, parameterTypes.length);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 199 */           Preconditions.checkArgument(
/* 200 */               !parameterTypes[0].isPrimitive(), "@Subscribe method %s's parameter is %s. Subscriber methods cannot accept primitives. Consider changing the parameter to %s.", method, parameterTypes[0]
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 205 */               .getName(), 
/* 206 */               Primitives.wrap(parameterTypes[0]).getSimpleName());
/*     */           
/* 208 */           MethodIdentifier ident = new MethodIdentifier(method);
/* 209 */           if (!identifiers.containsKey(ident)) {
/* 210 */             identifiers.put(ident, method);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 215 */     return ImmutableList.copyOf(identifiers.values());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 220 */   private static final LoadingCache<Class<?>, ImmutableSet<Class<?>>> flattenHierarchyCache = CacheBuilder.newBuilder()
/* 221 */     .weakKeys()
/* 222 */     .build(new CacheLoader<Class<?>, ImmutableSet<Class<?>>>()
/*     */       {
/*     */ 
/*     */         
/*     */         public ImmutableSet<Class<?>> load(Class<?> concreteClass)
/*     */         {
/* 228 */           return ImmutableSet.copyOf(
/* 229 */               TypeToken.of(concreteClass).getTypes().rawTypes());
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static ImmutableSet<Class<?>> flattenHierarchy(Class<?> concreteClass) {
/*     */     try {
/* 240 */       return (ImmutableSet<Class<?>>)flattenHierarchyCache.getUnchecked(concreteClass);
/* 241 */     } catch (UncheckedExecutionException e) {
/* 242 */       throw Throwables.propagate(e.getCause());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static final class MethodIdentifier
/*     */   {
/*     */     private final String name;
/*     */     private final List<Class<?>> parameterTypes;
/*     */     
/*     */     MethodIdentifier(Method method) {
/* 252 */       this.name = method.getName();
/* 253 */       this.parameterTypes = Arrays.asList(method.getParameterTypes());
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 258 */       return Objects.hashCode(new Object[] { this.name, this.parameterTypes });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object o) {
/* 263 */       if (o instanceof MethodIdentifier) {
/* 264 */         MethodIdentifier ident = (MethodIdentifier)o;
/* 265 */         return (this.name.equals(ident.name) && this.parameterTypes.equals(ident.parameterTypes));
/*     */       } 
/* 267 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/eventbus/SubscriberRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */