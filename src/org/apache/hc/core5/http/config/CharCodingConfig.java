/*     */ package org.apache.hc.core5.http.config;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.nio.charset.StandardCharsets;
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
/*     */ public class CharCodingConfig
/*     */ {
/*  46 */   public static final CharCodingConfig DEFAULT = (new Builder()).build();
/*     */ 
/*     */   
/*     */   private final Charset charset;
/*     */   
/*     */   private final CodingErrorAction malformedInputAction;
/*     */   
/*     */   private final CodingErrorAction unmappableInputAction;
/*     */ 
/*     */   
/*     */   CharCodingConfig(Charset charset, CodingErrorAction malformedInputAction, CodingErrorAction unmappableInputAction) {
/*  57 */     this.charset = charset;
/*  58 */     this.malformedInputAction = malformedInputAction;
/*  59 */     this.unmappableInputAction = unmappableInputAction;
/*     */   }
/*     */   
/*     */   public Charset getCharset() {
/*  63 */     return this.charset;
/*     */   }
/*     */   
/*     */   public CodingErrorAction getMalformedInputAction() {
/*  67 */     return this.malformedInputAction;
/*     */   }
/*     */   
/*     */   public CodingErrorAction getUnmappableInputAction() {
/*  71 */     return this.unmappableInputAction;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  76 */     StringBuilder builder = new StringBuilder();
/*  77 */     builder.append("[charset=").append(this.charset)
/*  78 */       .append(", malformedInputAction=").append(this.malformedInputAction)
/*  79 */       .append(", unmappableInputAction=").append(this.unmappableInputAction)
/*  80 */       .append("]");
/*  81 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public static Builder custom() {
/*  85 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static Builder copy(CharCodingConfig config) {
/*  89 */     Args.notNull(config, "Config");
/*  90 */     return (new Builder())
/*  91 */       .setCharset(config.getCharset())
/*  92 */       .setMalformedInputAction(config.getMalformedInputAction())
/*  93 */       .setUnmappableInputAction(config.getUnmappableInputAction());
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private Charset charset;
/*     */     
/*     */     private CodingErrorAction malformedInputAction;
/*     */     
/*     */     private CodingErrorAction unmappableInputAction;
/*     */     
/*     */     public Builder setCharset(Charset charset) {
/* 106 */       this.charset = charset;
/* 107 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setMalformedInputAction(CodingErrorAction malformedInputAction) {
/* 111 */       this.malformedInputAction = malformedInputAction;
/* 112 */       if (malformedInputAction != null && this.charset == null) {
/* 113 */         this.charset = StandardCharsets.US_ASCII;
/*     */       }
/* 115 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setUnmappableInputAction(CodingErrorAction unmappableInputAction) {
/* 119 */       this.unmappableInputAction = unmappableInputAction;
/* 120 */       if (unmappableInputAction != null && this.charset == null) {
/* 121 */         this.charset = StandardCharsets.US_ASCII;
/*     */       }
/* 123 */       return this;
/*     */     }
/*     */     
/*     */     public CharCodingConfig build() {
/* 127 */       Charset charsetCopy = this.charset;
/* 128 */       if (charsetCopy == null && (this.malformedInputAction != null || this.unmappableInputAction != null)) {
/* 129 */         charsetCopy = StandardCharsets.US_ASCII;
/*     */       }
/* 131 */       return new CharCodingConfig(charsetCopy, this.malformedInputAction, this.unmappableInputAction);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/config/CharCodingConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */