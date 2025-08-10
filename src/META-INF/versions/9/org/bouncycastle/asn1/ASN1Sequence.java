/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1SequenceParser;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.BERSequence;
/*     */ import org.bouncycastle.asn1.DERSequence;
/*     */ import org.bouncycastle.asn1.DLSequence;
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
/*     */ public abstract class ASN1Sequence
/*     */   extends ASN1Primitive
/*     */   implements Iterable<ASN1Encodable>
/*     */ {
/*     */   ASN1Encodable[] elements;
/*     */   
/*     */   public static org.bouncycastle.asn1.ASN1Sequence getInstance(Object paramObject) {
/*  76 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.ASN1Sequence)
/*     */     {
/*  78 */       return (org.bouncycastle.asn1.ASN1Sequence)paramObject;
/*     */     }
/*  80 */     if (paramObject instanceof ASN1SequenceParser)
/*     */     {
/*  82 */       return getInstance(((ASN1SequenceParser)paramObject).toASN1Primitive());
/*     */     }
/*  84 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  88 */         return getInstance(fromByteArray((byte[])paramObject));
/*     */       }
/*  90 */       catch (IOException iOException) {
/*     */         
/*  92 */         throw new IllegalArgumentException("failed to construct sequence from byte[]: " + iOException.getMessage());
/*     */       } 
/*     */     }
/*  95 */     if (paramObject instanceof ASN1Encodable) {
/*     */       
/*  97 */       ASN1Primitive aSN1Primitive = ((ASN1Encodable)paramObject).toASN1Primitive();
/*     */       
/*  99 */       if (aSN1Primitive instanceof org.bouncycastle.asn1.ASN1Sequence)
/*     */       {
/* 101 */         return (org.bouncycastle.asn1.ASN1Sequence)aSN1Primitive;
/*     */       }
/*     */     } 
/*     */     
/* 105 */     throw new IllegalArgumentException("unknown object in getInstance: " + paramObject.getClass().getName());
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
/*     */   public static org.bouncycastle.asn1.ASN1Sequence getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/* 129 */     if (paramBoolean) {
/*     */       
/* 131 */       if (!paramASN1TaggedObject.isExplicit())
/*     */       {
/* 133 */         throw new IllegalArgumentException("object implicit - explicit expected.");
/*     */       }
/*     */       
/* 136 */       return getInstance(paramASN1TaggedObject.getObject());
/*     */     } 
/*     */     
/* 139 */     ASN1Primitive aSN1Primitive = paramASN1TaggedObject.getObject();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 145 */     if (paramASN1TaggedObject.isExplicit()) {
/*     */       
/* 147 */       if (paramASN1TaggedObject instanceof org.bouncycastle.asn1.BERTaggedObject)
/*     */       {
/* 149 */         return (org.bouncycastle.asn1.ASN1Sequence)new BERSequence((ASN1Encodable)aSN1Primitive);
/*     */       }
/*     */       
/* 152 */       return (org.bouncycastle.asn1.ASN1Sequence)new DLSequence((ASN1Encodable)aSN1Primitive);
/*     */     } 
/*     */     
/* 155 */     if (aSN1Primitive instanceof org.bouncycastle.asn1.ASN1Sequence) {
/*     */       
/* 157 */       org.bouncycastle.asn1.ASN1Sequence aSN1Sequence = (org.bouncycastle.asn1.ASN1Sequence)aSN1Primitive;
/*     */       
/* 159 */       if (paramASN1TaggedObject instanceof org.bouncycastle.asn1.BERTaggedObject)
/*     */       {
/* 161 */         return aSN1Sequence;
/*     */       }
/*     */       
/* 164 */       return (org.bouncycastle.asn1.ASN1Sequence)aSN1Sequence.toDLObject();
/*     */     } 
/*     */     
/* 167 */     throw new IllegalArgumentException("unknown object in getInstance: " + paramASN1TaggedObject.getClass().getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ASN1Sequence() {
/* 175 */     this.elements = ASN1EncodableVector.EMPTY_ELEMENTS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ASN1Sequence(ASN1Encodable paramASN1Encodable) {
/* 184 */     if (null == paramASN1Encodable)
/*     */     {
/* 186 */       throw new NullPointerException("'element' cannot be null");
/*     */     }
/*     */     
/* 189 */     this.elements = new ASN1Encodable[] { paramASN1Encodable };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ASN1Sequence(ASN1EncodableVector paramASN1EncodableVector) {
/* 198 */     if (null == paramASN1EncodableVector)
/*     */     {
/* 200 */       throw new NullPointerException("'elementVector' cannot be null");
/*     */     }
/*     */     
/* 203 */     this.elements = paramASN1EncodableVector.takeElements();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ASN1Sequence(ASN1Encodable[] paramArrayOfASN1Encodable) {
/* 212 */     if (Arrays.isNullOrContainsNull((Object[])paramArrayOfASN1Encodable))
/*     */     {
/* 214 */       throw new NullPointerException("'elements' cannot be null, or contain null");
/*     */     }
/*     */     
/* 217 */     this.elements = ASN1EncodableVector.cloneElements(paramArrayOfASN1Encodable);
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Sequence(ASN1Encodable[] paramArrayOfASN1Encodable, boolean paramBoolean) {
/* 222 */     this.elements = paramBoolean ? ASN1EncodableVector.cloneElements(paramArrayOfASN1Encodable) : paramArrayOfASN1Encodable;
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1Encodable[] toArray() {
/* 227 */     return ASN1EncodableVector.cloneElements(this.elements);
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Encodable[] toArrayInternal() {
/* 232 */     return this.elements;
/*     */   }
/*     */ 
/*     */   
/*     */   public Enumeration getObjects() {
/* 237 */     return (Enumeration)new Object(this);
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
/*     */   public ASN1SequenceParser parser() {
/* 260 */     int i = size();
/*     */     
/* 262 */     return (ASN1SequenceParser)new Object(this, i);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1Encodable getObjectAt(int paramInt) {
/* 306 */     return this.elements[paramInt];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 316 */     return this.elements.length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 322 */     int i = this.elements.length;
/* 323 */     int j = i + 1;
/*     */     
/* 325 */     while (--i >= 0) {
/*     */       
/* 327 */       j *= 257;
/* 328 */       j ^= this.elements[i].toASN1Primitive().hashCode();
/*     */     } 
/*     */     
/* 331 */     return j;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 336 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.ASN1Sequence))
/*     */     {
/* 338 */       return false;
/*     */     }
/*     */     
/* 341 */     org.bouncycastle.asn1.ASN1Sequence aSN1Sequence = (org.bouncycastle.asn1.ASN1Sequence)paramASN1Primitive;
/*     */     
/* 343 */     int i = size();
/* 344 */     if (aSN1Sequence.size() != i)
/*     */     {
/* 346 */       return false;
/*     */     }
/*     */     
/* 349 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 351 */       ASN1Primitive aSN1Primitive1 = this.elements[b].toASN1Primitive();
/* 352 */       ASN1Primitive aSN1Primitive2 = aSN1Sequence.elements[b].toASN1Primitive();
/*     */       
/* 354 */       if (aSN1Primitive1 != aSN1Primitive2 && !aSN1Primitive1.asn1Equals(aSN1Primitive2))
/*     */       {
/* 356 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 360 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ASN1Primitive toDERObject() {
/* 369 */     return (ASN1Primitive)new DERSequence(this.elements, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ASN1Primitive toDLObject() {
/* 378 */     return (ASN1Primitive)new DLSequence(this.elements, false);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/* 383 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   abstract void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException;
/*     */ 
/*     */   
/*     */   public String toString() {
/* 391 */     int i = size();
/* 392 */     if (0 == i)
/*     */     {
/* 394 */       return "[]";
/*     */     }
/*     */     
/* 397 */     StringBuffer stringBuffer = new StringBuffer();
/* 398 */     stringBuffer.append('[');
/* 399 */     byte b = 0;
/*     */     while (true) {
/* 401 */       stringBuffer.append(this.elements[b]);
/* 402 */       if (++b >= i) {
/*     */         break;
/*     */       }
/*     */       
/* 406 */       stringBuffer.append(", ");
/*     */     } 
/* 408 */     stringBuffer.append(']');
/* 409 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<ASN1Encodable> iterator() {
/* 414 */     return (Iterator<ASN1Encodable>)new Arrays.Iterator((Object[])this.elements);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1Sequence.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */