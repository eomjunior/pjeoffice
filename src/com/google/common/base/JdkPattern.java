/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import java.io.Serializable;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
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
/*    */ @J2ktIncompatible
/*    */ @GwtIncompatible
/*    */ final class JdkPattern
/*    */   extends CommonPattern
/*    */   implements Serializable
/*    */ {
/*    */   private final Pattern pattern;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   JdkPattern(Pattern pattern) {
/* 31 */     this.pattern = Preconditions.<Pattern>checkNotNull(pattern);
/*    */   }
/*    */ 
/*    */   
/*    */   public CommonMatcher matcher(CharSequence t) {
/* 36 */     return new JdkMatcher(this.pattern.matcher(t));
/*    */   }
/*    */ 
/*    */   
/*    */   public String pattern() {
/* 41 */     return this.pattern.pattern();
/*    */   }
/*    */ 
/*    */   
/*    */   public int flags() {
/* 46 */     return this.pattern.flags();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 51 */     return this.pattern.toString();
/*    */   }
/*    */   
/*    */   private static final class JdkMatcher extends CommonMatcher {
/*    */     final Matcher matcher;
/*    */     
/*    */     JdkMatcher(Matcher matcher) {
/* 58 */       this.matcher = Preconditions.<Matcher>checkNotNull(matcher);
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean matches() {
/* 63 */       return this.matcher.matches();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean find() {
/* 68 */       return this.matcher.find();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean find(int index) {
/* 73 */       return this.matcher.find(index);
/*    */     }
/*    */ 
/*    */     
/*    */     public String replaceAll(String replacement) {
/* 78 */       return this.matcher.replaceAll(replacement);
/*    */     }
/*    */ 
/*    */     
/*    */     public int end() {
/* 83 */       return this.matcher.end();
/*    */     }
/*    */ 
/*    */     
/*    */     public int start() {
/* 88 */       return this.matcher.start();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/base/JdkPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */