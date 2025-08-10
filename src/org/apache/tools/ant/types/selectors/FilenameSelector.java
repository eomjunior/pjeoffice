/*     */ package org.apache.tools.ant.types.selectors;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.Parameter;
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
/*     */ 
/*     */ 
/*     */ public class FilenameSelector
/*     */   extends BaseExtendSelector
/*     */ {
/*     */   public static final String NAME_KEY = "name";
/*     */   public static final String CASE_KEY = "casesensitive";
/*     */   public static final String NEGATE_KEY = "negate";
/*     */   public static final String REGEX_KEY = "regex";
/*  44 */   private String pattern = null;
/*  45 */   private String regex = null;
/*     */ 
/*     */   
/*     */   private boolean casesensitive = true;
/*     */   
/*     */   private boolean negated = false;
/*     */   
/*     */   private RegularExpression reg;
/*     */   
/*     */   private Regexp expression;
/*     */ 
/*     */   
/*     */   public String toString() {
/*  58 */     StringBuilder buf = new StringBuilder("{filenameselector name: ");
/*  59 */     if (this.pattern != null) {
/*  60 */       buf.append(this.pattern);
/*     */     }
/*  62 */     if (this.regex != null) {
/*  63 */       buf.append(this.regex).append(" [as regular expression]");
/*     */     }
/*  65 */     buf.append(" negate: ").append(this.negated);
/*  66 */     buf.append(" casesensitive: ").append(this.casesensitive);
/*  67 */     buf.append("}");
/*  68 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String pattern) {
/*  79 */     pattern = pattern.replace('/', File.separatorChar).replace('\\', File.separatorChar);
/*     */     
/*  81 */     if (pattern.endsWith(File.separator)) {
/*  82 */       pattern = pattern + "**";
/*     */     }
/*  84 */     this.pattern = pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRegex(String pattern) {
/*  94 */     this.regex = pattern;
/*  95 */     this.reg = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCasesensitive(boolean casesensitive) {
/* 104 */     this.casesensitive = casesensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNegate(boolean negated) {
/* 116 */     this.negated = negated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameters(Parameter... parameters) {
/* 126 */     super.setParameters(parameters);
/* 127 */     if (parameters != null) {
/* 128 */       for (Parameter parameter : parameters) {
/* 129 */         String paramname = parameter.getName();
/* 130 */         if ("name".equalsIgnoreCase(paramname)) {
/* 131 */           setName(parameter.getValue());
/* 132 */         } else if ("casesensitive".equalsIgnoreCase(paramname)) {
/* 133 */           setCasesensitive(Project.toBoolean(parameter
/* 134 */                 .getValue()));
/* 135 */         } else if ("negate".equalsIgnoreCase(paramname)) {
/* 136 */           setNegate(Project.toBoolean(parameter.getValue()));
/* 137 */         } else if ("regex".equalsIgnoreCase(paramname)) {
/* 138 */           setRegex(parameter.getValue());
/*     */         } else {
/* 140 */           setError("Invalid parameter " + paramname);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void verifySettings() {
/* 152 */     if (this.pattern == null && this.regex == null) {
/* 153 */       setError("The name or regex attribute is required");
/* 154 */     } else if (this.pattern != null && this.regex != null) {
/* 155 */       setError("Only one of name and regex attribute is allowed");
/*     */     } 
/*     */   }
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
/*     */   public boolean isSelected(File basedir, String filename, File file) {
/* 172 */     validate();
/* 173 */     if (this.pattern != null) {
/* 174 */       return (SelectorUtils.matchPath(this.pattern, filename, this.casesensitive) == (!this.negated));
/*     */     }
/*     */     
/* 177 */     if (this.reg == null) {
/* 178 */       this.reg = new RegularExpression();
/* 179 */       this.reg.setPattern(this.regex);
/* 180 */       this.expression = this.reg.getRegexp(getProject());
/*     */     } 
/* 182 */     int options = RegexpUtil.asOptions(this.casesensitive);
/* 183 */     return (this.expression.matches(filename, options) == (!this.negated));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/FilenameSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */