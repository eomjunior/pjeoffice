/*    */ package META-INF.versions.9.org.bouncycastle.pqc.asn1;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import org.bouncycastle.asn1.ASN1Encodable;
/*    */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*    */ import org.bouncycastle.asn1.ASN1Integer;
/*    */ import org.bouncycastle.asn1.ASN1Object;
/*    */ import org.bouncycastle.asn1.ASN1Primitive;
/*    */ import org.bouncycastle.asn1.ASN1Sequence;
/*    */ import org.bouncycastle.asn1.DEROctetString;
/*    */ import org.bouncycastle.asn1.DERSequence;
/*    */ import org.bouncycastle.util.Arrays;
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
/*    */ public class XMSSMTPublicKey
/*    */   extends ASN1Object
/*    */ {
/*    */   private final byte[] publicSeed;
/*    */   private final byte[] root;
/*    */   
/*    */   public XMSSMTPublicKey(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
/* 32 */     this.publicSeed = Arrays.clone(paramArrayOfbyte1);
/* 33 */     this.root = Arrays.clone(paramArrayOfbyte2);
/*    */   }
/*    */ 
/*    */   
/*    */   private XMSSMTPublicKey(ASN1Sequence paramASN1Sequence) {
/* 38 */     if (!ASN1Integer.getInstance(paramASN1Sequence.getObjectAt(0)).hasValue(BigInteger.valueOf(0L)))
/*    */     {
/* 40 */       throw new IllegalArgumentException("unknown version of sequence");
/*    */     }
/*    */     
/* 43 */     this.publicSeed = Arrays.clone(DEROctetString.getInstance(paramASN1Sequence.getObjectAt(1)).getOctets());
/* 44 */     this.root = Arrays.clone(DEROctetString.getInstance(paramASN1Sequence.getObjectAt(2)).getOctets());
/*    */   }
/*    */ 
/*    */   
/*    */   public static org.bouncycastle.pqc.asn1.XMSSMTPublicKey getInstance(Object paramObject) {
/* 49 */     if (paramObject instanceof org.bouncycastle.pqc.asn1.XMSSMTPublicKey)
/*    */     {
/* 51 */       return (org.bouncycastle.pqc.asn1.XMSSMTPublicKey)paramObject;
/*    */     }
/* 53 */     if (paramObject != null)
/*    */     {
/* 55 */       return new org.bouncycastle.pqc.asn1.XMSSMTPublicKey(ASN1Sequence.getInstance(paramObject));
/*    */     }
/*    */     
/* 58 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getPublicSeed() {
/* 63 */     return Arrays.clone(this.publicSeed);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getRoot() {
/* 68 */     return Arrays.clone(this.root);
/*    */   }
/*    */ 
/*    */   
/*    */   public ASN1Primitive toASN1Primitive() {
/* 73 */     ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
/*    */     
/* 75 */     aSN1EncodableVector.add((ASN1Encodable)new ASN1Integer(0L));
/*    */     
/* 77 */     aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(this.publicSeed));
/* 78 */     aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(this.root));
/*    */     
/* 80 */     return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/asn1/XMSSMTPublicKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */