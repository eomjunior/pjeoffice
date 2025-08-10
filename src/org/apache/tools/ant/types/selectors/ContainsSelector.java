/*     */ package org.apache.tools.ant.types.selectors;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.charset.Charset;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.Parameter;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
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
/*     */ public class ContainsSelector
/*     */   extends BaseExtendSelector
/*     */   implements ResourceSelector
/*     */ {
/*     */   public static final String EXPRESSION_KEY = "expression";
/*     */   public static final String CONTAINS_KEY = "text";
/*     */   public static final String CASE_KEY = "casesensitive";
/*     */   public static final String WHITESPACE_KEY = "ignorewhitespace";
/*  51 */   private String contains = null;
/*     */   private boolean casesensitive = true;
/*     */   private boolean ignorewhitespace = false;
/*  54 */   private String encoding = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  60 */     return String.format("{containsselector text: \"%s\" casesensitive: %s ignorewhitespace: %s}", new Object[] { this.contains, 
/*  61 */           Boolean.valueOf(this.casesensitive), Boolean.valueOf(this.ignorewhitespace) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setText(String contains) {
/*  70 */     this.contains = contains;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEncoding(String encoding) {
/*  79 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCasesensitive(boolean casesensitive) {
/*  88 */     this.casesensitive = casesensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnorewhitespace(boolean ignorewhitespace) {
/*  98 */     this.ignorewhitespace = ignorewhitespace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameters(Parameter... parameters) {
/* 108 */     super.setParameters(parameters);
/* 109 */     if (parameters != null) {
/* 110 */       for (Parameter parameter : parameters) {
/* 111 */         String paramname = parameter.getName();
/* 112 */         if ("text".equalsIgnoreCase(paramname)) {
/* 113 */           setText(parameter.getValue());
/* 114 */         } else if ("casesensitive".equalsIgnoreCase(paramname)) {
/* 115 */           setCasesensitive(Project.toBoolean(parameter
/* 116 */                 .getValue()));
/* 117 */         } else if ("ignorewhitespace".equalsIgnoreCase(paramname)) {
/* 118 */           setIgnorewhitespace(Project.toBoolean(parameter
/* 119 */                 .getValue()));
/*     */         } else {
/* 121 */           setError("Invalid parameter " + paramname);
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
/* 133 */     if (this.contains == null) {
/* 134 */       setError("The text attribute is required");
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
/*     */   public boolean isSelected(File basedir, String filename, File file) {
/* 148 */     return isSelected((Resource)new FileResource(file));
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
/*     */   public boolean isSelected(Resource r) {
/* 160 */     validate();
/*     */     
/* 162 */     if (r.isDirectory() || this.contains.isEmpty()) {
/* 163 */       return true;
/*     */     }
/*     */     
/* 166 */     String userstr = this.contains;
/* 167 */     if (!this.casesensitive) {
/* 168 */       userstr = this.contains.toLowerCase();
/*     */     }
/* 170 */     if (this.ignorewhitespace) {
/* 171 */       userstr = SelectorUtils.removeWhitespace(userstr);
/*     */     }
/*     */ 
/*     */     
/* 175 */     try { BufferedReader in = new BufferedReader(new InputStreamReader(r.getInputStream(), (this.encoding == null) ? Charset.defaultCharset() : Charset.forName(this.encoding)));
/*     */       
/* 177 */       try { String teststr = in.readLine();
/* 178 */         while (teststr != null)
/* 179 */         { if (!this.casesensitive) {
/* 180 */             teststr = teststr.toLowerCase();
/*     */           }
/* 182 */           if (this.ignorewhitespace) {
/* 183 */             teststr = SelectorUtils.removeWhitespace(teststr);
/*     */           }
/* 185 */           if (teststr.contains(userstr))
/* 186 */           { boolean bool1 = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 194 */             in.close(); return bool1; }  teststr = in.readLine(); }  boolean bool = false; in.close(); return bool; } catch (IOException ioe) { throw new BuildException("Could not read " + r.toLongString()); } catch (Throwable throwable) { try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/* 195 */     { throw new BuildException("Could not get InputStream from " + r
/* 196 */           .toLongString(), e); }
/*     */   
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/ContainsSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */