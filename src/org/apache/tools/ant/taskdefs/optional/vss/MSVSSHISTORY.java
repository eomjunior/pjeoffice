/*     */ package org.apache.tools.ant.taskdefs.optional.vss;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.text.SimpleDateFormat;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
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
/*     */ public class MSVSSHISTORY
/*     */   extends MSVSS
/*     */ {
/*     */   Commandline buildCmdLine() {
/*  40 */     Commandline commandLine = new Commandline();
/*     */ 
/*     */     
/*  43 */     if (getVsspath() == null) {
/*  44 */       String msg = "vsspath attribute must be set!";
/*  45 */       throw new BuildException(msg, getLocation());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  51 */     commandLine.setExecutable(getSSCommand());
/*  52 */     commandLine.createArgument().setValue("History");
/*     */ 
/*     */     
/*  55 */     commandLine.createArgument().setValue(getVsspath());
/*     */     
/*  57 */     commandLine.createArgument().setValue("-I-");
/*     */     
/*  59 */     commandLine.createArgument().setValue(getVersionDate());
/*     */     
/*  61 */     commandLine.createArgument().setValue(getVersionLabel());
/*     */     
/*  63 */     commandLine.createArgument().setValue(getRecursive());
/*     */     
/*  65 */     commandLine.createArgument().setValue(getStyle());
/*     */     
/*  67 */     commandLine.createArgument().setValue(getLogin());
/*     */     
/*  69 */     commandLine.createArgument().setValue(getOutput());
/*     */     
/*  71 */     return commandLine;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRecursive(boolean recursive) {
/*  80 */     setInternalRecursive(recursive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUser(String user) {
/*  89 */     setInternalUser(user);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFromDate(String fromDate) {
/*  98 */     setInternalFromDate(fromDate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToDate(String toDate) {
/* 107 */     setInternalToDate(toDate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFromLabel(String fromLabel) {
/* 116 */     setInternalFromLabel(fromLabel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setToLabel(String toLabel) {
/* 125 */     setInternalToLabel(toLabel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNumdays(int numd) {
/* 135 */     setInternalNumDays(numd);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutput(File outfile) {
/* 144 */     if (outfile != null) {
/* 145 */       setInternalOutputFilename(outfile.getAbsolutePath());
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
/*     */   public void setDateFormat(String dateFormat) {
/* 158 */     setInternalDateFormat(new SimpleDateFormat(dateFormat));
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
/*     */   public void setStyle(BriefCodediffNofile attr) {
/* 173 */     String option = attr.getValue();
/* 174 */     switch (option) {
/*     */       case "brief":
/* 176 */         setInternalStyle("-B");
/*     */         return;
/*     */       case "codediff":
/* 179 */         setInternalStyle("-D");
/*     */         return;
/*     */       case "default":
/* 182 */         setInternalStyle("");
/*     */         return;
/*     */       case "nofile":
/* 185 */         setInternalStyle("-F-");
/*     */         return;
/*     */     } 
/* 188 */     throw new BuildException("Style " + attr + " unknown.", getLocation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class BriefCodediffNofile
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public String[] getValues() {
/* 201 */       return new String[] { "brief", "codediff", "nofile", "default" };
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/vss/MSVSSHISTORY.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */