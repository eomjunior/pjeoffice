/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.classic.BackoffManager;
/*     */ import org.apache.hc.core5.annotation.Experimental;
/*     */ import org.apache.hc.core5.pool.ConnPoolControl;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TimeValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Experimental
/*     */ public class AIMDBackoffManager
/*     */   implements BackoffManager
/*     */ {
/*     */   private final ConnPoolControl<HttpRoute> connPerRoute;
/*     */   private final Clock clock;
/*     */   private final Map<HttpRoute, Long> lastRouteProbes;
/*     */   private final Map<HttpRoute, Long> lastRouteBackoffs;
/*  66 */   private TimeValue coolDown = TimeValue.ofSeconds(5L);
/*  67 */   private double backoffFactor = 0.5D;
/*  68 */   private int cap = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AIMDBackoffManager(ConnPoolControl<HttpRoute> connPerRoute) {
/*  78 */     this(connPerRoute, new SystemClock());
/*     */   }
/*     */   
/*     */   AIMDBackoffManager(ConnPoolControl<HttpRoute> connPerRoute, Clock clock) {
/*  82 */     this.clock = clock;
/*  83 */     this.connPerRoute = connPerRoute;
/*  84 */     this.lastRouteProbes = new HashMap<>();
/*  85 */     this.lastRouteBackoffs = new HashMap<>();
/*     */   }
/*     */ 
/*     */   
/*     */   public void backOff(HttpRoute route) {
/*  90 */     synchronized (this.connPerRoute) {
/*  91 */       int curr = this.connPerRoute.getMaxPerRoute(route);
/*  92 */       Long lastUpdate = getLastUpdate(this.lastRouteBackoffs, route);
/*  93 */       long now = this.clock.getCurrentTime();
/*  94 */       if (now - lastUpdate.longValue() < this.coolDown.toMilliseconds()) {
/*     */         return;
/*     */       }
/*  97 */       this.connPerRoute.setMaxPerRoute(route, getBackedOffPoolSize(curr));
/*  98 */       this.lastRouteBackoffs.put(route, Long.valueOf(now));
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getBackedOffPoolSize(int curr) {
/* 103 */     if (curr <= 1) {
/* 104 */       return 1;
/*     */     }
/* 106 */     return (int)Math.floor(this.backoffFactor * curr);
/*     */   }
/*     */ 
/*     */   
/*     */   public void probe(HttpRoute route) {
/* 111 */     synchronized (this.connPerRoute) {
/* 112 */       int curr = this.connPerRoute.getMaxPerRoute(route);
/* 113 */       int max = (curr >= this.cap) ? this.cap : (curr + 1);
/* 114 */       Long lastProbe = getLastUpdate(this.lastRouteProbes, route);
/* 115 */       Long lastBackoff = getLastUpdate(this.lastRouteBackoffs, route);
/* 116 */       long now = this.clock.getCurrentTime();
/* 117 */       if (now - lastProbe.longValue() < this.coolDown.toMilliseconds() || now - lastBackoff
/* 118 */         .longValue() < this.coolDown.toMilliseconds()) {
/*     */         return;
/*     */       }
/* 121 */       this.connPerRoute.setMaxPerRoute(route, max);
/* 122 */       this.lastRouteProbes.put(route, Long.valueOf(now));
/*     */     } 
/*     */   }
/*     */   
/*     */   private Long getLastUpdate(Map<HttpRoute, Long> updates, HttpRoute route) {
/* 127 */     Long lastUpdate = updates.get(route);
/* 128 */     if (lastUpdate == null) {
/* 129 */       lastUpdate = Long.valueOf(0L);
/*     */     }
/* 131 */     return lastUpdate;
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
/*     */   public void setBackoffFactor(double d) {
/* 144 */     Args.check((d > 0.0D && d < 1.0D), "Backoff factor must be 0.0 < f < 1.0");
/* 145 */     this.backoffFactor = d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCoolDown(TimeValue coolDown) {
/* 155 */     Args.positive(coolDown.getDuration(), "coolDown");
/* 156 */     this.coolDown = coolDown;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPerHostConnectionCap(int cap) {
/* 165 */     Args.positive(cap, "Per host connection cap");
/* 166 */     this.cap = cap;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/AIMDBackoffManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */