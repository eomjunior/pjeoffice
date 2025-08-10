/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Iterator;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConcatResourceInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private static final int EOF = -1;
/*     */   private boolean eof = false;
/*     */   private Iterator<Resource> iter;
/*     */   private InputStream currentStream;
/*     */   private ProjectComponent managingPc;
/*     */   private boolean ignoreErrors = false;
/*     */   
/*     */   public ConcatResourceInputStream(ResourceCollection rc) {
/*  51 */     this.iter = rc.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreErrors(boolean b) {
/*  59 */     this.ignoreErrors = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIgnoreErrors() {
/*  67 */     return this.ignoreErrors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  76 */     closeCurrent();
/*  77 */     this.eof = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  87 */     if (this.eof) {
/*  88 */       return -1;
/*     */     }
/*  90 */     int result = readCurrent();
/*  91 */     if (result == -1) {
/*  92 */       nextResource();
/*  93 */       result = readCurrent();
/*     */     } 
/*  95 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setManagingComponent(ProjectComponent pc) {
/* 104 */     this.managingPc = pc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void log(String message, int loglevel) {
/* 113 */     if (this.managingPc != null) {
/* 114 */       this.managingPc.log(message, loglevel);
/*     */     } else {
/* 116 */       ((loglevel > 1) ? System.out : System.err).println(message);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int readCurrent() throws IOException {
/* 121 */     return (this.eof || this.currentStream == null) ? -1 : this.currentStream.read();
/*     */   }
/*     */   
/*     */   private void nextResource() throws IOException {
/* 125 */     closeCurrent();
/* 126 */     while (this.iter.hasNext()) {
/* 127 */       Resource r = this.iter.next();
/* 128 */       if (!r.isExists()) {
/*     */         continue;
/*     */       }
/* 131 */       log("Concatenating " + r.toLongString(), 3);
/*     */       try {
/* 133 */         this.currentStream = new BufferedInputStream(r.getInputStream());
/*     */         return;
/* 135 */       } catch (IOException eyeOhEx) {
/* 136 */         if (!this.ignoreErrors) {
/* 137 */           log("Failed to get input stream for " + r, 0);
/* 138 */           throw eyeOhEx;
/*     */         } 
/*     */       } 
/*     */     } 
/* 142 */     this.eof = true;
/*     */   }
/*     */   
/*     */   private void closeCurrent() {
/* 146 */     FileUtils.close(this.currentStream);
/* 147 */     this.currentStream = null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/ConcatResourceInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */