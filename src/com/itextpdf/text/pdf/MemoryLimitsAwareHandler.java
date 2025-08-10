/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MemoryLimitsAwareHandler
/*     */ {
/*     */   private static final int SINGLE_SCALE_COEFFICIENT = 100;
/*     */   private static final int SUM_SCALE_COEFFICIENT = 500;
/*     */   private static final int SINGLE_DECOMPRESSED_PDF_STREAM_MIN_SIZE = 21474836;
/*     */   private static final long SUM_OF_DECOMPRESSED_PDF_STREAMW_MIN_SIZE = 107374182L;
/*     */   private int maxSizeOfSingleDecompressedPdfStream;
/*     */   private long maxSizeOfDecompressedPdfStreamsSum;
/*  59 */   private long allMemoryUsedForDecompression = 0L;
/*  60 */   private long memoryUsedForCurrentPdfStreamDecompression = 0L;
/*     */ 
/*     */ 
/*     */   
/*     */   boolean considerCurrentPdfStream = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public MemoryLimitsAwareHandler() {
/*  69 */     this.maxSizeOfSingleDecompressedPdfStream = 21474836;
/*  70 */     this.maxSizeOfDecompressedPdfStreamsSum = 107374182L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemoryLimitsAwareHandler(long documentSize) {
/*  80 */     this.maxSizeOfSingleDecompressedPdfStream = (int)calculateDefaultParameter(documentSize, 100, 21474836L);
/*  81 */     this.maxSizeOfDecompressedPdfStreamsSum = calculateDefaultParameter(documentSize, 500, 107374182L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxSizeOfSingleDecompressedPdfStream() {
/*  90 */     return this.maxSizeOfSingleDecompressedPdfStream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemoryLimitsAwareHandler setMaxSizeOfSingleDecompressedPdfStream(int maxSizeOfSingleDecompressedPdfStream) {
/* 104 */     this.maxSizeOfSingleDecompressedPdfStream = maxSizeOfSingleDecompressedPdfStream;
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getMaxSizeOfDecompressedPdfStreamsSum() {
/* 114 */     return this.maxSizeOfDecompressedPdfStreamsSum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MemoryLimitsAwareHandler setMaxSizeOfDecompressedPdfStreamsSum(long maxSizeOfDecompressedPdfStreamsSum) {
/* 129 */     this.maxSizeOfDecompressedPdfStreamsSum = maxSizeOfDecompressedPdfStreamsSum;
/* 130 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MemoryLimitsAwareHandler considerBytesOccupiedByDecompressedPdfStream(long numOfOccupiedBytes) {
/* 142 */     if (this.considerCurrentPdfStream && 
/* 143 */       this.memoryUsedForCurrentPdfStreamDecompression < numOfOccupiedBytes) {
/* 144 */       this.memoryUsedForCurrentPdfStreamDecompression = numOfOccupiedBytes;
/* 145 */       if (this.memoryUsedForCurrentPdfStreamDecompression > this.maxSizeOfSingleDecompressedPdfStream) {
/* 146 */         throw new MemoryLimitsAwareException("During decompression a single stream occupied more memory than allowed. Please either check your pdf or increase the allowed multiple decompressed pdf streams maximum size value by setting the appropriate parameter of ReaderProperties's MemoryLimitsAwareHandler.");
/*     */       }
/*     */     } 
/*     */     
/* 150 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MemoryLimitsAwareHandler beginDecompressedPdfStreamProcessing() {
/* 159 */     ensureCurrentStreamIsReset();
/* 160 */     this.considerCurrentPdfStream = true;
/* 161 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MemoryLimitsAwareHandler endDecompressedPdfStreamProcessing() {
/* 172 */     this.allMemoryUsedForDecompression += this.memoryUsedForCurrentPdfStreamDecompression;
/* 173 */     if (this.allMemoryUsedForDecompression > this.maxSizeOfDecompressedPdfStreamsSum) {
/* 174 */       throw new MemoryLimitsAwareException("During decompression multiple streams in sum occupied more memory than allowed. Please either check your pdf or increase the allowed single decompressed pdf stream maximum size value by setting the appropriate parameter of ReaderProperties's MemoryLimitsAwareHandler.");
/*     */     }
/* 176 */     ensureCurrentStreamIsReset();
/* 177 */     this.considerCurrentPdfStream = false;
/* 178 */     return this;
/*     */   }
/*     */   
/*     */   long getAllMemoryUsedForDecompression() {
/* 182 */     return this.allMemoryUsedForDecompression;
/*     */   }
/*     */   
/*     */   private static long calculateDefaultParameter(long documentSize, int scale, long min) {
/* 186 */     long result = documentSize * scale;
/* 187 */     if (result < min) {
/* 188 */       result = min;
/*     */     }
/* 190 */     if (result > min * scale) {
/* 191 */       result = min * scale;
/*     */     }
/* 193 */     return result;
/*     */   }
/*     */   
/*     */   private void ensureCurrentStreamIsReset() {
/* 197 */     this.memoryUsedForCurrentPdfStreamDecompression = 0L;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/MemoryLimitsAwareHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */