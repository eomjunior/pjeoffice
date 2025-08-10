/*     */ package org.apache.log4j;
/*     */ 
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.helpers.QuietWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileAppender
/*     */   extends WriterAppender
/*     */ {
/*     */   protected boolean fileAppend = true;
/*  60 */   protected String fileName = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean bufferedIO = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   protected int bufferSize = 8192;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileAppender() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileAppender(Layout layout, String filename, boolean append, boolean bufferedIO, int bufferSize) throws IOException {
/*  95 */     this.layout = layout;
/*  96 */     setFile(filename, append, bufferedIO, bufferSize);
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
/*     */   public FileAppender(Layout layout, String filename, boolean append) throws IOException {
/* 110 */     this.layout = layout;
/* 111 */     setFile(filename, append, false, this.bufferSize);
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
/*     */   public FileAppender(Layout layout, String filename) throws IOException {
/* 123 */     this(layout, filename, true);
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
/*     */   public void setFile(String file) {
/* 141 */     String val = file.trim();
/* 142 */     this.fileName = val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getAppend() {
/* 149 */     return this.fileAppend;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFile() {
/* 154 */     return this.fileName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {
/* 164 */     if (this.fileName != null) {
/*     */       try {
/* 166 */         setFile(this.fileName, this.fileAppend, this.bufferedIO, this.bufferSize);
/* 167 */       } catch (IOException e) {
/* 168 */         this.errorHandler.error("setFile(" + this.fileName + "," + this.fileAppend + ") call failed.", e, 4);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 173 */       LogLog.warn("File option not set for appender [" + this.name + "].");
/* 174 */       LogLog.warn("Are you using FileAppender instead of ConsoleAppender?");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeFile() {
/* 182 */     if (this.qw != null) {
/*     */       try {
/* 184 */         this.qw.close();
/* 185 */       } catch (IOException e) {
/* 186 */         if (e instanceof java.io.InterruptedIOException) {
/* 187 */           Thread.currentThread().interrupt();
/*     */         }
/*     */ 
/*     */         
/* 191 */         LogLog.error("Could not close " + this.qw, e);
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
/*     */   public boolean getBufferedIO() {
/* 204 */     return this.bufferedIO;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBufferSize() {
/* 211 */     return this.bufferSize;
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
/*     */   public void setAppend(boolean flag) {
/* 225 */     this.fileAppend = flag;
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
/*     */   public void setBufferedIO(boolean bufferedIO) {
/* 238 */     this.bufferedIO = bufferedIO;
/* 239 */     if (bufferedIO) {
/* 240 */       this.immediateFlush = false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBufferSize(int bufferSize) {
/* 248 */     this.bufferSize = bufferSize;
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
/*     */   public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize) throws IOException {
/* 269 */     LogLog.debug("setFile called: " + fileName + ", " + append);
/*     */ 
/*     */     
/* 272 */     if (bufferedIO) {
/* 273 */       setImmediateFlush(false);
/*     */     }
/*     */     
/* 276 */     reset();
/* 277 */     FileOutputStream ostream = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 282 */       ostream = new FileOutputStream(fileName, append);
/* 283 */     } catch (FileNotFoundException ex) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 289 */       String parentName = (new File(fileName)).getParent();
/* 290 */       if (parentName != null) {
/* 291 */         File parentDir = new File(parentName);
/* 292 */         if (!parentDir.exists() && parentDir.mkdirs()) {
/* 293 */           ostream = new FileOutputStream(fileName, append);
/*     */         } else {
/* 295 */           throw ex;
/*     */         } 
/*     */       } else {
/* 298 */         throw ex;
/*     */       } 
/*     */     } 
/* 301 */     Writer fw = createWriter(ostream);
/* 302 */     if (bufferedIO) {
/* 303 */       fw = new BufferedWriter(fw, bufferSize);
/*     */     }
/* 305 */     setQWForFiles(fw);
/* 306 */     this.fileName = fileName;
/* 307 */     this.fileAppend = append;
/* 308 */     this.bufferedIO = bufferedIO;
/* 309 */     this.bufferSize = bufferSize;
/* 310 */     writeHeader();
/* 311 */     LogLog.debug("setFile ended");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setQWForFiles(Writer writer) {
/* 320 */     this.qw = new QuietWriter(writer, this.errorHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void reset() {
/* 327 */     closeFile();
/* 328 */     this.fileName = null;
/* 329 */     super.reset();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/FileAppender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */