/*     */ package META-INF.versions.9.org.bouncycastle.pqc.crypto.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.DEROctetString;
/*     */ import org.bouncycastle.asn1.isara.IsaraObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*     */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*     */ import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
/*     */ import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
/*     */ import org.bouncycastle.pqc.asn1.SPHINCS256KeyParams;
/*     */ import org.bouncycastle.pqc.asn1.XMSSKeyParams;
/*     */ import org.bouncycastle.pqc.asn1.XMSSMTKeyParams;
/*     */ import org.bouncycastle.pqc.asn1.XMSSMTPublicKey;
/*     */ import org.bouncycastle.pqc.asn1.XMSSPublicKey;
/*     */ import org.bouncycastle.pqc.crypto.lms.Composer;
/*     */ import org.bouncycastle.pqc.crypto.lms.HSSPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.lms.LMSPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.newhope.NHPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.qtesla.QTESLAPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.sphincs.SPHINCSPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.util.Utils;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSMTPublicKeyParameters;
/*     */ import org.bouncycastle.pqc.crypto.xmss.XMSSPublicKeyParameters;
/*     */ import org.bouncycastle.util.Encodable;
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
/*     */ public class SubjectPublicKeyInfoFactory
/*     */ {
/*     */   public static SubjectPublicKeyInfo createSubjectPublicKeyInfo(AsymmetricKeyParameter paramAsymmetricKeyParameter) throws IOException {
/*  46 */     if (paramAsymmetricKeyParameter instanceof QTESLAPublicKeyParameters) {
/*     */       
/*  48 */       QTESLAPublicKeyParameters qTESLAPublicKeyParameters = (QTESLAPublicKeyParameters)paramAsymmetricKeyParameter;
/*  49 */       AlgorithmIdentifier algorithmIdentifier = Utils.qTeslaLookupAlgID(qTESLAPublicKeyParameters.getSecurityCategory());
/*     */       
/*  51 */       return new SubjectPublicKeyInfo(algorithmIdentifier, qTESLAPublicKeyParameters.getPublicData());
/*     */     } 
/*  53 */     if (paramAsymmetricKeyParameter instanceof SPHINCSPublicKeyParameters) {
/*     */       
/*  55 */       SPHINCSPublicKeyParameters sPHINCSPublicKeyParameters = (SPHINCSPublicKeyParameters)paramAsymmetricKeyParameter;
/*     */ 
/*     */       
/*  58 */       AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.sphincs256, (ASN1Encodable)new SPHINCS256KeyParams(Utils.sphincs256LookupTreeAlgID(sPHINCSPublicKeyParameters.getTreeDigest())));
/*  59 */       return new SubjectPublicKeyInfo(algorithmIdentifier, sPHINCSPublicKeyParameters.getKeyData());
/*     */     } 
/*  61 */     if (paramAsymmetricKeyParameter instanceof NHPublicKeyParameters) {
/*     */       
/*  63 */       NHPublicKeyParameters nHPublicKeyParameters = (NHPublicKeyParameters)paramAsymmetricKeyParameter;
/*     */       
/*  65 */       AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.newHope);
/*  66 */       return new SubjectPublicKeyInfo(algorithmIdentifier, nHPublicKeyParameters.getPubData());
/*     */     } 
/*  68 */     if (paramAsymmetricKeyParameter instanceof LMSPublicKeyParameters) {
/*     */       
/*  70 */       LMSPublicKeyParameters lMSPublicKeyParameters = (LMSPublicKeyParameters)paramAsymmetricKeyParameter;
/*     */       
/*  72 */       byte[] arrayOfByte = Composer.compose().u32str(1).bytes((Encodable)lMSPublicKeyParameters).build();
/*     */       
/*  74 */       AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_alg_hss_lms_hashsig);
/*  75 */       return new SubjectPublicKeyInfo(algorithmIdentifier, (ASN1Encodable)new DEROctetString(arrayOfByte));
/*     */     } 
/*  77 */     if (paramAsymmetricKeyParameter instanceof HSSPublicKeyParameters) {
/*     */       
/*  79 */       HSSPublicKeyParameters hSSPublicKeyParameters = (HSSPublicKeyParameters)paramAsymmetricKeyParameter;
/*     */       
/*  81 */       byte[] arrayOfByte = Composer.compose().u32str(hSSPublicKeyParameters.getL()).bytes((Encodable)hSSPublicKeyParameters.getLMSPublicKey()).build();
/*     */       
/*  83 */       AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_alg_hss_lms_hashsig);
/*  84 */       return new SubjectPublicKeyInfo(algorithmIdentifier, (ASN1Encodable)new DEROctetString(arrayOfByte));
/*     */     } 
/*  86 */     if (paramAsymmetricKeyParameter instanceof XMSSPublicKeyParameters) {
/*     */       
/*  88 */       XMSSPublicKeyParameters xMSSPublicKeyParameters = (XMSSPublicKeyParameters)paramAsymmetricKeyParameter;
/*     */       
/*  90 */       byte[] arrayOfByte1 = xMSSPublicKeyParameters.getPublicSeed();
/*  91 */       byte[] arrayOfByte2 = xMSSPublicKeyParameters.getRoot();
/*  92 */       byte[] arrayOfByte3 = xMSSPublicKeyParameters.getEncoded();
/*  93 */       if (arrayOfByte3.length > arrayOfByte1.length + arrayOfByte2.length) {
/*     */         
/*  95 */         AlgorithmIdentifier algorithmIdentifier1 = new AlgorithmIdentifier(IsaraObjectIdentifiers.id_alg_xmss);
/*     */         
/*  97 */         return new SubjectPublicKeyInfo(algorithmIdentifier1, (ASN1Encodable)new DEROctetString(arrayOfByte3));
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 102 */       AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.xmss, (ASN1Encodable)new XMSSKeyParams(xMSSPublicKeyParameters.getParameters().getHeight(), Utils.xmssLookupTreeAlgID(xMSSPublicKeyParameters.getTreeDigest())));
/*     */       
/* 104 */       return new SubjectPublicKeyInfo(algorithmIdentifier, (ASN1Encodable)new XMSSPublicKey(arrayOfByte1, arrayOfByte2));
/*     */     } 
/*     */     
/* 107 */     if (paramAsymmetricKeyParameter instanceof XMSSMTPublicKeyParameters) {
/*     */       
/* 109 */       XMSSMTPublicKeyParameters xMSSMTPublicKeyParameters = (XMSSMTPublicKeyParameters)paramAsymmetricKeyParameter;
/*     */       
/* 111 */       byte[] arrayOfByte1 = xMSSMTPublicKeyParameters.getPublicSeed();
/* 112 */       byte[] arrayOfByte2 = xMSSMTPublicKeyParameters.getRoot();
/* 113 */       byte[] arrayOfByte3 = xMSSMTPublicKeyParameters.getEncoded();
/* 114 */       if (arrayOfByte3.length > arrayOfByte1.length + arrayOfByte2.length) {
/*     */         
/* 116 */         AlgorithmIdentifier algorithmIdentifier1 = new AlgorithmIdentifier(IsaraObjectIdentifiers.id_alg_xmssmt);
/*     */         
/* 118 */         return new SubjectPublicKeyInfo(algorithmIdentifier1, (ASN1Encodable)new DEROctetString(arrayOfByte3));
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 123 */       AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PQCObjectIdentifiers.xmss_mt, (ASN1Encodable)new XMSSMTKeyParams(xMSSMTPublicKeyParameters.getParameters().getHeight(), xMSSMTPublicKeyParameters.getParameters().getLayers(), Utils.xmssLookupTreeAlgID(xMSSMTPublicKeyParameters.getTreeDigest())));
/* 124 */       return new SubjectPublicKeyInfo(algorithmIdentifier, (ASN1Encodable)new XMSSMTPublicKey(xMSSMTPublicKeyParameters.getPublicSeed(), xMSSMTPublicKeyParameters.getRoot()));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 129 */     throw new IOException("key parameters not recognized");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/crypto/util/SubjectPublicKeyInfoFactory.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */