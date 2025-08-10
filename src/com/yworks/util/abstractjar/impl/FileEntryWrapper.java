/*    */ package com.yworks.util.abstractjar.impl;
/*    */ 
/*    */ import com.yworks.util.abstractjar.Entry;
/*    */ import java.io.File;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileEntryWrapper
/*    */   implements Entry
/*    */ {
/*    */   private File file;
/*    */   private String relative;
/*    */   
/*    */   FileEntryWrapper(File file, String relative) {
/* 21 */     this.relative = relative;
/* 22 */     this.file = file;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDirectory() {
/* 27 */     return this.file.isDirectory();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 32 */     return this.relative;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getSize() {
/* 37 */     return this.file.length();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public File getFile() {
/* 46 */     return this.file;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/abstractjar/impl/FileEntryWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */