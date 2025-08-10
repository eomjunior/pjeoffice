/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1Integer;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.DERExternal;
/*     */ import org.bouncycastle.asn1.DERTaggedObject;
/*     */ import org.bouncycastle.asn1.DLExternal;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ASN1External
/*     */   extends ASN1Primitive
/*     */ {
/*     */   protected ASN1ObjectIdentifier directReference;
/*     */   protected ASN1Integer indirectReference;
/*     */   protected ASN1Primitive dataValueDescriptor;
/*     */   protected int encoding;
/*     */   protected ASN1Primitive externalContent;
/*     */   
/*     */   public ASN1External(ASN1EncodableVector paramASN1EncodableVector) {
/*  31 */     byte b = 0;
/*     */     
/*  33 */     ASN1Primitive aSN1Primitive = getObjFromVector(paramASN1EncodableVector, b);
/*  34 */     if (aSN1Primitive instanceof ASN1ObjectIdentifier) {
/*     */       
/*  36 */       this.directReference = (ASN1ObjectIdentifier)aSN1Primitive;
/*  37 */       b++;
/*  38 */       aSN1Primitive = getObjFromVector(paramASN1EncodableVector, b);
/*     */     } 
/*  40 */     if (aSN1Primitive instanceof ASN1Integer) {
/*     */       
/*  42 */       this.indirectReference = (ASN1Integer)aSN1Primitive;
/*  43 */       b++;
/*  44 */       aSN1Primitive = getObjFromVector(paramASN1EncodableVector, b);
/*     */     } 
/*  46 */     if (!(aSN1Primitive instanceof ASN1TaggedObject)) {
/*     */       
/*  48 */       this.dataValueDescriptor = aSN1Primitive;
/*  49 */       b++;
/*  50 */       aSN1Primitive = getObjFromVector(paramASN1EncodableVector, b);
/*     */     } 
/*     */     
/*  53 */     if (paramASN1EncodableVector.size() != b + 1)
/*     */     {
/*  55 */       throw new IllegalArgumentException("input vector too large");
/*     */     }
/*     */     
/*  58 */     if (!(aSN1Primitive instanceof ASN1TaggedObject))
/*     */     {
/*  60 */       throw new IllegalArgumentException("No tagged object found in vector. Structure doesn't seem to be of type External");
/*     */     }
/*  62 */     ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject)aSN1Primitive;
/*  63 */     setEncoding(aSN1TaggedObject.getTagNo());
/*  64 */     this.externalContent = aSN1TaggedObject.getObject();
/*     */   }
/*     */ 
/*     */   
/*     */   private ASN1Primitive getObjFromVector(ASN1EncodableVector paramASN1EncodableVector, int paramInt) {
/*  69 */     if (paramASN1EncodableVector.size() <= paramInt)
/*     */     {
/*  71 */       throw new IllegalArgumentException("too few objects in input vector");
/*     */     }
/*     */     
/*  74 */     return paramASN1EncodableVector.get(paramInt).toASN1Primitive();
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
/*     */   public ASN1External(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ASN1Integer paramASN1Integer, ASN1Primitive paramASN1Primitive, DERTaggedObject paramDERTaggedObject) {
/*  87 */     this(paramASN1ObjectIdentifier, paramASN1Integer, paramASN1Primitive, paramDERTaggedObject.getTagNo(), paramDERTaggedObject.toASN1Primitive());
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
/*     */   public ASN1External(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ASN1Integer paramASN1Integer, ASN1Primitive paramASN1Primitive1, int paramInt, ASN1Primitive paramASN1Primitive2) {
/* 101 */     setDirectReference(paramASN1ObjectIdentifier);
/* 102 */     setIndirectReference(paramASN1Integer);
/* 103 */     setDataValueDescriptor(paramASN1Primitive1);
/* 104 */     setEncoding(paramInt);
/* 105 */     setExternalContent(paramASN1Primitive2.toASN1Primitive());
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDERObject() {
/* 110 */     return (ASN1Primitive)new DERExternal(this.directReference, this.indirectReference, this.dataValueDescriptor, this.encoding, this.externalContent);
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDLObject() {
/* 115 */     return (ASN1Primitive)new DLExternal(this.directReference, this.indirectReference, this.dataValueDescriptor, this.encoding, this.externalContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 123 */     int i = 0;
/* 124 */     if (this.directReference != null)
/*     */     {
/* 126 */       i = this.directReference.hashCode();
/*     */     }
/* 128 */     if (this.indirectReference != null)
/*     */     {
/* 130 */       i ^= this.indirectReference.hashCode();
/*     */     }
/* 132 */     if (this.dataValueDescriptor != null)
/*     */     {
/* 134 */       i ^= this.dataValueDescriptor.hashCode();
/*     */     }
/* 136 */     i ^= this.externalContent.hashCode();
/* 137 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isConstructed() {
/* 142 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int encodedLength() throws IOException {
/* 148 */     return (getEncoded()).length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/* 156 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.ASN1External))
/*     */     {
/* 158 */       return false;
/*     */     }
/* 160 */     if (this == paramASN1Primitive)
/*     */     {
/* 162 */       return true;
/*     */     }
/* 164 */     org.bouncycastle.asn1.ASN1External aSN1External = (org.bouncycastle.asn1.ASN1External)paramASN1Primitive;
/* 165 */     if (this.directReference != null)
/*     */     {
/* 167 */       if (aSN1External.directReference == null || !aSN1External.directReference.equals((ASN1Primitive)this.directReference))
/*     */       {
/* 169 */         return false;
/*     */       }
/*     */     }
/* 172 */     if (this.indirectReference != null)
/*     */     {
/* 174 */       if (aSN1External.indirectReference == null || !aSN1External.indirectReference.equals((ASN1Primitive)this.indirectReference))
/*     */       {
/* 176 */         return false;
/*     */       }
/*     */     }
/* 179 */     if (this.dataValueDescriptor != null)
/*     */     {
/* 181 */       if (aSN1External.dataValueDescriptor == null || !aSN1External.dataValueDescriptor.equals(this.dataValueDescriptor))
/*     */       {
/* 183 */         return false;
/*     */       }
/*     */     }
/* 186 */     return this.externalContent.equals(aSN1External.externalContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1Primitive getDataValueDescriptor() {
/* 195 */     return this.dataValueDescriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1ObjectIdentifier getDirectReference() {
/* 204 */     return this.directReference;
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
/*     */   public int getEncoding() {
/* 218 */     return this.encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1Primitive getExternalContent() {
/* 227 */     return this.externalContent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1Integer getIndirectReference() {
/* 236 */     return this.indirectReference;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setDataValueDescriptor(ASN1Primitive paramASN1Primitive) {
/* 245 */     this.dataValueDescriptor = paramASN1Primitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setDirectReference(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 254 */     this.directReference = paramASN1ObjectIdentifier;
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
/*     */   private void setEncoding(int paramInt) {
/* 268 */     if (paramInt < 0 || paramInt > 2)
/*     */     {
/* 270 */       throw new IllegalArgumentException("invalid encoding value: " + paramInt);
/*     */     }
/* 272 */     this.encoding = paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setExternalContent(ASN1Primitive paramASN1Primitive) {
/* 281 */     this.externalContent = paramASN1Primitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setIndirectReference(ASN1Integer paramASN1Integer) {
/* 290 */     this.indirectReference = paramASN1Integer;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1External.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */