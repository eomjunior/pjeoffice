/*     */ package org.apache.tools.ant.taskdefs.optional.pvcs;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.text.MessageFormat;
/*     */ import java.text.ParseException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Random;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.Execute;
/*     */ import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
/*     */ import org.apache.tools.ant.taskdefs.LogOutputStream;
/*     */ import org.apache.tools.ant.taskdefs.LogStreamHandler;
/*     */ import org.apache.tools.ant.taskdefs.PumpStreamHandler;
/*     */ import org.apache.tools.ant.types.Commandline;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Pvcs
/*     */   extends Task
/*     */ {
/* 112 */   private String pvcsProject = null;
/* 113 */   private Vector<PvcsProject> pvcsProjects = new Vector<>();
/* 114 */   private String workspace = null;
/* 115 */   private String repository = null;
/* 116 */   private String pvcsbin = null;
/* 117 */   private String force = null;
/* 118 */   private String promotiongroup = null;
/* 119 */   private String label = null;
/*     */   private boolean ignorerc = false;
/*     */   private boolean updateOnly = false;
/* 122 */   private String lineStart = "\"P:";
/* 123 */   private String filenameFormat = "{0}-arc({1})"; private static final int POS_1 = 1;
/*     */   private static final int POS_2 = 2;
/*     */   private static final int POS_3 = 3;
/*     */   private static final String PCLI_EXE = "pcli";
/*     */   private static final String GET_EXE = "get";
/*     */   private String revision;
/*     */   private String userId;
/*     */   private String config;
/*     */   
/*     */   protected int runCmd(Commandline cmd, ExecuteStreamHandler out) {
/*     */     try {
/* 134 */       Project aProj = getProject();
/* 135 */       Execute exe = new Execute(out);
/* 136 */       exe.setAntRun(aProj);
/* 137 */       exe.setWorkingDirectory(aProj.getBaseDir());
/* 138 */       exe.setCommandline(cmd.getCommandline());
/* 139 */       return exe.execute();
/* 140 */     } catch (IOException e) {
/*     */       
/* 142 */       String msg = "Failed executing: " + cmd.toString() + ". Exception: " + e.getMessage();
/* 143 */       throw new BuildException(msg, getLocation());
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getExecutable(String exe) {
/* 148 */     StringBuilder correctedExe = new StringBuilder();
/* 149 */     if (getPvcsbin() != null) {
/* 150 */       if (this.pvcsbin.endsWith(File.separator)) {
/* 151 */         correctedExe.append(this.pvcsbin);
/*     */       } else {
/* 153 */         correctedExe.append(this.pvcsbin).append(File.separator);
/*     */       } 
/*     */     }
/* 156 */     return correctedExe.append(exe).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 164 */     int result = 0;
/*     */     
/* 166 */     if (this.repository == null || this.repository.trim().isEmpty()) {
/* 167 */       throw new BuildException("Required argument repository not specified");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     Commandline commandLine = new Commandline();
/* 175 */     commandLine.setExecutable(getExecutable("pcli"));
/*     */     
/* 177 */     commandLine.createArgument().setValue("lvf");
/* 178 */     commandLine.createArgument().setValue("-z");
/* 179 */     commandLine.createArgument().setValue("-aw");
/* 180 */     if (getWorkspace() != null) {
/* 181 */       commandLine.createArgument().setValue("-sp" + getWorkspace());
/*     */     }
/* 183 */     commandLine.createArgument().setValue("-pr" + getRepository());
/*     */     
/* 185 */     String uid = getUserId();
/*     */     
/* 187 */     if (uid != null) {
/* 188 */       commandLine.createArgument().setValue("-id" + uid);
/*     */     }
/*     */ 
/*     */     
/* 192 */     if (getPvcsproject() == null && getPvcsprojects().isEmpty()) {
/* 193 */       this.pvcsProject = "/";
/*     */     }
/*     */     
/* 196 */     if (getPvcsproject() != null) {
/* 197 */       commandLine.createArgument().setValue(getPvcsproject());
/*     */     }
/* 199 */     if (!getPvcsprojects().isEmpty()) {
/* 200 */       for (PvcsProject pvcsProject : getPvcsprojects()) {
/* 201 */         String projectName = pvcsProject.getName();
/* 202 */         if (projectName == null || projectName.trim().isEmpty()) {
/* 203 */           throw new BuildException("name is a required attribute of pvcsproject");
/*     */         }
/* 205 */         commandLine.createArgument().setValue(projectName);
/*     */       } 
/*     */     }
/*     */     
/* 209 */     File tmp = null;
/* 210 */     File tmp2 = null;
/*     */     try {
/* 212 */       Random rand = new Random(System.currentTimeMillis());
/* 213 */       tmp = new File("pvcs_ant_" + rand.nextLong() + ".log");
/* 214 */       OutputStream fos = Files.newOutputStream(tmp.toPath(), new java.nio.file.OpenOption[0]);
/* 215 */       tmp2 = new File("pvcs_ant_" + rand.nextLong() + ".log");
/* 216 */       log(commandLine.describeCommand(), 3);
/*     */       try {
/* 218 */         result = runCmd(commandLine, (ExecuteStreamHandler)new PumpStreamHandler(fos, (OutputStream)new LogOutputStream(this, 1)));
/*     */       } finally {
/*     */         
/* 221 */         FileUtils.close(fos);
/*     */       } 
/*     */       
/* 224 */       if (Execute.isFailure(result) && !this.ignorerc) {
/* 225 */         String msg = "Failed executing: " + commandLine.toString();
/* 226 */         throw new BuildException(msg, getLocation());
/*     */       } 
/*     */       
/* 229 */       if (!tmp.exists()) {
/* 230 */         throw new BuildException("Communication between ant and pvcs failed. No output generated from executing PVCS commandline interface \"pcli\" and \"get\"");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 236 */       log("Creating folders", 2);
/* 237 */       createFolders(tmp);
/*     */ 
/*     */       
/* 240 */       massagePCLI(tmp, tmp2);
/*     */ 
/*     */       
/* 243 */       commandLine.clearArgs();
/* 244 */       commandLine.setExecutable(getExecutable("get"));
/*     */       
/* 246 */       if (getConfig() != null && !getConfig().isEmpty()) {
/* 247 */         commandLine.createArgument().setValue("-c" + getConfig());
/*     */       }
/*     */       
/* 250 */       if (getForce() != null && getForce().equals("yes")) {
/* 251 */         commandLine.createArgument().setValue("-Y");
/*     */       } else {
/* 253 */         commandLine.createArgument().setValue("-N");
/*     */       } 
/*     */       
/* 256 */       if (getPromotiongroup() != null) {
/* 257 */         commandLine.createArgument().setValue("-G" + 
/* 258 */             getPromotiongroup());
/*     */       }
/* 260 */       else if (getLabel() != null) {
/* 261 */         commandLine.createArgument().setValue("-v" + getLabel());
/*     */       }
/* 263 */       else if (getRevision() != null) {
/* 264 */         commandLine.createArgument().setValue("-r" + getRevision());
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 269 */       if (this.updateOnly) {
/* 270 */         commandLine.createArgument().setValue("-U");
/*     */       }
/*     */       
/* 273 */       commandLine.createArgument().setValue("@" + tmp2.getAbsolutePath());
/* 274 */       log("Getting files", 2);
/* 275 */       log("Executing " + commandLine.toString(), 3);
/* 276 */       result = runCmd(commandLine, (ExecuteStreamHandler)new LogStreamHandler(this, 2, 1));
/*     */       
/* 278 */       if (result != 0 && !this.ignorerc) {
/* 279 */         String msg = "Failed executing: " + commandLine.toString() + ". Return code was " + result;
/*     */         
/* 281 */         throw new BuildException(msg, getLocation());
/*     */       }
/*     */     
/* 284 */     } catch (ParseException|IOException e) {
/*     */       
/* 286 */       String msg = "Failed executing: " + commandLine.toString() + ". Exception: " + e.getMessage();
/* 287 */       throw new BuildException(msg, getLocation());
/*     */     } finally {
/* 289 */       if (tmp != null) {
/* 290 */         tmp.delete();
/*     */       }
/* 292 */       if (tmp2 != null) {
/* 293 */         tmp2.delete();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void createFolders(File file) throws IOException, ParseException {
/* 302 */     BufferedReader in = new BufferedReader(new FileReader(file)); try {
/* 303 */       MessageFormat mf = new MessageFormat(getFilenameFormat());
/* 304 */       String line = in.readLine();
/* 305 */       while (line != null) {
/* 306 */         log("Considering \"" + line + "\"", 3);
/* 307 */         if (line.startsWith("\"\\") || line
/* 308 */           .startsWith("\"/") || (line
/*     */           
/* 310 */           .length() > 3 && line.startsWith("\"") && 
/* 311 */           Character.isLetter(line.charAt(1)) && 
/* 312 */           String.valueOf(line.charAt(2)).equals(":") && 
/* 313 */           String.valueOf(line.charAt(3)).equals("\\"))) {
/* 314 */           Object[] objs = mf.parse(line);
/* 315 */           String f = (String)objs[1];
/*     */           
/* 317 */           int index = f.lastIndexOf(File.separator);
/* 318 */           if (index > -1) {
/* 319 */             File dir = new File(f.substring(0, index));
/* 320 */             if (dir.exists()) {
/* 321 */               log(dir.getAbsolutePath() + " exists. Skipping", 3);
/*     */             } else {
/*     */               
/* 324 */               log("Creating " + dir.getAbsolutePath(), 3);
/*     */               
/* 326 */               if (dir.mkdirs() || dir.isDirectory()) {
/* 327 */                 log("Created " + dir.getAbsolutePath(), 2);
/*     */               } else {
/*     */                 
/* 330 */                 log("Failed to create " + dir
/* 331 */                     .getAbsolutePath(), 2);
/*     */               } 
/*     */             } 
/*     */           } else {
/*     */             
/* 336 */             log("File separator problem with " + line, 1);
/*     */           } 
/*     */         } else {
/*     */           
/* 340 */           log("Skipped \"" + line + "\"", 3);
/*     */         } 
/* 342 */         line = in.readLine();
/*     */       } 
/* 344 */       in.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         in.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/* 355 */   private void massagePCLI(File in, File out) throws IOException { BufferedReader inReader = new BufferedReader(new FileReader(in)); try {
/* 356 */       BufferedWriter outWriter = new BufferedWriter(new FileWriter(out));
/*     */       
/*     */       try { for (String line : () -> inReader.lines().map(()).iterator()) {
/* 359 */           outWriter.write(line);
/* 360 */           outWriter.newLine();
/*     */         } 
/* 362 */         outWriter.close(); } catch (Throwable throwable) { try { outWriter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  inReader.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         inReader.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/* 370 */     }  } public String getRepository() { return this.repository; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilenameFormat() {
/* 381 */     return this.filenameFormat;
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
/*     */   public void setFilenameFormat(String f) {
/* 393 */     this.filenameFormat = f;
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
/*     */   public String getLineStart() {
/* 406 */     return this.lineStart;
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
/*     */   public void setLineStart(String l) {
/* 419 */     this.lineStart = l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRepository(String repo) {
/* 427 */     this.repository = repo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPvcsproject() {
/* 435 */     return this.pvcsProject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPvcsproject(String prj) {
/* 444 */     this.pvcsProject = prj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector<PvcsProject> getPvcsprojects() {
/* 452 */     return this.pvcsProjects;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWorkspace() {
/* 460 */     return this.workspace;
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
/*     */   public void setWorkspace(String ws) {
/* 473 */     this.workspace = ws;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPvcsbin() {
/* 481 */     return this.pvcsbin;
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
/*     */   public void setPvcsbin(String bin) {
/* 495 */     this.pvcsbin = bin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getForce() {
/* 503 */     return this.force;
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
/*     */   public void setForce(String f) {
/* 516 */     this.force = "yes".equalsIgnoreCase(f) ? "yes" : "no";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPromotiongroup() {
/* 524 */     return this.promotiongroup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPromotiongroup(String w) {
/* 532 */     this.promotiongroup = w;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLabel() {
/* 540 */     return this.label;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLabel(String l) {
/* 548 */     this.label = l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRevision() {
/* 556 */     return this.revision;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRevision(String r) {
/* 564 */     this.revision = r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getIgnoreReturnCode() {
/* 572 */     return this.ignorerc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreReturnCode(boolean b) {
/* 581 */     this.ignorerc = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPvcsproject(PvcsProject p) {
/* 589 */     this.pvcsProjects.addElement(p);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getUpdateOnly() {
/* 597 */     return this.updateOnly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUpdateOnly(boolean l) {
/* 606 */     this.updateOnly = l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getConfig() {
/* 614 */     return this.config;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConfig(File f) {
/* 623 */     this.config = f.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserId() {
/* 632 */     return this.userId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserId(String u) {
/* 640 */     this.userId = u;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/pvcs/Pvcs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */