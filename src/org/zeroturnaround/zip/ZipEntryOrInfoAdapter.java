/*    */ package org.zeroturnaround.zip;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.zip.ZipEntry;
/*    */ 
/*    */ class ZipEntryOrInfoAdapter
/*    */   implements ZipEntryCallback, ZipInfoCallback {
/*    */   private final ZipEntryCallback entryCallback;
/*    */   private final ZipInfoCallback infoCallback;
/*    */   
/*    */   public ZipEntryOrInfoAdapter(ZipEntryCallback entryCallback, ZipInfoCallback infoCallback) {
/* 13 */     if ((entryCallback != null && infoCallback != null) || (entryCallback == null && infoCallback == null)) {
/* 14 */       throw new IllegalArgumentException("Only one of ZipEntryCallback and ZipInfoCallback must be specified together");
/*    */     }
/* 16 */     this.entryCallback = entryCallback;
/* 17 */     this.infoCallback = infoCallback;
/*    */   }
/*    */   
/*    */   public void process(ZipEntry zipEntry) throws IOException {
/* 21 */     this.infoCallback.process(zipEntry);
/*    */   }
/*    */   
/*    */   public void process(InputStream in, ZipEntry zipEntry) throws IOException {
/* 25 */     if (this.entryCallback != null) {
/* 26 */       this.entryCallback.process(in, zipEntry);
/*    */     } else {
/*    */       
/* 29 */       process(zipEntry);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/ZipEntryOrInfoAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */