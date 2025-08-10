/*     */ package org.apache.hc.core5.http.message;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.ReasonPhraseCatalog;
/*     */ import org.apache.hc.core5.http.impl.EnglishReasonPhraseCatalog;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicHttpResponse
/*     */   extends HeaderGroup
/*     */   implements HttpResponse
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final ReasonPhraseCatalog reasonCatalog;
/*     */   private ProtocolVersion version;
/*     */   private Locale locale;
/*     */   private int code;
/*     */   private String reasonPhrase;
/*     */   
/*     */   public BasicHttpResponse(int code, ReasonPhraseCatalog catalog, Locale locale) {
/*  70 */     this.code = Args.positive(code, "Status code");
/*  71 */     this.reasonCatalog = (catalog != null) ? catalog : (ReasonPhraseCatalog)EnglishReasonPhraseCatalog.INSTANCE;
/*  72 */     this.locale = locale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHttpResponse(int code, String reasonPhrase) {
/*  82 */     this.code = Args.positive(code, "Status code");
/*  83 */     this.reasonPhrase = reasonPhrase;
/*  84 */     this.reasonCatalog = (ReasonPhraseCatalog)EnglishReasonPhraseCatalog.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHttpResponse(int code) {
/*  93 */     this.code = Args.positive(code, "Status code");
/*  94 */     this.reasonPhrase = null;
/*  95 */     this.reasonCatalog = (ReasonPhraseCatalog)EnglishReasonPhraseCatalog.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addHeader(String name, Object value) {
/* 100 */     Args.notNull(name, "Header name");
/* 101 */     addHeader(new BasicHeader(name, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeader(String name, Object value) {
/* 106 */     Args.notNull(name, "Header name");
/* 107 */     setHeader(new BasicHeader(name, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVersion(ProtocolVersion version) {
/* 112 */     this.version = version;
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolVersion getVersion() {
/* 117 */     return this.version;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCode() {
/* 122 */     return this.code;
/*     */   }
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 127 */     return this.locale;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCode(int code) {
/* 132 */     Args.positive(code, "Status code");
/* 133 */     this.code = code;
/* 134 */     this.reasonPhrase = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getReasonPhrase() {
/* 139 */     return (this.reasonPhrase != null) ? this.reasonPhrase : getReason(this.code);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setReasonPhrase(String reason) {
/* 144 */     this.reasonPhrase = TextUtils.isBlank(reason) ? null : reason;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLocale(Locale locale) {
/* 149 */     this.locale = (Locale)Args.notNull(locale, "Locale");
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
/*     */   protected String getReason(int code) {
/* 162 */     return (this.reasonCatalog != null) ? this.reasonCatalog.getReason(code, (this.locale != null) ? this.locale : 
/* 163 */         Locale.getDefault()) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 168 */     StringBuilder sb = new StringBuilder();
/* 169 */     sb.append(this.code).append(' ').append(this.reasonPhrase).append(' ').append(this.version);
/* 170 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/BasicHttpResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */