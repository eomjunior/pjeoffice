/*     */ package com.itextpdf.text.pdf;
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
/*     */ public class PdfString
/*     */   extends PdfObject
/*     */ {
/*  74 */   protected String value = "";
/*     */   
/*  76 */   protected String originalValue = null;
/*     */ 
/*     */   
/*  79 */   protected String encoding = "PDF";
/*     */   
/*  81 */   protected int objNum = 0;
/*     */   
/*  83 */   protected int objGen = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hexWriting = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfString() {
/*  93 */     super(3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfString(String value) {
/* 103 */     super(3);
/* 104 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfString(String value, String encoding) {
/* 115 */     super(3);
/* 116 */     this.value = value;
/* 117 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfString(byte[] bytes) {
/* 126 */     super(3);
/* 127 */     this.value = PdfEncodings.convertToString(bytes, null);
/* 128 */     this.encoding = "";
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
/*     */   public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
/* 141 */     PdfWriter.checkPdfIsoConformance(writer, 11, this);
/* 142 */     byte[] b = getBytes();
/* 143 */     PdfEncryption crypto = null;
/* 144 */     if (writer != null)
/* 145 */       crypto = writer.getEncryption(); 
/* 146 */     if (crypto != null && !crypto.isEmbeddedFilesOnly())
/* 147 */       b = crypto.encryptByteArray(b); 
/* 148 */     if (this.hexWriting) {
/* 149 */       ByteBuffer buf = new ByteBuffer();
/* 150 */       buf.append('<');
/* 151 */       int len = b.length;
/* 152 */       for (int k = 0; k < len; k++)
/* 153 */         buf.appendHex(b[k]); 
/* 154 */       buf.append('>');
/* 155 */       os.write(buf.toByteArray());
/*     */     } else {
/*     */       
/* 158 */       os.write(StringUtils.escapeString(b));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 167 */     return this.value;
/*     */   }
/*     */   
/*     */   public byte[] getBytes() {
/* 171 */     if (this.bytes == null)
/* 172 */       if (this.encoding != null && this.encoding.equals("UnicodeBig") && PdfEncodings.isPdfDocEncoding(this.value)) {
/* 173 */         this.bytes = PdfEncodings.convertToBytes(this.value, "PDF");
/*     */       } else {
/* 175 */         this.bytes = PdfEncodings.convertToBytes(this.value, this.encoding);
/*     */       }  
/* 177 */     return this.bytes;
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
/*     */   public String toUnicodeString() {
/* 189 */     if (this.encoding != null && this.encoding.length() != 0)
/* 190 */       return this.value; 
/* 191 */     getBytes();
/* 192 */     if (this.bytes.length >= 2 && this.bytes[0] == -2 && this.bytes[1] == -1) {
/* 193 */       return PdfEncodings.convertToString(this.bytes, "UnicodeBig");
/*     */     }
/* 195 */     return PdfEncodings.convertToString(this.bytes, "PDF");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 204 */     return this.encoding;
/*     */   }
/*     */   
/*     */   void setObjNum(int objNum, int objGen) {
/* 208 */     this.objNum = objNum;
/* 209 */     this.objGen = objGen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void decrypt(PdfReader reader) {
/* 216 */     PdfEncryption decrypt = reader.getDecrypt();
/* 217 */     if (decrypt != null) {
/* 218 */       this.originalValue = this.value;
/* 219 */       decrypt.setHashKey(this.objNum, this.objGen);
/* 220 */       this.bytes = PdfEncodings.convertToBytes(this.value, (String)null);
/* 221 */       this.bytes = decrypt.decryptByteArray(this.bytes);
/* 222 */       this.value = PdfEncodings.convertToString(this.bytes, null);
/*     */     } 
/*     */   }
/*     */   
/*     */   public byte[] getOriginalBytes() {
/* 227 */     if (this.originalValue == null)
/* 228 */       return getBytes(); 
/* 229 */     return PdfEncodings.convertToBytes(this.originalValue, (String)null);
/*     */   }
/*     */   
/*     */   public PdfString setHexWriting(boolean hexWriting) {
/* 233 */     this.hexWriting = hexWriting;
/* 234 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isHexWriting() {
/* 238 */     return this.hexWriting;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfString.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */