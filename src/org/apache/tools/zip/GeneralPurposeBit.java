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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GeneralPurposeBit
/*     */   implements Cloneable
/*     */ {
/*     */   private static final int ENCRYPTION_FLAG = 1;
/*     */   private static final int DATA_DESCRIPTOR_FLAG = 8;
/*     */   private static final int STRONG_ENCRYPTION_FLAG = 64;
/*     */   public static final int UFT8_NAMES_FLAG = 2048;
/*     */   private boolean languageEncodingFlag = false;
/*     */   private boolean dataDescriptorFlag = false;
/*     */   private boolean encryptionFlag = false;
/*     */   private boolean strongEncryptionFlag = false;
/*     */   
/*     */   public boolean usesUTF8ForNames() {
/*  66 */     return this.languageEncodingFlag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void useUTF8ForNames(boolean b) {
/*  75 */     this.languageEncodingFlag = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean usesDataDescriptor() {
/*  85 */     return this.dataDescriptorFlag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void useDataDescriptor(boolean b) {
/*  95 */     this.dataDescriptorFlag = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean usesEncryption() {
/* 104 */     return this.encryptionFlag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void useEncryption(boolean b) {
/* 113 */     this.encryptionFlag = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean usesStrongEncryption() {
/* 122 */     return (this.encryptionFlag && this.strongEncryptionFlag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void useStrongEncryption(boolean b) {
/* 131 */     this.strongEncryptionFlag = b;
/* 132 */     if (b) {
/* 133 */       useEncryption(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] encode() {
/* 143 */     byte[] result = new byte[2];
/* 144 */     encode(result, 0);
/* 145 */     return result;
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
/*     */   public void encode(byte[] buf, int offset) {
/* 157 */     ZipShort.putShort((this.dataDescriptorFlag ? 8 : 0) | (
/* 158 */         this.languageEncodingFlag ? 2048 : 0) | (
/* 159 */         this.encryptionFlag ? 1 : 0) | (
/* 160 */         this.strongEncryptionFlag ? 64 : 0), buf, offset);
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
/*     */   public static GeneralPurposeBit parse(byte[] data, int offset) {
/* 172 */     int generalPurposeFlag = ZipShort.getValue(data, offset);
/* 173 */     GeneralPurposeBit b = new GeneralPurposeBit();
/* 174 */     b.useDataDescriptor(((generalPurposeFlag & 0x8) != 0));
/* 175 */     b.useUTF8ForNames(((generalPurposeFlag & 0x800) != 0));
/* 176 */     b.useStrongEncryption(((generalPurposeFlag & 0x40) != 0));
/*     */     
/* 178 */     b.useEncryption(((generalPurposeFlag & 0x1) != 0));
/* 179 */     return b;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 184 */     return 3 * (7 * (13 * (17 * (this.encryptionFlag ? 1 : 0) + (
/* 185 */       this.strongEncryptionFlag ? 1 : 0)) + (
/* 186 */       this.languageEncodingFlag ? 1 : 0)) + (
/* 187 */       this.dataDescriptorFlag ? 1 : 0));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 192 */     if (o instanceof GeneralPurposeBit) {
/* 193 */       GeneralPurposeBit g = (GeneralPurposeBit)o;
/* 194 */       return (g.encryptionFlag == this.encryptionFlag && g.strongEncryptionFlag == this.strongEncryptionFlag && g.languageEncodingFlag == this.languageEncodingFlag && g.dataDescriptorFlag == this.dataDescriptorFlag);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 200 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     try {
/* 206 */       return super.clone();
/* 207 */     } catch (CloneNotSupportedException ex) {
/*     */       
/* 209 */       throw new RuntimeException("GeneralPurposeBit is not Cloneable?", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/GeneralPurposeBit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */