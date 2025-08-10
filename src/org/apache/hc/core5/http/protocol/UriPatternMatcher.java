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
/*     */ public class UriPatternMatcher<T>
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
/*     */ 
/*     */   
/*     */   public synchronized void register(String pattern, T obj) {
/*  90 */     Args.notNull(pattern, "URI request pattern");
/*  91 */     this.map.put(pattern, obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void unregister(String pattern) {
/* 102 */     if (pattern == null) {
/*     */       return;
/*     */     }
/* 105 */     this.map.remove(pattern);
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
/*     */   public synchronized T lookup(String path) {
/* 117 */     Args.notNull(path, "Request path");
/*     */     
/* 119 */     T obj = this.map.get(path);
/* 120 */     if (obj == null) {
/*     */       
/* 122 */       String bestMatch = null;
/* 123 */       for (String pattern : this.map.keySet()) {
/* 124 */         if (matchUriRequestPattern(pattern, path))
/*     */         {
/* 126 */           if (bestMatch == null || bestMatch.length() < pattern.length() || (bestMatch
/* 127 */             .length() == pattern.length() && pattern.endsWith("*"))) {
/* 128 */             obj = this.map.get(pattern);
/* 129 */             bestMatch = pattern;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/* 134 */     return obj;
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
/*     */   protected boolean matchUriRequestPattern(String pattern, String path) {
/* 147 */     if (pattern.equals("*")) {
/* 148 */       return true;
/*     */     }
/* 150 */     return ((pattern.endsWith("*") && path.startsWith(pattern.substring(0, pattern.length() - 1))) || (pattern
/* 151 */       .startsWith("*") && path.endsWith(pattern.substring(1))));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 156 */     return this.map.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/protocol/UriPatternMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */