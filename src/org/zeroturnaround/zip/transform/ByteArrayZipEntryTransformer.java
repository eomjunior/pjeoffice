/*    */ package org.zeroturnaround.zip.transform;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.zip.ZipEntry;
/*    */ import java.util.zip.ZipOutputStream;
/*    */ import org.zeroturnaround.zip.ByteSource;
/*    */ import org.zeroturnaround.zip.ZipEntrySource;
/*    */ import org.zeroturnaround.zip.commons.IOUtils;
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
/*    */ public abstract class ByteArrayZipEntryTransformer
/*    */   implements ZipEntryTransformer
/*    */ {
/*    */   protected abstract byte[] transform(ZipEntry paramZipEntry, byte[] paramArrayOfbyte) throws IOException;
/*    */   
/*    */   public void transform(InputStream in, ZipEntry zipEntry, ZipOutputStream out) throws IOException {
/*    */     ByteSource source;
/* 40 */     byte[] bytes = IOUtils.toByteArray(in);
/* 41 */     bytes = transform(zipEntry, bytes);
/*    */ 
/*    */ 
/*    */     
/* 45 */     if (preserveTimestamps()) {
/* 46 */       source = new ByteSource(zipEntry.getName(), bytes, zipEntry.getTime());
/*    */     } else {
/*    */       
/* 49 */       source = new ByteSource(zipEntry.getName(), bytes);
/*    */     } 
/*    */     
/* 52 */     ZipEntrySourceZipEntryTransformer.addEntry((ZipEntrySource)source, out);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean preserveTimestamps() {
/* 61 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/transform/ByteArrayZipEntryTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */