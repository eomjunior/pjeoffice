/*     */ package org.apache.tools.zip;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.zip.CRC32;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ZipUtil
/*     */ {
/*  33 */   private static final byte[] DOS_TIME_MIN = ZipLong.getBytes(8448L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ZipLong toDosTime(Date time) {
/*  42 */     return new ZipLong(toDosTime(time.getTime()));
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
/*     */   public static byte[] toDosTime(long t) {
/*  54 */     byte[] result = new byte[4];
/*  55 */     toDosTime(t, result, 0);
/*  56 */     return result;
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
/*     */   public static void toDosTime(long t, byte[] buf, int offset) {
/*  71 */     toDosTime(Calendar.getInstance(), t, buf, offset);
/*     */   }
/*     */   
/*     */   static void toDosTime(Calendar c, long t, byte[] buf, int offset) {
/*  75 */     c.setTimeInMillis(t);
/*     */     
/*  77 */     int year = c.get(1);
/*  78 */     if (year < 1980) {
/*  79 */       System.arraycopy(DOS_TIME_MIN, 0, buf, offset, DOS_TIME_MIN.length);
/*     */       return;
/*     */     } 
/*  82 */     int month = c.get(2) + 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     long value = (year - 1980 << 25 | month << 21 | c.get(5) << 16 | c.get(11) << 11 | c.get(12) << 5 | c.get(13) >> 1);
/*  89 */     ZipLong.putLong(value, buf, offset);
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
/*     */   public static long adjustToLong(int i) {
/* 102 */     if (i < 0) {
/* 103 */       return 4294967296L + i;
/*     */     }
/* 105 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Date fromDosTime(ZipLong zipDosTime) {
/* 116 */     long dosTime = zipDosTime.getValue();
/* 117 */     return new Date(dosToJavaTime(dosTime));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long dosToJavaTime(long dosTime) {
/* 128 */     Calendar cal = Calendar.getInstance();
/*     */     
/* 130 */     cal.set(1, (int)(dosTime >> 25L & 0x7FL) + 1980);
/* 131 */     cal.set(2, (int)(dosTime >> 21L & 0xFL) - 1);
/* 132 */     cal.set(5, (int)(dosTime >> 16L) & 0x1F);
/* 133 */     cal.set(11, (int)(dosTime >> 11L) & 0x1F);
/* 134 */     cal.set(12, (int)(dosTime >> 5L) & 0x3F);
/* 135 */     cal.set(13, (int)(dosTime << 1L) & 0x3E);
/* 136 */     cal.set(14, 0);
/*     */     
/* 138 */     return cal.getTime().getTime();
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
/*     */   static void setNameAndCommentFromExtraFields(ZipEntry ze, byte[] originalNameBytes, byte[] commentBytes) {
/* 154 */     UnicodePathExtraField name = (UnicodePathExtraField)ze.getExtraField(UnicodePathExtraField.UPATH_ID);
/* 155 */     String originalName = ze.getName();
/* 156 */     String newName = getUnicodeStringIfOriginalMatches(name, originalNameBytes);
/*     */     
/* 158 */     if (newName != null && !originalName.equals(newName)) {
/* 159 */       ze.setName(newName);
/*     */     }
/*     */     
/* 162 */     if (commentBytes != null && commentBytes.length > 0) {
/*     */       
/* 164 */       UnicodeCommentExtraField cmt = (UnicodeCommentExtraField)ze.getExtraField(UnicodeCommentExtraField.UCOM_ID);
/*     */       
/* 166 */       String newComment = getUnicodeStringIfOriginalMatches(cmt, commentBytes);
/* 167 */       if (newComment != null) {
/* 168 */         ze.setComment(newComment);
/*     */       }
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
/*     */   private static String getUnicodeStringIfOriginalMatches(AbstractUnicodeExtraField f, byte[] orig) {
/* 186 */     if (f != null) {
/* 187 */       CRC32 crc32 = new CRC32();
/* 188 */       crc32.update(orig);
/* 189 */       long origCRC32 = crc32.getValue();
/*     */       
/* 191 */       if (origCRC32 == f.getNameCRC32()) {
/*     */         try {
/* 193 */           return ZipEncodingHelper.UTF8_ZIP_ENCODING
/* 194 */             .decode(f.getUnicodeName());
/* 195 */         } catch (IOException ex) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 200 */           return null;
/*     */         } 
/*     */       }
/*     */     } 
/* 204 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static byte[] copy(byte[] from) {
/* 215 */     if (from != null) {
/* 216 */       byte[] to = new byte[from.length];
/* 217 */       System.arraycopy(from, 0, to, 0, to.length);
/* 218 */       return to;
/*     */     } 
/* 220 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean canHandleEntryData(ZipEntry entry) {
/* 229 */     return (supportsEncryptionOf(entry) && supportsMethodOf(entry));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean supportsEncryptionOf(ZipEntry entry) {
/* 239 */     return !entry.getGeneralPurposeBit().usesEncryption();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean supportsMethodOf(ZipEntry entry) {
/* 249 */     return (entry.getMethod() == 0 || entry
/* 250 */       .getMethod() == 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void checkRequestedFeatures(ZipEntry ze) throws UnsupportedZipFeatureException {
/* 259 */     if (!supportsEncryptionOf(ze)) {
/* 260 */       throw new UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature.ENCRYPTION, ze);
/*     */     }
/*     */ 
/*     */     
/* 264 */     if (!supportsMethodOf(ze))
/* 265 */       throw new UnsupportedZipFeatureException(UnsupportedZipFeatureException.Feature.METHOD, ze); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/ZipUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */