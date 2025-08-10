/*     */ package META-INF.versions.9.org.bouncycastle.asn1;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1Exception;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.ASN1Set;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObjectParser;
/*     */ import org.bouncycastle.asn1.DERTaggedObject;
/*     */ import org.bouncycastle.asn1.DLTaggedObject;
/*     */ 
/*     */ public abstract class ASN1TaggedObject
/*     */   extends ASN1Primitive implements ASN1TaggedObjectParser {
/*     */   final int tagNo;
/*     */   final boolean explicit;
/*     */   final ASN1Encodable obj;
/*     */   
/*     */   public static org.bouncycastle.asn1.ASN1TaggedObject getInstance(org.bouncycastle.asn1.ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  22 */     if (paramBoolean)
/*     */     {
/*  24 */       return getInstance(paramASN1TaggedObject.getObject());
/*     */     }
/*     */     
/*  27 */     throw new IllegalArgumentException("implicitly tagged tagged object");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.asn1.ASN1TaggedObject getInstance(Object paramObject) {
/*  33 */     if (paramObject == null || paramObject instanceof org.bouncycastle.asn1.ASN1TaggedObject)
/*     */     {
/*  35 */       return (org.bouncycastle.asn1.ASN1TaggedObject)paramObject;
/*     */     }
/*  37 */     if (paramObject instanceof byte[]) {
/*     */       
/*     */       try {
/*     */         
/*  41 */         return getInstance(fromByteArray((byte[])paramObject));
/*     */       }
/*  43 */       catch (IOException iOException) {
/*     */         
/*  45 */         throw new IllegalArgumentException("failed to construct tagged object from byte[]: " + iOException.getMessage());
/*     */       } 
/*     */     }
/*     */     
/*  49 */     throw new IllegalArgumentException("unknown object in getInstance: " + paramObject.getClass().getName());
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
/*     */   public ASN1TaggedObject(boolean paramBoolean, int paramInt, ASN1Encodable paramASN1Encodable) {
/*  67 */     if (null == paramASN1Encodable)
/*     */     {
/*  69 */       throw new NullPointerException("'obj' cannot be null");
/*     */     }
/*     */     
/*  72 */     this.tagNo = paramInt;
/*  73 */     this.explicit = (paramBoolean || paramASN1Encodable instanceof org.bouncycastle.asn1.ASN1Choice);
/*  74 */     this.obj = paramASN1Encodable;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean asn1Equals(ASN1Primitive paramASN1Primitive) {
/*  79 */     if (!(paramASN1Primitive instanceof org.bouncycastle.asn1.ASN1TaggedObject))
/*     */     {
/*  81 */       return false;
/*     */     }
/*     */     
/*  84 */     org.bouncycastle.asn1.ASN1TaggedObject aSN1TaggedObject = (org.bouncycastle.asn1.ASN1TaggedObject)paramASN1Primitive;
/*     */     
/*  86 */     if (this.tagNo != aSN1TaggedObject.tagNo || this.explicit != aSN1TaggedObject.explicit)
/*     */     {
/*  88 */       return false;
/*     */     }
/*     */     
/*  91 */     ASN1Primitive aSN1Primitive1 = this.obj.toASN1Primitive();
/*  92 */     ASN1Primitive aSN1Primitive2 = aSN1TaggedObject.obj.toASN1Primitive();
/*     */     
/*  94 */     return (aSN1Primitive1 == aSN1Primitive2 || aSN1Primitive1.asn1Equals(aSN1Primitive2));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  99 */     return this.tagNo ^ (this.explicit ? 15 : 240) ^ this.obj.toASN1Primitive().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTagNo() {
/* 109 */     return this.tagNo;
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
/*     */   public boolean isExplicit() {
/* 123 */     return this.explicit;
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
/*     */   public ASN1Primitive getObject() {
/* 135 */     return this.obj.toASN1Primitive();
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
/*     */   public ASN1Encodable getObjectParser(int paramInt, boolean paramBoolean) throws IOException {
/* 148 */     switch (paramInt) {
/*     */       
/*     */       case 17:
/* 151 */         return (ASN1Encodable)ASN1Set.getInstance(this, paramBoolean).parser();
/*     */       case 16:
/* 153 */         return (ASN1Encodable)ASN1Sequence.getInstance(this, paramBoolean).parser();
/*     */       case 4:
/* 155 */         return (ASN1Encodable)ASN1OctetString.getInstance(this, paramBoolean).parser();
/*     */     } 
/*     */     
/* 158 */     if (paramBoolean)
/*     */     {
/* 160 */       return (ASN1Encodable)getObject();
/*     */     }
/*     */     
/* 163 */     throw new ASN1Exception("implicit tagging not implemented for tag: " + paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1Primitive getLoadedObject() {
/* 168 */     return toASN1Primitive();
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDERObject() {
/* 173 */     return (ASN1Primitive)new DERTaggedObject(this.explicit, this.tagNo, this.obj);
/*     */   }
/*     */ 
/*     */   
/*     */   ASN1Primitive toDLObject() {
/* 178 */     return (ASN1Primitive)new DLTaggedObject(this.explicit, this.tagNo, this.obj);
/*     */   }
/*     */ 
/*     */   
/*     */   abstract void encode(ASN1OutputStream paramASN1OutputStream, boolean paramBoolean) throws IOException;
/*     */   
/*     */   public String toString() {
/* 185 */     return "[" + this.tagNo + "]" + this.obj;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1TaggedObject.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */