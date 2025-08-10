/*     */ package com.itextpdf.text.pdf.codec;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ public class LZWCompressor
/*     */ {
/*     */   int codeSize_;
/*     */   int clearCode_;
/*     */   int endOfInfo_;
/*     */   int numBits_;
/*     */   int limit_;
/*     */   short prefix_;
/*     */   BitFile bf_;
/*     */   LZWStringTable lzss_;
/*     */   boolean tiffFudge_;
/*     */   
/*     */   public LZWCompressor(OutputStream out, int codeSize, boolean TIFF) throws IOException {
/*  98 */     this.bf_ = new BitFile(out, !TIFF);
/*  99 */     this.codeSize_ = codeSize;
/* 100 */     this.tiffFudge_ = TIFF;
/* 101 */     this.clearCode_ = 1 << this.codeSize_;
/* 102 */     this.endOfInfo_ = this.clearCode_ + 1;
/* 103 */     this.numBits_ = this.codeSize_ + 1;
/*     */     
/* 105 */     this.limit_ = (1 << this.numBits_) - 1;
/* 106 */     if (this.tiffFudge_) {
/* 107 */       this.limit_--;
/*     */     }
/* 109 */     this.prefix_ = -1;
/* 110 */     this.lzss_ = new LZWStringTable();
/* 111 */     this.lzss_.ClearTable(this.codeSize_);
/* 112 */     this.bf_.writeBits(this.clearCode_, this.numBits_);
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
/*     */   public void compress(byte[] buf, int offset, int length) throws IOException {
/* 126 */     int maxOffset = offset + length;
/* 127 */     for (int idx = offset; idx < maxOffset; idx++) {
/*     */       
/* 129 */       byte c = buf[idx]; short index;
/* 130 */       if ((index = this.lzss_.FindCharString(this.prefix_, c)) != -1) {
/* 131 */         this.prefix_ = index;
/*     */       } else {
/*     */         
/* 134 */         this.bf_.writeBits(this.prefix_, this.numBits_);
/* 135 */         if (this.lzss_.AddCharString(this.prefix_, c) > this.limit_) {
/*     */           
/* 137 */           if (this.numBits_ == 12) {
/*     */             
/* 139 */             this.bf_.writeBits(this.clearCode_, this.numBits_);
/* 140 */             this.lzss_.ClearTable(this.codeSize_);
/* 141 */             this.numBits_ = this.codeSize_ + 1;
/*     */           } else {
/*     */             
/* 144 */             this.numBits_++;
/*     */           } 
/* 146 */           this.limit_ = (1 << this.numBits_) - 1;
/* 147 */           if (this.tiffFudge_)
/* 148 */             this.limit_--; 
/*     */         } 
/* 150 */         this.prefix_ = (short)((short)c & 0xFF);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 163 */     if (this.prefix_ != -1) {
/* 164 */       this.bf_.writeBits(this.prefix_, this.numBits_);
/*     */     }
/* 166 */     this.bf_.writeBits(this.endOfInfo_, this.numBits_);
/* 167 */     this.bf_.flush();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/LZWCompressor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */