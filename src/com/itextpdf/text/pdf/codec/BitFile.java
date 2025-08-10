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
/*     */ public class BitFile
/*     */ {
/*     */   OutputStream output_;
/*     */   byte[] buffer_;
/*     */   int index_;
/*     */   int bitsLeft_;
/*     */   boolean blocks_ = false;
/*     */   
/*     */   public BitFile(OutputStream output, boolean blocks) {
/*  77 */     this.output_ = output;
/*  78 */     this.blocks_ = blocks;
/*  79 */     this.buffer_ = new byte[256];
/*  80 */     this.index_ = 0;
/*  81 */     this.bitsLeft_ = 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/*  86 */     int numBytes = this.index_ + ((this.bitsLeft_ == 8) ? 0 : 1);
/*  87 */     if (numBytes > 0) {
/*     */       
/*  89 */       if (this.blocks_)
/*  90 */         this.output_.write(numBytes); 
/*  91 */       this.output_.write(this.buffer_, 0, numBytes);
/*  92 */       this.buffer_[0] = 0;
/*  93 */       this.index_ = 0;
/*  94 */       this.bitsLeft_ = 8;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeBits(int bits, int numbits) throws IOException {
/* 100 */     int bitsWritten = 0;
/* 101 */     int numBytes = 255;
/*     */ 
/*     */     
/*     */     do {
/* 105 */       if ((this.index_ == 254 && this.bitsLeft_ == 0) || this.index_ > 254) {
/*     */         
/* 107 */         if (this.blocks_) {
/* 108 */           this.output_.write(numBytes);
/*     */         }
/* 110 */         this.output_.write(this.buffer_, 0, numBytes);
/*     */         
/* 112 */         this.buffer_[0] = 0;
/* 113 */         this.index_ = 0;
/* 114 */         this.bitsLeft_ = 8;
/*     */       } 
/*     */       
/* 117 */       if (numbits <= this.bitsLeft_)
/*     */       {
/* 119 */         if (this.blocks_)
/*     */         {
/* 121 */           this.buffer_[this.index_] = (byte)(this.buffer_[this.index_] | (bits & (1 << numbits) - 1) << 8 - this.bitsLeft_);
/* 122 */           bitsWritten += numbits;
/* 123 */           this.bitsLeft_ -= numbits;
/* 124 */           numbits = 0;
/*     */         }
/*     */         else
/*     */         {
/* 128 */           this.buffer_[this.index_] = (byte)(this.buffer_[this.index_] | (bits & (1 << numbits) - 1) << this.bitsLeft_ - numbits);
/* 129 */           bitsWritten += numbits;
/* 130 */           this.bitsLeft_ -= numbits;
/* 131 */           numbits = 0;
/*     */         
/*     */         }
/*     */ 
/*     */       
/*     */       }
/* 137 */       else if (this.blocks_)
/*     */       {
/*     */ 
/*     */         
/* 141 */         this.buffer_[this.index_] = (byte)(this.buffer_[this.index_] | (bits & (1 << this.bitsLeft_) - 1) << 8 - this.bitsLeft_);
/* 142 */         bitsWritten += this.bitsLeft_;
/* 143 */         bits >>= this.bitsLeft_;
/* 144 */         numbits -= this.bitsLeft_;
/* 145 */         this.buffer_[++this.index_] = 0;
/* 146 */         this.bitsLeft_ = 8;
/*     */ 
/*     */       
/*     */       }
/*     */       else
/*     */       {
/*     */         
/* 153 */         int topbits = bits >>> numbits - this.bitsLeft_ & (1 << this.bitsLeft_) - 1;
/* 154 */         this.buffer_[this.index_] = (byte)(this.buffer_[this.index_] | topbits);
/* 155 */         numbits -= this.bitsLeft_;
/* 156 */         bitsWritten += this.bitsLeft_;
/* 157 */         this.buffer_[++this.index_] = 0;
/* 158 */         this.bitsLeft_ = 8;
/*     */       }
/*     */     
/*     */     }
/* 162 */     while (numbits != 0);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/BitFile.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */