/*     */ package org.apache.tools.zip;
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
/*     */ public class AsiExtraField
/*     */   implements ZipExtraField, UnixStat, Cloneable
/*     */ {
/*  54 */   private static final ZipShort HEADER_ID = new ZipShort(30062);
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int WORD = 4;
/*     */ 
/*     */   
/*  61 */   private int mode = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   private int uid = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   private int gid = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   private String link = "";
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
/*  94 */   private CRC32 crc = new CRC32();
/*     */ 
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
/* 106 */     return HEADER_ID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort getLocalFileDataLength() {
/* 116 */     return new ZipShort(14 + (
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 121 */         getLinkedFile().getBytes()).length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipShort getCentralDirectoryLength() {
/* 131 */     return getLocalFileDataLength();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getLocalFileDataData() {
/* 142 */     byte[] data = new byte[getLocalFileDataLength().getValue() - 4];
/* 143 */     System.arraycopy(ZipShort.getBytes(getMode()), 0, data, 0, 2);
/*     */     
/* 145 */     byte[] linkArray = getLinkedFile().getBytes();
/*     */     
/* 147 */     System.arraycopy(ZipLong.getBytes(linkArray.length), 0, data, 2, 4);
/*     */ 
/*     */     
/* 150 */     System.arraycopy(ZipShort.getBytes(getUserId()), 0, data, 6, 2);
/*     */     
/* 152 */     System.arraycopy(ZipShort.getBytes(getGroupId()), 0, data, 8, 2);
/*     */ 
/*     */     
/* 155 */     System.arraycopy(linkArray, 0, data, 10, linkArray.length);
/*     */ 
/*     */     
/* 158 */     this.crc.reset();
/* 159 */     this.crc.update(data);
/* 160 */     long checksum = this.crc.getValue();
/*     */     
/* 162 */     byte[] result = new byte[data.length + 4];
/* 163 */     System.arraycopy(ZipLong.getBytes(checksum), 0, result, 0, 4);
/* 164 */     System.arraycopy(data, 0, result, 4, data.length);
/* 165 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getCentralDirectoryData() {
/* 174 */     return getLocalFileDataData();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserId(int uid) {
/* 183 */     this.uid = uid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUserId() {
/* 192 */     return this.uid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGroupId(int gid) {
/* 201 */     this.gid = gid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getGroupId() {
/* 210 */     return this.gid;
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
/* 222 */     this.link = name;
/* 223 */     this.mode = getMode(this.mode);
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
/* 235 */     return this.link;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLink() {
/* 244 */     return !getLinkedFile().isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMode(int mode) {
/* 253 */     this.mode = getMode(mode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMode() {
/* 262 */     return this.mode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDirectory(boolean dirFlag) {
/* 271 */     this.dirFlag = dirFlag;
/* 272 */     this.mode = getMode(this.mode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDirectory() {
/* 281 */     return (this.dirFlag && !isLink());
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
/*     */   public void parseFromLocalFileData(byte[] data, int offset, int length) throws ZipException {
/* 295 */     long givenChecksum = ZipLong.getValue(data, offset);
/* 296 */     byte[] tmp = new byte[length - 4];
/* 297 */     System.arraycopy(data, offset + 4, tmp, 0, length - 4);
/* 298 */     this.crc.reset();
/* 299 */     this.crc.update(tmp);
/* 300 */     long realChecksum = this.crc.getValue();
/* 301 */     if (givenChecksum != realChecksum) {
/* 302 */       throw new ZipException("bad CRC checksum " + 
/* 303 */           Long.toHexString(givenChecksum) + " instead of " + 
/*     */           
/* 305 */           Long.toHexString(realChecksum));
/*     */     }
/*     */     
/* 308 */     int newMode = ZipShort.getValue(tmp, 0);
/*     */     
/* 310 */     int linkArrayLength = (int)ZipLong.getValue(tmp, 2);
/* 311 */     if (linkArrayLength < 0 || linkArrayLength > tmp.length - 10) {
/* 312 */       throw new ZipException("Bad symbolic link name length " + linkArrayLength + " in ASI extra field");
/*     */     }
/*     */     
/* 315 */     this.uid = ZipShort.getValue(tmp, 6);
/* 316 */     this.gid = ZipShort.getValue(tmp, 8);
/* 317 */     if (linkArrayLength == 0) {
/* 318 */       this.link = "";
/*     */     } else {
/* 320 */       byte[] linkArray = new byte[linkArrayLength];
/* 321 */       System.arraycopy(tmp, 10, linkArray, 0, linkArrayLength);
/* 322 */       this.link = new String(linkArray);
/*     */     } 
/*     */     
/* 325 */     setDirectory(((newMode & 0x4000) != 0));
/* 326 */     setMode(newMode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getMode(int mode) {
/* 336 */     int type = 32768;
/* 337 */     if (isLink()) {
/* 338 */       type = 40960;
/* 339 */     } else if (isDirectory()) {
/* 340 */       type = 16384;
/*     */     } 
/* 342 */     return type | mode & 0xFFF;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 348 */       AsiExtraField cloned = (AsiExtraField)super.clone();
/* 349 */       cloned.crc = new CRC32();
/* 350 */       return cloned;
/* 351 */     } catch (CloneNotSupportedException cnfe) {
/*     */       
/* 353 */       throw new RuntimeException(cnfe);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/AsiExtraField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */