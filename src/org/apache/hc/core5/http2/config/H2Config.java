/*     */ package org.apache.hc.core5.http2.config;
/*     */ 
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class H2Config
/*     */ {
/*  43 */   public static final H2Config DEFAULT = custom().build();
/*  44 */   public static final H2Config INIT = initial().build();
/*     */   
/*     */   private final int headerTableSize;
/*     */   
/*     */   private final boolean pushEnabled;
/*     */   
/*     */   private final int maxConcurrentStreams;
/*     */   
/*     */   private final int initialWindowSize;
/*     */   
/*     */   private final int maxFrameSize;
/*     */   private final int maxHeaderListSize;
/*     */   
/*     */   H2Config(int headerTableSize, boolean pushEnabled, int maxConcurrentStreams, int initialWindowSize, int maxFrameSize, int maxHeaderListSize, boolean compressionEnabled) {
/*  58 */     this.headerTableSize = headerTableSize;
/*  59 */     this.pushEnabled = pushEnabled;
/*  60 */     this.maxConcurrentStreams = maxConcurrentStreams;
/*  61 */     this.initialWindowSize = initialWindowSize;
/*  62 */     this.maxFrameSize = maxFrameSize;
/*  63 */     this.maxHeaderListSize = maxHeaderListSize;
/*  64 */     this.compressionEnabled = compressionEnabled;
/*     */   }
/*     */   private final boolean compressionEnabled; private static final int INIT_HEADER_TABLE_SIZE = 4096; private static final boolean INIT_ENABLE_PUSH = true; private static final int INIT_MAX_FRAME_SIZE = 16384; private static final int INIT_WINDOW_SIZE = 65535; private static final int INIT_CONCURRENT_STREAM = 250;
/*     */   public int getHeaderTableSize() {
/*  68 */     return this.headerTableSize;
/*     */   }
/*     */   
/*     */   public boolean isPushEnabled() {
/*  72 */     return this.pushEnabled;
/*     */   }
/*     */   
/*     */   public int getMaxConcurrentStreams() {
/*  76 */     return this.maxConcurrentStreams;
/*     */   }
/*     */   
/*     */   public int getInitialWindowSize() {
/*  80 */     return this.initialWindowSize;
/*     */   }
/*     */   
/*     */   public int getMaxFrameSize() {
/*  84 */     return this.maxFrameSize;
/*     */   }
/*     */   
/*     */   public int getMaxHeaderListSize() {
/*  88 */     return this.maxHeaderListSize;
/*     */   }
/*     */   
/*     */   public boolean isCompressionEnabled() {
/*  92 */     return this.compressionEnabled;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  97 */     StringBuilder builder = new StringBuilder();
/*  98 */     builder.append("[headerTableSize=").append(this.headerTableSize)
/*  99 */       .append(", pushEnabled=").append(this.pushEnabled)
/* 100 */       .append(", maxConcurrentStreams=").append(this.maxConcurrentStreams)
/* 101 */       .append(", initialWindowSize=").append(this.initialWindowSize)
/* 102 */       .append(", maxFrameSize=").append(this.maxFrameSize)
/* 103 */       .append(", maxHeaderListSize=").append(this.maxHeaderListSize)
/* 104 */       .append(", compressionEnabled=").append(this.compressionEnabled)
/* 105 */       .append("]");
/* 106 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static Builder custom() {
/* 110 */     return new Builder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder initial() {
/* 120 */     return (new Builder())
/* 121 */       .setHeaderTableSize(4096)
/* 122 */       .setPushEnabled(true)
/* 123 */       .setMaxConcurrentStreams(2147483647)
/* 124 */       .setMaxFrameSize(16384)
/* 125 */       .setInitialWindowSize(65535)
/* 126 */       .setMaxHeaderListSize(2147483647);
/*     */   }
/*     */   
/*     */   public static Builder copy(H2Config config) {
/* 130 */     Args.notNull(config, "Connection config");
/* 131 */     return (new Builder())
/* 132 */       .setHeaderTableSize(config.getHeaderTableSize())
/* 133 */       .setPushEnabled(config.isPushEnabled())
/* 134 */       .setMaxConcurrentStreams(config.getMaxConcurrentStreams())
/* 135 */       .setInitialWindowSize(config.getInitialWindowSize())
/* 136 */       .setMaxFrameSize(config.getMaxFrameSize())
/* 137 */       .setMaxHeaderListSize(config.getMaxHeaderListSize())
/* 138 */       .setCompressionEnabled(config.isCompressionEnabled());
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
/*     */   public static class Builder
/*     */   {
/* 152 */     private int headerTableSize = 8192;
/*     */     private boolean pushEnabled = true;
/* 154 */     private int maxConcurrentStreams = 250;
/* 155 */     private int initialWindowSize = 65535;
/* 156 */     private int maxFrameSize = 65536;
/* 157 */     private int maxHeaderListSize = 16777215;
/*     */     
/*     */     private boolean compressionEnabled = true;
/*     */     
/*     */     public Builder setHeaderTableSize(int headerTableSize) {
/* 162 */       Args.notNegative(headerTableSize, "Header table size");
/* 163 */       this.headerTableSize = headerTableSize;
/* 164 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setPushEnabled(boolean pushEnabled) {
/* 168 */       this.pushEnabled = pushEnabled;
/* 169 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setMaxConcurrentStreams(int maxConcurrentStreams) {
/* 173 */       Args.positive(maxConcurrentStreams, "Max concurrent streams");
/* 174 */       this.maxConcurrentStreams = maxConcurrentStreams;
/* 175 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setInitialWindowSize(int initialWindowSize) {
/* 179 */       Args.positive(initialWindowSize, "Initial window size");
/* 180 */       this.initialWindowSize = initialWindowSize;
/* 181 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setMaxFrameSize(int maxFrameSize) {
/* 185 */       this.maxFrameSize = Args.checkRange(maxFrameSize, 16384, 16777215, "Invalid max frame size");
/*     */       
/* 187 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setMaxHeaderListSize(int maxHeaderListSize) {
/* 191 */       Args.positive(maxHeaderListSize, "Max header list size");
/* 192 */       this.maxHeaderListSize = maxHeaderListSize;
/* 193 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setCompressionEnabled(boolean compressionEnabled) {
/* 197 */       this.compressionEnabled = compressionEnabled;
/* 198 */       return this;
/*     */     }
/*     */     
/*     */     public H2Config build() {
/* 202 */       return new H2Config(this.headerTableSize, this.pushEnabled, this.maxConcurrentStreams, this.initialWindowSize, this.maxFrameSize, this.maxHeaderListSize, this.compressionEnabled);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/config/H2Config.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */