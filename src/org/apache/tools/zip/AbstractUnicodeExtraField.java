/*     */ package org.apache.tools.zip;
/*     */ 
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.ZipException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractUnicodeExtraField
/*     */   implements ZipExtraField
/*     */ {
/*     */   private long nameCRC32;
/*     */   private byte[] unicodeName;
/*     */   private byte[] data;
/*     */   
/*     */   protected AbstractUnicodeExtraField() {}
/*     */   
/*     */   protected AbstractUnicodeExtraField(String text, byte[] bytes, int off, int len) {
/*  50 */     CRC32 crc32 = new CRC32();
/*  51 */     crc32.update(bytes, off, len);
/*  52 */     this.nameCRC32 = crc32.getValue();
/*     */     
/*  54 */     this.unicodeName = text.getBytes(StandardCharsets.UTF_8);
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
/*     */   protected AbstractUnicodeExtraField(String text, byte[] bytes) {
/*  67 */     this(text, bytes, 0, bytes.length);
/*     */   }
/*     */   
/*     */   private void assembleData() {
/*  71 */     if (this.unicodeName == null) {
/*     */       return;
/*     */     }
/*     */     
/*  75 */     this.data = new byte[5 + this.unicodeName.length];
/*     */     
/*  77 */     this.data[0] = 1;
/*  78 */     System.arraycopy(ZipLong.getBytes(this.nameCRC32), 0, this.data, 1, 4);
/*  79 */     System.arraycopy(this.unicodeName, 0, this.data, 5, this.unicodeName.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getNameCRC32() {
/*  87 */     return this.nameCRC32;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNameCRC32(long nameCRC32) {
/*  95 */     this.nameCRC32 = nameCRC32;
/*  96 */     this.data = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getUnicodeName() {
/* 103 */     byte[] b = null;
/* 104 */     if (this.unicodeName != null) {
/* 105 */       b = new byte[this.unicodeName.length];
/* 106 */       System.arraycopy(this.unicodeName, 0, b, 0, b.length);
/*     */     } 
/* 108 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnicodeName(byte[] unicodeName) {
/* 115 */     if (unicodeName != null) {
/* 116 */       this.unicodeName = new byte[unicodeName.length];
/* 117 */       System.arraycopy(unicodeName, 0, this.unicodeName, 0, unicodeName.length);
/*     */     } else {
/*     */       
/* 120 */       this.unicodeName = null;
/*     */     } 
/* 122 */     this.data = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getCentralDirectoryData() {
/* 127 */     if (this.data == null) {
/* 128 */       assembleData();
/*     */     }
/* 130 */     byte[] b = null;
/* 131 */     if (this.data != null) {
/* 132 */       b = new byte[this.data.length];
/* 133 */       System.arraycopy(this.data, 0, b, 0, b.length);
/*     */     } 
/* 135 */     return b;
/*     */   }
/*     */ 
/*     */   
/*     */   public ZipShort getCentralDirectoryLength() {
/* 140 */     if (this.data == null) {
/* 141 */       assembleData();
/*     */     }
/* 143 */     return new ZipShort(this.data.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getLocalFileDataData() {
/* 148 */     return getCentralDirectoryData();
/*     */   }
/*     */ 
/*     */   
/*     */   public ZipShort getLocalFileDataLength() {
/* 153 */     return getCentralDirectoryLength();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parseFromLocalFileData(byte[] buffer, int offset, int length) throws ZipException {
/* 160 */     if (length < 5) {
/* 161 */       throw new ZipException("UniCode path extra data must have at least 5 bytes.");
/*     */     }
/*     */ 
/*     */     
/* 165 */     int version = buffer[offset];
/*     */     
/* 167 */     if (version != 1) {
/* 168 */       throw new ZipException("Unsupported version [" + version + "] for UniCode path extra data.");
/*     */     }
/*     */ 
/*     */     
/* 172 */     this.nameCRC32 = ZipLong.getValue(buffer, offset + 1);
/* 173 */     this.unicodeName = new byte[length - 5];
/* 174 */     System.arraycopy(buffer, offset + 5, this.unicodeName, 0, length - 5);
/* 175 */     this.data = null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/AbstractUnicodeExtraField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */