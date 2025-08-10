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
/*     */ import org.bouncycastle.asn1.LazyConstructionEnumeration;
/*     */ import org.bouncycastle.asn1.StreamUtil;
/*     */ 
/*     */ class LazyEncodedSequence
/*     */   extends ASN1Sequence
/*     */ {
/*     */   private byte[] encoded;
/*     */   
/*     */   LazyEncodedSequence(byte[] paramArrayOfbyte) throws IOException {
/*  20 */     this.encoded = paramArrayOfbyte;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ASN1Encodable getObjectAt(int paramInt) {
/*  25 */     force();
/*     */     
/*  27 */     return super.getObjectAt(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Enumeration getObjects() {
/*  32 */     if (null != this.encoded)
/*     */     {
/*  34 */       return (Enumeration)new LazyConstructionEnumeration(this.encoded);
/*     */     }
/*     */     
/*  37 */     return super.getObjects();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int hashCode() {
/*  42 */     force();
/*     */     
/*  44 */     return super.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Iterator<ASN1Encodable> iterator() {
/*  49 */     force();
/*     */     
/*  51 */     return super.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int size() {
/*  56 */     force();
/*     */     
/*  58 */     return super.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ASN1Encodable[] toArray() {
/*  63 */     force();
/*     */     
/*  65 */     return super.toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Encodable[] toArrayInternal() {
/*  70 */     force();
/*     */     
/*  72 */     return super.toArrayInternal();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized int encodedLength() throws IOException {
/*  78 */     if (null != this.encoded)
/*     */     {
/*  80 */       return 1 + StreamUtil.calculateBodyLength(this.encoded.length) + this.encoded.length;
/*     */     }
/*     */     
/*  83 */     return super.toDLObject().encodedLength();
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException {
/*  88 */     if (null != this.encoded) {
/*     */       
/*  90 */       paramASN1OutputStream.writeEncoded(paramBoolean, 48, this.encoded);
/*     */     }
/*     */     else {
/*     */       
/*  94 */       super.toDLObject().encode(paramASN1OutputStream, paramBoolean);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized ASN1Primitive toDERObject() {
/* 100 */     force();
/*     */     
/* 102 */     return super.toDERObject();
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized ASN1Primitive toDLObject() {
/* 107 */     force();
/*     */     
/* 109 */     return super.toDLObject();
/*     */   }
/*     */ 
/*     */   
/*     */   private void force() {
/* 114 */     if (null != this.encoded) {
/*     */       
/* 116 */       ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
/*     */       
/* 118 */       LazyConstructionEnumeration<ASN1Primitive> lazyConstructionEnumeration = new LazyConstructionEnumeration(this.encoded);
/* 119 */       while (lazyConstructionEnumeration.hasMoreElements())
/*     */       {
/* 121 */         aSN1EncodableVector.add((ASN1Encodable)lazyConstructionEnumeration.nextElement());
/*     */       }
/*     */       
/* 124 */       this.elements = aSN1EncodableVector.takeElements();
/* 125 */       this.encoded = null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/LazyEncodedSequence.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */