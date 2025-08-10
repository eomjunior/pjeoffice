/*    */ package META-INF.versions.9.org.bouncycastle.pqc.asn1;
/*    */ 
/*    */ import org.bouncycastle.asn1.ASN1Encodable;
/*    */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*    */ import org.bouncycastle.asn1.ASN1Integer;
/*    */ import org.bouncycastle.asn1.ASN1Object;
/*    */ import org.bouncycastle.asn1.ASN1OctetString;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.ASN1Sequence;
/*    */ import org.bouncycastle.asn1.DEROctetString;
/*    */ import org.bouncycastle.asn1.DERSequence;
/*    */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*    */ import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class McElieceCCA2PublicKey
/*    */   extends ASN1Object
/*    */ {
/*    */   private final int n;
/*    */   private final int t;
/*    */   private final GF2Matrix g;
/*    */   private final AlgorithmIdentifier digest;
/*    */   
/*    */   public McElieceCCA2PublicKey(int paramInt1, int paramInt2, GF2Matrix paramGF2Matrix, AlgorithmIdentifier paramAlgorithmIdentifier) {
/* 26 */     this.n = paramInt1;
/* 27 */     this.t = paramInt2;
/* 28 */     this.g = new GF2Matrix(paramGF2Matrix.getEncoded());
/* 29 */     this.digest = paramAlgorithmIdentifier;
/*    */   }
/*    */ 
/*    */   
/*    */   private McElieceCCA2PublicKey(ASN1Sequence paramASN1Sequence) {
/* 34 */     this.n = ((ASN1Integer)paramASN1Sequence.getObjectAt(0)).intValueExact();
/*    */     
/* 36 */     this.t = ((ASN1Integer)paramASN1Sequence.getObjectAt(1)).intValueExact();
/*    */     
/* 38 */     this.g = new GF2Matrix(((ASN1OctetString)paramASN1Sequence.getObjectAt(2)).getOctets());
/*    */     
/* 40 */     this.digest = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(3));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getN() {
/* 45 */     return this.n;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getT() {
/* 50 */     return this.t;
/*    */   }
/*    */ 
/*    */   
/*    */   public GF2Matrix getG() {
/* 55 */     return this.g;
/*    */   }
/*    */ 
/*    */   
/*    */   public AlgorithmIdentifier getDigest() {
/* 60 */     return this.digest;
/*    */   }
/*    */ 
/*    */   
/*    */   public ASN1Primitive toASN1Primitive() {
/* 65 */     ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
/*    */ 
/*    */     
/* 68 */     aSN1EncodableVector.add((ASN1Encodable)new ASN1Integer(this.n));
/*    */ 
/*    */     
/* 71 */     aSN1EncodableVector.add((ASN1Encodable)new ASN1Integer(this.t));
/*    */ 
/*    */     
/* 74 */     aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(this.g.getEncoded()));
/*    */     
/* 76 */     aSN1EncodableVector.add((ASN1Encodable)this.digest);
/*    */     
/* 78 */     return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
/*    */   }
/*    */ 
/*    */   
/*    */   public static org.bouncycastle.pqc.asn1.McElieceCCA2PublicKey getInstance(Object paramObject) {
/* 83 */     if (paramObject instanceof org.bouncycastle.pqc.asn1.McElieceCCA2PublicKey)
/*    */     {
/* 85 */       return (org.bouncycastle.pqc.asn1.McElieceCCA2PublicKey)paramObject;
/*    */     }
/* 87 */     if (paramObject != null)
/*    */     {
/* 89 */       return new org.bouncycastle.pqc.asn1.McElieceCCA2PublicKey(ASN1Sequence.getInstance(paramObject));
/*    */     }
/*    */     
/* 92 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/asn1/McElieceCCA2PublicKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */