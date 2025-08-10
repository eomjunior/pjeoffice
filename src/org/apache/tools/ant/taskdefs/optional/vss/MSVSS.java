/*     */ package org.apache.tools.ant.taskdefs.optional.vss;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.Execute;
/*     */ import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
/*     */ import org.apache.tools.ant.taskdefs.LogStreamHandler;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.util.FileUtils;
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
/*     */ public abstract class MSVSS
/*     */   extends Task
/*     */   implements MSVSSConstants
/*     */ {
/*  53 */   private String ssDir = null;
/*  54 */   private String vssLogin = null;
/*  55 */   private String vssPath = null;
/*  56 */   private String serverPath = null;
/*     */ 
/*     */   
/*  59 */   private String version = null;
/*     */   
/*  61 */   private String date = null;
/*     */   
/*  63 */   private String label = null;
/*     */   
/*  65 */   private String autoResponse = null;
/*     */   
/*  67 */   private String localPath = null;
/*     */   
/*  69 */   private String comment = null;
/*     */   
/*  71 */   private String fromLabel = null;
/*     */   
/*  73 */   private String toLabel = null;
/*     */   
/*  75 */   private String outputFileName = null;
/*     */   
/*  77 */   private String user = null;
/*     */   
/*  79 */   private String fromDate = null;
/*     */   
/*  81 */   private String toDate = null;
/*     */   
/*  83 */   private String style = null;
/*     */   
/*     */   private boolean quiet = false;
/*     */   
/*     */   private boolean recursive = false;
/*     */   
/*     */   private boolean writable = false;
/*     */   
/*     */   private boolean failOnError = true;
/*     */   
/*     */   private boolean getLocalCopy = true;
/*     */   
/*  95 */   private int numDays = Integer.MIN_VALUE;
/*     */   
/*  97 */   private DateFormat dateFormat = DateFormat.getDateInstance(3);
/*     */   
/*  99 */   private CurrentModUpdated timestamp = null;
/*     */   
/* 101 */   private WritableFiles writableFiles = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Commandline buildCmdLine();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setSsdir(String dir) {
/* 117 */     this.ssDir = FileUtils.translatePath(dir);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setLogin(String vssLogin) {
/* 128 */     this.vssLogin = vssLogin;
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
/*     */   public final void setVsspath(String vssPath) {
/*     */     String projectPath;
/* 142 */     if (vssPath.startsWith("vss://")) {
/* 143 */       projectPath = vssPath.substring(5);
/*     */     } else {
/* 145 */       projectPath = vssPath;
/*     */     } 
/*     */ 
/*     */     
/* 149 */     if (projectPath.startsWith("$")) {
/* 150 */       this.vssPath = projectPath;
/*     */     } else {
/* 152 */       this.vssPath = "$" + projectPath;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setServerpath(String serverPath) {
/* 161 */     this.serverPath = serverPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setFailOnError(boolean failOnError) {
/* 169 */     this.failOnError = failOnError;
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
/*     */   public void execute() throws BuildException {
/* 182 */     Commandline commandLine = buildCmdLine();
/* 183 */     int result = run(commandLine);
/* 184 */     if (Execute.isFailure(result) && getFailOnError()) {
/* 185 */       String msg = "Failed executing: " + formatCommandLine(commandLine) + " With a return code of " + result;
/*     */       
/* 187 */       throw new BuildException(msg, getLocation());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalComment(String comment) {
/* 198 */     this.comment = comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalAutoResponse(String autoResponse) {
/* 206 */     this.autoResponse = autoResponse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalDate(String date) {
/* 214 */     this.date = date;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalDateFormat(DateFormat dateFormat) {
/* 222 */     this.dateFormat = dateFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalFailOnError(boolean failOnError) {
/* 230 */     this.failOnError = failOnError;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalFromDate(String fromDate) {
/* 238 */     this.fromDate = fromDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalFromLabel(String fromLabel) {
/* 246 */     this.fromLabel = fromLabel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalLabel(String label) {
/* 254 */     this.label = label;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalLocalPath(String localPath) {
/* 262 */     this.localPath = localPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalNumDays(int numDays) {
/* 270 */     this.numDays = numDays;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalOutputFilename(String outputFileName) {
/* 278 */     this.outputFileName = outputFileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalQuiet(boolean quiet) {
/* 286 */     this.quiet = quiet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalRecursive(boolean recursive) {
/* 294 */     this.recursive = recursive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalStyle(String style) {
/* 302 */     this.style = style;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalToDate(String toDate) {
/* 310 */     this.toDate = toDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalToLabel(String toLabel) {
/* 318 */     this.toLabel = toLabel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalUser(String user) {
/* 326 */     this.user = user;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalVersion(String version) {
/* 334 */     this.version = version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalWritable(boolean writable) {
/* 342 */     this.writable = writable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalFileTimeStamp(CurrentModUpdated timestamp) {
/* 350 */     this.timestamp = timestamp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalWritableFiles(WritableFiles writableFiles) {
/* 358 */     this.writableFiles = writableFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalGetLocalCopy(boolean getLocalCopy) {
/* 366 */     this.getLocalCopy = getLocalCopy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getSSCommand() {
/* 374 */     if (this.ssDir == null) {
/* 375 */       return "ss";
/*     */     }
/* 377 */     return this.ssDir.endsWith(File.separator) ? (this.ssDir + "ss") : (
/* 378 */       this.ssDir + File.separator + "ss");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getVsspath() {
/* 386 */     return this.vssPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getQuiet() {
/* 394 */     return this.quiet ? "-O-" : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getRecursive() {
/* 402 */     return this.recursive ? "-R" : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getWritable() {
/* 410 */     return this.writable ? "-W" : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getLabel() {
/* 419 */     String shortLabel = "";
/* 420 */     if (this.label != null && !this.label.isEmpty()) {
/* 421 */       shortLabel = "-L" + getShortLabel();
/*     */     }
/* 423 */     return shortLabel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getShortLabel() {
/*     */     String shortLabel;
/* 433 */     if (this.label != null && this.label.length() > 31) {
/* 434 */       shortLabel = this.label.substring(0, 30);
/* 435 */       log("Label is longer than 31 characters, truncated to: " + shortLabel, 1);
/*     */     } else {
/*     */       
/* 438 */       shortLabel = this.label;
/*     */     } 
/*     */     
/* 441 */     return shortLabel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getStyle() {
/* 448 */     return (this.style != null) ? this.style : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getVersionDateLabel() {
/* 457 */     String versionDateLabel = "";
/* 458 */     if (this.version != null) {
/* 459 */       versionDateLabel = "-V" + this.version;
/* 460 */     } else if (this.date != null) {
/* 461 */       versionDateLabel = "-Vd" + this.date;
/*     */     }
/*     */     else {
/*     */       
/* 465 */       String shortLabel = getShortLabel();
/* 466 */       if (shortLabel != null && !shortLabel.isEmpty()) {
/* 467 */         versionDateLabel = "-VL" + shortLabel;
/*     */       }
/*     */     } 
/* 470 */     return versionDateLabel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getVersion() {
/* 478 */     return (this.version != null) ? ("-V" + this.version) : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getLocalpath() {
/* 487 */     String lclPath = "";
/* 488 */     if (this.localPath != null) {
/*     */       
/* 490 */       File dir = getProject().resolveFile(this.localPath);
/* 491 */       if (!dir.exists()) {
/* 492 */         boolean done = (dir.mkdirs() || dir.exists());
/* 493 */         if (!done) {
/* 494 */           String msg = "Directory " + this.localPath + " creation was not successful for an unknown reason";
/*     */           
/* 496 */           throw new BuildException(msg, getLocation());
/*     */         } 
/* 498 */         getProject().log("Created dir: " + dir.getAbsolutePath());
/*     */       } 
/* 500 */       lclPath = "-GL" + this.localPath;
/*     */     } 
/* 502 */     return lclPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getComment() {
/* 510 */     return (this.comment != null) ? ("-C" + this.comment) : "-C-";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getAutoresponse() {
/* 518 */     if (this.autoResponse == null) {
/* 519 */       return "-I-";
/*     */     }
/* 521 */     if (this.autoResponse.equalsIgnoreCase("Y"))
/* 522 */       return "-I-Y"; 
/* 523 */     if (this.autoResponse.equalsIgnoreCase("N")) {
/* 524 */       return "-I-N";
/*     */     }
/* 526 */     return "-I-";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getLogin() {
/* 536 */     return (this.vssLogin != null) ? ("-Y" + this.vssLogin) : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getOutput() {
/* 544 */     return (this.outputFileName != null) ? ("-O" + this.outputFileName) : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUser() {
/* 552 */     return (this.user != null) ? ("-U" + this.user) : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getVersionLabel() {
/* 561 */     if (this.fromLabel == null && this.toLabel == null) {
/* 562 */       return "";
/*     */     }
/*     */     
/* 565 */     if (this.fromLabel != null && this.toLabel != null) {
/* 566 */       if (this.fromLabel.length() > 31) {
/* 567 */         this.fromLabel = this.fromLabel.substring(0, 30);
/* 568 */         log("FromLabel is longer than 31 characters, truncated to: " + this.fromLabel, 1);
/*     */       } 
/*     */       
/* 571 */       if (this.toLabel.length() > 31) {
/* 572 */         this.toLabel = this.toLabel.substring(0, 30);
/* 573 */         log("ToLabel is longer than 31 characters, truncated to: " + this.toLabel, 1);
/*     */       } 
/*     */       
/* 576 */       return "-VL" + this.toLabel + "~L" + this.fromLabel;
/* 577 */     }  if (this.fromLabel != null) {
/* 578 */       if (this.fromLabel.length() > 31) {
/* 579 */         this.fromLabel = this.fromLabel.substring(0, 30);
/* 580 */         log("FromLabel is longer than 31 characters, truncated to: " + this.fromLabel, 1);
/*     */       } 
/*     */       
/* 583 */       return "-V~L" + this.fromLabel;
/*     */     } 
/* 585 */     if (this.toLabel.length() > 31) {
/* 586 */       this.toLabel = this.toLabel.substring(0, 30);
/* 587 */       log("ToLabel is longer than 31 characters, truncated to: " + this.toLabel, 1);
/*     */     } 
/*     */     
/* 590 */     return "-VL" + this.toLabel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getVersionDate() throws BuildException {
/* 601 */     if (this.fromDate == null && this.toDate == null && this.numDays == Integer.MIN_VALUE)
/*     */     {
/* 603 */       return "";
/*     */     }
/* 605 */     if (this.fromDate != null && this.toDate != null)
/* 606 */       return "-Vd" + this.toDate + "~d" + this.fromDate; 
/* 607 */     if (this.toDate != null && this.numDays != Integer.MIN_VALUE)
/*     */       try {
/* 609 */         return "-Vd" + this.toDate + "~d" + 
/* 610 */           calcDate(this.toDate, this.numDays);
/* 611 */       } catch (ParseException ex) {
/* 612 */         String msg = "Error parsing date: " + this.toDate;
/* 613 */         throw new BuildException(msg, getLocation());
/*     */       }  
/* 615 */     if (this.fromDate != null && this.numDays != Integer.MIN_VALUE) {
/*     */       try {
/* 617 */         return "-Vd" + calcDate(this.fromDate, this.numDays) + "~d" + this.fromDate;
/*     */       }
/* 619 */       catch (ParseException ex) {
/* 620 */         String msg = "Error parsing date: " + this.fromDate;
/* 621 */         throw new BuildException(msg, getLocation());
/*     */       } 
/*     */     }
/* 624 */     return (this.fromDate != null) ? (
/* 625 */       "-V~d" + this.fromDate) : ("-Vd" + this.toDate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getGetLocalCopy() {
/* 634 */     return !this.getLocalCopy ? "-G-" : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean getFailOnError() {
/* 642 */     return (!getWritableFiles().equals("skip") && this.failOnError);
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
/*     */   public String getFileTimeStamp() {
/* 656 */     if (this.timestamp == null)
/* 657 */       return ""; 
/* 658 */     if (this.timestamp.getValue().equals("modified"))
/* 659 */       return "-GTM"; 
/* 660 */     if (this.timestamp.getValue().equals("updated")) {
/* 661 */       return "-GTU";
/*     */     }
/* 663 */     return "-GTC";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWritableFiles() {
/* 673 */     if (this.writableFiles == null)
/* 674 */       return ""; 
/* 675 */     if (this.writableFiles.getValue().equals("replace"))
/* 676 */       return "-GWR"; 
/* 677 */     if (this.writableFiles.getValue().equals("skip")) {
/*     */ 
/*     */       
/* 680 */       this.failOnError = false;
/* 681 */       return "-GWS";
/*     */     } 
/* 683 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int run(Commandline cmd) {
/*     */     try {
/* 695 */       Execute exe = new Execute((ExecuteStreamHandler)new LogStreamHandler(this, 2, 1));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 701 */       if (this.serverPath != null) {
/* 702 */         String[] env = exe.getEnvironment();
/* 703 */         if (env == null) {
/* 704 */           env = new String[0];
/*     */         }
/* 706 */         String[] newEnv = new String[env.length + 1];
/* 707 */         System.arraycopy(env, 0, newEnv, 0, env.length);
/* 708 */         newEnv[env.length] = "SSDIR=" + this.serverPath;
/*     */         
/* 710 */         exe.setEnvironment(newEnv);
/*     */       } 
/*     */       
/* 713 */       exe.setAntRun(getProject());
/* 714 */       exe.setWorkingDirectory(getProject().getBaseDir());
/* 715 */       exe.setCommandline(cmd.getCommandline());
/*     */       
/* 717 */       exe.setVMLauncher(false);
/* 718 */       return exe.execute();
/* 719 */     } catch (IOException e) {
/* 720 */       throw new BuildException(e, getLocation());
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
/*     */   private String calcDate(String startDate, int daysToAdd) throws ParseException {
/* 734 */     Calendar calendar = new GregorianCalendar();
/* 735 */     Date currentDate = this.dateFormat.parse(startDate);
/* 736 */     calendar.setTime(currentDate);
/* 737 */     calendar.add(5, daysToAdd);
/* 738 */     return this.dateFormat.format(calendar.getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String formatCommandLine(Commandline cmd) {
/* 748 */     StringBuilder sBuff = new StringBuilder(cmd.toString());
/* 749 */     int indexUser = sBuff.substring(0).indexOf("-Y");
/* 750 */     if (indexUser > 0) {
/* 751 */       int indexPass = sBuff.substring(0).indexOf(",", indexUser);
/* 752 */       int indexAfterPass = sBuff.substring(0).indexOf(" ", indexPass);
/*     */       
/* 754 */       for (int i = indexPass + 1; i < indexAfterPass; i++) {
/* 755 */         sBuff.setCharAt(i, '*');
/*     */       }
/*     */     } 
/* 758 */     return sBuff.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class CurrentModUpdated
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public String[] getValues() {
/* 770 */       return new String[] { "current", "modified", "updated" };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class WritableFiles
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public String[] getValues() {
/* 783 */       return new String[] { "replace", "skip", "fail" };
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/vss/MSVSS.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */