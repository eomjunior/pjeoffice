/*     */ package org.apache.log4j;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import org.apache.log4j.helpers.CountingQuietWriter;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ import org.apache.log4j.helpers.OptionConverter;
/*     */ import org.apache.log4j.helpers.QuietWriter;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RollingFileAppender
/*     */   extends FileAppender
/*     */ {
/*  48 */   protected long maxFileSize = 10485760L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   protected int maxBackupIndex = 1;
/*     */   
/*  55 */   private long nextRollover = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RollingFileAppender() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RollingFileAppender(Layout layout, String filename, boolean append) throws IOException {
/*  76 */     super(layout, filename, append);
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
/*     */   public RollingFileAppender(Layout layout, String filename) throws IOException {
/*  88 */     super(layout, filename);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxBackupIndex() {
/*  95 */     return this.maxBackupIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMaximumFileSize() {
/* 105 */     return this.maxFileSize;
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
/*     */   public void rollOver() {
/* 128 */     if (this.qw != null) {
/* 129 */       long size = ((CountingQuietWriter)this.qw).getCount();
/* 130 */       LogLog.debug("rolling over count=" + size);
/*     */ 
/*     */       
/* 133 */       this.nextRollover = size + this.maxFileSize;
/*     */     } 
/* 135 */     LogLog.debug("maxBackupIndex=" + this.maxBackupIndex);
/*     */     
/* 137 */     boolean renameSucceeded = true;
/*     */     
/* 139 */     if (this.maxBackupIndex > 0) {
/*     */       
/* 141 */       File file = new File(this.fileName + '.' + this.maxBackupIndex);
/* 142 */       if (file.exists()) {
/* 143 */         renameSucceeded = file.delete();
/*     */       }
/*     */       
/* 146 */       for (int i = this.maxBackupIndex - 1; i >= 1 && renameSucceeded; i--) {
/* 147 */         file = new File(this.fileName + "." + i);
/* 148 */         if (file.exists()) {
/* 149 */           File target = new File(this.fileName + '.' + (i + 1));
/* 150 */           LogLog.debug("Renaming file " + file + " to " + target);
/* 151 */           renameSucceeded = file.renameTo(target);
/*     */         } 
/*     */       } 
/*     */       
/* 155 */       if (renameSucceeded) {
/*     */         
/* 157 */         File target = new File(this.fileName + "." + '\001');
/*     */         
/* 159 */         closeFile();
/*     */         
/* 161 */         file = new File(this.fileName);
/* 162 */         LogLog.debug("Renaming file " + file + " to " + target);
/* 163 */         renameSucceeded = file.renameTo(target);
/*     */ 
/*     */ 
/*     */         
/* 167 */         if (!renameSucceeded) {
/*     */           try {
/* 169 */             setFile(this.fileName, true, this.bufferedIO, this.bufferSize);
/* 170 */           } catch (IOException e) {
/* 171 */             if (e instanceof java.io.InterruptedIOException) {
/* 172 */               Thread.currentThread().interrupt();
/*     */             }
/* 174 */             LogLog.error("setFile(" + this.fileName + ", true) call failed.", e);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     if (renameSucceeded) {
/*     */       
/*     */       try {
/*     */         
/* 187 */         setFile(this.fileName, false, this.bufferedIO, this.bufferSize);
/* 188 */         this.nextRollover = 0L;
/* 189 */       } catch (IOException e) {
/* 190 */         if (e instanceof java.io.InterruptedIOException) {
/* 191 */           Thread.currentThread().interrupt();
/*     */         }
/* 193 */         LogLog.error("setFile(" + this.fileName + ", false) call failed.", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize) throws IOException {
/* 200 */     super.setFile(fileName, append, this.bufferedIO, this.bufferSize);
/* 201 */     if (append) {
/* 202 */       File f = new File(fileName);
/* 203 */       ((CountingQuietWriter)this.qw).setCount(f.length());
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
/*     */   public void setMaxBackupIndex(int maxBackups) {
/* 217 */     this.maxBackupIndex = maxBackups;
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
/*     */   public void setMaximumFileSize(long maxFileSize) {
/* 233 */     this.maxFileSize = maxFileSize;
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
/*     */   public void setMaxFileSize(String value) {
/* 248 */     this.maxFileSize = OptionConverter.toFileSize(value, this.maxFileSize + 1L);
/*     */   }
/*     */   
/*     */   protected void setQWForFiles(Writer writer) {
/* 252 */     this.qw = (QuietWriter)new CountingQuietWriter(writer, this.errorHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void subAppend(LoggingEvent event) {
/* 261 */     super.subAppend(event);
/* 262 */     if (this.fileName != null && this.qw != null) {
/* 263 */       long size = ((CountingQuietWriter)this.qw).getCount();
/* 264 */       if (size >= this.maxFileSize && size >= this.nextRollover)
/* 265 */         rollOver(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/RollingFileAppender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */