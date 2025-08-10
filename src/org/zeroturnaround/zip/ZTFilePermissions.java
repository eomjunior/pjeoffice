/*     */ package org.zeroturnaround.zip;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ZTFilePermissions
/*     */ {
/*     */   private boolean isDirectory;
/*     */   private boolean ownerCanRead;
/*     */   private boolean ownerCanWrite;
/*     */   private boolean ownerCanExecute;
/*     */   private boolean groupCanRead;
/*     */   private boolean groupCanWrite;
/*     */   private boolean groupCanExecute;
/*     */   private boolean othersCanRead;
/*     */   private boolean othersCanWrite;
/*     */   private boolean othersCanExecute;
/*     */   
/*     */   boolean isDirectory() {
/*  24 */     return this.isDirectory;
/*     */   }
/*     */   
/*     */   void setDirectory(boolean isDirectory) {
/*  28 */     this.isDirectory = isDirectory;
/*     */   }
/*     */   
/*     */   boolean isOwnerCanRead() {
/*  32 */     return this.ownerCanRead;
/*     */   }
/*     */   
/*     */   void setOwnerCanRead(boolean ownerCanRead) {
/*  36 */     this.ownerCanRead = ownerCanRead;
/*     */   }
/*     */   
/*     */   boolean isOwnerCanWrite() {
/*  40 */     return this.ownerCanWrite;
/*     */   }
/*     */   
/*     */   void setOwnerCanWrite(boolean ownerCanWrite) {
/*  44 */     this.ownerCanWrite = ownerCanWrite;
/*     */   }
/*     */   
/*     */   boolean isOwnerCanExecute() {
/*  48 */     return this.ownerCanExecute;
/*     */   }
/*     */   
/*     */   void setOwnerCanExecute(boolean ownerCanExecute) {
/*  52 */     this.ownerCanExecute = ownerCanExecute;
/*     */   }
/*     */   
/*     */   boolean isGroupCanRead() {
/*  56 */     return this.groupCanRead;
/*     */   }
/*     */   
/*     */   void setGroupCanRead(boolean groupCanRead) {
/*  60 */     this.groupCanRead = groupCanRead;
/*     */   }
/*     */   
/*     */   boolean isGroupCanWrite() {
/*  64 */     return this.groupCanWrite;
/*     */   }
/*     */   
/*     */   void setGroupCanWrite(boolean groupCanWrite) {
/*  68 */     this.groupCanWrite = groupCanWrite;
/*     */   }
/*     */   
/*     */   boolean isGroupCanExecute() {
/*  72 */     return this.groupCanExecute;
/*     */   }
/*     */   
/*     */   void setGroupCanExecute(boolean groupCanExecute) {
/*  76 */     this.groupCanExecute = groupCanExecute;
/*     */   }
/*     */   
/*     */   boolean isOthersCanRead() {
/*  80 */     return this.othersCanRead;
/*     */   }
/*     */   
/*     */   void setOthersCanRead(boolean othersCanRead) {
/*  84 */     this.othersCanRead = othersCanRead;
/*     */   }
/*     */   
/*     */   boolean isOthersCanWrite() {
/*  88 */     return this.othersCanWrite;
/*     */   }
/*     */   
/*     */   void setOthersCanWrite(boolean othersCanWrite) {
/*  92 */     this.othersCanWrite = othersCanWrite;
/*     */   }
/*     */   
/*     */   boolean isOthersCanExecute() {
/*  96 */     return this.othersCanExecute;
/*     */   }
/*     */   
/*     */   void setOthersCanExecute(boolean othersCanExecute) {
/* 100 */     this.othersCanExecute = othersCanExecute;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/zeroturnaround/zip/ZTFilePermissions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */