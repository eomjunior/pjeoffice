/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.util.regexp.Regexp;
/*     */ import org.apache.tools.ant.util.regexp.RegexpFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RegularExpression
/*     */   extends DataType
/*     */ {
/*     */   public static final String DATA_TYPE_NAME = "regexp";
/*     */   private boolean alreadyInit = false;
/*  65 */   private static final RegexpFactory FACTORY = new RegexpFactory();
/*     */   
/*  67 */   private Regexp regexp = null;
/*     */   
/*     */   private String myPattern;
/*     */   private boolean setPatternPending = false;
/*     */   
/*     */   private void init(Project p) {
/*  73 */     if (!this.alreadyInit) {
/*  74 */       this.regexp = FACTORY.newRegexp(p);
/*  75 */       this.alreadyInit = true;
/*     */     } 
/*     */   }
/*     */   private void setPattern() {
/*  79 */     if (this.setPatternPending) {
/*  80 */       this.regexp.setPattern(this.myPattern);
/*  81 */       this.setPatternPending = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPattern(String pattern) {
/*  89 */     if (this.regexp == null) {
/*  90 */       this.myPattern = pattern;
/*  91 */       this.setPatternPending = true;
/*     */     } else {
/*  93 */       this.regexp.setPattern(pattern);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPattern(Project p) {
/* 104 */     init(p);
/* 105 */     if (isReference()) {
/* 106 */       return getRef(p).getPattern(p);
/*     */     }
/* 108 */     setPattern();
/* 109 */     return this.regexp.getPattern();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Regexp getRegexp(Project p) {
/* 118 */     init(p);
/* 119 */     if (isReference()) {
/* 120 */       return getRef(p).getRegexp(p);
/*     */     }
/* 122 */     setPattern();
/* 123 */     return this.regexp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RegularExpression getRef(Project p) {
/* 133 */     return getCheckedRef(RegularExpression.class, getDataTypeName(), p);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/RegularExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */