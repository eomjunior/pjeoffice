/*     */ package org.zeroturnaround.zip.extra;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsiExtraField
/*     */   implements ZipExtraField, Cloneable
/*     */ {
/*  70 */   final int PERM_MASK = 4095;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   final int LINK_FLAG = 40960;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   final int FILE_FLAG = 32768;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   final int DIR_FLAG = 16384;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   final int DEFAULT_LINK_PERM = 511;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   final int DEFAULT_DIR_PERM = 493;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   final int DEFAULT_FILE_PERM = 420;
/*     */   
/* 114 */   private static final ZipShort HEADER_ID = new ZipShort(30062);
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int WORD = 4;
/*     */ 
/*     */   
/* 121 */   private int mode = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   private int uid = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 133 */   private int gid = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   private String link = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean dirFlag = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 156 */   private CRC32 crc = new CRC32();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort getHeaderId() {
/* 169 */     return HEADER_ID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort getLocalFileDataLength() {
/* 180 */     return new ZipShort(14 + (
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 185 */         getLinkedFile().getBytes()).length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort getCentralDirectoryLength() {
/* 196 */     return getLocalFileDataLength();
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
/*     */   public byte[] getLocalFileDataData() {
/* 208 */     byte[] data = new byte[getLocalFileDataLength().getValue() - 4];
/* 209 */     System.arraycopy(ZipShort.getBytes(getMode()), 0, data, 0, 2);
/*     */     
/* 211 */     byte[] linkArray = getLinkedFile().getBytes();
/*     */     
/* 213 */     System.arraycopy(ZipLong.getBytes(linkArray.length), 0, data, 2, 4);
/*     */ 
/*     */     
/* 216 */     System.arraycopy(ZipShort.getBytes(getUserId()), 0, data, 6, 2);
/*     */     
/* 218 */     System.arraycopy(ZipShort.getBytes(getGroupId()), 0, data, 8, 2);
/*     */ 
/*     */     
/* 221 */     System.arraycopy(linkArray, 0, data, 10, linkArray.length);
/*     */ 
/*     */     
/* 224 */     this.crc.reset();
/* 225 */     this.crc.update(data);
/* 226 */     long checksum = this.crc.getValue();
/*     */     
/* 228 */     byte[] result = new byte[data.length + 4];
/* 229 */     System.arraycopy(ZipLong.getBytes(checksum), 0, result, 0, 4);
/* 230 */     System.arraycopy(data, 0, result, 4, data.length);
/* 231 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getCentralDirectoryData() {
/* 241 */     return getLocalFileDataData();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserId(int uid) {
/* 251 */     this.uid = uid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUserId() {
/* 261 */     return this.uid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGroupId(int gid) {
/* 271 */     this.gid = gid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGroupId() {
/* 281 */     return this.gid;
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
/*     */   public void setLinkedFile(String name) {
/* 293 */     this.link = name;
/* 294 */     this.mode = getMode(this.mode);
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
/*     */   public String getLinkedFile() {
/* 306 */     return this.link;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLink() {
/* 316 */     return (getLinkedFile().length() != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMode(int mode) {
/* 326 */     this.mode = getMode(mode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMode() {
/* 336 */     return this.mode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDirectory(boolean dirFlag) {
/* 346 */     this.dirFlag = dirFlag;
/* 347 */     this.mode = getMode(this.mode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDirectory() {
/* 357 */     return (this.dirFlag && !isLink());
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
/*     */   public void parseFromLocalFileData(byte[] data, int offset, int length) throws ZipException {
/* 372 */     long givenChecksum = ZipLong.getValue(data, offset);
/* 373 */     byte[] tmp = new byte[length - 4];
/* 374 */     System.arraycopy(data, offset + 4, tmp, 0, length - 4);
/* 375 */     this.crc.reset();
/* 376 */     this.crc.update(tmp);
/* 377 */     long realChecksum = this.crc.getValue();
/* 378 */     if (givenChecksum != realChecksum) {
/* 379 */       throw new ZipException("bad CRC checksum " + 
/* 380 */           Long.toHexString(givenChecksum) + " instead of " + 
/*     */           
/* 382 */           Long.toHexString(realChecksum));
/*     */     }
/*     */     
/* 385 */     int newMode = ZipShort.getValue(tmp, 0);
/*     */     
/* 387 */     byte[] linkArray = new byte[(int)ZipLong.getValue(tmp, 2)];
/* 388 */     this.uid = ZipShort.getValue(tmp, 6);
/* 389 */     this.gid = ZipShort.getValue(tmp, 8);
/*     */     
/* 391 */     if (linkArray.length == 0) {
/* 392 */       this.link = "";
/*     */     } else {
/*     */       
/* 395 */       System.arraycopy(tmp, 10, linkArray, 0, linkArray.length);
/* 396 */       this.link = new String(linkArray);
/*     */     } 
/*     */     
/* 399 */     setDirectory(((newMode & 0x4000) != 0));
/* 400 */     setMode(newMode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getMode(int mode) {
/* 411 */     int type = 32768;
/* 412 */     if (isLink()) {
/* 413 */       type = 40960;
/*     */     }
/* 415 */     else if (isDirectory()) {
/* 416 */       type = 16384;
/*     */     } 
/* 418 */     return type | mode & 0xFFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 424 */       AsiExtraField cloned = (AsiExtraField)super.clone();
/* 425 */       cloned.crc = new CRC32();
/* 426 */       return cloned;
/*     */     }
/* 428 */     catch (CloneNotSupportedException cnfe) {
/*     */       
/* 430 */       throw new RuntimeException(cnfe);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/extra/AsiExtraField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */