/*    */ package META-INF.versions.9.org.bouncycastle.asn1.cryptopro;
/*    */ 
/*    */ import org.bouncycastle.asn1.ASN1Encodable;
/*    */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*    */ import org.bouncycastle.asn1.ASN1Object;
/*    */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.ASN1Sequence;
/*    */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*    */ import org.bouncycastle.asn1.DERSequence;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GOST3410PublicKeyAlgParameters
/*    */   extends ASN1Object
/*    */ {
/*    */   private ASN1ObjectIdentifier publicKeyParamSet;
/*    */   private ASN1ObjectIdentifier digestParamSet;
/*    */   private ASN1ObjectIdentifier encryptionParamSet;
/*    */   
/*    */   public static org.bouncycastle.asn1.cryptopro.GOST3410PublicKeyAlgParameters getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/* 22 */     return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static org.bouncycastle.asn1.cryptopro.GOST3410PublicKeyAlgParameters getInstance(Object paramObject) {
/* 28 */     if (paramObject instanceof org.bouncycastle.asn1.cryptopro.GOST3410PublicKeyAlgParameters)
/*    */     {
/* 30 */       return (org.bouncycastle.asn1.cryptopro.GOST3410PublicKeyAlgParameters)paramObject;
/*    */     }
/*    */     
/* 33 */     if (paramObject != null)
/*    */     {
/* 35 */       return new org.bouncycastle.asn1.cryptopro.GOST3410PublicKeyAlgParameters(ASN1Sequence.getInstance(paramObject));
/*    */     }
/*    */     
/* 38 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GOST3410PublicKeyAlgParameters(ASN1ObjectIdentifier paramASN1ObjectIdentifier1, ASN1ObjectIdentifier paramASN1ObjectIdentifier2) {
/* 45 */     this.publicKeyParamSet = paramASN1ObjectIdentifier1;
/* 46 */     this.digestParamSet = paramASN1ObjectIdentifier2;
/* 47 */     this.encryptionParamSet = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GOST3410PublicKeyAlgParameters(ASN1ObjectIdentifier paramASN1ObjectIdentifier1, ASN1ObjectIdentifier paramASN1ObjectIdentifier2, ASN1ObjectIdentifier paramASN1ObjectIdentifier3) {
/* 55 */     this.publicKeyParamSet = paramASN1ObjectIdentifier1;
/* 56 */     this.digestParamSet = paramASN1ObjectIdentifier2;
/* 57 */     this.encryptionParamSet = paramASN1ObjectIdentifier3;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private GOST3410PublicKeyAlgParameters(ASN1Sequence paramASN1Sequence) {
/* 63 */     this.publicKeyParamSet = (ASN1ObjectIdentifier)paramASN1Sequence.getObjectAt(0);
/* 64 */     this.digestParamSet = (ASN1ObjectIdentifier)paramASN1Sequence.getObjectAt(1);
/*    */     
/* 66 */     if (paramASN1Sequence.size() > 2)
/*    */     {
/* 68 */       this.encryptionParamSet = (ASN1ObjectIdentifier)paramASN1Sequence.getObjectAt(2);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public ASN1ObjectIdentifier getPublicKeyParamSet() {
/* 74 */     return this.publicKeyParamSet;
/*    */   }
/*    */ 
/*    */   
/*    */   public ASN1ObjectIdentifier getDigestParamSet() {
/* 79 */     return this.digestParamSet;
/*    */   }
/*    */ 
/*    */   
/*    */   public ASN1ObjectIdentifier getEncryptionParamSet() {
/* 84 */     return this.encryptionParamSet;
/*    */   }
/*    */ 
/*    */   
/*    */   public ASN1Primitive toASN1Primitive() {
/* 89 */     ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(3);
/*    */     
/* 91 */     aSN1EncodableVector.add((ASN1Encodable)this.publicKeyParamSet);
/* 92 */     aSN1EncodableVector.add((ASN1Encodable)this.digestParamSet);
/*    */     
/* 94 */     if (this.encryptionParamSet != null)
/*    */     {
/* 96 */       aSN1EncodableVector.add((ASN1Encodable)this.encryptionParamSet);
/*    */     }
/*    */     
/* 99 */     return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/cryptopro/GOST3410PublicKeyAlgParameters.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */