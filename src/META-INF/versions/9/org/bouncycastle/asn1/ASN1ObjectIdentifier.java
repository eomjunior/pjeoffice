/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.OIDTokenizer;
/*     */ import org.bouncycastle.asn1.StreamUtil;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ASN1ObjectIdentifier
/*     */   extends ASN1Primitive
/*     */ {
/*     */   private final String identifier;
/*     */   private byte[] body;
/*     */   private static final long LONG_LIMIT = 72057594037927808L;
/*     */   
/*     */   public static org.bouncycastle.asn1.ASN1ObjectIdentifier getInstance(Object paramObject) {
/*  31 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.ASN1ObjectIdentifier)
/*     */     {
/*  33 */       return (org.bouncycastle.asn1.ASN1ObjectIdentifier)paramObject;
/*     */     }
/*     */     
/*  36 */     if (paramObject instanceof ASN1Encodable) {
/*     */       
/*  38 */       ASN1Primitive aSN1Primitive = ((ASN1Encodable)paramObject).toASN1Primitive();
/*     */       
/*  40 */       if (aSN1Primitive instanceof org.bouncycastle.asn1.ASN1ObjectIdentifier)
/*     */       {
/*  42 */         return (org.bouncycastle.asn1.ASN1ObjectIdentifier)aSN1Primitive;
/*     */       }
/*     */     } 
/*     */     
/*  46 */     if (paramObject instanceof byte[]) {
/*     */       
/*  48 */       byte[] arrayOfByte = (byte[])paramObject;
/*     */       
/*     */       try {
/*  51 */         return (org.bouncycastle.asn1.ASN1ObjectIdentifier)fromByteArray(arrayOfByte);
/*     */       }
/*  53 */       catch (IOException iOException) {
/*     */         
/*  55 */         throw new IllegalArgumentException("failed to construct object identifier from byte[]: " + iOException.getMessage());
/*     */       } 
/*     */     } 
/*     */     
/*  59 */     throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
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
/*     */   public static org.bouncycastle.asn1.ASN1ObjectIdentifier getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  76 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */     
/*  78 */     if (paramBoolean || aSN1Primitive instanceof org.bouncycastle.asn1.ASN1ObjectIdentifier)
/*     */     {
/*  80 */       return getInstance(aSN1Primitive);
/*     */     }
/*     */ 
/*     */     
/*  84 */     return fromOctetString(ASN1OctetString.getInstance(aSN1Primitive).getOctets());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ASN1ObjectIdentifier(byte[] paramArrayOfbyte) {
/*  93 */     StringBuffer stringBuffer = new StringBuffer();
/*  94 */     long l = 0L;
/*  95 */     BigInteger bigInteger = null;
/*  96 */     boolean bool = true;
/*     */     
/*  98 */     for (byte b = 0; b != paramArrayOfbyte.length; b++) {
/*     */       
/* 100 */       int i = paramArrayOfbyte[b] & 0xFF;
/*     */       
/* 102 */       if (l <= 72057594037927808L) {
/*     */         
/* 104 */         l += (i & 0x7F);
/* 105 */         if ((i & 0x80) == 0)
/*     */         {
/* 107 */           if (bool) {
/*     */             
/* 109 */             if (l < 40L) {
/*     */               
/* 111 */               stringBuffer.append('0');
/*     */             }
/* 113 */             else if (l < 80L) {
/*     */               
/* 115 */               stringBuffer.append('1');
/* 116 */               l -= 40L;
/*     */             }
/*     */             else {
/*     */               
/* 120 */               stringBuffer.append('2');
/* 121 */               l -= 80L;
/*     */             } 
/* 123 */             bool = false;
/*     */           } 
/*     */           
/* 126 */           stringBuffer.append('.');
/* 127 */           stringBuffer.append(l);
/* 128 */           l = 0L;
/*     */         }
/*     */         else
/*     */         {
/* 132 */           l <<= 7L;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 137 */         if (bigInteger == null)
/*     */         {
/* 139 */           bigInteger = BigInteger.valueOf(l);
/*     */         }
/* 141 */         bigInteger = bigInteger.or(BigInteger.valueOf((i & 0x7F)));
/* 142 */         if ((i & 0x80) == 0) {
/*     */           
/* 144 */           if (bool) {
/*     */             
/* 146 */             stringBuffer.append('2');
/* 147 */             bigInteger = bigInteger.subtract(BigInteger.valueOf(80L));
/* 148 */             bool = false;
/*     */           } 
/*     */           
/* 151 */           stringBuffer.append('.');
/* 152 */           stringBuffer.append(bigInteger);
/* 153 */           bigInteger = null;
/* 154 */           l = 0L;
/*     */         }
/*     */         else {
/*     */           
/* 158 */           bigInteger = bigInteger.shiftLeft(7);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 163 */     this.identifier = stringBuffer.toString();
/* 164 */     this.body = Arrays.clone(paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1ObjectIdentifier(String paramString) {
/* 175 */     if (paramString == null)
/*     */     {
/* 177 */       throw new NullPointerException("'identifier' cannot be null");
/*     */     }
/* 179 */     if (!isValidIdentifier(paramString))
/*     */     {
/* 181 */       throw new IllegalArgumentException("string " + paramString + " not an OID");
/*     */     }
/*     */     
/* 184 */     this.identifier = paramString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ASN1ObjectIdentifier(org.bouncycastle.asn1.ASN1ObjectIdentifier paramASN1ObjectIdentifier, String paramString) {
/* 195 */     if (!isValidBranchID(paramString, 0))
/*     */     {
/* 197 */       throw new IllegalArgumentException("string " + paramString + " not a valid OID branch");
/*     */     }
/*     */     
/* 200 */     this.identifier = paramASN1ObjectIdentifier.getId() + "." + paramASN1ObjectIdentifier.getId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/* 210 */     return this.identifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public org.bouncycastle.asn1.ASN1ObjectIdentifier branch(String paramString) {
/* 221 */     return new org.bouncycastle.asn1.ASN1ObjectIdentifier(this, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean on(org.bouncycastle.asn1.ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 232 */     String str1 = getId(), str2 = paramASN1ObjectIdentifier.getId();
/* 233 */     return (str1.length() > str2.length() && str1.charAt(str2.length()) == '.' && str1.startsWith(str2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeField(ByteArrayOutputStream paramByteArrayOutputStream, long paramLong) {
/* 240 */     byte[] arrayOfByte = new byte[9];
/* 241 */     byte b = 8;
/* 242 */     arrayOfByte[b] = (byte)((int)paramLong & 0x7F);
/* 243 */     while (paramLong >= 128L) {
/*     */       
/* 245 */       paramLong >>= 7L;
/* 246 */       arrayOfByte[--b] = (byte)((int)paramLong & 0x7F | 0x80);
/*     */     } 
/* 248 */     paramByteArrayOutputStream.write(arrayOfByte, b, 9 - b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeField(ByteArrayOutputStream paramByteArrayOutputStream, BigInteger paramBigInteger) {
/* 255 */     int i = (paramBigInteger.bitLength() + 6) / 7;
/* 256 */     if (i == 0) {
/*     */       
/* 258 */       paramByteArrayOutputStream.write(0);
/*     */     }
/*     */     else {
/*     */       
/* 262 */       BigInteger bigInteger = paramBigInteger;
/* 263 */       byte[] arrayOfByte = new byte[i];
/* 264 */       for (int j = i - 1; j >= 0; j--) {
/*     */         
/* 266 */         arrayOfByte[j] = (byte)(bigInteger.intValue() & 0x7F | 0x80);
/* 267 */         bigInteger = bigInteger.shiftRight(7);
/*     */       } 
/* 269 */       arrayOfByte[i - 1] = (byte)(arrayOfByte[i - 1] & Byte.MAX_VALUE);
/* 270 */       paramByteArrayOutputStream.write(arrayOfByte, 0, arrayOfByte.length);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void doOutput(ByteArrayOutputStream paramByteArrayOutputStream) {
/* 276 */     OIDTokenizer oIDTokenizer = new OIDTokenizer(this.identifier);
/* 277 */     int i = Integer.parseInt(oIDTokenizer.nextToken()) * 40;
/*     */     
/* 279 */     String str = oIDTokenizer.nextToken();
/* 280 */     if (str.length() <= 18) {
/*     */       
/* 282 */       writeField(paramByteArrayOutputStream, i + Long.parseLong(str));
/*     */     }
/*     */     else {
/*     */       
/* 286 */       writeField(paramByteArrayOutputStream, (new BigInteger(str)).add(BigInteger.valueOf(i)));
/*     */     } 
/*     */     
/* 289 */     while (oIDTokenizer.hasMoreTokens()) {
/*     */       
/* 291 */       String str1 = oIDTokenizer.nextToken();
/* 292 */       if (str1.length() <= 18) {
/*     */         
/* 294 */         writeField(paramByteArrayOutputStream, Long.parseLong(str1));
/*     */         
/*     */         continue;
/*     */       } 
/* 298 */       writeField(paramByteArrayOutputStream, new BigInteger(str1));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized byte[] getBody() {
/* 305 */     if (this.body == null) {
/*     */       
/* 307 */       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*     */       
/* 309 */       doOutput(byteArrayOutputStream);
/*     */       
/* 311 */       this.body = byteArrayOutputStream.toByteArray();
/*     */     } 
/*     */     
/* 314 */     return this.body;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/* 319 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int encodedLength() throws IOException {
/* 325 */     int i = (getBody()).length;
/*     */     
/* 327 */     return 1 + StreamUtil.calculateBodyLength(i) + i;
/*     */   }
/*     */ 
/*     */   
/*     */   void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/* 332 */     paramASN1OutputStream.writeEncoded(paramBoolean, 6, getBody());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 337 */     return this.identifier.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 343 */     if (paramASN1Primitive == this)
/*     */     {
/* 345 */       return true;
/*     */     }
/*     */     
/* 348 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.ASN1ObjectIdentifier))
/*     */     {
/* 350 */       return false;
/*     */     }
/*     */     
/* 353 */     return this.identifier.equals(((org.bouncycastle.asn1.ASN1ObjectIdentifier)paramASN1Primitive).identifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 358 */     return getId();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isValidBranchID(String paramString, int paramInt) {
/* 364 */     byte b = 0;
/*     */     
/* 366 */     int i = paramString.length();
/* 367 */     while (--i >= paramInt) {
/*     */       
/* 369 */       char c = paramString.charAt(i);
/*     */       
/* 371 */       if (c == '.') {
/*     */         
/* 373 */         if (0 == b || (b > 1 && paramString
/* 374 */           .charAt(i + 1) == '0'))
/*     */         {
/* 376 */           return false;
/*     */         }
/*     */         
/* 379 */         b = 0; continue;
/*     */       } 
/* 381 */       if ('0' <= c && c <= '9') {
/*     */         
/* 383 */         b++;
/*     */         
/*     */         continue;
/*     */       } 
/* 387 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 391 */     if (0 == b || (b > 1 && paramString
/* 392 */       .charAt(i + 1) == '0'))
/*     */     {
/* 394 */       return false;
/*     */     }
/*     */     
/* 397 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isValidIdentifier(String paramString) {
/* 403 */     if (paramString.length() < 3 || paramString.charAt(1) != '.')
/*     */     {
/* 405 */       return false;
/*     */     }
/*     */     
/* 408 */     char c = paramString.charAt(0);
/* 409 */     if (c < '0' || c > '2')
/*     */     {
/* 411 */       return false;
/*     */     }
/*     */     
/* 414 */     return isValidBranchID(paramString, 2);
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
/*     */   public org.bouncycastle.asn1.ASN1ObjectIdentifier intern() {
/* 429 */     OidHandle oidHandle = new OidHandle(getBody());
/* 430 */     org.bouncycastle.asn1.ASN1ObjectIdentifier aSN1ObjectIdentifier = pool.get(oidHandle);
/* 431 */     if (aSN1ObjectIdentifier == null) {
/*     */       
/* 433 */       aSN1ObjectIdentifier = pool.putIfAbsent(oidHandle, this);
/* 434 */       if (aSN1ObjectIdentifier == null)
/*     */       {
/* 436 */         aSN1ObjectIdentifier = this;
/*     */       }
/*     */     } 
/* 439 */     return aSN1ObjectIdentifier;
/*     */   }
/*     */   
/* 442 */   private static final ConcurrentMap<OidHandle, org.bouncycastle.asn1.ASN1ObjectIdentifier> pool = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static org.bouncycastle.asn1.ASN1ObjectIdentifier fromOctetString(byte[] paramArrayOfbyte) {
/* 473 */     OidHandle oidHandle = new OidHandle(paramArrayOfbyte);
/* 474 */     org.bouncycastle.asn1.ASN1ObjectIdentifier aSN1ObjectIdentifier = pool.get(oidHandle);
/* 475 */     if (aSN1ObjectIdentifier == null)
/*     */     {
/* 477 */       return new org.bouncycastle.asn1.ASN1ObjectIdentifier(paramArrayOfbyte);
/*     */     }
/* 479 */     return aSN1ObjectIdentifier;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1ObjectIdentifier.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */