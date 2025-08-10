/*    */ package org.apache.log4j;
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
/*    */ class CategoryKey
/*    */ {
/*    */   String name;
/*    */   int hashCache;
/*    */   
/*    */   CategoryKey(String name) {
/* 32 */     this.name = name;
/* 33 */     this.hashCache = name.hashCode();
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/* 37 */     return this.hashCache;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object rArg) {
/* 41 */     if (this == rArg) {
/* 42 */       return true;
/*    */     }
/* 44 */     if (rArg != null && CategoryKey.class == rArg.getClass()) {
/* 45 */       return this.name.equals(((CategoryKey)rArg).name);
/*    */     }
/* 47 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/CategoryKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */