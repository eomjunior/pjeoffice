/*     */ package com.itextpdf.text.pdf.codec;
/*     */ 
/*     */ import com.itextpdf.text.DocWriter;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.zip.DeflaterOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PngWriter
/*     */ {
/*  60 */   private static final byte[] PNG_SIGNTURE = new byte[] { -119, 80, 78, 71, 13, 10, 26, 10 };
/*     */   
/*  62 */   private static final byte[] IHDR = DocWriter.getISOBytes("IHDR");
/*  63 */   private static final byte[] PLTE = DocWriter.getISOBytes("PLTE");
/*  64 */   private static final byte[] IDAT = DocWriter.getISOBytes("IDAT");
/*  65 */   private static final byte[] IEND = DocWriter.getISOBytes("IEND");
/*  66 */   private static final byte[] iCCP = DocWriter.getISOBytes("iCCP");
/*     */   
/*     */   private static int[] crc_table;
/*     */   
/*     */   private OutputStream outp;
/*     */   
/*     */   public PngWriter(OutputStream outp) throws IOException {
/*  73 */     this.outp = outp;
/*  74 */     outp.write(PNG_SIGNTURE);
/*     */   }
/*     */   
/*     */   public void writeHeader(int width, int height, int bitDepth, int colorType) throws IOException {
/*  78 */     ByteArrayOutputStream ms = new ByteArrayOutputStream();
/*  79 */     outputInt(width, ms);
/*  80 */     outputInt(height, ms);
/*  81 */     ms.write(bitDepth);
/*  82 */     ms.write(colorType);
/*  83 */     ms.write(0);
/*  84 */     ms.write(0);
/*  85 */     ms.write(0);
/*  86 */     writeChunk(IHDR, ms.toByteArray());
/*     */   }
/*     */   
/*     */   public void writeEnd() throws IOException {
/*  90 */     writeChunk(IEND, new byte[0]);
/*     */   }
/*     */   
/*     */   public void writeData(byte[] data, int stride) throws IOException {
/*  94 */     ByteArrayOutputStream stream = new ByteArrayOutputStream();
/*  95 */     DeflaterOutputStream zip = new DeflaterOutputStream(stream);
/*     */     int k;
/*  97 */     for (k = 0; k < data.length - stride; k += stride) {
/*  98 */       zip.write(0);
/*  99 */       zip.write(data, k, stride);
/*     */     } 
/* 101 */     int remaining = data.length - k;
/* 102 */     if (remaining > 0) {
/* 103 */       zip.write(0);
/* 104 */       zip.write(data, k, remaining);
/*     */     } 
/* 106 */     zip.close();
/* 107 */     writeChunk(IDAT, stream.toByteArray());
/*     */   }
/*     */   
/*     */   public void writePalette(byte[] data) throws IOException {
/* 111 */     writeChunk(PLTE, data);
/*     */   }
/*     */   
/*     */   public void writeIccProfile(byte[] data) throws IOException {
/* 115 */     ByteArrayOutputStream stream = new ByteArrayOutputStream();
/* 116 */     stream.write(73);
/* 117 */     stream.write(67);
/* 118 */     stream.write(67);
/* 119 */     stream.write(0);
/* 120 */     stream.write(0);
/* 121 */     DeflaterOutputStream zip = new DeflaterOutputStream(stream);
/* 122 */     zip.write(data);
/* 123 */     zip.close();
/* 124 */     writeChunk(iCCP, stream.toByteArray());
/*     */   }
/*     */   
/*     */   private static void make_crc_table() {
/* 128 */     if (crc_table != null)
/*     */       return; 
/* 130 */     int[] crc2 = new int[256];
/* 131 */     for (int n = 0; n < 256; n++) {
/* 132 */       int c = n;
/* 133 */       for (int k = 0; k < 8; k++) {
/* 134 */         if ((c & 0x1) != 0) {
/* 135 */           c = 0xEDB88320 ^ c >>> 1;
/*     */         } else {
/* 137 */           c >>>= 1;
/*     */         } 
/* 139 */       }  crc2[n] = c;
/*     */     } 
/* 141 */     crc_table = crc2;
/*     */   }
/*     */   
/*     */   private static int update_crc(int crc, byte[] buf, int offset, int len) {
/* 145 */     int c = crc;
/*     */     
/* 147 */     if (crc_table == null)
/* 148 */       make_crc_table(); 
/* 149 */     for (int n = 0; n < len; n++) {
/* 150 */       c = crc_table[(c ^ buf[n + offset]) & 0xFF] ^ c >>> 8;
/*     */     }
/* 152 */     return c;
/*     */   }
/*     */   
/*     */   private static int crc(byte[] buf, int offset, int len) {
/* 156 */     return update_crc(-1, buf, offset, len) ^ 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   private static int crc(byte[] buf) {
/* 160 */     return update_crc(-1, buf, 0, buf.length) ^ 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public void outputInt(int n) throws IOException {
/* 164 */     outputInt(n, this.outp);
/*     */   }
/*     */   
/*     */   public static void outputInt(int n, OutputStream s) throws IOException {
/* 168 */     s.write((byte)(n >> 24));
/* 169 */     s.write((byte)(n >> 16));
/* 170 */     s.write((byte)(n >> 8));
/* 171 */     s.write((byte)n);
/*     */   }
/*     */   
/*     */   public void writeChunk(byte[] chunkType, byte[] data) throws IOException {
/* 175 */     outputInt(data.length);
/* 176 */     this.outp.write(chunkType, 0, 4);
/* 177 */     this.outp.write(data);
/* 178 */     int c = update_crc(-1, chunkType, 0, chunkType.length);
/* 179 */     c = update_crc(c, data, 0, data.length) ^ 0xFFFFFFFF;
/* 180 */     outputInt(c);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/PngWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */