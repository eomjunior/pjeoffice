/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LazyFileOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private OutputStream fos;
/*     */   private File file;
/*     */   private boolean append;
/*     */   private boolean alwaysCreate;
/*     */   private boolean opened = false;
/*     */   private boolean closed = false;
/*     */   
/*     */   public LazyFileOutputStream(String name) {
/*  47 */     this(name, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LazyFileOutputStream(String name, boolean append) {
/*  58 */     this(new File(name), append);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LazyFileOutputStream(File f) {
/*  67 */     this(f, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LazyFileOutputStream(File file, boolean append) {
/*  78 */     this(file, append, false);
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
/*     */   public LazyFileOutputStream(File file, boolean append, boolean alwaysCreate) {
/*  91 */     this.file = file;
/*  92 */     this.append = append;
/*  93 */     this.alwaysCreate = alwaysCreate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void open() throws IOException {
/* 103 */     ensureOpened();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void close() throws IOException {
/* 112 */     if (this.alwaysCreate && !this.closed) {
/* 113 */       ensureOpened();
/*     */     }
/* 115 */     if (this.opened) {
/* 116 */       this.fos.close();
/*     */     }
/* 118 */     this.closed = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/* 128 */     write(b, 0, b.length);
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
/*     */   public synchronized void write(byte[] b, int offset, int len) throws IOException {
/* 141 */     ensureOpened();
/* 142 */     this.fos.write(b, offset, len);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void write(int b) throws IOException {
/* 152 */     ensureOpened();
/* 153 */     this.fos.write(b);
/*     */   }
/*     */   
/*     */   private synchronized void ensureOpened() throws IOException {
/* 157 */     if (this.closed) {
/* 158 */       throw new IOException(this.file + " has already been closed.");
/*     */     }
/*     */     
/* 161 */     if (!this.opened) {
/* 162 */       this.fos = FileUtils.newOutputStream(this.file.toPath(), this.append);
/* 163 */       this.opened = true;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/LazyFileOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */