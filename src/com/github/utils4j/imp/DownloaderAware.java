/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import com.github.utils4j.IDownloader;
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
/*    */ 
/*    */ public class DownloaderAware
/*    */ {
/*    */   protected final IDownloader downloader;
/*    */   
/*    */   protected DownloaderAware(IDownloader downloader) {
/* 36 */     this.downloader = Args.<IDownloader>requireNonNull(downloader, "downloader is null");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/DownloaderAware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */