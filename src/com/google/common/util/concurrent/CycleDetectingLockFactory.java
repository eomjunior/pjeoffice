/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.MapMaker;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.logging.Level;
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
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public class CycleDetectingLockFactory
/*     */ {
/*     */   public enum Policies
/*     */     implements Policy
/*     */   {
/* 198 */     THROW
/*     */     {
/*     */       public void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException e) {
/* 201 */         throw e;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 210 */     WARN
/*     */     {
/*     */       public void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException e) {
/* 213 */         CycleDetectingLockFactory.logger.get().log(Level.SEVERE, "Detected potential deadlock", e);
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     DISABLED
/*     */     {
/*     */       public void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException e) {}
/*     */     };
/*     */   }
/*     */ 
/*     */   
/*     */   public static CycleDetectingLockFactory newInstance(Policy policy) {
/* 233 */     return new CycleDetectingLockFactory(policy);
/*     */   }
/*     */ 
/*     */   
/*     */   public ReentrantLock newReentrantLock(String lockName) {
/* 238 */     return newReentrantLock(lockName, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReentrantLock newReentrantLock(String lockName, boolean fair) {
/* 246 */     return (this.policy == Policies.DISABLED) ? 
/* 247 */       new ReentrantLock(fair) : 
/* 248 */       new CycleDetectingReentrantLock(new LockGraphNode(lockName), fair);
/*     */   }
/*     */ 
/*     */   
/*     */   public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName) {
/* 253 */     return newReentrantReadWriteLock(lockName, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName, boolean fair) {
/* 262 */     return (this.policy == Policies.DISABLED) ? 
/* 263 */       new ReentrantReadWriteLock(fair) : 
/* 264 */       new CycleDetectingReentrantReadWriteLock(new LockGraphNode(lockName), fair);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 270 */   private static final ConcurrentMap<Class<? extends Enum<?>>, Map<? extends Enum<?>, LockGraphNode>> lockGraphNodesPerType = (new MapMaker()).weakKeys().makeMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Enum<E>> WithExplicitOrdering<E> newInstanceWithExplicitOrdering(Class<E> enumClass, Policy policy) {
/* 277 */     Preconditions.checkNotNull(enumClass);
/* 278 */     Preconditions.checkNotNull(policy);
/*     */     
/* 280 */     Map<E, LockGraphNode> lockGraphNodes = (Map)getOrCreateNodes(enumClass);
/* 281 */     return new WithExplicitOrdering<>(policy, lockGraphNodes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E extends Enum<E>> Map<? extends E, LockGraphNode> getOrCreateNodes(Class<E> clazz) {
/* 287 */     Map<E, LockGraphNode> existing = (Map<E, LockGraphNode>)lockGraphNodesPerType.get(clazz);
/* 288 */     if (existing != null) {
/* 289 */       return existing;
/*     */     }
/* 291 */     Map<E, LockGraphNode> created = createNodes(clazz);
/* 292 */     existing = (Map<E, LockGraphNode>)lockGraphNodesPerType.putIfAbsent(clazz, created);
/* 293 */     return (Map<? extends E, LockGraphNode>)MoreObjects.firstNonNull(existing, created);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static <E extends Enum<E>> Map<E, LockGraphNode> createNodes(Class<E> clazz) {
/* 304 */     EnumMap<E, LockGraphNode> map = Maps.newEnumMap(clazz);
/* 305 */     Enum[] arrayOfEnum = (Enum[])clazz.getEnumConstants();
/* 306 */     int numKeys = arrayOfEnum.length;
/* 307 */     ArrayList<LockGraphNode> nodes = Lists.newArrayListWithCapacity(numKeys);
/*     */     
/* 309 */     for (Enum<?> enum_ : arrayOfEnum) {
/* 310 */       LockGraphNode node = new LockGraphNode(getLockName(enum_));
/* 311 */       nodes.add(node);
/* 312 */       map.put((E)enum_, node);
/*     */     } 
/*     */     int i;
/* 315 */     for (i = 1; i < numKeys; i++) {
/* 316 */       ((LockGraphNode)nodes.get(i)).checkAcquiredLocks(Policies.THROW, nodes.subList(0, i));
/*     */     }
/*     */     
/* 319 */     for (i = 0; i < numKeys - 1; i++) {
/* 320 */       ((LockGraphNode)nodes.get(i)).checkAcquiredLocks(Policies.DISABLED, nodes.subList(i + 1, numKeys));
/*     */     }
/* 322 */     return Collections.unmodifiableMap(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getLockName(Enum<?> rank) {
/* 330 */     return rank.getDeclaringClass().getSimpleName() + "." + rank.name();
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
/*     */   public static final class WithExplicitOrdering<E extends Enum<E>>
/*     */     extends CycleDetectingLockFactory
/*     */   {
/*     */     private final Map<E, CycleDetectingLockFactory.LockGraphNode> lockGraphNodes;
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
/*     */     @VisibleForTesting
/*     */     WithExplicitOrdering(CycleDetectingLockFactory.Policy policy, Map<E, CycleDetectingLockFactory.LockGraphNode> lockGraphNodes) {
/* 399 */       super(policy);
/* 400 */       this.lockGraphNodes = lockGraphNodes;
/*     */     }
/*     */ 
/*     */     
/*     */     public ReentrantLock newReentrantLock(E rank) {
/* 405 */       return newReentrantLock(rank, false);
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
/*     */     public ReentrantLock newReentrantLock(E rank, boolean fair) {
/* 417 */       return (this.policy == CycleDetectingLockFactory.Policies.DISABLED) ? 
/* 418 */         new ReentrantLock(fair) : 
/*     */ 
/*     */         
/* 421 */         new CycleDetectingLockFactory.CycleDetectingReentrantLock(Objects.<CycleDetectingLockFactory.LockGraphNode>requireNonNull(this.lockGraphNodes.get(rank)), fair);
/*     */     }
/*     */ 
/*     */     
/*     */     public ReentrantReadWriteLock newReentrantReadWriteLock(E rank) {
/* 426 */       return newReentrantReadWriteLock(rank, false);
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
/*     */     public ReentrantReadWriteLock newReentrantReadWriteLock(E rank, boolean fair) {
/* 438 */       return (this.policy == CycleDetectingLockFactory.Policies.DISABLED) ? 
/* 439 */         new ReentrantReadWriteLock(fair) : 
/*     */ 
/*     */         
/* 442 */         new CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock(
/* 443 */           Objects.<CycleDetectingLockFactory.LockGraphNode>requireNonNull(this.lockGraphNodes.get(rank)), fair);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 449 */   private static final LazyLogger logger = new LazyLogger(CycleDetectingLockFactory.class);
/*     */   
/*     */   final Policy policy;
/*     */   
/*     */   private CycleDetectingLockFactory(Policy policy) {
/* 454 */     this.policy = (Policy)Preconditions.checkNotNull(policy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 463 */   private static final ThreadLocal<ArrayList<LockGraphNode>> acquiredLocks = new ThreadLocal<ArrayList<LockGraphNode>>()
/*     */     {
/*     */       protected ArrayList<CycleDetectingLockFactory.LockGraphNode> initialValue()
/*     */       {
/* 467 */         return Lists.newArrayListWithCapacity(3);
/*     */       }
/*     */     };
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
/*     */   private static class ExampleStackTrace
/*     */     extends IllegalStateException
/*     */   {
/* 486 */     static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
/*     */ 
/*     */     
/* 489 */     static final ImmutableSet<String> EXCLUDED_CLASS_NAMES = ImmutableSet.of(CycleDetectingLockFactory.class
/* 490 */         .getName(), ExampleStackTrace.class
/* 491 */         .getName(), CycleDetectingLockFactory.LockGraphNode.class
/* 492 */         .getName());
/*     */     
/*     */     ExampleStackTrace(CycleDetectingLockFactory.LockGraphNode node1, CycleDetectingLockFactory.LockGraphNode node2) {
/* 495 */       super(node1.getLockName() + " -> " + node2.getLockName());
/* 496 */       StackTraceElement[] origStackTrace = getStackTrace();
/* 497 */       for (int i = 0, n = origStackTrace.length; i < n; i++) {
/* 498 */         if (CycleDetectingLockFactory.WithExplicitOrdering.class.getName().equals(origStackTrace[i].getClassName())) {
/*     */           
/* 500 */           setStackTrace(EMPTY_STACK_TRACE);
/*     */           break;
/*     */         } 
/* 503 */         if (!EXCLUDED_CLASS_NAMES.contains(origStackTrace[i].getClassName())) {
/* 504 */           setStackTrace(Arrays.<StackTraceElement>copyOfRange(origStackTrace, i, n));
/*     */           break;
/*     */         } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class PotentialDeadlockException
/*     */     extends ExampleStackTrace
/*     */   {
/*     */     private final CycleDetectingLockFactory.ExampleStackTrace conflictingStackTrace;
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
/*     */     private PotentialDeadlockException(CycleDetectingLockFactory.LockGraphNode node1, CycleDetectingLockFactory.LockGraphNode node2, CycleDetectingLockFactory.ExampleStackTrace conflictingStackTrace) {
/* 537 */       super(node1, node2);
/* 538 */       this.conflictingStackTrace = conflictingStackTrace;
/* 539 */       initCause(conflictingStackTrace);
/*     */     }
/*     */     
/*     */     public CycleDetectingLockFactory.ExampleStackTrace getConflictingStackTrace() {
/* 543 */       return this.conflictingStackTrace;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getMessage() {
/* 553 */       StringBuilder message = new StringBuilder(Objects.<String>requireNonNull(super.getMessage()));
/* 554 */       for (Throwable t = this.conflictingStackTrace; t != null; t = t.getCause()) {
/* 555 */         message.append(", ").append(t.getMessage());
/*     */       }
/* 557 */       return message.toString();
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
/*     */   private static class LockGraphNode
/*     */   {
/* 585 */     final Map<LockGraphNode, CycleDetectingLockFactory.ExampleStackTrace> allowedPriorLocks = (new MapMaker())
/* 586 */       .weakKeys().makeMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 592 */     final Map<LockGraphNode, CycleDetectingLockFactory.PotentialDeadlockException> disallowedPriorLocks = (new MapMaker())
/* 593 */       .weakKeys().makeMap();
/*     */     
/*     */     final String lockName;
/*     */     
/*     */     LockGraphNode(String lockName) {
/* 598 */       this.lockName = (String)Preconditions.checkNotNull(lockName);
/*     */     }
/*     */     
/*     */     String getLockName() {
/* 602 */       return this.lockName;
/*     */     }
/*     */     
/*     */     void checkAcquiredLocks(CycleDetectingLockFactory.Policy policy, List<LockGraphNode> acquiredLocks) {
/* 606 */       for (LockGraphNode acquiredLock : acquiredLocks) {
/* 607 */         checkAcquiredLock(policy, acquiredLock);
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void checkAcquiredLock(CycleDetectingLockFactory.Policy policy, LockGraphNode acquiredLock) {
/* 627 */       Preconditions.checkState((this != acquiredLock), "Attempted to acquire multiple locks with the same rank %s", acquiredLock
/*     */ 
/*     */           
/* 630 */           .getLockName());
/*     */       
/* 632 */       if (this.allowedPriorLocks.containsKey(acquiredLock)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 638 */       CycleDetectingLockFactory.PotentialDeadlockException previousDeadlockException = this.disallowedPriorLocks.get(acquiredLock);
/* 639 */       if (previousDeadlockException != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 645 */         CycleDetectingLockFactory.PotentialDeadlockException exception = new CycleDetectingLockFactory.PotentialDeadlockException(acquiredLock, this, previousDeadlockException.getConflictingStackTrace());
/* 646 */         policy.handlePotentialDeadlock(exception);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 651 */       Set<LockGraphNode> seen = Sets.newIdentityHashSet();
/* 652 */       CycleDetectingLockFactory.ExampleStackTrace path = acquiredLock.findPathTo(this, seen);
/*     */       
/* 654 */       if (path == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 663 */         this.allowedPriorLocks.put(acquiredLock, new CycleDetectingLockFactory.ExampleStackTrace(acquiredLock, this));
/*     */       }
/*     */       else {
/*     */         
/* 667 */         CycleDetectingLockFactory.PotentialDeadlockException exception = new CycleDetectingLockFactory.PotentialDeadlockException(acquiredLock, this, path);
/*     */         
/* 669 */         this.disallowedPriorLocks.put(acquiredLock, exception);
/* 670 */         policy.handlePotentialDeadlock(exception);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CheckForNull
/*     */     private CycleDetectingLockFactory.ExampleStackTrace findPathTo(LockGraphNode node, Set<LockGraphNode> seen) {
/* 683 */       if (!seen.add(this)) {
/* 684 */         return null;
/*     */       }
/* 686 */       CycleDetectingLockFactory.ExampleStackTrace found = this.allowedPriorLocks.get(node);
/* 687 */       if (found != null) {
/* 688 */         return found;
/*     */       }
/*     */       
/* 691 */       for (Map.Entry<LockGraphNode, CycleDetectingLockFactory.ExampleStackTrace> entry : this.allowedPriorLocks.entrySet()) {
/* 692 */         LockGraphNode preAcquiredLock = entry.getKey();
/* 693 */         found = preAcquiredLock.findPathTo(node, seen);
/* 694 */         if (found != null) {
/*     */ 
/*     */ 
/*     */           
/* 698 */           CycleDetectingLockFactory.ExampleStackTrace path = new CycleDetectingLockFactory.ExampleStackTrace(preAcquiredLock, this);
/* 699 */           path.setStackTrace(((CycleDetectingLockFactory.ExampleStackTrace)entry.getValue()).getStackTrace());
/* 700 */           path.initCause(found);
/* 701 */           return path;
/*     */         } 
/*     */       } 
/* 704 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void aboutToAcquire(CycleDetectingLock lock) {
/* 712 */     if (!lock.isAcquiredByCurrentThread()) {
/*     */       
/* 714 */       ArrayList<LockGraphNode> acquiredLockList = Objects.<ArrayList<LockGraphNode>>requireNonNull(acquiredLocks.get());
/* 715 */       LockGraphNode node = lock.getLockGraphNode();
/* 716 */       node.checkAcquiredLocks(this.policy, acquiredLockList);
/* 717 */       acquiredLockList.add(node);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void lockStateChanged(CycleDetectingLock lock) {
/* 727 */     if (!lock.isAcquiredByCurrentThread()) {
/*     */       
/* 729 */       ArrayList<LockGraphNode> acquiredLockList = Objects.<ArrayList<LockGraphNode>>requireNonNull(acquiredLocks.get());
/* 730 */       LockGraphNode node = lock.getLockGraphNode();
/*     */ 
/*     */       
/* 733 */       for (int i = acquiredLockList.size() - 1; i >= 0; i--) {
/* 734 */         if (acquiredLockList.get(i) == node) {
/* 735 */           acquiredLockList.remove(i);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   final class CycleDetectingReentrantLock
/*     */     extends ReentrantLock implements CycleDetectingLock {
/*     */     private final CycleDetectingLockFactory.LockGraphNode lockGraphNode;
/*     */     
/*     */     private CycleDetectingReentrantLock(CycleDetectingLockFactory.LockGraphNode lockGraphNode, boolean fair) {
/* 747 */       super(fair);
/* 748 */       this.lockGraphNode = (CycleDetectingLockFactory.LockGraphNode)Preconditions.checkNotNull(lockGraphNode);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CycleDetectingLockFactory.LockGraphNode getLockGraphNode() {
/* 755 */       return this.lockGraphNode;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAcquiredByCurrentThread() {
/* 760 */       return isHeldByCurrentThread();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void lock() {
/* 767 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*     */       try {
/* 769 */         super.lock();
/*     */       } finally {
/* 771 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void lockInterruptibly() throws InterruptedException {
/* 777 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*     */       try {
/* 779 */         super.lockInterruptibly();
/*     */       } finally {
/* 781 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock() {
/* 787 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*     */       try {
/* 789 */         return super.tryLock();
/*     */       } finally {
/* 791 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
/* 797 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*     */       try {
/* 799 */         return super.tryLock(timeout, unit);
/*     */       } finally {
/* 801 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void unlock() {
/*     */       try {
/* 808 */         super.unlock();
/*     */       } finally {
/* 810 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   final class CycleDetectingReentrantReadWriteLock
/*     */     extends ReentrantReadWriteLock
/*     */     implements CycleDetectingLock
/*     */   {
/*     */     private final CycleDetectingLockFactory.CycleDetectingReentrantReadLock readLock;
/*     */     
/*     */     private final CycleDetectingLockFactory.CycleDetectingReentrantWriteLock writeLock;
/*     */     
/*     */     private final CycleDetectingLockFactory.LockGraphNode lockGraphNode;
/*     */ 
/*     */     
/*     */     private CycleDetectingReentrantReadWriteLock(CycleDetectingLockFactory this$0, CycleDetectingLockFactory.LockGraphNode lockGraphNode, boolean fair) {
/* 828 */       super(fair);
/* 829 */       this.readLock = new CycleDetectingLockFactory.CycleDetectingReentrantReadLock(this);
/* 830 */       this.writeLock = new CycleDetectingLockFactory.CycleDetectingReentrantWriteLock(this);
/* 831 */       this.lockGraphNode = (CycleDetectingLockFactory.LockGraphNode)Preconditions.checkNotNull(lockGraphNode);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ReentrantReadWriteLock.ReadLock readLock() {
/* 838 */       return this.readLock;
/*     */     }
/*     */ 
/*     */     
/*     */     public ReentrantReadWriteLock.WriteLock writeLock() {
/* 843 */       return this.writeLock;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public CycleDetectingLockFactory.LockGraphNode getLockGraphNode() {
/* 850 */       return this.lockGraphNode;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAcquiredByCurrentThread() {
/* 855 */       return (isWriteLockedByCurrentThread() || getReadHoldCount() > 0);
/*     */     }
/*     */   }
/*     */   
/*     */   private class CycleDetectingReentrantReadLock
/*     */     extends ReentrantReadWriteLock.ReadLock {
/*     */     @Weak
/*     */     final CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock;
/*     */     
/*     */     CycleDetectingReentrantReadLock(CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock) {
/* 865 */       this.readWriteLock = readWriteLock;
/*     */     }
/*     */ 
/*     */     
/*     */     public void lock() {
/* 870 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 872 */         super.lock();
/*     */       } finally {
/* 874 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void lockInterruptibly() throws InterruptedException {
/* 880 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 882 */         super.lockInterruptibly();
/*     */       } finally {
/* 884 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock() {
/* 890 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 892 */         return super.tryLock();
/*     */       } finally {
/* 894 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
/* 900 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 902 */         return super.tryLock(timeout, unit);
/*     */       } finally {
/* 904 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void unlock() {
/*     */       try {
/* 911 */         super.unlock();
/*     */       } finally {
/* 913 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class CycleDetectingReentrantWriteLock
/*     */     extends ReentrantReadWriteLock.WriteLock {
/*     */     @Weak
/*     */     final CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock;
/*     */     
/*     */     CycleDetectingReentrantWriteLock(CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock) {
/* 924 */       this.readWriteLock = readWriteLock;
/*     */     }
/*     */ 
/*     */     
/*     */     public void lock() {
/* 929 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 931 */         super.lock();
/*     */       } finally {
/* 933 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void lockInterruptibly() throws InterruptedException {
/* 939 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 941 */         super.lockInterruptibly();
/*     */       } finally {
/* 943 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock() {
/* 949 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 951 */         return super.tryLock();
/*     */       } finally {
/* 953 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
/* 959 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 961 */         return super.tryLock(timeout, unit);
/*     */       } finally {
/* 963 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void unlock() {
/*     */       try {
/* 970 */         super.unlock();
/*     */       } finally {
/* 972 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static interface CycleDetectingLock {
/*     */     CycleDetectingLockFactory.LockGraphNode getLockGraphNode();
/*     */     
/*     */     boolean isAcquiredByCurrentThread();
/*     */   }
/*     */   
/*     */   public static interface Policy {
/*     */     void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException param1PotentialDeadlockException);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/CycleDetectingLockFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */