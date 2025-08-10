/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import org.apache.tools.ant.ProjectComponent;
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
/*     */ public class ConcatFileInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private static final int EOF = -1;
/*  38 */   private int currentIndex = -1;
/*     */ 
/*     */   
/*     */   private boolean eof = false;
/*     */   
/*     */   private File[] file;
/*     */   
/*     */   private InputStream currentStream;
/*     */   
/*     */   private ProjectComponent managingPc;
/*     */ 
/*     */   
/*     */   public ConcatFileInputStream(File[] file) throws IOException {
/*  51 */     this.file = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  60 */     closeCurrent();
/*  61 */     this.eof = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  71 */     int result = readCurrent();
/*  72 */     if (result == -1 && !this.eof) {
/*  73 */       openFile(++this.currentIndex);
/*  74 */       result = readCurrent();
/*     */     } 
/*  76 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setManagingTask(Task task) {
/*  85 */     setManagingComponent((ProjectComponent)task);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setManagingComponent(ProjectComponent pc) {
/*  94 */     this.managingPc = pc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(String message, int loglevel) {
/* 103 */     if (this.managingPc != null) {
/* 104 */       this.managingPc.log(message, loglevel);
/*     */     }
/* 106 */     else if (loglevel > 1) {
/* 107 */       System.out.println(message);
/*     */     } else {
/* 109 */       System.err.println(message);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int readCurrent() throws IOException {
/* 115 */     return (this.eof || this.currentStream == null) ? -1 : this.currentStream.read();
/*     */   }
/*     */   
/*     */   private void openFile(int index) throws IOException {
/* 119 */     closeCurrent();
/* 120 */     if (this.file != null && index < this.file.length) {
/* 121 */       log("Opening " + this.file[index], 3);
/*     */       try {
/* 123 */         this
/* 124 */           .currentStream = new BufferedInputStream(Files.newInputStream(this.file[index].toPath(), new java.nio.file.OpenOption[0]));
/* 125 */       } catch (IOException eyeOhEx) {
/* 126 */         log("Failed to open " + this.file[index], 0);
/* 127 */         throw eyeOhEx;
/*     */       } 
/*     */     } else {
/* 130 */       this.eof = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void closeCurrent() {
/* 135 */     FileUtils.close(this.currentStream);
/* 136 */     this.currentStream = null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/ConcatFileInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */