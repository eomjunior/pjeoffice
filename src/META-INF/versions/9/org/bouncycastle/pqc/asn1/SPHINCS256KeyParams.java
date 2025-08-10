/*    */ package META-INF.versions.9.org.bouncycastle.pqc.asn1;
/*    */ 
/*    */ import org.bouncycastle.asn1.ASN1Encodable;
/*    */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*    */ import org.bouncycastle.asn1.ASN1Integer;
/*    */ import org.bouncycastle.asn1.ASN1Object;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.ASN1Sequence;
/*    */ import org.bouncycastle.asn1.DERSequence;
/*    */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*    */ 
/*    */ public class SPHINCS256KeyParams
/*    */   extends ASN1Object
/*    */ {
/*    */   private final ASN1Integer version;
/*    */   private final AlgorithmIdentifier treeDigest;
/*    */   
/*    */   public SPHINCS256KeyParams(AlgorithmIdentifier paramAlgorithmIdentifier) {
/* 19 */     this.version = new ASN1Integer(0L);
/* 20 */     this.treeDigest = paramAlgorithmIdentifier;
/*    */   }
/*    */ 
/*    */   
/*    */   private SPHINCS256KeyParams(ASN1Sequence paramASN1Sequence) {
/* 25 */     this.version = ASN1Integer.getInstance(paramASN1Sequence.getObjectAt(0));
/* 26 */     this.treeDigest = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(1));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final org.bouncycastle.pqc.asn1.SPHINCS256KeyParams getInstance(Object paramObject) {
/* 31 */     if (paramObject instanceof org.bouncycastle.pqc.asn1.SPHINCS256KeyParams)
/*    */     {
/* 33 */       return (org.bouncycastle.pqc.asn1.SPHINCS256KeyParams)paramObject;
/*    */     }
/* 35 */     if (paramObject != null)
/*    */     {
/* 37 */       return new org.bouncycastle.pqc.asn1.SPHINCS256KeyParams(ASN1Sequence.getInstance(paramObject));
/*    */     }
/*    */     
/* 40 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public AlgorithmIdentifier getTreeDigest() {
/* 45 */     return this.treeDigest;
/*    */   }
/*    */ 
/*    */   
/*    */   public ASN1Primitive toASN1Primitive() {
/* 50 */     ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
/*    */     
/* 52 */     aSN1EncodableVector.add((ASN1Encodable)this.version);
/* 53 */     aSN1EncodableVector.add((ASN1Encodable)this.treeDigest);
/*    */     
/* 55 */     return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/asn1/SPHINCS256KeyParams.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */