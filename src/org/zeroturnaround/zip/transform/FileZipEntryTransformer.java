/*    */ package org.zeroturnaround.zip.transform;
/*    */ 
/*    */ import java.io.BufferedOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.util.zip.ZipEntry;
/*    */ import java.util.zip.ZipOutputStream;
/*    */ import org.zeroturnaround.zip.FileSource;
/*    */ import org.zeroturnaround.zip.ZipEntrySource;
/*    */ import org.zeroturnaround.zip.commons.FileUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class FileZipEntryTransformer
/*    */   implements ZipEntryTransformer
/*    */ {
/*    */   protected abstract void transform(ZipEntry paramZipEntry, File paramFile1, File paramFile2) throws IOException;
/*    */   
/*    */   public void transform(InputStream in, ZipEntry zipEntry, ZipOutputStream out) throws IOException {
/* 47 */     File inFile = null;
/* 48 */     File outFile = null;
/*    */     try {
/* 50 */       inFile = File.createTempFile("zip", null);
/* 51 */       outFile = File.createTempFile("zip", null);
/* 52 */       copy(in, inFile);
/* 53 */       transform(zipEntry, inFile, outFile);
/* 54 */       FileSource source = new FileSource(zipEntry.getName(), outFile);
/* 55 */       ZipEntrySourceZipEntryTransformer.addEntry((ZipEntrySource)source, out);
/*    */     } finally {
/*    */       
/* 58 */       FileUtils.deleteQuietly(inFile);
/* 59 */       FileUtils.deleteQuietly(outFile);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void copy(InputStream in, File file) throws IOException {
/* 64 */     OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
/*    */     try {
/* 66 */       IOUtils.copy(in, out);
/*    */     } finally {
/*    */       
/* 69 */       IOUtils.closeQuietly(out);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/transform/FileZipEntryTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */