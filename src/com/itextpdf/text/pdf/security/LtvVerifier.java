/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import com.itextpdf.text.log.Level;
/*     */ import com.itextpdf.text.log.Logger;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import com.itextpdf.text.pdf.AcroFields;
/*     */ import com.itextpdf.text.pdf.PRStream;
/*     */ import com.itextpdf.text.pdf.PdfArray;
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfReader;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509CRL;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import org.bouncycastle.cert.ocsp.BasicOCSPResp;
/*     */ import org.bouncycastle.cert.ocsp.OCSPException;
/*     */ import org.bouncycastle.cert.ocsp.OCSPResp;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LtvVerifier
/*     */   extends RootStoreVerifier
/*     */ {
/*  78 */   protected static final Logger LOGGER = LoggerFactory.getLogger(LtvVerifier.class);
/*     */ 
/*     */   
/*  81 */   protected LtvVerification.CertificateOption option = LtvVerification.CertificateOption.SIGNING_CERTIFICATE;
/*     */ 
/*     */   
/*     */   protected boolean verifyRootCertificate = true;
/*     */ 
/*     */   
/*     */   protected PdfReader reader;
/*     */ 
/*     */   
/*     */   protected AcroFields fields;
/*     */ 
/*     */   
/*     */   protected Date signDate;
/*     */ 
/*     */   
/*     */   protected String signatureName;
/*     */   
/*     */   protected PdfPKCS7 pkcs7;
/*     */   
/*     */   protected boolean latestRevision = true;
/*     */   
/*     */   protected PdfDictionary dss;
/*     */ 
/*     */   
/*     */   public LtvVerifier(PdfReader reader) throws GeneralSecurityException {
/* 106 */     super((CertificateVerifier)null);
/* 107 */     this.reader = reader;
/* 108 */     this.fields = reader.getAcroFields();
/* 109 */     List<String> names = this.fields.getSignatureNames();
/* 110 */     this.signatureName = names.get(names.size() - 1);
/* 111 */     this.signDate = new Date();
/* 112 */     this.pkcs7 = coversWholeDocument();
/* 113 */     if (LOGGER.isLogging(Level.INFO)) {
/* 114 */       LOGGER.info(String.format("Checking %ssignature %s", new Object[] { this.pkcs7.isTsp() ? "document-level timestamp " : "", this.signatureName }));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerifier(CertificateVerifier verifier) {
/* 123 */     this.verifier = verifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCertificateOption(LtvVerification.CertificateOption option) {
/* 131 */     this.option = option;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerifyRootCertificate(boolean verifyRootCertificate) {
/* 138 */     this.verifyRootCertificate = verifyRootCertificate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected PdfPKCS7 coversWholeDocument() throws GeneralSecurityException {
/* 148 */     PdfPKCS7 pkcs7 = this.fields.verifySignature(this.signatureName);
/* 149 */     if (this.fields.signatureCoversWholeDocument(this.signatureName)) {
/* 150 */       LOGGER.info("The timestamp covers whole document.");
/*     */     } else {
/*     */       
/* 153 */       throw new VerificationException(null, "Signature doesn't cover whole document.");
/*     */     } 
/* 155 */     if (pkcs7.verify()) {
/* 156 */       LOGGER.info("The signed document has not been modified.");
/* 157 */       return pkcs7;
/*     */     } 
/*     */     
/* 160 */     throw new VerificationException(null, "The document was altered after the final signature was applied.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<VerificationOK> verify(List<VerificationOK> result) throws IOException, GeneralSecurityException {
/* 170 */     if (result == null)
/* 171 */       result = new ArrayList<VerificationOK>(); 
/* 172 */     while (this.pkcs7 != null) {
/* 173 */       result.addAll(verifySignature());
/*     */     }
/* 175 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<VerificationOK> verifySignature() throws GeneralSecurityException, IOException {
/* 184 */     LOGGER.info("Verifying signature.");
/* 185 */     List<VerificationOK> result = new ArrayList<VerificationOK>();
/*     */     
/* 187 */     Certificate[] chain = this.pkcs7.getSignCertificateChain();
/* 188 */     verifyChain(chain);
/*     */     
/* 190 */     int total = 1;
/* 191 */     if (LtvVerification.CertificateOption.WHOLE_CHAIN.equals(this.option)) {
/* 192 */       total = chain.length;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 197 */     for (int i = 0; i < total; ) {
/*     */       
/* 199 */       X509Certificate signCert = (X509Certificate)chain[i++];
/*     */       
/* 201 */       X509Certificate issuerCert = null;
/* 202 */       if (i < chain.length) {
/* 203 */         issuerCert = (X509Certificate)chain[i];
/*     */       }
/* 205 */       LOGGER.info(signCert.getSubjectDN().getName());
/* 206 */       List<VerificationOK> list = verify(signCert, issuerCert, this.signDate);
/* 207 */       if (list.size() == 0) {
/*     */         try {
/* 209 */           signCert.verify(signCert.getPublicKey());
/* 210 */           if (this.latestRevision && chain.length > 1) {
/* 211 */             list.add(new VerificationOK(signCert, (Class)getClass(), "Root certificate in final revision"));
/*     */           }
/* 213 */           if (list.size() == 0 && this.verifyRootCertificate) {
/* 214 */             throw new GeneralSecurityException();
/*     */           }
/* 216 */           if (chain.length > 1) {
/* 217 */             list.add(new VerificationOK(signCert, (Class)getClass(), "Root certificate passed without checking"));
/*     */           }
/* 219 */         } catch (GeneralSecurityException e) {
/* 220 */           throw new VerificationException(signCert, "Couldn't verify with CRL or OCSP or trusted anchor");
/*     */         } 
/*     */       }
/* 223 */       result.addAll(list);
/*     */     } 
/*     */     
/* 226 */     switchToPreviousRevision();
/* 227 */     return result;
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
/*     */   public void verifyChain(Certificate[] chain) throws GeneralSecurityException {
/* 239 */     for (int i = 0; i < chain.length; i++) {
/* 240 */       X509Certificate cert = (X509Certificate)chain[i];
/*     */       
/* 242 */       cert.checkValidity(this.signDate);
/*     */       
/* 244 */       if (i > 0)
/* 245 */         chain[i - 1].verify(chain[i].getPublicKey()); 
/*     */     } 
/* 247 */     LOGGER.info("All certificates are valid on " + this.signDate.toString());
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
/*     */ 
/*     */   
/*     */   public List<VerificationOK> verify(X509Certificate signCert, X509Certificate issuerCert, Date signDate) throws GeneralSecurityException, IOException {
/* 262 */     RootStoreVerifier rootStoreVerifier = new RootStoreVerifier(this.verifier);
/* 263 */     rootStoreVerifier.setRootStore(this.rootStore);
/*     */     
/* 265 */     CRLVerifier crlVerifier = new CRLVerifier(rootStoreVerifier, getCRLsFromDSS());
/* 266 */     crlVerifier.setRootStore(this.rootStore);
/* 267 */     crlVerifier.setOnlineCheckingAllowed((this.latestRevision || this.onlineCheckingAllowed));
/*     */     
/* 269 */     OCSPVerifier ocspVerifier = new OCSPVerifier(crlVerifier, getOCSPResponsesFromDSS());
/* 270 */     ocspVerifier.setRootStore(this.rootStore);
/* 271 */     ocspVerifier.setOnlineCheckingAllowed((this.latestRevision || this.onlineCheckingAllowed));
/*     */     
/* 273 */     return ocspVerifier.verify(signCert, issuerCert, signDate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void switchToPreviousRevision() throws IOException, GeneralSecurityException {
/* 282 */     LOGGER.info("Switching to previous revision.");
/* 283 */     this.latestRevision = false;
/* 284 */     this.dss = this.reader.getCatalog().getAsDict(PdfName.DSS);
/* 285 */     Calendar cal = this.pkcs7.getTimeStampDate();
/* 286 */     if (cal == null) {
/* 287 */       cal = this.pkcs7.getSignDate();
/*     */     }
/* 289 */     this.signDate = cal.getTime();
/* 290 */     List<String> names = this.fields.getSignatureNames();
/* 291 */     if (names.size() > 1) {
/* 292 */       this.signatureName = names.get(names.size() - 2);
/* 293 */       this.reader = new PdfReader(this.fields.extractRevision(this.signatureName));
/* 294 */       this.fields = this.reader.getAcroFields();
/* 295 */       names = this.fields.getSignatureNames();
/* 296 */       this.signatureName = names.get(names.size() - 1);
/* 297 */       this.pkcs7 = coversWholeDocument();
/* 298 */       if (LOGGER.isLogging(Level.INFO)) {
/* 299 */         LOGGER.info(String.format("Checking %ssignature %s", new Object[] { this.pkcs7.isTsp() ? "document-level timestamp " : "", this.signatureName }));
/*     */       }
/*     */     } else {
/*     */       
/* 303 */       LOGGER.info("No signatures in revision");
/* 304 */       this.pkcs7 = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<X509CRL> getCRLsFromDSS() throws GeneralSecurityException, IOException {
/* 315 */     List<X509CRL> crls = new ArrayList<X509CRL>();
/* 316 */     if (this.dss == null)
/* 317 */       return crls; 
/* 318 */     PdfArray crlarray = this.dss.getAsArray(PdfName.CRLS);
/* 319 */     if (crlarray == null)
/* 320 */       return crls; 
/* 321 */     CertificateFactory cf = CertificateFactory.getInstance("X.509");
/* 322 */     for (int i = 0; i < crlarray.size(); i++) {
/* 323 */       PRStream stream = (PRStream)crlarray.getAsStream(i);
/* 324 */       X509CRL crl = (X509CRL)cf.generateCRL(new ByteArrayInputStream(PdfReader.getStreamBytes(stream)));
/* 325 */       crls.add(crl);
/*     */     } 
/* 327 */     return crls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BasicOCSPResp> getOCSPResponsesFromDSS() throws IOException, GeneralSecurityException {
/* 337 */     List<BasicOCSPResp> ocsps = new ArrayList<BasicOCSPResp>();
/* 338 */     if (this.dss == null)
/* 339 */       return ocsps; 
/* 340 */     PdfArray ocsparray = this.dss.getAsArray(PdfName.OCSPS);
/* 341 */     if (ocsparray == null)
/* 342 */       return ocsps; 
/* 343 */     for (int i = 0; i < ocsparray.size(); i++) {
/* 344 */       PRStream stream = (PRStream)ocsparray.getAsStream(i);
/* 345 */       OCSPResp ocspResponse = new OCSPResp(PdfReader.getStreamBytes(stream));
/* 346 */       if (ocspResponse.getStatus() == 0)
/*     */         try {
/* 348 */           ocsps.add((BasicOCSPResp)ocspResponse.getResponseObject());
/* 349 */         } catch (OCSPException e) {
/* 350 */           throw new GeneralSecurityException(e);
/*     */         }  
/*     */     } 
/* 353 */     return ocsps;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/LtvVerifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */