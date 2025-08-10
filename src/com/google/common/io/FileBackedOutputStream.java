/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Objects;
/*     */ import javax.annotation.CheckForNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @Beta
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public final class FileBackedOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private final int fileThreshold;
/*     */   private final boolean resetOnFinalize;
/*     */   private final ByteSource source;
/*     */   @GuardedBy("this")
/*     */   private OutputStream out;
/*     */   @CheckForNull
/*     */   @GuardedBy("this")
/*     */   private MemoryOutput memory;
/*     */   @CheckForNull
/*     */   @GuardedBy("this")
/*     */   private File file;
/*     */   
/*     */   private static class MemoryOutput
/*     */     extends ByteArrayOutputStream
/*     */   {
/*     */     private MemoryOutput() {}
/*     */     
/*     */     byte[] getBuffer() {
/*  88 */       return this.buf;
/*     */     }
/*     */     
/*     */     int getCount() {
/*  92 */       return this.count;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   @VisibleForTesting
/*     */   synchronized File getFile() {
/* 100 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileBackedOutputStream(int fileThreshold) {
/* 111 */     this(fileThreshold, false);
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
/*     */   public FileBackedOutputStream(int fileThreshold, boolean resetOnFinalize) {
/* 124 */     Preconditions.checkArgument((fileThreshold >= 0), "fileThreshold must be non-negative, but was %s", fileThreshold);
/*     */     
/* 126 */     this.fileThreshold = fileThreshold;
/* 127 */     this.resetOnFinalize = resetOnFinalize;
/* 128 */     this.memory = new MemoryOutput();
/* 129 */     this.out = this.memory;
/*     */     
/* 131 */     if (resetOnFinalize) {
/* 132 */       this.source = new ByteSource()
/*     */         {
/*     */           public InputStream openStream() throws IOException
/*     */           {
/* 136 */             return FileBackedOutputStream.this.openInputStream();
/*     */           }
/*     */ 
/*     */           
/*     */           protected void finalize() {
/*     */             try {
/* 142 */               FileBackedOutputStream.this.reset();
/* 143 */             } catch (Throwable t) {
/* 144 */               t.printStackTrace(System.err);
/*     */             } 
/*     */           }
/*     */         };
/*     */     } else {
/* 149 */       this.source = new ByteSource()
/*     */         {
/*     */           public InputStream openStream() throws IOException
/*     */           {
/* 153 */             return FileBackedOutputStream.this.openInputStream();
/*     */           }
/*     */         };
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteSource asByteSource() {
/* 165 */     return this.source;
/*     */   }
/*     */   
/*     */   private synchronized InputStream openInputStream() throws IOException {
/* 169 */     if (this.file != null) {
/* 170 */       return new FileInputStream(this.file);
/*     */     }
/*     */     
/* 173 */     Objects.requireNonNull(this.memory);
/* 174 */     return new ByteArrayInputStream(this.memory.getBuffer(), 0, this.memory.getCount());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws IOException {
/*     */     try {
/* 186 */       close();
/*     */     } finally {
/* 188 */       if (this.memory == null) {
/* 189 */         this.memory = new MemoryOutput();
/*     */       } else {
/* 191 */         this.memory.reset();
/*     */       } 
/* 193 */       this.out = this.memory;
/* 194 */       if (this.file != null) {
/* 195 */         File deleteMe = this.file;
/* 196 */         this.file = null;
/* 197 */         if (!deleteMe.delete()) {
/* 198 */           throw new IOException("Could not delete: " + deleteMe);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void write(int b) throws IOException {
/* 206 */     update(1);
/* 207 */     this.out.write(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void write(byte[] b) throws IOException {
/* 212 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void write(byte[] b, int off, int len) throws IOException {
/* 217 */     update(len);
/* 218 */     this.out.write(b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void close() throws IOException {
/* 223 */     this.out.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void flush() throws IOException {
/* 228 */     this.out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("this")
/*     */   private void update(int len) throws IOException {
/* 237 */     if (this.memory != null && this.memory.getCount() + len > this.fileThreshold) {
/* 238 */       File temp = TempFileCreator.INSTANCE.createTempFile("FileBackedOutputStream");
/* 239 */       if (this.resetOnFinalize)
/*     */       {
/*     */         
/* 242 */         temp.deleteOnExit();
/*     */       }
/*     */       try {
/* 245 */         FileOutputStream transfer = new FileOutputStream(temp);
/* 246 */         transfer.write(this.memory.getBuffer(), 0, this.memory.getCount());
/* 247 */         transfer.flush();
/*     */         
/* 249 */         this.out = transfer;
/* 250 */       } catch (IOException e) {
/* 251 */         temp.delete();
/* 252 */         throw e;
/*     */       } 
/*     */       
/* 255 */       this.file = temp;
/* 256 */       this.memory = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/io/FileBackedOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */