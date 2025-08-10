/*     */ package com.itextpdf.text.pdf.codec;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TiffWriter
/*     */ {
/*  54 */   private TreeMap<Integer, FieldBase> ifd = new TreeMap<Integer, FieldBase>();
/*     */   
/*     */   public void addField(FieldBase field) {
/*  57 */     this.ifd.put(Integer.valueOf(field.getTag()), field);
/*     */   }
/*     */   
/*     */   public int getIfdSize() {
/*  61 */     return 6 + this.ifd.size() * 12;
/*     */   }
/*     */   
/*     */   public void writeFile(OutputStream stream) throws IOException {
/*  65 */     stream.write(77);
/*  66 */     stream.write(77);
/*  67 */     stream.write(0);
/*  68 */     stream.write(42);
/*  69 */     writeLong(8, stream);
/*  70 */     writeShort(this.ifd.size(), stream);
/*  71 */     int offset = 8 + getIfdSize();
/*  72 */     for (FieldBase field : this.ifd.values()) {
/*  73 */       int size = field.getValueSize();
/*  74 */       if (size > 4) {
/*  75 */         field.setOffset(offset);
/*  76 */         offset += size;
/*     */       } 
/*  78 */       field.writeField(stream);
/*     */     } 
/*  80 */     writeLong(0, stream);
/*  81 */     for (FieldBase field : this.ifd.values()) {
/*  82 */       field.writeValue(stream);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static abstract class FieldBase
/*     */   {
/*     */     private int tag;
/*     */     
/*     */     private int fieldType;
/*     */     
/*     */     private int count;
/*     */     protected byte[] data;
/*     */     private int offset;
/*     */     
/*     */     protected FieldBase(int tag, int fieldType, int count) {
/*  98 */       this.tag = tag;
/*  99 */       this.fieldType = fieldType;
/* 100 */       this.count = count;
/*     */     }
/*     */     
/*     */     public int getValueSize() {
/* 104 */       return this.data.length + 1 & 0xFFFFFFFE;
/*     */     }
/*     */     
/*     */     public int getTag() {
/* 108 */       return this.tag;
/*     */     }
/*     */     
/*     */     public void setOffset(int offset) {
/* 112 */       this.offset = offset;
/*     */     }
/*     */     
/*     */     public void writeField(OutputStream stream) throws IOException {
/* 116 */       TiffWriter.writeShort(this.tag, stream);
/* 117 */       TiffWriter.writeShort(this.fieldType, stream);
/* 118 */       TiffWriter.writeLong(this.count, stream);
/* 119 */       if (this.data.length <= 4) {
/* 120 */         stream.write(this.data);
/* 121 */         for (int k = this.data.length; k < 4; k++) {
/* 122 */           stream.write(0);
/*     */         }
/*     */       } else {
/*     */         
/* 126 */         TiffWriter.writeLong(this.offset, stream);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void writeValue(OutputStream stream) throws IOException {
/* 131 */       if (this.data.length <= 4)
/*     */         return; 
/* 133 */       stream.write(this.data);
/* 134 */       if ((this.data.length & 0x1) == 1) {
/* 135 */         stream.write(0);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class FieldShort
/*     */     extends FieldBase
/*     */   {
/*     */     public FieldShort(int tag, int value) {
/* 145 */       super(tag, 3, 1);
/* 146 */       this.data = new byte[2];
/* 147 */       this.data[0] = (byte)(value >> 8);
/* 148 */       this.data[1] = (byte)value;
/*     */     }
/*     */     
/*     */     public FieldShort(int tag, int[] values) {
/* 152 */       super(tag, 3, values.length);
/* 153 */       this.data = new byte[values.length * 2];
/* 154 */       int ptr = 0;
/* 155 */       for (int value : values) {
/* 156 */         this.data[ptr++] = (byte)(value >> 8);
/* 157 */         this.data[ptr++] = (byte)value;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FieldLong
/*     */     extends FieldBase
/*     */   {
/*     */     public FieldLong(int tag, int value) {
/* 168 */       super(tag, 4, 1);
/* 169 */       this.data = new byte[4];
/* 170 */       this.data[0] = (byte)(value >> 24);
/* 171 */       this.data[1] = (byte)(value >> 16);
/* 172 */       this.data[2] = (byte)(value >> 8);
/* 173 */       this.data[3] = (byte)value;
/*     */     }
/*     */     
/*     */     public FieldLong(int tag, int[] values) {
/* 177 */       super(tag, 4, values.length);
/* 178 */       this.data = new byte[values.length * 4];
/* 179 */       int ptr = 0;
/* 180 */       for (int value : values) {
/* 181 */         this.data[ptr++] = (byte)(value >> 24);
/* 182 */         this.data[ptr++] = (byte)(value >> 16);
/* 183 */         this.data[ptr++] = (byte)(value >> 8);
/* 184 */         this.data[ptr++] = (byte)value;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FieldRational
/*     */     extends FieldBase
/*     */   {
/*     */     public FieldRational(int tag, int[] value) {
/* 195 */       this(tag, new int[][] { value });
/*     */     }
/*     */     
/*     */     public FieldRational(int tag, int[][] values) {
/* 199 */       super(tag, 5, values.length);
/* 200 */       this.data = new byte[values.length * 8];
/* 201 */       int ptr = 0;
/* 202 */       for (int[] value : values) {
/* 203 */         this.data[ptr++] = (byte)(value[0] >> 24);
/* 204 */         this.data[ptr++] = (byte)(value[0] >> 16);
/* 205 */         this.data[ptr++] = (byte)(value[0] >> 8);
/* 206 */         this.data[ptr++] = (byte)value[0];
/* 207 */         this.data[ptr++] = (byte)(value[1] >> 24);
/* 208 */         this.data[ptr++] = (byte)(value[1] >> 16);
/* 209 */         this.data[ptr++] = (byte)(value[1] >> 8);
/* 210 */         this.data[ptr++] = (byte)value[1];
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FieldByte
/*     */     extends FieldBase
/*     */   {
/*     */     public FieldByte(int tag, byte[] values) {
/* 221 */       super(tag, 1, values.length);
/* 222 */       this.data = values;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FieldUndefined
/*     */     extends FieldBase
/*     */   {
/*     */     public FieldUndefined(int tag, byte[] values) {
/* 232 */       super(tag, 7, values.length);
/* 233 */       this.data = values;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FieldImage
/*     */     extends FieldBase
/*     */   {
/*     */     public FieldImage(byte[] values) {
/* 243 */       super(273, 4, 1);
/* 244 */       this.data = values;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FieldAscii
/*     */     extends FieldBase
/*     */   {
/*     */     public FieldAscii(int tag, String values) {
/* 254 */       super(tag, 2, (values.getBytes()).length + 1);
/* 255 */       byte[] b = values.getBytes();
/* 256 */       this.data = new byte[b.length + 1];
/* 257 */       System.arraycopy(b, 0, this.data, 0, b.length);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void writeShort(int v, OutputStream stream) throws IOException {
/* 262 */     stream.write(v >> 8 & 0xFF);
/* 263 */     stream.write(v & 0xFF);
/*     */   }
/*     */   
/*     */   public static void writeLong(int v, OutputStream stream) throws IOException {
/* 267 */     stream.write(v >> 24 & 0xFF);
/* 268 */     stream.write(v >> 16 & 0xFF);
/* 269 */     stream.write(v >> 8 & 0xFF);
/* 270 */     stream.write(v & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void compressLZW(OutputStream stream, int predictor, byte[] b, int height, int samplesPerPixel, int stride) throws IOException {
/* 275 */     LZWCompressor lzwCompressor = new LZWCompressor(stream, 8, true);
/* 276 */     boolean usePredictor = (predictor == 2);
/*     */     
/* 278 */     if (!usePredictor) {
/* 279 */       lzwCompressor.compress(b, 0, b.length);
/*     */     } else {
/* 281 */       int off = 0;
/* 282 */       byte[] rowBuf = usePredictor ? new byte[stride] : null;
/* 283 */       for (int i = 0; i < height; i++) {
/* 284 */         System.arraycopy(b, off, rowBuf, 0, stride);
/* 285 */         for (int j = stride - 1; j >= samplesPerPixel; j--) {
/* 286 */           rowBuf[j] = (byte)(rowBuf[j] - rowBuf[j - samplesPerPixel]);
/*     */         }
/* 288 */         lzwCompressor.compress(rowBuf, 0, stride);
/* 289 */         off += stride;
/*     */       } 
/*     */     } 
/*     */     
/* 293 */     lzwCompressor.flush();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/codec/TiffWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */