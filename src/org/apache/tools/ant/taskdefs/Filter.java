/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
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
/*     */ public class Filter
/*     */   extends Task
/*     */ {
/*     */   private String token;
/*     */   private String value;
/*     */   private File filtersFile;
/*     */   
/*     */   public void setToken(String token) {
/*  47 */     this.token = token;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String value) {
/*  55 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFiltersfile(File filtersFile) {
/*  65 */     this.filtersFile = filtersFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*  74 */     boolean isFiltersFromFile = (this.filtersFile != null && this.token == null && this.value == null);
/*     */     
/*  76 */     boolean isSingleFilter = (this.filtersFile == null && this.token != null && this.value != null);
/*     */ 
/*     */     
/*  79 */     if (!isFiltersFromFile && !isSingleFilter) {
/*  80 */       throw new BuildException("both token and value parameters, or only a filtersFile parameter is required", 
/*     */           
/*  82 */           getLocation());
/*     */     }
/*     */     
/*  85 */     if (isSingleFilter) {
/*  86 */       getProject().getGlobalFilterSet().addFilter(this.token, this.value);
/*     */     }
/*     */     
/*  89 */     if (isFiltersFromFile) {
/*  90 */       readFilters();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readFilters() throws BuildException {
/*  99 */     log("Reading filters from " + this.filtersFile, 3);
/* 100 */     getProject().getGlobalFilterSet().readFiltersFromFile(this.filtersFile);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Filter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */