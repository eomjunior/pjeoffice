/*    */ package org.apache.hc.core5.http.config;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ public final class RegistryBuilder<I>
/*    */ {
/*    */   private final Map<String, I> items;
/*    */   
/*    */   public static <I> RegistryBuilder<I> create() {
/* 46 */     return new RegistryBuilder<>();
/*    */   }
/*    */ 
/*    */   
/*    */   RegistryBuilder() {
/* 51 */     this.items = new HashMap<>();
/*    */   }
/*    */   
/*    */   public RegistryBuilder<I> register(String id, I item) {
/* 55 */     Args.notEmpty(id, "ID");
/* 56 */     Args.notNull(item, "Item");
/* 57 */     this.items.put(TextUtils.toLowerCase(id), item);
/* 58 */     return this;
/*    */   }
/*    */   
/*    */   public Registry<I> build() {
/* 62 */     return new Registry<>(this.items);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 67 */     return this.items.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/config/RegistryBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */