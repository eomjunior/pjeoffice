/*     */ package com.itextpdf.text.io;
/*     */ 
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.RandomAccessFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TempFileCache
/*     */ {
/*     */   private String filename;
/*     */   private RandomAccessFile cache;
/*     */   private ByteArrayOutputStream baos;
/*     */   private byte[] buf;
/*     */   
/*     */   public class ObjectPosition
/*     */   {
/*     */     long offset;
/*     */     int length;
/*     */     
/*     */     ObjectPosition(long offset, int length) {
/*  52 */       this.offset = offset;
/*  53 */       this.length = length;
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
/*     */   public TempFileCache(String filename) throws IOException {
/*  68 */     this.filename = filename;
/*  69 */     File f = new File(filename);
/*  70 */     File parent = f.getParentFile();
/*  71 */     if (parent != null) {
/*  72 */       parent.mkdirs();
/*     */     }
/*     */     
/*  75 */     this.cache = new RandomAccessFile(filename, "rw");
/*     */     
/*  77 */     this.baos = new ByteArrayOutputStream();
/*     */   }
/*     */   
/*     */   public ObjectPosition put(PdfObject obj) throws IOException {
/*  81 */     this.baos.reset();
/*  82 */     ObjectOutputStream oos = new ObjectOutputStream(this.baos);
/*     */ 
/*     */     
/*  85 */     long offset = this.cache.length();
/*     */     
/*  87 */     oos.writeObject(obj);
/*  88 */     this.cache.seek(offset);
/*  89 */     this.cache.write(this.baos.toByteArray());
/*     */     
/*  91 */     long size = this.cache.length() - offset;
/*     */     
/*  93 */     return new ObjectPosition(offset, (int)size);
/*     */   }
/*     */   
/*     */   public PdfObject get(ObjectPosition pos) throws IOException, ClassNotFoundException {
/*  97 */     PdfObject obj = null;
/*  98 */     if (pos != null) {
/*  99 */       this.cache.seek(pos.offset);
/* 100 */       this.cache.read(getBuffer(pos.length), 0, pos.length);
/*     */       
/* 102 */       ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(getBuffer(pos.length)));
/*     */       try {
/* 104 */         obj = (PdfObject)ois.readObject();
/*     */       } finally {
/* 106 */         ois.close();
/*     */       } 
/*     */     } 
/*     */     
/* 110 */     return obj;
/*     */   }
/*     */   
/*     */   private byte[] getBuffer(int size) {
/* 114 */     if (this.buf == null || this.buf.length < size) {
/* 115 */       this.buf = new byte[size];
/*     */     }
/*     */     
/* 118 */     return this.buf;
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 122 */     this.cache.close();
/* 123 */     this.cache = null;
/*     */     
/* 125 */     (new File(this.filename)).delete();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/io/TempFileCache.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */