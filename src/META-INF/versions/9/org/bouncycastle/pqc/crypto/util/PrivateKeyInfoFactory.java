/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1Set;
/*     */ import org.bouncycastle.asn1.DEROctetString;
/*     */ import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
/*     */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
/*     */ import org.bouncycastle.pqc.asn1.SPHINCS256KeyParams;
/*     */ import org.bouncycastle.pqc.asn1.XMSSKeyParams;
/*     */ import org.bouncycastle.pqc.asn1.XMSSMTKeyParams;
/*     */ import org.bouncycastle.pqc.asn1.XMSSMTPrivateKey;
/*     */ import org.bouncycastle.pqc.asn1.XMSSPrivateKey;
/*     */ import org.bouncycastle.pqc.crypto.lms.Composer;
/*     */ import org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.newhope.NHPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.qtesla.QTESLAPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.sphincs.SPHINCSPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.util.Utils;
/*     */ import org.bouncycastle.pqc.crypto.xmss.BDS;
/*     */ import org.bouncycastle.pqc.crypto.xmss.BDSStateMap;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSMTPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
/*     */ import org.bouncycastle.util.Encodable;
/*     */ import org.bouncycastle.util.Pack;
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
/*     */ public class PrivateKeyInfoFactory
/*     */ {
/*     */   public static PrivateKeyInfo createPrivateKeyInfo(AsymmetricKeyParameter paramAsymmetricKeyParameter) throws IOException {
/*  49 */     return createPrivateKeyInfo(paramAsymmetricKeyParameter, null);
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
/*     */   public static PrivateKeyInfo createPrivateKeyInfo(AsymmetricKeyParameter paramAsymmetricKeyParameter, ASN1Set paramASN1Set) throws IOException {
/*  62 */     if (paramAsymmetricKeyParameter instanceof QTESLAPrivateKeyParameters) {
/*     */       
/*  64 */       QTESLAPrivateKeyParameters qTESLAPrivateKeyParameters = (QTESLAPrivateKeyParameters)paramAsymmetricKeyParameter;
/*     */       
/*  66 */       AlgorithmIdentifier algorithmIdentifier = Utils.qTeslaLookupAlgID(qTESLAPrivateKeyParameters.getSecurityCategory());
/*     */       
/*  68 */       return new PrivateKeyInfo(algorithmIdentifier, (ASN1Encodable)new DEROctetString(qTESLAPrivateKeyParameters.getSecret()), paramASN1Set);
/*     */     } 
/*  70 */     if (paramAsymmetricKeyParameter instanceof SPHINCSPrivateKeyParameters) {
/*     */       
/*  72 */       SPHINCSPrivateKeyParameters sPHINCSPrivateKeyParameters = (SPHINCSPrivateKeyParameters)paramAsymmetricKeyParameter;
/*     */       
/*  74 */       AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.sphincs256, (ASN1Encodable)new SPHINCS256KeyParams(Utils.sphincs256LookupTreeAlgID(sPHINCSPrivateKeyParameters.getTreeDigest())));
/*     */       
/*  76 */       return new PrivateKeyInfo(algorithmIdentifier, (ASN1Encodable)new DEROctetString(sPHINCSPrivateKeyParameters.getKeyData()));
/*     */     } 
/*  78 */     if (paramAsymmetricKeyParameter instanceof NHPrivateKeyParameters) {
/*     */       
/*  80 */       NHPrivateKeyParameters nHPrivateKeyParameters = (NHPrivateKeyParameters)paramAsymmetricKeyParameter;
/*     */       
/*  82 */       AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.newHope);
/*     */       
/*  84 */       short[] arrayOfShort = nHPrivateKeyParameters.getSecData();
/*     */       
/*  86 */       byte[] arrayOfByte = new byte[arrayOfShort.length * 2];
/*  87 */       for (byte b = 0; b != arrayOfShort.length; b++)
/*     */       {
/*  89 */         Pack.shortToLittleEndian(arrayOfShort[b], arrayOfByte, b * 2);
/*     */       }
/*     */       
/*  92 */       return new PrivateKeyInfo(algorithmIdentifier, (ASN1Encodable)new DEROctetString(arrayOfByte));
/*     */     } 
/*  94 */     if (paramAsymmetricKeyParameter instanceof LMSPrivateKeyParameters) {
/*     */       
/*  96 */       LMSPrivateKeyParameters lMSPrivateKeyParameters = (LMSPrivateKeyParameters)paramAsymmetricKeyParameter;
/*     */       
/*  98 */       byte[] arrayOfByte1 = Composer.compose().u32str(1).bytes((Encodable)lMSPrivateKeyParameters).build();
/*  99 */       byte[] arrayOfByte2 = Composer.compose().u32str(1).bytes((Encodable)lMSPrivateKeyParameters.getPublicKey()).build();
/*     */       
/* 101 */       AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_alg_hss_lms_hashsig);
/* 102 */       return new PrivateKeyInfo(algorithmIdentifier, (ASN1Encodable)new DEROctetString(arrayOfByte1), paramASN1Set, arrayOfByte2);
/*     */     } 
/* 104 */     if (paramAsymmetricKeyParameter instanceof HSSPrivateKeyParameters) {
/*     */       
/* 106 */       HSSPrivateKeyParameters hSSPrivateKeyParameters = (HSSPrivateKeyParameters)paramAsymmetricKeyParameter;
/*     */       
/* 108 */       byte[] arrayOfByte1 = Composer.compose().u32str(hSSPrivateKeyParameters.getL()).bytes((Encodable)hSSPrivateKeyParameters).build();
/* 109 */       byte[] arrayOfByte2 = Composer.compose().u32str(hSSPrivateKeyParameters.getL()).bytes((Encodable)hSSPrivateKeyParameters.getPublicKey().getLMSPublicKey()).build();
/*     */       
/* 111 */       AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_alg_hss_lms_hashsig);
/* 112 */       return new PrivateKeyInfo(algorithmIdentifier, (ASN1Encodable)new DEROctetString(arrayOfByte1), paramASN1Set, arrayOfByte2);
/*     */     } 
/* 114 */     if (paramAsymmetricKeyParameter instanceof XMSSPrivateKeyParameters) {
/*     */       
/* 116 */       XMSSPrivateKeyParameters xMSSPrivateKeyParameters = (XMSSPrivateKeyParameters)paramAsymmetricKeyParameter;
/*     */ 
/*     */       
/* 119 */       AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.xmss, (ASN1Encodable)new XMSSKeyParams(xMSSPrivateKeyParameters.getParameters().getHeight(), Utils.xmssLookupTreeAlgID(xMSSPrivateKeyParameters.getTreeDigest())));
/*     */       
/* 121 */       return new PrivateKeyInfo(algorithmIdentifier, (ASN1Encodable)xmssCreateKeyStructure(xMSSPrivateKeyParameters), paramASN1Set);
/*     */     } 
/* 123 */     if (paramAsymmetricKeyParameter instanceof XMSSMTPrivateKeyParameters) {
/*     */       
/* 125 */       XMSSMTPrivateKeyParameters xMSSMTPrivateKeyParameters = (XMSSMTPrivateKeyParameters)paramAsymmetricKeyParameter;
/*     */ 
/*     */       
/* 128 */       AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.xmss_mt, (ASN1Encodable)new XMSSMTKeyParams(xMSSMTPrivateKeyParameters.getParameters().getHeight(), xMSSMTPrivateKeyParameters.getParameters().getLayers(), Utils.xmssLookupTreeAlgID(xMSSMTPrivateKeyParameters.getTreeDigest())));
/*     */       
/* 130 */       return new PrivateKeyInfo(algorithmIdentifier, (ASN1Encodable)xmssmtCreateKeyStructure(xMSSMTPrivateKeyParameters), paramASN1Set);
/*     */     } 
/*     */ 
/*     */     
/* 134 */     throw new IOException("key parameters not recognized");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static XMSSPrivateKey xmssCreateKeyStructure(XMSSPrivateKeyParameters paramXMSSPrivateKeyParameters) throws IOException {
/* 141 */     byte[] arrayOfByte1 = paramXMSSPrivateKeyParameters.getEncoded();
/*     */     
/* 143 */     int i = paramXMSSPrivateKeyParameters.getParameters().getTreeDigestSize();
/* 144 */     int j = paramXMSSPrivateKeyParameters.getParameters().getHeight();
/* 145 */     byte b = 4;
/* 146 */     int k = i;
/* 147 */     int m = i;
/* 148 */     int n = i;
/* 149 */     int i1 = i;
/*     */     
/* 151 */     int i2 = 0;
/* 152 */     int i3 = (int)XMSSUtil.bytesToXBigEndian(arrayOfByte1, i2, b);
/* 153 */     if (!XMSSUtil.isIndexValid(j, i3))
/*     */     {
/* 155 */       throw new IllegalArgumentException("index out of bounds");
/*     */     }
/* 157 */     i2 += b;
/* 158 */     byte[] arrayOfByte2 = XMSSUtil.extractBytesAtOffset(arrayOfByte1, i2, k);
/* 159 */     i2 += k;
/* 160 */     byte[] arrayOfByte3 = XMSSUtil.extractBytesAtOffset(arrayOfByte1, i2, m);
/* 161 */     i2 += m;
/* 162 */     byte[] arrayOfByte4 = XMSSUtil.extractBytesAtOffset(arrayOfByte1, i2, n);
/* 163 */     i2 += n;
/* 164 */     byte[] arrayOfByte5 = XMSSUtil.extractBytesAtOffset(arrayOfByte1, i2, i1);
/* 165 */     i2 += i1;
/*     */     
/* 167 */     byte[] arrayOfByte6 = XMSSUtil.extractBytesAtOffset(arrayOfByte1, i2, arrayOfByte1.length - i2);
/* 168 */     BDS bDS = null;
/*     */     
/*     */     try {
/* 171 */       bDS = (BDS)XMSSUtil.deserialize(arrayOfByte6, BDS.class);
/*     */     }
/* 173 */     catch (ClassNotFoundException classNotFoundException) {
/*     */       
/* 175 */       throw new IOException("cannot parse BDS: " + classNotFoundException.getMessage());
/*     */     } 
/*     */     
/* 178 */     if (bDS.getMaxIndex() != (1 << j) - 1)
/*     */     {
/* 180 */       return new XMSSPrivateKey(i3, arrayOfByte2, arrayOfByte3, arrayOfByte4, arrayOfByte5, arrayOfByte6, bDS.getMaxIndex());
/*     */     }
/*     */ 
/*     */     
/* 184 */     return new XMSSPrivateKey(i3, arrayOfByte2, arrayOfByte3, arrayOfByte4, arrayOfByte5, arrayOfByte6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static XMSSMTPrivateKey xmssmtCreateKeyStructure(XMSSMTPrivateKeyParameters paramXMSSMTPrivateKeyParameters) throws IOException {
/* 191 */     byte[] arrayOfByte1 = paramXMSSMTPrivateKeyParameters.getEncoded();
/*     */     
/* 193 */     int i = paramXMSSMTPrivateKeyParameters.getParameters().getTreeDigestSize();
/* 194 */     int j = paramXMSSMTPrivateKeyParameters.getParameters().getHeight();
/* 195 */     int k = (j + 7) / 8;
/* 196 */     int m = i;
/* 197 */     int n = i;
/* 198 */     int i1 = i;
/* 199 */     int i2 = i;
/*     */     
/* 201 */     int i3 = 0;
/* 202 */     int i4 = (int)XMSSUtil.bytesToXBigEndian(arrayOfByte1, i3, k);
/* 203 */     if (!XMSSUtil.isIndexValid(j, i4))
/*     */     {
/* 205 */       throw new IllegalArgumentException("index out of bounds");
/*     */     }
/* 207 */     i3 += k;
/* 208 */     byte[] arrayOfByte2 = XMSSUtil.extractBytesAtOffset(arrayOfByte1, i3, m);
/* 209 */     i3 += m;
/* 210 */     byte[] arrayOfByte3 = XMSSUtil.extractBytesAtOffset(arrayOfByte1, i3, n);
/* 211 */     i3 += n;
/* 212 */     byte[] arrayOfByte4 = XMSSUtil.extractBytesAtOffset(arrayOfByte1, i3, i1);
/* 213 */     i3 += i1;
/* 214 */     byte[] arrayOfByte5 = XMSSUtil.extractBytesAtOffset(arrayOfByte1, i3, i2);
/* 215 */     i3 += i2;
/*     */     
/* 217 */     byte[] arrayOfByte6 = XMSSUtil.extractBytesAtOffset(arrayOfByte1, i3, arrayOfByte1.length - i3);
/* 218 */     BDSStateMap bDSStateMap = null;
/*     */     
/*     */     try {
/* 221 */       bDSStateMap = (BDSStateMap)XMSSUtil.deserialize(arrayOfByte6, BDSStateMap.class);
/*     */     }
/* 223 */     catch (ClassNotFoundException classNotFoundException) {
/*     */       
/* 225 */       throw new IOException("cannot parse BDSStateMap: " + classNotFoundException.getMessage());
/*     */     } 
/*     */     
/* 228 */     if (bDSStateMap.getMaxIndex() != (1L << j) - 1L)
/*     */     {
/* 230 */       return new XMSSMTPrivateKey(i4, arrayOfByte2, arrayOfByte3, arrayOfByte4, arrayOfByte5, arrayOfByte6, bDSStateMap.getMaxIndex());
/*     */     }
/*     */ 
/*     */     
/* 234 */     return new XMSSMTPrivateKey(i4, arrayOfByte2, arrayOfByte3, arrayOfByte4, arrayOfByte5, arrayOfByte6);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/util/PrivateKeyInfoFactory.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */