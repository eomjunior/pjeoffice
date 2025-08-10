/*     */ package org.apache.tools.ant.types.resources.selectors;
/*     */ 
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.RegularExpression;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.selectors.SelectorUtils;
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
/*     */ public class Name
/*     */   implements ResourceSelector
/*     */ {
/*  32 */   private String regex = null;
/*     */   
/*     */   private String pattern;
/*     */   
/*     */   private boolean cs = true;
/*     */   
/*     */   private boolean handleDirSep = false;
/*     */   private RegularExpression reg;
/*     */   private Regexp expression;
/*     */   private Project project;
/*     */   
/*     */   public void setProject(Project p) {
/*  44 */     this.project = p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String n) {
/*  52 */     this.pattern = n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  60 */     return this.pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRegex(String r) {
/*  69 */     this.regex = r;
/*  70 */     this.reg = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRegex() {
/*  79 */     return this.regex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCaseSensitive(boolean b) {
/*  87 */     this.cs = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCaseSensitive() {
/*  95 */     return this.cs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandleDirSep(boolean handleDirSep) {
/* 105 */     this.handleDirSep = handleDirSep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doesHandledirSep() {
/* 115 */     return this.handleDirSep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSelected(Resource r) {
/* 124 */     String n = r.getName();
/* 125 */     if (matches(n)) {
/* 126 */       return true;
/*     */     }
/* 128 */     String s = r.toString();
/* 129 */     return (!s.equals(n) && matches(s));
/*     */   }
/*     */   
/*     */   private boolean matches(String name) {
/* 133 */     if (this.pattern != null) {
/* 134 */       return SelectorUtils.match(modify(this.pattern), modify(name), this.cs);
/*     */     }
/* 136 */     if (this.reg == null) {
/* 137 */       this.reg = new RegularExpression();
/* 138 */       this.reg.setPattern(this.regex);
/* 139 */       this.expression = this.reg.getRegexp(this.project);
/*     */     } 
/* 141 */     return this.expression.matches(modify(name), RegexpUtil.asOptions(this.cs));
/*     */   }
/*     */   
/*     */   private String modify(String s) {
/* 145 */     if (s == null || !this.handleDirSep || !s.contains("\\")) {
/* 146 */       return s;
/*     */     }
/* 148 */     return s.replace('\\', '/');
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/selectors/Name.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */