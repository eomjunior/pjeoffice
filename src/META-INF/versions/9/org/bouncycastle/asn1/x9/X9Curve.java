/*     */ package META-INF.versions.9.org.bouncycastle.asn1.x9;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1Integer;
/*     */ import org.bouncycastle.asn1.ASN1Object;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.DERBitString;
/*     */ import org.bouncycastle.asn1.DERSequence;
/*     */ import org.bouncycastle.asn1.x9.X9FieldElement;
/*     */ import org.bouncycastle.asn1.x9.X9FieldID;
/*     */ import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
/*     */ import org.bouncycastle.math.ec.ECAlgorithms;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ 
/*     */ public class X9Curve
/*     */   extends ASN1Object
/*     */   implements X9ObjectIdentifiers
/*     */ {
/*     */   private ECCurve curve;
/*     */   private byte[] seed;
/*  28 */   private ASN1ObjectIdentifier fieldIdentifier = null;
/*     */ 
/*     */ 
/*     */   
/*     */   public X9Curve(ECCurve paramECCurve) {
/*  33 */     this(paramECCurve, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public X9Curve(ECCurve paramECCurve, byte[] paramArrayOfbyte) {
/*  40 */     this.curve = paramECCurve;
/*  41 */     this.seed = Arrays.clone(paramArrayOfbyte);
/*  42 */     setFieldIdentifier();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public X9Curve(X9FieldID paramX9FieldID, BigInteger paramBigInteger1, BigInteger paramBigInteger2, ASN1Sequence paramASN1Sequence) {
/*  51 */     this.fieldIdentifier = paramX9FieldID.getIdentifier();
/*  52 */     if (this.fieldIdentifier.equals((ASN1Primitive)prime_field)) {
/*     */       
/*  54 */       BigInteger bigInteger1 = ((ASN1Integer)paramX9FieldID.getParameters()).getValue();
/*  55 */       BigInteger bigInteger2 = new BigInteger(1, ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(0)).getOctets());
/*  56 */       BigInteger bigInteger3 = new BigInteger(1, ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(1)).getOctets());
/*  57 */       this.curve = (ECCurve)new ECCurve.Fp(bigInteger1, bigInteger2, bigInteger3, paramBigInteger1, paramBigInteger2);
/*     */     }
/*  59 */     else if (this.fieldIdentifier.equals((ASN1Primitive)characteristic_two_field)) {
/*     */ 
/*     */       
/*  62 */       ASN1Sequence aSN1Sequence = ASN1Sequence.getInstance(paramX9FieldID.getParameters());
/*  63 */       int i = ((ASN1Integer)aSN1Sequence.getObjectAt(0)).intValueExact();
/*  64 */       ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)aSN1Sequence.getObjectAt(1);
/*     */       
/*  66 */       int j = 0;
/*  67 */       int k = 0;
/*  68 */       int m = 0;
/*     */       
/*  70 */       if (aSN1ObjectIdentifier.equals((ASN1Primitive)tpBasis)) {
/*     */ 
/*     */         
/*  73 */         j = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(2)).intValueExact();
/*     */       }
/*  75 */       else if (aSN1ObjectIdentifier.equals((ASN1Primitive)ppBasis)) {
/*     */ 
/*     */         
/*  78 */         ASN1Sequence aSN1Sequence1 = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(2));
/*  79 */         j = ASN1Integer.getInstance(aSN1Sequence1.getObjectAt(0)).intValueExact();
/*  80 */         k = ASN1Integer.getInstance(aSN1Sequence1.getObjectAt(1)).intValueExact();
/*  81 */         m = ASN1Integer.getInstance(aSN1Sequence1.getObjectAt(2)).intValueExact();
/*     */       }
/*     */       else {
/*     */         
/*  85 */         throw new IllegalArgumentException("This type of EC basis is not implemented");
/*     */       } 
/*  87 */       BigInteger bigInteger1 = new BigInteger(1, ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(0)).getOctets());
/*  88 */       BigInteger bigInteger2 = new BigInteger(1, ASN1OctetString.getInstance(paramASN1Sequence.getObjectAt(1)).getOctets());
/*  89 */       this.curve = (ECCurve)new ECCurve.F2m(i, j, k, m, bigInteger1, bigInteger2, paramBigInteger1, paramBigInteger2);
/*     */     }
/*     */     else {
/*     */       
/*  93 */       throw new IllegalArgumentException("This type of ECCurve is not implemented");
/*     */     } 
/*     */     
/*  96 */     if (paramASN1Sequence.size() == 3)
/*     */     {
/*  98 */       this.seed = ((DERBitString)paramASN1Sequence.getObjectAt(2)).getBytes();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void setFieldIdentifier() {
/* 104 */     if (ECAlgorithms.isFpCurve(this.curve)) {
/*     */       
/* 106 */       this.fieldIdentifier = prime_field;
/*     */     }
/* 108 */     else if (ECAlgorithms.isF2mCurve(this.curve)) {
/*     */       
/* 110 */       this.fieldIdentifier = characteristic_two_field;
/*     */     }
/*     */     else {
/*     */       
/* 114 */       throw new IllegalArgumentException("This type of ECCurve is not implemented");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ECCurve getCurve() {
/* 120 */     return this.curve;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getSeed() {
/* 125 */     return Arrays.clone(this.seed);
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
/*     */   public ASN1Primitive toASN1Primitive() {
/* 140 */     ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(3);
/*     */     
/* 142 */     if (this.fieldIdentifier.equals((ASN1Primitive)prime_field)) {
/*     */       
/* 144 */       aSN1EncodableVector.add((ASN1Encodable)(new X9FieldElement(this.curve.getA())).toASN1Primitive());
/* 145 */       aSN1EncodableVector.add((ASN1Encodable)(new X9FieldElement(this.curve.getB())).toASN1Primitive());
/*     */     }
/* 147 */     else if (this.fieldIdentifier.equals((ASN1Primitive)characteristic_two_field)) {
/*     */       
/* 149 */       aSN1EncodableVector.add((ASN1Encodable)(new X9FieldElement(this.curve.getA())).toASN1Primitive());
/* 150 */       aSN1EncodableVector.add((ASN1Encodable)(new X9FieldElement(this.curve.getB())).toASN1Primitive());
/*     */     } 
/*     */     
/* 153 */     if (this.seed != null)
/*     */     {
/* 155 */       aSN1EncodableVector.add((ASN1Encodable)new DERBitString(this.seed));
/*     */     }
/*     */     
/* 158 */     return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/x9/X9Curve.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */