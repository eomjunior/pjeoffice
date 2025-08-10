/*    */ package org.apache.hc.core5.http2.impl.nio.bootstrap;
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
/*    */ class FilterEntry<T>
/*    */ {
/*    */   final Position position;
/*    */   final String name;
/*    */   final T filterHandler;
/*    */   final String existing;
/*    */   
/*    */   enum Position
/*    */   {
/* 32 */     BEFORE, AFTER, REPLACE, FIRST, LAST;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   FilterEntry(Position position, String name, T filterHandler, String existing) {
/* 44 */     this.position = position;
/* 45 */     this.name = name;
/* 46 */     this.filterHandler = filterHandler;
/* 47 */     this.existing = existing;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/bootstrap/FilterEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */