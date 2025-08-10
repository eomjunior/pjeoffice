/*     */ package META-INF.versions.9.org.bouncycastle.pqc.asn1;
/*     */ 
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1Integer;
/*     */ import org.bouncycastle.asn1.ASN1Object;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.DEROctetString;
/*     */ import org.bouncycastle.asn1.DERSequence;
/*     */ import org.bouncycastle.asn1.DERTaggedObject;
/*     */ import org.bouncycastle.util.Arrays;
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
/*     */ public class XMSSMTPrivateKey
/*     */   extends ASN1Object
/*     */ {
/*     */   private final int version;
/*     */   private final long index;
/*     */   private final long maxIndex;
/*     */   private final byte[] secretKeySeed;
/*     */   private final byte[] secretKeyPRF;
/*     */   private final byte[] publicSeed;
/*     */   private final byte[] root;
/*     */   private final byte[] bdsState;
/*     */   
/*     */   public XMSSMTPrivateKey(long paramLong, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, byte[] paramArrayOfbyte4, byte[] paramArrayOfbyte5) {
/*  48 */     this.version = 0;
/*  49 */     this.index = paramLong;
/*  50 */     this.secretKeySeed = Arrays.clone(paramArrayOfbyte1);
/*  51 */     this.secretKeyPRF = Arrays.clone(paramArrayOfbyte2);
/*  52 */     this.publicSeed = Arrays.clone(paramArrayOfbyte3);
/*  53 */     this.root = Arrays.clone(paramArrayOfbyte4);
/*  54 */     this.bdsState = Arrays.clone(paramArrayOfbyte5);
/*  55 */     this.maxIndex = -1L;
/*     */   }
/*     */ 
/*     */   
/*     */   public XMSSMTPrivateKey(long paramLong1, byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, byte[] paramArrayOfbyte4, byte[] paramArrayOfbyte5, long paramLong2) {
/*  60 */     this.version = 1;
/*  61 */     this.index = paramLong1;
/*  62 */     this.secretKeySeed = Arrays.clone(paramArrayOfbyte1);
/*  63 */     this.secretKeyPRF = Arrays.clone(paramArrayOfbyte2);
/*  64 */     this.publicSeed = Arrays.clone(paramArrayOfbyte3);
/*  65 */     this.root = Arrays.clone(paramArrayOfbyte4);
/*  66 */     this.bdsState = Arrays.clone(paramArrayOfbyte5);
/*  67 */     this.maxIndex = paramLong2;
/*     */   }
/*     */ 
/*     */   
/*     */   private XMSSMTPrivateKey(ASN1Sequence paramASN1Sequence) {
/*  72 */     ASN1Integer aSN1Integer = ASN1Integer.getInstance(paramASN1Sequence.getObjectAt(0));
/*  73 */     if (!aSN1Integer.hasValue(BigIntegers.ZERO) && !aSN1Integer.hasValue(BigIntegers.ONE))
/*     */     {
/*  75 */       throw new IllegalArgumentException("unknown version of sequence");
/*     */     }
/*  77 */     this.version = aSN1Integer.intValueExact();
/*     */     
/*  79 */     if (paramASN1Sequence.size() != 2 && paramASN1Sequence.size() != 3)
/*     */     {
/*  81 */       throw new IllegalArgumentException("key sequence wrong size");
/*     */     }
/*     */     
/*  84 */     ASN1Sequence aSN1Sequence = ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(1));
/*     */     
/*  86 */     this.index = ASN1Integer.getInstance(aSN1Sequence.getObjectAt(0)).longValueExact();
/*  87 */     this.secretKeySeed = Arrays.clone(DEROctetString.getInstance(aSN1Sequence.getObjectAt(1)).getOctets());
/*  88 */     this.secretKeyPRF = Arrays.clone(DEROctetString.getInstance(aSN1Sequence.getObjectAt(2)).getOctets());
/*  89 */     this.publicSeed = Arrays.clone(DEROctetString.getInstance(aSN1Sequence.getObjectAt(3)).getOctets());
/*  90 */     this.root = Arrays.clone(DEROctetString.getInstance(aSN1Sequence.getObjectAt(4)).getOctets());
/*     */     
/*  92 */     if (aSN1Sequence.size() == 6) {
/*     */       
/*  94 */       ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(aSN1Sequence.getObjectAt(5));
/*  95 */       if (aSN1TaggedObject.getTagNo() != 0)
/*     */       {
/*  97 */         throw new IllegalArgumentException("unknown tag in XMSSPrivateKey");
/*     */       }
/*  99 */       this.maxIndex = ASN1Integer.getInstance(aSN1TaggedObject, false).longValueExact();
/*     */     }
/* 101 */     else if (aSN1Sequence.size() == 5) {
/*     */       
/* 103 */       this.maxIndex = -1L;
/*     */     }
/*     */     else {
/*     */       
/* 107 */       throw new IllegalArgumentException("keySeq should be 5 or 6 in length");
/*     */     } 
/*     */     
/* 110 */     if (paramASN1Sequence.size() == 3) {
/*     */       
/* 112 */       this.bdsState = Arrays.clone(DEROctetString.getInstance(ASN1TaggedObject.getInstance(paramASN1Sequence.getObjectAt(2)), true).getOctets());
/*     */     }
/*     */     else {
/*     */       
/* 116 */       this.bdsState = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.pqc.asn1.XMSSMTPrivateKey getInstance(Object paramObject) {
/* 122 */     if (paramObject instanceof org.bouncycastle.pqc.asn1.XMSSMTPrivateKey)
/*     */     {
/* 124 */       return (org.bouncycastle.pqc.asn1.XMSSMTPrivateKey)paramObject;
/*     */     }
/* 126 */     if (paramObject != null)
/*     */     {
/* 128 */       return new org.bouncycastle.pqc.asn1.XMSSMTPrivateKey(ASN1Sequence.getInstance(paramObject));
/*     */     }
/*     */     
/* 131 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 136 */     return this.version;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getIndex() {
/* 141 */     return this.index;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getMaxIndex() {
/* 146 */     return this.maxIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getSecretKeySeed() {
/* 151 */     return Arrays.clone(this.secretKeySeed);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getSecretKeyPRF() {
/* 156 */     return Arrays.clone(this.secretKeyPRF);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getPublicSeed() {
/* 161 */     return Arrays.clone(this.publicSeed);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getRoot() {
/* 166 */     return Arrays.clone(this.root);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getBdsState() {
/* 171 */     return Arrays.clone(this.bdsState);
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1Primitive toASN1Primitive() {
/* 176 */     ASN1EncodableVector aSN1EncodableVector1 = new ASN1EncodableVector();
/*     */     
/* 178 */     if (this.maxIndex >= 0L) {
/*     */       
/* 180 */       aSN1EncodableVector1.add((ASN1Encodable)new ASN1Integer(1L));
/*     */     }
/*     */     else {
/*     */       
/* 184 */       aSN1EncodableVector1.add((ASN1Encodable)new ASN1Integer(0L));
/*     */     } 
/*     */     
/* 187 */     ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
/*     */     
/* 189 */     aSN1EncodableVector2.add((ASN1Encodable)new ASN1Integer(this.index));
/* 190 */     aSN1EncodableVector2.add((ASN1Encodable)new DEROctetString(this.secretKeySeed));
/* 191 */     aSN1EncodableVector2.add((ASN1Encodable)new DEROctetString(this.secretKeyPRF));
/* 192 */     aSN1EncodableVector2.add((ASN1Encodable)new DEROctetString(this.publicSeed));
/* 193 */     aSN1EncodableVector2.add((ASN1Encodable)new DEROctetString(this.root));
/* 194 */     if (this.maxIndex >= 0L)
/*     */     {
/* 196 */       aSN1EncodableVector2.add((ASN1Encodable)new DERTaggedObject(false, 0, (ASN1Encodable)new ASN1Integer(this.maxIndex)));
/*     */     }
/*     */     
/* 199 */     aSN1EncodableVector1.add((ASN1Encodable)new DERSequence(aSN1EncodableVector2));
/* 200 */     aSN1EncodableVector1.add((ASN1Encodable)new DERTaggedObject(true, 0, (ASN1Encodable)new DEROctetString(this.bdsState)));
/*     */     
/* 202 */     return (ASN1Primitive)new DERSequence(aSN1EncodableVector1);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/asn1/XMSSMTPrivateKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */