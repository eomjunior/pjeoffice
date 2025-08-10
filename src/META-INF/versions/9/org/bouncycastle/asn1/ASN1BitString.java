/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1ParsingException;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1String;
/*     */ import org.bouncycastle.asn1.DERBitString;
/*     */ import org.bouncycastle.asn1.DLBitString;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.io.Streams;
/*     */ 
/*     */ public abstract class ASN1BitString
/*     */   extends ASN1Primitive implements ASN1String {
/*  17 */   private static final char[] table = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */ 
/*     */ 
/*     */   
/*     */   protected final byte[] data;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int padBits;
/*     */ 
/*     */ 
/*     */   
/*     */   protected static int getPadBits(int paramInt) {
/*  30 */     int i = 0; byte b;
/*  31 */     for (b = 3; b >= 0; b--) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  37 */       if (b != 0) {
/*     */         
/*  39 */         if (paramInt >> b * 8 != 0) {
/*     */           
/*  41 */           i = paramInt >> b * 8 & 0xFF;
/*     */ 
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*  47 */       } else if (paramInt != 0) {
/*     */         
/*  49 */         i = paramInt & 0xFF;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/*  55 */     if (i == 0)
/*     */     {
/*  57 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*  61 */     b = 1;
/*     */     
/*  63 */     while (((i <<= 1) & 0xFF) != 0)
/*     */     {
/*  65 */       b++;
/*     */     }
/*     */     
/*  68 */     return 8 - b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static byte[] getBytes(int paramInt) {
/*  78 */     if (paramInt == 0)
/*     */     {
/*  80 */       return new byte[0];
/*     */     }
/*     */     
/*  83 */     byte b1 = 4;
/*  84 */     for (byte b2 = 3; b2 >= 1; b2--) {
/*     */       
/*  86 */       if ((paramInt & 255 << b2 * 8) != 0) {
/*     */         break;
/*     */       }
/*     */       
/*  90 */       b1--;
/*     */     } 
/*     */     
/*  93 */     byte[] arrayOfByte = new byte[b1];
/*  94 */     for (byte b3 = 0; b3 < b1; b3++)
/*     */     {
/*  96 */       arrayOfByte[b3] = (byte)(paramInt >> b3 * 8 & 0xFF);
/*     */     }
/*     */     
/*  99 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ASN1BitString(byte paramByte, int paramInt) {
/* 104 */     if (paramInt > 7 || paramInt < 0)
/*     */     {
/* 106 */       throw new IllegalArgumentException("pad bits cannot be greater than 7 or less than 0");
/*     */     }
/*     */     
/* 109 */     this.data = new byte[] { paramByte };
/* 110 */     this.padBits = paramInt;
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
/*     */   public ASN1BitString(byte[] paramArrayOfbyte, int paramInt) {
/* 123 */     if (paramArrayOfbyte == null)
/*     */     {
/* 125 */       throw new NullPointerException("'data' cannot be null");
/*     */     }
/* 127 */     if (paramArrayOfbyte.length == 0 && paramInt != 0)
/*     */     {
/* 129 */       throw new IllegalArgumentException("zero length data with non-zero pad bits");
/*     */     }
/* 131 */     if (paramInt > 7 || paramInt < 0)
/*     */     {
/* 133 */       throw new IllegalArgumentException("pad bits cannot be greater than 7 or less than 0");
/*     */     }
/*     */     
/* 136 */     this.data = Arrays.clone(paramArrayOfbyte);
/* 137 */     this.padBits = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString() {
/*     */     byte[] arrayOfByte;
/* 147 */     StringBuffer stringBuffer = new StringBuffer("#");
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 152 */       arrayOfByte = getEncoded();
/*     */     }
/* 154 */     catch (IOException iOException) {
/*     */       
/* 156 */       throw new ASN1ParsingException("Internal error encoding BitString: " + iOException.getMessage(), iOException);
/*     */     } 
/*     */     
/* 159 */     for (byte b = 0; b != arrayOfByte.length; b++) {
/*     */       
/* 161 */       stringBuffer.append(table[arrayOfByte[b] >>> 4 & 0xF]);
/* 162 */       stringBuffer.append(table[arrayOfByte[b] & 0xF]);
/*     */     } 
/*     */     
/* 165 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 173 */     int i = 0;
/* 174 */     int j = Math.min(4, this.data.length - 1); byte b;
/* 175 */     for (b = 0; b < j; b++)
/*     */     {
/* 177 */       i |= (this.data[b] & 0xFF) << 8 * b;
/*     */     }
/* 179 */     if (0 <= j && j < 4) {
/*     */       
/* 181 */       b = (byte)(this.data[j] & 255 << this.padBits);
/* 182 */       i |= (b & 0xFF) << 8 * j;
/*     */     } 
/* 184 */     return i;
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
/*     */   public byte[] getOctets() {
/* 196 */     if (this.padBits != 0)
/*     */     {
/* 198 */       throw new IllegalStateException("attempt to get non-octet aligned data from BIT STRING");
/*     */     }
/*     */     
/* 201 */     return Arrays.clone(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getBytes() {
/* 206 */     if (0 == this.data.length)
/*     */     {
/* 208 */       return this.data;
/*     */     }
/*     */     
/* 211 */     byte[] arrayOfByte = Arrays.clone(this.data);
/*     */     
/* 213 */     arrayOfByte[this.data.length - 1] = (byte)(arrayOfByte[this.data.length - 1] & 255 << this.padBits);
/* 214 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPadBits() {
/* 219 */     return this.padBits;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 224 */     return getString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 229 */     int i = this.data.length;
/* 230 */     if (--i < 0)
/*     */     {
/* 232 */       return 1;
/*     */     }
/*     */     
/* 235 */     byte b = (byte)(this.data[i] & 255 << this.padBits);
/*     */     
/* 237 */     int j = Arrays.hashCode(this.data, 0, i);
/* 238 */     j *= 257;
/* 239 */     j ^= b;
/* 240 */     return j ^ this.padBits;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 246 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.ASN1BitString))
/*     */     {
/* 248 */       return false;
/*     */     }
/*     */     
/* 251 */     org.bouncycastle.asn1.ASN1BitString aSN1BitString = (org.bouncycastle.asn1.ASN1BitString)paramASN1Primitive;
/* 252 */     if (this.padBits != aSN1BitString.padBits)
/*     */     {
/* 254 */       return false;
/*     */     }
/* 256 */     byte[] arrayOfByte1 = this.data, arrayOfByte2 = aSN1BitString.data;
/* 257 */     int i = arrayOfByte1.length;
/* 258 */     if (i != arrayOfByte2.length)
/*     */     {
/* 260 */       return false;
/*     */     }
/* 262 */     if (--i < 0)
/*     */     {
/* 264 */       return true; } 
/*     */     byte b;
/* 266 */     for (b = 0; b < i; b++) {
/*     */       
/* 268 */       if (arrayOfByte1[b] != arrayOfByte2[b])
/*     */       {
/* 270 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 274 */     b = (byte)(arrayOfByte1[i] & 255 << this.padBits);
/* 275 */     byte b1 = (byte)(arrayOfByte2[i] & 255 << this.padBits);
/*     */     
/* 277 */     return (b == b1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static org.bouncycastle.asn1.ASN1BitString fromInputStream(int paramInt, InputStream paramInputStream) throws IOException {
/* 283 */     if (paramInt < 1)
/*     */     {
/* 285 */       throw new IllegalArgumentException("truncated BIT STRING detected");
/*     */     }
/*     */     
/* 288 */     int i = paramInputStream.read();
/* 289 */     byte[] arrayOfByte = new byte[paramInt - 1];
/*     */     
/* 291 */     if (arrayOfByte.length != 0) {
/*     */       
/* 293 */       if (Streams.readFully(paramInputStream, arrayOfByte) != arrayOfByte.length)
/*     */       {
/* 295 */         throw new EOFException("EOF encountered in middle of BIT STRING");
/*     */       }
/*     */       
/* 298 */       if (i > 0 && i < 8)
/*     */       {
/* 300 */         if (arrayOfByte[arrayOfByte.length - 1] != (byte)(arrayOfByte[arrayOfByte.length - 1] & 255 << i))
/*     */         {
/* 302 */           return (org.bouncycastle.asn1.ASN1BitString)new DLBitString(arrayOfByte, i);
/*     */         }
/*     */       }
/*     */     } 
/*     */     
/* 307 */     return (org.bouncycastle.asn1.ASN1BitString)new DERBitString(arrayOfByte, i);
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1Primitive getLoadedObject() {
/* 312 */     return toASN1Primitive();
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDERObject() {
/* 317 */     return (ASN1Primitive)new DERBitString(this.data, this.padBits);
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDLObject() {
/* 322 */     return (ASN1Primitive)new DLBitString(this.data, this.padBits);
/*     */   }
/*     */   
/*     */   abstract void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1BitString.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */