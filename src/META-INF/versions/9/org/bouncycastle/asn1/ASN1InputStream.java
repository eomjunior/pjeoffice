/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1Exception;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1StreamParser;
/*     */ import org.bouncycastle.asn1.BEROctetStringParser;
/*     */ import org.bouncycastle.asn1.BERSetParser;
/*     */ import org.bouncycastle.asn1.DERExternalParser;
/*     */ import org.bouncycastle.asn1.DERIA5String;
/*     */ import org.bouncycastle.asn1.DERPrintableString;
/*     */ import org.bouncycastle.asn1.DERUniversalString;
/*     */ import org.bouncycastle.asn1.DERVideotexString;
/*     */ import org.bouncycastle.asn1.DERVisibleString;
/*     */ import org.bouncycastle.asn1.DLFactory;
/*     */ import org.bouncycastle.asn1.DefiniteLengthInputStream;
/*     */ import org.bouncycastle.asn1.IndefiniteLengthInputStream;
/*     */ import org.bouncycastle.asn1.LazyEncodedSequence;
/*     */ import org.bouncycastle.asn1.StreamUtil;
/*     */ import org.bouncycastle.util.io.Streams;
/*     */ 
/*     */ public class ASN1InputStream extends FilterInputStream implements BERTags {
/*     */   public ASN1InputStream(InputStream paramInputStream) {
/*  29 */     this(paramInputStream, StreamUtil.findLimit(paramInputStream));
/*     */   }
/*     */ 
/*     */   
/*     */   private final int limit;
/*     */   
/*     */   private final boolean lazyEvaluate;
/*     */   
/*     */   private final byte[][] tmpBuffers;
/*     */ 
/*     */   
/*     */   public ASN1InputStream(byte[] paramArrayOfbyte) {
/*  41 */     this(new ByteArrayInputStream(paramArrayOfbyte), paramArrayOfbyte.length);
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
/*     */   public ASN1InputStream(byte[] paramArrayOfbyte, boolean paramBoolean) {
/*  55 */     this(new ByteArrayInputStream(paramArrayOfbyte), paramArrayOfbyte.length, paramBoolean);
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
/*     */   public ASN1InputStream(InputStream paramInputStream, int paramInt) {
/*  68 */     this(paramInputStream, paramInt, false);
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
/*     */   public ASN1InputStream(InputStream paramInputStream, boolean paramBoolean) {
/*  82 */     this(paramInputStream, StreamUtil.findLimit(paramInputStream), paramBoolean);
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
/*     */   public ASN1InputStream(InputStream paramInputStream, int paramInt, boolean paramBoolean) {
/*  98 */     super(paramInputStream);
/*  99 */     this.limit = paramInt;
/* 100 */     this.lazyEvaluate = paramBoolean;
/* 101 */     this.tmpBuffers = new byte[11][];
/*     */   }
/*     */ 
/*     */   
/*     */   int getLimit() {
/* 106 */     return this.limit;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int readLength() throws IOException {
/* 112 */     return readLength(this, this.limit, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readFully(byte[] paramArrayOfbyte) throws IOException {
/* 119 */     if (Streams.readFully(this, paramArrayOfbyte) != paramArrayOfbyte.length)
/*     */     {
/* 121 */       throw new EOFException("EOF encountered in middle of object");
/*     */     }
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
/*     */   protected ASN1Primitive buildObject(int paramInt1, int paramInt2, int paramInt3) throws IOException {
/* 140 */     boolean bool = ((paramInt1 & 0x20) != 0) ? true : false;
/*     */     
/* 142 */     DefiniteLengthInputStream definiteLengthInputStream = new DefiniteLengthInputStream(this, paramInt3, this.limit);
/*     */     
/* 144 */     if ((paramInt1 & 0x40) != 0)
/*     */     {
/* 146 */       return (ASN1Primitive)new DLApplicationSpecific(bool, paramInt2, definiteLengthInputStream.toByteArray());
/*     */     }
/*     */     
/* 149 */     if ((paramInt1 & 0x80) != 0)
/*     */     {
/* 151 */       return (new ASN1StreamParser((InputStream)definiteLengthInputStream)).readTaggedObject(bool, paramInt2);
/*     */     }
/*     */     
/* 154 */     if (bool) {
/*     */       ASN1EncodableVector aSN1EncodableVector; ASN1OctetString[] arrayOfASN1OctetString;
/*     */       byte b;
/* 157 */       switch (paramInt2) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 4:
/* 163 */           aSN1EncodableVector = readVector(definiteLengthInputStream);
/* 164 */           arrayOfASN1OctetString = new ASN1OctetString[aSN1EncodableVector.size()];
/*     */           
/* 166 */           for (b = 0; b != arrayOfASN1OctetString.length; b++) {
/*     */             
/* 168 */             ASN1Encodable aSN1Encodable = aSN1EncodableVector.get(b);
/* 169 */             if (aSN1Encodable instanceof ASN1OctetString) {
/*     */               
/* 171 */               arrayOfASN1OctetString[b] = (ASN1OctetString)aSN1Encodable;
/*     */             }
/*     */             else {
/*     */               
/* 175 */               throw new ASN1Exception("unknown object encountered in constructed OCTET STRING: " + aSN1Encodable.getClass());
/*     */             } 
/*     */           } 
/*     */           
/* 179 */           return (ASN1Primitive)new BEROctetString(arrayOfASN1OctetString);
/*     */         case 16:
/* 181 */           if (this.lazyEvaluate)
/*     */           {
/* 183 */             return (ASN1Primitive)new LazyEncodedSequence(definiteLengthInputStream.toByteArray());
/*     */           }
/*     */ 
/*     */           
/* 187 */           return (ASN1Primitive)DLFactory.createSequence(readVector(definiteLengthInputStream));
/*     */         
/*     */         case 17:
/* 190 */           return (ASN1Primitive)DLFactory.createSet(readVector(definiteLengthInputStream));
/*     */         case 8:
/* 192 */           return (ASN1Primitive)new DLExternal(readVector(definiteLengthInputStream));
/*     */       } 
/* 194 */       throw new IOException("unknown tag " + paramInt2 + " encountered");
/*     */     } 
/*     */ 
/*     */     
/* 198 */     return createPrimitiveDERObject(paramInt2, definiteLengthInputStream, this.tmpBuffers);
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1EncodableVector readVector(DefiniteLengthInputStream paramDefiniteLengthInputStream) throws IOException {
/* 203 */     if (paramDefiniteLengthInputStream.getRemaining() < 1)
/*     */     {
/* 205 */       return new ASN1EncodableVector(0);
/*     */     }
/*     */     
/* 208 */     org.bouncycastle.asn1.ASN1InputStream aSN1InputStream = new org.bouncycastle.asn1.ASN1InputStream((InputStream)paramDefiniteLengthInputStream);
/* 209 */     ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
/*     */     ASN1Primitive aSN1Primitive;
/* 211 */     while ((aSN1Primitive = aSN1InputStream.readObject()) != null)
/*     */     {
/* 213 */       aSN1EncodableVector.add((ASN1Encodable)aSN1Primitive);
/*     */     }
/* 215 */     return aSN1EncodableVector;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1Primitive readObject() throws IOException {
/* 221 */     int i = read();
/* 222 */     if (i <= 0) {
/*     */       
/* 224 */       if (i == 0)
/*     */       {
/* 226 */         throw new IOException("unexpected end-of-contents marker");
/*     */       }
/*     */       
/* 229 */       return null;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 235 */     int j = readTagNumber(this, i);
/*     */     
/* 237 */     boolean bool = ((i & 0x20) != 0) ? true : false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 242 */     int k = readLength();
/*     */     
/* 244 */     if (k < 0) {
/*     */       
/* 246 */       if (!bool)
/*     */       {
/* 248 */         throw new IOException("indefinite-length primitive encoding encountered");
/*     */       }
/*     */       
/* 251 */       IndefiniteLengthInputStream indefiniteLengthInputStream = new IndefiniteLengthInputStream(this, this.limit);
/* 252 */       ASN1StreamParser aSN1StreamParser = new ASN1StreamParser((InputStream)indefiniteLengthInputStream, this.limit);
/*     */       
/* 254 */       if ((i & 0x40) != 0)
/*     */       {
/* 256 */         return (new BERApplicationSpecificParser(j, aSN1StreamParser)).getLoadedObject();
/*     */       }
/*     */       
/* 259 */       if ((i & 0x80) != 0)
/*     */       {
/* 261 */         return (new BERTaggedObjectParser(true, j, aSN1StreamParser)).getLoadedObject();
/*     */       }
/*     */ 
/*     */       
/* 265 */       switch (j) {
/*     */         
/*     */         case 4:
/* 268 */           return (new BEROctetStringParser(aSN1StreamParser)).getLoadedObject();
/*     */         case 16:
/* 270 */           return (new BERSequenceParser(aSN1StreamParser)).getLoadedObject();
/*     */         case 17:
/* 272 */           return (new BERSetParser(aSN1StreamParser)).getLoadedObject();
/*     */         case 8:
/* 274 */           return (new DERExternalParser(aSN1StreamParser)).getLoadedObject();
/*     */       } 
/* 276 */       throw new IOException("unknown BER object encountered");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 283 */       return buildObject(i, j, k);
/*     */     }
/* 285 */     catch (IllegalArgumentException illegalArgumentException) {
/*     */       
/* 287 */       throw new ASN1Exception("corrupted stream detected", illegalArgumentException);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int readTagNumber(InputStream paramInputStream, int paramInt) throws IOException {
/* 295 */     int i = paramInt & 0x1F;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 300 */     if (i == 31) {
/*     */       
/* 302 */       i = 0;
/*     */       
/* 304 */       int j = paramInputStream.read();
/*     */ 
/*     */ 
/*     */       
/* 308 */       if ((j & 0x7F) == 0)
/*     */       {
/* 310 */         throw new IOException("corrupted stream - invalid high tag number found");
/*     */       }
/*     */       
/* 313 */       while (j >= 0 && (j & 0x80) != 0) {
/*     */         
/* 315 */         i |= j & 0x7F;
/* 316 */         i <<= 7;
/* 317 */         j = paramInputStream.read();
/*     */       } 
/*     */       
/* 320 */       if (j < 0)
/*     */       {
/* 322 */         throw new EOFException("EOF found inside tag value.");
/*     */       }
/*     */       
/* 325 */       i |= j & 0x7F;
/*     */     } 
/*     */     
/* 328 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int readLength(InputStream paramInputStream, int paramInt, boolean paramBoolean) throws IOException {
/* 334 */     int i = paramInputStream.read();
/* 335 */     if (i < 0)
/*     */     {
/* 337 */       throw new EOFException("EOF found when length expected");
/*     */     }
/*     */     
/* 340 */     if (i == 128)
/*     */     {
/* 342 */       return -1;
/*     */     }
/*     */     
/* 345 */     if (i > 127) {
/*     */       
/* 347 */       int j = i & 0x7F;
/*     */ 
/*     */       
/* 350 */       if (j > 4)
/*     */       {
/* 352 */         throw new IOException("DER length more than 4 bytes: " + j);
/*     */       }
/*     */       
/* 355 */       i = 0;
/* 356 */       for (byte b = 0; b < j; b++) {
/*     */         
/* 358 */         int k = paramInputStream.read();
/*     */         
/* 360 */         if (k < 0)
/*     */         {
/* 362 */           throw new EOFException("EOF found reading length");
/*     */         }
/*     */         
/* 365 */         i = (i << 8) + k;
/*     */       } 
/*     */       
/* 368 */       if (i < 0)
/*     */       {
/* 370 */         throw new IOException("corrupted stream - negative length found");
/*     */       }
/*     */       
/* 373 */       if (i >= paramInt && !paramBoolean)
/*     */       {
/* 375 */         throw new IOException("corrupted stream - out of bounds length found: " + i + " >= " + paramInt);
/*     */       }
/*     */     } 
/*     */     
/* 379 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] getBuffer(DefiniteLengthInputStream paramDefiniteLengthInputStream, byte[][] paramArrayOfbyte) throws IOException {
/* 385 */     int i = paramDefiniteLengthInputStream.getRemaining();
/* 386 */     if (i >= paramArrayOfbyte.length)
/*     */     {
/* 388 */       return paramDefiniteLengthInputStream.toByteArray();
/*     */     }
/*     */     
/* 391 */     byte[] arrayOfByte = paramArrayOfbyte[i];
/* 392 */     if (arrayOfByte == null)
/*     */     {
/* 394 */       arrayOfByte = paramArrayOfbyte[i] = new byte[i];
/*     */     }
/*     */     
/* 397 */     paramDefiniteLengthInputStream.readAllIntoByteArray(arrayOfByte);
/*     */     
/* 399 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] getBMPCharBuffer(DefiniteLengthInputStream paramDefiniteLengthInputStream) throws IOException {
/* 405 */     int i = paramDefiniteLengthInputStream.getRemaining();
/* 406 */     if (0 != (i & 0x1))
/*     */     {
/* 408 */       throw new IOException("malformed BMPString encoding encountered");
/*     */     }
/*     */     
/* 411 */     char[] arrayOfChar = new char[i / 2];
/* 412 */     byte b = 0;
/*     */     
/* 414 */     byte[] arrayOfByte = new byte[8];
/* 415 */     while (i >= 8) {
/*     */       
/* 417 */       if (Streams.readFully((InputStream)paramDefiniteLengthInputStream, arrayOfByte, 0, 8) != 8)
/*     */       {
/* 419 */         throw new EOFException("EOF encountered in middle of BMPString");
/*     */       }
/*     */       
/* 422 */       arrayOfChar[b] = (char)(arrayOfByte[0] << 8 | arrayOfByte[1] & 0xFF);
/* 423 */       arrayOfChar[b + 1] = (char)(arrayOfByte[2] << 8 | arrayOfByte[3] & 0xFF);
/* 424 */       arrayOfChar[b + 2] = (char)(arrayOfByte[4] << 8 | arrayOfByte[5] & 0xFF);
/* 425 */       arrayOfChar[b + 3] = (char)(arrayOfByte[6] << 8 | arrayOfByte[7] & 0xFF);
/* 426 */       b += 4;
/* 427 */       i -= 8;
/*     */     } 
/* 429 */     if (i > 0) {
/*     */       
/* 431 */       if (Streams.readFully((InputStream)paramDefiniteLengthInputStream, arrayOfByte, 0, i) != i)
/*     */       {
/* 433 */         throw new EOFException("EOF encountered in middle of BMPString");
/*     */       }
/*     */       
/* 436 */       byte b1 = 0;
/*     */       
/*     */       do {
/* 439 */         int j = arrayOfByte[b1++] << 8;
/* 440 */         int k = arrayOfByte[b1++] & 0xFF;
/* 441 */         arrayOfChar[b++] = (char)(j | k);
/*     */       }
/* 443 */       while (b1 < i);
/*     */     } 
/*     */     
/* 446 */     if (0 != paramDefiniteLengthInputStream.getRemaining() || arrayOfChar.length != b)
/*     */     {
/* 448 */       throw new IllegalStateException();
/*     */     }
/*     */     
/* 451 */     return arrayOfChar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ASN1Primitive createPrimitiveDERObject(int paramInt, DefiniteLengthInputStream paramDefiniteLengthInputStream, byte[][] paramArrayOfbyte) throws IOException {
/* 460 */     switch (paramInt) {
/*     */       
/*     */       case 3:
/* 463 */         return (ASN1Primitive)ASN1BitString.fromInputStream(paramDefiniteLengthInputStream.getRemaining(), (InputStream)paramDefiniteLengthInputStream);
/*     */       case 30:
/* 465 */         return (ASN1Primitive)new DERBMPString(getBMPCharBuffer(paramDefiniteLengthInputStream));
/*     */       case 1:
/* 467 */         return (ASN1Primitive)ASN1Boolean.fromOctetString(getBuffer(paramDefiniteLengthInputStream, paramArrayOfbyte));
/*     */       case 10:
/* 469 */         return (ASN1Primitive)ASN1Enumerated.fromOctetString(getBuffer(paramDefiniteLengthInputStream, paramArrayOfbyte));
/*     */       case 24:
/* 471 */         return (ASN1Primitive)new ASN1GeneralizedTime(paramDefiniteLengthInputStream.toByteArray());
/*     */       case 27:
/* 473 */         return (ASN1Primitive)new DERGeneralString(paramDefiniteLengthInputStream.toByteArray());
/*     */       case 22:
/* 475 */         return (ASN1Primitive)new DERIA5String(paramDefiniteLengthInputStream.toByteArray());
/*     */       case 2:
/* 477 */         return (ASN1Primitive)new ASN1Integer(paramDefiniteLengthInputStream.toByteArray(), false);
/*     */       case 5:
/* 479 */         return (ASN1Primitive)DERNull.INSTANCE;
/*     */       case 18:
/* 481 */         return (ASN1Primitive)new DERNumericString(paramDefiniteLengthInputStream.toByteArray());
/*     */       case 6:
/* 483 */         return (ASN1Primitive)ASN1ObjectIdentifier.fromOctetString(getBuffer(paramDefiniteLengthInputStream, paramArrayOfbyte));
/*     */       case 4:
/* 485 */         return (ASN1Primitive)new DEROctetString(paramDefiniteLengthInputStream.toByteArray());
/*     */       case 19:
/* 487 */         return (ASN1Primitive)new DERPrintableString(paramDefiniteLengthInputStream.toByteArray());
/*     */       case 20:
/* 489 */         return (ASN1Primitive)new DERT61String(paramDefiniteLengthInputStream.toByteArray());
/*     */       case 28:
/* 491 */         return (ASN1Primitive)new DERUniversalString(paramDefiniteLengthInputStream.toByteArray());
/*     */       case 23:
/* 493 */         return (ASN1Primitive)new ASN1UTCTime(paramDefiniteLengthInputStream.toByteArray());
/*     */       case 12:
/* 495 */         return (ASN1Primitive)new DERUTF8String(paramDefiniteLengthInputStream.toByteArray());
/*     */       case 26:
/* 497 */         return (ASN1Primitive)new DERVisibleString(paramDefiniteLengthInputStream.toByteArray());
/*     */       case 25:
/* 499 */         return (ASN1Primitive)new DERGraphicString(paramDefiniteLengthInputStream.toByteArray());
/*     */       case 21:
/* 501 */         return (ASN1Primitive)new DERVideotexString(paramDefiniteLengthInputStream.toByteArray());
/*     */     } 
/* 503 */     throw new IOException("unknown tag " + paramInt + " encountered");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1InputStream.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */