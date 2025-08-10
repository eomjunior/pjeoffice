/*     */ package org.apache.tools.ant.taskdefs.optional.ejb;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.ExecTask;
/*     */ import org.apache.tools.ant.taskdefs.Java;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BorlandGenerateClient
/*     */   extends Task
/*     */ {
/*     */   static final String JAVA_MODE = "java";
/*     */   static final String FORK_MODE = "fork";
/*     */   boolean debug = false;
/*  50 */   File ejbjarfile = null;
/*     */ 
/*     */   
/*  53 */   File clientjarfile = null;
/*     */ 
/*     */   
/*     */   Path classpath;
/*     */ 
/*     */   
/*  59 */   String mode = "fork";
/*     */ 
/*     */   
/*  62 */   int version = 4;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVersion(int version) {
/*  70 */     this.version = version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMode(String s) {
/*  78 */     this.mode = s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDebug(boolean debug) {
/*  86 */     this.debug = debug;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEjbjar(File ejbfile) {
/*  94 */     this.ejbjarfile = ejbfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClientjar(File clientjar) {
/* 102 */     this.clientjarfile = clientjar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path classpath) {
/* 110 */     if (this.classpath == null) {
/* 111 */       this.classpath = classpath;
/*     */     } else {
/* 113 */       this.classpath.append(classpath);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 122 */     if (this.classpath == null) {
/* 123 */       this.classpath = new Path(getProject());
/*     */     }
/* 125 */     return this.classpath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/* 133 */     createClasspath().setRefid(r);
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
/*     */   public void execute() throws BuildException {
/* 145 */     if (this.ejbjarfile == null || this.ejbjarfile.isDirectory()) {
/* 146 */       throw new BuildException("invalid ejb jar file.");
/*     */     }
/*     */     
/* 149 */     if (this.clientjarfile == null || this.clientjarfile.isDirectory()) {
/* 150 */       log("invalid or missing client jar file.", 3);
/* 151 */       String ejbjarname = this.ejbjarfile.getAbsolutePath();
/*     */       
/* 153 */       String clientname = ejbjarname.substring(0, ejbjarname.lastIndexOf("."));
/* 154 */       clientname = clientname + "client.jar";
/* 155 */       this.clientjarfile = new File(clientname);
/*     */     } 
/*     */     
/* 158 */     if (this.mode == null) {
/* 159 */       log("mode is null default mode  is java");
/* 160 */       setMode("java");
/*     */     } 
/*     */     
/* 163 */     if (this.version != 5 && this.version != 4) {
/* 164 */       throw new BuildException("version %d is not supported", new Object[] { Integer.valueOf(this.version) });
/*     */     }
/*     */     
/* 167 */     log("client jar file is " + this.clientjarfile);
/*     */     
/* 169 */     if ("fork".equalsIgnoreCase(this.mode)) {
/* 170 */       executeFork();
/*     */     } else {
/* 172 */       executeJava();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void executeJava() throws BuildException {
/*     */     try {
/* 182 */       if (this.version == 5) {
/* 183 */         throw new BuildException("java mode is supported only for previous version <= %d", new Object[] {
/*     */               
/* 185 */               Integer.valueOf(4)
/*     */             });
/*     */       }
/* 188 */       log("mode : java");
/*     */       
/* 190 */       Java execTask = new Java(this);
/* 191 */       execTask.setDir(new File("."));
/* 192 */       execTask.setClassname("com.inprise.server.commandline.EJBUtilities");
/*     */ 
/*     */ 
/*     */       
/* 196 */       execTask.setClasspath(this.classpath.concatSystemClasspath());
/*     */       
/* 198 */       execTask.setFork(true);
/* 199 */       execTask.createArg().setValue("generateclient");
/* 200 */       if (this.debug) {
/* 201 */         execTask.createArg().setValue("-trace");
/*     */       }
/*     */       
/* 204 */       execTask.createArg().setValue("-short");
/* 205 */       execTask.createArg().setValue("-jarfile");
/*     */       
/* 207 */       execTask.createArg().setValue(this.ejbjarfile.getAbsolutePath());
/*     */       
/* 209 */       execTask.createArg().setValue("-single");
/* 210 */       execTask.createArg().setValue("-clientjarfile");
/* 211 */       execTask.createArg().setValue(this.clientjarfile.getAbsolutePath());
/*     */       
/* 213 */       log("Calling EJBUtilities", 3);
/* 214 */       execTask.execute();
/*     */     }
/* 216 */     catch (Exception e) {
/*     */       
/* 218 */       throw new BuildException("Exception while calling generateclient", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void executeFork() throws BuildException {
/* 227 */     if (this.version == 4) {
/* 228 */       executeForkV4();
/*     */     }
/* 230 */     if (this.version == 5) {
/* 231 */       executeForkV5();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void executeForkV4() throws BuildException {
/*     */     try {
/* 241 */       log("mode : fork 4", 4);
/*     */       
/* 243 */       ExecTask execTask = new ExecTask(this);
/* 244 */       execTask.setDir(new File("."));
/* 245 */       execTask.setExecutable("iastool");
/* 246 */       execTask.createArg().setValue("generateclient");
/* 247 */       if (this.debug) {
/* 248 */         execTask.createArg().setValue("-trace");
/*     */       }
/*     */       
/* 251 */       execTask.createArg().setValue("-short");
/* 252 */       execTask.createArg().setValue("-jarfile");
/*     */       
/* 254 */       execTask.createArg().setValue(this.ejbjarfile.getAbsolutePath());
/*     */       
/* 256 */       execTask.createArg().setValue("-single");
/* 257 */       execTask.createArg().setValue("-clientjarfile");
/* 258 */       execTask.createArg().setValue(this.clientjarfile.getAbsolutePath());
/*     */       
/* 260 */       log("Calling iastool", 3);
/* 261 */       execTask.execute();
/* 262 */     } catch (Exception e) {
/*     */       
/* 264 */       throw new BuildException("Exception while calling generateclient", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void executeForkV5() throws BuildException {
/*     */     try {
/* 274 */       log("mode : fork 5", 4);
/* 275 */       ExecTask execTask = new ExecTask(this);
/* 276 */       execTask.setDir(new File("."));
/* 277 */       execTask.setExecutable("iastool");
/* 278 */       if (this.debug) {
/* 279 */         execTask.createArg().setValue("-debug");
/*     */       }
/* 281 */       execTask.createArg().setValue("-genclient");
/* 282 */       execTask.createArg().setValue("-jars");
/*     */       
/* 284 */       execTask.createArg().setValue(this.ejbjarfile.getAbsolutePath());
/*     */       
/* 286 */       execTask.createArg().setValue("-target");
/* 287 */       execTask.createArg().setValue(this.clientjarfile.getAbsolutePath());
/*     */       
/* 289 */       execTask.createArg().setValue("-cp");
/* 290 */       execTask.createArg().setValue(this.classpath.toString());
/* 291 */       log("Calling iastool", 3);
/* 292 */       execTask.execute();
/* 293 */     } catch (Exception e) {
/*     */       
/* 295 */       throw new BuildException("Exception while calling generateclient", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ejb/BorlandGenerateClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */