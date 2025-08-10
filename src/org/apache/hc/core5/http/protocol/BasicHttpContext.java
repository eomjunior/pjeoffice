/*     */ package org.apache.hc.core5.http.protocol;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
/*     */ public class BasicHttpContext
/*     */   implements HttpContext
/*     */ {
/*     */   private final HttpContext parentContext;
/*     */   private final Map<String, Object> map;
/*     */   private ProtocolVersion version;
/*     */   
/*     */   public BasicHttpContext() {
/*  56 */     this(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicHttpContext(HttpContext parentContext) {
/*  61 */     this.map = new ConcurrentHashMap<>();
/*  62 */     this.parentContext = parentContext;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getAttribute(String id) {
/*  67 */     Args.notNull(id, "Id");
/*  68 */     Object obj = this.map.get(id);
/*  69 */     if (obj == null && this.parentContext != null) {
/*  70 */       obj = this.parentContext.getAttribute(id);
/*     */     }
/*  72 */     return obj;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object setAttribute(String id, Object obj) {
/*  77 */     Args.notNull(id, "Id");
/*  78 */     if (obj != null) {
/*  79 */       return this.map.put(id, obj);
/*     */     }
/*  81 */     return this.map.remove(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object removeAttribute(String id) {
/*  86 */     Args.notNull(id, "Id");
/*  87 */     return this.map.remove(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/*  95 */     return (this.version != null) ? this.version : (ProtocolVersion)HttpVersion.DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProtocolVersion(ProtocolVersion version) {
/* 103 */     this.version = version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 110 */     this.map.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 115 */     return this.map.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/protocol/BasicHttpContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */