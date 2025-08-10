/*     */ package org.zeroturnaround.zip;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.List;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipException;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ import org.zeroturnaround.zip.commons.IOUtils;
/*     */ import org.zeroturnaround.zip.extra.AsiExtraField;
/*     */ import org.zeroturnaround.zip.extra.ExtraFieldUtils;
/*     */ import org.zeroturnaround.zip.extra.ZipExtraField;
/*     */ import org.zeroturnaround.zip.timestamps.TimestampStrategyFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ZipEntryUtil
/*     */ {
/*     */   static ZipEntry copy(ZipEntry original) {
/*  50 */     return copy(original, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ZipEntry copy(ZipEntry original, String newName) {
/*  61 */     ZipEntry copy = new ZipEntry((newName == null) ? original.getName() : newName);
/*  62 */     if (original.getCrc() != -1L) {
/*  63 */       copy.setCrc(original.getCrc());
/*     */     }
/*  65 */     if (original.getMethod() != -1) {
/*  66 */       copy.setMethod(original.getMethod());
/*     */     }
/*  68 */     if (original.getSize() >= 0L) {
/*  69 */       copy.setSize(original.getSize());
/*     */     }
/*  71 */     if (original.getExtra() != null) {
/*  72 */       copy.setExtra(original.getExtra());
/*     */     }
/*     */     
/*  75 */     copy.setComment(original.getComment());
/*  76 */     copy.setTime(original.getTime());
/*  77 */     return copy;
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
/*     */   static void copyEntry(ZipEntry zipEntry, InputStream in, ZipOutputStream out) throws IOException {
/*  91 */     copyEntry(zipEntry, in, out, true);
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
/*     */   static void copyEntry(ZipEntry originalEntry, InputStream in, ZipOutputStream out, boolean preserveTimestamps) throws IOException {
/* 106 */     ZipEntry copy = copy(originalEntry);
/*     */     
/* 108 */     if (preserveTimestamps) {
/* 109 */       TimestampStrategyFactory.getInstance().setTime(copy, originalEntry);
/*     */     } else {
/*     */       
/* 112 */       copy.setTime(System.currentTimeMillis());
/*     */     } 
/*     */     
/* 115 */     addEntry(copy, new BufferedInputStream(in), out);
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
/*     */   static void addEntry(ZipEntry zipEntry, InputStream in, ZipOutputStream out) throws IOException {
/* 129 */     out.putNextEntry(zipEntry);
/* 130 */     if (in != null) {
/* 131 */       IOUtils.copy(in, out);
/*     */     }
/* 133 */     out.closeEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ZipEntry fromFile(String name, File file) {
/* 144 */     ZipEntry zipEntry = new ZipEntry(name);
/* 145 */     if (!file.isDirectory()) {
/* 146 */       zipEntry.setSize(file.length());
/*     */     }
/* 148 */     zipEntry.setTime(file.lastModified());
/*     */     
/* 150 */     ZTFilePermissions permissions = ZTFilePermissionsUtil.getDefaultStategy().getPermissions(file);
/* 151 */     if (permissions != null) {
/* 152 */       setZTFilePermissions(zipEntry, permissions);
/*     */     }
/* 154 */     return zipEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean setZTFilePermissions(ZipEntry zipEntry, ZTFilePermissions permissions) {
/*     */     try {
/* 166 */       List<ZipExtraField> fields = ExtraFieldUtils.parse(zipEntry.getExtra());
/* 167 */       AsiExtraField asiExtraField = getFirstAsiExtraField(fields);
/* 168 */       if (asiExtraField == null) {
/* 169 */         asiExtraField = new AsiExtraField();
/* 170 */         fields.add(asiExtraField);
/*     */       } 
/*     */       
/* 173 */       asiExtraField.setDirectory(zipEntry.isDirectory());
/* 174 */       asiExtraField.setMode(ZTFilePermissionsUtil.toPosixFileMode(permissions));
/* 175 */       zipEntry.setExtra(ExtraFieldUtils.mergeLocalFileDataData(fields));
/* 176 */       return true;
/*     */     }
/* 178 */     catch (ZipException ze) {
/* 179 */       return false;
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
/*     */   static ZTFilePermissions getZTFilePermissions(ZipEntry zipEntry) {
/*     */     try {
/* 192 */       ZTFilePermissions permissions = null;
/* 193 */       List<ZipExtraField> fields = ExtraFieldUtils.parse(zipEntry.getExtra());
/* 194 */       AsiExtraField asiExtraField = getFirstAsiExtraField(fields);
/* 195 */       if (asiExtraField != null) {
/* 196 */         int mode = asiExtraField.getMode() & 0x1FF;
/* 197 */         permissions = ZTFilePermissionsUtil.fromPosixFileMode(mode);
/*     */       } 
/* 199 */       return permissions;
/*     */     }
/* 201 */     catch (ZipException ze) {
/* 202 */       throw new ZipException(ze);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static AsiExtraField getFirstAsiExtraField(List<ZipExtraField> fields) {
/* 207 */     AsiExtraField asiExtraField = null;
/* 208 */     for (ZipExtraField field : fields) {
/* 209 */       if (field instanceof AsiExtraField) {
/* 210 */         asiExtraField = (AsiExtraField)field;
/*     */       }
/*     */     } 
/* 213 */     return asiExtraField;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/ZipEntryUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */