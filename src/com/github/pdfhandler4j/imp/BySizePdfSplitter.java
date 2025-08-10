/*    */ package com.github.pdfhandler4j.imp;
/*    */ 
/*    */ import com.github.filehandler4j.IInputFile;
/*    */ import com.github.pdfhandler4j.IPagesSlice;
/*    */ import com.github.utils4j.ISmartIterator;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.ArrayIterator;
/*    */ import java.util.function.Supplier;
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
/*    */ public class BySizePdfSplitter
/*    */   extends ByVolumePdfSplitter
/*    */ {
/*    */   private final long maxFileSize;
/*    */   
/*    */   public BySizePdfSplitter(long maxFileSize) {
/* 42 */     this.maxFileSize = Args.requirePositive(maxFileSize, "maxFileSize is < 1");
/*    */   }
/*    */ 
/*    */   
/*    */   protected final boolean forceCopy(IInputFile file) {
/* 47 */     return (file.length() <= this.maxFileSize);
/*    */   }
/*    */ 
/*    */   
/*    */   public long combinedIncrement(long currentCombined, Supplier<Long> currentDocumentSize) {
/* 52 */     long size = ((Long)currentDocumentSize.get()).longValue();
/* 53 */     return size = (long)(size + 0.03D * size);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean accept(long finalFileSize, long currentPageNumber, long currentTotalPages, long originalTotalPages) {
/* 58 */     if (finalFileSize > this.maxFileSize && currentTotalPages > 1L) {
/* 59 */       setIterator((ISmartIterator)new ArrayIterator((Object[])new IPagesSlice[] { new DefaultPagesSlice(currentPageNumber, originalTotalPages) }));
/* 60 */       return false;
/*    */     } 
/* 62 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mustSplit(long currentFileSize, IPagesSlice slice, long currentMaxPageSize, long totalPages) {
/* 67 */     long marginOfError = computeMarginOfError(currentMaxPageSize, totalPages);
/* 68 */     return (currentFileSize + marginOfError > this.maxFileSize);
/*    */   }
/*    */   
/*    */   protected long computeMarginOfError(long currentMaxPageSize, long totalPages) {
/* 72 */     long pageSizeAverage = this.maxFileSize / totalPages;
/* 73 */     long twoPagesSize = 2L * pageSizeAverage;
/*    */     
/* 75 */     return twoPagesSize;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/pdfhandler4j/imp/BySizePdfSplitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */