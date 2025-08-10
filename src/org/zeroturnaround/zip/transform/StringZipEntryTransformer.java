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
/*    */ public abstract class StringZipEntryTransformer
/*    */   implements ZipEntryTransformer
/*    */ {
/*    */   private final String encoding;
/*    */   
/*    */   public StringZipEntryTransformer() {
/* 19 */     this(null);
/*    */   }
/*    */   
/*    */   public StringZipEntryTransformer(String encoding) {
/* 23 */     this.encoding = encoding;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract String transform(ZipEntry paramZipEntry, String paramString) throws IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void transform(InputStream in, ZipEntry zipEntry, ZipOutputStream out) throws IOException {
/* 41 */     String data = IOUtils.toString(in, this.encoding);
/* 42 */     data = transform(zipEntry, data);
/* 43 */     byte[] bytes = (this.encoding == null) ? data.getBytes() : data.getBytes(this.encoding);
/* 44 */     ByteSource source = new ByteSource(zipEntry.getName(), bytes);
/* 45 */     ZipEntrySourceZipEntryTransformer.addEntry((ZipEntrySource)source, out);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/transform/StringZipEntryTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */