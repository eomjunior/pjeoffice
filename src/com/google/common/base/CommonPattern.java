/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible
/*    */ abstract class CommonPattern
/*    */ {
/*    */   public abstract CommonMatcher matcher(CharSequence paramCharSequence);
/*    */   
/*    */   public abstract String pattern();
/*    */   
/*    */   public abstract int flags();
/*    */   
/*    */   public abstract String toString();
/*    */   
/*    */   public static CommonPattern compile(String pattern) {
/* 38 */     return Platform.compilePattern(pattern);
/*    */   }
/*    */   
/*    */   public static boolean isPcreLike() {
/* 42 */     return Platform.patternCompilerIsPcreLike();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/CommonPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */