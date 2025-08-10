/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.DEROutputStream;
/*     */ import org.bouncycastle.asn1.DLOutputStream;
/*     */ 
/*     */ public class ASN1OutputStream
/*     */ {
/*     */   public static org.bouncycastle.asn1.ASN1OutputStream create(OutputStream paramOutputStream) {
/*  14 */     return new org.bouncycastle.asn1.ASN1OutputStream(paramOutputStream);
/*     */   }
/*     */   private OutputStream os;
/*     */   
/*     */   public static org.bouncycastle.asn1.ASN1OutputStream create(OutputStream paramOutputStream, String paramString) {
/*  19 */     if (paramString.equals("DER"))
/*     */     {
/*  21 */       return (org.bouncycastle.asn1.ASN1OutputStream)new DEROutputStream(paramOutputStream);
/*     */     }
/*  23 */     if (paramString.equals("DL"))
/*     */     {
/*  25 */       return (org.bouncycastle.asn1.ASN1OutputStream)new DLOutputStream(paramOutputStream);
/*     */     }
/*     */ 
/*     */     
/*  29 */     return new org.bouncycastle.asn1.ASN1OutputStream(paramOutputStream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1OutputStream(OutputStream paramOutputStream) {
/*  40 */     this.os = paramOutputStream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void writeLength(int paramInt) throws IOException {
/*  47 */     if (paramInt > 127) {
/*     */       
/*  49 */       byte b = 1;
/*  50 */       int i = paramInt;
/*     */       
/*  52 */       while ((i >>>= 8) != 0)
/*     */       {
/*  54 */         b++;
/*     */       }
/*     */       
/*  57 */       write((byte)(b | 0x80));
/*     */       
/*  59 */       for (int j = (b - 1) * 8; j >= 0; j -= 8)
/*     */       {
/*  61 */         write((byte)(paramInt >> j));
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/*  66 */       write((byte)paramInt);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final void write(int paramInt) throws IOException {
/*  73 */     this.os.write(paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final void write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException {
/*  79 */     this.os.write(paramArrayOfbyte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final void writeElements(ASN1Encodable[] paramArrayOfASN1Encodable) throws IOException {
/*  85 */     int i = paramArrayOfASN1Encodable.length;
/*  86 */     for (byte b = 0; b < i; b++) {
/*     */       
/*  88 */       ASN1Primitive aSN1Primitive = paramArrayOfASN1Encodable[b].toASN1Primitive();
/*     */       
/*  90 */       writePrimitive(aSN1Primitive, true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final void writeElements(Enumeration<ASN1Encodable> paramEnumeration) throws IOException {
/*  97 */     while (paramEnumeration.hasMoreElements()) {
/*     */       
/*  99 */       ASN1Primitive aSN1Primitive = ((ASN1Encodable)paramEnumeration.nextElement()).toASN1Primitive();
/*     */       
/* 101 */       writePrimitive(aSN1Primitive, true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void writeEncoded(boolean paramBoolean, int paramInt, byte paramByte) throws IOException {
/* 111 */     if (paramBoolean)
/*     */     {
/* 113 */       write(paramInt);
/*     */     }
/* 115 */     writeLength(1);
/* 116 */     write(paramByte);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void writeEncoded(boolean paramBoolean, int paramInt, byte[] paramArrayOfbyte) throws IOException {
/* 125 */     if (paramBoolean)
/*     */     {
/* 127 */       write(paramInt);
/*     */     }
/* 129 */     writeLength(paramArrayOfbyte.length);
/* 130 */     write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void writeEncoded(boolean paramBoolean, int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3) throws IOException {
/* 141 */     if (paramBoolean)
/*     */     {
/* 143 */       write(paramInt1);
/*     */     }
/* 145 */     writeLength(paramInt3);
/* 146 */     write(paramArrayOfbyte, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void writeEncoded(boolean paramBoolean, int paramInt, byte paramByte, byte[] paramArrayOfbyte) throws IOException {
/* 156 */     if (paramBoolean)
/*     */     {
/* 158 */       write(paramInt);
/*     */     }
/* 160 */     writeLength(1 + paramArrayOfbyte.length);
/* 161 */     write(paramByte);
/* 162 */     write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
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
/*     */   final void writeEncoded(boolean paramBoolean, int paramInt1, byte paramByte1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3, byte paramByte2) throws IOException {
/* 175 */     if (paramBoolean)
/*     */     {
/* 177 */       write(paramInt1);
/*     */     }
/* 179 */     writeLength(2 + paramInt3);
/* 180 */     write(paramByte1);
/* 181 */     write(paramArrayOfbyte, paramInt2, paramInt3);
/* 182 */     write(paramByte2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final void writeEncoded(boolean paramBoolean, int paramInt1, int paramInt2, byte[] paramArrayOfbyte) throws IOException {
/* 188 */     writeTag(paramBoolean, paramInt1, paramInt2);
/* 189 */     writeLength(paramArrayOfbyte.length);
/* 190 */     write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final void writeEncodedIndef(boolean paramBoolean, int paramInt1, int paramInt2, byte[] paramArrayOfbyte) throws IOException {
/* 196 */     writeTag(paramBoolean, paramInt1, paramInt2);
/* 197 */     write(128);
/* 198 */     write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
/* 199 */     write(0);
/* 200 */     write(0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final void writeEncodedIndef(boolean paramBoolean, int paramInt, ASN1Encodable[] paramArrayOfASN1Encodable) throws IOException {
/* 206 */     if (paramBoolean)
/*     */     {
/* 208 */       write(paramInt);
/*     */     }
/* 210 */     write(128);
/* 211 */     writeElements(paramArrayOfASN1Encodable);
/* 212 */     write(0);
/* 213 */     write(0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final void writeEncodedIndef(boolean paramBoolean, int paramInt, Enumeration paramEnumeration) throws IOException {
/* 219 */     if (paramBoolean)
/*     */     {
/* 221 */       write(paramInt);
/*     */     }
/* 223 */     write(128);
/* 224 */     writeElements(paramEnumeration);
/* 225 */     write(0);
/* 226 */     write(0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final void writeTag(boolean paramBoolean, int paramInt1, int paramInt2) throws IOException {
/* 232 */     if (!paramBoolean) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 237 */     if (paramInt2 < 31) {
/*     */       
/* 239 */       write(paramInt1 | paramInt2);
/*     */     }
/*     */     else {
/*     */       
/* 243 */       write(paramInt1 | 0x1F);
/* 244 */       if (paramInt2 < 128) {
/*     */         
/* 246 */         write(paramInt2);
/*     */       }
/*     */       else {
/*     */         
/* 250 */         byte[] arrayOfByte = new byte[5];
/* 251 */         int i = arrayOfByte.length;
/*     */         
/* 253 */         arrayOfByte[--i] = (byte)(paramInt2 & 0x7F);
/*     */ 
/*     */         
/*     */         do {
/* 257 */           paramInt2 >>= 7;
/* 258 */           arrayOfByte[--i] = (byte)(paramInt2 & 0x7F | 0x80);
/*     */         }
/* 260 */         while (paramInt2 > 127);
/*     */         
/* 262 */         write(arrayOfByte, i, arrayOfByte.length - i);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeObject(ASN1Encodable paramASN1Encodable) throws IOException {
/* 269 */     if (null == paramASN1Encodable)
/*     */     {
/* 271 */       throw new IOException("null object detected");
/*     */     }
/*     */     
/* 274 */     writePrimitive(paramASN1Encodable.toASN1Primitive(), true);
/* 275 */     flushInternal();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeObject(ASN1Primitive paramASN1Primitive) throws IOException {
/* 280 */     if (null == paramASN1Primitive)
/*     */     {
/* 282 */       throw new IOException("null object detected");
/*     */     }
/*     */     
/* 285 */     writePrimitive(paramASN1Primitive, true);
/* 286 */     flushInternal();
/*     */   }
/*     */ 
/*     */   
/*     */   void writePrimitive(ASN1Primitive paramASN1Primitive, boolean paramBoolean) throws IOException {
/* 291 */     paramASN1Primitive.encode(this, paramBoolean);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 297 */     this.os.close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 303 */     this.os.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void flushInternal() throws IOException {}
/*     */ 
/*     */ 
/*     */   
/*     */   DEROutputStream getDERSubStream() {
/* 314 */     return new DEROutputStream(this.os);
/*     */   }
/*     */ 
/*     */   
/*     */   org.bouncycastle.asn1.ASN1OutputStream getDLSubStream() {
/* 319 */     return (org.bouncycastle.asn1.ASN1OutputStream)new DLOutputStream(this.os);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1OutputStream.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */