/*    */ package org.zeroturnaround.zip.transform;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.util.zip.ZipEntry;
/*    */ import java.util.zip.ZipOutputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class StreamZipEntryTransformer
/*    */   implements ZipEntryTransformer
/*    */ {
/*    */   protected abstract void transform(ZipEntry paramZipEntry, InputStream paramInputStream, OutputStream paramOutputStream) throws IOException;
/*    */   
/*    */   public void transform(InputStream in, ZipEntry zipEntry, ZipOutputStream out) throws IOException {
/* 37 */     ZipEntry entry = new ZipEntry(zipEntry.getName());
/* 38 */     entry.setTime(System.currentTimeMillis());
/* 39 */     out.putNextEntry(entry);
/* 40 */     transform(zipEntry, in, out);
/* 41 */     out.closeEntry();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/transform/StreamZipEntryTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */