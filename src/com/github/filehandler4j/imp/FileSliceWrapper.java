/*    */ package com.github.filehandler4j.imp;
/*    */ 
/*    */ import com.github.filehandler4j.IFileSlice;
/*    */ import com.github.utils4j.imp.Args;
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
/*    */ public class FileSliceWrapper
/*    */   implements IFileSlice
/*    */ {
/*    */   protected IFileSlice slice;
/*    */   
/*    */   protected FileSliceWrapper(IFileSlice slice) {
/* 38 */     setSlice(slice);
/*    */   }
/*    */ 
/*    */   
/*    */   public long start() {
/* 43 */     return this.slice.start();
/*    */   }
/*    */ 
/*    */   
/*    */   public long end() {
/* 48 */     return this.slice.start();
/*    */   }
/*    */ 
/*    */   
/*    */   protected final <T extends IFileSlice> T getSlice() {
/* 53 */     return (T)this.slice;
/*    */   }
/*    */   
/*    */   protected final void setSlice(IFileSlice slice) {
/* 57 */     this.slice = (IFileSlice)Args.requireNonNull(slice, "slice is null");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/filehandler4j/imp/FileSliceWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */