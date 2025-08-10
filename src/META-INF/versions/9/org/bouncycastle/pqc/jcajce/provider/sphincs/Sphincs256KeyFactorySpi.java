/*     */ package META-INF.versions.9.org.bouncycastle.pqc.jcajce.provider.sphincs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.KeyFactorySpi;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.KeySpec;
/*     */ import java.security.spec.PKCS8EncodedKeySpec;
/*     */ import java.security.spec.X509EncodedKeySpec;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
/*     */ import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
/*     */ import org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;
/*     */ import org.bouncycastle.pqc.jcajce.provider.sphincs.BCSphincs256PrivateKey;
/*     */ import org.bouncycastle.pqc.jcajce.provider.sphincs.BCSphincs256PublicKey;
/*     */ 
/*     */ 
/*     */ public class Sphincs256KeyFactorySpi
/*     */   extends KeyFactorySpi
/*     */   implements AsymmetricKeyInfoConverter
/*     */ {
/*     */   public PrivateKey engineGeneratePrivate(KeySpec paramKeySpec) throws InvalidKeySpecException {
/*  26 */     if (paramKeySpec instanceof PKCS8EncodedKeySpec) {
/*     */ 
/*     */       
/*  29 */       byte[] arrayOfByte = ((PKCS8EncodedKeySpec)paramKeySpec).getEncoded();
/*     */ 
/*     */       
/*     */       try {
/*  33 */         return generatePrivate(PrivateKeyInfo.getInstance(ASN1Primitive.fromByteArray(arrayOfByte)));
/*     */       }
/*  35 */       catch (Exception exception) {
/*     */         
/*  37 */         throw new InvalidKeySpecException(exception.toString());
/*     */       } 
/*     */     } 
/*     */     
/*  41 */     throw new InvalidKeySpecException("Unsupported key specification: " + paramKeySpec
/*  42 */         .getClass() + ".");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PublicKey engineGeneratePublic(KeySpec paramKeySpec) throws InvalidKeySpecException {
/*  48 */     if (paramKeySpec instanceof X509EncodedKeySpec) {
/*     */ 
/*     */       
/*  51 */       byte[] arrayOfByte = ((X509EncodedKeySpec)paramKeySpec).getEncoded();
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/*  56 */         return generatePublic(SubjectPublicKeyInfo.getInstance(arrayOfByte));
/*     */       }
/*  58 */       catch (Exception exception) {
/*     */         
/*  60 */         throw new InvalidKeySpecException(exception.toString());
/*     */       } 
/*     */     } 
/*     */     
/*  64 */     throw new InvalidKeySpecException("Unknown key specification: " + paramKeySpec + ".");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final KeySpec engineGetKeySpec(Key paramKey, Class<?> paramClass) throws InvalidKeySpecException {
/*  70 */     if (paramKey instanceof BCSphincs256PrivateKey) {
/*     */       
/*  72 */       if (PKCS8EncodedKeySpec.class.isAssignableFrom(paramClass))
/*     */       {
/*  74 */         return new PKCS8EncodedKeySpec(paramKey.getEncoded());
/*     */       }
/*     */     }
/*  77 */     else if (paramKey instanceof BCSphincs256PublicKey) {
/*     */       
/*  79 */       if (X509EncodedKeySpec.class.isAssignableFrom(paramClass))
/*     */       {
/*  81 */         return new X509EncodedKeySpec(paramKey.getEncoded());
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/*  86 */       throw new InvalidKeySpecException("Unsupported key type: " + paramKey
/*  87 */           .getClass() + ".");
/*     */     } 
/*     */     
/*  90 */     throw new InvalidKeySpecException("Unknown key specification: " + paramClass + ".");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Key engineTranslateKey(Key paramKey) throws InvalidKeyException {
/*  97 */     if (paramKey instanceof BCSphincs256PrivateKey || paramKey instanceof BCSphincs256PublicKey)
/*     */     {
/*  99 */       return paramKey;
/*     */     }
/*     */     
/* 102 */     throw new InvalidKeyException("Unsupported key type");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PrivateKey generatePrivate(PrivateKeyInfo paramPrivateKeyInfo) throws IOException {
/* 108 */     return (PrivateKey)new BCSphincs256PrivateKey(paramPrivateKeyInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PublicKey generatePublic(SubjectPublicKeyInfo paramSubjectPublicKeyInfo) throws IOException {
/* 114 */     return (PublicKey)new BCSphincs256PublicKey(paramSubjectPublicKeyInfo);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/pqc/jcajce/provider/sphincs/Sphincs256KeyFactorySpi.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */