/*     */ package org.apache.hc.core5.http.io;
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
/*     */ public class SocketConfig
/*     */ {
/*  47 */   private static final Timeout DEFAULT_SOCKET_TIMEOUT = Timeout.ofMinutes(3L);
/*     */   
/*  49 */   public static final SocketConfig DEFAULT = (new Builder()).build();
/*     */ 
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
/*     */   private final int sndBufSize;
/*     */   
/*     */   private final int rcvBufSize;
/*     */   
/*     */   private final int backlogSize;
/*     */   
/*     */   private final SocketAddress socksProxyAddress;
/*     */ 
/*     */   
/*     */   SocketConfig(Timeout soTimeout, boolean soReuseAddress, TimeValue soLinger, boolean soKeepAlive, boolean tcpNoDelay, int sndBufSize, int rcvBufSize, int backlogSize, SocketAddress socksProxyAddress) {
/*  72 */     this.soTimeout = soTimeout;
/*  73 */     this.soReuseAddress = soReuseAddress;
/*  74 */     this.soLinger = soLinger;
/*  75 */     this.soKeepAlive = soKeepAlive;
/*  76 */     this.tcpNoDelay = tcpNoDelay;
/*  77 */     this.sndBufSize = sndBufSize;
/*  78 */     this.rcvBufSize = rcvBufSize;
/*  79 */     this.backlogSize = backlogSize;
/*  80 */     this.socksProxyAddress = socksProxyAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Timeout getSoTimeout() {
/*  87 */     return this.soTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSoReuseAddress() {
/*  94 */     return this.soReuseAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeValue getSoLinger() {
/* 101 */     return this.soLinger;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSoKeepAlive() {
/* 108 */     return this.soKeepAlive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTcpNoDelay() {
/* 115 */     return this.tcpNoDelay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSndBufSize() {
/* 123 */     return this.sndBufSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRcvBufSize() {
/* 131 */     return this.rcvBufSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBacklogSize() {
/* 139 */     return this.backlogSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketAddress getSocksProxyAddress() {
/* 146 */     return this.socksProxyAddress;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 151 */     StringBuilder builder = new StringBuilder();
/* 152 */     builder.append("[soTimeout=").append(this.soTimeout)
/* 153 */       .append(", soReuseAddress=").append(this.soReuseAddress)
/* 154 */       .append(", soLinger=").append(this.soLinger)
/* 155 */       .append(", soKeepAlive=").append(this.soKeepAlive)
/* 156 */       .append(", tcpNoDelay=").append(this.tcpNoDelay)
/* 157 */       .append(", sndBufSize=").append(this.sndBufSize)
/* 158 */       .append(", rcvBufSize=").append(this.rcvBufSize)
/* 159 */       .append(", backlogSize=").append(this.backlogSize)
/* 160 */       .append(", socksProxyAddress=").append(this.socksProxyAddress)
/* 161 */       .append("]");
/* 162 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static Builder custom() {
/* 166 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static Builder copy(SocketConfig config) {
/* 170 */     Args.notNull(config, "Socket config");
/* 171 */     return (new Builder())
/* 172 */       .setSoTimeout(config.getSoTimeout())
/* 173 */       .setSoReuseAddress(config.isSoReuseAddress())
/* 174 */       .setSoLinger(config.getSoLinger())
/* 175 */       .setSoKeepAlive(config.isSoKeepAlive())
/* 176 */       .setTcpNoDelay(config.isTcpNoDelay())
/* 177 */       .setSndBufSize(config.getSndBufSize())
/* 178 */       .setRcvBufSize(config.getRcvBufSize())
/* 179 */       .setBacklogSize(config.getBacklogSize())
/* 180 */       .setSocksProxyAddress(config.getSocksProxyAddress());
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
/*     */   public static class Builder
/*     */   {
/* 196 */     private Timeout soTimeout = SocketConfig.DEFAULT_SOCKET_TIMEOUT;
/*     */     private boolean soReuseAddress = false;
/* 198 */     private TimeValue soLinger = TimeValue.NEG_ONE_SECOND;
/*     */     private boolean soKeepAlive = false;
/*     */     private boolean tcpNoDelay = true;
/* 201 */     private int sndBufSize = 0;
/* 202 */     private int rcvBufSize = 0;
/* 203 */     private int backlogSize = 0;
/* 204 */     private SocketAddress socksProxyAddress = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSoTimeout(int soTimeout, TimeUnit timeUnit) {
/* 211 */       this.soTimeout = Timeout.of(soTimeout, timeUnit);
/* 212 */       return this;
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
/*     */     public Builder setSoTimeout(Timeout soTimeout) {
/* 225 */       this.soTimeout = soTimeout;
/* 226 */       return this;
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
/*     */     public Builder setSoReuseAddress(boolean soReuseAddress) {
/* 240 */       this.soReuseAddress = soReuseAddress;
/* 241 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSoLinger(int soLinger, TimeUnit timeUnit) {
/* 248 */       this.soLinger = (TimeValue)Timeout.of(soLinger, timeUnit);
/* 249 */       return this;
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
/*     */     public Builder setSoLinger(TimeValue soLinger) {
/* 263 */       this.soLinger = soLinger;
/* 264 */       return this;
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
/*     */     public Builder setSoKeepAlive(boolean soKeepAlive) {
/* 278 */       this.soKeepAlive = soKeepAlive;
/* 279 */       return this;
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
/*     */     public Builder setTcpNoDelay(boolean tcpNoDelay) {
/* 293 */       this.tcpNoDelay = tcpNoDelay;
/* 294 */       return this;
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
/*     */     public Builder setSndBufSize(int sndBufSize) {
/* 309 */       this.sndBufSize = sndBufSize;
/* 310 */       return this;
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
/*     */     public Builder setRcvBufSize(int rcvBufSize) {
/* 325 */       this.rcvBufSize = rcvBufSize;
/* 326 */       return this;
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
/*     */     public Builder setBacklogSize(int backlogSize) {
/* 339 */       this.backlogSize = backlogSize;
/* 340 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSocksProxyAddress(SocketAddress socksProxyAddress) {
/* 347 */       this.socksProxyAddress = socksProxyAddress;
/* 348 */       return this;
/*     */     }
/*     */     
/*     */     public SocketConfig build() {
/* 352 */       return new SocketConfig(
/* 353 */           Timeout.defaultsToDisabled(this.soTimeout), this.soReuseAddress, (this.soLinger != null) ? this.soLinger : TimeValue.NEG_ONE_SECOND, this.soKeepAlive, this.tcpNoDelay, this.sndBufSize, this.rcvBufSize, this.backlogSize, this.socksProxyAddress);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/SocketConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */