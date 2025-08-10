/*     */ package org.apache.hc.core5.reactor;
/*     */ 
/*     */ import java.net.SocketAddress;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TimeValue;
/*     */ import org.apache.hc.core5.util.Timeout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public final class IOReactorConfig
/*     */ {
/*  47 */   public static final IOReactorConfig DEFAULT = (new Builder()).build();
/*     */ 
/*     */   
/*     */   private final TimeValue selectInterval;
/*     */   
/*     */   private final int ioThreadCount;
/*     */   
/*     */   private final Timeout soTimeout;
/*     */   
/*     */   private final boolean soReuseAddress;
/*     */   
/*     */   private final TimeValue soLinger;
/*     */   
/*     */   private final boolean soKeepAlive;
/*     */   
/*     */   private final boolean tcpNoDelay;
/*     */   
/*     */   private final int trafficClass;
/*     */   
/*     */   private final int sndBufSize;
/*     */   
/*     */   private final int rcvBufSize;
/*     */   
/*     */   private final int backlogSize;
/*     */   
/*     */   private final SocketAddress socksProxyAddress;
/*     */   
/*     */   private final String socksProxyUsername;
/*     */   
/*     */   private final String socksProxyPassword;
/*     */ 
/*     */   
/*     */   IOReactorConfig(TimeValue selectInterval, int ioThreadCount, Timeout soTimeout, boolean soReuseAddress, TimeValue soLinger, boolean soKeepAlive, boolean tcpNoDelay, int trafficClass, int sndBufSize, int rcvBufSize, int backlogSize, SocketAddress socksProxyAddress, String socksProxyUsername, String socksProxyPassword) {
/*  80 */     this.selectInterval = selectInterval;
/*  81 */     this.ioThreadCount = ioThreadCount;
/*  82 */     this.soTimeout = soTimeout;
/*  83 */     this.soReuseAddress = soReuseAddress;
/*  84 */     this.soLinger = soLinger;
/*  85 */     this.soKeepAlive = soKeepAlive;
/*  86 */     this.tcpNoDelay = tcpNoDelay;
/*  87 */     this.trafficClass = trafficClass;
/*  88 */     this.sndBufSize = sndBufSize;
/*  89 */     this.rcvBufSize = rcvBufSize;
/*  90 */     this.backlogSize = backlogSize;
/*  91 */     this.socksProxyAddress = socksProxyAddress;
/*  92 */     this.socksProxyUsername = socksProxyUsername;
/*  93 */     this.socksProxyPassword = socksProxyPassword;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeValue getSelectInterval() {
/* 100 */     return this.selectInterval;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIoThreadCount() {
/* 107 */     return this.ioThreadCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Timeout getSoTimeout() {
/* 114 */     return this.soTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSoReuseAddress() {
/* 121 */     return this.soReuseAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeValue getSoLinger() {
/* 128 */     return this.soLinger;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSoKeepAlive() {
/* 136 */     return this.soKeepAlive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean isSoKeepalive() {
/* 145 */     return this.soKeepAlive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTcpNoDelay() {
/* 152 */     return this.tcpNoDelay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTrafficClass() {
/* 161 */     return this.trafficClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSndBufSize() {
/* 168 */     return this.sndBufSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRcvBufSize() {
/* 175 */     return this.rcvBufSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBacklogSize() {
/* 182 */     return this.backlogSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketAddress getSocksProxyAddress() {
/* 189 */     return this.socksProxyAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSocksProxyUsername() {
/* 196 */     return this.socksProxyUsername;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSocksProxyPassword() {
/* 203 */     return this.socksProxyPassword;
/*     */   }
/*     */   
/*     */   public static Builder custom() {
/* 207 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static Builder copy(IOReactorConfig config) {
/* 211 */     Args.notNull(config, "I/O reactor config");
/* 212 */     return (new Builder())
/* 213 */       .setSelectInterval(config.getSelectInterval())
/* 214 */       .setIoThreadCount(config.getIoThreadCount())
/* 215 */       .setSoTimeout(config.getSoTimeout())
/* 216 */       .setSoReuseAddress(config.isSoReuseAddress())
/* 217 */       .setSoLinger(config.getSoLinger())
/* 218 */       .setSoKeepAlive(config.isSoKeepAlive())
/* 219 */       .setTcpNoDelay(config.isTcpNoDelay())
/* 220 */       .setSndBufSize(config.getSndBufSize())
/* 221 */       .setRcvBufSize(config.getRcvBufSize())
/* 222 */       .setBacklogSize(config.getBacklogSize())
/* 223 */       .setSocksProxyAddress(config.getSocksProxyAddress())
/* 224 */       .setSocksProxyUsername(config.getSocksProxyUsername())
/* 225 */       .setSocksProxyPassword(config.getSocksProxyPassword());
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */   {
/* 230 */     private static int defaultMaxIOThreadCount = -1;
/*     */     
/*     */     private TimeValue selectInterval;
/*     */     private int ioThreadCount;
/*     */     private Timeout soTimeout;
/*     */     private boolean soReuseAddress;
/*     */     private TimeValue soLinger;
/*     */     private boolean soKeepAlive;
/*     */     private boolean tcpNoDelay;
/*     */     
/*     */     public static int getDefaultMaxIOThreadCount() {
/* 241 */       return (defaultMaxIOThreadCount > 0) ? defaultMaxIOThreadCount : Runtime.getRuntime().availableProcessors();
/*     */     }
/*     */ 
/*     */     
/*     */     private int trafficClass;
/*     */     private int sndBufSize;
/*     */     private int rcvBufSize;
/*     */     private int backlogSize;
/*     */     private SocketAddress socksProxyAddress;
/*     */     private String socksProxyUsername;
/*     */     private String socksProxyPassword;
/*     */     
/*     */     public static void setDefaultMaxIOThreadCount(int defaultMaxIOThreadCount) {
/* 254 */       Builder.defaultMaxIOThreadCount = defaultMaxIOThreadCount;
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
/*     */     Builder() {
/* 273 */       this.selectInterval = TimeValue.ofSeconds(1L);
/* 274 */       this.ioThreadCount = getDefaultMaxIOThreadCount();
/* 275 */       this.soTimeout = Timeout.ZERO_MILLISECONDS;
/* 276 */       this.soReuseAddress = false;
/* 277 */       this.soLinger = TimeValue.NEG_ONE_SECOND;
/* 278 */       this.soKeepAlive = false;
/* 279 */       this.tcpNoDelay = true;
/* 280 */       this.trafficClass = 0;
/* 281 */       this.sndBufSize = 0;
/* 282 */       this.rcvBufSize = 0;
/* 283 */       this.backlogSize = 0;
/* 284 */       this.socksProxyAddress = null;
/* 285 */       this.socksProxyUsername = null;
/* 286 */       this.socksProxyPassword = null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSelectInterval(TimeValue selectInterval) {
/* 297 */       this.selectInterval = selectInterval;
/* 298 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setIoThreadCount(int ioThreadCount) {
/* 308 */       this.ioThreadCount = ioThreadCount;
/* 309 */       return this;
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
/*     */     public Builder setSoTimeout(int soTimeout, TimeUnit timeUnit) {
/* 321 */       this.soTimeout = Timeout.of(soTimeout, timeUnit);
/* 322 */       return this;
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
/*     */     public Builder setSoTimeout(Timeout soTimeout) {
/* 334 */       this.soTimeout = soTimeout;
/* 335 */       return this;
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
/*     */     public Builder setSoReuseAddress(boolean soReuseAddress) {
/* 348 */       this.soReuseAddress = soReuseAddress;
/* 349 */       return this;
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
/*     */     public Builder setSoLinger(int soLinger, TimeUnit timeUnit) {
/* 362 */       this.soLinger = TimeValue.of(soLinger, timeUnit);
/* 363 */       return this;
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
/*     */     public Builder setSoLinger(TimeValue soLinger) {
/* 376 */       this.soLinger = soLinger;
/* 377 */       return this;
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
/*     */     public Builder setSoKeepAlive(boolean soKeepAlive) {
/* 390 */       this.soKeepAlive = soKeepAlive;
/* 391 */       return this;
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
/*     */     public Builder setTcpNoDelay(boolean tcpNoDelay) {
/* 404 */       this.tcpNoDelay = tcpNoDelay;
/* 405 */       return this;
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
/*     */     public Builder setTrafficClass(int trafficClass) {
/* 420 */       this.trafficClass = trafficClass;
/* 421 */       return this;
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
/*     */     public Builder setSndBufSize(int sndBufSize) {
/* 434 */       this.sndBufSize = sndBufSize;
/* 435 */       return this;
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
/*     */     public Builder setRcvBufSize(int rcvBufSize) {
/* 448 */       this.rcvBufSize = rcvBufSize;
/* 449 */       return this;
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
/*     */     public Builder setBacklogSize(int backlogSize) {
/* 461 */       this.backlogSize = backlogSize;
/* 462 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSocksProxyAddress(SocketAddress socksProxyAddress) {
/* 469 */       this.socksProxyAddress = socksProxyAddress;
/* 470 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSocksProxyUsername(String socksProxyUsername) {
/* 477 */       this.socksProxyUsername = socksProxyUsername;
/* 478 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSocksProxyPassword(String socksProxyPassword) {
/* 485 */       this.socksProxyPassword = socksProxyPassword;
/* 486 */       return this;
/*     */     }
/*     */     
/*     */     public IOReactorConfig build() {
/* 490 */       return new IOReactorConfig((this.selectInterval != null) ? this.selectInterval : 
/* 491 */           TimeValue.ofSeconds(1L), this.ioThreadCount, 
/*     */           
/* 493 */           Timeout.defaultsToDisabled(this.soTimeout), this.soReuseAddress, 
/*     */           
/* 495 */           TimeValue.defaultsToNegativeOneMillisecond(this.soLinger), this.soKeepAlive, this.tcpNoDelay, this.trafficClass, this.sndBufSize, this.rcvBufSize, this.backlogSize, this.socksProxyAddress, this.socksProxyUsername, this.socksProxyPassword);
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
/*     */   public String toString() {
/* 507 */     StringBuilder builder = new StringBuilder();
/* 508 */     builder.append("[selectInterval=").append(this.selectInterval)
/* 509 */       .append(", ioThreadCount=").append(this.ioThreadCount)
/* 510 */       .append(", soTimeout=").append(this.soTimeout)
/* 511 */       .append(", soReuseAddress=").append(this.soReuseAddress)
/* 512 */       .append(", soLinger=").append(this.soLinger)
/* 513 */       .append(", soKeepAlive=").append(this.soKeepAlive)
/* 514 */       .append(", tcpNoDelay=").append(this.tcpNoDelay)
/* 515 */       .append(", trafficClass=").append(this.trafficClass)
/* 516 */       .append(", sndBufSize=").append(this.sndBufSize)
/* 517 */       .append(", rcvBufSize=").append(this.rcvBufSize)
/* 518 */       .append(", backlogSize=").append(this.backlogSize)
/* 519 */       .append(", socksProxyAddress=").append(this.socksProxyAddress)
/* 520 */       .append("]");
/* 521 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/IOReactorConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */