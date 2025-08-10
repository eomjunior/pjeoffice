/*     */ package META-INF.versions.9.org.bouncycastle.util.encoders;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import org.bouncycastle.util.encoders.Encoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HexEncoder
/*     */   implements Encoder
/*     */ {
/*  12 */   protected final byte[] encodingTable = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  21 */   protected final byte[] decodingTable = new byte[128];
/*     */   
/*     */   protected void initialiseDecodingTable() {
/*     */     byte b;
/*  25 */     for (b = 0; b < this.decodingTable.length; b++)
/*     */     {
/*  27 */       this.decodingTable[b] = -1;
/*     */     }
/*     */     
/*  30 */     for (b = 0; b < this.encodingTable.length; b++)
/*     */     {
/*  32 */       this.decodingTable[this.encodingTable[b]] = (byte)b;
/*     */     }
/*     */     
/*  35 */     this.decodingTable[65] = this.decodingTable[97];
/*  36 */     this.decodingTable[66] = this.decodingTable[98];
/*  37 */     this.decodingTable[67] = this.decodingTable[99];
/*  38 */     this.decodingTable[68] = this.decodingTable[100];
/*  39 */     this.decodingTable[69] = this.decodingTable[101];
/*  40 */     this.decodingTable[70] = this.decodingTable[102];
/*     */   }
/*     */ 
/*     */   
/*     */   public HexEncoder() {
/*  45 */     initialiseDecodingTable();
/*     */   }
/*     */ 
/*     */   
/*     */   public int encode(byte[] paramArrayOfbyte1, int paramInt1, int paramInt2, byte[] paramArrayOfbyte2, int paramInt3) throws IOException {
/*  50 */     int i = paramInt1;
/*  51 */     int j = paramInt1 + paramInt2;
/*  52 */     int k = paramInt3;
/*     */     
/*  54 */     while (i < j) {
/*     */       
/*  56 */       int m = paramArrayOfbyte1[i++] & 0xFF;
/*     */       
/*  58 */       paramArrayOfbyte2[k++] = this.encodingTable[m >>> 4];
/*  59 */       paramArrayOfbyte2[k++] = this.encodingTable[m & 0xF];
/*     */     } 
/*     */     
/*  62 */     return k - paramInt3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int encode(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, OutputStream paramOutputStream) throws IOException {
/*  73 */     byte[] arrayOfByte = new byte[72];
/*  74 */     while (paramInt2 > 0) {
/*     */       
/*  76 */       int i = Math.min(36, paramInt2);
/*  77 */       int j = encode(paramArrayOfbyte, paramInt1, i, arrayOfByte, 0);
/*  78 */       paramOutputStream.write(arrayOfByte, 0, j);
/*  79 */       paramInt1 += i;
/*  80 */       paramInt2 -= i;
/*     */     } 
/*  82 */     return paramInt2 * 2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean ignore(char paramChar) {
/*  88 */     return (paramChar == '\n' || paramChar == '\r' || paramChar == '\t' || paramChar == ' ');
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
/*     */   public int decode(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, OutputStream paramOutputStream) throws IOException {
/* 105 */     byte b1 = 0;
/* 106 */     byte[] arrayOfByte = new byte[36];
/* 107 */     byte b2 = 0;
/*     */     
/* 109 */     int i = paramInt1 + paramInt2;
/*     */     
/* 111 */     while (i > paramInt1) {
/*     */       
/* 113 */       if (!ignore((char)paramArrayOfbyte[i - 1])) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 118 */       i--;
/*     */     } 
/*     */     
/* 121 */     int j = paramInt1;
/* 122 */     while (j < i) {
/*     */       
/* 124 */       while (j < i && ignore((char)paramArrayOfbyte[j]))
/*     */       {
/* 126 */         j++;
/*     */       }
/*     */       
/* 129 */       byte b3 = this.decodingTable[paramArrayOfbyte[j++]];
/*     */       
/* 131 */       while (j < i && ignore((char)paramArrayOfbyte[j]))
/*     */       {
/* 133 */         j++;
/*     */       }
/*     */       
/* 136 */       byte b4 = this.decodingTable[paramArrayOfbyte[j++]];
/*     */       
/* 138 */       if ((b3 | b4) < 0)
/*     */       {
/* 140 */         throw new IOException("invalid characters encountered in Hex data");
/*     */       }
/*     */       
/* 143 */       arrayOfByte[b2++] = (byte)(b3 << 4 | b4);
/*     */       
/* 145 */       if (b2 == arrayOfByte.length) {
/*     */         
/* 147 */         paramOutputStream.write(arrayOfByte);
/* 148 */         b2 = 0;
/*     */       } 
/* 150 */       b1++;
/*     */     } 
/*     */     
/* 153 */     if (b2 > 0)
/*     */     {
/* 155 */       paramOutputStream.write(arrayOfByte, 0, b2);
/*     */     }
/*     */     
/* 158 */     return b1;
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
/*     */   public int decode(String paramString, OutputStream paramOutputStream) throws IOException {
/* 173 */     byte b1 = 0;
/* 174 */     byte[] arrayOfByte = new byte[36];
/* 175 */     byte b2 = 0;
/*     */     
/* 177 */     int i = paramString.length();
/*     */     
/* 179 */     while (i > 0) {
/*     */       
/* 181 */       if (!ignore(paramString.charAt(i - 1))) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 186 */       i--;
/*     */     } 
/*     */     
/* 189 */     byte b3 = 0;
/* 190 */     while (b3 < i) {
/*     */       
/* 192 */       while (b3 < i && ignore(paramString.charAt(b3)))
/*     */       {
/* 194 */         b3++;
/*     */       }
/*     */       
/* 197 */       byte b4 = this.decodingTable[paramString.charAt(b3++)];
/*     */       
/* 199 */       while (b3 < i && ignore(paramString.charAt(b3)))
/*     */       {
/* 201 */         b3++;
/*     */       }
/*     */       
/* 204 */       byte b5 = this.decodingTable[paramString.charAt(b3++)];
/*     */       
/* 206 */       if ((b4 | b5) < 0)
/*     */       {
/* 208 */         throw new IOException("invalid characters encountered in Hex string");
/*     */       }
/*     */       
/* 211 */       arrayOfByte[b2++] = (byte)(b4 << 4 | b5);
/*     */       
/* 213 */       if (b2 == arrayOfByte.length) {
/*     */         
/* 215 */         paramOutputStream.write(arrayOfByte);
/* 216 */         b2 = 0;
/*     */       } 
/*     */       
/* 219 */       b1++;
/*     */     } 
/*     */     
/* 222 */     if (b2 > 0)
/*     */     {
/* 224 */       paramOutputStream.write(arrayOfByte, 0, b2);
/*     */     }
/*     */     
/* 227 */     return b1;
/*     */   }
/*     */ 
/*     */   
/*     */   byte[] decodeStrict(String paramString, int paramInt1, int paramInt2) throws IOException {
/* 232 */     if (null == paramString)
/*     */     {
/* 234 */       throw new NullPointerException("'str' cannot be null");
/*     */     }
/* 236 */     if (paramInt1 < 0 || paramInt2 < 0 || paramInt1 > paramString.length() - paramInt2)
/*     */     {
/* 238 */       throw new IndexOutOfBoundsException("invalid offset and/or length specified");
/*     */     }
/* 240 */     if (0 != (paramInt2 & 0x1))
/*     */     {
/* 242 */       throw new IOException("a hexadecimal encoding must have an even number of characters");
/*     */     }
/*     */     
/* 245 */     int i = paramInt2 >>> 1;
/* 246 */     byte[] arrayOfByte = new byte[i];
/*     */     
/* 248 */     int j = paramInt1;
/* 249 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 251 */       byte b1 = this.decodingTable[paramString.charAt(j++)];
/* 252 */       byte b2 = this.decodingTable[paramString.charAt(j++)];
/*     */       
/* 254 */       int k = b1 << 4 | b2;
/* 255 */       if (k < 0)
/*     */       {
/* 257 */         throw new IOException("invalid characters encountered in Hex string");
/*     */       }
/*     */       
/* 260 */       arrayOfByte[b] = (byte)k;
/*     */     } 
/* 262 */     return arrayOfByte;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/util/encoders/HexEncoder.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */