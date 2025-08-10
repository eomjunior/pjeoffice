/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import com.itextpdf.text.Utilities;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.log.Logger;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import com.itextpdf.text.pdf.AcroFields;
/*     */ import com.itextpdf.text.pdf.PRIndirectReference;
/*     */ import com.itextpdf.text.pdf.PdfArray;
/*     */ import com.itextpdf.text.pdf.PdfDeveloperExtension;
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfIndirectReference;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfReader;
/*     */ import com.itextpdf.text.pdf.PdfStamper;
/*     */ import com.itextpdf.text.pdf.PdfStream;
/*     */ import com.itextpdf.text.pdf.PdfString;
/*     */ import com.itextpdf.text.pdf.PdfWriter;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*     */ import org.bouncycastle.asn1.ASN1Enumerated;
/*     */ import org.bouncycastle.asn1.ASN1InputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.DEROctetString;
/*     */ import org.bouncycastle.asn1.DERSequence;
/*     */ import org.bouncycastle.asn1.DERTaggedObject;
/*     */ import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
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
/*     */ public class LtvVerification
/*     */ {
/*  93 */   private Logger LOGGER = LoggerFactory.getLogger(LtvVerification.class);
/*     */   
/*     */   private PdfStamper stp;
/*     */   private PdfWriter writer;
/*     */   private PdfReader reader;
/*     */   private AcroFields acroFields;
/*  99 */   private Map<PdfName, ValidationData> validated = new HashMap<PdfName, ValidationData>();
/*     */ 
/*     */   
/*     */   private boolean used = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Level
/*     */   {
/* 108 */     OCSP,
/*     */ 
/*     */ 
/*     */     
/* 112 */     CRL,
/*     */ 
/*     */ 
/*     */     
/* 116 */     OCSP_CRL,
/*     */ 
/*     */ 
/*     */     
/* 120 */     OCSP_OPTIONAL_CRL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum CertificateOption
/*     */   {
/* 130 */     SIGNING_CERTIFICATE,
/*     */ 
/*     */ 
/*     */     
/* 134 */     WHOLE_CHAIN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum CertificateInclusion
/*     */   {
/* 145 */     YES,
/*     */ 
/*     */ 
/*     */     
/* 149 */     NO;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LtvVerification(PdfStamper stp) {
/* 158 */     this.stp = stp;
/* 159 */     this.writer = stp.getWriter();
/* 160 */     this.reader = stp.getReader();
/* 161 */     this.acroFields = stp.getAcroFields();
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
/*     */   
/*     */   public boolean addVerification(String signatureName, OcspClient ocsp, CrlClient crl, CertificateOption certOption, Level level, CertificateInclusion certInclude) throws IOException, GeneralSecurityException {
/* 177 */     if (this.used)
/* 178 */       throw new IllegalStateException(MessageLocalization.getComposedMessage("verification.already.output", new Object[0])); 
/* 179 */     PdfPKCS7 pk = this.acroFields.verifySignature(signatureName);
/* 180 */     this.LOGGER.info("Adding verification for " + signatureName);
/* 181 */     Certificate[] xc = pk.getCertificates();
/*     */     
/* 183 */     X509Certificate signingCert = pk.getSigningCertificate();
/* 184 */     ValidationData vd = new ValidationData();
/* 185 */     for (int k = 0; k < xc.length; k++) {
/* 186 */       X509Certificate cert = (X509Certificate)xc[k];
/* 187 */       this.LOGGER.info("Certificate: " + cert.getSubjectDN());
/* 188 */       if (certOption != CertificateOption.SIGNING_CERTIFICATE || cert
/* 189 */         .equals(signingCert)) {
/*     */ 
/*     */         
/* 192 */         byte[] ocspEnc = null;
/* 193 */         if (ocsp != null && level != Level.CRL) {
/* 194 */           ocspEnc = ocsp.getEncoded(cert, getParent(cert, xc), null);
/* 195 */           if (ocspEnc != null) {
/* 196 */             vd.ocsps.add(buildOCSPResponse(ocspEnc));
/* 197 */             this.LOGGER.info("OCSP added");
/*     */           } 
/*     */         } 
/* 200 */         if (crl != null && (level == Level.CRL || level == Level.OCSP_CRL || (level == Level.OCSP_OPTIONAL_CRL && ocspEnc == null))) {
/* 201 */           Collection<byte[]> cims = crl.getEncoded(cert, null);
/* 202 */           if (cims != null) {
/* 203 */             for (byte[] cim : cims) {
/* 204 */               boolean dup = false;
/* 205 */               for (byte[] b : vd.crls) {
/* 206 */                 if (Arrays.equals(b, cim)) {
/* 207 */                   dup = true;
/*     */                   break;
/*     */                 } 
/*     */               } 
/* 211 */               if (!dup) {
/* 212 */                 vd.crls.add(cim);
/* 213 */                 this.LOGGER.info("CRL added");
/*     */               } 
/*     */             } 
/*     */           }
/*     */         } 
/* 218 */         if (certInclude == CertificateInclusion.YES)
/* 219 */           vd.certs.add(cert.getEncoded()); 
/*     */       } 
/*     */     } 
/* 222 */     if (vd.crls.isEmpty() && vd.ocsps.isEmpty())
/* 223 */       return false; 
/* 224 */     this.validated.put(getSignatureHashKey(signatureName), vd);
/* 225 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private X509Certificate getParent(X509Certificate cert, Certificate[] certs) {
/* 236 */     for (int i = 0; i < certs.length; i++) {
/* 237 */       X509Certificate parent = (X509Certificate)certs[i];
/* 238 */       if (cert.getIssuerDN().equals(parent.getSubjectDN())) {
/*     */         
/*     */         try {
/* 241 */           cert.verify(parent.getPublicKey());
/* 242 */           return parent;
/* 243 */         } catch (Exception exception) {}
/*     */       }
/*     */     } 
/*     */     
/* 247 */     return null;
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
/*     */   public boolean addVerification(String signatureName, Collection<byte[]> ocsps, Collection<byte[]> crls, Collection<byte[]> certs) throws IOException, GeneralSecurityException {
/* 260 */     if (this.used)
/* 261 */       throw new IllegalStateException(MessageLocalization.getComposedMessage("verification.already.output", new Object[0])); 
/* 262 */     ValidationData vd = new ValidationData();
/* 263 */     if (ocsps != null) {
/* 264 */       for (byte[] ocsp : ocsps) {
/* 265 */         vd.ocsps.add(buildOCSPResponse(ocsp));
/*     */       }
/*     */     }
/* 268 */     if (crls != null) {
/* 269 */       for (byte[] crl : crls) {
/* 270 */         vd.crls.add(crl);
/*     */       }
/*     */     }
/* 273 */     if (certs != null) {
/* 274 */       for (byte[] cert : certs) {
/* 275 */         vd.certs.add(cert);
/*     */       }
/*     */     }
/* 278 */     this.validated.put(getSignatureHashKey(signatureName), vd);
/* 279 */     return true;
/*     */   }
/*     */   
/*     */   private static byte[] buildOCSPResponse(byte[] BasicOCSPResponse) throws IOException {
/* 283 */     DEROctetString doctet = new DEROctetString(BasicOCSPResponse);
/* 284 */     ASN1EncodableVector v2 = new ASN1EncodableVector();
/* 285 */     v2.add((ASN1Encodable)OCSPObjectIdentifiers.id_pkix_ocsp_basic);
/* 286 */     v2.add((ASN1Encodable)doctet);
/* 287 */     ASN1Enumerated den = new ASN1Enumerated(0);
/* 288 */     ASN1EncodableVector v3 = new ASN1EncodableVector();
/* 289 */     v3.add((ASN1Encodable)den);
/* 290 */     v3.add((ASN1Encodable)new DERTaggedObject(true, 0, (ASN1Encodable)new DERSequence(v2)));
/* 291 */     DERSequence seq = new DERSequence(v3);
/* 292 */     return seq.getEncoded();
/*     */   }
/*     */   
/*     */   private PdfName getSignatureHashKey(String signatureName) throws NoSuchAlgorithmException, IOException {
/* 296 */     PdfDictionary dic = this.acroFields.getSignatureDictionary(signatureName);
/* 297 */     PdfString contents = dic.getAsString(PdfName.CONTENTS);
/* 298 */     byte[] bc = null;
/* 299 */     if (!this.reader.isEncrypted()) {
/* 300 */       bc = contents.getOriginalBytes();
/*     */     } else {
/* 302 */       bc = contents.getBytes();
/*     */     } 
/* 304 */     byte[] bt = null;
/* 305 */     if (PdfName.ETSI_RFC3161.equals(PdfReader.getPdfObject(dic.get(PdfName.SUBFILTER)))) {
/* 306 */       ASN1InputStream din = new ASN1InputStream(new ByteArrayInputStream(bc));
/* 307 */       ASN1Primitive pkcs = din.readObject();
/* 308 */       bc = pkcs.getEncoded();
/*     */     } 
/* 310 */     bt = hashBytesSha1(bc);
/* 311 */     return new PdfName(Utilities.convertToHex(bt));
/*     */   }
/*     */   
/*     */   private static byte[] hashBytesSha1(byte[] b) throws NoSuchAlgorithmException {
/* 315 */     MessageDigest sh = MessageDigest.getInstance("SHA1");
/* 316 */     return sh.digest(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void merge() throws IOException {
/* 325 */     if (this.used || this.validated.isEmpty())
/*     */       return; 
/* 327 */     this.used = true;
/* 328 */     PdfDictionary catalog = this.reader.getCatalog();
/* 329 */     PdfObject dss = catalog.get(PdfName.DSS);
/* 330 */     if (dss == null) {
/* 331 */       createDss();
/*     */     } else {
/* 333 */       updateDss();
/*     */     } 
/*     */   }
/*     */   private void updateDss() throws IOException {
/* 337 */     PdfDictionary catalog = this.reader.getCatalog();
/* 338 */     this.stp.markUsed((PdfObject)catalog);
/* 339 */     PdfDictionary dss = catalog.getAsDict(PdfName.DSS);
/* 340 */     PdfArray ocsps = dss.getAsArray(PdfName.OCSPS);
/* 341 */     PdfArray crls = dss.getAsArray(PdfName.CRLS);
/* 342 */     PdfArray certs = dss.getAsArray(PdfName.CERTS);
/* 343 */     dss.remove(PdfName.OCSPS);
/* 344 */     dss.remove(PdfName.CRLS);
/* 345 */     dss.remove(PdfName.CERTS);
/* 346 */     PdfDictionary vrim = dss.getAsDict(PdfName.VRI);
/*     */     
/* 348 */     if (vrim != null) {
/* 349 */       for (PdfName n : vrim.getKeys()) {
/* 350 */         if (this.validated.containsKey(n)) {
/* 351 */           PdfDictionary vri = vrim.getAsDict(n);
/* 352 */           if (vri != null) {
/* 353 */             deleteOldReferences(ocsps, vri.getAsArray(PdfName.OCSP));
/* 354 */             deleteOldReferences(crls, vri.getAsArray(PdfName.CRL));
/* 355 */             deleteOldReferences(certs, vri.getAsArray(PdfName.CERT));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 360 */     if (ocsps == null)
/* 361 */       ocsps = new PdfArray(); 
/* 362 */     if (crls == null)
/* 363 */       crls = new PdfArray(); 
/* 364 */     if (certs == null)
/* 365 */       certs = new PdfArray(); 
/* 366 */     if (vrim == null) {
/* 367 */       vrim = new PdfDictionary();
/*     */     }
/* 369 */     outputDss(dss, vrim, ocsps, crls, certs);
/*     */   }
/*     */   
/*     */   private static void deleteOldReferences(PdfArray all, PdfArray toDelete) {
/* 373 */     if (all == null || toDelete == null)
/*     */       return; 
/* 375 */     for (PdfObject pi : toDelete) {
/* 376 */       if (!pi.isIndirect())
/*     */         continue; 
/* 378 */       PRIndirectReference pir = (PRIndirectReference)pi;
/* 379 */       for (int k = 0; k < all.size(); k++) {
/* 380 */         PdfObject po = all.getPdfObject(k);
/* 381 */         if (po.isIndirect()) {
/*     */           
/* 383 */           PRIndirectReference pod = (PRIndirectReference)po;
/* 384 */           if (pir.getNumber() == pod.getNumber()) {
/* 385 */             all.remove(k);
/* 386 */             k--;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void createDss() throws IOException {
/* 393 */     outputDss(new PdfDictionary(), new PdfDictionary(), new PdfArray(), new PdfArray(), new PdfArray());
/*     */   }
/*     */   
/*     */   private void outputDss(PdfDictionary dss, PdfDictionary vrim, PdfArray ocsps, PdfArray crls, PdfArray certs) throws IOException {
/* 397 */     this.writer.addDeveloperExtension(PdfDeveloperExtension.ESIC_1_7_EXTENSIONLEVEL5);
/* 398 */     PdfDictionary catalog = this.reader.getCatalog();
/* 399 */     this.stp.markUsed((PdfObject)catalog);
/* 400 */     for (PdfName vkey : this.validated.keySet()) {
/* 401 */       PdfArray ocsp = new PdfArray();
/* 402 */       PdfArray crl = new PdfArray();
/* 403 */       PdfArray cert = new PdfArray();
/* 404 */       PdfDictionary vri = new PdfDictionary();
/* 405 */       for (byte[] b : ((ValidationData)this.validated.get(vkey)).crls) {
/* 406 */         PdfStream ps = new PdfStream(b);
/* 407 */         ps.flateCompress();
/* 408 */         PdfIndirectReference iref = this.writer.addToBody((PdfObject)ps, false).getIndirectReference();
/* 409 */         crl.add((PdfObject)iref);
/* 410 */         crls.add((PdfObject)iref);
/*     */       } 
/* 412 */       for (byte[] b : ((ValidationData)this.validated.get(vkey)).ocsps) {
/* 413 */         PdfStream ps = new PdfStream(b);
/* 414 */         ps.flateCompress();
/* 415 */         PdfIndirectReference iref = this.writer.addToBody((PdfObject)ps, false).getIndirectReference();
/* 416 */         ocsp.add((PdfObject)iref);
/* 417 */         ocsps.add((PdfObject)iref);
/*     */       } 
/* 419 */       for (byte[] b : ((ValidationData)this.validated.get(vkey)).certs) {
/* 420 */         PdfStream ps = new PdfStream(b);
/* 421 */         ps.flateCompress();
/* 422 */         PdfIndirectReference iref = this.writer.addToBody((PdfObject)ps, false).getIndirectReference();
/* 423 */         cert.add((PdfObject)iref);
/* 424 */         certs.add((PdfObject)iref);
/*     */       } 
/* 426 */       if (ocsp.size() > 0)
/* 427 */         vri.put(PdfName.OCSP, (PdfObject)this.writer.addToBody((PdfObject)ocsp, false).getIndirectReference()); 
/* 428 */       if (crl.size() > 0)
/* 429 */         vri.put(PdfName.CRL, (PdfObject)this.writer.addToBody((PdfObject)crl, false).getIndirectReference()); 
/* 430 */       if (cert.size() > 0)
/* 431 */         vri.put(PdfName.CERT, (PdfObject)this.writer.addToBody((PdfObject)cert, false).getIndirectReference()); 
/* 432 */       vrim.put(vkey, (PdfObject)this.writer.addToBody((PdfObject)vri, false).getIndirectReference());
/*     */     } 
/* 434 */     dss.put(PdfName.VRI, (PdfObject)this.writer.addToBody((PdfObject)vrim, false).getIndirectReference());
/* 435 */     if (ocsps.size() > 0)
/* 436 */       dss.put(PdfName.OCSPS, (PdfObject)this.writer.addToBody((PdfObject)ocsps, false).getIndirectReference()); 
/* 437 */     if (crls.size() > 0)
/* 438 */       dss.put(PdfName.CRLS, (PdfObject)this.writer.addToBody((PdfObject)crls, false).getIndirectReference()); 
/* 439 */     if (certs.size() > 0)
/* 440 */       dss.put(PdfName.CERTS, (PdfObject)this.writer.addToBody((PdfObject)certs, false).getIndirectReference()); 
/* 441 */     catalog.put(PdfName.DSS, (PdfObject)this.writer.addToBody((PdfObject)dss, false).getIndirectReference());
/*     */   }
/*     */   
/*     */   private static class ValidationData {
/* 445 */     public List<byte[]> crls = (List)new ArrayList<byte>(); private ValidationData() {}
/* 446 */     public List<byte[]> ocsps = (List)new ArrayList<byte>();
/* 447 */     public List<byte[]> certs = (List)new ArrayList<byte>();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/LtvVerification.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */