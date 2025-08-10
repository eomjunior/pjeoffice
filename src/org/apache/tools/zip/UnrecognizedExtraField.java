/*     */ package org.apache.tools.zip;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   implements CentralDirectoryParsingZipExtraField
/*     */ {
/*     */   private ZipShort headerId;
/*     */   private byte[] localData;
/*     */   private byte[] centralData;
/*     */   
/*     */   public void setHeaderId(ZipShort headerId) {
/*  44 */     this.headerId = headerId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort getHeaderId() {
/*  52 */     return this.headerId;
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
/*     */   public void setLocalFileDataData(byte[] data) {
/*  69 */     this.localData = ZipUtil.copy(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort getLocalFileDataLength() {
/*  77 */     return new ZipShort(this.localData.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getLocalFileDataData() {
/*  85 */     return ZipUtil.copy(this.localData);
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
/*     */   public void setCentralDirectoryData(byte[] data) {
/* 101 */     this.centralData = ZipUtil.copy(data);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort getCentralDirectoryLength() {
/* 110 */     if (this.centralData != null) {
/* 111 */       return new ZipShort(this.centralData.length);
/*     */     }
/* 113 */     return getLocalFileDataLength();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getCentralDirectoryData() {
/* 121 */     if (this.centralData != null) {
/* 122 */       return ZipUtil.copy(this.centralData);
/*     */     }
/* 124 */     return getLocalFileDataData();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parseFromLocalFileData(byte[] data, int offset, int length) {
/* 134 */     byte[] tmp = new byte[length];
/* 135 */     System.arraycopy(data, offset, tmp, 0, length);
/* 136 */     setLocalFileDataData(tmp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parseFromCentralDirectoryData(byte[] data, int offset, int length) {
/* 146 */     byte[] tmp = new byte[length];
/* 147 */     System.arraycopy(data, offset, tmp, 0, length);
/* 148 */     setCentralDirectoryData(tmp);
/* 149 */     if (this.localData == null)
/* 150 */       setLocalFileDataData(tmp); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/UnrecognizedExtraField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */