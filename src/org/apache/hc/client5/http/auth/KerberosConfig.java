/*     */ package org.apache.hc.client5.http.auth;
/*     */ 
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class KerberosConfig
/*     */   implements Cloneable
/*     */ {
/*     */   public enum Option
/*     */   {
/*  43 */     DEFAULT,
/*  44 */     ENABLE,
/*  45 */     DISABLE;
/*     */   }
/*     */ 
/*     */   
/*  49 */   public static final KerberosConfig DEFAULT = (new Builder()).build();
/*     */   
/*     */   private final Option stripPort;
/*     */   
/*     */   private final Option useCanonicalHostname;
/*     */   
/*     */   private final Option requestDelegCreds;
/*     */ 
/*     */   
/*     */   protected KerberosConfig() {
/*  59 */     this(Option.DEFAULT, Option.DEFAULT, Option.DEFAULT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   KerberosConfig(Option stripPort, Option useCanonicalHostname, Option requestDelegCreds) {
/*  67 */     this.stripPort = stripPort;
/*  68 */     this.useCanonicalHostname = useCanonicalHostname;
/*  69 */     this.requestDelegCreds = requestDelegCreds;
/*     */   }
/*     */   
/*     */   public Option getStripPort() {
/*  73 */     return this.stripPort;
/*     */   }
/*     */   
/*     */   public Option getUseCanonicalHostname() {
/*  77 */     return this.useCanonicalHostname;
/*     */   }
/*     */   
/*     */   public Option getRequestDelegCreds() {
/*  81 */     return this.requestDelegCreds;
/*     */   }
/*     */ 
/*     */   
/*     */   protected KerberosConfig clone() throws CloneNotSupportedException {
/*  86 */     return (KerberosConfig)super.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  91 */     StringBuilder builder = new StringBuilder();
/*  92 */     builder.append("[");
/*  93 */     builder.append("stripPort=").append(this.stripPort);
/*  94 */     builder.append(", useCanonicalHostname=").append(this.useCanonicalHostname);
/*  95 */     builder.append(", requestDelegCreds=").append(this.requestDelegCreds);
/*  96 */     builder.append("]");
/*  97 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static Builder custom() {
/* 101 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static Builder copy(KerberosConfig config) {
/* 105 */     return (new Builder())
/* 106 */       .setStripPort(config.getStripPort())
/* 107 */       .setUseCanonicalHostname(config.getUseCanonicalHostname())
/* 108 */       .setRequestDelegCreds(config.getRequestDelegCreds());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/* 119 */     private KerberosConfig.Option stripPort = KerberosConfig.Option.DEFAULT;
/* 120 */     private KerberosConfig.Option useCanonicalHostname = KerberosConfig.Option.DEFAULT;
/* 121 */     private KerberosConfig.Option requestDelegCreds = KerberosConfig.Option.DEFAULT;
/*     */ 
/*     */     
/*     */     public Builder setStripPort(KerberosConfig.Option stripPort) {
/* 125 */       this.stripPort = stripPort;
/* 126 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setStripPort(boolean stripPort) {
/* 130 */       this.stripPort = stripPort ? KerberosConfig.Option.ENABLE : KerberosConfig.Option.DISABLE;
/* 131 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setUseCanonicalHostname(KerberosConfig.Option useCanonicalHostname) {
/* 135 */       this.useCanonicalHostname = useCanonicalHostname;
/* 136 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setUseCanonicalHostname(boolean useCanonicalHostname) {
/* 140 */       this.useCanonicalHostname = useCanonicalHostname ? KerberosConfig.Option.ENABLE : KerberosConfig.Option.DISABLE;
/* 141 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setRequestDelegCreds(KerberosConfig.Option requestDelegCreds) {
/* 145 */       this.requestDelegCreds = requestDelegCreds;
/* 146 */       return this;
/*     */     }
/*     */     
/*     */     public KerberosConfig build() {
/* 150 */       return new KerberosConfig(this.stripPort, this.useCanonicalHostname, this.requestDelegCreds);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/auth/KerberosConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */