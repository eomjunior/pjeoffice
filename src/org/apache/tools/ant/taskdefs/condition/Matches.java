/*     */ package org.apache.tools.ant.taskdefs.condition;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.types.RegularExpression;
/*     */ import org.apache.tools.ant.util.regexp.Regexp;
/*     */ import org.apache.tools.ant.util.regexp.RegexpUtil;
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
/*     */ public class Matches
/*     */   extends ProjectComponent
/*     */   implements Condition
/*     */ {
/*     */   private String string;
/*     */   private boolean caseSensitive = true;
/*     */   private boolean multiLine = false;
/*     */   private boolean singleLine = false;
/*     */   private RegularExpression regularExpression;
/*     */   
/*     */   public void setString(String string) {
/*  45 */     this.string = string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPattern(String pattern) {
/*  54 */     if (this.regularExpression != null) {
/*  55 */       throw new BuildException("Only one regular expression is allowed.");
/*     */     }
/*     */     
/*  58 */     this.regularExpression = new RegularExpression();
/*  59 */     this.regularExpression.setPattern(pattern);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addRegexp(RegularExpression regularExpression) {
/*  70 */     if (this.regularExpression != null) {
/*  71 */       throw new BuildException("Only one regular expression is allowed.");
/*     */     }
/*     */     
/*  74 */     this.regularExpression = regularExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCasesensitive(boolean b) {
/*  83 */     this.caseSensitive = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMultiline(boolean b) {
/*  91 */     this.multiLine = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSingleLine(boolean b) {
/* 100 */     this.singleLine = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean eval() throws BuildException {
/* 108 */     if (this.string == null) {
/* 109 */       throw new BuildException("Parameter string is required in matches.");
/*     */     }
/*     */     
/* 112 */     if (this.regularExpression == null) {
/* 113 */       throw new BuildException("Missing pattern in matches.");
/*     */     }
/* 115 */     int options = RegexpUtil.asOptions(this.caseSensitive, this.multiLine, this.singleLine);
/* 116 */     Regexp regexp = this.regularExpression.getRegexp(getProject());
/* 117 */     return regexp.matches(this.string, options);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/Matches.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */