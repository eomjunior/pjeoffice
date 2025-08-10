/*     */ package org.apache.hc.core5.pool;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.ModalCloseable;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Deadline;
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
/*     */ public final class PoolEntry<T, C extends ModalCloseable>
/*     */ {
/*     */   private final T route;
/*     */   private final TimeValue timeToLive;
/*     */   private final AtomicReference<C> connRef;
/*     */   private final DisposalCallback<C> disposalCallback;
/*     */   private final Supplier<Long> currentTimeSupplier;
/*     */   private volatile Object state;
/*     */   private volatile long created;
/*     */   private volatile long updated;
/*  61 */   private volatile Deadline expiryDeadline = Deadline.MIN_VALUE;
/*  62 */   private volatile Deadline validityDeadline = Deadline.MIN_VALUE;
/*     */ 
/*     */ 
/*     */   
/*     */   PoolEntry(T route, TimeValue timeToLive, DisposalCallback<C> disposalCallback, Supplier<Long> currentTimeSupplier) {
/*  67 */     this.route = (T)Args.notNull(route, "Route");
/*  68 */     this.timeToLive = TimeValue.defaultsToNegativeOneMillisecond(timeToLive);
/*  69 */     this.connRef = new AtomicReference<>();
/*  70 */     this.disposalCallback = disposalCallback;
/*  71 */     this.currentTimeSupplier = currentTimeSupplier;
/*     */   }
/*     */   
/*     */   PoolEntry(T route, TimeValue timeToLive, Supplier<Long> currentTimeSupplier) {
/*  75 */     this(route, timeToLive, null, currentTimeSupplier);
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
/*     */   public PoolEntry(T route, TimeValue timeToLive, DisposalCallback<C> disposalCallback) {
/*  87 */     this(route, timeToLive, disposalCallback, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolEntry(T route, TimeValue timeToLive) {
/*  98 */     this(route, timeToLive, null, null);
/*     */   }
/*     */   
/*     */   public PoolEntry(T route) {
/* 102 */     this(route, null);
/*     */   }
/*     */   
/*     */   long getCurrentTime() {
/* 106 */     return (this.currentTimeSupplier != null) ? ((Long)this.currentTimeSupplier.get()).longValue() : System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   public T getRoute() {
/* 110 */     return this.route;
/*     */   }
/*     */   
/*     */   public C getConnection() {
/* 114 */     return this.connRef.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Deadline getValidityDeadline() {
/* 121 */     return this.validityDeadline;
/*     */   }
/*     */   
/*     */   public Object getState() {
/* 125 */     return this.state;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCreated() {
/* 132 */     return this.created;
/*     */   }
/*     */   
/*     */   public long getUpdated() {
/* 136 */     return this.updated;
/*     */   }
/*     */   
/*     */   public Deadline getExpiryDeadline() {
/* 140 */     return this.expiryDeadline;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasConnection() {
/* 147 */     return (this.connRef.get() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void assignConnection(C conn) {
/* 154 */     Args.notNull(conn, "connection");
/* 155 */     if (this.connRef.compareAndSet(null, conn)) {
/* 156 */       this.created = getCurrentTime();
/* 157 */       this.updated = this.created;
/* 158 */       this.validityDeadline = Deadline.calculate(this.created, this.timeToLive);
/* 159 */       this.expiryDeadline = this.validityDeadline;
/* 160 */       this.state = null;
/*     */     } else {
/* 162 */       throw new IllegalStateException("Connection already assigned");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void discardConnection(CloseMode closeMode) {
/* 170 */     ModalCloseable modalCloseable = (ModalCloseable)this.connRef.getAndSet(null);
/* 171 */     if (modalCloseable != null) {
/* 172 */       this.state = null;
/* 173 */       this.created = 0L;
/* 174 */       this.updated = 0L;
/* 175 */       this.expiryDeadline = Deadline.MIN_VALUE;
/* 176 */       this.validityDeadline = Deadline.MIN_VALUE;
/* 177 */       if (this.disposalCallback != null) {
/* 178 */         this.disposalCallback.execute((C)modalCloseable, closeMode);
/*     */       } else {
/* 180 */         modalCloseable.close(closeMode);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateExpiry(TimeValue expiryTime) {
/* 189 */     Args.notNull(expiryTime, "Expiry time");
/* 190 */     long currentTime = getCurrentTime();
/* 191 */     Deadline newExpiry = Deadline.calculate(currentTime, expiryTime);
/* 192 */     this.expiryDeadline = newExpiry.min(this.validityDeadline);
/* 193 */     this.updated = currentTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateState(Object state) {
/* 200 */     this.state = state;
/* 201 */     this.updated = getCurrentTime();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 206 */     StringBuilder buffer = new StringBuilder();
/* 207 */     buffer.append("[route:");
/* 208 */     buffer.append(this.route);
/* 209 */     buffer.append("][state:");
/* 210 */     buffer.append(this.state);
/* 211 */     buffer.append("]");
/* 212 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/pool/PoolEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */