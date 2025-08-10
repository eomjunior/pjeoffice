/*     */ package META-INF.versions.9.org.bouncycastle.asn1.x509;
/*     */ 
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1Object;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.DERSequence;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AlgorithmIdentifier
/*     */   extends ASN1Object
/*     */ {
/*     */   private ASN1ObjectIdentifier algorithm;
/*     */   private ASN1Encodable parameters;
/*     */   
/*     */   public static org.bouncycastle.asn1.x509.AlgorithmIdentifier getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  22 */     return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.asn1.x509.AlgorithmIdentifier getInstance(Object paramObject) {
/*  28 */     if (paramObject instanceof org.bouncycastle.asn1.x509.AlgorithmIdentifier)
/*     */     {
/*  30 */       return (org.bouncycastle.asn1.x509.AlgorithmIdentifier)paramObject;
/*     */     }
/*  32 */     if (paramObject != null)
/*     */     {
/*  34 */       return new org.bouncycastle.asn1.x509.AlgorithmIdentifier(ASN1Sequence.getInstance(paramObject));
/*     */     }
/*     */     
/*  37 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AlgorithmIdentifier(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/*  43 */     this.algorithm = paramASN1ObjectIdentifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AlgorithmIdentifier(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ASN1Encodable paramASN1Encodable) {
/*  50 */     this.algorithm = paramASN1ObjectIdentifier;
/*  51 */     this.parameters = paramASN1Encodable;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private AlgorithmIdentifier(ASN1Sequence paramASN1Sequence) {
/*  57 */     if (paramASN1Sequence.size() < 1 || paramASN1Sequence.size() > 2)
/*     */     {
/*  59 */       throw new IllegalArgumentException("Bad sequence size: " + paramASN1Sequence
/*  60 */           .size());
/*     */     }
/*     */     
/*  63 */     this.algorithm = ASN1ObjectIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
/*     */     
/*  65 */     if (paramASN1Sequence.size() == 2) {
/*     */       
/*  67 */       this.parameters = paramASN1Sequence.getObjectAt(1);
/*     */     }
/*     */     else {
/*     */       
/*  71 */       this.parameters = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1ObjectIdentifier getAlgorithm() {
/*  77 */     return this.algorithm;
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1Encodable getParameters() {
/*  82 */     return this.parameters;
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
/*  95 */     ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(2);
/*     */     
/*  97 */     aSN1EncodableVector.add((ASN1Encodable)this.algorithm);
/*     */     
/*  99 */     if (this.parameters != null)
/*     */     {
/* 101 */       aSN1EncodableVector.add(this.parameters);
/*     */     }
/*     */     
/* 104 */     return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/x509/AlgorithmIdentifier.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */