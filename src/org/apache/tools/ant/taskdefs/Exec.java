/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Locale;
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
/*     */ @Deprecated
/*     */ public class Exec
/*     */   extends Task
/*     */ {
/*     */   private String os;
/*     */   private String out;
/*     */   private File dir;
/*     */   private String command;
/*  54 */   protected PrintWriter fos = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean failOnError = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public Exec() {
/*  63 */     System.err.println("As of Ant 1.2 released in October 2000, the Exec class");
/*     */     
/*  65 */     System.err.println("is considered to be dead code by the Ant developers and is unmaintained.");
/*     */     
/*  67 */     System.err.println("Don't use it!");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*  76 */     run(this.command);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int run(String command) throws BuildException {
/*  87 */     int err = -1;
/*     */ 
/*     */     
/*  90 */     String myos = System.getProperty("os.name");
/*  91 */     log("Myos = " + myos, 3);
/*  92 */     if (this.os != null && !this.os.contains(myos)) {
/*     */       
/*  94 */       log("Not found in " + this.os, 3);
/*  95 */       return 0;
/*     */     } 
/*     */ 
/*     */     
/*  99 */     if (this.dir == null) {
/* 100 */       this.dir = getProject().getBaseDir();
/*     */     }
/*     */     
/* 103 */     if (myos.toLowerCase(Locale.ENGLISH).contains("windows")) {
/* 104 */       if (!this.dir.equals(getProject().resolveFile("."))) {
/* 105 */         if (myos.toLowerCase(Locale.ENGLISH).contains("nt")) {
/* 106 */           command = "cmd /c cd " + this.dir + " && " + command;
/*     */         } else {
/* 108 */           String ant = getProject().getProperty("ant.home");
/* 109 */           if (ant == null) {
/* 110 */             throw new BuildException("Property 'ant.home' not found", 
/* 111 */                 getLocation());
/*     */           }
/*     */           
/* 114 */           String antRun = getProject().resolveFile(ant + "/bin/antRun.bat").toString();
/* 115 */           command = antRun + " " + this.dir + " " + command;
/*     */         } 
/*     */       }
/*     */     } else {
/* 119 */       String ant = getProject().getProperty("ant.home");
/* 120 */       if (ant == null) {
/* 121 */         throw new BuildException("Property 'ant.home' not found", 
/* 122 */             getLocation());
/*     */       }
/* 124 */       String antRun = getProject().resolveFile(ant + "/bin/antRun").toString();
/*     */       
/* 126 */       command = antRun + " " + this.dir + " " + command;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 131 */       log(command, 3);
/*     */ 
/*     */       
/* 134 */       Process proc = Runtime.getRuntime().exec(command);
/*     */       
/* 136 */       if (this.out != null) {
/* 137 */         this.fos = new PrintWriter(new FileWriter(this.out));
/* 138 */         log("Output redirected to " + this.out, 3);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 143 */       StreamPumper inputPumper = new StreamPumper(proc.getInputStream(), 2);
/*     */       
/* 145 */       StreamPumper errorPumper = new StreamPumper(proc.getErrorStream(), 1);
/*     */ 
/*     */       
/* 148 */       inputPumper.start();
/* 149 */       errorPumper.start();
/*     */ 
/*     */       
/* 152 */       proc.waitFor();
/* 153 */       inputPumper.join();
/* 154 */       errorPumper.join();
/* 155 */       proc.destroy();
/*     */ 
/*     */       
/* 158 */       logFlush();
/*     */ 
/*     */       
/* 161 */       err = proc.exitValue();
/* 162 */       if (err != 0) {
/* 163 */         if (this.failOnError) {
/* 164 */           throw new BuildException("Exec returned: " + err, getLocation());
/*     */         }
/* 166 */         log("Result: " + err, 0);
/*     */       } 
/* 168 */     } catch (IOException ioe) {
/* 169 */       throw new BuildException("Error exec: " + command, ioe, getLocation());
/* 170 */     } catch (InterruptedException interruptedException) {}
/*     */ 
/*     */ 
/*     */     
/* 174 */     return err;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDir(String d) {
/* 182 */     this.dir = getProject().resolveFile(d);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOs(String os) {
/* 190 */     this.os = os;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommand(String command) {
/* 198 */     this.command = command;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutput(String out) {
/* 206 */     this.out = out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailonerror(boolean fail) {
/* 215 */     this.failOnError = fail;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void outputLog(String line, int messageLevel) {
/* 225 */     if (this.fos == null) {
/* 226 */       log(line, messageLevel);
/*     */     } else {
/* 228 */       this.fos.println(line);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void logFlush() {
/* 236 */     if (this.fos != null) {
/* 237 */       this.fos.close();
/*     */     }
/*     */   }
/*     */   
/*     */   class StreamPumper
/*     */     extends Thread
/*     */   {
/*     */     private BufferedReader din;
/*     */     private int messageLevel;
/*     */     private boolean endOfStream = false;
/*     */     private static final int SLEEP_TIME = 5;
/*     */     
/*     */     public StreamPumper(InputStream is, int messageLevel) {
/* 250 */       this.din = new BufferedReader(new InputStreamReader(is));
/* 251 */       this.messageLevel = messageLevel;
/*     */     }
/*     */     
/*     */     public void pumpStream() throws IOException {
/* 255 */       if (!this.endOfStream) {
/* 256 */         String line = this.din.readLine();
/*     */         
/* 258 */         if (line != null) {
/* 259 */           Exec.this.outputLog(line, this.messageLevel);
/*     */         } else {
/* 261 */           this.endOfStream = true;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void run() {
/*     */       try {
/*     */         while (true) {
/*     */           try {
/* 270 */             if (!this.endOfStream) {
/* 271 */               pumpStream();
/* 272 */               sleep(5L); continue;
/*     */             } 
/* 274 */           } catch (InterruptedException interruptedException) {}
/*     */           break;
/*     */         } 
/* 277 */         this.din.close();
/* 278 */       } catch (IOException iOException) {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Exec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */