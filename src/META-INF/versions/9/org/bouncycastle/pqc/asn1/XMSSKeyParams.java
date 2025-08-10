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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XMSSKeyParams
/*    */   extends ASN1Object
/*    */ {
/*    */   private final ASN1Integer version;
/*    */   private final int height;
/*    */   private final AlgorithmIdentifier treeDigest;
/*    */   
/*    */   public XMSSKeyParams(int paramInt, AlgorithmIdentifier paramAlgorithmIdentifier) {
/* 30 */     this.version = new ASN1Integer(0L);
/* 31 */     this.height = paramInt;
/* 32 */     this.treeDigest = paramAlgorithmIdentifier;
/*    */   }
/*    */ 
/*    */   
/*    */   private XMSSKeyParams(ASN1Sequence paramASN1Sequence) {
/* 37 */     this.version = ASN1Integer.getInstance(paramASN1Sequence.getObjectAt(0));
/* 38 */     this.height = ASN1Integer.getInstance(paramASN1Sequence.getObjectAt(1)).intValueExact();
/* 39 */     this.treeDigest = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(2));
/*    */   }
/*    */ 
/*    */   
/*    */   public static org.bouncycastle.pqc.asn1.XMSSKeyParams getInstance(Object paramObject) {
/* 44 */     if (paramObject instanceof org.bouncycastle.pqc.asn1.XMSSKeyParams)
/*    */     {
/* 46 */       return (org.bouncycastle.pqc.asn1.XMSSKeyParams)paramObject;
/*    */     }
/* 48 */     if (paramObject != null)
/*    */     {
/* 50 */       return new org.bouncycastle.pqc.asn1.XMSSKeyParams(ASN1Sequence.getInstance(paramObject));
/*    */     }
/*    */     
/* 53 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 58 */     return this.height;
/*    */   }
/*    */ 
/*    */   
/*    */   public AlgorithmIdentifier getTreeDigest() {
/* 63 */     return this.treeDigest;
/*    */   }
/*    */ 
/*    */   
/*    */   public ASN1Primitive toASN1Primitive() {
/* 68 */     ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
/*    */     
/* 70 */     aSN1EncodableVector.add((ASN1Encodable)this.version);
/* 71 */     aSN1EncodableVector.add((ASN1Encodable)new ASN1Integer(this.height));
/* 72 */     aSN1EncodableVector.add((ASN1Encodable)this.treeDigest);
/*    */     
/* 74 */     return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/asn1/XMSSKeyParams.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */