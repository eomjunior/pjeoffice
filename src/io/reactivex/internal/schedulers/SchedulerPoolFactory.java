/*     */ package io.reactivex.internal.schedulers;
/*     */ 
/*     */ import io.reactivex.functions.Function;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SchedulerPoolFactory
/*     */ {
/*     */   static final String PURGE_ENABLED_KEY = "rx2.purge-enabled";
/*     */   public static final boolean PURGE_ENABLED;
/*     */   static final String PURGE_PERIOD_SECONDS_KEY = "rx2.purge-period-seconds";
/*     */   public static final int PURGE_PERIOD_SECONDS;
/*     */   
/*     */   private SchedulerPoolFactory() {
/*  31 */     throw new IllegalStateException("No instances!");
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
/*  48 */   static final AtomicReference<ScheduledExecutorService> PURGE_THREAD = new AtomicReference<ScheduledExecutorService>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   static final Map<ScheduledThreadPoolExecutor, Object> POOLS = new ConcurrentHashMap<ScheduledThreadPoolExecutor, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void start() {
/*  60 */     tryStart(PURGE_ENABLED);
/*     */   }
/*     */   
/*     */   static void tryStart(boolean purgeEnabled) {
/*  64 */     if (purgeEnabled) {
/*     */       while (true) {
/*  66 */         ScheduledExecutorService curr = PURGE_THREAD.get();
/*  67 */         if (curr != null) {
/*     */           return;
/*     */         }
/*  70 */         ScheduledExecutorService next = Executors.newScheduledThreadPool(1, new RxThreadFactory("RxSchedulerPurge"));
/*  71 */         if (PURGE_THREAD.compareAndSet(curr, next)) {
/*     */           
/*  73 */           next.scheduleAtFixedRate(new ScheduledTask(), PURGE_PERIOD_SECONDS, PURGE_PERIOD_SECONDS, TimeUnit.SECONDS);
/*     */           
/*     */           return;
/*     */         } 
/*  77 */         next.shutdownNow();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void shutdown() {
/*  87 */     ScheduledExecutorService exec = PURGE_THREAD.getAndSet(null);
/*  88 */     if (exec != null) {
/*  89 */       exec.shutdownNow();
/*     */     }
/*  91 */     POOLS.clear();
/*     */   }
/*     */   
/*     */   static {
/*  95 */     SystemPropertyAccessor propertyAccessor = new SystemPropertyAccessor();
/*  96 */     PURGE_ENABLED = getBooleanProperty(true, "rx2.purge-enabled", true, true, propertyAccessor);
/*  97 */     PURGE_PERIOD_SECONDS = getIntProperty(PURGE_ENABLED, "rx2.purge-period-seconds", 1, 1, propertyAccessor);
/*     */     
/*  99 */     start();
/*     */   }
/*     */   
/*     */   static int getIntProperty(boolean enabled, String key, int defaultNotFound, int defaultNotEnabled, Function<String, String> propertyAccessor) {
/* 103 */     if (enabled) {
/*     */       try {
/* 105 */         String value = (String)propertyAccessor.apply(key);
/* 106 */         if (value == null) {
/* 107 */           return defaultNotFound;
/*     */         }
/* 109 */         return Integer.parseInt(value);
/* 110 */       } catch (Throwable ex) {
/* 111 */         return defaultNotFound;
/*     */       } 
/*     */     }
/* 114 */     return defaultNotEnabled;
/*     */   }
/*     */   
/*     */   static boolean getBooleanProperty(boolean enabled, String key, boolean defaultNotFound, boolean defaultNotEnabled, Function<String, String> propertyAccessor) {
/* 118 */     if (enabled) {
/*     */       try {
/* 120 */         String value = (String)propertyAccessor.apply(key);
/* 121 */         if (value == null) {
/* 122 */           return defaultNotFound;
/*     */         }
/* 124 */         return "true".equals(value);
/* 125 */       } catch (Throwable ex) {
/* 126 */         return defaultNotFound;
/*     */       } 
/*     */     }
/* 129 */     return defaultNotEnabled;
/*     */   }
/*     */   
/*     */   static final class SystemPropertyAccessor
/*     */     implements Function<String, String> {
/*     */     public String apply(String t) throws Exception {
/* 135 */       return System.getProperty(t);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ScheduledExecutorService create(ThreadFactory factory) {
/* 145 */     ScheduledExecutorService exec = Executors.newScheduledThreadPool(1, factory);
/* 146 */     tryPutIntoPool(PURGE_ENABLED, exec);
/* 147 */     return exec;
/*     */   }
/*     */   
/*     */   static void tryPutIntoPool(boolean purgeEnabled, ScheduledExecutorService exec) {
/* 151 */     if (purgeEnabled && exec instanceof ScheduledThreadPoolExecutor) {
/* 152 */       ScheduledThreadPoolExecutor e = (ScheduledThreadPoolExecutor)exec;
/* 153 */       POOLS.put(e, exec);
/*     */     } 
/*     */   }
/*     */   
/*     */   static final class ScheduledTask
/*     */     implements Runnable {
/*     */     public void run() {
/* 160 */       for (ScheduledThreadPoolExecutor e : new ArrayList(SchedulerPoolFactory.POOLS.keySet())) {
/* 161 */         if (e.isShutdown()) {
/* 162 */           SchedulerPoolFactory.POOLS.remove(e); continue;
/*     */         } 
/* 164 */         e.purge();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/schedulers/SchedulerPoolFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */