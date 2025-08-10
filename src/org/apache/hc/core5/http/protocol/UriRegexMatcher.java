/*     */ package org.apache.hc.core5.http.protocol;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
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
/*     */ @Contract(threading = ThreadingBehavior.SAFE)
/*     */ public class UriRegexMatcher<T>
/*     */   implements LookupRegistry<T>
/*     */ {
/*  58 */   private final Map<String, T> objectMap = new LinkedHashMap<>();
/*  59 */   private final Map<String, Pattern> patternMap = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void register(String regex, T obj) {
/*  72 */     Args.notNull(regex, "URI request regex");
/*  73 */     this.objectMap.put(regex, obj);
/*  74 */     this.patternMap.put(regex, Pattern.compile(regex));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void unregister(String regex) {
/*  85 */     if (regex == null) {
/*     */       return;
/*     */     }
/*  88 */     this.objectMap.remove(regex);
/*  89 */     this.patternMap.remove(regex);
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
/* 101 */     Args.notNull(path, "Request path");
/*     */     
/* 103 */     T obj = this.objectMap.get(path);
/* 104 */     if (obj == null)
/*     */     {
/* 106 */       for (Map.Entry<String, Pattern> entry : this.patternMap.entrySet()) {
/* 107 */         if (((Pattern)entry.getValue()).matcher(path).matches()) {
/* 108 */           return this.objectMap.get(entry.getKey());
/*     */         }
/*     */       } 
/*     */     }
/* 112 */     return obj;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 117 */     return this.objectMap.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/protocol/UriRegexMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */