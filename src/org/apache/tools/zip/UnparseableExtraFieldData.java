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
/*     */ public final class UnparseableExtraFieldData
/*     */   implements CentralDirectoryParsingZipExtraField
/*     */ {
/*  34 */   private static final ZipShort HEADER_ID = new ZipShort(44225);
/*     */ 
/*     */   
/*     */   private byte[] localFileData;
/*     */ 
/*     */   
/*     */   private byte[] centralDirectoryData;
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort getHeaderId() {
/*  45 */     return HEADER_ID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort getLocalFileDataLength() {
/*  54 */     return new ZipShort((this.localFileData == null) ? 0 : this.localFileData.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort getCentralDirectoryLength() {
/*  63 */     return (this.centralDirectoryData == null) ? 
/*  64 */       getLocalFileDataLength() : 
/*  65 */       new ZipShort(this.centralDirectoryData.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getLocalFileDataData() {
/*  74 */     return ZipUtil.copy(this.localFileData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getCentralDirectoryData() {
/*  83 */     return (this.centralDirectoryData == null) ? 
/*  84 */       getLocalFileDataData() : ZipUtil.copy(this.centralDirectoryData);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parseFromLocalFileData(byte[] buffer, int offset, int length) {
/*  95 */     this.localFileData = new byte[length];
/*  96 */     System.arraycopy(buffer, offset, this.localFileData, 0, length);
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
/*     */   public void parseFromCentralDirectoryData(byte[] buffer, int offset, int length) {
/* 108 */     this.centralDirectoryData = new byte[length];
/* 109 */     System.arraycopy(buffer, offset, this.centralDirectoryData, 0, length);
/* 110 */     if (this.localFileData == null)
/* 111 */       parseFromLocalFileData(buffer, offset, length); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/UnparseableExtraFieldData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */