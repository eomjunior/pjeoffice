/*    */ package com.itextpdf.text.pdf;
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
/*    */ public class MemoryLimitsAwareException
/*    */   extends RuntimeException
/*    */ {
/*    */   public static final String DuringDecompressionMultipleStreamsInSumOccupiedMoreMemoryThanAllowed = "During decompression multiple streams in sum occupied more memory than allowed. Please either check your pdf or increase the allowed single decompressed pdf stream maximum size value by setting the appropriate parameter of ReaderProperties's MemoryLimitsAwareHandler.";
/*    */   public static final String DuringDecompressionSingleStreamOccupiedMoreMemoryThanAllowed = "During decompression a single stream occupied more memory than allowed. Please either check your pdf or increase the allowed multiple decompressed pdf streams maximum size value by setting the appropriate parameter of ReaderProperties's MemoryLimitsAwareHandler.";
/*    */   public static final String DuringDecompressionSingleStreamOccupiedMoreThanMaxIntegerValue = "During decompression a single stream occupied more than a maximum integer value. Please check your pdf.";
/*    */   public static final String UnknownPdfException = "Unknown PdfException.";
/*    */   
/*    */   public MemoryLimitsAwareException(String message) {
/* 60 */     super(message);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/MemoryLimitsAwareException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */