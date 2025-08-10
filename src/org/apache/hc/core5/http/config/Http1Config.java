/*     */ package org.apache.hc.core5.http.config;
/*     */ 
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public class Http1Config
/*     */ {
/*  48 */   public static final Http1Config DEFAULT = (new Builder()).build();
/*     */   
/*     */   private final int bufferSize;
/*     */   
/*     */   private final int chunkSizeHint;
/*     */   private final Timeout waitForContinueTimeout;
/*     */   private final int maxLineLength;
/*     */   private final int maxHeaderCount;
/*     */   private final int maxEmptyLineCount;
/*     */   private final int initialWindowSize;
/*     */   private static final int INIT_WINDOW_SIZE = 65535;
/*     */   private static final int INIT_BUF_SIZE = 8192;
/*     */   
/*     */   Http1Config(int bufferSize, int chunkSizeHint, Timeout waitForContinueTimeout, int maxLineLength, int maxHeaderCount, int maxEmptyLineCount, int initialWindowSize) {
/*  62 */     this.bufferSize = bufferSize;
/*  63 */     this.chunkSizeHint = chunkSizeHint;
/*  64 */     this.waitForContinueTimeout = waitForContinueTimeout;
/*  65 */     this.maxLineLength = maxLineLength;
/*  66 */     this.maxHeaderCount = maxHeaderCount;
/*  67 */     this.maxEmptyLineCount = maxEmptyLineCount;
/*  68 */     this.initialWindowSize = initialWindowSize;
/*     */   }
/*     */   
/*     */   public int getBufferSize() {
/*  72 */     return this.bufferSize;
/*     */   }
/*     */   
/*     */   public int getChunkSizeHint() {
/*  76 */     return this.chunkSizeHint;
/*     */   }
/*     */   
/*     */   public Timeout getWaitForContinueTimeout() {
/*  80 */     return this.waitForContinueTimeout;
/*     */   }
/*     */   
/*     */   public int getMaxLineLength() {
/*  84 */     return this.maxLineLength;
/*     */   }
/*     */   
/*     */   public int getMaxHeaderCount() {
/*  88 */     return this.maxHeaderCount;
/*     */   }
/*     */   
/*     */   public int getMaxEmptyLineCount() {
/*  92 */     return this.maxEmptyLineCount;
/*     */   }
/*     */   
/*     */   public int getInitialWindowSize() {
/*  96 */     return this.initialWindowSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 101 */     StringBuilder builder = new StringBuilder();
/* 102 */     builder.append("[bufferSize=").append(this.bufferSize)
/* 103 */       .append(", chunkSizeHint=").append(this.chunkSizeHint)
/* 104 */       .append(", waitForContinueTimeout=").append(this.waitForContinueTimeout)
/* 105 */       .append(", maxLineLength=").append(this.maxLineLength)
/* 106 */       .append(", maxHeaderCount=").append(this.maxHeaderCount)
/* 107 */       .append(", maxEmptyLineCount=").append(this.maxEmptyLineCount)
/* 108 */       .append(", initialWindowSize=").append(this.initialWindowSize)
/* 109 */       .append("]");
/* 110 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static Builder custom() {
/* 114 */     return new Builder();
/*     */   }
/*     */   public static Builder copy(Http1Config config) {
/* 117 */     Args.notNull(config, "Config");
/* 118 */     return (new Builder())
/* 119 */       .setBufferSize(config.getBufferSize())
/* 120 */       .setChunkSizeHint(config.getChunkSizeHint())
/* 121 */       .setWaitForContinueTimeout(config.getWaitForContinueTimeout())
/* 122 */       .setMaxHeaderCount(config.getMaxHeaderCount())
/* 123 */       .setMaxLineLength(config.getMaxLineLength())
/* 124 */       .setMaxEmptyLineCount(config.getMaxEmptyLineCount())
/* 125 */       .setInitialWindowSize(config.getInitialWindowSize());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 130 */   private static final Timeout INIT_WAIT_FOR_CONTINUE = Timeout.ofSeconds(3L);
/*     */ 
/*     */   
/*     */   private static final int INIT_BUF_CHUNK = -1;
/*     */ 
/*     */   
/*     */   private static final int INIT_MAX_HEADER_COUNT = -1;
/*     */ 
/*     */   
/*     */   private static final int INIT_MAX_LINE_LENGTH = -1;
/*     */ 
/*     */   
/*     */   private static final int INIT_MAX_EMPTY_LINE_COUNT = 10;
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/* 147 */     private int bufferSize = 8192;
/* 148 */     private int chunkSizeHint = -1;
/* 149 */     private Timeout waitForContinueTimeout = Http1Config.INIT_WAIT_FOR_CONTINUE;
/* 150 */     private int maxLineLength = -1;
/* 151 */     private int maxHeaderCount = -1;
/* 152 */     private int maxEmptyLineCount = 10;
/* 153 */     private int initialWindowSize = 65535;
/*     */ 
/*     */     
/*     */     public Builder setBufferSize(int bufferSize) {
/* 157 */       this.bufferSize = bufferSize;
/* 158 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setChunkSizeHint(int chunkSizeHint) {
/* 162 */       this.chunkSizeHint = chunkSizeHint;
/* 163 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setWaitForContinueTimeout(Timeout waitForContinueTimeout) {
/* 167 */       this.waitForContinueTimeout = waitForContinueTimeout;
/* 168 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setMaxLineLength(int maxLineLength) {
/* 172 */       this.maxLineLength = maxLineLength;
/* 173 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setMaxHeaderCount(int maxHeaderCount) {
/* 177 */       this.maxHeaderCount = maxHeaderCount;
/* 178 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setMaxEmptyLineCount(int maxEmptyLineCount) {
/* 182 */       this.maxEmptyLineCount = maxEmptyLineCount;
/* 183 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setInitialWindowSize(int initialWindowSize) {
/* 187 */       Args.positive(initialWindowSize, "Initial window size");
/* 188 */       this.initialWindowSize = initialWindowSize;
/* 189 */       return this;
/*     */     }
/*     */     
/*     */     public Http1Config build() {
/* 193 */       return new Http1Config(this.bufferSize, this.chunkSizeHint, this.waitForContinueTimeout, this.maxLineLength, this.maxHeaderCount, this.maxEmptyLineCount, this.initialWindowSize);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/config/Http1Config.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */