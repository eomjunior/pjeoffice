/*     */ package org.zeroturnaround.zip.extra;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UnrecognizedExtraField
/*     */   implements ZipExtraField
/*     */ {
/*     */   private ZipShort headerId;
/*     */   private byte[] localData;
/*     */   private byte[] centralData;
/*     */   
/*     */   public void setHeaderId(ZipShort headerId) {
/*  49 */     this.headerId = headerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort getHeaderId() {
/*  58 */     return this.headerId;
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
/*     */   public void setLocalFileDataData(byte[] data) {
/*  76 */     this.localData = copy(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort getLocalFileDataLength() {
/*  85 */     return new ZipShort(this.localData.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getLocalFileDataData() {
/*  94 */     return copy(this.localData);
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
/*     */   public void setCentralDirectoryData(byte[] data) {
/* 111 */     this.centralData = copy(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort getCentralDirectoryLength() {
/* 121 */     if (this.centralData != null) {
/* 122 */       return new ZipShort(this.centralData.length);
/*     */     }
/* 124 */     return getLocalFileDataLength();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getCentralDirectoryData() {
/* 133 */     if (this.centralData != null) {
/* 134 */       return copy(this.centralData);
/*     */     }
/* 136 */     return getLocalFileDataData();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parseFromLocalFileData(byte[] data, int offset, int length) {
/* 146 */     byte[] tmp = new byte[length];
/* 147 */     System.arraycopy(data, offset, tmp, 0, length);
/* 148 */     setLocalFileDataData(tmp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parseFromCentralDirectoryData(byte[] data, int offset, int length) {
/* 158 */     byte[] tmp = new byte[length];
/* 159 */     System.arraycopy(data, offset, tmp, 0, length);
/* 160 */     setCentralDirectoryData(tmp);
/* 161 */     if (this.localData == null) {
/* 162 */       setLocalFileDataData(tmp);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] copy(byte[] from) {
/* 171 */     if (from != null) {
/* 172 */       byte[] to = new byte[from.length];
/* 173 */       System.arraycopy(from, 0, to, 0, to.length);
/* 174 */       return to;
/*     */     } 
/* 176 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/extra/UnrecognizedExtraField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */