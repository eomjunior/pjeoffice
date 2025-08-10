/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.common.collect.Collections2;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableMultimap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.ImmutableSetMultimap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.MultimapBuilder;
/*     */ import com.google.common.collect.Multimaps;
/*     */ import com.google.common.collect.Multiset;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.google.common.collect.SetMultimap;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.time.Duration;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.logging.Level;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class ServiceManager
/*     */   implements ServiceManagerBridge
/*     */ {
/* 127 */   private static final LazyLogger logger = new LazyLogger(ServiceManager.class);
/* 128 */   private static final ListenerCallQueue.Event<Listener> HEALTHY_EVENT = new ListenerCallQueue.Event<Listener>()
/*     */     {
/*     */       public void call(ServiceManager.Listener listener)
/*     */       {
/* 132 */         listener.healthy();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 137 */         return "healthy()";
/*     */       }
/*     */     };
/* 140 */   private static final ListenerCallQueue.Event<Listener> STOPPED_EVENT = new ListenerCallQueue.Event<Listener>()
/*     */     {
/*     */       public void call(ServiceManager.Listener listener)
/*     */       {
/* 144 */         listener.stopped();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 149 */         return "stopped()";
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
/*     */   private final ServiceManagerState state;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ImmutableList<Service> services;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class Listener
/*     */   {
/*     */     public void healthy() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void stopped() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void failure(Service service) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServiceManager(Iterable<? extends Service> services) {
/* 206 */     ImmutableList<Service> copy = ImmutableList.copyOf(services);
/* 207 */     if (copy.isEmpty()) {
/*     */ 
/*     */       
/* 210 */       logger
/* 211 */         .get()
/* 212 */         .log(Level.WARNING, "ServiceManager configured with no services.  Is your application configured properly?", new EmptyServiceManagerWarning());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 217 */       copy = ImmutableList.of(new NoOpService());
/*     */     } 
/* 219 */     this.state = new ServiceManagerState((ImmutableCollection<Service>)copy);
/* 220 */     this.services = copy;
/* 221 */     WeakReference<ServiceManagerState> stateReference = new WeakReference<>(this.state);
/* 222 */     for (UnmodifiableIterator<Service> unmodifiableIterator = copy.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 223 */       service.addListener(new ServiceListener(service, stateReference), MoreExecutors.directExecutor());
/*     */ 
/*     */       
/* 226 */       Preconditions.checkArgument((service.state() == Service.State.NEW), "Can only manage NEW services, %s", service); }
/*     */ 
/*     */ 
/*     */     
/* 230 */     this.state.markReady();
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
/*     */   public void addListener(Listener listener, Executor executor) {
/* 258 */     this.state.addListener(listener, executor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public ServiceManager startAsync() {
/*     */     UnmodifiableIterator<Service> unmodifiableIterator;
/* 271 */     for (unmodifiableIterator = this.services.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 272 */       Preconditions.checkState((service.state() == Service.State.NEW), "Not all services are NEW, cannot start %s", this); }
/*     */     
/* 274 */     for (unmodifiableIterator = this.services.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/*     */       try {
/* 276 */         this.state.tryStartTiming(service);
/* 277 */         service.startAsync();
/* 278 */       } catch (IllegalStateException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 283 */         logger.get().log(Level.WARNING, "Unable to start Service " + service, e);
/*     */       }  }
/*     */     
/* 286 */     return this;
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
/*     */   public void awaitHealthy() {
/* 298 */     this.state.awaitHealthy();
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
/*     */   public void awaitHealthy(Duration timeout) throws TimeoutException {
/* 313 */     awaitHealthy(Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*     */   public void awaitHealthy(long timeout, TimeUnit unit) throws TimeoutException {
/* 329 */     this.state.awaitHealthy(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public ServiceManager stopAsync() {
/* 340 */     for (UnmodifiableIterator<Service> unmodifiableIterator = this.services.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 341 */       service.stopAsync(); }
/*     */     
/* 343 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void awaitStopped() {
/* 352 */     this.state.awaitStopped();
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
/*     */   public void awaitStopped(Duration timeout) throws TimeoutException {
/* 365 */     awaitStopped(Internal.toNanosSaturated(timeout), TimeUnit.NANOSECONDS);
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
/*     */   public void awaitStopped(long timeout, TimeUnit unit) throws TimeoutException {
/* 379 */     this.state.awaitStopped(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHealthy() {
/* 389 */     for (UnmodifiableIterator<Service> unmodifiableIterator = this.services.iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 390 */       if (!service.isRunning()) {
/* 391 */         return false;
/*     */       } }
/*     */     
/* 394 */     return true;
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
/*     */   public ImmutableSetMultimap<Service.State, Service> servicesByState() {
/* 407 */     return this.state.servicesByState();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<Service, Long> startupTimes() {
/* 418 */     return this.state.startupTimes();
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
/*     */   public ImmutableMap<Service, Duration> startupDurations() {
/* 431 */     return ImmutableMap.copyOf(
/* 432 */         Maps.transformValues((Map)startupTimes(), Duration::ofMillis));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 437 */     return MoreObjects.toStringHelper(ServiceManager.class)
/* 438 */       .add("services", Collections2.filter((Collection)this.services, Predicates.not(Predicates.instanceOf(NoOpService.class))))
/* 439 */       .toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ServiceManagerState
/*     */   {
/* 447 */     final Monitor monitor = new Monitor();
/*     */ 
/*     */     
/*     */     @GuardedBy("monitor")
/* 451 */     final SetMultimap<Service.State, Service> servicesByState = MultimapBuilder.enumKeys(Service.State.class).linkedHashSetValues().build();
/*     */     @GuardedBy("monitor")
/* 453 */     final Multiset<Service.State> states = this.servicesByState
/* 454 */       .keys();
/*     */     
/*     */     @GuardedBy("monitor")
/* 457 */     final Map<Service, Stopwatch> startupTimers = Maps.newIdentityHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GuardedBy("monitor")
/*     */     boolean ready;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GuardedBy("monitor")
/*     */     boolean transitioned;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final int numberOfServices;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 484 */     final Monitor.Guard awaitHealthGuard = new AwaitHealthGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final class AwaitHealthGuard
/*     */       extends Monitor.Guard
/*     */     {
/*     */       @GuardedBy("ServiceManagerState.this.monitor")
/*     */       public boolean isSatisfied() {
/* 496 */         return (ServiceManager.ServiceManagerState.this.states.count(Service.State.RUNNING) == ServiceManager.ServiceManagerState.this.numberOfServices || ServiceManager.ServiceManagerState.this.states
/* 497 */           .contains(Service.State.STOPPING) || ServiceManager.ServiceManagerState.this.states
/* 498 */           .contains(Service.State.TERMINATED) || ServiceManager.ServiceManagerState.this.states
/* 499 */           .contains(Service.State.FAILED));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 504 */     final Monitor.Guard stoppedGuard = new StoppedGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final class StoppedGuard
/*     */       extends Monitor.Guard
/*     */     {
/*     */       @GuardedBy("ServiceManagerState.this.monitor")
/*     */       public boolean isSatisfied() {
/* 515 */         return (ServiceManager.ServiceManagerState.this.states.count(Service.State.TERMINATED) + ServiceManager.ServiceManagerState.this.states.count(Service.State.FAILED) == ServiceManager.ServiceManagerState.this.numberOfServices);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 520 */     final ListenerCallQueue<ServiceManager.Listener> listeners = new ListenerCallQueue<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ServiceManagerState(ImmutableCollection<Service> services) {
/* 529 */       this.numberOfServices = services.size();
/* 530 */       this.servicesByState.putAll(Service.State.NEW, (Iterable)services);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void tryStartTiming(Service service) {
/* 538 */       this.monitor.enter();
/*     */       try {
/* 540 */         Stopwatch stopwatch = this.startupTimers.get(service);
/* 541 */         if (stopwatch == null) {
/* 542 */           this.startupTimers.put(service, Stopwatch.createStarted());
/*     */         }
/*     */       } finally {
/* 545 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void markReady() {
/* 554 */       this.monitor.enter();
/*     */       try {
/* 556 */         if (!this.transitioned) {
/*     */           
/* 558 */           this.ready = true;
/*     */         } else {
/*     */           
/* 561 */           List<Service> servicesInBadStates = Lists.newArrayList();
/* 562 */           for (UnmodifiableIterator<Service> unmodifiableIterator = servicesByState().values().iterator(); unmodifiableIterator.hasNext(); ) { Service service = unmodifiableIterator.next();
/* 563 */             if (service.state() != Service.State.NEW) {
/* 564 */               servicesInBadStates.add(service);
/*     */             } }
/*     */           
/* 567 */           throw new IllegalArgumentException("Services started transitioning asynchronously before the ServiceManager was constructed: " + servicesInBadStates);
/*     */         }
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 573 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     void addListener(ServiceManager.Listener listener, Executor executor) {
/* 578 */       this.listeners.addListener(listener, executor);
/*     */     }
/*     */     
/*     */     void awaitHealthy() {
/* 582 */       this.monitor.enterWhenUninterruptibly(this.awaitHealthGuard);
/*     */       try {
/* 584 */         checkHealthy();
/*     */       } finally {
/* 586 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     void awaitHealthy(long timeout, TimeUnit unit) throws TimeoutException {
/* 591 */       this.monitor.enter();
/*     */       try {
/* 593 */         if (!this.monitor.waitForUninterruptibly(this.awaitHealthGuard, timeout, unit)) {
/* 594 */           throw new TimeoutException("Timeout waiting for the services to become healthy. The following services have not started: " + 
/*     */ 
/*     */               
/* 597 */               Multimaps.filterKeys(this.servicesByState, Predicates.in(ImmutableSet.of(Service.State.NEW, Service.State.STARTING))));
/*     */         }
/* 599 */         checkHealthy();
/*     */       } finally {
/* 601 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     void awaitStopped() {
/* 606 */       this.monitor.enterWhenUninterruptibly(this.stoppedGuard);
/* 607 */       this.monitor.leave();
/*     */     }
/*     */     
/*     */     void awaitStopped(long timeout, TimeUnit unit) throws TimeoutException {
/* 611 */       this.monitor.enter();
/*     */       try {
/* 613 */         if (!this.monitor.waitForUninterruptibly(this.stoppedGuard, timeout, unit)) {
/* 614 */           throw new TimeoutException("Timeout waiting for the services to stop. The following services have not stopped: " + 
/*     */ 
/*     */               
/* 617 */               Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.in(EnumSet.of(Service.State.TERMINATED, Service.State.FAILED)))));
/*     */         }
/*     */       } finally {
/* 620 */         this.monitor.leave();
/*     */       } 
/*     */     }
/*     */     
/*     */     ImmutableSetMultimap<Service.State, Service> servicesByState() {
/* 625 */       ImmutableSetMultimap.Builder<Service.State, Service> builder = ImmutableSetMultimap.builder();
/* 626 */       this.monitor.enter();
/*     */       try {
/* 628 */         for (Map.Entry<Service.State, Service> entry : (Iterable<Map.Entry<Service.State, Service>>)this.servicesByState.entries()) {
/* 629 */           if (!(entry.getValue() instanceof ServiceManager.NoOpService)) {
/* 630 */             builder.put(entry);
/*     */           }
/*     */         } 
/*     */       } finally {
/* 634 */         this.monitor.leave();
/*     */       } 
/* 636 */       return builder.build();
/*     */     }
/*     */     
/*     */     ImmutableMap<Service, Long> startupTimes() {
/*     */       List<Map.Entry<Service, Long>> loadTimes;
/* 641 */       this.monitor.enter();
/*     */       try {
/* 643 */         loadTimes = Lists.newArrayListWithCapacity(this.startupTimers.size());
/*     */         
/* 645 */         for (Map.Entry<Service, Stopwatch> entry : this.startupTimers.entrySet()) {
/* 646 */           Service service = entry.getKey();
/* 647 */           Stopwatch stopwatch = entry.getValue();
/* 648 */           if (!stopwatch.isRunning() && !(service instanceof ServiceManager.NoOpService)) {
/* 649 */             loadTimes.add(Maps.immutableEntry(service, Long.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS))));
/*     */           }
/*     */         } 
/*     */       } finally {
/* 653 */         this.monitor.leave();
/*     */       } 
/* 655 */       Collections.sort(loadTimes, 
/*     */           
/* 657 */           (Comparator<? super Map.Entry<Service, Long>>)Ordering.natural()
/* 658 */           .onResultOf(new Function<Map.Entry<Service, Long>, Long>(this)
/*     */             {
/*     */               public Long apply(Map.Entry<Service, Long> input)
/*     */               {
/* 662 */                 return input.getValue();
/*     */               }
/*     */             }));
/* 665 */       return ImmutableMap.copyOf(loadTimes);
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
/*     */     void transitionService(Service service, Service.State from, Service.State to) {
/* 681 */       Preconditions.checkNotNull(service);
/* 682 */       Preconditions.checkArgument((from != to));
/* 683 */       this.monitor.enter();
/*     */       try {
/* 685 */         this.transitioned = true;
/* 686 */         if (!this.ready) {
/*     */           return;
/*     */         }
/*     */         
/* 690 */         Preconditions.checkState(this.servicesByState
/* 691 */             .remove(from, service), "Service %s not at the expected location in the state map %s", service, from);
/*     */ 
/*     */ 
/*     */         
/* 695 */         Preconditions.checkState(this.servicesByState
/* 696 */             .put(to, service), "Service %s in the state map unexpectedly at %s", service, to);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 701 */         Stopwatch stopwatch = this.startupTimers.get(service);
/* 702 */         if (stopwatch == null) {
/*     */           
/* 704 */           stopwatch = Stopwatch.createStarted();
/* 705 */           this.startupTimers.put(service, stopwatch);
/*     */         } 
/* 707 */         if (to.compareTo(Service.State.RUNNING) >= 0 && stopwatch.isRunning()) {
/*     */           
/* 709 */           stopwatch.stop();
/* 710 */           if (!(service instanceof ServiceManager.NoOpService)) {
/* 711 */             ServiceManager.logger.get().log(Level.FINE, "Started {0} in {1}.", new Object[] { service, stopwatch });
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 717 */         if (to == Service.State.FAILED) {
/* 718 */           enqueueFailedEvent(service);
/*     */         }
/*     */         
/* 721 */         if (this.states.count(Service.State.RUNNING) == this.numberOfServices) {
/*     */ 
/*     */           
/* 724 */           enqueueHealthyEvent();
/* 725 */         } else if (this.states.count(Service.State.TERMINATED) + this.states.count(Service.State.FAILED) == this.numberOfServices) {
/* 726 */           enqueueStoppedEvent();
/*     */         } 
/*     */       } finally {
/* 729 */         this.monitor.leave();
/*     */         
/* 731 */         dispatchListenerEvents();
/*     */       } 
/*     */     }
/*     */     
/*     */     void enqueueStoppedEvent() {
/* 736 */       this.listeners.enqueue(ServiceManager.STOPPED_EVENT);
/*     */     }
/*     */     
/*     */     void enqueueHealthyEvent() {
/* 740 */       this.listeners.enqueue(ServiceManager.HEALTHY_EVENT);
/*     */     }
/*     */     
/*     */     void enqueueFailedEvent(final Service service) {
/* 744 */       this.listeners.enqueue(new ListenerCallQueue.Event<ServiceManager.Listener>(this)
/*     */           {
/*     */             public void call(ServiceManager.Listener listener)
/*     */             {
/* 748 */               listener.failure(service);
/*     */             }
/*     */ 
/*     */             
/*     */             public String toString() {
/* 753 */               return "failed({service=" + service + "})";
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     void dispatchListenerEvents() {
/* 760 */       Preconditions.checkState(
/* 761 */           !this.monitor.isOccupiedByCurrentThread(), "It is incorrect to execute listeners with the monitor held.");
/*     */       
/* 763 */       this.listeners.dispatch();
/*     */     }
/*     */     
/*     */     @GuardedBy("monitor")
/*     */     void checkHealthy() {
/* 768 */       if (this.states.count(Service.State.RUNNING) != this.numberOfServices) {
/*     */ 
/*     */ 
/*     */         
/* 772 */         IllegalStateException exception = new IllegalStateException("Expected to be healthy after starting. The following services are not running: " + Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.equalTo(Service.State.RUNNING))));
/* 773 */         for (Service service : this.servicesByState.get(Service.State.FAILED)) {
/* 774 */           exception.addSuppressed(new ServiceManager.FailedService(service));
/*     */         }
/* 776 */         throw exception;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ServiceListener
/*     */     extends Service.Listener
/*     */   {
/*     */     final Service service;
/*     */ 
/*     */     
/*     */     final WeakReference<ServiceManager.ServiceManagerState> state;
/*     */ 
/*     */     
/*     */     ServiceListener(Service service, WeakReference<ServiceManager.ServiceManagerState> state) {
/* 793 */       this.service = service;
/* 794 */       this.state = state;
/*     */     }
/*     */ 
/*     */     
/*     */     public void starting() {
/* 799 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 800 */       if (state != null) {
/* 801 */         state.transitionService(this.service, Service.State.NEW, Service.State.STARTING);
/* 802 */         if (!(this.service instanceof ServiceManager.NoOpService)) {
/* 803 */           ServiceManager.logger.get().log(Level.FINE, "Starting {0}.", this.service);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void running() {
/* 810 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 811 */       if (state != null) {
/* 812 */         state.transitionService(this.service, Service.State.STARTING, Service.State.RUNNING);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void stopping(Service.State from) {
/* 818 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 819 */       if (state != null) {
/* 820 */         state.transitionService(this.service, from, Service.State.STOPPING);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void terminated(Service.State from) {
/* 826 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 827 */       if (state != null) {
/* 828 */         if (!(this.service instanceof ServiceManager.NoOpService)) {
/* 829 */           ServiceManager.logger
/* 830 */             .get()
/* 831 */             .log(Level.FINE, "Service {0} has terminated. Previous state was: {1}", new Object[] { this.service, from });
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 836 */         state.transitionService(this.service, from, Service.State.TERMINATED);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void failed(Service.State from, Throwable failure) {
/* 842 */       ServiceManager.ServiceManagerState state = this.state.get();
/* 843 */       if (state != null) {
/*     */ 
/*     */         
/* 846 */         boolean log = !(this.service instanceof ServiceManager.NoOpService);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 851 */         int i = log & ((from != Service.State.STARTING) ? 1 : 0);
/* 852 */         if (i != 0) {
/* 853 */           ServiceManager.logger
/* 854 */             .get()
/* 855 */             .log(Level.SEVERE, "Service " + this.service + " has failed in the " + from + " state.", failure);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 860 */         state.transitionService(this.service, from, Service.State.FAILED);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class NoOpService
/*     */     extends AbstractService
/*     */   {
/*     */     private NoOpService() {}
/*     */ 
/*     */ 
/*     */     
/*     */     protected void doStart() {
/* 876 */       notifyStarted();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doStop() {
/* 881 */       notifyStopped();
/*     */     } }
/*     */   
/*     */   private static final class EmptyServiceManagerWarning extends Throwable {
/*     */     private EmptyServiceManagerWarning() {}
/*     */   }
/*     */   
/*     */   private static final class FailedService extends Throwable {
/*     */     FailedService(Service service) {
/* 890 */       super(service
/* 891 */           .toString(), service
/* 892 */           .failureCause(), false, false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/ServiceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */