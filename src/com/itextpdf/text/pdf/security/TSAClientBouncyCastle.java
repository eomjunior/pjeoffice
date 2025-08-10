/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.log.Logger;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import com.itextpdf.text.pdf.codec.Base64;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.math.BigInteger;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.MessageDigest;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.cmp.PKIFailureInfo;
/*     */ import org.bouncycastle.tsp.TSPException;
/*     */ import org.bouncycastle.tsp.TimeStampRequest;
/*     */ import org.bouncycastle.tsp.TimeStampRequestGenerator;
/*     */ import org.bouncycastle.tsp.TimeStampResponse;
/*     */ import org.bouncycastle.tsp.TimeStampToken;
/*     */ import org.bouncycastle.tsp.TimeStampTokenInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TSAClientBouncyCastle
/*     */   implements TSAClient
/*     */ {
/*  82 */   private static final Logger LOGGER = LoggerFactory.getLogger(TSAClientBouncyCastle.class);
/*     */ 
/*     */   
/*     */   protected String tsaURL;
/*     */ 
/*     */   
/*     */   protected String tsaUsername;
/*     */ 
/*     */   
/*     */   protected String tsaPassword;
/*     */ 
/*     */   
/*     */   protected TSAInfoBouncyCastle tsaInfo;
/*     */ 
/*     */   
/*     */   public static final int DEFAULTTOKENSIZE = 4096;
/*     */ 
/*     */   
/*     */   protected int tokenSizeEstimate;
/*     */ 
/*     */   
/*     */   public static final String DEFAULTHASHALGORITHM = "SHA-256";
/*     */ 
/*     */   
/*     */   protected String digestAlgorithm;
/*     */ 
/*     */   
/* 109 */   private String tsaReqPolicy = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSAClientBouncyCastle(String url) {
/* 116 */     this(url, null, null, 4096, "SHA-256");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSAClientBouncyCastle(String url, String username, String password) {
/* 126 */     this(url, username, password, 4096, "SHA-256");
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
/*     */   public TSAClientBouncyCastle(String url, String username, String password, int tokSzEstimate, String digestAlgorithm) {
/* 140 */     this.tsaURL = url;
/* 141 */     this.tsaUsername = username;
/* 142 */     this.tsaPassword = password;
/* 143 */     this.tokenSizeEstimate = tokSzEstimate;
/* 144 */     this.digestAlgorithm = digestAlgorithm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTSAInfo(TSAInfoBouncyCastle tsaInfo) {
/* 151 */     this.tsaInfo = tsaInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTokenSizeEstimate() {
/* 160 */     return this.tokenSizeEstimate;
/*     */   }
/*     */   
/*     */   public String getTSAReqPolicy() {
/* 164 */     return this.tsaReqPolicy;
/*     */   }
/*     */   
/*     */   public void setTSAReqPolicy(String tsaReqPolicy) {
/* 168 */     this.tsaReqPolicy = tsaReqPolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageDigest getMessageDigest() throws GeneralSecurityException {
/* 176 */     return (new BouncyCastleDigest()).getMessageDigest(this.digestAlgorithm);
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
/*     */   public byte[] getTimeStampToken(byte[] imprint) throws IOException, TSPException {
/* 188 */     byte[] respBytes = null;
/*     */     
/* 190 */     TimeStampRequestGenerator tsqGenerator = new TimeStampRequestGenerator();
/* 191 */     tsqGenerator.setCertReq(true);
/* 192 */     if (this.tsaReqPolicy != null && this.tsaReqPolicy.length() > 0) {
/* 193 */       tsqGenerator.setReqPolicy(new ASN1ObjectIdentifier(this.tsaReqPolicy));
/*     */     }
/*     */     
/* 196 */     BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());
/* 197 */     TimeStampRequest request = tsqGenerator.generate(new ASN1ObjectIdentifier(DigestAlgorithms.getAllowedDigests(this.digestAlgorithm)), imprint, nonce);
/* 198 */     byte[] requestBytes = request.getEncoded();
/*     */ 
/*     */     
/* 201 */     respBytes = getTSAResponse(requestBytes);
/*     */ 
/*     */     
/* 204 */     TimeStampResponse response = new TimeStampResponse(respBytes);
/*     */ 
/*     */     
/* 207 */     response.validate(request);
/* 208 */     PKIFailureInfo failure = response.getFailInfo();
/* 209 */     int value = (failure == null) ? 0 : failure.intValue();
/* 210 */     if (value != 0)
/*     */     {
/* 212 */       throw new IOException(MessageLocalization.getComposedMessage("invalid.tsa.1.response.code.2", new Object[] { this.tsaURL, String.valueOf(value) }));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 218 */     TimeStampToken tsToken = response.getTimeStampToken();
/* 219 */     if (tsToken == null) {
/* 220 */       throw new IOException(MessageLocalization.getComposedMessage("tsa.1.failed.to.return.time.stamp.token.2", new Object[] { this.tsaURL, response.getStatusString() }));
/*     */     }
/* 222 */     TimeStampTokenInfo tsTokenInfo = tsToken.getTimeStampInfo();
/* 223 */     byte[] encoded = tsToken.getEncoded();
/*     */     
/* 225 */     LOGGER.info("Timestamp generated: " + tsTokenInfo.getGenTime());
/* 226 */     if (this.tsaInfo != null) {
/* 227 */       this.tsaInfo.inspectTimeStampTokenInfo(tsTokenInfo);
/*     */     }
/*     */     
/* 230 */     this.tokenSizeEstimate = encoded.length + 32;
/* 231 */     return encoded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] getTSAResponse(byte[] requestBytes) throws IOException {
/*     */     URLConnection tsaConnection;
/* 241 */     URL url = new URL(this.tsaURL);
/*     */     
/*     */     try {
/* 244 */       tsaConnection = url.openConnection();
/*     */     }
/* 246 */     catch (IOException ioe) {
/* 247 */       throw new IOException(MessageLocalization.getComposedMessage("failed.to.get.tsa.response.from.1", new Object[] { this.tsaURL }));
/*     */     } 
/* 249 */     tsaConnection.setDoInput(true);
/* 250 */     tsaConnection.setDoOutput(true);
/* 251 */     tsaConnection.setUseCaches(false);
/* 252 */     tsaConnection.setRequestProperty("Content-Type", "application/timestamp-query");
/*     */     
/* 254 */     tsaConnection.setRequestProperty("Content-Transfer-Encoding", "binary");
/*     */     
/* 256 */     if (this.tsaUsername != null && !this.tsaUsername.equals("")) {
/* 257 */       String userPassword = this.tsaUsername + ":" + this.tsaPassword;
/* 258 */       tsaConnection.setRequestProperty("Authorization", "Basic " + 
/* 259 */           Base64.encodeBytes(userPassword.getBytes(), 8));
/*     */     } 
/* 261 */     OutputStream out = tsaConnection.getOutputStream();
/* 262 */     out.write(requestBytes);
/* 263 */     out.close();
/*     */ 
/*     */     
/* 266 */     InputStream inp = tsaConnection.getInputStream();
/* 267 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 268 */     byte[] buffer = new byte[1024];
/* 269 */     int bytesRead = 0;
/* 270 */     while ((bytesRead = inp.read(buffer, 0, buffer.length)) >= 0) {
/* 271 */       baos.write(buffer, 0, bytesRead);
/*     */     }
/* 273 */     byte[] respBytes = baos.toByteArray();
/*     */     
/* 275 */     String encoding = tsaConnection.getContentEncoding();
/* 276 */     if (encoding != null && encoding.equalsIgnoreCase("base64")) {
/* 277 */       respBytes = Base64.decode(new String(respBytes));
/*     */     }
/* 279 */     return respBytes;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/TSAClientBouncyCastle.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */