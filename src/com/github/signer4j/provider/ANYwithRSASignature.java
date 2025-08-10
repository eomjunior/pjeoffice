/*     */ package com.github.signer4j.provider;
/*     */ 
/*     */ import com.github.utils4j.INameable;
/*     */ import com.github.utils4j.imp.NotImplementedException;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.InvalidParameterException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.Signature;
/*     */ import java.security.SignatureException;
/*     */ import java.security.spec.AlgorithmParameterSpec;
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
/*     */ public abstract class ANYwithRSASignature
/*     */   extends Signature
/*     */ {
/*     */   private final Signature signature;
/*     */   
/*     */   protected ANYwithRSASignature(String algorithm) throws Exception {
/*  54 */     super(algorithm);
/*  55 */     this.signature = Signature.getInstance("NONEwithRSA");
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
/*  60 */     this.signature.initVerify(publicKey);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
/*  65 */     this.signature.initSign(privateKey);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void engineUpdate(byte b) throws SignatureException {
/*  70 */     getDigester().update(b);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void engineUpdate(byte[] b, int off, int len) throws SignatureException {
/*  75 */     getDigester().update(b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void engineUpdate(ByteBuffer buffer) {
/*  80 */     getDigester().update(buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final byte[] engineSign() throws SignatureException {
/*  85 */     this.signature.update(getDigester().digest());
/*  86 */     return this.signature.sign();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final boolean engineVerify(byte[] sigBytes) throws SignatureException {
/*  91 */     this.signature.update(getDigester().digest());
/*  92 */     return this.signature.verify(sigBytes);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void engineSetParameter(String param, Object value) throws InvalidParameterException {
/*  97 */     throw new NotImplementedException("Deprecated method, please use engineSetParameter");
/*     */   }
/*     */ 
/*     */   
/*     */   protected final Object engineGetParameter(String param) throws InvalidParameterException {
/* 102 */     throw new NotImplementedException("Deprecated method, please use engineGetParameter");
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void engineSetParameter(AlgorithmParameterSpec params) throws InvalidAlgorithmParameterException {
/* 107 */     String hashName = Strings.trim(((HashName)params).getName());
/*     */     try {
/* 109 */       setupDigester(hashName);
/* 110 */     } catch (NoSuchAlgorithmException|NoSuchProviderException e) {
/* 111 */       throw new InvalidAlgorithmParameterException("Algorithm '" + hashName + " is not supported", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract MessageDigest getDigester();
/*     */   
/*     */   protected void setupDigester(String name) throws NoSuchAlgorithmException, NoSuchProviderException {}
/*     */   
/*     */   public static interface HashName extends AlgorithmParameterSpec, INameable {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/provider/ANYwithRSASignature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */