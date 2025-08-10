/*    */ package org.zeroturnaround.zip;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.zip.CRC32;
/*    */ import java.util.zip.ZipEntry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ByteSource
/*    */   implements ZipEntrySource
/*    */ {
/*    */   private final String path;
/*    */   private final byte[] bytes;
/*    */   private final long time;
/*    */   private final int compressionMethod;
/*    */   private final long crc;
/*    */   
/*    */   public ByteSource(String path, byte[] bytes) {
/* 33 */     this(path, bytes, System.currentTimeMillis());
/*    */   }
/*    */   
/*    */   public ByteSource(String path, byte[] bytes, long time) {
/* 37 */     this(path, bytes, time, -1);
/*    */   }
/*    */   public ByteSource(String path, byte[] bytes, int compressionMethod) {
/* 40 */     this(path, bytes, System.currentTimeMillis(), compressionMethod);
/*    */   }
/*    */   
/*    */   public ByteSource(String path, byte[] bytes, long time, int compressionMethod) {
/* 44 */     this.path = path;
/* 45 */     this.bytes = (byte[])bytes.clone();
/* 46 */     this.time = time;
/* 47 */     this.compressionMethod = compressionMethod;
/* 48 */     if (compressionMethod != -1) {
/* 49 */       CRC32 crc32 = new CRC32();
/* 50 */       crc32.update(bytes);
/* 51 */       this.crc = crc32.getValue();
/*    */     } else {
/* 53 */       this.crc = -1L;
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getPath() {
/* 58 */     return this.path;
/*    */   }
/*    */   
/*    */   public ZipEntry getEntry() {
/* 62 */     ZipEntry entry = new ZipEntry(this.path);
/* 63 */     if (this.bytes != null) {
/* 64 */       entry.setSize(this.bytes.length);
/*    */     }
/* 66 */     if (this.compressionMethod != -1) {
/* 67 */       entry.setMethod(this.compressionMethod);
/*    */     }
/* 69 */     if (this.crc != -1L) {
/* 70 */       entry.setCrc(this.crc);
/*    */     }
/* 72 */     entry.setTime(this.time);
/* 73 */     return entry;
/*    */   }
/*    */   
/*    */   public InputStream getInputStream() throws IOException {
/* 77 */     if (this.bytes == null) {
/* 78 */       return null;
/*    */     }
/*    */     
/* 81 */     return new ByteArrayInputStream(this.bytes);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 86 */     return "ByteSource[" + this.path + "]";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/ByteSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */