/*     */ package org.apache.tools.ant.taskdefs.launcher;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Optional;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
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
/*     */ public class CommandLauncher
/*     */ {
/*  40 */   protected static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */   
/*  42 */   private static CommandLauncher vmLauncher = null;
/*  43 */   private static CommandLauncher shellLauncher = null;
/*     */   
/*     */   static {
/*  46 */     if (!Os.isFamily("os/2")) {
/*  47 */       vmLauncher = new Java13CommandLauncher();
/*     */     }
/*     */     
/*  50 */     if (Os.isFamily("mac") && !Os.isFamily("unix")) {
/*     */       
/*  52 */       shellLauncher = new MacCommandLauncher(new CommandLauncher());
/*  53 */     } else if (Os.isFamily("os/2")) {
/*     */       
/*  55 */       shellLauncher = new OS2CommandLauncher(new CommandLauncher());
/*  56 */     } else if (Os.isFamily("windows")) {
/*  57 */       CommandLauncher baseLauncher = new CommandLauncher();
/*     */       
/*  59 */       if (!Os.isFamily("win9x")) {
/*     */         
/*  61 */         shellLauncher = new WinNTCommandLauncher(baseLauncher);
/*     */       } else {
/*     */         
/*  64 */         shellLauncher = new ScriptCommandLauncher("bin/antRun.bat", baseLauncher);
/*     */       }
/*     */     
/*  67 */     } else if (Os.isFamily("netware")) {
/*     */       
/*  69 */       CommandLauncher baseLauncher = new CommandLauncher();
/*     */       
/*  71 */       shellLauncher = new PerlScriptCommandLauncher("bin/antRun.pl", baseLauncher);
/*     */     }
/*  73 */     else if (Os.isFamily("openvms")) {
/*     */       
/*  75 */       shellLauncher = new VmsCommandLauncher();
/*     */     } else {
/*     */       
/*  78 */       shellLauncher = new ScriptCommandLauncher("bin/antRun", new CommandLauncher());
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
/*     */ 
/*     */   
/*     */   public Process exec(Project project, String[] cmd, String[] env) throws IOException {
/*  99 */     if (project != null) {
/* 100 */       project.log("Execute:CommandLauncher: " + 
/* 101 */           Commandline.describeCommand(cmd), 4);
/*     */     }
/* 103 */     return Runtime.getRuntime().exec(cmd, env);
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
/*     */ 
/*     */   
/*     */   public Process exec(Project project, String[] cmd, String[] env, File workingDir) throws IOException {
/* 126 */     if (workingDir == null) {
/* 127 */       return exec(project, cmd, env);
/*     */     }
/* 129 */     throw new IOException("Cannot execute a process in different directory under this JVM");
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
/*     */   public static CommandLauncher getShellLauncher(Project project) {
/* 141 */     CommandLauncher launcher = extractLauncher("ant.shellLauncher", project);
/*     */     
/* 143 */     if (launcher == null) {
/* 144 */       launcher = shellLauncher;
/*     */     }
/*     */     
/* 147 */     return launcher;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CommandLauncher getVMLauncher(Project project) {
/* 158 */     CommandLauncher launcher = extractLauncher("ant.vmLauncher", project);
/*     */     
/* 160 */     if (launcher == null) {
/* 161 */       launcher = vmLauncher;
/*     */     }
/* 163 */     return launcher;
/*     */   }
/*     */ 
/*     */   
/*     */   private static CommandLauncher extractLauncher(String referenceName, Project project) {
/* 168 */     return Optional.<Project>ofNullable(project)
/* 169 */       .map(p -> (CommandLauncher)p.getReference(referenceName))
/* 170 */       .orElseGet(() -> getSystemLauncher(referenceName));
/*     */   }
/*     */   
/*     */   private static CommandLauncher getSystemLauncher(String launcherRefId) {
/* 174 */     String launcherClass = System.getProperty(launcherRefId);
/* 175 */     if (launcherClass != null) {
/*     */       try {
/* 177 */         return Class.forName(launcherClass).<CommandLauncher>asSubclass(CommandLauncher.class)
/* 178 */           .getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 179 */       } catch (InstantiationException|IllegalAccessException|ClassNotFoundException|NoSuchMethodException|java.lang.reflect.InvocationTargetException e) {
/*     */ 
/*     */         
/* 182 */         System.err.println("Could not instantiate launcher class " + launcherClass + ": " + e
/* 183 */             .getMessage());
/*     */       } 
/*     */     }
/* 186 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setVMLauncher(Project project, CommandLauncher launcher) {
/* 197 */     if (project != null) {
/* 198 */       project.addReference("ant.vmLauncher", launcher);
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
/*     */   public static void setShellLauncher(Project project, CommandLauncher launcher) {
/* 210 */     if (project != null)
/* 211 */       project.addReference("ant.shellLauncher", launcher); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/launcher/CommandLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */