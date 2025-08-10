/*    */ package org.zeroturnaround.zip.transform;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.zip.ZipEntry;
/*    */ import java.util.zip.ZipOutputStream;
/*    */ import org.zeroturnaround.zip.ZipEntrySource;
/*    */ import org.zeroturnaround.zip.commons.IOUtils;
/*    */ 
/*    */ public class ZipEntrySourceZipEntryTransformer
/*    */   implements ZipEntryTransformer
/*    */ {
/*    */   private final ZipEntrySource source;
/*    */   
/*    */   public ZipEntrySourceZipEntryTransformer(ZipEntrySource source) {
/* 16 */     this.source = source;
/*    */   }
/*    */   
/*    */   public void transform(InputStream in, ZipEntry zipEntry, ZipOutputStream out) throws IOException {
/* 20 */     addEntry(this.source, out);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void addEntry(ZipEntrySource entry, ZipOutputStream out) throws IOException {
/* 32 */     out.putNextEntry(entry.getEntry());
/* 33 */     InputStream in = entry.getInputStream();
/* 34 */     if (in != null) {
/*    */       try {
/* 36 */         IOUtils.copy(in, out);
/*    */       } finally {
/*    */         
/* 39 */         IOUtils.closeQuietly(in);
/*    */       } 
/*    */     }
/* 42 */     out.closeEntry();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/transform/ZipEntrySourceZipEntryTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */