/*     */ package org.apache.tools.ant.types.selectors;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.Parameter;
/*     */ import org.apache.tools.ant.types.RegularExpression;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
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
/*     */ 
/*     */ public class ContainsRegexpSelector
/*     */   extends BaseExtendSelector
/*     */   implements ResourceSelector
/*     */ {
/*     */   public static final String EXPRESSION_KEY = "expression";
/*     */   private static final String CS_KEY = "casesensitive";
/*     */   private static final String ML_KEY = "multiline";
/*     */   private static final String SL_KEY = "singleline";
/*  53 */   private String userProvidedExpression = null;
/*  54 */   private RegularExpression myRegExp = null;
/*  55 */   private Regexp myExpression = null;
/*     */   
/*     */   private boolean caseSensitive = true;
/*     */   
/*     */   private boolean multiLine = false;
/*     */   
/*     */   private boolean singleLine = false;
/*     */   
/*     */   public String toString() {
/*  64 */     return String.format("{containsregexpselector expression: %s}", new Object[] { this.userProvidedExpression });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpression(String theexpression) {
/*  74 */     this.userProvidedExpression = theexpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCaseSensitive(boolean b) {
/*  83 */     this.caseSensitive = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMultiLine(boolean b) {
/*  92 */     this.multiLine = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSingleLine(boolean b) {
/* 102 */     this.singleLine = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameters(Parameter... parameters) {
/* 112 */     super.setParameters(parameters);
/* 113 */     if (parameters != null) {
/* 114 */       for (Parameter parameter : parameters) {
/* 115 */         String paramname = parameter.getName();
/* 116 */         if ("expression".equalsIgnoreCase(paramname)) {
/* 117 */           setExpression(parameter.getValue());
/* 118 */         } else if ("casesensitive".equalsIgnoreCase(paramname)) {
/* 119 */           setCaseSensitive(Project.toBoolean(parameter.getValue()));
/* 120 */         } else if ("multiline".equalsIgnoreCase(paramname)) {
/* 121 */           setMultiLine(Project.toBoolean(parameter.getValue()));
/* 122 */         } else if ("singleline".equalsIgnoreCase(paramname)) {
/* 123 */           setSingleLine(Project.toBoolean(parameter.getValue()));
/*     */         } else {
/* 125 */           setError("Invalid parameter " + paramname);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void verifySettings() {
/* 136 */     if (this.userProvidedExpression == null) {
/* 137 */       setError("The expression attribute is required");
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
/*     */   public boolean isSelected(File basedir, String filename, File file) {
/* 150 */     return isSelected((Resource)new FileResource(file));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSelected(Resource r) {
/* 161 */     validate();
/*     */     
/* 163 */     if (r.isDirectory()) {
/* 164 */       return true;
/*     */     }
/*     */     
/* 167 */     if (this.myRegExp == null) {
/* 168 */       this.myRegExp = new RegularExpression();
/* 169 */       this.myRegExp.setPattern(this.userProvidedExpression);
/* 170 */       this.myExpression = this.myRegExp.getRegexp(getProject());
/*     */     } 
/*     */ 
/*     */     
/* 174 */     try { BufferedReader in = new BufferedReader(new InputStreamReader(r.getInputStream()));
/*     */       
/* 176 */       try { String teststr = in.readLine();
/*     */         
/* 178 */         while (teststr != null)
/* 179 */         { if (this.myExpression.matches(teststr, 
/* 180 */               RegexpUtil.asOptions(this.caseSensitive, this.multiLine, this.singleLine)))
/* 181 */           { boolean bool1 = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 189 */             in.close(); return bool1; }  teststr = in.readLine(); }  boolean bool = false; in.close(); return bool; } catch (IOException ioe) { throw new BuildException("Could not read " + r.toLongString()); } catch (Throwable throwable) { try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/* 190 */     { throw new BuildException("Could not get InputStream from " + r
/* 191 */           .toLongString(), e); }
/*     */   
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/ContainsRegexpSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */