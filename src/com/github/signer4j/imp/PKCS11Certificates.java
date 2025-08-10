/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.ICertificate;
/*     */ import com.github.signer4j.IToken;
/*     */ import com.github.signer4j.cert.ICertificateFactory;
/*     */ import com.github.signer4j.exception.DriverException;
/*     */ import com.github.signer4j.exception.DriverFailException;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.ArrayTools;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.security.cert.CertificateException;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
/*     */ import sun.security.pkcs11.wrapper.PKCS11;
/*     */ import sun.security.pkcs11.wrapper.PKCS11Exception;
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
/*     */ class PKCS11Certificates
/*     */   extends AbstractCertificates
/*     */ {
/*  56 */   private static final Logger LOGGER = LoggerFactory.getLogger(PKCS11Certificates.class);
/*     */   
/*  58 */   private static final long[] EMPTYLONG = new long[0];
/*     */   
/*     */   private static final long FINDOBJECTS_MAX = 10L;
/*     */   
/*  62 */   private static final CK_ATTRIBUTE ATTR_TOKEN_TRUE = new CK_ATTRIBUTE(1L, true);
/*     */   
/*  64 */   private static final CK_ATTRIBUTE ATTR_CLASS_CERT = new CK_ATTRIBUTE(0L, 1L);
/*     */   
/*     */   private final transient PKCS11Token token;
/*     */ 
/*     */   
/*     */   protected PKCS11Certificates(PKCS11Token token, long session, ICertificateFactory factory) throws DriverException {
/*  70 */     this.token = (PKCS11Token)Args.requireNonNull(token, "token is null");
/*  71 */     setup(session, factory);
/*     */   }
/*     */   
/*     */   final PKCS11 getPk() {
/*  75 */     return this.token.getPk();
/*     */   }
/*     */ 
/*     */   
/*     */   public final IToken getToken() {
/*  80 */     return this.token;
/*     */   }
/*     */   
/*     */   private long[] findObjects(long session, CK_ATTRIBUTE[] attrs) throws DriverFailException {
/*  84 */     PKCS11 pk = getPk();
/*  85 */     long[] objects = EMPTYLONG;
/*     */     
/*     */     try {
/*  88 */       pk.C_FindObjectsInit(session, attrs);
/*     */       try {
/*     */         while (true) {
/*  91 */           long[] h = pk.C_FindObjects(session, 10L);
/*  92 */           if (h.length == 0) {
/*     */             break;
/*     */           }
/*  95 */           objects = ArrayTools.concat(objects, h);
/*     */         } 
/*     */       } finally {
/*  98 */         pk.C_FindObjectsFinal(session);
/*     */       } 
/* 100 */     } catch (PKCS11Exception e) {
/* 101 */       throw new DriverFailException("findObjects fail", e);
/*     */     } 
/* 103 */     return objects;
/*     */   }
/*     */   
/*     */   private void setup(long session, ICertificateFactory factory) throws DriverException {
/* 107 */     PKCS11 pk = getPk();
/*     */     
/* 109 */     CK_ATTRIBUTE[] attrs = { ATTR_TOKEN_TRUE, ATTR_CLASS_CERT };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 114 */     long[] objects = findObjects(session, attrs);
/*     */     
/*     */     try {
/* 117 */       for (long object : objects) {
/*     */         
/* 119 */         attrs = new CK_ATTRIBUTE[] { new CK_ATTRIBUTE(17L) };
/*     */         try {
/* 121 */           pk.C_GetAttributeValue(session, object, attrs);
/* 122 */         } catch (PKCS11Exception e) {
/* 123 */           LOGGER.warn("Unabled to get attribute value from object " + object + ". Token:" + this.token, e);
/*     */         } 
/*     */ 
/*     */         
/* 127 */         byte[] certificate = attrs[0].getByteArray();
/*     */         
/* 129 */         if (certificate == null) {
/* 130 */           LOGGER.warn("'CKA_VALUE' of certificate " + object + " is null");
/*     */         }
/*     */         else {
/*     */           
/* 134 */           try (ByteArrayInputStream cert = new ByteArrayInputStream(certificate)) {
/* 135 */             this.certificates.add(factory.create(cert, null));
/* 136 */           } catch (CertificateException|java.io.IOException e) {
/* 137 */             LOGGER.warn("Unabled to create certificate instance from byte[]", e);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 142 */       this.certificates.sort((a, b) -> b.getAfterDate().compareTo(a.getAfterDate()));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PKCS11Certificates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */