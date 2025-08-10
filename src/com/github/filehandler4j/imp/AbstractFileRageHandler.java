/*    */ package com.github.filehandler4j.imp;
/*    */ 
/*    */ import com.github.filehandler4j.IFileInfoEvent;
/*    */ import com.github.filehandler4j.IFileSlice;
/*    */ import com.github.utils4j.ISmartIterator;
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
/*    */ public abstract class AbstractFileRageHandler<T extends IFileInfoEvent, R extends IFileSlice>
/*    */   extends AbstractFileHandler<T>
/*    */ {
/*    */   private ISmartIterator<R> iterator;
/*    */   
/*    */   public AbstractFileRageHandler(ISmartIterator<R> iterator) {
/* 40 */     setIterator(iterator);
/*    */   }
/*    */   
/*    */   protected final R nextSlice() {
/* 44 */     R next = null; IFileSlice iFileSlice;
/* 45 */     while (this.iterator.hasNext() && (iFileSlice = (IFileSlice)this.iterator.next()) == null);
/*    */     
/* 47 */     return (R)iFileSlice;
/*    */   }
/*    */   
/*    */   protected final R previousSlice() {
/* 51 */     R prev = null; IFileSlice iFileSlice;
/* 52 */     while (this.iterator.hasPrevious() && (iFileSlice = (IFileSlice)this.iterator.previous()) == null);
/*    */     
/* 54 */     return (R)iFileSlice;
/*    */   }
/*    */ 
/*    */   
/*    */   public void reset() {
/* 59 */     this.iterator.reset();
/* 60 */     super.reset();
/*    */   }
/*    */   
/*    */   protected void setIterator(ISmartIterator<R> iterator) {
/* 64 */     Args.requireNonNull(iterator, "iterator is null");
/* 65 */     this.iterator = iterator;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/filehandler4j/imp/AbstractFileRageHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */