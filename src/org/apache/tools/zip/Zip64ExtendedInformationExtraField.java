/*     */ package org.apache.tools.zip;
/*     */ 
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
/*     */ public class Zip64ExtendedInformationExtraField
/*     */   implements CentralDirectoryParsingZipExtraField
/*     */ {
/*  44 */   static final ZipShort HEADER_ID = new ZipShort(1);
/*     */ 
/*     */   
/*     */   private static final String LFH_MUST_HAVE_BOTH_SIZES_MSG = "Zip64 extended information must contain both size values in the local file header.";
/*     */   
/*  49 */   private static final byte[] EMPTY = new byte[0];
/*     */ 
/*     */ 
/*     */   
/*     */   private ZipEightByteInteger size;
/*     */ 
/*     */ 
/*     */   
/*     */   private ZipEightByteInteger compressedSize;
/*     */ 
/*     */ 
/*     */   
/*     */   private ZipEightByteInteger relativeHeaderOffset;
/*     */ 
/*     */ 
/*     */   
/*     */   private ZipLong diskStart;
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] rawCentralDirectoryData;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Zip64ExtendedInformationExtraField() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Zip64ExtendedInformationExtraField(ZipEightByteInteger size, ZipEightByteInteger compressedSize) {
/*  79 */     this(size, compressedSize, null, null);
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
/*     */   public Zip64ExtendedInformationExtraField(ZipEightByteInteger size, ZipEightByteInteger compressedSize, ZipEightByteInteger relativeHeaderOffset, ZipLong diskStart) {
/*  95 */     this.size = size;
/*  96 */     this.compressedSize = compressedSize;
/*  97 */     this.relativeHeaderOffset = relativeHeaderOffset;
/*  98 */     this.diskStart = diskStart;
/*     */   }
/*     */ 
/*     */   
/*     */   public ZipShort getHeaderId() {
/* 103 */     return HEADER_ID;
/*     */   }
/*     */ 
/*     */   
/*     */   public ZipShort getLocalFileDataLength() {
/* 108 */     return new ZipShort((this.size != null) ? 16 : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public ZipShort getCentralDirectoryLength() {
/* 113 */     return new ZipShort(((this.size != null) ? 8 : 0) + (
/* 114 */         (this.compressedSize != null) ? 8 : 0) + (
/* 115 */         (this.relativeHeaderOffset != null) ? 8 : 0) + (
/* 116 */         (this.diskStart != null) ? 4 : 0));
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getLocalFileDataData() {
/* 121 */     if (this.size != null || this.compressedSize != null) {
/* 122 */       if (this.size == null || this.compressedSize == null) {
/* 123 */         throw new IllegalArgumentException("Zip64 extended information must contain both size values in the local file header.");
/*     */       }
/* 125 */       byte[] data = new byte[16];
/* 126 */       addSizes(data);
/* 127 */       return data;
/*     */     } 
/* 129 */     return EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getCentralDirectoryData() {
/* 134 */     byte[] data = new byte[getCentralDirectoryLength().getValue()];
/* 135 */     int off = addSizes(data);
/* 136 */     if (this.relativeHeaderOffset != null) {
/* 137 */       System.arraycopy(this.relativeHeaderOffset.getBytes(), 0, data, off, 8);
/* 138 */       off += 8;
/*     */     } 
/* 140 */     if (this.diskStart != null) {
/* 141 */       System.arraycopy(this.diskStart.getBytes(), 0, data, off, 4);
/* 142 */       off += 4;
/*     */     } 
/* 144 */     return data;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void parseFromLocalFileData(byte[] buffer, int offset, int length) throws ZipException {
/* 150 */     if (length == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 157 */     if (length < 16) {
/* 158 */       throw new ZipException("Zip64 extended information must contain both size values in the local file header.");
/*     */     }
/* 160 */     this.size = new ZipEightByteInteger(buffer, offset);
/* 161 */     offset += 8;
/* 162 */     this.compressedSize = new ZipEightByteInteger(buffer, offset);
/* 163 */     offset += 8;
/* 164 */     int remaining = length - 16;
/* 165 */     if (remaining >= 8) {
/* 166 */       this.relativeHeaderOffset = new ZipEightByteInteger(buffer, offset);
/* 167 */       offset += 8;
/* 168 */       remaining -= 8;
/*     */     } 
/* 170 */     if (remaining >= 4) {
/* 171 */       this.diskStart = new ZipLong(buffer, offset);
/* 172 */       offset += 4;
/* 173 */       remaining -= 4;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parseFromCentralDirectoryData(byte[] buffer, int offset, int length) throws ZipException {
/* 182 */     this.rawCentralDirectoryData = new byte[length];
/* 183 */     System.arraycopy(buffer, offset, this.rawCentralDirectoryData, 0, length);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     if (length >= 28) {
/* 192 */       parseFromLocalFileData(buffer, offset, length);
/* 193 */     } else if (length == 24) {
/* 194 */       this.size = new ZipEightByteInteger(buffer, offset);
/* 195 */       offset += 8;
/* 196 */       this.compressedSize = new ZipEightByteInteger(buffer, offset);
/* 197 */       offset += 8;
/* 198 */       this.relativeHeaderOffset = new ZipEightByteInteger(buffer, offset);
/* 199 */     } else if (length % 8 == 4) {
/* 200 */       this.diskStart = new ZipLong(buffer, offset + length - 4);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reparseCentralDirectoryData(boolean hasUncompressedSize, boolean hasCompressedSize, boolean hasRelativeHeaderOffset, boolean hasDiskStart) throws ZipException {
/* 224 */     if (this.rawCentralDirectoryData != null) {
/*     */ 
/*     */ 
/*     */       
/* 228 */       int expectedLength = (hasUncompressedSize ? 8 : 0) + (hasCompressedSize ? 8 : 0) + (hasRelativeHeaderOffset ? 8 : 0) + (hasDiskStart ? 4 : 0);
/* 229 */       if (this.rawCentralDirectoryData.length < expectedLength) {
/* 230 */         throw new ZipException("central directory zip64 extended information extra field's length doesn't match central directory data.  Expected length " + expectedLength + " but is " + this.rawCentralDirectoryData.length);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 237 */       int offset = 0;
/* 238 */       if (hasUncompressedSize) {
/* 239 */         this.size = new ZipEightByteInteger(this.rawCentralDirectoryData, offset);
/* 240 */         offset += 8;
/*     */       } 
/* 242 */       if (hasCompressedSize) {
/* 243 */         this.compressedSize = new ZipEightByteInteger(this.rawCentralDirectoryData, offset);
/*     */         
/* 245 */         offset += 8;
/*     */       } 
/* 247 */       if (hasRelativeHeaderOffset) {
/* 248 */         this.relativeHeaderOffset = new ZipEightByteInteger(this.rawCentralDirectoryData, offset);
/*     */         
/* 250 */         offset += 8;
/*     */       } 
/* 252 */       if (hasDiskStart) {
/* 253 */         this.diskStart = new ZipLong(this.rawCentralDirectoryData, offset);
/* 254 */         offset += 4;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipEightByteInteger getSize() {
/* 265 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(ZipEightByteInteger size) {
/* 274 */     this.size = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipEightByteInteger getCompressedSize() {
/* 283 */     return this.compressedSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompressedSize(ZipEightByteInteger compressedSize) {
/* 292 */     this.compressedSize = compressedSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipEightByteInteger getRelativeHeaderOffset() {
/* 301 */     return this.relativeHeaderOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRelativeHeaderOffset(ZipEightByteInteger rho) {
/* 310 */     this.relativeHeaderOffset = rho;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipLong getDiskStartNumber() {
/* 319 */     return this.diskStart;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDiskStartNumber(ZipLong ds) {
/* 328 */     this.diskStart = ds;
/*     */   }
/*     */   
/*     */   private int addSizes(byte[] data) {
/* 332 */     int off = 0;
/* 333 */     if (this.size != null) {
/* 334 */       System.arraycopy(this.size.getBytes(), 0, data, 0, 8);
/* 335 */       off += 8;
/*     */     } 
/* 337 */     if (this.compressedSize != null) {
/* 338 */       System.arraycopy(this.compressedSize.getBytes(), 0, data, off, 8);
/* 339 */       off += 8;
/*     */     } 
/* 341 */     return off;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/Zip64ExtendedInformationExtraField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */