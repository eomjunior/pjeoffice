/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.ASN1SetParser;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.BERSet;
/*     */ import org.bouncycastle.asn1.DERSet;
/*     */ import org.bouncycastle.asn1.DLSet;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ import org.bouncycastle.util.Iterable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ASN1Set
/*     */   extends ASN1Primitive
/*     */   implements Iterable<ASN1Encodable>
/*     */ {
/*     */   protected final ASN1Encodable[] elements;
/*     */   protected final boolean isSorted;
/*     */   
/*     */   public static org.bouncycastle.asn1.ASN1Set getInstance(Object paramObject) {
/* 114 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.ASN1Set)
/*     */     {
/* 116 */       return (org.bouncycastle.asn1.ASN1Set)paramObject;
/*     */     }
/* 118 */     if (paramObject instanceof ASN1SetParser)
/*     */     {
/* 120 */       return getInstance(((ASN1SetParser)paramObject).toASN1Primitive());
/*     */     }
/* 122 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/* 126 */         return getInstance(ASN1Primitive.fromByteArray((byte[])paramObject));
/*     */       }
/* 128 */       catch (IOException iOException) {
/*     */         
/* 130 */         throw new IllegalArgumentException("failed to construct set from byte[]: " + iOException.getMessage());
/*     */       } 
/*     */     }
/* 133 */     if (paramObject instanceof ASN1Encodable) {
/*     */       
/* 135 */       ASN1Primitive aSN1Primitive = ((ASN1Encodable)paramObject).toASN1Primitive();
/*     */       
/* 137 */       if (aSN1Primitive instanceof org.bouncycastle.asn1.ASN1Set)
/*     */       {
/* 139 */         return (org.bouncycastle.asn1.ASN1Set)aSN1Primitive;
/*     */       }
/*     */     } 
/*     */     
/* 143 */     throw new IllegalArgumentException("unknown object in getInstance: " + paramObject.getClass().getName());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.asn1.ASN1Set getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/* 167 */     if (paramBoolean) {
/*     */       
/* 169 */       if (!paramASN1TaggedObject.isExplicit())
/*     */       {
/* 171 */         throw new IllegalArgumentException("object implicit - explicit expected.");
/*     */       }
/*     */       
/* 174 */       return getInstance(paramASN1TaggedObject.getObject());
/*     */     } 
/*     */     
/* 177 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     if (paramASN1TaggedObject.isExplicit()) {
/*     */       
/* 185 */       if (paramASN1TaggedObject instanceof org.bouncycastle.asn1.BERTaggedObject)
/*     */       {
/* 187 */         return (org.bouncycastle.asn1.ASN1Set)new BERSet((ASN1Encodable)aSN1Primitive);
/*     */       }
/*     */       
/* 190 */       return (org.bouncycastle.asn1.ASN1Set)new DLSet((ASN1Encodable)aSN1Primitive);
/*     */     } 
/*     */     
/* 193 */     if (aSN1Primitive instanceof org.bouncycastle.asn1.ASN1Set) {
/*     */       
/* 195 */       org.bouncycastle.asn1.ASN1Set aSN1Set = (org.bouncycastle.asn1.ASN1Set)aSN1Primitive;
/*     */       
/* 197 */       if (paramASN1TaggedObject instanceof org.bouncycastle.asn1.BERTaggedObject)
/*     */       {
/* 199 */         return aSN1Set;
/*     */       }
/*     */       
/* 202 */       return (org.bouncycastle.asn1.ASN1Set)aSN1Set.toDLObject();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     if (aSN1Primitive instanceof ASN1Sequence) {
/*     */       
/* 210 */       ASN1Sequence aSN1Sequence = (ASN1Sequence)aSN1Primitive;
/*     */ 
/*     */       
/* 213 */       ASN1Encodable[] arrayOfASN1Encodable = aSN1Sequence.toArrayInternal();
/*     */       
/* 215 */       if (paramASN1TaggedObject instanceof org.bouncycastle.asn1.BERTaggedObject)
/*     */       {
/* 217 */         return (org.bouncycastle.asn1.ASN1Set)new BERSet(false, arrayOfASN1Encodable);
/*     */       }
/*     */       
/* 220 */       return (org.bouncycastle.asn1.ASN1Set)new DLSet(false, arrayOfASN1Encodable);
/*     */     } 
/*     */     
/* 223 */     throw new IllegalArgumentException("unknown object in getInstance: " + paramASN1TaggedObject.getClass().getName());
/*     */   }
/*     */ 
/*     */   
/*     */   protected ASN1Set() {
/* 228 */     this.elements = ASN1EncodableVector.EMPTY_ELEMENTS;
/* 229 */     this.isSorted = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ASN1Set(ASN1Encodable paramASN1Encodable) {
/* 238 */     if (null == paramASN1Encodable)
/*     */     {
/* 240 */       throw new NullPointerException("'element' cannot be null");
/*     */     }
/*     */     
/* 243 */     this.elements = new ASN1Encodable[] { paramASN1Encodable };
/* 244 */     this.isSorted = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ASN1Set(ASN1EncodableVector paramASN1EncodableVector, boolean paramBoolean) {
/*     */     ASN1Encodable[] arrayOfASN1Encodable;
/* 254 */     if (null == paramASN1EncodableVector)
/*     */     {
/* 256 */       throw new NullPointerException("'elementVector' cannot be null");
/*     */     }
/*     */ 
/*     */     
/* 260 */     if (paramBoolean && paramASN1EncodableVector.size() >= 2) {
/*     */       
/* 262 */       arrayOfASN1Encodable = paramASN1EncodableVector.copyElements();
/* 263 */       sort(arrayOfASN1Encodable);
/*     */     }
/*     */     else {
/*     */       
/* 267 */       arrayOfASN1Encodable = paramASN1EncodableVector.takeElements();
/*     */     } 
/*     */     
/* 270 */     this.elements = arrayOfASN1Encodable;
/* 271 */     this.isSorted = (paramBoolean || arrayOfASN1Encodable.length < 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ASN1Set(ASN1Encodable[] paramArrayOfASN1Encodable, boolean paramBoolean) {
/* 281 */     if (Arrays.isNullOrContainsNull((Object[])paramArrayOfASN1Encodable))
/*     */     {
/* 283 */       throw new NullPointerException("'elements' cannot be null, or contain null");
/*     */     }
/*     */     
/* 286 */     ASN1Encodable[] arrayOfASN1Encodable = ASN1EncodableVector.cloneElements(paramArrayOfASN1Encodable);
/* 287 */     if (paramBoolean && arrayOfASN1Encodable.length >= 2)
/*     */     {
/* 289 */       sort(arrayOfASN1Encodable);
/*     */     }
/*     */     
/* 292 */     this.elements = arrayOfASN1Encodable;
/* 293 */     this.isSorted = (paramBoolean || arrayOfASN1Encodable.length < 2);
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Set(boolean paramBoolean, ASN1Encodable[] paramArrayOfASN1Encodable) {
/* 298 */     this.elements = paramArrayOfASN1Encodable;
/* 299 */     this.isSorted = (paramBoolean || paramArrayOfASN1Encodable.length < 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Enumeration getObjects() {
/* 304 */     return (Enumeration)new Object(this);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1Encodable getObjectAt(int paramInt) {
/* 332 */     return this.elements[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 342 */     return this.elements.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1Encodable[] toArray() {
/* 347 */     return ASN1EncodableVector.cloneElements(this.elements);
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1SetParser parser() {
/* 352 */     int i = size();
/*     */     
/* 354 */     return (ASN1SetParser)new Object(this, i);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 393 */     int i = this.elements.length;
/* 394 */     int j = i + 1;
/*     */ 
/*     */     
/* 397 */     while (--i >= 0)
/*     */     {
/* 399 */       j += this.elements[i].toASN1Primitive().hashCode();
/*     */     }
/*     */     
/* 402 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ASN1Primitive toDERObject() {
/*     */     ASN1Encodable[] arrayOfASN1Encodable;
/* 412 */     if (this.isSorted) {
/*     */       
/* 414 */       arrayOfASN1Encodable = this.elements;
/*     */     }
/*     */     else {
/*     */       
/* 418 */       arrayOfASN1Encodable = (ASN1Encodable[])this.elements.clone();
/* 419 */       sort(arrayOfASN1Encodable);
/*     */     } 
/*     */     
/* 422 */     return (ASN1Primitive)new DERSet(true, arrayOfASN1Encodable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ASN1Primitive toDLObject() {
/* 431 */     return (ASN1Primitive)new DLSet(this.isSorted, this.elements);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 436 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.ASN1Set))
/*     */     {
/* 438 */       return false;
/*     */     }
/*     */     
/* 441 */     org.bouncycastle.asn1.ASN1Set aSN1Set = (org.bouncycastle.asn1.ASN1Set)paramASN1Primitive;
/*     */     
/* 443 */     int i = size();
/* 444 */     if (aSN1Set.size() != i)
/*     */     {
/* 446 */       return false;
/*     */     }
/*     */     
/* 449 */     DERSet dERSet1 = (DERSet)toDERObject();
/* 450 */     DERSet dERSet2 = (DERSet)aSN1Set.toDERObject();
/*     */     
/* 452 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 454 */       ASN1Primitive aSN1Primitive1 = dERSet1.elements[b].toASN1Primitive();
/* 455 */       ASN1Primitive aSN1Primitive2 = dERSet2.elements[b].toASN1Primitive();
/*     */       
/* 457 */       if (aSN1Primitive1 != aSN1Primitive2 && !aSN1Primitive1.asn1Equals(aSN1Primitive2))
/*     */       {
/* 459 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 463 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/* 468 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   abstract void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException;
/*     */   
/*     */   public String toString() {
/* 475 */     int i = size();
/* 476 */     if (0 == i)
/*     */     {
/* 478 */       return "[]";
/*     */     }
/*     */     
/* 481 */     StringBuffer stringBuffer = new StringBuffer();
/* 482 */     stringBuffer.append('[');
/* 483 */     byte b = 0;
/*     */     while (true) {
/* 485 */       stringBuffer.append(this.elements[b]);
/* 486 */       if (++b >= i) {
/*     */         break;
/*     */       }
/*     */       
/* 490 */       stringBuffer.append(", ");
/*     */     } 
/* 492 */     stringBuffer.append(']');
/* 493 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<ASN1Encodable> iterator() {
/* 498 */     return (Iterator<ASN1Encodable>)new Arrays.Iterator((Object[])toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] getDEREncoded(ASN1Encodable paramASN1Encodable) {
/*     */     try {
/* 505 */       return paramASN1Encodable.toASN1Primitive().getEncoded("DER");
/*     */     }
/* 507 */     catch (IOException iOException) {
/*     */       
/* 509 */       throw new IllegalArgumentException("cannot encode object added to SET");
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean lessThanOrEqual(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/* 532 */     int i = paramArrayOfbyte1[0] & 0xFFFFFFDF;
/* 533 */     int j = paramArrayOfbyte2[0] & 0xFFFFFFDF;
/* 534 */     if (i != j)
/*     */     {
/* 536 */       return (i < j);
/*     */     }
/*     */     
/* 539 */     int k = Math.min(paramArrayOfbyte1.length, paramArrayOfbyte2.length) - 1;
/* 540 */     for (byte b = 1; b < k; b++) {
/*     */       
/* 542 */       if (paramArrayOfbyte1[b] != paramArrayOfbyte2[b])
/*     */       {
/* 544 */         return ((paramArrayOfbyte1[b] & 0xFF) < (paramArrayOfbyte2[b] & 0xFF));
/*     */       }
/*     */     } 
/* 547 */     return ((paramArrayOfbyte1[k] & 0xFF) <= (paramArrayOfbyte2[k] & 0xFF));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void sort(ASN1Encodable[] paramArrayOfASN1Encodable) {
/* 552 */     int i = paramArrayOfASN1Encodable.length;
/* 553 */     if (i < 2) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 558 */     ASN1Encodable aSN1Encodable1 = paramArrayOfASN1Encodable[0], aSN1Encodable2 = paramArrayOfASN1Encodable[1];
/* 559 */     byte[] arrayOfByte1 = getDEREncoded(aSN1Encodable1), arrayOfByte2 = getDEREncoded(aSN1Encodable2);
/*     */     
/* 561 */     if (lessThanOrEqual(arrayOfByte2, arrayOfByte1)) {
/*     */       
/* 563 */       ASN1Encodable aSN1Encodable = aSN1Encodable2; aSN1Encodable2 = aSN1Encodable1; aSN1Encodable1 = aSN1Encodable;
/* 564 */       byte[] arrayOfByte = arrayOfByte2; arrayOfByte2 = arrayOfByte1; arrayOfByte1 = arrayOfByte;
/*     */     } 
/*     */     
/* 567 */     for (byte b = 2; b < i; b++) {
/*     */       
/* 569 */       ASN1Encodable aSN1Encodable = paramArrayOfASN1Encodable[b];
/* 570 */       byte[] arrayOfByte = getDEREncoded(aSN1Encodable);
/*     */       
/* 572 */       if (lessThanOrEqual(arrayOfByte2, arrayOfByte)) {
/*     */         
/* 574 */         paramArrayOfASN1Encodable[b - 2] = aSN1Encodable1;
/* 575 */         aSN1Encodable1 = aSN1Encodable2; arrayOfByte1 = arrayOfByte2;
/* 576 */         aSN1Encodable2 = aSN1Encodable; arrayOfByte2 = arrayOfByte;
/*     */ 
/*     */       
/*     */       }
/* 580 */       else if (lessThanOrEqual(arrayOfByte1, arrayOfByte)) {
/*     */         
/* 582 */         paramArrayOfASN1Encodable[b - 2] = aSN1Encodable1;
/* 583 */         aSN1Encodable1 = aSN1Encodable; arrayOfByte1 = arrayOfByte;
/*     */       }
/*     */       else {
/*     */         
/* 587 */         int j = b - 1;
/* 588 */         while (--j > 0) {
/*     */           
/* 590 */           ASN1Encodable aSN1Encodable3 = paramArrayOfASN1Encodable[j - 1];
/* 591 */           byte[] arrayOfByte3 = getDEREncoded(aSN1Encodable3);
/*     */           
/* 593 */           if (lessThanOrEqual(arrayOfByte3, arrayOfByte)) {
/*     */             break;
/*     */           }
/*     */ 
/*     */           
/* 598 */           paramArrayOfASN1Encodable[j] = aSN1Encodable3;
/*     */         } 
/*     */         
/* 601 */         paramArrayOfASN1Encodable[j] = aSN1Encodable;
/*     */       } 
/*     */     } 
/* 604 */     paramArrayOfASN1Encodable[i - 2] = aSN1Encodable1;
/* 605 */     paramArrayOfASN1Encodable[i - 1] = aSN1Encodable2;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1Set.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */