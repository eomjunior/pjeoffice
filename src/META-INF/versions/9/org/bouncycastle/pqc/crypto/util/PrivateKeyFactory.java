/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.bouncycastle.asn1.ASN1BitString;
/*     */ import org.bouncycastle.asn1.ASN1InputStream;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.bc.BCObjectIdentifiers;
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
/*     */ import org.bouncycastle.pqc.crypto.lms.HSSPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.newhope.NHPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.qtesla.QTESLAPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.sphincs.SPHINCSPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.util.Utils;
/*     */ import org.bouncycastle.pqc.crypto.xmss.BDS;
/*     */ import org.bouncycastle.pqc.crypto.xmss.BDSStateMap;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSMTParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSMTPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSPrivateKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSUtil;
/*     */ import org.bouncycastle.util.Arrays;
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
/*     */ public class PrivateKeyFactory
/*     */ {
/*     */   public static AsymmetricKeyParameter createKey(byte[] paramArrayOfbyte) throws IOException {
/*  51 */     return createKey(PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray(paramArrayOfbyte)));
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
/*     */   public static AsymmetricKeyParameter createKey(InputStream paramInputStream) throws IOException {
/*  64 */     return createKey(PrivateKeyInfo.getInstance((new ASN1InputStream(paramInputStream)).readObject()));
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
/*     */   public static AsymmetricKeyParameter createKey(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/*  76 */     AlgorithmIdentifier algorithmIdentifier = paramPrivateKeyInfo.getPrivateKeyAlgorithm();
/*  77 */     ASN1ObjectIdentifier aSN1ObjectIdentifier = algorithmIdentifier.getAlgorithm();
/*     */     
/*  79 */     if (aSN1ObjectIdentifier.on(BCObjectIdentifiers.qTESLA)) {
/*     */       
/*  81 */       ASN1OctetString aSN1OctetString = ASN1OctetString.getInstance(paramPrivateKeyInfo.parsePrivateKey());
/*     */       
/*  83 */       return (AsymmetricKeyParameter)new QTESLAPrivateKeyParameters(Utils.qTeslaLookupSecurityCategory(paramPrivateKeyInfo.getPrivateKeyAlgorithm()), aSN1OctetString.getOctets());
/*     */     } 
/*  85 */     if (aSN1ObjectIdentifier.equals((ASN1Primitive)BCObjectIdentifiers.sphincs256))
/*     */     {
/*  87 */       return (AsymmetricKeyParameter)new SPHINCSPrivateKeyParameters(ASN1OctetString.getInstance(paramPrivateKeyInfo.parsePrivateKey()).getOctets(), 
/*  88 */           Utils.sphincs256LookupTreeAlgName(SPHINCS256KeyParams.getInstance(paramPrivateKeyInfo.getPrivateKeyAlgorithm().getParameters())));
/*     */     }
/*  90 */     if (aSN1ObjectIdentifier.equals((ASN1Primitive)BCObjectIdentifiers.newHope))
/*     */     {
/*  92 */       return (AsymmetricKeyParameter)new NHPrivateKeyParameters(convert(ASN1OctetString.getInstance(paramPrivateKeyInfo.parsePrivateKey()).getOctets()));
/*     */     }
/*  94 */     if (aSN1ObjectIdentifier.equals((ASN1Primitive)PKCSObjectIdentifiers.id_alg_hss_lms_hashsig)) {
/*     */       
/*  96 */       byte[] arrayOfByte = ASN1OctetString.getInstance(paramPrivateKeyInfo.parsePrivateKey()).getOctets();
/*  97 */       ASN1BitString aSN1BitString = paramPrivateKeyInfo.getPublicKeyData();
/*     */       
/*  99 */       if (Pack.bigEndianToInt(arrayOfByte, 0) == 1) {
/*     */         
/* 101 */         if (aSN1BitString != null) {
/*     */           
/* 103 */           byte[] arrayOfByte1 = aSN1BitString.getOctets();
/*     */           
/* 105 */           return (AsymmetricKeyParameter)LMSPrivateKeyParameters.getInstance(Arrays.copyOfRange(arrayOfByte, 4, arrayOfByte.length), Arrays.copyOfRange(arrayOfByte1, 4, arrayOfByte1.length));
/*     */         } 
/* 107 */         return (AsymmetricKeyParameter)LMSPrivateKeyParameters.getInstance(Arrays.copyOfRange(arrayOfByte, 4, arrayOfByte.length));
/*     */       } 
/*     */ 
/*     */       
/* 111 */       if (aSN1BitString != null) {
/*     */         
/* 113 */         byte[] arrayOfByte1 = aSN1BitString.getOctets();
/*     */         
/* 115 */         return (AsymmetricKeyParameter)HSSPrivateKeyParameters.getInstance(Arrays.copyOfRange(arrayOfByte, 4, arrayOfByte.length), arrayOfByte1);
/*     */       } 
/* 117 */       return (AsymmetricKeyParameter)HSSPrivateKeyParameters.getInstance(Arrays.copyOfRange(arrayOfByte, 4, arrayOfByte.length));
/*     */     } 
/*     */     
/* 120 */     if (aSN1ObjectIdentifier.equals((ASN1Primitive)BCObjectIdentifiers.xmss)) {
/*     */       
/* 122 */       XMSSKeyParams xMSSKeyParams = XMSSKeyParams.getInstance(paramPrivateKeyInfo.getPrivateKeyAlgorithm().getParameters());
/* 123 */       ASN1ObjectIdentifier aSN1ObjectIdentifier1 = xMSSKeyParams.getTreeDigest().getAlgorithm();
/*     */       
/* 125 */       XMSSPrivateKey xMSSPrivateKey = XMSSPrivateKey.getInstance(paramPrivateKeyInfo.parsePrivateKey());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 135 */         XMSSPrivateKeyParameters.Builder builder = (new XMSSPrivateKeyParameters.Builder(new XMSSParameters(xMSSKeyParams.getHeight(), Utils.getDigest(aSN1ObjectIdentifier1)))).withIndex(xMSSPrivateKey.getIndex()).withSecretKeySeed(xMSSPrivateKey.getSecretKeySeed()).withSecretKeyPRF(xMSSPrivateKey.getSecretKeyPRF()).withPublicSeed(xMSSPrivateKey.getPublicSeed()).withRoot(xMSSPrivateKey.getRoot());
/*     */         
/* 137 */         if (xMSSPrivateKey.getVersion() != 0)
/*     */         {
/* 139 */           builder.withMaxIndex(xMSSPrivateKey.getMaxIndex());
/*     */         }
/*     */         
/* 142 */         if (xMSSPrivateKey.getBdsState() != null) {
/*     */           
/* 144 */           BDS bDS = (BDS)XMSSUtil.deserialize(xMSSPrivateKey.getBdsState(), BDS.class);
/* 145 */           builder.withBDSState(bDS.withWOTSDigest(aSN1ObjectIdentifier1));
/*     */         } 
/*     */         
/* 148 */         return (AsymmetricKeyParameter)builder.build();
/*     */       }
/* 150 */       catch (ClassNotFoundException classNotFoundException) {
/*     */         
/* 152 */         throw new IOException("ClassNotFoundException processing BDS state: " + classNotFoundException.getMessage());
/*     */       } 
/*     */     } 
/* 155 */     if (aSN1ObjectIdentifier.equals((ASN1Primitive)PQCObjectIdentifiers.xmss_mt)) {
/*     */       
/* 157 */       XMSSMTKeyParams xMSSMTKeyParams = XMSSMTKeyParams.getInstance(paramPrivateKeyInfo.getPrivateKeyAlgorithm().getParameters());
/* 158 */       ASN1ObjectIdentifier aSN1ObjectIdentifier1 = xMSSMTKeyParams.getTreeDigest().getAlgorithm();
/*     */ 
/*     */       
/*     */       try {
/* 162 */         XMSSMTPrivateKey xMSSMTPrivateKey = XMSSMTPrivateKey.getInstance(paramPrivateKeyInfo.parsePrivateKey());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 170 */         XMSSMTPrivateKeyParameters.Builder builder = (new XMSSMTPrivateKeyParameters.Builder(new XMSSMTParameters(xMSSMTKeyParams.getHeight(), xMSSMTKeyParams.getLayers(), Utils.getDigest(aSN1ObjectIdentifier1)))).withIndex(xMSSMTPrivateKey.getIndex()).withSecretKeySeed(xMSSMTPrivateKey.getSecretKeySeed()).withSecretKeyPRF(xMSSMTPrivateKey.getSecretKeyPRF()).withPublicSeed(xMSSMTPrivateKey.getPublicSeed()).withRoot(xMSSMTPrivateKey.getRoot());
/*     */         
/* 172 */         if (xMSSMTPrivateKey.getVersion() != 0)
/*     */         {
/* 174 */           builder.withMaxIndex(xMSSMTPrivateKey.getMaxIndex());
/*     */         }
/*     */         
/* 177 */         if (xMSSMTPrivateKey.getBdsState() != null) {
/*     */           
/* 179 */           BDSStateMap bDSStateMap = (BDSStateMap)XMSSUtil.deserialize(xMSSMTPrivateKey.getBdsState(), BDSStateMap.class);
/* 180 */           builder.withBDSState(bDSStateMap.withWOTSDigest(aSN1ObjectIdentifier1));
/*     */         } 
/*     */         
/* 183 */         return (AsymmetricKeyParameter)builder.build();
/*     */       }
/* 185 */       catch (ClassNotFoundException classNotFoundException) {
/*     */         
/* 187 */         throw new IOException("ClassNotFoundException processing BDS state: " + classNotFoundException.getMessage());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 192 */     throw new RuntimeException("algorithm identifier in private key not recognised");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static short[] convert(byte[] paramArrayOfbyte) {
/* 198 */     short[] arrayOfShort = new short[paramArrayOfbyte.length / 2];
/*     */     
/* 200 */     for (byte b = 0; b != arrayOfShort.length; b++)
/*     */     {
/* 202 */       arrayOfShort[b] = Pack.littleEndianToShort(paramArrayOfbyte, b * 2);
/*     */     }
/*     */     
/* 205 */     return arrayOfShort;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/util/PrivateKeyFactory.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */