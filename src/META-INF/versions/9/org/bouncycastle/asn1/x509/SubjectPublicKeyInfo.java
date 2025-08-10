/*     */ package META-INF.versions.9.org.bouncycastle.asn1.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1Object;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.DERBitString;
/*     */ import org.bouncycastle.asn1.DERSequence;
/*     */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
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
/*     */ public class SubjectPublicKeyInfo
/*     */   extends ASN1Object
/*     */ {
/*     */   private AlgorithmIdentifier algId;
/*     */   private DERBitString keyData;
/*     */   
/*     */   public static org.bouncycastle.asn1.x509.SubjectPublicKeyInfo getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  31 */     return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.asn1.x509.SubjectPublicKeyInfo getInstance(Object paramObject) {
/*  37 */     if (paramObject instanceof org.bouncycastle.asn1.x509.SubjectPublicKeyInfo)
/*     */     {
/*  39 */       return (org.bouncycastle.asn1.x509.SubjectPublicKeyInfo)paramObject;
/*     */     }
/*  41 */     if (paramObject != null)
/*     */     {
/*  43 */       return new org.bouncycastle.asn1.x509.SubjectPublicKeyInfo(ASN1Sequence.getInstance(paramObject));
/*     */     }
/*     */     
/*  46 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SubjectPublicKeyInfo(AlgorithmIdentifier paramAlgorithmIdentifier, ASN1Encodable paramASN1Encodable) throws IOException {
/*  54 */     this.keyData = new DERBitString(paramASN1Encodable);
/*  55 */     this.algId = paramAlgorithmIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SubjectPublicKeyInfo(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfbyte) {
/*  62 */     this.keyData = new DERBitString(paramArrayOfbyte);
/*  63 */     this.algId = paramAlgorithmIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SubjectPublicKeyInfo(ASN1Sequence paramASN1Sequence) {
/*  72 */     if (paramASN1Sequence.size() != 2)
/*     */     {
/*  74 */       throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence
/*  75 */           .size());
/*     */     }
/*     */     
/*  78 */     Enumeration enumeration = paramASN1Sequence.getObjects();
/*     */     
/*  80 */     this.algId = AlgorithmIdentifier.getInstance(enumeration.nextElement());
/*  81 */     this.keyData = DERBitString.getInstance(enumeration.nextElement());
/*     */   }
/*     */ 
/*     */   
/*     */   public AlgorithmIdentifier getAlgorithm() {
/*  86 */     return this.algId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AlgorithmIdentifier getAlgorithmId() {
/*  95 */     return this.algId;
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
/*     */   public ASN1Primitive parsePublicKey() throws IOException {
/* 109 */     return ASN1Primitive.fromByteArray(this.keyData.getOctets());
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
/*     */   public ASN1Primitive getPublicKey() throws IOException {
/* 124 */     return ASN1Primitive.fromByteArray(this.keyData.getOctets());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DERBitString getPublicKeyData() {
/* 134 */     return this.keyData;
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
/*     */   public ASN1Primitive toASN1Primitive() {
/* 147 */     ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(2);
/*     */     
/* 149 */     aSN1EncodableVector.add((ASN1Encodable)this.algId);
/* 150 */     aSN1EncodableVector.add((ASN1Encodable)this.keyData);
/*     */     
/* 152 */     return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/x509/SubjectPublicKeyInfo.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */