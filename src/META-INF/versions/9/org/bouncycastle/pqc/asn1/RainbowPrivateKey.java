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
/*     */ import org.bouncycastle.pqc.crypto.rainbow.Layer;
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
/*     */ public class RainbowPrivateKey
/*     */   extends ASN1Object
/*     */ {
/*     */   private ASN1Integer version;
/*     */   private ASN1ObjectIdentifier oid;
/*     */   private byte[][] invA1;
/*     */   private byte[] b1;
/*     */   private byte[][] invA2;
/*     */   private byte[] b2;
/*     */   private byte[] vi;
/*     */   private Layer[] layers;
/*     */   
/*     */   private RainbowPrivateKey(ASN1Sequence paramASN1Sequence) {
/*  60 */     if (paramASN1Sequence.getObjectAt(0) instanceof ASN1Integer) {
/*     */       
/*  62 */       this.version = ASN1Integer.getInstance(paramASN1Sequence.getObjectAt(0));
/*     */     }
/*     */     else {
/*     */       
/*  66 */       this.oid = ASN1ObjectIdentifier.getInstance(paramASN1Sequence.getObjectAt(0));
/*     */     } 
/*     */ 
/*     */     
/*  70 */     ASN1Sequence aSN1Sequence1 = (ASN1Sequence)paramASN1Sequence.getObjectAt(1);
/*  71 */     this.invA1 = new byte[aSN1Sequence1.size()][];
/*  72 */     for (byte b1 = 0; b1 < aSN1Sequence1.size(); b1++)
/*     */     {
/*  74 */       this.invA1[b1] = ((ASN1OctetString)aSN1Sequence1.getObjectAt(b1)).getOctets();
/*     */     }
/*     */ 
/*     */     
/*  78 */     ASN1Sequence aSN1Sequence2 = (ASN1Sequence)paramASN1Sequence.getObjectAt(2);
/*  79 */     this.b1 = ((ASN1OctetString)aSN1Sequence2.getObjectAt(0)).getOctets();
/*     */ 
/*     */     
/*  82 */     ASN1Sequence aSN1Sequence3 = (ASN1Sequence)paramASN1Sequence.getObjectAt(3);
/*  83 */     this.invA2 = new byte[aSN1Sequence3.size()][];
/*  84 */     for (byte b2 = 0; b2 < aSN1Sequence3.size(); b2++)
/*     */     {
/*  86 */       this.invA2[b2] = ((ASN1OctetString)aSN1Sequence3.getObjectAt(b2)).getOctets();
/*     */     }
/*     */ 
/*     */     
/*  90 */     ASN1Sequence aSN1Sequence4 = (ASN1Sequence)paramASN1Sequence.getObjectAt(4);
/*  91 */     this.b2 = ((ASN1OctetString)aSN1Sequence4.getObjectAt(0)).getOctets();
/*     */ 
/*     */     
/*  94 */     ASN1Sequence aSN1Sequence5 = (ASN1Sequence)paramASN1Sequence.getObjectAt(5);
/*  95 */     this.vi = ((ASN1OctetString)aSN1Sequence5.getObjectAt(0)).getOctets();
/*     */ 
/*     */     
/*  98 */     ASN1Sequence aSN1Sequence6 = (ASN1Sequence)paramASN1Sequence.getObjectAt(6);
/*     */     
/* 100 */     byte[][][][] arrayOfByte1 = new byte[aSN1Sequence6.size()][][][];
/* 101 */     byte[][][][] arrayOfByte2 = new byte[aSN1Sequence6.size()][][][];
/* 102 */     byte[][][] arrayOfByte = new byte[aSN1Sequence6.size()][][];
/* 103 */     byte[][] arrayOfByte3 = new byte[aSN1Sequence6.size()][];
/*     */     int i;
/* 105 */     for (i = 0; i < aSN1Sequence6.size(); i++) {
/*     */       
/* 107 */       ASN1Sequence aSN1Sequence7 = (ASN1Sequence)aSN1Sequence6.getObjectAt(i);
/*     */ 
/*     */       
/* 110 */       ASN1Sequence aSN1Sequence8 = (ASN1Sequence)aSN1Sequence7.getObjectAt(0);
/* 111 */       arrayOfByte1[i] = new byte[aSN1Sequence8.size()][][];
/* 112 */       for (byte b4 = 0; b4 < aSN1Sequence8.size(); b4++) {
/*     */         
/* 114 */         ASN1Sequence aSN1Sequence = (ASN1Sequence)aSN1Sequence8.getObjectAt(b4);
/* 115 */         arrayOfByte1[i][b4] = new byte[aSN1Sequence.size()][];
/* 116 */         for (byte b = 0; b < aSN1Sequence.size(); b++)
/*     */         {
/* 118 */           arrayOfByte1[i][b4][b] = ((ASN1OctetString)aSN1Sequence.getObjectAt(b)).getOctets();
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 123 */       ASN1Sequence aSN1Sequence9 = (ASN1Sequence)aSN1Sequence7.getObjectAt(1);
/* 124 */       arrayOfByte2[i] = new byte[aSN1Sequence9.size()][][];
/* 125 */       for (byte b5 = 0; b5 < aSN1Sequence9.size(); b5++) {
/*     */         
/* 127 */         ASN1Sequence aSN1Sequence = (ASN1Sequence)aSN1Sequence9.getObjectAt(b5);
/* 128 */         arrayOfByte2[i][b5] = new byte[aSN1Sequence.size()][];
/* 129 */         for (byte b = 0; b < aSN1Sequence.size(); b++)
/*     */         {
/* 131 */           arrayOfByte2[i][b5][b] = ((ASN1OctetString)aSN1Sequence.getObjectAt(b)).getOctets();
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 136 */       ASN1Sequence aSN1Sequence10 = (ASN1Sequence)aSN1Sequence7.getObjectAt(2);
/* 137 */       arrayOfByte[i] = new byte[aSN1Sequence10.size()][];
/* 138 */       for (byte b6 = 0; b6 < aSN1Sequence10.size(); b6++)
/*     */       {
/* 140 */         arrayOfByte[i][b6] = ((ASN1OctetString)aSN1Sequence10.getObjectAt(b6)).getOctets();
/*     */       }
/*     */ 
/*     */       
/* 144 */       arrayOfByte3[i] = ((ASN1OctetString)aSN1Sequence7.getObjectAt(3)).getOctets();
/*     */     } 
/*     */     
/* 147 */     i = this.vi.length - 1;
/* 148 */     this.layers = new Layer[i];
/* 149 */     for (byte b3 = 0; b3 < i; b3++) {
/*     */ 
/*     */       
/* 152 */       Layer layer = new Layer(this.vi[b3], this.vi[b3 + 1], RainbowUtil.convertArray(arrayOfByte1[b3]), RainbowUtil.convertArray(arrayOfByte2[b3]), RainbowUtil.convertArray(arrayOfByte[b3]), RainbowUtil.convertArray(arrayOfByte3[b3]));
/* 153 */       this.layers[b3] = layer;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RainbowPrivateKey(short[][] paramArrayOfshort1, short[] paramArrayOfshort2, short[][] paramArrayOfshort3, short[] paramArrayOfshort4, int[] paramArrayOfint, Layer[] paramArrayOfLayer) {
/* 161 */     this.version = new ASN1Integer(1L);
/* 162 */     this.invA1 = RainbowUtil.convertArray(paramArrayOfshort1);
/* 163 */     this.b1 = RainbowUtil.convertArray(paramArrayOfshort2);
/* 164 */     this.invA2 = RainbowUtil.convertArray(paramArrayOfshort3);
/* 165 */     this.b2 = RainbowUtil.convertArray(paramArrayOfshort4);
/* 166 */     this.vi = RainbowUtil.convertIntArray(paramArrayOfint);
/* 167 */     this.layers = paramArrayOfLayer;
/*     */   }
/*     */ 
/*     */   
/*     */   public static org.bouncycastle.pqc.asn1.RainbowPrivateKey getInstance(Object paramObject) {
/* 172 */     if (paramObject instanceof org.bouncycastle.pqc.asn1.RainbowPrivateKey)
/*     */     {
/* 174 */       return (org.bouncycastle.pqc.asn1.RainbowPrivateKey)paramObject;
/*     */     }
/* 176 */     if (paramObject != null)
/*     */     {
/* 178 */       return new org.bouncycastle.pqc.asn1.RainbowPrivateKey(ASN1Sequence.getInstance(paramObject));
/*     */     }
/*     */     
/* 181 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1Integer getVersion() {
/* 186 */     return this.version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[][] getInvA1() {
/* 196 */     return RainbowUtil.convertArray(this.invA1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[] getB1() {
/* 206 */     return RainbowUtil.convertArray(this.b1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[] getB2() {
/* 216 */     return RainbowUtil.convertArray(this.b2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[][] getInvA2() {
/* 226 */     return RainbowUtil.convertArray(this.invA2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Layer[] getLayers() {
/* 236 */     return this.layers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getVi() {
/* 246 */     return RainbowUtil.convertArraytoInt(this.vi);
/*     */   }
/*     */ 
/*     */   
/*     */   public ASN1Primitive toASN1Primitive() {
/* 251 */     ASN1EncodableVector aSN1EncodableVector1 = new ASN1EncodableVector();
/*     */ 
/*     */     
/* 254 */     if (this.version != null) {
/*     */       
/* 256 */       aSN1EncodableVector1.add((ASN1Encodable)this.version);
/*     */     }
/*     */     else {
/*     */       
/* 260 */       aSN1EncodableVector1.add((ASN1Encodable)this.oid);
/*     */     } 
/*     */ 
/*     */     
/* 264 */     ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
/* 265 */     for (byte b1 = 0; b1 < this.invA1.length; b1++)
/*     */     {
/* 267 */       aSN1EncodableVector2.add((ASN1Encodable)new DEROctetString(this.invA1[b1]));
/*     */     }
/* 269 */     aSN1EncodableVector1.add((ASN1Encodable)new DERSequence(aSN1EncodableVector2));
/*     */ 
/*     */     
/* 272 */     ASN1EncodableVector aSN1EncodableVector3 = new ASN1EncodableVector();
/* 273 */     aSN1EncodableVector3.add((ASN1Encodable)new DEROctetString(this.b1));
/* 274 */     aSN1EncodableVector1.add((ASN1Encodable)new DERSequence(aSN1EncodableVector3));
/*     */ 
/*     */     
/* 277 */     ASN1EncodableVector aSN1EncodableVector4 = new ASN1EncodableVector();
/* 278 */     for (byte b2 = 0; b2 < this.invA2.length; b2++)
/*     */     {
/* 280 */       aSN1EncodableVector4.add((ASN1Encodable)new DEROctetString(this.invA2[b2]));
/*     */     }
/* 282 */     aSN1EncodableVector1.add((ASN1Encodable)new DERSequence(aSN1EncodableVector4));
/*     */ 
/*     */     
/* 285 */     ASN1EncodableVector aSN1EncodableVector5 = new ASN1EncodableVector();
/* 286 */     aSN1EncodableVector5.add((ASN1Encodable)new DEROctetString(this.b2));
/* 287 */     aSN1EncodableVector1.add((ASN1Encodable)new DERSequence(aSN1EncodableVector5));
/*     */ 
/*     */     
/* 290 */     ASN1EncodableVector aSN1EncodableVector6 = new ASN1EncodableVector();
/* 291 */     aSN1EncodableVector6.add((ASN1Encodable)new DEROctetString(this.vi));
/* 292 */     aSN1EncodableVector1.add((ASN1Encodable)new DERSequence(aSN1EncodableVector6));
/*     */ 
/*     */     
/* 295 */     ASN1EncodableVector aSN1EncodableVector7 = new ASN1EncodableVector();
/*     */     
/* 297 */     for (byte b3 = 0; b3 < this.layers.length; b3++) {
/*     */       
/* 299 */       ASN1EncodableVector aSN1EncodableVector8 = new ASN1EncodableVector();
/*     */ 
/*     */       
/* 302 */       byte[][][] arrayOfByte1 = RainbowUtil.convertArray(this.layers[b3].getCoeffAlpha());
/* 303 */       ASN1EncodableVector aSN1EncodableVector9 = new ASN1EncodableVector();
/* 304 */       for (byte b4 = 0; b4 < arrayOfByte1.length; b4++) {
/*     */         
/* 306 */         ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
/* 307 */         for (byte b = 0; b < (arrayOfByte1[b4]).length; b++)
/*     */         {
/* 309 */           aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(arrayOfByte1[b4][b]));
/*     */         }
/* 311 */         aSN1EncodableVector9.add((ASN1Encodable)new DERSequence(aSN1EncodableVector));
/*     */       } 
/* 313 */       aSN1EncodableVector8.add((ASN1Encodable)new DERSequence(aSN1EncodableVector9));
/*     */ 
/*     */       
/* 316 */       byte[][][] arrayOfByte2 = RainbowUtil.convertArray(this.layers[b3].getCoeffBeta());
/* 317 */       ASN1EncodableVector aSN1EncodableVector10 = new ASN1EncodableVector();
/* 318 */       for (byte b5 = 0; b5 < arrayOfByte2.length; b5++) {
/*     */         
/* 320 */         ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
/* 321 */         for (byte b = 0; b < (arrayOfByte2[b5]).length; b++)
/*     */         {
/* 323 */           aSN1EncodableVector.add((ASN1Encodable)new DEROctetString(arrayOfByte2[b5][b]));
/*     */         }
/* 325 */         aSN1EncodableVector10.add((ASN1Encodable)new DERSequence(aSN1EncodableVector));
/*     */       } 
/* 327 */       aSN1EncodableVector8.add((ASN1Encodable)new DERSequence(aSN1EncodableVector10));
/*     */ 
/*     */       
/* 330 */       byte[][] arrayOfByte = RainbowUtil.convertArray(this.layers[b3].getCoeffGamma());
/* 331 */       ASN1EncodableVector aSN1EncodableVector11 = new ASN1EncodableVector();
/* 332 */       for (byte b6 = 0; b6 < arrayOfByte.length; b6++)
/*     */       {
/* 334 */         aSN1EncodableVector11.add((ASN1Encodable)new DEROctetString(arrayOfByte[b6]));
/*     */       }
/* 336 */       aSN1EncodableVector8.add((ASN1Encodable)new DERSequence(aSN1EncodableVector11));
/*     */ 
/*     */       
/* 339 */       aSN1EncodableVector8.add((ASN1Encodable)new DEROctetString(RainbowUtil.convertArray(this.layers[b3].getCoeffEta())));
/*     */ 
/*     */       
/* 342 */       aSN1EncodableVector7.add((ASN1Encodable)new DERSequence(aSN1EncodableVector8));
/*     */     } 
/*     */     
/* 345 */     aSN1EncodableVector1.add((ASN1Encodable)new DERSequence(aSN1EncodableVector7));
/*     */     
/* 347 */     return (ASN1Primitive)new DERSequence(aSN1EncodableVector1);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/asn1/RainbowPrivateKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */