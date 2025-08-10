/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.CommandlineJava;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Permissions;
/*     */ import org.apache.tools.ant.util.JavaEnvUtils;
/*     */ import org.apache.tools.ant.util.TimeoutObserver;
/*     */ import org.apache.tools.ant.util.Watchdog;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExecuteJava
/*     */   implements Runnable, TimeoutObserver
/*     */ {
/*  48 */   private Commandline javaCommand = null;
/*  49 */   private Path classpath = null;
/*  50 */   private CommandlineJava.SysProperties sysProperties = null;
/*  51 */   private Permissions perm = null;
/*  52 */   private Method main = null;
/*  53 */   private Long timeout = null;
/*  54 */   private volatile Throwable caught = null;
/*     */   private volatile boolean timedOut = false;
/*     */   private boolean done = false;
/*  57 */   private Thread thread = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJavaCommand(Commandline javaCommand) {
/*  64 */     this.javaCommand = javaCommand;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path p) {
/*  73 */     this.classpath = p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSystemProperties(CommandlineJava.SysProperties s) {
/*  81 */     this.sysProperties = s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPermissions(Permissions permissions) {
/*  90 */     this.perm = permissions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setOutput(PrintStream out) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeout(Long timeout) {
/* 110 */     this.timeout = timeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Project project) throws BuildException {
/* 119 */     String classname = this.javaCommand.getExecutable();
/*     */     
/* 121 */     AntClassLoader loader = null; try {
/*     */       Class<?> target;
/* 123 */       if (this.sysProperties != null) {
/* 124 */         this.sysProperties.setSystem();
/*     */       }
/*     */       
/*     */       try {
/* 128 */         if (this.classpath == null) {
/* 129 */           target = Class.forName(classname);
/*     */         } else {
/* 131 */           loader = project.createClassLoader(this.classpath);
/* 132 */           loader.setParent(project.getCoreLoader());
/* 133 */           loader.setParentFirst(false);
/* 134 */           loader.addJavaLibraries();
/* 135 */           loader.setIsolated(true);
/* 136 */           loader.setThreadContextLoader();
/* 137 */           loader.forceLoadClass(classname);
/* 138 */           target = Class.forName(classname, true, (ClassLoader)loader);
/*     */         } 
/* 140 */       } catch (ClassNotFoundException e) {
/* 141 */         throw new BuildException("Could not find %s. Make sure you have it in your classpath", new Object[] { classname });
/*     */       } 
/*     */ 
/*     */       
/* 145 */       this.main = target.getMethod("main", new Class[] { String[].class });
/* 146 */       if (this.main == null) {
/* 147 */         throw new BuildException("Could not find main() method in %s", new Object[] { classname });
/*     */       }
/*     */       
/* 150 */       if ((this.main.getModifiers() & 0x8) == 0) {
/* 151 */         throw new BuildException("main() method in %s is not declared static", new Object[] { classname });
/*     */       }
/*     */       
/* 154 */       if (this.timeout == null) {
/* 155 */         run();
/*     */       } else {
/* 157 */         this.thread = new Thread(this, "ExecuteJava");
/*     */         
/* 159 */         Task currentThreadTask = project.getThreadTask(Thread.currentThread());
/*     */         
/* 161 */         project.registerThreadTask(this.thread, currentThreadTask);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 166 */         this.thread.setDaemon(true);
/* 167 */         Watchdog w = new Watchdog(this.timeout.longValue());
/* 168 */         w.addTimeoutObserver(this);
/* 169 */         synchronized (this) {
/* 170 */           this.thread.start();
/* 171 */           w.start();
/*     */           try {
/* 173 */             while (!this.done) {
/* 174 */               wait();
/*     */             }
/* 176 */           } catch (InterruptedException interruptedException) {}
/*     */ 
/*     */           
/* 179 */           if (this.timedOut) {
/* 180 */             project.log("Timeout: sub-process interrupted", 1);
/*     */           } else {
/*     */             
/* 183 */             this.thread = null;
/* 184 */             w.stop();
/*     */           } 
/*     */         } 
/*     */       } 
/* 188 */       if (this.caught != null) {
/* 189 */         throw this.caught;
/*     */       }
/* 191 */     } catch (BuildException|ThreadDeath|SecurityException e) {
/* 192 */       Class<?> target; throw target;
/* 193 */     } catch (Throwable e) {
/* 194 */       throw new BuildException(e);
/*     */     } finally {
/* 196 */       if (loader != null) {
/* 197 */         loader.resetThreadContextLoader();
/* 198 */         loader.cleanup();
/* 199 */         loader = null;
/*     */       } 
/* 201 */       if (this.sysProperties != null) {
/* 202 */         this.sysProperties.restoreSystem();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 213 */     Object[] argument = { this.javaCommand.getArguments() };
/*     */     try {
/* 215 */       if (this.perm != null) {
/* 216 */         this.perm.setSecurityManager();
/*     */       }
/* 218 */       this.main.invoke(null, argument);
/* 219 */     } catch (InvocationTargetException e) {
/* 220 */       Throwable t = e.getTargetException();
/* 221 */       if (!(t instanceof InterruptedException)) {
/* 222 */         this.caught = t;
/*     */       }
/* 224 */     } catch (Throwable t) {
/* 225 */       this.caught = t;
/*     */     } finally {
/* 227 */       if (this.perm != null) {
/* 228 */         this.perm.restoreSecurityManager();
/*     */       }
/* 230 */       synchronized (this) {
/* 231 */         this.done = true;
/* 232 */         notifyAll();
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
/*     */   public synchronized void timeoutOccured(Watchdog w) {
/* 244 */     if (this.thread != null) {
/* 245 */       this.timedOut = true;
/* 246 */       this.thread.interrupt();
/*     */     } 
/* 248 */     this.done = true;
/* 249 */     notifyAll();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean killedProcess() {
/* 258 */     return this.timedOut;
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
/*     */   public int fork(ProjectComponent pc) throws BuildException {
/* 271 */     CommandlineJava cmdl = new CommandlineJava();
/* 272 */     cmdl.setClassname(this.javaCommand.getExecutable());
/* 273 */     for (String arg : this.javaCommand.getArguments()) {
/* 274 */       cmdl.createArgument().setValue(arg);
/*     */     }
/* 276 */     if (this.classpath != null) {
/* 277 */       cmdl.createClasspath(pc.getProject()).append(this.classpath);
/*     */     }
/* 279 */     if (this.sysProperties != null) {
/* 280 */       cmdl.addSysproperties(this.sysProperties);
/*     */     }
/* 282 */     Redirector redirector = new Redirector(pc);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 287 */     Execute exe = new Execute(redirector.createHandler(), (this.timeout == null) ? null : new ExecuteWatchdog(this.timeout.longValue()));
/* 288 */     exe.setAntRun(pc.getProject());
/* 289 */     if (Os.isFamily("openvms")) {
/* 290 */       setupCommandLineForVMS(exe, cmdl.getCommandline());
/*     */     } else {
/* 292 */       exe.setCommandline(cmdl.getCommandline());
/*     */     } 
/*     */     try {
/* 295 */       int rc = exe.execute();
/* 296 */       redirector.complete();
/* 297 */       return rc;
/* 298 */     } catch (IOException e) {
/* 299 */       throw new BuildException(e);
/*     */     } finally {
/* 301 */       this.timedOut = exe.killedProcess();
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
/*     */   public static void setupCommandLineForVMS(Execute exe, String[] command) {
/* 315 */     exe.setVMLauncher(true);
/* 316 */     File vmsJavaOptionFile = null;
/*     */     try {
/* 318 */       String[] args = new String[command.length - 1];
/* 319 */       System.arraycopy(command, 1, args, 0, command.length - 1);
/* 320 */       vmsJavaOptionFile = JavaEnvUtils.createVmsJavaOptionFile(args);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 325 */       vmsJavaOptionFile.deleteOnExit();
/* 326 */       String[] vmsCmd = { command[0], "-V", vmsJavaOptionFile.getPath() };
/* 327 */       exe.setCommandline(vmsCmd);
/* 328 */     } catch (IOException e) {
/* 329 */       throw new BuildException("Failed to create a temporary file for \"-V\" switch");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/ExecuteJava.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */