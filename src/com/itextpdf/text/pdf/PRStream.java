/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.Document;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.zip.Deflater;
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
/*     */ public class PRStream
/*     */   extends PdfStream
/*     */ {
/*     */   protected PdfReader reader;
/*     */   protected long offset;
/*     */   protected int length;
/*  62 */   protected int objNum = 0;
/*  63 */   protected int objGen = 0;
/*     */   
/*     */   public PRStream(PRStream stream, PdfDictionary newDic) {
/*  66 */     this.reader = stream.reader;
/*  67 */     this.offset = stream.offset;
/*  68 */     this.length = stream.length;
/*  69 */     this.compressed = stream.compressed;
/*  70 */     this.compressionLevel = stream.compressionLevel;
/*  71 */     this.streamBytes = stream.streamBytes;
/*  72 */     this.bytes = stream.bytes;
/*  73 */     this.objNum = stream.objNum;
/*  74 */     this.objGen = stream.objGen;
/*  75 */     if (newDic != null) {
/*  76 */       putAll(newDic);
/*     */     } else {
/*  78 */       this.hashMap.putAll(stream.hashMap);
/*     */     } 
/*     */   }
/*     */   public PRStream(PRStream stream, PdfDictionary newDic, PdfReader reader) {
/*  82 */     this(stream, newDic);
/*  83 */     this.reader = reader;
/*     */   }
/*     */   
/*     */   public PRStream(PdfReader reader, long offset) {
/*  87 */     this.reader = reader;
/*  88 */     this.offset = offset;
/*     */   }
/*     */   
/*     */   public PRStream(PdfReader reader, byte[] conts) {
/*  92 */     this(reader, conts, -1);
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
/*     */   public PRStream(PdfReader reader, byte[] conts, int compressionLevel) {
/* 104 */     this.reader = reader;
/* 105 */     this.offset = -1L;
/* 106 */     if (Document.compress) {
/*     */       try {
/* 108 */         ByteArrayOutputStream stream = new ByteArrayOutputStream();
/* 109 */         Deflater deflater = new Deflater(compressionLevel);
/* 110 */         DeflaterOutputStream zip = new DeflaterOutputStream(stream, deflater);
/* 111 */         zip.write(conts);
/* 112 */         zip.close();
/* 113 */         deflater.end();
/* 114 */         this.bytes = stream.toByteArray();
/*     */       }
/* 116 */       catch (IOException ioe) {
/* 117 */         throw new ExceptionConverter(ioe);
/*     */       } 
/* 119 */       put(PdfName.FILTER, PdfName.FLATEDECODE);
/*     */     } else {
/*     */       
/* 122 */       this.bytes = conts;
/* 123 */     }  setLength(this.bytes.length);
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
/*     */   public void setData(byte[] data, boolean compress) {
/* 136 */     setData(data, compress, -1);
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
/*     */   public void setData(byte[] data, boolean compress, int compressionLevel) {
/* 150 */     remove(PdfName.FILTER);
/* 151 */     this.offset = -1L;
/* 152 */     if (Document.compress && compress) {
/*     */       try {
/* 154 */         ByteArrayOutputStream stream = new ByteArrayOutputStream();
/* 155 */         Deflater deflater = new Deflater(compressionLevel);
/* 156 */         DeflaterOutputStream zip = new DeflaterOutputStream(stream, deflater);
/* 157 */         zip.write(data);
/* 158 */         zip.close();
/* 159 */         deflater.end();
/* 160 */         this.bytes = stream.toByteArray();
/* 161 */         this.compressionLevel = compressionLevel;
/*     */       }
/* 163 */       catch (IOException ioe) {
/* 164 */         throw new ExceptionConverter(ioe);
/*     */       } 
/* 166 */       put(PdfName.FILTER, PdfName.FLATEDECODE);
/*     */     } else {
/*     */       
/* 169 */       this.bytes = data;
/* 170 */     }  setLength(this.bytes.length);
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
/*     */   public void setDataRaw(byte[] data) {
/* 182 */     this.offset = -1L;
/* 183 */     this.bytes = data;
/* 184 */     setLength(this.bytes.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setData(byte[] data) {
/* 191 */     setData(data, true);
/*     */   }
/*     */   
/*     */   public void setLength(int length) {
/* 195 */     this.length = length;
/* 196 */     put(PdfName.LENGTH, new PdfNumber(length));
/*     */   }
/*     */   
/*     */   public long getOffset() {
/* 200 */     return this.offset;
/*     */   }
/*     */   
/*     */   public int getLength() {
/* 204 */     return this.length;
/*     */   }
/*     */   
/*     */   public PdfReader getReader() {
/* 208 */     return this.reader;
/*     */   }
/*     */   
/*     */   public byte[] getBytes() {
/* 212 */     return this.bytes;
/*     */   }
/*     */   
/*     */   public void setObjNum(int objNum, int objGen) {
/* 216 */     this.objNum = objNum;
/* 217 */     this.objGen = objGen;
/*     */   }
/*     */   
/*     */   int getObjNum() {
/* 221 */     return this.objNum;
/*     */   }
/*     */   
/*     */   int getObjGen() {
/* 225 */     return this.objGen;
/*     */   }
/*     */   
/*     */   public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
/* 229 */     byte[] b = PdfReader.getStreamBytesRaw(this);
/* 230 */     PdfEncryption crypto = null;
/* 231 */     if (writer != null)
/* 232 */       crypto = writer.getEncryption(); 
/* 233 */     PdfObject objLen = get(PdfName.LENGTH);
/* 234 */     int nn = b.length;
/* 235 */     if (crypto != null)
/* 236 */       nn = crypto.calculateStreamSize(nn); 
/* 237 */     put(PdfName.LENGTH, new PdfNumber(nn));
/* 238 */     superToPdf(writer, os);
/* 239 */     put(PdfName.LENGTH, objLen);
/* 240 */     os.write(STARTSTREAM);
/* 241 */     if (this.length > 0) {
/* 242 */       if (crypto != null && !crypto.isEmbeddedFilesOnly())
/* 243 */         b = crypto.encryptByteArray(b); 
/* 244 */       os.write(b);
/*     */     } 
/* 246 */     os.write(ENDSTREAM);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PRStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */