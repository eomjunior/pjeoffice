/*     */ package org.apache.tools.zip;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.zip.ZipEntry;
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
/*     */ public class ZipEntry
/*     */   extends ZipEntry
/*     */   implements Cloneable
/*     */ {
/*     */   public static final int PLATFORM_UNIX = 3;
/*     */   public static final int PLATFORM_FAT = 0;
/*     */   public static final int CRC_UNKNOWN = -1;
/*     */   private static final int SHORT_MASK = 65535;
/*     */   private static final int SHORT_SHIFT = 16;
/*  56 */   private static final byte[] EMPTY = new byte[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   private int method = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   private long size = -1L;
/*     */   
/*  74 */   private int internalAttributes = 0;
/*  75 */   private int platform = 0;
/*  76 */   private long externalAttributes = 0L;
/*     */   private ZipExtraField[] extraFields;
/*  78 */   private UnparseableExtraFieldData unparseableExtra = null;
/*  79 */   private String name = null;
/*  80 */   private byte[] rawName = null;
/*  81 */   private GeneralPurposeBit gpb = new GeneralPurposeBit();
/*  82 */   private static final ZipExtraField[] noExtraFields = new ZipExtraField[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipEntry(String name) {
/*  94 */     super(name);
/*  95 */     setName(name);
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
/*     */   public ZipEntry(ZipEntry entry) throws ZipException {
/* 109 */     super(entry);
/* 110 */     setName(entry.getName());
/* 111 */     byte[] extra = entry.getExtra();
/* 112 */     if (extra != null) {
/* 113 */       setExtraFields(ExtraFieldUtils.parse(extra, true, ExtraFieldUtils.UnparseableExtraField.READ));
/*     */     }
/*     */     else {
/*     */       
/* 117 */       setExtra();
/*     */     } 
/* 119 */     setMethod(entry.getMethod());
/* 120 */     this.size = entry.getSize();
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
/*     */   public ZipEntry(ZipEntry entry) throws ZipException {
/* 134 */     this(entry);
/* 135 */     setInternalAttributes(entry.getInternalAttributes());
/* 136 */     setExternalAttributes(entry.getExternalAttributes());
/* 137 */     setExtraFields(getAllExtraFieldsNoCopy());
/* 138 */     setPlatform(entry.getPlatform());
/* 139 */     GeneralPurposeBit other = entry.getGeneralPurposeBit();
/* 140 */     setGeneralPurposeBit((other == null) ? null : (GeneralPurposeBit)other.clone());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ZipEntry() {
/* 147 */     this("");
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
/*     */   public ZipEntry(File inputFile, String entryName) {
/* 163 */     this((inputFile.isDirectory() && !entryName.endsWith("/")) ? (entryName + "/") : entryName);
/* 164 */     if (inputFile.isFile()) {
/* 165 */       setSize(inputFile.length());
/*     */     }
/* 167 */     setTime(inputFile.lastModified());
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
/*     */   public Object clone() {
/* 179 */     ZipEntry e = (ZipEntry)super.clone();
/*     */     
/* 181 */     e.setInternalAttributes(getInternalAttributes());
/* 182 */     e.setExternalAttributes(getExternalAttributes());
/* 183 */     e.setExtraFields(getAllExtraFieldsNoCopy());
/* 184 */     return e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMethod() {
/* 195 */     return this.method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMethod(int method) {
/* 205 */     if (method < 0) {
/* 206 */       throw new IllegalArgumentException("ZIP compression method can not be negative: " + method);
/*     */     }
/*     */     
/* 209 */     this.method = method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInternalAttributes() {
/* 219 */     return this.internalAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInternalAttributes(int value) {
/* 229 */     this.internalAttributes = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getExternalAttributes() {
/* 239 */     return this.externalAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExternalAttributes(long value) {
/* 249 */     this.externalAttributes = value;
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
/*     */   public void setUnixMode(int mode) {
/* 261 */     setExternalAttributes((mode << 16 | (
/*     */         
/* 263 */         ((mode & 0x80) == 0) ? 1 : 0) | (
/*     */         
/* 265 */         isDirectory() ? 16 : 0)));
/*     */     
/* 267 */     this.platform = 3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUnixMode() {
/* 277 */     return (this.platform != 3) ? 0 : 
/* 278 */       (int)(getExternalAttributes() >> 16L & 0xFFFFL);
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
/*     */   public int getPlatform() {
/* 291 */     return this.platform;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setPlatform(int platform) {
/* 301 */     this.platform = platform;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExtraFields(ZipExtraField[] fields) {
/* 311 */     List<ZipExtraField> newFields = new ArrayList<>();
/* 312 */     for (ZipExtraField field : fields) {
/* 313 */       if (field instanceof UnparseableExtraFieldData) {
/* 314 */         this.unparseableExtra = (UnparseableExtraFieldData)field;
/*     */       } else {
/* 316 */         newFields.add(field);
/*     */       } 
/*     */     } 
/* 319 */     this.extraFields = newFields.<ZipExtraField>toArray(new ZipExtraField[0]);
/* 320 */     setExtra();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipExtraField[] getExtraFields() {
/* 329 */     return getParseableExtraFields();
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
/*     */   public ZipExtraField[] getExtraFields(boolean includeUnparseable) {
/* 342 */     return includeUnparseable ? getAllExtraFields() : getParseableExtraFields();
/*     */   }
/*     */   
/*     */   private ZipExtraField[] getParseableExtraFieldsNoCopy() {
/* 346 */     if (this.extraFields == null) {
/* 347 */       return noExtraFields;
/*     */     }
/* 349 */     return this.extraFields;
/*     */   }
/*     */   
/*     */   private ZipExtraField[] getParseableExtraFields() {
/* 353 */     ZipExtraField[] parseableExtraFields = getParseableExtraFieldsNoCopy();
/* 354 */     return (parseableExtraFields == this.extraFields) ? copyOf(parseableExtraFields) : 
/* 355 */       parseableExtraFields;
/*     */   }
/*     */   
/*     */   private ZipExtraField[] copyOf(ZipExtraField[] src) {
/* 359 */     return copyOf(src, src.length);
/*     */   }
/*     */   
/*     */   private ZipExtraField[] copyOf(ZipExtraField[] src, int length) {
/* 363 */     ZipExtraField[] cpy = new ZipExtraField[length];
/* 364 */     System.arraycopy(src, 0, cpy, 0, Math.min(src.length, length));
/* 365 */     return cpy;
/*     */   }
/*     */   
/*     */   private ZipExtraField[] getMergedFields() {
/* 369 */     ZipExtraField[] zipExtraFields = copyOf(this.extraFields, this.extraFields.length + 1);
/* 370 */     zipExtraFields[this.extraFields.length] = this.unparseableExtra;
/* 371 */     return zipExtraFields;
/*     */   }
/*     */   
/*     */   private ZipExtraField[] getUnparseableOnly() {
/* 375 */     (new ZipExtraField[1])[0] = this.unparseableExtra; return (this.unparseableExtra == null) ? noExtraFields : new ZipExtraField[1];
/*     */   }
/*     */   
/*     */   private ZipExtraField[] getAllExtraFields() {
/* 379 */     ZipExtraField[] allExtraFieldsNoCopy = getAllExtraFieldsNoCopy();
/* 380 */     return (allExtraFieldsNoCopy == this.extraFields) ? copyOf(allExtraFieldsNoCopy) : 
/* 381 */       allExtraFieldsNoCopy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ZipExtraField[] getAllExtraFieldsNoCopy() {
/* 390 */     if (this.extraFields == null) {
/* 391 */       return getUnparseableOnly();
/*     */     }
/* 393 */     return (this.unparseableExtra != null) ? getMergedFields() : this.extraFields;
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
/*     */   public void addExtraField(ZipExtraField ze) {
/* 407 */     if (ze instanceof UnparseableExtraFieldData) {
/* 408 */       this.unparseableExtra = (UnparseableExtraFieldData)ze;
/*     */     }
/* 410 */     else if (this.extraFields == null) {
/* 411 */       this.extraFields = new ZipExtraField[] { ze };
/*     */     } else {
/* 413 */       if (getExtraField(ze.getHeaderId()) != null) {
/* 414 */         removeExtraField(ze.getHeaderId());
/*     */       }
/* 416 */       ZipExtraField[] zipExtraFields = copyOf(this.extraFields, this.extraFields.length + 1);
/* 417 */       zipExtraFields[this.extraFields.length] = ze;
/* 418 */       this.extraFields = zipExtraFields;
/*     */     } 
/*     */     
/* 421 */     setExtra();
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
/*     */   public void addAsFirstExtraField(ZipExtraField ze) {
/* 434 */     if (ze instanceof UnparseableExtraFieldData) {
/* 435 */       this.unparseableExtra = (UnparseableExtraFieldData)ze;
/*     */     } else {
/* 437 */       if (getExtraField(ze.getHeaderId()) != null) {
/* 438 */         removeExtraField(ze.getHeaderId());
/*     */       }
/* 440 */       ZipExtraField[] copy = this.extraFields;
/* 441 */       int newLen = (this.extraFields != null) ? (this.extraFields.length + 1) : 1;
/* 442 */       this.extraFields = new ZipExtraField[newLen];
/* 443 */       this.extraFields[0] = ze;
/* 444 */       if (copy != null) {
/* 445 */         System.arraycopy(copy, 0, this.extraFields, 1, this.extraFields.length - 1);
/*     */       }
/*     */     } 
/* 448 */     setExtra();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeExtraField(ZipShort type) {
/* 458 */     if (this.extraFields == null) {
/* 459 */       throw new NoSuchElementException();
/*     */     }
/* 461 */     List<ZipExtraField> newResult = new ArrayList<>();
/* 462 */     for (ZipExtraField extraField : this.extraFields) {
/* 463 */       if (!type.equals(extraField.getHeaderId())) {
/* 464 */         newResult.add(extraField);
/*     */       }
/*     */     } 
/* 467 */     if (this.extraFields.length == newResult.size()) {
/* 468 */       throw new NoSuchElementException();
/*     */     }
/* 470 */     this.extraFields = newResult.<ZipExtraField>toArray(new ZipExtraField[0]);
/* 471 */     setExtra();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeUnparseableExtraFieldData() {
/* 478 */     if (this.unparseableExtra == null) {
/* 479 */       throw new NoSuchElementException();
/*     */     }
/* 481 */     this.unparseableExtra = null;
/* 482 */     setExtra();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ZipExtraField getExtraField(ZipShort type) {
/* 492 */     if (this.extraFields != null) {
/* 493 */       for (ZipExtraField extraField : this.extraFields) {
/* 494 */         if (type.equals(extraField.getHeaderId())) {
/* 495 */           return extraField;
/*     */         }
/*     */       } 
/*     */     }
/* 499 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnparseableExtraFieldData getUnparseableExtraFieldData() {
/* 508 */     return this.unparseableExtra;
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
/*     */   public void setExtra(byte[] extra) throws RuntimeException {
/*     */     try {
/* 524 */       ZipExtraField[] local = ExtraFieldUtils.parse(extra, true, ExtraFieldUtils.UnparseableExtraField.READ);
/*     */       
/* 526 */       mergeExtraFields(local, true);
/* 527 */     } catch (ZipException e) {
/*     */       
/* 529 */       throw new RuntimeException("Error parsing extra fields for entry: " + 
/* 530 */           getName() + " - " + e.getMessage(), e);
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
/*     */   protected void setExtra() {
/* 543 */     super.setExtra(ExtraFieldUtils.mergeLocalFileDataData(getExtraFields(true)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCentralDirectoryExtra(byte[] b) {
/*     */     try {
/* 553 */       ZipExtraField[] central = ExtraFieldUtils.parse(b, false, ExtraFieldUtils.UnparseableExtraField.READ);
/*     */       
/* 555 */       mergeExtraFields(central, false);
/* 556 */     } catch (ZipException e) {
/* 557 */       throw new RuntimeException(e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getLocalFileDataExtra() {
/* 568 */     byte[] extra = getExtra();
/* 569 */     return (extra != null) ? extra : EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getCentralDirectoryExtra() {
/* 579 */     return ExtraFieldUtils.mergeCentralDirectoryData(getExtraFields(true));
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
/*     */   @Deprecated
/*     */   public void setComprSize(long size) {
/* 595 */     setCompressedSize(size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 606 */     return (this.name == null) ? super.getName() : this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDirectory() {
/* 617 */     return getName().endsWith("/");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setName(String name) {
/* 626 */     if (name != null && getPlatform() == 0 && !name.contains("/")) {
/* 627 */       name = name.replace('\\', '/');
/*     */     }
/* 629 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSize() {
/* 639 */     return this.size;
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
/*     */   public void setSize(long size) {
/* 651 */     if (size < 0L) {
/* 652 */       throw new IllegalArgumentException("invalid entry size");
/*     */     }
/* 654 */     this.size = size;
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
/*     */   protected void setName(String name, byte[] rawName) {
/* 667 */     setName(name);
/* 668 */     this.rawName = rawName;
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
/*     */   public byte[] getRawName() {
/* 681 */     if (this.rawName != null) {
/* 682 */       byte[] b = new byte[this.rawName.length];
/* 683 */       System.arraycopy(this.rawName, 0, b, 0, this.rawName.length);
/* 684 */       return b;
/*     */     } 
/* 686 */     return null;
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
/*     */   public int hashCode() {
/* 702 */     return getName().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GeneralPurposeBit getGeneralPurposeBit() {
/* 711 */     return this.gpb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGeneralPurposeBit(GeneralPurposeBit b) {
/* 720 */     this.gpb = b;
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
/*     */   private void mergeExtraFields(ZipExtraField[] f, boolean local) throws ZipException {
/* 734 */     if (this.extraFields == null) {
/* 735 */       setExtraFields(f);
/*     */     } else {
/* 737 */       for (ZipExtraField element : f) {
/*     */         ZipExtraField existing;
/* 739 */         if (element instanceof UnparseableExtraFieldData) {
/* 740 */           existing = this.unparseableExtra;
/*     */         } else {
/* 742 */           existing = getExtraField(element.getHeaderId());
/*     */         } 
/* 744 */         if (existing == null) {
/* 745 */           addExtraField(element);
/*     */         }
/* 747 */         else if (local || !(existing instanceof CentralDirectoryParsingZipExtraField)) {
/*     */           
/* 749 */           byte[] b = element.getLocalFileDataData();
/* 750 */           existing.parseFromLocalFileData(b, 0, b.length);
/*     */         } else {
/* 752 */           byte[] b = element.getCentralDirectoryData();
/* 753 */           ((CentralDirectoryParsingZipExtraField)existing)
/* 754 */             .parseFromCentralDirectoryData(b, 0, b.length);
/*     */         } 
/*     */       } 
/*     */       
/* 758 */       setExtra();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getLastModifiedDate() {
/* 764 */     return new Date(getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 772 */     if (this == obj) {
/* 773 */       return true;
/*     */     }
/* 775 */     if (obj == null || getClass() != obj.getClass()) {
/* 776 */       return false;
/*     */     }
/* 778 */     ZipEntry other = (ZipEntry)obj;
/* 779 */     String myName = getName();
/* 780 */     String otherName = other.getName();
/* 781 */     if (myName == null) {
/* 782 */       if (otherName != null) {
/* 783 */         return false;
/*     */       }
/* 785 */     } else if (!myName.equals(otherName)) {
/* 786 */       return false;
/*     */     } 
/* 788 */     String myComment = getComment();
/* 789 */     String otherComment = other.getComment();
/* 790 */     if (myComment == null) {
/* 791 */       myComment = "";
/*     */     }
/* 793 */     if (otherComment == null) {
/* 794 */       otherComment = "";
/*     */     }
/* 796 */     return (getTime() == other.getTime() && myComment
/* 797 */       .equals(otherComment) && 
/* 798 */       getInternalAttributes() == other.getInternalAttributes() && 
/* 799 */       getPlatform() == other.getPlatform() && 
/* 800 */       getExternalAttributes() == other.getExternalAttributes() && 
/* 801 */       getMethod() == other.getMethod() && 
/* 802 */       getSize() == other.getSize() && 
/* 803 */       getCrc() == other.getCrc() && 
/* 804 */       getCompressedSize() == other.getCompressedSize() && 
/* 805 */       Arrays.equals(getCentralDirectoryExtra(), other.getCentralDirectoryExtra()) && 
/* 806 */       Arrays.equals(getLocalFileDataExtra(), other.getLocalFileDataExtra()) && this.gpb
/* 807 */       .equals(other.gpb));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/ZipEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */