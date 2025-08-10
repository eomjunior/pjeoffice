/*     */ package com.itextpdf.text.pdf.codec;
/*     */ 
/*     */ import java.io.PrintStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LZWStringTable
/*     */ {
/*  94 */   byte[] strChr_ = new byte[4096];
/*  95 */   short[] strNxt_ = new short[4096];
/*  96 */   int[] strLen_ = new int[4096];
/*  97 */   short[] strHsh_ = new short[9973];
/*     */   
/*     */   private static final int RES_CODES = 2;
/*     */   
/*     */   private static final short HASH_FREE = -1;
/*     */   
/*     */   private static final short NEXT_FIRST = -1;
/*     */   private static final int MAXBITS = 12;
/*     */   private static final int MAXSTR = 4096;
/*     */   private static final short HASHSIZE = 9973;
/*     */   private static final short HASHSTEP = 2039;
/*     */   short numStrings_;
/*     */   
/*     */   public int AddCharString(short index, byte b) {
/* 111 */     if (this.numStrings_ >= 4096)
/*     */     {
/* 113 */       return 65535;
/*     */     }
/*     */     
/* 116 */     int hshidx = Hash(index, b);
/* 117 */     while (this.strHsh_[hshidx] != -1) {
/* 118 */       hshidx = (hshidx + 2039) % 9973;
/*     */     }
/* 120 */     this.strHsh_[hshidx] = this.numStrings_;
/* 121 */     this.strChr_[this.numStrings_] = b;
/* 122 */     if (index == -1) {
/*     */       
/* 124 */       this.strNxt_[this.numStrings_] = -1;
/* 125 */       this.strLen_[this.numStrings_] = 1;
/*     */     }
/*     */     else {
/*     */       
/* 129 */       this.strNxt_[this.numStrings_] = index;
/* 130 */       this.strLen_[this.numStrings_] = this.strLen_[index] + 1;
/*     */     } 
/*     */     
/* 133 */     this.numStrings_ = (short)(this.numStrings_ + 1); return this.numStrings_;
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
/*     */   public short FindCharString(short index, byte b) {
/* 146 */     if (index == -1) {
/* 147 */       return (short)(b & 0xFF);
/*     */     }
/* 149 */     int hshidx = Hash(index, b); int nxtidx;
/* 150 */     while ((nxtidx = this.strHsh_[hshidx]) != -1) {
/*     */       
/* 152 */       if (this.strNxt_[nxtidx] == index && this.strChr_[nxtidx] == b)
/* 153 */         return (short)nxtidx; 
/* 154 */       hshidx = (hshidx + 2039) % 9973;
/*     */     } 
/*     */     
/* 157 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void ClearTable(int codesize) {
/* 166 */     this.numStrings_ = 0;
/*     */     
/* 168 */     for (int q = 0; q < 9973; q++) {
/* 169 */       this.strHsh_[q] = -1;
/*     */     }
/* 171 */     int w = (1 << codesize) + 2;
/* 172 */     for (int i = 0; i < w; i++) {
/* 173 */       AddCharString((short)-1, (byte)i);
/*     */     }
/*     */   }
/*     */   
/*     */   public static int Hash(short index, byte lastbyte) {
/* 178 */     return (((short)(lastbyte << 8) ^ index) & 0xFFFF) % 9973;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int expandCode(byte[] buf, int offset, short code, int skipHead) {
/*     */     int expandLen;
/* 203 */     if (offset == -2)
/*     */     {
/* 205 */       if (skipHead == 1) skipHead = 0; 
/*     */     }
/* 207 */     if (code == -1 || skipHead == this.strLen_[code])
/*     */     {
/* 209 */       return 0;
/*     */     }
/*     */     
/* 212 */     int codeLen = this.strLen_[code] - skipHead;
/* 213 */     int bufSpace = buf.length - offset;
/* 214 */     if (bufSpace > codeLen) {
/* 215 */       expandLen = codeLen;
/*     */     } else {
/* 217 */       expandLen = bufSpace;
/*     */     } 
/* 219 */     int skipTail = codeLen - expandLen;
/*     */     
/* 221 */     int idx = offset + expandLen;
/*     */ 
/*     */ 
/*     */     
/* 225 */     while (idx > offset && code != -1) {
/*     */       
/* 227 */       if (--skipTail < 0)
/*     */       {
/* 229 */         buf[--idx] = this.strChr_[code];
/*     */       }
/* 231 */       code = this.strNxt_[code];
/*     */     } 
/*     */     
/* 234 */     if (codeLen > expandLen) {
/* 235 */       return -expandLen;
/*     */     }
/* 237 */     return expandLen;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dump(PrintStream out) {
/* 243 */     for (int i = 258; i < this.numStrings_; i++)
/* 244 */       out.println(" strNxt_[" + i + "] = " + this.strNxt_[i] + " strChr_ " + 
/* 245 */           Integer.toHexString(this.strChr_[i] & 0xFF) + " strLen_ " + 
/* 246 */           Integer.toHexString(this.strLen_[i])); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/LZWStringTable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */