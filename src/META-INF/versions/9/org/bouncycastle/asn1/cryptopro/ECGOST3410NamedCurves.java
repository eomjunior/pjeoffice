/*     */ package META-INF.versions.9.org.bouncycastle.asn1.cryptopro;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.rosstandart.RosstandartObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.x9.X9ECParameters;
/*     */ import org.bouncycastle.asn1.x9.X9ECPoint;
/*     */ import org.bouncycastle.crypto.params.ECDomainParameters;
/*     */ import org.bouncycastle.math.ec.ECConstants;
/*     */ import org.bouncycastle.math.ec.ECCurve;
/*     */ import org.bouncycastle.math.ec.ECPoint;
/*     */ import org.bouncycastle.math.ec.WNafUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ECGOST3410NamedCurves
/*     */ {
/*     */   private static ECPoint configureBasepoint(ECCurve paramECCurve, BigInteger paramBigInteger1, BigInteger paramBigInteger2) {
/*  24 */     ECPoint eCPoint = paramECCurve.createPoint(paramBigInteger1, paramBigInteger2);
/*  25 */     WNafUtil.configureBasepoint(eCPoint);
/*  26 */     return eCPoint;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ECCurve configureCurve(ECCurve paramECCurve) {
/*  31 */     return paramECCurve;
/*     */   }
/*     */   
/*  34 */   static final Hashtable objIds = new Hashtable<>();
/*  35 */   static final Hashtable params = new Hashtable<>();
/*  36 */   static final Hashtable names = new Hashtable<>();
/*     */ 
/*     */   
/*     */   static {
/*  40 */     BigInteger bigInteger1 = new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639319");
/*  41 */     BigInteger bigInteger2 = new BigInteger("115792089237316195423570985008687907853073762908499243225378155805079068850323");
/*     */     
/*  43 */     ECCurve eCCurve = configureCurve((ECCurve)new ECCurve.Fp(bigInteger1, new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639316"), new BigInteger("166"), bigInteger2, ECConstants.ONE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  51 */     ECDomainParameters eCDomainParameters = new ECDomainParameters(eCCurve, configureBasepoint(eCCurve, new BigInteger("1"), new BigInteger("64033881142927202683649881450433473985931760268884941288852745803908878638612")), bigInteger2, ECConstants.ONE);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  56 */     params.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_A, eCDomainParameters);
/*     */     
/*  58 */     bigInteger1 = new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639319");
/*  59 */     bigInteger2 = new BigInteger("115792089237316195423570985008687907853073762908499243225378155805079068850323");
/*     */     
/*  61 */     eCCurve = configureCurve((ECCurve)new ECCurve.Fp(bigInteger1, new BigInteger("115792089237316195423570985008687907853269984665640564039457584007913129639316"), new BigInteger("166"), bigInteger2, ECConstants.ONE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  69 */     eCDomainParameters = new ECDomainParameters(eCCurve, configureBasepoint(eCCurve, new BigInteger("1"), new BigInteger("64033881142927202683649881450433473985931760268884941288852745803908878638612")), bigInteger2, ECConstants.ONE);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     params.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchA, eCDomainParameters);
/*     */     
/*  76 */     bigInteger1 = new BigInteger("57896044618658097711785492504343953926634992332820282019728792003956564823193");
/*  77 */     bigInteger2 = new BigInteger("57896044618658097711785492504343953927102133160255826820068844496087732066703");
/*     */     
/*  79 */     eCCurve = configureCurve((ECCurve)new ECCurve.Fp(bigInteger1, new BigInteger("57896044618658097711785492504343953926634992332820282019728792003956564823190"), new BigInteger("28091019353058090096996979000309560759124368558014865957655842872397301267595"), bigInteger2, ECConstants.ONE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  87 */     eCDomainParameters = new ECDomainParameters(eCCurve, configureBasepoint(eCCurve, new BigInteger("1"), new BigInteger("28792665814854611296992347458380284135028636778229113005756334730996303888124")), bigInteger2, ECConstants.ONE);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     params.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_B, eCDomainParameters);
/*     */     
/*  94 */     bigInteger1 = new BigInteger("70390085352083305199547718019018437841079516630045180471284346843705633502619");
/*  95 */     bigInteger2 = new BigInteger("70390085352083305199547718019018437840920882647164081035322601458352298396601");
/*     */     
/*  97 */     eCCurve = configureCurve((ECCurve)new ECCurve.Fp(bigInteger1, new BigInteger("70390085352083305199547718019018437841079516630045180471284346843705633502616"), new BigInteger("32858"), bigInteger2, ECConstants.ONE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     eCDomainParameters = new ECDomainParameters(eCCurve, configureBasepoint(eCCurve, new BigInteger("0"), new BigInteger("29818893917731240733471273240314769927240550812383695689146495261604565990247")), bigInteger2, ECConstants.ONE);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     params.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchB, eCDomainParameters);
/*     */     
/* 112 */     bigInteger1 = new BigInteger("70390085352083305199547718019018437841079516630045180471284346843705633502619");
/* 113 */     bigInteger2 = new BigInteger("70390085352083305199547718019018437840920882647164081035322601458352298396601");
/*     */     
/* 115 */     eCCurve = configureCurve((ECCurve)new ECCurve.Fp(bigInteger1, new BigInteger("70390085352083305199547718019018437841079516630045180471284346843705633502616"), new BigInteger("32858"), bigInteger2, ECConstants.ONE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 123 */     eCDomainParameters = new ECDomainParameters(eCCurve, configureBasepoint(eCCurve, new BigInteger("0"), new BigInteger("29818893917731240733471273240314769927240550812383695689146495261604565990247")), bigInteger2, ECConstants.ONE);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 128 */     params.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_C, eCDomainParameters);
/*     */ 
/*     */     
/* 131 */     bigInteger1 = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFD97", 16);
/* 132 */     bigInteger2 = new BigInteger("400000000000000000000000000000000FD8CDDFC87B6635C115AF556C360C67", 16);
/*     */     
/* 134 */     eCCurve = configureCurve((ECCurve)new ECCurve.Fp(bigInteger1, new BigInteger("C2173F1513981673AF4892C23035A27CE25E2013BF95AA33B22C656F277E7335", 16), new BigInteger("295F9BAE7428ED9CCC20E7C359A9D41A22FCCD9108E17BF7BA9337A6F8AE9513", 16), bigInteger2, ECConstants.FOUR));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     eCDomainParameters = new ECDomainParameters(eCCurve, configureBasepoint(eCCurve, new BigInteger("91E38443A5E82C0D880923425712B2BB658B9196932E02C78B2582FE742DAA28", 16), new BigInteger("32879423AB1A0375895786C4BB46E9565FDE0B5344766740AF268ADB32322E5C", 16)), bigInteger2, ECConstants.FOUR);
/*     */ 
/*     */ 
/*     */     
/* 146 */     params.put(RosstandartObjectIdentifiers.id_tc26_gost_3410_12_256_paramSetA, eCDomainParameters);
/*     */     
/* 148 */     bigInteger1 = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFDC7", 16);
/* 149 */     bigInteger2 = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF27E69532F48D89116FF22B8D4E0560609B4B38ABFAD2B85DCACDB1411F10B275", 16);
/*     */     
/* 151 */     eCCurve = configureCurve((ECCurve)new ECCurve.Fp(bigInteger1, new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFDC4", 16), new BigInteger("E8C2505DEDFC86DDC1BD0B2B6667F1DA34B82574761CB0E879BD081CFD0B6265EE3CB090F30D27614CB4574010DA90DD862EF9D4EBEE4761503190785A71C760", 16), bigInteger2, ECConstants.ONE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 159 */     eCDomainParameters = new ECDomainParameters(eCCurve, configureBasepoint(eCCurve, new BigInteger("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003"), new BigInteger("7503CFE87A836AE3A61B8816E25450E6CE5E1C93ACF1ABC1778064FDCBEFA921DF1626BE4FD036E93D75E6A50E3A41E98028FE5FC235F5B889A589CB5215F2A4", 16)), bigInteger2, ECConstants.ONE);
/*     */ 
/*     */ 
/*     */     
/* 163 */     params.put(RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512_paramSetA, eCDomainParameters);
/*     */     
/* 165 */     bigInteger1 = new BigInteger("8000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000006F", 16);
/* 166 */     bigInteger2 = new BigInteger("800000000000000000000000000000000000000000000000000000000000000149A1EC142565A545ACFDB77BD9D40CFA8B996712101BEA0EC6346C54374F25BD", 16);
/*     */     
/* 168 */     eCCurve = configureCurve((ECCurve)new ECCurve.Fp(bigInteger1, new BigInteger("8000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000006C", 16), new BigInteger("687D1B459DC841457E3E06CF6F5E2517B97C7D614AF138BCBF85DC806C4B289F3E965D2DB1416D217F8B276FAD1AB69C50F78BEE1FA3106EFB8CCBC7C5140116", 16), bigInteger2, ECConstants.ONE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 176 */     eCDomainParameters = new ECDomainParameters(eCCurve, configureBasepoint(eCCurve, new BigInteger("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002"), new BigInteger("1A8F7EDA389B094C2C071E3647A8940F3C123B697578C213BE6DD9E6C8EC7335DCB228FD1EDF4A39152CBCAAF8C0398828041055F94CEEEC7E21340780FE41BD", 16)), bigInteger2, ECConstants.ONE);
/*     */ 
/*     */ 
/*     */     
/* 180 */     params.put(RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512_paramSetB, eCDomainParameters);
/*     */     
/* 182 */     bigInteger1 = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFDC7", 16);
/* 183 */     bigInteger2 = new BigInteger("3FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC98CDBA46506AB004C33A9FF5147502CC8EDA9E7A769A12694623CEF47F023ED", 16);
/*     */     
/* 185 */     eCCurve = configureCurve((ECCurve)new ECCurve.Fp(bigInteger1, new BigInteger("DC9203E514A721875485A529D2C722FB187BC8980EB866644DE41C68E143064546E861C0E2C9EDD92ADE71F46FCF50FF2AD97F951FDA9F2A2EB6546F39689BD3", 16), new BigInteger("B4C4EE28CEBC6C2C8AC12952CF37F16AC7EFB6A9F69F4B57FFDA2E4F0DE5ADE038CBC2FFF719D2C18DE0284B8BFEF3B52B8CC7A5F5BF0A3C8D2319A5312557E1", 16), bigInteger2, ECConstants.FOUR));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 193 */     eCDomainParameters = new ECDomainParameters(eCCurve, configureBasepoint(eCCurve, new BigInteger("E2E31EDFC23DE7BDEBE241CE593EF5DE2295B7A9CBAEF021D385F7074CEA043AA27272A7AE602BF2A7B9033DB9ED3610C6FB85487EAE97AAC5BC7928C1950148", 16), new BigInteger("F5CE40D95B5EB899ABBCCFF5911CB8577939804D6527378B8C108C3D2090FF9BE18E2D33E3021ED2EF32D85822423B6304F726AA854BAE07D0396E9A9ADDC40F", 16)), bigInteger2, ECConstants.FOUR);
/*     */ 
/*     */ 
/*     */     
/* 197 */     params.put(RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512_paramSetC, eCDomainParameters);
/*     */ 
/*     */ 
/*     */     
/* 201 */     objIds.put("GostR3410-2001-CryptoPro-A", CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_A);
/* 202 */     objIds.put("GostR3410-2001-CryptoPro-B", CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_B);
/* 203 */     objIds.put("GostR3410-2001-CryptoPro-C", CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_C);
/* 204 */     objIds.put("GostR3410-2001-CryptoPro-XchA", CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchA);
/* 205 */     objIds.put("GostR3410-2001-CryptoPro-XchB", CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchB);
/* 206 */     objIds.put("Tc26-Gost-3410-12-256-paramSetA", RosstandartObjectIdentifiers.id_tc26_gost_3410_12_256_paramSetA);
/* 207 */     objIds.put("Tc26-Gost-3410-12-512-paramSetA", RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512_paramSetA);
/* 208 */     objIds.put("Tc26-Gost-3410-12-512-paramSetB", RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512_paramSetB);
/* 209 */     objIds.put("Tc26-Gost-3410-12-512-paramSetC", RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512_paramSetC);
/*     */     
/* 211 */     names.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_A, "GostR3410-2001-CryptoPro-A");
/* 212 */     names.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_B, "GostR3410-2001-CryptoPro-B");
/* 213 */     names.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_C, "GostR3410-2001-CryptoPro-C");
/* 214 */     names.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchA, "GostR3410-2001-CryptoPro-XchA");
/* 215 */     names.put(CryptoProObjectIdentifiers.gostR3410_2001_CryptoPro_XchB, "GostR3410-2001-CryptoPro-XchB");
/* 216 */     names.put(RosstandartObjectIdentifiers.id_tc26_gost_3410_12_256_paramSetA, "Tc26-Gost-3410-12-256-paramSetA");
/* 217 */     names.put(RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512_paramSetA, "Tc26-Gost-3410-12-512-paramSetA");
/* 218 */     names.put(RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512_paramSetB, "Tc26-Gost-3410-12-512-paramSetB");
/* 219 */     names.put(RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512_paramSetC, "Tc26-Gost-3410-12-512-paramSetC");
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
/*     */   public static ECDomainParameters getByOID(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 232 */     return (ECDomainParameters)params.get(paramASN1ObjectIdentifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public static X9ECParameters getByOIDX9(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 237 */     ECDomainParameters eCDomainParameters = (ECDomainParameters)params.get(paramASN1ObjectIdentifier);
/* 238 */     return (eCDomainParameters == null) ? null : new X9ECParameters(eCDomainParameters.getCurve(), new X9ECPoint(eCDomainParameters.getG(), false), eCDomainParameters.getN(), eCDomainParameters
/* 239 */         .getH(), eCDomainParameters.getSeed());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Enumeration getNames() {
/* 248 */     return names.elements();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ECDomainParameters getByName(String paramString) {
/* 256 */     ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)objIds.get(paramString);
/* 257 */     return (aSN1ObjectIdentifier == null) ? null : (ECDomainParameters)params.get(aSN1ObjectIdentifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public static X9ECParameters getByNameX9(String paramString) {
/* 262 */     ASN1ObjectIdentifier aSN1ObjectIdentifier = (ASN1ObjectIdentifier)objIds.get(paramString);
/* 263 */     return (aSN1ObjectIdentifier == null) ? null : getByOIDX9(aSN1ObjectIdentifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getName(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
/* 272 */     return (String)names.get(paramASN1ObjectIdentifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ASN1ObjectIdentifier getOID(String paramString) {
/* 277 */     return (ASN1ObjectIdentifier)objIds.get(paramString);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/cryptopro/ECGOST3410NamedCurves.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */