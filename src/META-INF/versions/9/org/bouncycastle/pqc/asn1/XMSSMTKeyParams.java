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
/*    */ 
/*    */ public class XMSSMTKeyParams
/*    */   extends ASN1Object
/*    */ {
/*    */   private final ASN1Integer version;
/*    */   private final int height;
/*    */   private final int layers;
/*    */   private final AlgorithmIdentifier treeDigest;
/*    */   
/*    */   public XMSSMTKeyParams(int paramInt1, int paramInt2, AlgorithmIdentifier paramAlgorithmIdentifier) {
/* 32 */     this.version = new ASN1Integer(0L);
/* 33 */     this.height = paramInt1;
/* 34 */     this.layers = paramInt2;
/* 35 */     this.treeDigest = paramAlgorithmIdentifier;
/*    */   }
/*    */ 
/*    */   
/*    */   private XMSSMTKeyParams(ASN1Sequence paramASN1Sequence) {
/* 40 */     this.version = ASN1Integer.getInstance(paramASN1Sequence.getObjectAt(0));
/* 41 */     this.height = ASN1Integer.getInstance(paramASN1Sequence.getObjectAt(1)).intValueExact();
/* 42 */     this.layers = ASN1Integer.getInstance(paramASN1Sequence.getObjectAt(2)).intValueExact();
/* 43 */     this.treeDigest = AlgorithmIdentifier.getInstance(paramASN1Sequence.getObjectAt(3));
/*    */   }
/*    */ 
/*    */   
/*    */   public static org.bouncycastle.pqc.asn1.XMSSMTKeyParams getInstance(Object paramObject) {
/* 48 */     if (paramObject instanceof org.bouncycastle.pqc.asn1.XMSSMTKeyParams)
/*    */     {
/* 50 */       return (org.bouncycastle.pqc.asn1.XMSSMTKeyParams)paramObject;
/*    */     }
/* 52 */     if (paramObject != null)
/*    */     {
/* 54 */       return new org.bouncycastle.pqc.asn1.XMSSMTKeyParams(ASN1Sequence.getInstance(paramObject));
/*    */     }
/*    */     
/* 57 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getHeight() {
/* 62 */     return this.height;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLayers() {
/* 67 */     return this.layers;
/*    */   }
/*    */ 
/*    */   
/*    */   public AlgorithmIdentifier getTreeDigest() {
/* 72 */     return this.treeDigest;
/*    */   }
/*    */ 
/*    */   
/*    */   public ASN1Primitive toASN1Primitive() {
/* 77 */     ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
/*    */     
/* 79 */     aSN1EncodableVector.add((ASN1Encodable)this.version);
/* 80 */     aSN1EncodableVector.add((ASN1Encodable)new ASN1Integer(this.height));
/* 81 */     aSN1EncodableVector.add((ASN1Encodable)new ASN1Integer(this.layers));
/* 82 */     aSN1EncodableVector.add((ASN1Encodable)this.treeDigest);
/*    */     
/* 84 */     return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/asn1/XMSSMTKeyParams.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */