/*     */ package META-INF.versions.9.org.bouncycastle.asn1.x9;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1Integer;
/*     */ import org.bouncycastle.asn1.ASN1Object;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.DERSequence;
/*     */ import org.bouncycastle.asn1.x9.X9Curve;
/*     */ import org.bouncycastle.asn1.x9.X9ECPoint;
/*     */ import org.bouncycastle.asn1.x9.X9FieldID;
/*     */ import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
/*     */ import org.bouncycastle.math.ec.ECAlgorithms;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.field.PolynomialExtensionField;
/*     */ import org.bouncycastle.util.Arrays;
/*     */ 
/*     */ public class X9ECParameters
/*     */   extends ASN1Object
/*     */   implements X9ObjectIdentifiers
/*     */ {
/*  26 */   private static final BigInteger ONE = BigInteger.valueOf(1L);
/*     */   
/*     */   private X9FieldID fieldID;
/*     */   
/*     */   private ECCurve curve;
/*     */   
/*     */   private X9ECPoint g;
/*     */   private BigInteger n;
/*     */   private BigInteger h;
/*     */   private byte[] seed;
/*     */   
/*     */   private X9ECParameters(ASN1Sequence paramASN1Sequence) {
/*  38 */     if (!(paramASN1Sequence.getObjectAt(0) instanceof ASN1Integer) || 
/*  39 */       !((ASN1Integer)paramASN1Sequence.getObjectAt(0)).hasValue(ONE))
/*     */     {
/*  41 */       throw new IllegalArgumentException("bad version in X9ECParameters");
/*     */     }
/*     */     
/*  44 */     this.n = ((ASN1Integer)paramASN1Sequence.getObjectAt(4)).getValue();
/*     */     
/*  46 */     if (paramASN1Sequence.size() == 6)
/*     */     {
/*  48 */       this.h = ((ASN1Integer)paramASN1Sequence.getObjectAt(5)).getValue();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  53 */     X9Curve x9Curve = new X9Curve(X9FieldID.getInstance(paramASN1Sequence.getObjectAt(1)), this.n, this.h, ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(2)));
/*     */     
/*  55 */     this.curve = x9Curve.getCurve();
/*  56 */     ASN1Encodable aSN1Encodable = paramASN1Sequence.getObjectAt(3);
/*     */     
/*  58 */     if (aSN1Encodable instanceof X9ECPoint) {
/*     */       
/*  60 */       this.g = (X9ECPoint)aSN1Encodable;
/*     */     }
/*     */     else {
/*     */       
/*  64 */       this.g = new X9ECPoint(this.curve, (ASN1OctetString)aSN1Encodable);
/*     */     } 
/*     */     
/*  67 */     this.seed = x9Curve.getSeed();
/*     */   }
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.asn1.x9.X9ECParameters getInstance(Object paramObject) {
/*  72 */     if (paramObject instanceof org.bouncycastle.asn1.x9.X9ECParameters)
/*     */     {
/*  74 */       return (org.bouncycastle.asn1.x9.X9ECParameters)paramObject;
/*     */     }
/*     */     
/*  77 */     if (paramObject != null)
/*     */     {
/*  79 */       return new org.bouncycastle.asn1.x9.X9ECParameters(ASN1Sequence.getInstance(paramObject));
/*     */     }
/*     */     
/*  82 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public X9ECParameters(ECCurve paramECCurve, X9ECPoint paramX9ECPoint, BigInteger paramBigInteger) {
/*  90 */     this(paramECCurve, paramX9ECPoint, paramBigInteger, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public X9ECParameters(ECCurve paramECCurve, X9ECPoint paramX9ECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/*  99 */     this(paramECCurve, paramX9ECPoint, paramBigInteger1, paramBigInteger2, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public X9ECParameters(ECCurve paramECCurve, X9ECPoint paramX9ECPoint, BigInteger paramBigInteger1, BigInteger paramBigInteger2, byte[] paramArrayOfbyte) {
/* 109 */     this.curve = paramECCurve;
/* 110 */     this.g = paramX9ECPoint;
/* 111 */     this.n = paramBigInteger1;
/* 112 */     this.h = paramBigInteger2;
/* 113 */     this.seed = Arrays.clone(paramArrayOfbyte);
/*     */     
/* 115 */     if (ECAlgorithms.isFpCurve(paramECCurve)) {
/*     */       
/* 117 */       this.fieldID = new X9FieldID(paramECCurve.getField().getCharacteristic());
/*     */     }
/* 119 */     else if (ECAlgorithms.isF2mCurve(paramECCurve)) {
/*     */       
/* 121 */       PolynomialExtensionField polynomialExtensionField = (PolynomialExtensionField)paramECCurve.getField();
/* 122 */       int[] arrayOfInt = polynomialExtensionField.getMinimalPolynomial().getExponentsPresent();
/* 123 */       if (arrayOfInt.length == 3)
/*     */       {
/* 125 */         this.fieldID = new X9FieldID(arrayOfInt[2], arrayOfInt[1]);
/*     */       }
/* 127 */       else if (arrayOfInt.length == 5)
/*     */       {
/* 129 */         this.fieldID = new X9FieldID(arrayOfInt[4], arrayOfInt[1], arrayOfInt[2], arrayOfInt[3]);
/*     */       }
/*     */       else
/*     */       {
/* 133 */         throw new IllegalArgumentException("Only trinomial and pentomial curves are supported");
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 138 */       throw new IllegalArgumentException("'curve' is of an unsupported type");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ECCurve getCurve() {
/* 144 */     return this.curve;
/*     */   }
/*     */ 
/*     */   
/*     */   public ECPoint getG() {
/* 149 */     return this.g.getPoint();
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger getN() {
/* 154 */     return this.n;
/*     */   }
/*     */ 
/*     */   
/*     */   public BigInteger getH() {
/* 159 */     return this.h;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getSeed() {
/* 164 */     return Arrays.clone(this.seed);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSeed() {
/* 169 */     return (null != this.seed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public X9Curve getCurveEntry() {
/* 179 */     return new X9Curve(this.curve, this.seed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public X9FieldID getFieldIDEntry() {
/* 189 */     return this.fieldID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public X9ECPoint getBaseEntry() {
/* 199 */     return this.g;
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
/*     */   public ASN1Primitive toASN1Primitive() {
/* 217 */     ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(6);
/*     */     
/* 219 */     aSN1EncodableVector.add((ASN1Encodable)new ASN1Integer(ONE));
/* 220 */     aSN1EncodableVector.add((ASN1Encodable)this.fieldID);
/* 221 */     aSN1EncodableVector.add((ASN1Encodable)new X9Curve(this.curve, this.seed));
/* 222 */     aSN1EncodableVector.add((ASN1Encodable)this.g);
/* 223 */     aSN1EncodableVector.add((ASN1Encodable)new ASN1Integer(this.n));
/*     */     
/* 225 */     if (this.h != null)
/*     */     {
/* 227 */       aSN1EncodableVector.add((ASN1Encodable)new ASN1Integer(this.h));
/*     */     }
/*     */     
/* 230 */     return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/x9/X9ECParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */