/*     */ package org.apache.hc.core5.http.protocol;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class UriPatternOrderedMatcher<T>
/*     */   implements LookupRegistry<T>
/*     */ {
/*  65 */   private final Map<String, T> map = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Set<Map.Entry<String, T>> entrySet() {
/*  77 */     return new HashSet<>(this.map.entrySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void register(String pattern, T obj) {
/*  88 */     Args.notNull(pattern, "URI request pattern");
/*  89 */     this.map.put(pattern, obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void unregister(String pattern) {
/*  99 */     if (pattern == null) {
/*     */       return;
/*     */     }
/* 102 */     this.map.remove(pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized T lookup(String path) {
/* 113 */     Args.notNull(path, "Request path");
/* 114 */     for (Map.Entry<String, T> entry : this.map.entrySet()) {
/* 115 */       String pattern = entry.getKey();
/* 116 */       if (path.equals(pattern)) {
/* 117 */         return entry.getValue();
/*     */       }
/* 119 */       if (matchUriRequestPattern(pattern, path)) {
/* 120 */         return this.map.get(pattern);
/*     */       }
/*     */     } 
/* 123 */     return null;
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
/*     */   protected boolean matchUriRequestPattern(String pattern, String path) {
/* 135 */     if (pattern.equals("*")) {
/* 136 */       return true;
/*     */     }
/* 138 */     return ((pattern.endsWith("*") && path.startsWith(pattern.substring(0, pattern.length() - 1))) || (pattern
/* 139 */       .startsWith("*") && path.endsWith(pattern.substring(1))));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 144 */     return this.map.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/protocol/UriPatternOrderedMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */