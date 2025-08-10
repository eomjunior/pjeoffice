/*    */ package org.apache.hc.core5.http.protocol;
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
/*    */ public enum UriPatternType
/*    */ {
/* 35 */   REGEX, URI_PATTERN, URI_PATTERN_IN_ORDER;
/*    */   
/*    */   public static <T> LookupRegistry<T> newMatcher(UriPatternType type) {
/* 38 */     if (type == null) {
/* 39 */       return new UriPatternMatcher<>();
/*    */     }
/* 41 */     switch (type) {
/*    */       case REGEX:
/* 43 */         return new UriRegexMatcher<>();
/*    */       case URI_PATTERN_IN_ORDER:
/* 45 */         return new UriPatternOrderedMatcher<>();
/*    */     } 
/*    */     
/* 48 */     return new UriPatternMatcher<>();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/protocol/UriPatternType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */