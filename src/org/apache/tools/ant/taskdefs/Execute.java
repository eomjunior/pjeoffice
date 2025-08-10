/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.StringReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
/*     */ import org.apache.tools.ant.taskdefs.launcher.CommandLauncher;
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
/*     */ public class Execute
/*     */ {
/*     */   private static final int ONE_SECOND = 1000;
/*     */   public static final int INVALID = 2147483647;
/*  55 */   private static String antWorkingDirectory = System.getProperty("user.dir");
/*  56 */   private static Map<String, String> procEnvironment = null;
/*     */ 
/*     */   
/*  59 */   private static ProcessDestroyer processDestroyer = new ProcessDestroyer();
/*     */   
/*     */   private static boolean environmentCaseInSensitive = false;
/*     */ 
/*     */   
/*     */   static {
/*  65 */     if (Os.isFamily("windows")) {
/*  66 */       environmentCaseInSensitive = true;
/*     */     }
/*     */   }
/*     */   
/*  70 */   private String[] cmdl = null;
/*  71 */   private String[] env = null;
/*  72 */   private int exitValue = Integer.MAX_VALUE;
/*     */   private ExecuteStreamHandler streamHandler;
/*     */   private final ExecuteWatchdog watchdog;
/*  75 */   private File workingDirectory = null;
/*  76 */   private Project project = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean newEnvironment = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean useVMLauncher = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setSpawn(boolean spawn) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized Map<String, String> getEnvironmentVariables() {
/* 106 */     if (procEnvironment != null) {
/* 107 */       return procEnvironment;
/*     */     }
/* 109 */     if (!Os.isFamily("openvms")) {
/*     */       try {
/* 111 */         procEnvironment = System.getenv();
/* 112 */         return procEnvironment;
/* 113 */       } catch (Exception x) {
/* 114 */         x.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/* 118 */     procEnvironment = new LinkedHashMap<>();
/*     */     try {
/* 120 */       ByteArrayOutputStream out = new ByteArrayOutputStream();
/* 121 */       Execute exe = new Execute(new PumpStreamHandler(out));
/* 122 */       exe.setCommandline(getProcEnvCommand());
/*     */       
/* 124 */       exe.setNewenvironment(true);
/* 125 */       int retval = exe.execute();
/* 126 */       if (retval != 0);
/*     */ 
/*     */ 
/*     */       
/* 130 */       BufferedReader in = new BufferedReader(new StringReader(toString(out)));
/*     */       
/* 132 */       if (Os.isFamily("openvms")) {
/* 133 */         procEnvironment = getVMSLogicals(in);
/* 134 */         return procEnvironment;
/*     */       } 
/* 136 */       StringBuilder var = null;
/*     */       String line;
/* 138 */       while ((line = in.readLine()) != null) {
/* 139 */         if (line.contains("=")) {
/*     */           
/* 141 */           if (var != null) {
/* 142 */             int eq = var.toString().indexOf('=');
/* 143 */             procEnvironment.put(var.substring(0, eq), var
/* 144 */                 .substring(eq + 1));
/*     */           } 
/* 146 */           var = new StringBuilder(line);
/*     */           
/*     */           continue;
/*     */         } 
/* 150 */         if (var == null) {
/* 151 */           var = new StringBuilder(System.lineSeparator() + line); continue;
/*     */         } 
/* 153 */         var.append(System.lineSeparator()).append(line);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 158 */       if (var != null) {
/* 159 */         int eq = var.toString().indexOf('=');
/* 160 */         procEnvironment.put(var.substring(0, eq), var.substring(eq + 1));
/*     */       } 
/* 162 */     } catch (IOException exc) {
/* 163 */       exc.printStackTrace();
/*     */     } 
/*     */     
/* 166 */     return procEnvironment;
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
/*     */   public static synchronized Vector<String> getProcEnvironment() {
/* 178 */     Vector<String> v = new Vector<>();
/* 179 */     getEnvironmentVariables().forEach((key, value) -> v.add(key + "=" + value));
/* 180 */     return v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String[] getProcEnvCommand() {
/* 191 */     if (Os.isFamily("os/2"))
/*     */     {
/* 193 */       return new String[] { "cmd", "/c", "set" };
/*     */     }
/* 195 */     if (Os.isFamily("windows")) {
/*     */       
/* 197 */       if (Os.isFamily("win9x"))
/*     */       {
/* 199 */         return new String[] { "command.com", "/c", "set" };
/*     */       }
/*     */       
/* 202 */       return new String[] { "cmd", "/c", "set" };
/*     */     } 
/* 204 */     if (Os.isFamily("z/os") || Os.isFamily("unix")) {
/*     */ 
/*     */ 
/*     */       
/* 208 */       String[] cmd = new String[1];
/* 209 */       if ((new File("/bin/env")).canRead()) {
/* 210 */         cmd[0] = "/bin/env";
/* 211 */       } else if ((new File("/usr/bin/env")).canRead()) {
/* 212 */         cmd[0] = "/usr/bin/env";
/*     */       } else {
/*     */         
/* 215 */         cmd[0] = "env";
/*     */       } 
/* 217 */       return cmd;
/*     */     } 
/* 219 */     if (Os.isFamily("netware") || Os.isFamily("os/400"))
/*     */     {
/* 221 */       return new String[] { "env" };
/*     */     }
/* 223 */     if (Os.isFamily("openvms")) {
/* 224 */       return new String[] { "show", "logical" };
/*     */     }
/*     */ 
/*     */     
/* 228 */     return null;
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
/*     */   public static String toString(ByteArrayOutputStream bos) {
/* 242 */     if (Os.isFamily("z/os")) {
/*     */       try {
/* 244 */         return bos.toString("Cp1047");
/* 245 */       } catch (UnsupportedEncodingException unsupportedEncodingException) {}
/*     */     
/*     */     }
/* 248 */     else if (Os.isFamily("os/400")) {
/*     */       try {
/* 250 */         return bos.toString("Cp500");
/* 251 */       } catch (UnsupportedEncodingException unsupportedEncodingException) {}
/*     */     } 
/*     */ 
/*     */     
/* 255 */     return bos.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Execute() {
/* 263 */     this(new PumpStreamHandler(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Execute(ExecuteStreamHandler streamHandler) {
/* 273 */     this(streamHandler, null);
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
/*     */   public Execute(ExecuteStreamHandler streamHandler, ExecuteWatchdog watchdog) {
/* 286 */     setStreamHandler(streamHandler);
/* 287 */     this.watchdog = watchdog;
/*     */ 
/*     */     
/* 290 */     if (Os.isFamily("openvms")) {
/* 291 */       this.useVMLauncher = false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStreamHandler(ExecuteStreamHandler streamHandler) {
/* 302 */     this.streamHandler = streamHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getCommandline() {
/* 311 */     return this.cmdl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommandline(String[] commandline) {
/* 320 */     this.cmdl = commandline;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNewenvironment(boolean newenv) {
/* 329 */     this.newEnvironment = newenv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getEnvironment() {
/* 338 */     return (this.env == null || this.newEnvironment) ? 
/* 339 */       this.env : patchEnvironment();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnvironment(String[] env) {
/* 349 */     this.env = env;
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
/*     */   public void setWorkingDirectory(File wd) {
/* 363 */     this.workingDirectory = wd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getWorkingDirectory() {
/* 373 */     return (this.workingDirectory == null) ? new File(antWorkingDirectory) : 
/* 374 */       this.workingDirectory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAntRun(Project project) throws BuildException {
/* 385 */     this.project = project;
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
/*     */   public void setVMLauncher(boolean useVMLauncher) {
/* 399 */     this.useVMLauncher = useVMLauncher;
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
/*     */   public static Process launch(Project project, String[] command, String[] env, File dir, boolean useVM) throws IOException {
/* 417 */     if (dir != null && !dir.exists()) {
/* 418 */       throw new BuildException("%s doesn't exist.", new Object[] { dir });
/*     */     }
/*     */     
/* 421 */     CommandLauncher vmLauncher = CommandLauncher.getVMLauncher(project);
/*     */     
/* 423 */     CommandLauncher launcher = (useVM && vmLauncher != null) ? vmLauncher : CommandLauncher.getShellLauncher(project);
/* 424 */     return launcher.exec(project, command, env, dir);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int execute() throws IOException {
/* 435 */     if (this.workingDirectory != null && !this.workingDirectory.exists()) {
/* 436 */       throw new BuildException("%s doesn't exist.", new Object[] { this.workingDirectory });
/*     */     }
/* 438 */     Process process = launch(this.project, getCommandline(), 
/* 439 */         getEnvironment(), this.workingDirectory, this.useVMLauncher);
/*     */     
/*     */     try {
/* 442 */       this.streamHandler.setProcessInputStream(process.getOutputStream());
/* 443 */       this.streamHandler.setProcessOutputStream(process.getInputStream());
/* 444 */       this.streamHandler.setProcessErrorStream(process.getErrorStream());
/* 445 */     } catch (IOException e) {
/* 446 */       process.destroy();
/* 447 */       throw e;
/*     */     } 
/* 449 */     this.streamHandler.start();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 454 */       processDestroyer.add(process);
/*     */       
/* 456 */       if (this.watchdog != null) {
/* 457 */         this.watchdog.start(process);
/*     */       }
/* 459 */       waitFor(process);
/*     */       
/* 461 */       if (this.watchdog != null) {
/* 462 */         this.watchdog.stop();
/*     */       }
/* 464 */       this.streamHandler.stop();
/* 465 */       closeStreams(process);
/*     */       
/* 467 */       if (this.watchdog != null) {
/* 468 */         this.watchdog.checkException();
/*     */       }
/* 470 */       return getExitValue();
/* 471 */     } catch (ThreadDeath t) {
/*     */       
/* 473 */       process.destroy();
/* 474 */       throw t;
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 479 */       processDestroyer.remove(process);
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
/*     */   public void spawn() throws IOException {
/* 492 */     if (this.workingDirectory != null && !this.workingDirectory.exists()) {
/* 493 */       throw new BuildException("%s doesn't exist.", new Object[] { this.workingDirectory });
/*     */     }
/* 495 */     Process process = launch(this.project, getCommandline(), 
/* 496 */         getEnvironment(), this.workingDirectory, this.useVMLauncher);
/*     */     
/* 498 */     if (Os.isFamily("windows")) {
/*     */       try {
/* 500 */         Thread.sleep(1000L);
/* 501 */       } catch (InterruptedException e) {
/* 502 */         this.project.log("interruption in the sleep after having spawned a process", 3);
/*     */       } 
/*     */     }
/*     */     
/* 506 */     OutputStream dummyOut = new OutputStream()
/*     */       {
/*     */         public void write(int b) throws IOException {}
/*     */       };
/*     */ 
/*     */ 
/*     */     
/* 513 */     ExecuteStreamHandler handler = new PumpStreamHandler(dummyOut);
/* 514 */     handler.setProcessErrorStream(process.getErrorStream());
/* 515 */     handler.setProcessOutputStream(process.getInputStream());
/* 516 */     handler.start();
/* 517 */     process.getOutputStream().close();
/*     */     
/* 519 */     this.project.log("spawned process " + process.toString(), 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void waitFor(Process process) {
/*     */     try {
/* 530 */       process.waitFor();
/* 531 */       setExitValue(process.exitValue());
/* 532 */     } catch (InterruptedException e) {
/* 533 */       process.destroy();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setExitValue(int value) {
/* 543 */     this.exitValue = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getExitValue() {
/* 553 */     return this.exitValue;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFailure(int exitValue) {
/* 574 */     return Os.isFamily("openvms") ? (
/* 575 */       (exitValue % 2 == 0)) : ((exitValue != 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFailure() {
/* 586 */     return isFailure(getExitValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean killedProcess() {
/* 596 */     return (this.watchdog != null && this.watchdog.killedProcess());
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
/*     */   private String[] patchEnvironment() {
/* 608 */     if (Os.isFamily("openvms")) {
/* 609 */       return this.env;
/*     */     }
/*     */     
/* 612 */     Map<String, String> osEnv = new LinkedHashMap<>(getEnvironmentVariables());
/* 613 */     for (String keyValue : this.env) {
/* 614 */       String key = keyValue.substring(0, keyValue.indexOf('='));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 619 */       if (osEnv.remove(key) == null && environmentCaseInSensitive)
/*     */       {
/*     */         
/* 622 */         for (String osEnvItem : osEnv.keySet()) {
/*     */           
/* 624 */           if (osEnvItem.equalsIgnoreCase(key)) {
/*     */             
/* 626 */             key = osEnvItem;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 633 */       osEnv.put(key, keyValue.substring(key.length() + 1));
/*     */     } 
/*     */     
/* 636 */     return (String[])osEnv.entrySet().stream()
/* 637 */       .map(e -> (String)e.getKey() + "=" + (String)e.getValue()).toArray(x$0 -> new String[x$0]);
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
/*     */   public static void runCommand(Task task, String... cmdline) throws BuildException {
/*     */     try {
/* 651 */       task.log(Commandline.describeCommand(cmdline), 3);
/*     */       
/* 653 */       Execute exe = new Execute(new LogStreamHandler(task, 2, 0));
/*     */       
/* 655 */       exe.setAntRun(task.getProject());
/* 656 */       exe.setCommandline(cmdline);
/* 657 */       int retval = exe.execute();
/* 658 */       if (isFailure(retval)) {
/* 659 */         throw new BuildException(cmdline[0] + " failed with return code " + retval, task
/* 660 */             .getLocation());
/*     */       }
/* 662 */     } catch (IOException exc) {
/* 663 */       throw new BuildException("Could not launch " + cmdline[0] + ": " + exc, task
/* 664 */           .getLocation());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void closeStreams(Process process) {
/* 674 */     FileUtils.close(process.getInputStream());
/* 675 */     FileUtils.close(process.getOutputStream());
/* 676 */     FileUtils.close(process.getErrorStream());
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
/*     */   private static Map<String, String> getVMSLogicals(BufferedReader in) throws IOException {
/* 692 */     Map<String, String> logicals = new HashMap<>();
/* 693 */     String logName = null, logValue = null;
/*     */     
/*     */     String line;
/* 696 */     while ((line = in.readLine()) != null) {
/*     */       
/* 698 */       if (line.startsWith("\t=")) {
/*     */         
/* 700 */         if (logName != null)
/* 701 */           logValue = logValue + "," + line.substring(4, line.length() - 1);  continue;
/*     */       } 
/* 703 */       if (line.startsWith("  \"")) {
/*     */         
/* 705 */         if (logName != null) {
/* 706 */           logicals.put(logName, logValue);
/*     */         }
/* 708 */         int eqIndex = line.indexOf('=');
/* 709 */         String newLogName = line.substring(3, eqIndex - 2);
/* 710 */         if (logicals.containsKey(newLogName)) {
/*     */           
/* 712 */           logName = null; continue;
/*     */         } 
/* 714 */         logName = newLogName;
/* 715 */         logValue = line.substring(eqIndex + 3, line.length() - 1);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 721 */     if (logName != null) {
/* 722 */       logicals.put(logName, logValue);
/*     */     }
/* 724 */     return logicals;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Execute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */