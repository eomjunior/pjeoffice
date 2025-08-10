/*    */ package com.yworks.yshrink.ant.filters;
/*    */ 
/*    */ import com.yworks.common.ant.PatternMatchedSection;
/*    */ import com.yworks.common.ant.TypePatternSet;
/*    */ import org.apache.tools.ant.Project;
/*    */ import org.apache.tools.ant.types.PatternSet;
/*    */ import org.apache.tools.ant.types.selectors.SelectorUtils;
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
/*    */ public class PatternMatchedFilter
/*    */   extends AbstractEntryPointFilter
/*    */ {
/*    */   private Project project;
/*    */   
/*    */   public PatternMatchedFilter(Project p) {
/* 24 */     this.project = p;
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
/*    */   
/*    */   protected boolean match(TypePatternSet.Type type, String str, PatternMatchedSection section) {
/* 37 */     PatternSet patternSet = section.getPatternSet(type);
/*    */     
/* 39 */     if (patternSet != null) {
/*    */       
/* 41 */       String[] excludePatterns = patternSet.getExcludePatterns(this.project);
/* 42 */       if (null != excludePatterns) {
/* 43 */         for (String excludePattern : excludePatterns) {
/* 44 */           if (SelectorUtils.match(excludePattern, str)) {
/* 45 */             return false;
/*    */           }
/*    */         } 
/*    */       }
/*    */       
/* 50 */       String[] includePatterns = patternSet.getIncludePatterns(this.project);
/* 51 */       if (null != includePatterns) {
/* 52 */         for (String includePattern : includePatterns) {
/* 53 */           if (SelectorUtils.match(includePattern, str)) {
/* 54 */             return true;
/*    */           }
/*    */         } 
/*    */       } else {
/* 58 */         return true;
/*    */       } 
/*    */     } else {
/* 61 */       return true;
/*    */     } 
/* 63 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/ant/filters/PatternMatchedFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */