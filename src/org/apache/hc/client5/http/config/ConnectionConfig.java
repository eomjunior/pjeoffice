/*     */ package org.apache.hc.client5.http.config;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
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
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public class ConnectionConfig
/*     */   implements Cloneable
/*     */ {
/*  45 */   private static final Timeout DEFAULT_CONNECT_TIMEOUT = Timeout.ofMinutes(3L);
/*     */   
/*  47 */   public static final ConnectionConfig DEFAULT = (new Builder()).build();
/*     */   
/*     */   private final Timeout connectTimeout;
/*     */   
/*     */   private final Timeout socketTimeout;
/*     */   
/*     */   private final TimeValue validateAfterInactivity;
/*     */   
/*     */   private final TimeValue timeToLive;
/*     */   
/*     */   protected ConnectionConfig() {
/*  58 */     this(DEFAULT_CONNECT_TIMEOUT, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ConnectionConfig(Timeout connectTimeout, Timeout socketTimeout, TimeValue validateAfterInactivity, TimeValue timeToLive) {
/*  67 */     this.connectTimeout = connectTimeout;
/*  68 */     this.socketTimeout = socketTimeout;
/*  69 */     this.validateAfterInactivity = validateAfterInactivity;
/*  70 */     this.timeToLive = timeToLive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Timeout getSocketTimeout() {
/*  77 */     return this.socketTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Timeout getConnectTimeout() {
/*  84 */     return this.connectTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeValue getValidateAfterInactivity() {
/*  91 */     return this.validateAfterInactivity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeValue getTimeToLive() {
/*  98 */     return this.timeToLive;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ConnectionConfig clone() throws CloneNotSupportedException {
/* 103 */     return (ConnectionConfig)super.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 108 */     StringBuilder builder = new StringBuilder();
/* 109 */     builder.append("[");
/* 110 */     builder.append("connectTimeout=").append(this.connectTimeout);
/* 111 */     builder.append(", socketTimeout=").append(this.socketTimeout);
/* 112 */     builder.append(", validateAfterInactivity=").append(this.validateAfterInactivity);
/* 113 */     builder.append(", timeToLive=").append(this.timeToLive);
/* 114 */     builder.append("]");
/* 115 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static Builder custom() {
/* 119 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static Builder copy(ConnectionConfig config) {
/* 123 */     return (new Builder())
/* 124 */       .setConnectTimeout(config.getConnectTimeout())
/* 125 */       .setSocketTimeout(config.getSocketTimeout())
/* 126 */       .setValidateAfterInactivity(config.getValidateAfterInactivity())
/* 127 */       .setTimeToLive(config.getTimeToLive());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private Timeout socketTimeout;
/*     */ 
/*     */ 
/*     */     
/* 139 */     private Timeout connectTimeout = ConnectionConfig.DEFAULT_CONNECT_TIMEOUT;
/*     */     
/*     */     private TimeValue validateAfterInactivity;
/*     */     
/*     */     private TimeValue timeToLive;
/*     */     
/*     */     public Builder setSocketTimeout(int soTimeout, TimeUnit timeUnit) {
/* 146 */       this.socketTimeout = Timeout.of(soTimeout, timeUnit);
/* 147 */       return this;
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
/*     */     public Builder setSocketTimeout(Timeout soTimeout) {
/* 159 */       this.socketTimeout = soTimeout;
/* 160 */       return this;
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
/*     */     public Builder setConnectTimeout(Timeout connectTimeout) {
/* 173 */       this.connectTimeout = connectTimeout;
/* 174 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setConnectTimeout(long connectTimeout, TimeUnit timeUnit) {
/* 181 */       this.connectTimeout = Timeout.of(connectTimeout, timeUnit);
/* 182 */       return this;
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
/*     */     public Builder setValidateAfterInactivity(TimeValue validateAfterInactivity) {
/* 194 */       this.validateAfterInactivity = validateAfterInactivity;
/* 195 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setValidateAfterInactivity(long validateAfterInactivity, TimeUnit timeUnit) {
/* 202 */       this.validateAfterInactivity = TimeValue.of(validateAfterInactivity, timeUnit);
/* 203 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setTimeToLive(TimeValue timeToLive) {
/* 213 */       this.timeToLive = timeToLive;
/* 214 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setTimeToLive(long timeToLive, TimeUnit timeUnit) {
/* 221 */       this.timeToLive = TimeValue.of(timeToLive, timeUnit);
/* 222 */       return this;
/*     */     }
/*     */     
/*     */     public ConnectionConfig build() {
/* 226 */       return new ConnectionConfig((this.connectTimeout != null) ? this.connectTimeout : ConnectionConfig
/* 227 */           .DEFAULT_CONNECT_TIMEOUT, this.socketTimeout, this.validateAfterInactivity, this.timeToLive);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/config/ConnectionConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */