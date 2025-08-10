/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.io.StreamUtil;
/*     */ import com.itextpdf.text.log.Level;
/*     */ import com.itextpdf.text.log.Logger;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import com.itextpdf.text.pdf.PdfEncryption;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.math.BigInteger;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.DEROctetString;
/*     */ import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.x509.Extension;
/*     */ import org.bouncycastle.asn1.x509.Extensions;
/*     */ import org.bouncycastle.cert.X509CertificateHolder;
/*     */ import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
/*     */ import org.bouncycastle.cert.ocsp.BasicOCSPResp;
/*     */ import org.bouncycastle.cert.ocsp.CertificateID;
/*     */ import org.bouncycastle.cert.ocsp.CertificateStatus;
/*     */ import org.bouncycastle.cert.ocsp.OCSPException;
/*     */ import org.bouncycastle.cert.ocsp.OCSPReq;
/*     */ import org.bouncycastle.cert.ocsp.OCSPReqBuilder;
/*     */ import org.bouncycastle.cert.ocsp.OCSPResp;
/*     */ import org.bouncycastle.cert.ocsp.SingleResp;
/*     */ import org.bouncycastle.jce.provider.BouncyCastleProvider;
/*     */ import org.bouncycastle.operator.OperatorException;
/*     */ import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
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
/*     */ 
/*     */ public class OcspClientBouncyCastle
/*     */   implements OcspClient
/*     */ {
/*  93 */   private static final Logger LOGGER = LoggerFactory.getLogger(OcspClientBouncyCastle.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final OCSPVerifier verifier;
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public OcspClientBouncyCastle() {
/* 103 */     this.verifier = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OcspClientBouncyCastle(OCSPVerifier verifier) {
/* 111 */     this.verifier = verifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicOCSPResp getBasicOCSPResp(X509Certificate checkCert, X509Certificate rootCert, String url) {
/*     */     try {
/* 119 */       OCSPResp ocspResponse = getOcspResponse(checkCert, rootCert, url);
/* 120 */       if (ocspResponse == null) {
/* 121 */         return null;
/*     */       }
/* 123 */       if (ocspResponse.getStatus() != 0) {
/* 124 */         return null;
/*     */       }
/* 126 */       BasicOCSPResp basicResponse = (BasicOCSPResp)ocspResponse.getResponseObject();
/* 127 */       if (this.verifier != null) {
/* 128 */         this.verifier.isValidResponse(basicResponse, rootCert);
/*     */       }
/* 130 */       return basicResponse;
/* 131 */     } catch (Exception ex) {
/* 132 */       if (LOGGER.isLogging(Level.ERROR)) {
/* 133 */         LOGGER.error(ex.getMessage());
/*     */       }
/* 135 */       return null;
/*     */     } 
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
/*     */   public byte[] getEncoded(X509Certificate checkCert, X509Certificate rootCert, String url) {
/*     */     try {
/* 149 */       BasicOCSPResp basicResponse = getBasicOCSPResp(checkCert, rootCert, url);
/* 150 */       if (basicResponse != null) {
/* 151 */         SingleResp[] responses = basicResponse.getResponses();
/* 152 */         if (responses.length == 1) {
/* 153 */           SingleResp resp = responses[0];
/* 154 */           Object status = resp.getCertStatus();
/* 155 */           if (status == CertificateStatus.GOOD)
/* 156 */             return basicResponse.getEncoded(); 
/* 157 */           if (status instanceof org.bouncycastle.cert.ocsp.RevokedStatus) {
/* 158 */             throw new IOException(MessageLocalization.getComposedMessage("ocsp.status.is.revoked", new Object[0]));
/*     */           }
/* 160 */           throw new IOException(MessageLocalization.getComposedMessage("ocsp.status.is.unknown", new Object[0]));
/*     */         }
/*     */       
/*     */       } 
/* 164 */     } catch (Exception ex) {
/* 165 */       if (LOGGER.isLogging(Level.ERROR))
/* 166 */         LOGGER.error(ex.getMessage()); 
/*     */     } 
/* 168 */     return null;
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
/*     */   private static OCSPReq generateOCSPRequest(X509Certificate issuerCert, BigInteger serialNumber) throws OCSPException, IOException, OperatorException, CertificateEncodingException {
/* 184 */     Security.addProvider((Provider)new BouncyCastleProvider());
/*     */ 
/*     */ 
/*     */     
/* 188 */     CertificateID id = new CertificateID((new JcaDigestCalculatorProviderBuilder()).build().get(CertificateID.HASH_SHA1), (X509CertificateHolder)new JcaX509CertificateHolder(issuerCert), serialNumber);
/*     */ 
/*     */ 
/*     */     
/* 192 */     OCSPReqBuilder gen = new OCSPReqBuilder();
/* 193 */     gen.addRequest(id);
/*     */     
/* 195 */     Extension ext = new Extension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce, false, (ASN1OctetString)new DEROctetString((new DEROctetString(PdfEncryption.createDocumentId())).getEncoded()));
/* 196 */     gen.setRequestExtensions(new Extensions(new Extension[] { ext }));
/* 197 */     return gen.build();
/*     */   }
/*     */   
/*     */   private OCSPResp getOcspResponse(X509Certificate checkCert, X509Certificate rootCert, String url) throws GeneralSecurityException, OCSPException, IOException, OperatorException {
/* 201 */     if (checkCert == null || rootCert == null)
/* 202 */       return null; 
/* 203 */     if (url == null) {
/* 204 */       url = CertificateUtil.getOCSPURL(checkCert);
/*     */     }
/* 206 */     if (url == null)
/* 207 */       return null; 
/* 208 */     LOGGER.info("Getting OCSP from " + url);
/* 209 */     OCSPReq request = generateOCSPRequest(rootCert, checkCert.getSerialNumber());
/* 210 */     byte[] array = request.getEncoded();
/* 211 */     URL urlt = new URL(url);
/* 212 */     HttpURLConnection con = (HttpURLConnection)urlt.openConnection();
/* 213 */     con.setRequestProperty("Content-Type", "application/ocsp-request");
/* 214 */     con.setRequestProperty("Accept", "application/ocsp-response");
/* 215 */     con.setDoOutput(true);
/* 216 */     OutputStream out = con.getOutputStream();
/* 217 */     DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(out));
/* 218 */     dataOut.write(array);
/* 219 */     dataOut.flush();
/* 220 */     dataOut.close();
/* 221 */     if (con.getResponseCode() / 100 != 2) {
/* 222 */       throw new IOException(MessageLocalization.getComposedMessage("invalid.http.response.1", con.getResponseCode()));
/*     */     }
/*     */     
/* 225 */     InputStream in = (InputStream)con.getContent();
/* 226 */     return new OCSPResp(StreamUtil.inputStreamToArray(in));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/OcspClientBouncyCastle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */