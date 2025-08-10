/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.rainbow;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.PrivateKey;
/*     */ import java.util.Arrays;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.DERNull;
/*     */ import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
/*     */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*     */ import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
/*     */ import org.bouncycastle.pqc.asn1.RainbowPrivateKey;
/*     */ import org.bouncycastle.pqc.crypto.rainbow.Layer;
/*     */ import org.bouncycastle.pqc.crypto.rainbow.RainbowPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.rainbow.util.RainbowUtil;
/*     */ import org.bouncycastle.pqc.jcajce.spec.RainbowPrivateKeySpec;
/*     */ import org.bouncycastle.util.Arrays;
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
/*     */ public class BCRainbowPrivateKey
/*     */   implements PrivateKey
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private short[][] A1inv;
/*     */   private short[] b1;
/*     */   private short[][] A2inv;
/*     */   private short[] b2;
/*     */   private Layer[] layers;
/*     */   private int[] vi;
/*     */   
/*     */   public BCRainbowPrivateKey(short[][] paramArrayOfshort1, short[] paramArrayOfshort2, short[][] paramArrayOfshort3, short[] paramArrayOfshort4, int[] paramArrayOfint, Layer[] paramArrayOfLayer) {
/*  69 */     this.A1inv = paramArrayOfshort1;
/*  70 */     this.b1 = paramArrayOfshort2;
/*  71 */     this.A2inv = paramArrayOfshort3;
/*  72 */     this.b2 = paramArrayOfshort4;
/*  73 */     this.vi = paramArrayOfint;
/*  74 */     this.layers = paramArrayOfLayer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BCRainbowPrivateKey(RainbowPrivateKeySpec paramRainbowPrivateKeySpec) {
/*  84 */     this(paramRainbowPrivateKeySpec.getInvA1(), paramRainbowPrivateKeySpec.getB1(), paramRainbowPrivateKeySpec.getInvA2(), paramRainbowPrivateKeySpec
/*  85 */         .getB2(), paramRainbowPrivateKeySpec.getVi(), paramRainbowPrivateKeySpec.getLayers());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BCRainbowPrivateKey(RainbowPrivateKeyParameters paramRainbowPrivateKeyParameters) {
/*  91 */     this(paramRainbowPrivateKeyParameters.getInvA1(), paramRainbowPrivateKeyParameters.getB1(), paramRainbowPrivateKeyParameters.getInvA2(), paramRainbowPrivateKeyParameters.getB2(), paramRainbowPrivateKeyParameters.getVi(), paramRainbowPrivateKeyParameters.getLayers());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[][] getInvA1() {
/* 101 */     return this.A1inv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[] getB1() {
/* 111 */     return this.b1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[] getB2() {
/* 121 */     return this.b2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[][] getInvA2() {
/* 131 */     return this.A2inv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Layer[] getLayers() {
/* 141 */     return this.layers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getVi() {
/* 151 */     return this.vi;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 162 */     if (paramObject == null || !(paramObject instanceof org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPrivateKey))
/*     */     {
/* 164 */       return false;
/*     */     }
/* 166 */     org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPrivateKey bCRainbowPrivateKey = (org.bouncycastle.pqc.jcajce.provider.rainbow.BCRainbowPrivateKey)paramObject;
/*     */     
/* 168 */     boolean bool = true;
/*     */     
/* 170 */     bool = (bool && RainbowUtil.equals(this.A1inv, bCRainbowPrivateKey.getInvA1()));
/* 171 */     bool = (bool && RainbowUtil.equals(this.A2inv, bCRainbowPrivateKey.getInvA2()));
/* 172 */     bool = (bool && RainbowUtil.equals(this.b1, bCRainbowPrivateKey.getB1()));
/* 173 */     bool = (bool && RainbowUtil.equals(this.b2, bCRainbowPrivateKey.getB2()));
/* 174 */     bool = (bool && Arrays.equals(this.vi, bCRainbowPrivateKey.getVi()));
/* 175 */     if (this.layers.length != (bCRainbowPrivateKey.getLayers()).length)
/*     */     {
/* 177 */       return false;
/*     */     }
/* 179 */     for (int i = this.layers.length - 1; i >= 0; i--)
/*     */     {
/* 181 */       bool &= this.layers[i].equals(bCRainbowPrivateKey.getLayers()[i]);
/*     */     }
/* 183 */     return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 188 */     int i = this.layers.length;
/*     */     
/* 190 */     i = i * 37 + Arrays.hashCode(this.A1inv);
/* 191 */     i = i * 37 + Arrays.hashCode(this.b1);
/* 192 */     i = i * 37 + Arrays.hashCode(this.A2inv);
/* 193 */     i = i * 37 + Arrays.hashCode(this.b2);
/* 194 */     i = i * 37 + Arrays.hashCode(this.vi);
/*     */     
/* 196 */     for (int j = this.layers.length - 1; j >= 0; j--)
/*     */     {
/* 198 */       i = i * 37 + this.layers[j].hashCode();
/*     */     }
/*     */ 
/*     */     
/* 202 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getAlgorithm() {
/* 210 */     return "Rainbow";
/*     */   }
/*     */   
/*     */   public byte[] getEncoded() {
/*     */     PrivateKeyInfo privateKeyInfo;
/* 215 */     RainbowPrivateKey rainbowPrivateKey = new RainbowPrivateKey(this.A1inv, this.b1, this.A2inv, this.b2, this.vi, this.layers);
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 220 */       AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.rainbow, (ASN1Encodable)DERNull.INSTANCE);
/* 221 */       privateKeyInfo = new PrivateKeyInfo(algorithmIdentifier, (ASN1Encodable)rainbowPrivateKey);
/*     */     }
/* 223 */     catch (IOException iOException) {
/*     */       
/* 225 */       return null;
/*     */     } 
/*     */     
/*     */     try {
/* 229 */       return privateKeyInfo.getEncoded();
/*     */     
/*     */     }
/* 232 */     catch (IOException iOException) {
/*     */       
/* 234 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFormat() {
/* 240 */     return "PKCS#8";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/rainbow/BCRainbowPrivateKey.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */