/*     */ package org.apache.tools.ant.taskdefs.optional.windows;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.taskdefs.ExecuteOn;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
/*     */ import org.apache.tools.ant.types.FileSet;
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
/*     */ public class Attrib
/*     */   extends ExecuteOn
/*     */ {
/*     */   private static final String ATTR_READONLY = "R";
/*     */   private static final String ATTR_ARCHIVE = "A";
/*     */   private static final String ATTR_SYSTEM = "S";
/*     */   private static final String ATTR_HIDDEN = "H";
/*     */   private static final String SET = "+";
/*     */   private static final String UNSET = "-";
/*     */   private boolean haveAttr = false;
/*     */   
/*     */   public Attrib() {
/*  47 */     super.setExecutable("attrib");
/*  48 */     super.setParallel(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File src) {
/*  56 */     FileSet fs = new FileSet();
/*  57 */     fs.setFile(src);
/*  58 */     addFileset(fs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadonly(boolean value) {
/*  66 */     addArg(value, "R");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArchive(boolean value) {
/*  74 */     addArg(value, "A");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSystem(boolean value) {
/*  82 */     addArg(value, "S");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHidden(boolean value) {
/*  90 */     addArg(value, "H");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkConfiguration() {
/*  98 */     if (!haveAttr()) {
/*  99 */       throw new BuildException("Missing attribute parameter", 
/* 100 */           getLocation());
/*     */     }
/* 102 */     super.checkConfiguration();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExecutable(String e) {
/* 113 */     throw new BuildException(getTaskType() + " doesn't support the executable attribute", 
/* 114 */         getLocation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommand(String e) {
/* 124 */     throw new BuildException(getTaskType() + " doesn't support the command attribute", 
/* 125 */         getLocation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAddsourcefile(boolean b) {
/* 136 */     throw new BuildException(getTaskType() + " doesn't support the addsourcefile attribute", 
/* 137 */         getLocation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSkipEmptyFilesets(boolean skip) {
/* 148 */     throw new BuildException(getTaskType() + " doesn't support the skipemptyfileset attribute", 
/*     */         
/* 150 */         getLocation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParallel(boolean parallel) {
/* 161 */     throw new BuildException(getTaskType() + " doesn't support the parallel attribute", 
/*     */         
/* 163 */         getLocation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxParallel(int max) {
/* 174 */     throw new BuildException(getTaskType() + " doesn't support the maxparallel attribute", 
/*     */         
/* 176 */         getLocation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isValidOs() {
/* 186 */     return (getOs() == null && getOsFamily() == null) ? 
/* 187 */       Os.isFamily("windows") : super.isValidOs();
/*     */   }
/*     */   
/*     */   private static String getSignString(boolean attr) {
/* 191 */     return attr ? "+" : "-";
/*     */   }
/*     */   
/*     */   private void addArg(boolean sign, String attribute) {
/* 195 */     createArg().setValue(getSignString(sign) + attribute);
/* 196 */     this.haveAttr = true;
/*     */   }
/*     */   
/*     */   private boolean haveAttr() {
/* 200 */     return this.haveAttr;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/windows/Attrib.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */