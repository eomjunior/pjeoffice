/*     */ package META-INF.versions.9.org.bouncycastle.asn1.x9;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1Integer;
/*     */ import org.bouncycastle.asn1.ASN1Object;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.DERSequence;
/*     */ import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class X9FieldID
/*     */   extends ASN1Object
/*     */   implements X9ObjectIdentifiers
/*     */ {
/*     */   private ASN1ObjectIdentifier id;
/*     */   private ASN1Primitive parameters;
/*     */   
/*     */   public X9FieldID(BigInteger paramBigInteger) {
/*  31 */     this.id = prime_field;
/*  32 */     this.parameters = (ASN1Primitive)new ASN1Integer(paramBigInteger);
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
/*     */   public X9FieldID(int paramInt1, int paramInt2) {
/*  46 */     this(paramInt1, paramInt2, 0, 0);
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
/*     */   public X9FieldID(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  66 */     this.id = characteristic_two_field;
/*  67 */     ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(3);
/*  68 */     aSN1EncodableVector.add((ASN1Encodable)new ASN1Integer(paramInt1));
/*     */     
/*  70 */     if (paramInt3 == 0) {
/*     */       
/*  72 */       if (paramInt4 != 0)
/*     */       {
/*  74 */         throw new IllegalArgumentException("inconsistent k values");
/*     */       }
/*     */       
/*  77 */       aSN1EncodableVector.add((ASN1Encodable)tpBasis);
/*  78 */       aSN1EncodableVector.add((ASN1Encodable)new ASN1Integer(paramInt2));
/*     */     }
/*     */     else {
/*     */       
/*  82 */       if (paramInt3 <= paramInt2 || paramInt4 <= paramInt3)
/*     */       {
/*  84 */         throw new IllegalArgumentException("inconsistent k values");
/*     */       }
/*     */       
/*  87 */       aSN1EncodableVector.add((ASN1Encodable)ppBasis);
/*  88 */       ASN1EncodableVector aSN1EncodableVector1 = new ASN1EncodableVector(3);
/*  89 */       aSN1EncodableVector1.add((ASN1Encodable)new ASN1Integer(paramInt2));
/*  90 */       aSN1EncodableVector1.add((ASN1Encodable)new ASN1Integer(paramInt3));
/*  91 */       aSN1EncodableVector1.add((ASN1Encodable)new ASN1Integer(paramInt4));
/*  92 */       aSN1EncodableVector.add((ASN1Encodable)new DERSequence(aSN1EncodableVector1));
/*     */     } 
/*     */     
/*  95 */     this.parameters = (ASN1Primitive)new DERSequence(aSN1EncodableVector);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private X9FieldID(ASN1Sequence paramASN1Sequence) {
/* 101 */     this.id = ASN1ObjectIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
/* 102 */     this.parameters = paramASN1Sequence.getObjectAt(1).toASN1Primitive();
/*     */   }
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.asn1.x9.X9FieldID getInstance(Object paramObject) {
/* 107 */     if (paramObject instanceof org.bouncycastle.asn1.x9.X9FieldID)
/*     */     {
/* 109 */       return (org.bouncycastle.asn1.x9.X9FieldID)paramObject;
/*     */     }
/*     */     
/* 112 */     if (paramObject != null)
/*     */     {
/* 114 */       return new org.bouncycastle.asn1.x9.X9FieldID(ASN1Sequence.getInstance(paramObject));
/*     */     }
/*     */     
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1ObjectIdentifier getIdentifier() {
/* 122 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1Primitive getParameters() {
/* 127 */     return this.parameters;
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
/*     */   public ASN1Primitive toASN1Primitive() {
/* 141 */     ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(2);
/*     */     
/* 143 */     aSN1EncodableVector.add((ASN1Encodable)this.id);
/* 144 */     aSN1EncodableVector.add((ASN1Encodable)this.parameters);
/*     */     
/* 146 */     return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/x9/X9FieldID.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */