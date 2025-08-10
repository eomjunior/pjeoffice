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
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.GF2mField;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.Permutation;
/*     */ import org.bouncycastle.pqc.math.linearalgebra.PolynomialGF2mSmallM;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class McEliecePrivateKey
/*     */   extends ASN1Object
/*     */ {
/*     */   private int n;
/*     */   private int k;
/*     */   private byte[] encField;
/*     */   private byte[] encGp;
/*     */   private byte[] encSInv;
/*     */   private byte[] encP1;
/*     */   private byte[] encP2;
/*     */   
/*     */   public McEliecePrivateKey(int paramInt1, int paramInt2, GF2mField paramGF2mField, PolynomialGF2mSmallM paramPolynomialGF2mSmallM, Permutation paramPermutation1, Permutation paramPermutation2, GF2Matrix paramGF2Matrix) {
/*  31 */     this.n = paramInt1;
/*  32 */     this.k = paramInt2;
/*  33 */     this.encField = paramGF2mField.getEncoded();
/*  34 */     this.encGp = paramPolynomialGF2mSmallM.getEncoded();
/*  35 */     this.encSInv = paramGF2Matrix.getEncoded();
/*  36 */     this.encP1 = paramPermutation1.getEncoded();
/*  37 */     this.encP2 = paramPermutation2.getEncoded();
/*     */   }
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.pqc.asn1.McEliecePrivateKey getInstance(Object paramObject) {
/*  42 */     if (paramObject instanceof org.bouncycastle.pqc.asn1.McEliecePrivateKey)
/*     */     {
/*  44 */       return (org.bouncycastle.pqc.asn1.McEliecePrivateKey)paramObject;
/*     */     }
/*  46 */     if (paramObject != null)
/*     */     {
/*  48 */       return new org.bouncycastle.pqc.asn1.McEliecePrivateKey(ASN1Sequence.getInstance(paramObject));
/*     */     }
/*     */     
/*  51 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private McEliecePrivateKey(ASN1Sequence paramASN1Sequence) {
/*  56 */     this.n = ((ASN1Integer)paramASN1Sequence.getObjectAt(0)).intValueExact();
/*     */     
/*  58 */     this.k = ((ASN1Integer)paramASN1Sequence.getObjectAt(1)).intValueExact();
/*     */     
/*  60 */     this.encField = ((ASN1OctetString)paramASN1Sequence.getObjectAt(2)).getOctets();
/*     */     
/*  62 */     this.encGp = ((ASN1OctetString)paramASN1Sequence.getObjectAt(3)).getOctets();
/*     */     
/*  64 */     this.encP1 = ((ASN1OctetString)paramASN1Sequence.getObjectAt(4)).getOctets();
/*     */     
/*  66 */     this.encP2 = ((ASN1OctetString)paramASN1Sequence.getObjectAt(5)).getOctets();
/*     */     
/*  68 */     this.encSInv = ((ASN1OctetString)paramASN1Sequence.getObjectAt(6)).getOctets();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getN() {
/*  73 */     return this.n;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getK() {
/*  78 */     return this.k;
/*     */   }
/*     */ 
/*     */   
/*     */   public GF2mField getField() {
/*  83 */     return new GF2mField(this.encField);
/*     */   }
/*     */ 
/*     */   
/*     */   public PolynomialGF2mSmallM getGoppaPoly() {
/*  88 */     return new PolynomialGF2mSmallM(getField(), this.encGp);
/*     */   }
/*     */ 
/*     */   
/*     */   public GF2Matrix getSInv() {
/*  93 */     return new GF2Matrix(this.encSInv);
/*     */   }
/*     */ 
/*     */   
/*     */   public Permutation getP1() {
/*  98 */     return new Permutation(this.encP1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Permutation getP2() {
/* 103 */     return new Permutation(this.encP2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1Primitive toASN1Primitive() {
/* 110 */     ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
/*     */ 
/*     */     
/* 113 */     aSN1EncodableVector.add((ASN1Encodable)new ASN1Integer(this.n));
/*     */ 
/*     */     
/* 116 */     aSN1EncodableVector.add((ASN1Encodable)new ASN1Integer(this.k));
/*     */ 
/*     */     
/* 119 */     aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(this.encField));
/*     */ 
/*     */     
/* 122 */     aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(this.encGp));
/*     */ 
/*     */     
/* 125 */     aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(this.encP1));
/*     */ 
/*     */     
/* 128 */     aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(this.encP2));
/*     */ 
/*     */     
/* 131 */     aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(this.encSInv));
/*     */     
/* 133 */     return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/asn1/McEliecePrivateKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */