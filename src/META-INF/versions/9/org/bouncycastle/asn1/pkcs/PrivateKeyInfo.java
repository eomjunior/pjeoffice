/*     */ package META-INF.versions.9.org.bouncycastle.asn1.pkcs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ import org.bouncycastle.asn1.ASN1BitString;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1Integer;
/*     */ import org.bouncycastle.asn1.ASN1Object;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.ASN1Set;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.DERBitString;
/*     */ import org.bouncycastle.asn1.DEROctetString;
/*     */ import org.bouncycastle.asn1.DERSequence;
/*     */ import org.bouncycastle.asn1.DERTaggedObject;
/*     */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*     */ import org.bouncycastle.util.BigIntegers;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PrivateKeyInfo
/*     */   extends ASN1Object
/*     */ {
/*     */   private ASN1Integer version;
/*     */   private AlgorithmIdentifier privateKeyAlgorithm;
/*     */   private ASN1OctetString privateKey;
/*     */   private ASN1Set attributes;
/*     */   private ASN1BitString publicKey;
/*     */   
/*     */   public static org.bouncycastle.asn1.pkcs.PrivateKeyInfo getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean) {
/*  71 */     return getInstance(ASN1Sequence.getInstance(paramASN1TaggedObject, paramBoolean));
/*     */   }
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.asn1.pkcs.PrivateKeyInfo getInstance(Object paramObject) {
/*  76 */     if (paramObject instanceof org.bouncycastle.asn1.pkcs.PrivateKeyInfo)
/*     */     {
/*  78 */       return (org.bouncycastle.asn1.pkcs.PrivateKeyInfo)paramObject;
/*     */     }
/*  80 */     if (paramObject != null)
/*     */     {
/*  82 */       return new org.bouncycastle.asn1.pkcs.PrivateKeyInfo(ASN1Sequence.getInstance(paramObject));
/*     */     }
/*     */     
/*  85 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getVersionValue(ASN1Integer paramASN1Integer) {
/*  90 */     int i = paramASN1Integer.intValueExact();
/*  91 */     if (i < 0 || i > 1)
/*     */     {
/*  93 */       throw new IllegalArgumentException("invalid version for private key info");
/*     */     }
/*  95 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrivateKeyInfo(AlgorithmIdentifier paramAlgorithmIdentifier, ASN1Encodable paramASN1Encodable) throws IOException {
/* 103 */     this(paramAlgorithmIdentifier, paramASN1Encodable, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrivateKeyInfo(AlgorithmIdentifier paramAlgorithmIdentifier, ASN1Encodable paramASN1Encodable, ASN1Set paramASN1Set) throws IOException {
/* 112 */     this(paramAlgorithmIdentifier, paramASN1Encodable, paramASN1Set, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrivateKeyInfo(AlgorithmIdentifier paramAlgorithmIdentifier, ASN1Encodable paramASN1Encodable, ASN1Set paramASN1Set, byte[] paramArrayOfbyte) throws IOException {
/* 122 */     this.version = new ASN1Integer((paramArrayOfbyte != null) ? BigIntegers.ONE : BigIntegers.ZERO);
/* 123 */     this.privateKeyAlgorithm = paramAlgorithmIdentifier;
/* 124 */     this.privateKey = (ASN1OctetString)new DEROctetString(paramASN1Encodable);
/* 125 */     this.attributes = paramASN1Set;
/* 126 */     this.publicKey = (paramArrayOfbyte == null) ? null : (ASN1BitString)new DERBitString(paramArrayOfbyte);
/*     */   }
/*     */ 
/*     */   
/*     */   private PrivateKeyInfo(ASN1Sequence paramASN1Sequence) {
/* 131 */     Enumeration<ASN1TaggedObject> enumeration = paramASN1Sequence.getObjects();
/*     */     
/* 133 */     this.version = ASN1Integer.getInstance(enumeration.nextElement());
/*     */     
/* 135 */     int i = getVersionValue(this.version);
/*     */     
/* 137 */     this.privateKeyAlgorithm = AlgorithmIdentifier.getInstance(enumeration.nextElement());
/* 138 */     this.privateKey = ASN1OctetString.getInstance(enumeration.nextElement());
/*     */     
/* 140 */     int j = -1;
/* 141 */     while (enumeration.hasMoreElements()) {
/*     */       
/* 143 */       ASN1TaggedObject aSN1TaggedObject = enumeration.nextElement();
/*     */       
/* 145 */       int k = aSN1TaggedObject.getTagNo();
/* 146 */       if (k <= j)
/*     */       {
/* 148 */         throw new IllegalArgumentException("invalid optional field in private key info");
/*     */       }
/*     */       
/* 151 */       j = k;
/*     */       
/* 153 */       switch (k) {
/*     */ 
/*     */         
/*     */         case 0:
/* 157 */           this.attributes = ASN1Set.getInstance(aSN1TaggedObject, false);
/*     */           continue;
/*     */ 
/*     */         
/*     */         case 1:
/* 162 */           if (i < 1)
/*     */           {
/* 164 */             throw new IllegalArgumentException("'publicKey' requires version v2(1) or later");
/*     */           }
/*     */           
/* 167 */           this.publicKey = (ASN1BitString)DERBitString.getInstance(aSN1TaggedObject, false);
/*     */           continue;
/*     */       } 
/*     */ 
/*     */       
/* 172 */       throw new IllegalArgumentException("unknown optional field in private key info");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1Integer getVersion() {
/* 180 */     return this.version;
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1Set getAttributes() {
/* 185 */     return this.attributes;
/*     */   }
/*     */ 
/*     */   
/*     */   public AlgorithmIdentifier getPrivateKeyAlgorithm() {
/* 190 */     return this.privateKeyAlgorithm;
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1OctetString getPrivateKey() {
/* 195 */     return (ASN1OctetString)new DEROctetString(this.privateKey.getOctets());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1Encodable parsePrivateKey() throws IOException {
/* 201 */     return (ASN1Encodable)ASN1Primitive.fromByteArray(this.privateKey.getOctets());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPublicKey() {
/* 211 */     return (this.publicKey != null);
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
/*     */   public ASN1Encodable parsePublicKey() throws IOException {
/* 225 */     return (this.publicKey == null) ? null : (ASN1Encodable)ASN1Primitive.fromByteArray(this.publicKey.getOctets());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ASN1BitString getPublicKeyData() {
/* 235 */     return this.publicKey;
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1Primitive toASN1Primitive() {
/* 240 */     ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(5);
/*     */     
/* 242 */     aSN1EncodableVector.add((ASN1Encodable)this.version);
/* 243 */     aSN1EncodableVector.add((ASN1Encodable)this.privateKeyAlgorithm);
/* 244 */     aSN1EncodableVector.add((ASN1Encodable)this.privateKey);
/*     */     
/* 246 */     if (this.attributes != null)
/*     */     {
/* 248 */       aSN1EncodableVector.add((ASN1Encodable)new DERTaggedObject(false, 0, (ASN1Encodable)this.attributes));
/*     */     }
/*     */     
/* 251 */     if (this.publicKey != null)
/*     */     {
/* 253 */       aSN1EncodableVector.add((ASN1Encodable)new DERTaggedObject(false, 1, (ASN1Encodable)this.publicKey));
/*     */     }
/*     */     
/* 256 */     return (ASN1Primitive)new DERSequence(aSN1EncodableVector);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/pkcs/PrivateKeyInfo.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */