/*     */ package META-INF.versions.9.org.bouncycastle.pqc.asn1;
/*     */ 
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1Integer;
/*     */ import org.bouncycastle.asn1.ASN1Object;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.DEROctetString;
/*     */ import org.bouncycastle.asn1.DERSequence;
/*     */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.Permutation;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class McElieceCCA2PrivateKey
/*     */   extends ASN1Object
/*     */ {
/*     */   private int n;
/*     */   private int k;
/*     */   private byte[] encField;
/*     */   private byte[] encGp;
/*     */   private byte[] encP;
/*     */   private AlgorithmIdentifier digest;
/*     */   
/*     */   public McElieceCCA2PrivateKey(int paramInt1, int paramInt2, GF2mField paramGF2mField, PolynomialGF2mSmallM paramPolynomialGF2mSmallM, Permutation paramPermutation, AlgorithmIdentifier paramAlgorithmIdentifier) {
/*  47 */     this.n = paramInt1;
/*  48 */     this.k = paramInt2;
/*  49 */     this.encField = paramGF2mField.getEncoded();
/*  50 */     this.encGp = paramPolynomialGF2mSmallM.getEncoded();
/*  51 */     this.encP = paramPermutation.getEncoded();
/*  52 */     this.digest = paramAlgorithmIdentifier;
/*     */   }
/*     */ 
/*     */   
/*     */   private McElieceCCA2PrivateKey(ASN1Sequence paramASN1Sequence) {
/*  57 */     this.n = ((ASN1Integer)paramASN1Sequence.getObjectAt(0)).intValueExact();
/*     */     
/*  59 */     this.k = ((ASN1Integer)paramASN1Sequence.getObjectAt(1)).intValueExact();
/*     */     
/*  61 */     this.encField = ((ASN1OctetString)paramASN1Sequence.getObjectAt(2)).getOctets();
/*     */     
/*  63 */     this.encGp = ((ASN1OctetString)paramASN1Sequence.getObjectAt(3)).getOctets();
/*     */     
/*  65 */     this.encP = ((ASN1OctetString)paramASN1Sequence.getObjectAt(4)).getOctets();
/*     */     
/*  67 */     this.digest = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(5));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getN() {
/*  72 */     return this.n;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK() {
/*  77 */     return this.k;
/*     */   }
/*     */ 
/*     */   
/*     */   public GF2mField getField() {
/*  82 */     return new GF2mField(this.encField);
/*     */   }
/*     */ 
/*     */   
/*     */   public PolynomialGF2mSmallM getGoppaPoly() {
/*  87 */     return new PolynomialGF2mSmallM(getField(), this.encGp);
/*     */   }
/*     */ 
/*     */   
/*     */   public Permutation getP() {
/*  92 */     return new Permutation(this.encP);
/*     */   }
/*     */ 
/*     */   
/*     */   public AlgorithmIdentifier getDigest() {
/*  97 */     return this.digest;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1Primitive toASN1Primitive() {
/* 103 */     ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
/*     */ 
/*     */     
/* 106 */     aSN1EncodableVector.add((ASN1Encodable)new ASN1Integer(this.n));
/*     */ 
/*     */     
/* 109 */     aSN1EncodableVector.add((ASN1Encodable)new ASN1Integer(this.k));
/*     */ 
/*     */     
/* 112 */     aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(this.encField));
/*     */ 
/*     */     
/* 115 */     aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(this.encGp));
/*     */ 
/*     */     
/* 118 */     aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(this.encP));
/*     */     
/* 120 */     aSN1EncodableVector.add((ASN1Encodable)this.digest);
/*     */     
/* 122 */     return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
/*     */   }
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.pqc.asn1.McElieceCCA2PrivateKey getInstance(Object paramObject) {
/* 127 */     if (paramObject instanceof org.bouncycastle.pqc.asn1.McElieceCCA2PrivateKey)
/*     */     {
/* 129 */       return (org.bouncycastle.pqc.asn1.McElieceCCA2PrivateKey)paramObject;
/*     */     }
/* 131 */     if (paramObject != null)
/*     */     {
/* 133 */       return new org.bouncycastle.pqc.asn1.McElieceCCA2PrivateKey(ASN1Sequence.getInstance(paramObject));
/*     */     }
/*     */     
/* 136 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/asn1/McElieceCCA2PrivateKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */