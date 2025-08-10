/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import com.itextpdf.text.io.RASInputStream;
/*     */ import com.itextpdf.text.io.RandomAccessSource;
/*     */ import com.itextpdf.text.io.RandomAccessSourceFactory;
/*     */ import com.itextpdf.text.io.StreamUtil;
/*     */ import com.itextpdf.text.log.Logger;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import com.itextpdf.text.pdf.AcroFields;
/*     */ import com.itextpdf.text.pdf.ByteBuffer;
/*     */ import com.itextpdf.text.pdf.PdfArray;
/*     */ import com.itextpdf.text.pdf.PdfDate;
/*     */ import com.itextpdf.text.pdf.PdfDeveloperExtension;
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfName;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfReader;
/*     */ import com.itextpdf.text.pdf.PdfSignature;
/*     */ import com.itextpdf.text.pdf.PdfSignatureAppearance;
/*     */ import com.itextpdf.text.pdf.PdfString;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import org.bouncycastle.asn1.esf.SignaturePolicyIdentifier;
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
/*     */ public class MakeSignature
/*     */ {
/*  82 */   private static final Logger LOGGER = LoggerFactory.getLogger(MakeSignature.class);
/*     */   
/*     */   public enum CryptoStandard {
/*  85 */     CMS, CADES;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void signDetached(PdfSignatureAppearance sap, ExternalDigest externalDigest, ExternalSignature externalSignature, Certificate[] chain, Collection<CrlClient> crlList, OcspClient ocspClient, TSAClient tsaClient, int estimatedSize, CryptoStandard sigtype) throws IOException, DocumentException, GeneralSecurityException {
/* 107 */     signDetached(sap, externalDigest, externalSignature, chain, crlList, ocspClient, tsaClient, estimatedSize, sigtype, (SignaturePolicyIdentifier)null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void signDetached(PdfSignatureAppearance sap, ExternalDigest externalDigest, ExternalSignature externalSignature, Certificate[] chain, Collection<CrlClient> crlList, OcspClient ocspClient, TSAClient tsaClient, int estimatedSize, CryptoStandard sigtype, SignaturePolicyInfo signaturePolicy) throws IOException, DocumentException, GeneralSecurityException {
/* 130 */     signDetached(sap, externalDigest, externalSignature, chain, crlList, ocspClient, tsaClient, estimatedSize, sigtype, signaturePolicy.toSignaturePolicyIdentifier());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void signDetached(PdfSignatureAppearance sap, ExternalDigest externalDigest, ExternalSignature externalSignature, Certificate[] chain, Collection<CrlClient> crlList, OcspClient ocspClient, TSAClient tsaClient, int estimatedSize, CryptoStandard sigtype, SignaturePolicyIdentifier signaturePolicy) throws IOException, DocumentException, GeneralSecurityException {
/* 153 */     Collection<byte[]> crlBytes = null;
/* 154 */     int i = 0;
/* 155 */     while (crlBytes == null && i < chain.length)
/* 156 */       crlBytes = processCrl(chain[i++], crlList); 
/* 157 */     if (estimatedSize == 0) {
/* 158 */       estimatedSize = 8192;
/* 159 */       if (crlBytes != null) {
/* 160 */         for (byte[] element : crlBytes) {
/* 161 */           estimatedSize += element.length + 10;
/*     */         }
/*     */       }
/* 164 */       if (ocspClient != null)
/* 165 */         estimatedSize += 4192; 
/* 166 */       if (tsaClient != null)
/* 167 */         estimatedSize += 4192; 
/*     */     } 
/* 169 */     sap.setCertificate(chain[0]);
/* 170 */     if (sigtype == CryptoStandard.CADES) {
/* 171 */       sap.addDeveloperExtension(PdfDeveloperExtension.ESIC_1_7_EXTENSIONLEVEL2);
/*     */     }
/* 173 */     PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, (sigtype == CryptoStandard.CADES) ? PdfName.ETSI_CADES_DETACHED : PdfName.ADBE_PKCS7_DETACHED);
/* 174 */     dic.setReason(sap.getReason());
/* 175 */     dic.setLocation(sap.getLocation());
/* 176 */     dic.setSignatureCreator(sap.getSignatureCreator());
/* 177 */     dic.setContact(sap.getContact());
/* 178 */     dic.setDate(new PdfDate(sap.getSignDate()));
/* 179 */     sap.setCryptoDictionary((PdfDictionary)dic);
/*     */     
/* 181 */     HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
/* 182 */     exc.put(PdfName.CONTENTS, new Integer(estimatedSize * 2 + 2));
/* 183 */     sap.preClose(exc);
/*     */     
/* 185 */     String hashAlgorithm = externalSignature.getHashAlgorithm();
/* 186 */     PdfPKCS7 sgn = new PdfPKCS7(null, chain, hashAlgorithm, null, externalDigest, false);
/* 187 */     if (signaturePolicy != null) {
/* 188 */       sgn.setSignaturePolicy(signaturePolicy);
/*     */     }
/* 190 */     InputStream data = sap.getRangeStream();
/* 191 */     byte[] hash = DigestAlgorithms.digest(data, externalDigest.getMessageDigest(hashAlgorithm));
/* 192 */     byte[] ocsp = null;
/* 193 */     if (chain.length >= 2 && ocspClient != null) {
/* 194 */       ocsp = ocspClient.getEncoded((X509Certificate)chain[0], (X509Certificate)chain[1], null);
/*     */     }
/* 196 */     byte[] sh = sgn.getAuthenticatedAttributeBytes(hash, ocsp, crlBytes, sigtype);
/* 197 */     byte[] extSignature = externalSignature.sign(sh);
/* 198 */     sgn.setExternalDigest(extSignature, null, externalSignature.getEncryptionAlgorithm());
/*     */     
/* 200 */     byte[] encodedSig = sgn.getEncodedPKCS7(hash, tsaClient, ocsp, crlBytes, sigtype);
/*     */     
/* 202 */     if (estimatedSize < encodedSig.length) {
/* 203 */       throw new IOException("Not enough space");
/*     */     }
/* 205 */     byte[] paddedSig = new byte[estimatedSize];
/* 206 */     System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);
/*     */     
/* 208 */     PdfDictionary dic2 = new PdfDictionary();
/* 209 */     dic2.put(PdfName.CONTENTS, (PdfObject)(new PdfString(paddedSig)).setHexWriting(true));
/* 210 */     sap.close(dic2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Collection<byte[]> processCrl(Certificate cert, Collection<CrlClient> crlList) {
/* 220 */     if (crlList == null)
/* 221 */       return null; 
/* 222 */     ArrayList<byte[]> crlBytes = (ArrayList)new ArrayList<byte>();
/* 223 */     for (CrlClient cc : crlList) {
/* 224 */       if (cc == null)
/*     */         continue; 
/* 226 */       LOGGER.info("Processing " + cc.getClass().getName());
/* 227 */       Collection<byte[]> b = cc.getEncoded((X509Certificate)cert, null);
/* 228 */       if (b == null)
/*     */         continue; 
/* 230 */       crlBytes.addAll((Collection)b);
/*     */     } 
/* 232 */     if (crlBytes.isEmpty()) {
/* 233 */       return null;
/*     */     }
/* 235 */     return (Collection<byte[]>)crlBytes;
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
/*     */   public static void signExternalContainer(PdfSignatureAppearance sap, ExternalSignatureContainer externalSignatureContainer, int estimatedSize) throws GeneralSecurityException, IOException, DocumentException {
/* 249 */     PdfSignature dic = new PdfSignature(null, null);
/* 250 */     dic.setReason(sap.getReason());
/* 251 */     dic.setLocation(sap.getLocation());
/* 252 */     dic.setSignatureCreator(sap.getSignatureCreator());
/* 253 */     dic.setContact(sap.getContact());
/* 254 */     dic.setDate(new PdfDate(sap.getSignDate()));
/* 255 */     externalSignatureContainer.modifySigningDictionary((PdfDictionary)dic);
/* 256 */     sap.setCryptoDictionary((PdfDictionary)dic);
/*     */     
/* 258 */     HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
/* 259 */     exc.put(PdfName.CONTENTS, new Integer(estimatedSize * 2 + 2));
/* 260 */     sap.preClose(exc);
/*     */     
/* 262 */     InputStream data = sap.getRangeStream();
/* 263 */     byte[] encodedSig = externalSignatureContainer.sign(data);
/*     */     
/* 265 */     if (estimatedSize < encodedSig.length) {
/* 266 */       throw new IOException("Not enough space");
/*     */     }
/* 268 */     byte[] paddedSig = new byte[estimatedSize];
/* 269 */     System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);
/*     */     
/* 271 */     PdfDictionary dic2 = new PdfDictionary();
/* 272 */     dic2.put(PdfName.CONTENTS, (PdfObject)(new PdfString(paddedSig)).setHexWriting(true));
/* 273 */     sap.close(dic2);
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
/*     */   public static void signDeferred(PdfReader reader, String fieldName, OutputStream outs, ExternalSignatureContainer externalSignatureContainer) throws DocumentException, IOException, GeneralSecurityException {
/* 288 */     AcroFields af = reader.getAcroFields();
/* 289 */     PdfDictionary v = af.getSignatureDictionary(fieldName);
/* 290 */     if (v == null)
/* 291 */       throw new DocumentException("No field"); 
/* 292 */     if (!af.signatureCoversWholeDocument(fieldName))
/* 293 */       throw new DocumentException("Not the last signature"); 
/* 294 */     PdfArray b = v.getAsArray(PdfName.BYTERANGE);
/* 295 */     long[] gaps = b.asLongArray();
/* 296 */     if (b.size() != 4 || gaps[0] != 0L)
/* 297 */       throw new DocumentException("Single exclusion space supported"); 
/* 298 */     RandomAccessSource readerSource = reader.getSafeFile().createSourceView();
/* 299 */     RASInputStream rASInputStream = new RASInputStream((new RandomAccessSourceFactory()).createRanged(readerSource, gaps));
/* 300 */     byte[] signedContent = externalSignatureContainer.sign((InputStream)rASInputStream);
/* 301 */     int spaceAvailable = (int)(gaps[2] - gaps[1]) - 2;
/* 302 */     if ((spaceAvailable & 0x1) != 0)
/* 303 */       throw new DocumentException("Gap is not a multiple of 2"); 
/* 304 */     spaceAvailable /= 2;
/* 305 */     if (spaceAvailable < signedContent.length)
/* 306 */       throw new DocumentException("Not enough space"); 
/* 307 */     StreamUtil.CopyBytes(readerSource, 0L, gaps[1] + 1L, outs);
/* 308 */     ByteBuffer bb = new ByteBuffer(spaceAvailable * 2);
/* 309 */     for (byte bi : signedContent) {
/* 310 */       bb.appendHex(bi);
/*     */     }
/* 312 */     int remain = (spaceAvailable - signedContent.length) * 2;
/* 313 */     for (int k = 0; k < remain; k++) {
/* 314 */       bb.append((byte)48);
/*     */     }
/* 316 */     bb.writeTo(outs);
/* 317 */     StreamUtil.CopyBytes(readerSource, gaps[2] - 1L, gaps[3] + 1L, outs);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/MakeSignature.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */