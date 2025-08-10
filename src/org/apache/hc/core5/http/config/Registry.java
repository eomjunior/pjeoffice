/*    */ package org.apache.hc.core5.http.config;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.util.TextUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.SAFE)
/*    */ public final class Registry<I>
/*    */   implements Lookup<I>
/*    */ {
/*    */   private final Map<String, I> map;
/*    */   
/*    */   Registry(Map<String, I> map) {
/* 49 */     this.map = new ConcurrentHashMap<>(map);
/*    */   }
/*    */ 
/*    */   
/*    */   public I lookup(String key) {
/* 54 */     if (key == null) {
/* 55 */       return null;
/*    */     }
/* 57 */     return this.map.get(TextUtils.toLowerCase(key));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 62 */     return this.map.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/config/Registry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */