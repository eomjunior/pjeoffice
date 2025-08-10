/*     */ package META-INF.versions.9.org.bouncycastle.pqc.asn1;
/*     */ 
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1Integer;
/*     */ import org.bouncycastle.asn1.ASN1Object;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Sequence;
/*     */ import org.bouncycastle.asn1.DEROctetString;
/*     */ import org.bouncycastle.asn1.DERSequence;
/*     */ import org.bouncycastle.pqc.crypto.rainbow.util.RainbowUtil;
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
/*     */ public class RainbowPublicKey
/*     */   extends ASN1Object
/*     */ {
/*     */   private ASN1Integer version;
/*     */   private ASN1ObjectIdentifier oid;
/*     */   private ASN1Integer docLength;
/*     */   private byte[][] coeffQuadratic;
/*     */   private byte[][] coeffSingular;
/*     */   private byte[] coeffScalar;
/*     */   
/*     */   private RainbowPublicKey(ASN1Sequence paramASN1Sequence) {
/*  44 */     if (paramASN1Sequence.getObjectAt(0) instanceof ASN1Integer) {
/*     */       
/*  46 */       this.version = ASN1Integer.getInstance(paramASN1Sequence.getObjectAt(0));
/*     */     }
/*     */     else {
/*     */       
/*  50 */       this.oid = ASN1ObjectIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
/*     */     } 
/*     */     
/*  53 */     this.docLength = ASN1Integer.getInstance(paramASN1Sequence.getObjectAt(1));
/*     */     
/*  55 */     ASN1Sequence aSN1Sequence1 = ASN1Sequence.getInstance(paramASN1Sequence.getObjectAt(2));
/*  56 */     this.coeffQuadratic = new byte[aSN1Sequence1.size()][];
/*  57 */     for (byte b1 = 0; b1 < aSN1Sequence1.size(); b1++)
/*     */     {
/*  59 */       this.coeffQuadratic[b1] = ASN1OctetString.getInstance(aSN1Sequence1.getObjectAt(b1)).getOctets();
/*     */     }
/*     */     
/*  62 */     ASN1Sequence aSN1Sequence2 = (ASN1Sequence)paramASN1Sequence.getObjectAt(3);
/*  63 */     this.coeffSingular = new byte[aSN1Sequence2.size()][];
/*  64 */     for (byte b2 = 0; b2 < aSN1Sequence2.size(); b2++)
/*     */     {
/*  66 */       this.coeffSingular[b2] = ASN1OctetString.getInstance(aSN1Sequence2.getObjectAt(b2)).getOctets();
/*     */     }
/*     */     
/*  69 */     ASN1Sequence aSN1Sequence3 = (ASN1Sequence)paramASN1Sequence.getObjectAt(4);
/*  70 */     this.coeffScalar = ASN1OctetString.getInstance(aSN1Sequence3.getObjectAt(0)).getOctets();
/*     */   }
/*     */ 
/*     */   
/*     */   public RainbowPublicKey(int paramInt, short[][] paramArrayOfshort1, short[][] paramArrayOfshort2, short[] paramArrayOfshort) {
/*  75 */     this.version = new ASN1Integer(0L);
/*  76 */     this.docLength = new ASN1Integer(paramInt);
/*  77 */     this.coeffQuadratic = RainbowUtil.convertArray(paramArrayOfshort1);
/*  78 */     this.coeffSingular = RainbowUtil.convertArray(paramArrayOfshort2);
/*  79 */     this.coeffScalar = RainbowUtil.convertArray(paramArrayOfshort);
/*     */   }
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.pqc.asn1.RainbowPublicKey getInstance(Object paramObject) {
/*  84 */     if (paramObject instanceof org.bouncycastle.pqc.asn1.RainbowPublicKey)
/*     */     {
/*  86 */       return (org.bouncycastle.pqc.asn1.RainbowPublicKey)paramObject;
/*     */     }
/*  88 */     if (paramObject != null)
/*     */     {
/*  90 */       return new org.bouncycastle.pqc.asn1.RainbowPublicKey(ASN1Sequence.getInstance(paramObject));
/*     */     }
/*     */     
/*  93 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1Integer getVersion() {
/*  98 */     return this.version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDocLength() {
/* 106 */     return this.docLength.intValueExact();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[][] getCoeffQuadratic() {
/* 114 */     return RainbowUtil.convertArray(this.coeffQuadratic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[][] getCoeffSingular() {
/* 122 */     return RainbowUtil.convertArray(this.coeffSingular);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[] getCoeffScalar() {
/* 130 */     return RainbowUtil.convertArray(this.coeffScalar);
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1Primitive toASN1Primitive() {
/* 135 */     ASN1EncodableVector aSN1EncodableVector1 = new ASN1EncodableVector();
/*     */ 
/*     */     
/* 138 */     if (this.version != null) {
/*     */       
/* 140 */       aSN1EncodableVector1.add((ASN1Encodable)this.version);
/*     */     }
/*     */     else {
/*     */       
/* 144 */       aSN1EncodableVector1.add((ASN1Encodable)this.oid);
/*     */     } 
/*     */ 
/*     */     
/* 148 */     aSN1EncodableVector1.add((ASN1Encodable)this.docLength);
/*     */ 
/*     */     
/* 151 */     ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
/* 152 */     for (byte b1 = 0; b1 < this.coeffQuadratic.length; b1++)
/*     */     {
/* 154 */       aSN1EncodableVector2.add((ASN1Encodable)new DEROctetString(this.coeffQuadratic[b1]));
/*     */     }
/* 156 */     aSN1EncodableVector1.add((ASN1Encodable)new DERSequence(aSN1EncodableVector2));
/*     */ 
/*     */     
/* 159 */     ASN1EncodableVector aSN1EncodableVector3 = new ASN1EncodableVector();
/* 160 */     for (byte b2 = 0; b2 < this.coeffSingular.length; b2++)
/*     */     {
/* 162 */       aSN1EncodableVector3.add((ASN1Encodable)new DEROctetString(this.coeffSingular[b2]));
/*     */     }
/* 164 */     aSN1EncodableVector1.add((ASN1Encodable)new DERSequence(aSN1EncodableVector3));
/*     */ 
/*     */     
/* 167 */     ASN1EncodableVector aSN1EncodableVector4 = new ASN1EncodableVector();
/* 168 */     aSN1EncodableVector4.add((ASN1Encodable)new DEROctetString(this.coeffScalar));
/* 169 */     aSN1EncodableVector1.add((ASN1Encodable)new DERSequence(aSN1EncodableVector4));
/*     */ 
/*     */     
/* 172 */     return (ASN1Primitive)new DERSequence(aSN1EncodableVector1);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/asn1/RainbowPublicKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */