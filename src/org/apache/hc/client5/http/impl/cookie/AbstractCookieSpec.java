/*     */ package org.apache.hc.client5.http.impl.cookie;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.hc.client5.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.hc.client5.http.cookie.CookieAttributeHandler;
/*     */ import org.apache.hc.client5.http.cookie.CookieSpec;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.util.Asserts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.SAFE)
/*     */ public abstract class AbstractCookieSpec
/*     */   implements CookieSpec
/*     */ {
/*     */   private final Map<String, CookieAttributeHandler> attribHandlerMap;
/*     */   
/*     */   public AbstractCookieSpec() {
/*  62 */     this.attribHandlerMap = new ConcurrentHashMap<>(10);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractCookieSpec(HashMap<String, CookieAttributeHandler> map) {
/*  70 */     Asserts.notNull(map, "Attribute handler map");
/*  71 */     this.attribHandlerMap = new ConcurrentHashMap<>(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractCookieSpec(CommonCookieAttributeHandler... handlers) {
/*  79 */     this.attribHandlerMap = new ConcurrentHashMap<>(handlers.length);
/*  80 */     for (CommonCookieAttributeHandler handler : handlers) {
/*  81 */       this.attribHandlerMap.put(handler.getAttributeName(), handler);
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
/*     */   
/*     */   protected CookieAttributeHandler findAttribHandler(String name) {
/*  94 */     return this.attribHandlerMap.get(name);
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
/*     */   protected CookieAttributeHandler getAttribHandler(String name) {
/* 106 */     CookieAttributeHandler handler = findAttribHandler(name);
/* 107 */     Asserts.check((handler != null), "Handler not registered for " + name + " attribute");
/*     */     
/* 109 */     return handler;
/*     */   }
/*     */   
/*     */   protected Collection<CookieAttributeHandler> getAttribHandlers() {
/* 113 */     return this.attribHandlerMap.values();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/cookie/AbstractCookieSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */