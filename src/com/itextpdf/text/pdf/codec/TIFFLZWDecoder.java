/*     */ package com.itextpdf.text.pdf.codec;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TIFFLZWDecoder
/*     */ {
/*     */   byte[][] stringTable;
/*  57 */   byte[] data = null; byte[] uncompData; int tableIndex;
/*  58 */   int bitsToGet = 9;
/*     */   
/*     */   int bytePointer;
/*     */   int bitPointer;
/*     */   int dstIndex;
/*  63 */   int nextData = 0; int w; int h; int predictor; int samplesPerPixel;
/*  64 */   int nextBits = 0;
/*     */   
/*  66 */   int[] andTable = new int[] { 511, 1023, 2047, 4095 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TIFFLZWDecoder(int w, int predictor, int samplesPerPixel) {
/*  74 */     this.w = w;
/*  75 */     this.predictor = predictor;
/*  76 */     this.samplesPerPixel = samplesPerPixel;
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
/*     */   public byte[] decode(byte[] data, byte[] uncompData, int h) {
/*  88 */     if (data[0] == 0 && data[1] == 1) {
/*  89 */       throw new UnsupportedOperationException(MessageLocalization.getComposedMessage("tiff.5.0.style.lzw.codes.are.not.supported", new Object[0]));
/*     */     }
/*     */     
/*  92 */     initializeStringTable();
/*     */     
/*  94 */     this.data = data;
/*  95 */     this.h = h;
/*  96 */     this.uncompData = uncompData;
/*     */ 
/*     */     
/*  99 */     this.bytePointer = 0;
/* 100 */     this.bitPointer = 0;
/* 101 */     this.dstIndex = 0;
/*     */ 
/*     */     
/* 104 */     this.nextData = 0;
/* 105 */     this.nextBits = 0;
/*     */     
/* 107 */     int oldCode = 0;
/*     */     
/*     */     int code;
/* 110 */     while ((code = getNextCode()) != 257 && this.dstIndex < uncompData.length) {
/*     */ 
/*     */       
/* 113 */       if (code == 256) {
/*     */         
/* 115 */         initializeStringTable();
/* 116 */         code = getNextCode();
/*     */         
/* 118 */         if (code == 257) {
/*     */           break;
/*     */         }
/*     */         
/* 122 */         writeString(this.stringTable[code]);
/* 123 */         oldCode = code;
/*     */         
/*     */         continue;
/*     */       } 
/* 127 */       if (code < this.tableIndex) {
/*     */         
/* 129 */         byte[] arrayOfByte = this.stringTable[code];
/*     */         
/* 131 */         writeString(arrayOfByte);
/* 132 */         addStringToTable(this.stringTable[oldCode], arrayOfByte[0]);
/* 133 */         oldCode = code;
/*     */         
/*     */         continue;
/*     */       } 
/* 137 */       byte[] string = this.stringTable[oldCode];
/* 138 */       string = composeString(string, string[0]);
/* 139 */       writeString(string);
/* 140 */       addStringToTable(string);
/* 141 */       oldCode = code;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     if (this.predictor == 2)
/*     */     {
/*     */       
/* 152 */       for (int j = 0; j < h; j++) {
/*     */         
/* 154 */         int count = this.samplesPerPixel * (j * this.w + 1);
/*     */         
/* 156 */         for (int i = this.samplesPerPixel; i < this.w * this.samplesPerPixel; i++) {
/*     */           
/* 158 */           uncompData[count] = (byte)(uncompData[count] + uncompData[count - this.samplesPerPixel]);
/* 159 */           count++;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 164 */     return uncompData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initializeStringTable() {
/* 173 */     this.stringTable = new byte[4096][];
/*     */     
/* 175 */     for (int i = 0; i < 256; i++) {
/* 176 */       this.stringTable[i] = new byte[1];
/* 177 */       this.stringTable[i][0] = (byte)i;
/*     */     } 
/*     */     
/* 180 */     this.tableIndex = 258;
/* 181 */     this.bitsToGet = 9;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeString(byte[] string) {
/* 189 */     int max = this.uncompData.length - this.dstIndex;
/* 190 */     if (string.length < max)
/* 191 */       max = string.length; 
/* 192 */     System.arraycopy(string, 0, this.uncompData, this.dstIndex, max);
/* 193 */     this.dstIndex += max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addStringToTable(byte[] oldString, byte newString) {
/* 200 */     int length = oldString.length;
/* 201 */     byte[] string = new byte[length + 1];
/* 202 */     System.arraycopy(oldString, 0, string, 0, length);
/* 203 */     string[length] = newString;
/*     */ 
/*     */     
/* 206 */     this.stringTable[this.tableIndex++] = string;
/*     */     
/* 208 */     if (this.tableIndex == 511) {
/* 209 */       this.bitsToGet = 10;
/* 210 */     } else if (this.tableIndex == 1023) {
/* 211 */       this.bitsToGet = 11;
/* 212 */     } else if (this.tableIndex == 2047) {
/* 213 */       this.bitsToGet = 12;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addStringToTable(byte[] string) {
/* 223 */     this.stringTable[this.tableIndex++] = string;
/*     */     
/* 225 */     if (this.tableIndex == 511) {
/* 226 */       this.bitsToGet = 10;
/* 227 */     } else if (this.tableIndex == 1023) {
/* 228 */       this.bitsToGet = 11;
/* 229 */     } else if (this.tableIndex == 2047) {
/* 230 */       this.bitsToGet = 12;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] composeString(byte[] oldString, byte newString) {
/* 238 */     int length = oldString.length;
/* 239 */     byte[] string = new byte[length + 1];
/* 240 */     System.arraycopy(oldString, 0, string, 0, length);
/* 241 */     string[length] = newString;
/*     */     
/* 243 */     return string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNextCode() {
/*     */     try {
/* 253 */       this.nextData = this.nextData << 8 | this.data[this.bytePointer++] & 0xFF;
/* 254 */       this.nextBits += 8;
/*     */       
/* 256 */       if (this.nextBits < this.bitsToGet) {
/* 257 */         this.nextData = this.nextData << 8 | this.data[this.bytePointer++] & 0xFF;
/* 258 */         this.nextBits += 8;
/*     */       } 
/*     */       
/* 261 */       int code = this.nextData >> this.nextBits - this.bitsToGet & this.andTable[this.bitsToGet - 9];
/*     */       
/* 263 */       this.nextBits -= this.bitsToGet;
/*     */       
/* 265 */       return code;
/* 266 */     } catch (ArrayIndexOutOfBoundsException e) {
/*     */       
/* 268 */       return 257;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/TIFFLZWDecoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */