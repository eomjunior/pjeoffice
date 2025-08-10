/*     */ package org.apache.tools.ant.types.selectors;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.Parameter;
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
/*     */ public class DepthSelector
/*     */   extends BaseExtendSelector
/*     */ {
/*     */   public static final String MIN_KEY = "min";
/*     */   public static final String MAX_KEY = "max";
/*  43 */   public int min = -1;
/*     */   
/*  45 */   public int max = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  53 */     return "{depthselector min: " + this.min + " max: " + this.max + "}";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMin(int min) {
/*  62 */     this.min = min;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMax(int max) {
/*  71 */     this.max = max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameters(Parameter... parameters) {
/*  81 */     super.setParameters(parameters);
/*  82 */     if (parameters != null) {
/*  83 */       for (Parameter parameter : parameters) {
/*  84 */         String paramname = parameter.getName();
/*  85 */         if ("min".equalsIgnoreCase(paramname)) {
/*     */           try {
/*  87 */             setMin(Integer.parseInt(parameter.getValue()));
/*  88 */           } catch (NumberFormatException nfe1) {
/*  89 */             setError("Invalid minimum value " + parameter
/*  90 */                 .getValue());
/*     */           } 
/*  92 */         } else if ("max".equalsIgnoreCase(paramname)) {
/*     */           try {
/*  94 */             setMax(Integer.parseInt(parameter.getValue()));
/*  95 */           } catch (NumberFormatException nfe1) {
/*  96 */             setError("Invalid maximum value " + parameter
/*  97 */                 .getValue());
/*     */           } 
/*     */         } else {
/* 100 */           setError("Invalid parameter " + paramname);
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
/* 111 */     if (this.min < 0 && this.max < 0) {
/* 112 */       setError("You must set at least one of the min or the max levels.");
/*     */     }
/* 114 */     if (this.max < this.min && this.max > -1) {
/* 115 */       setError("The maximum depth is lower than the minimum.");
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
/*     */ 
/*     */   
/*     */   public boolean isSelected(File basedir, String filename, File file) {
/* 134 */     validate();
/*     */     
/* 136 */     int depth = -1;
/*     */     
/* 138 */     String absBase = basedir.getAbsolutePath();
/* 139 */     String absFile = file.getAbsolutePath();
/* 140 */     StringTokenizer tokBase = new StringTokenizer(absBase, File.separator);
/* 141 */     StringTokenizer tokFile = new StringTokenizer(absFile, File.separator);
/* 142 */     while (tokFile.hasMoreTokens()) {
/* 143 */       String filetoken = tokFile.nextToken();
/* 144 */       if (tokBase.hasMoreTokens()) {
/* 145 */         String basetoken = tokBase.nextToken();
/*     */         
/* 147 */         if (!basetoken.equals(filetoken)) {
/* 148 */           throw new BuildException("File %s does not appear within %s directory", new Object[] { filename, absBase });
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/* 153 */       depth++;
/* 154 */       if (this.max > -1 && depth > this.max) {
/* 155 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 159 */     if (tokBase.hasMoreTokens()) {
/* 160 */       throw new BuildException("File %s is outside of %s directory tree", new Object[] { filename, absBase });
/*     */     }
/*     */     
/* 163 */     return (this.min <= -1 || depth >= this.min);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/DepthSelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */