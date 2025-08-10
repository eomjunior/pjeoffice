/*     */ package org.apache.tools.ant.taskdefs.cvslib;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.taskdefs.AbstractCvsTask;
/*     */ import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
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
/*     */ public class ChangeLogTask
/*     */   extends AbstractCvsTask
/*     */ {
/*     */   private File usersFile;
/*  73 */   private List<CvsUser> cvsUsers = new Vector<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private File inputDir;
/*     */ 
/*     */ 
/*     */   
/*     */   private File destFile;
/*     */ 
/*     */ 
/*     */   
/*     */   private Date startDate;
/*     */ 
/*     */ 
/*     */   
/*     */   private Date endDate;
/*     */ 
/*     */   
/*     */   private boolean remote = false;
/*     */ 
/*     */   
/*     */   private String startTag;
/*     */ 
/*     */   
/*     */   private String endTag;
/*     */ 
/*     */   
/* 101 */   private final List<FileSet> filesets = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDir(File inputDir) {
/* 109 */     this.inputDir = inputDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestfile(File destFile) {
/* 118 */     this.destFile = destFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUsersfile(File usersFile) {
/* 127 */     this.usersFile = usersFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addUser(CvsUser user) {
/* 136 */     this.cvsUsers.add(user);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStart(Date start) {
/* 145 */     this.startDate = start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnd(Date endDate) {
/* 154 */     this.endDate = endDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDaysinpast(int days) {
/* 164 */     long time = System.currentTimeMillis() - days * 24L * 60L * 60L * 1000L;
/*     */ 
/*     */ 
/*     */     
/* 168 */     setStart(new Date(time));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemote(boolean remote) {
/* 179 */     this.remote = remote;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStartTag(String start) {
/* 188 */     this.startTag = start;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEndTag(String end) {
/* 197 */     this.endTag = end;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet fileSet) {
/* 206 */     this.filesets.add(fileSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 217 */     File savedDir = this.inputDir;
/*     */     
/*     */     try {
/* 220 */       validate();
/* 221 */       Properties userList = new Properties();
/*     */       
/* 223 */       loadUserlist(userList);
/*     */       
/* 225 */       for (CvsUser user : this.cvsUsers) {
/* 226 */         user.validate();
/* 227 */         userList.put(user.getUserID(), user.getDisplayname());
/*     */       } 
/*     */       
/* 230 */       if (!this.remote) {
/* 231 */         setCommand("log");
/*     */         
/* 233 */         if (getTag() != null) {
/* 234 */           CvsVersion myCvsVersion = new CvsVersion();
/* 235 */           myCvsVersion.setProject(getProject());
/* 236 */           myCvsVersion.setTaskName("cvsversion");
/* 237 */           myCvsVersion.setCvsRoot(getCvsRoot());
/* 238 */           myCvsVersion.setCvsRsh(getCvsRsh());
/* 239 */           myCvsVersion.setPassfile(getPassFile());
/* 240 */           myCvsVersion.setDest(this.inputDir);
/* 241 */           myCvsVersion.execute();
/* 242 */           if (myCvsVersion.supportsCvsLogWithSOption()) {
/* 243 */             addCommandArgument("-S");
/*     */           }
/*     */         } 
/*     */       } else {
/*     */         
/* 248 */         setCommand("");
/* 249 */         addCommandArgument("rlog");
/*     */ 
/*     */         
/* 252 */         addCommandArgument("-S");
/*     */ 
/*     */         
/* 255 */         addCommandArgument("-N");
/*     */       } 
/* 257 */       if (null != this.startTag || null != this.endTag) {
/*     */         
/* 259 */         String startValue = (this.startTag == null) ? "" : this.startTag;
/* 260 */         String endValue = (this.endTag == null) ? "" : this.endTag;
/* 261 */         addCommandArgument("-r" + startValue + "::" + endValue);
/* 262 */       } else if (null != this.startDate) {
/* 263 */         SimpleDateFormat outputDate = new SimpleDateFormat("yyyy-MM-dd");
/*     */ 
/*     */ 
/*     */         
/* 267 */         String dateRange = ">=" + outputDate.format(this.startDate);
/*     */ 
/*     */         
/* 270 */         addCommandArgument("-d");
/* 271 */         addCommandArgument(dateRange);
/*     */       } 
/*     */ 
/*     */       
/* 275 */       for (FileSet fileSet : this.filesets) {
/*     */         
/* 277 */         DirectoryScanner scanner = fileSet.getDirectoryScanner(getProject());
/* 278 */         for (String file : scanner.getIncludedFiles()) {
/* 279 */           addCommandArgument(file);
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 285 */       ChangeLogParser parser = new ChangeLogParser(this.remote, getPackage(), getModules());
/* 286 */       RedirectingStreamHandler handler = new RedirectingStreamHandler(parser);
/*     */ 
/*     */       
/* 289 */       log(getCommand(), 3);
/*     */       
/* 291 */       setDest(this.inputDir);
/* 292 */       setExecuteStreamHandler((ExecuteStreamHandler)handler);
/*     */       try {
/* 294 */         super.execute();
/*     */       } finally {
/* 296 */         String errors = handler.getErrors();
/*     */         
/* 298 */         if (null != errors) {
/* 299 */           log(errors, 0);
/*     */         }
/*     */       } 
/* 302 */       CVSEntry[] entrySet = parser.getEntrySetAsArray();
/* 303 */       CVSEntry[] filteredEntrySet = filterEntrySet(entrySet);
/*     */       
/* 305 */       replaceAuthorIdWithName(userList, filteredEntrySet);
/*     */       
/* 307 */       writeChangeLog(filteredEntrySet);
/*     */     } finally {
/*     */       
/* 310 */       this.inputDir = savedDir;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validate() throws BuildException {
/* 321 */     if (null == this.inputDir) {
/* 322 */       this.inputDir = getProject().getBaseDir();
/*     */     }
/* 324 */     if (null == this.destFile) {
/* 325 */       throw new BuildException("Destfile must be set.");
/*     */     }
/* 327 */     if (!this.inputDir.exists()) {
/* 328 */       throw new BuildException("Cannot find base dir %s", new Object[] { this.inputDir
/* 329 */             .getAbsolutePath() });
/*     */     }
/* 331 */     if (null != this.usersFile && !this.usersFile.exists()) {
/* 332 */       throw new BuildException("Cannot find user lookup list %s", new Object[] { this.usersFile
/* 333 */             .getAbsolutePath() });
/*     */     }
/* 335 */     if ((null != this.startTag || null != this.endTag) && (null != this.startDate || null != this.endDate))
/*     */     {
/* 337 */       throw new BuildException("Specify either a tag or date range, not both");
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
/*     */   private void loadUserlist(Properties userList) throws BuildException {
/* 351 */     if (null != this.usersFile) {
/*     */       try {
/* 353 */         userList.load(Files.newInputStream(this.usersFile.toPath(), new java.nio.file.OpenOption[0]));
/* 354 */       } catch (IOException ioe) {
/* 355 */         throw new BuildException(ioe.toString(), ioe);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CVSEntry[] filterEntrySet(CVSEntry[] entrySet) {
/* 367 */     List<CVSEntry> results = new ArrayList<>();
/*     */     
/* 369 */     for (CVSEntry cvsEntry : entrySet) {
/* 370 */       Date date = cvsEntry.getDate();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 379 */       if (null != date)
/*     */       {
/*     */ 
/*     */         
/* 383 */         if (null == this.startDate || !this.startDate.after(date))
/*     */         {
/*     */ 
/*     */           
/* 387 */           if (null == this.endDate || !this.endDate.before(date))
/*     */           {
/*     */ 
/*     */             
/* 391 */             results.add(cvsEntry); }  } 
/*     */       }
/*     */     } 
/* 394 */     return results.<CVSEntry>toArray(new CVSEntry[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void replaceAuthorIdWithName(Properties userList, CVSEntry[] entrySet) {
/* 402 */     for (CVSEntry entry : entrySet) {
/* 403 */       if (userList.containsKey(entry.getAuthor())) {
/* 404 */         entry.setAuthor(userList.getProperty(entry.getAuthor()));
/*     */       }
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
/*     */   private void writeChangeLog(CVSEntry[] entrySet) throws BuildException {
/*     */     
/* 419 */     try { PrintWriter writer = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(this.destFile.toPath(), new java.nio.file.OpenOption[0]), StandardCharsets.UTF_8));
/*     */       
/* 421 */       try { (new ChangeLogWriter()).printChangeLog(writer, entrySet);
/*     */         
/* 423 */         if (writer.checkError()) {
/* 424 */           throw new IOException("Encountered an error writing changelog");
/*     */         }
/* 426 */         writer.close(); } catch (Throwable throwable) { try { writer.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (UnsupportedEncodingException uee)
/* 427 */     { getProject().log(uee.toString(), 0); }
/* 428 */     catch (IOException ioe)
/* 429 */     { throw new BuildException(ioe.toString(), ioe); }
/*     */   
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/cvslib/ChangeLogTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */