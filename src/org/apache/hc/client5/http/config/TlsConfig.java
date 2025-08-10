/*     */ package org.apache.hc.client5.http.config;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ssl.TLS;
/*     */ import org.apache.hc.core5.http2.HttpVersionPolicy;
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
/*     */ public class TlsConfig
/*     */   implements Cloneable
/*     */ {
/*  47 */   public static final TlsConfig DEFAULT = (new Builder()).build();
/*     */   
/*     */   private final Timeout handshakeTimeout;
/*     */   
/*     */   private final String[] supportedProtocols;
/*     */   
/*     */   private final String[] supportedCipherSuites;
/*     */   
/*     */   private final HttpVersionPolicy httpVersionPolicy;
/*     */   
/*     */   protected TlsConfig() {
/*  58 */     this(null, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TlsConfig(Timeout handshakeTimeout, String[] supportedProtocols, String[] supportedCipherSuites, HttpVersionPolicy httpVersionPolicy) {
/*  67 */     this.handshakeTimeout = handshakeTimeout;
/*  68 */     this.supportedProtocols = supportedProtocols;
/*  69 */     this.supportedCipherSuites = supportedCipherSuites;
/*  70 */     this.httpVersionPolicy = httpVersionPolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Timeout getHandshakeTimeout() {
/*  77 */     return this.handshakeTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getSupportedProtocols() {
/*  84 */     return (this.supportedProtocols != null) ? (String[])this.supportedProtocols.clone() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getSupportedCipherSuites() {
/*  91 */     return (this.supportedCipherSuites != null) ? (String[])this.supportedCipherSuites.clone() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpVersionPolicy getHttpVersionPolicy() {
/*  98 */     return this.httpVersionPolicy;
/*     */   }
/*     */ 
/*     */   
/*     */   protected TlsConfig clone() throws CloneNotSupportedException {
/* 103 */     return (TlsConfig)super.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 108 */     StringBuilder builder = new StringBuilder();
/* 109 */     builder.append("[");
/* 110 */     builder.append("handshakeTimeout=").append(this.handshakeTimeout);
/* 111 */     builder.append(", supportedProtocols=").append(Arrays.toString((Object[])this.supportedProtocols));
/* 112 */     builder.append(", supportedCipherSuites=").append(Arrays.toString((Object[])this.supportedCipherSuites));
/* 113 */     builder.append(", httpVersionPolicy=").append(this.httpVersionPolicy);
/* 114 */     builder.append("]");
/* 115 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static Builder custom() {
/* 119 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static Builder copy(TlsConfig config) {
/* 123 */     return (new Builder())
/* 124 */       .setHandshakeTimeout(config.getHandshakeTimeout())
/* 125 */       .setSupportedProtocols(config.getSupportedProtocols())
/* 126 */       .setSupportedCipherSuites(config.getSupportedCipherSuites())
/* 127 */       .setVersionPolicy(config.getHttpVersionPolicy());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private Timeout handshakeTimeout;
/*     */ 
/*     */     
/*     */     private String[] supportedProtocols;
/*     */ 
/*     */     
/*     */     private String[] supportedCipherSuites;
/*     */ 
/*     */     
/*     */     private HttpVersionPolicy versionPolicy;
/*     */ 
/*     */     
/*     */     public Builder setHandshakeTimeout(Timeout handshakeTimeout) {
/* 147 */       this.handshakeTimeout = handshakeTimeout;
/* 148 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setHandshakeTimeout(long handshakeTimeout, TimeUnit timeUnit) {
/* 155 */       this.handshakeTimeout = Timeout.of(handshakeTimeout, timeUnit);
/* 156 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSupportedProtocols(String... supportedProtocols) {
/* 166 */       this.supportedProtocols = supportedProtocols;
/* 167 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSupportedProtocols(TLS... supportedProtocols) {
/* 177 */       this.supportedProtocols = new String[supportedProtocols.length];
/* 178 */       for (int i = 0; i < supportedProtocols.length; i++) {
/* 179 */         TLS protocol = supportedProtocols[i];
/* 180 */         if (protocol != null) {
/* 181 */           this.supportedProtocols[i] = protocol.id;
/*     */         }
/*     */       } 
/* 184 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setSupportedCipherSuites(String... supportedCipherSuites) {
/* 194 */       this.supportedCipherSuites = supportedCipherSuites;
/* 195 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setVersionPolicy(HttpVersionPolicy versionPolicy) {
/* 206 */       this.versionPolicy = versionPolicy;
/* 207 */       return this;
/*     */     }
/*     */     
/*     */     public TlsConfig build() {
/* 211 */       return new TlsConfig(this.handshakeTimeout, this.supportedProtocols, this.supportedCipherSuites, (this.versionPolicy != null) ? this.versionPolicy : HttpVersionPolicy.NEGOTIATE);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/config/TlsConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */