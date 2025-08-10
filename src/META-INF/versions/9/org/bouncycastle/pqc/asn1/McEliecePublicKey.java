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
/*    */ import org.bouncycastle.pqc.math.linearalgebra.GF2Matrix;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class McEliecePublicKey
/*    */   extends ASN1Object
/*    */ {
/*    */   private final int n;
/*    */   private final int t;
/*    */   private final GF2Matrix g;
/*    */   
/*    */   public McEliecePublicKey(int paramInt1, int paramInt2, GF2Matrix paramGF2Matrix) {
/* 24 */     this.n = paramInt1;
/* 25 */     this.t = paramInt2;
/* 26 */     this.g = new GF2Matrix(paramGF2Matrix);
/*    */   }
/*    */ 
/*    */   
/*    */   private McEliecePublicKey(ASN1Sequence paramASN1Sequence) {
/* 31 */     this.n = ((ASN1Integer)paramASN1Sequence.getObjectAt(0)).intValueExact();
/*    */     
/* 33 */     this.t = ((ASN1Integer)paramASN1Sequence.getObjectAt(1)).intValueExact();
/*    */     
/* 35 */     this.g = new GF2Matrix(((ASN1OctetString)paramASN1Sequence.getObjectAt(2)).getOctets());
/*    */   }
/*    */ 
/*    */   
/*    */   public int getN() {
/* 40 */     return this.n;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getT() {
/* 45 */     return this.t;
/*    */   }
/*    */ 
/*    */   
/*    */   public GF2Matrix getG() {
/* 50 */     return new GF2Matrix(this.g);
/*    */   }
/*    */ 
/*    */   
/*    */   public ASN1Primitive toASN1Primitive() {
/* 55 */     ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
/*    */ 
/*    */     
/* 58 */     aSN1EncodableVector.add((ASN1Encodable)new ASN1Integer(this.n));
/*    */ 
/*    */     
/* 61 */     aSN1EncodableVector.add((ASN1Encodable)new ASN1Integer(this.t));
/*    */ 
/*    */     
/* 64 */     aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(this.g.getEncoded()));
/*    */     
/* 66 */     return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
/*    */   }
/*    */ 
/*    */   
/*    */   public static org.bouncycastle.pqc.asn1.McEliecePublicKey getInstance(Object paramObject) {
/* 71 */     if (paramObject instanceof org.bouncycastle.pqc.asn1.McEliecePublicKey)
/*    */     {
/* 73 */       return (org.bouncycastle.pqc.asn1.McEliecePublicKey)paramObject;
/*    */     }
/* 75 */     if (paramObject != null)
/*    */     {
/* 77 */       return new org.bouncycastle.pqc.asn1.McEliecePublicKey(ASN1Sequence.getInstance(paramObject));
/*    */     }
/*    */     
/* 80 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/asn1/McEliecePublicKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */