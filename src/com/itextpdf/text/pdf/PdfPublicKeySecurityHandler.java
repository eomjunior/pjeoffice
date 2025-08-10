/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.security.AlgorithmParameterGenerator;
/*     */ import java.security.AlgorithmParameters;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.KeyGenerator;
/*     */ import javax.crypto.SecretKey;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.ASN1InputStream;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1OutputStream;
/*     */ import org.bouncycastle.asn1.ASN1Primitive;
/*     */ import org.bouncycastle.asn1.ASN1Set;
/*     */ import org.bouncycastle.asn1.DEROctetString;
/*     */ import org.bouncycastle.asn1.DERSet;
/*     */ import org.bouncycastle.asn1.cms.ContentInfo;
/*     */ import org.bouncycastle.asn1.cms.EncryptedContentInfo;
/*     */ import org.bouncycastle.asn1.cms.EnvelopedData;
/*     */ import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
/*     */ import org.bouncycastle.asn1.cms.KeyTransRecipientInfo;
/*     */ import org.bouncycastle.asn1.cms.RecipientIdentifier;
/*     */ import org.bouncycastle.asn1.cms.RecipientInfo;
/*     */ import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
/*     */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*     */ import org.bouncycastle.asn1.x509.TBSCertificateStructure;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfPublicKeySecurityHandler
/*     */ {
/*     */   static final int SEED_LENGTH = 20;
/* 130 */   private ArrayList<PdfPublicKeyRecipient> recipients = null;
/*     */   
/* 132 */   private byte[] seed = new byte[20];
/*     */ 
/*     */   
/*     */   public PdfPublicKeySecurityHandler() {
/*     */     try {
/* 137 */       KeyGenerator key = KeyGenerator.getInstance("AES");
/* 138 */       key.init(192, new SecureRandom());
/* 139 */       SecretKey sk = key.generateKey();
/* 140 */       System.arraycopy(sk.getEncoded(), 0, this.seed, 0, 20);
/* 141 */     } catch (NoSuchAlgorithmException e) {
/* 142 */       this.seed = SecureRandom.getSeed(20);
/*     */     } 
/*     */     
/* 145 */     this.recipients = new ArrayList<PdfPublicKeyRecipient>();
/*     */   }
/*     */   
/*     */   public void addRecipient(PdfPublicKeyRecipient recipient) {
/* 149 */     this.recipients.add(recipient);
/*     */   }
/*     */   
/*     */   protected byte[] getSeed() {
/* 153 */     return (byte[])this.seed.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRecipientsSize() {
/* 162 */     return this.recipients.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getEncodedRecipient(int index) throws IOException, GeneralSecurityException {
/* 167 */     PdfPublicKeyRecipient recipient = this.recipients.get(index);
/* 168 */     byte[] cms = recipient.getCms();
/*     */     
/* 170 */     if (cms != null) return cms;
/*     */     
/* 172 */     Certificate certificate = recipient.getCertificate();
/* 173 */     int permission = recipient.getPermission();
/* 174 */     int revision = 3;
/*     */     
/* 176 */     permission |= (revision == 3) ? -3904 : -64;
/* 177 */     permission &= 0xFFFFFFFC;
/* 178 */     permission++;
/*     */     
/* 180 */     byte[] pkcs7input = new byte[24];
/*     */     
/* 182 */     byte one = (byte)permission;
/* 183 */     byte two = (byte)(permission >> 8);
/* 184 */     byte three = (byte)(permission >> 16);
/* 185 */     byte four = (byte)(permission >> 24);
/*     */     
/* 187 */     System.arraycopy(this.seed, 0, pkcs7input, 0, 20);
/*     */     
/* 189 */     pkcs7input[20] = four;
/* 190 */     pkcs7input[21] = three;
/* 191 */     pkcs7input[22] = two;
/* 192 */     pkcs7input[23] = one;
/*     */     
/* 194 */     ASN1Primitive obj = createDERForRecipient(pkcs7input, (X509Certificate)certificate);
/*     */     
/* 196 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*     */     
/* 198 */     ASN1OutputStream k = ASN1OutputStream.create(baos, "DER");
/*     */     
/* 200 */     k.writeObject(obj);
/*     */     
/* 202 */     cms = baos.toByteArray();
/*     */     
/* 204 */     recipient.setCms(cms);
/*     */     
/* 206 */     return cms;
/*     */   }
/*     */ 
/*     */   
/*     */   public PdfArray getEncodedRecipients() throws IOException, GeneralSecurityException {
/* 211 */     PdfArray EncodedRecipients = new PdfArray();
/* 212 */     byte[] cms = null;
/* 213 */     for (int i = 0; i < this.recipients.size(); i++) {
/*     */       try {
/* 215 */         cms = getEncodedRecipient(i);
/* 216 */         EncodedRecipients.add(new PdfLiteral(StringUtils.escapeString(cms)));
/* 217 */       } catch (GeneralSecurityException e) {
/* 218 */         EncodedRecipients = null;
/* 219 */       } catch (IOException e) {
/* 220 */         EncodedRecipients = null;
/*     */       } 
/*     */     } 
/* 223 */     return EncodedRecipients;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ASN1Primitive createDERForRecipient(byte[] in, X509Certificate cert) throws IOException, GeneralSecurityException {
/* 231 */     String s = "1.2.840.113549.3.2";
/*     */     
/* 233 */     AlgorithmParameterGenerator algorithmparametergenerator = AlgorithmParameterGenerator.getInstance(s);
/* 234 */     AlgorithmParameters algorithmparameters = algorithmparametergenerator.generateParameters();
/* 235 */     ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(algorithmparameters.getEncoded("ASN.1"));
/* 236 */     ASN1InputStream asn1inputstream = new ASN1InputStream(bytearrayinputstream);
/* 237 */     ASN1Primitive derobject = asn1inputstream.readObject();
/* 238 */     KeyGenerator keygenerator = KeyGenerator.getInstance(s);
/* 239 */     keygenerator.init(128);
/* 240 */     SecretKey secretkey = keygenerator.generateKey();
/* 241 */     Cipher cipher = Cipher.getInstance(s);
/* 242 */     cipher.init(1, secretkey, algorithmparameters);
/* 243 */     byte[] abyte1 = cipher.doFinal(in);
/* 244 */     DEROctetString deroctetstring = new DEROctetString(abyte1);
/* 245 */     KeyTransRecipientInfo keytransrecipientinfo = computeRecipientInfo(cert, secretkey.getEncoded());
/* 246 */     DERSet derset = new DERSet((ASN1Encodable)new RecipientInfo(keytransrecipientinfo));
/* 247 */     AlgorithmIdentifier algorithmidentifier = new AlgorithmIdentifier(new ASN1ObjectIdentifier(s), (ASN1Encodable)derobject);
/* 248 */     EncryptedContentInfo encryptedcontentinfo = new EncryptedContentInfo(PKCSObjectIdentifiers.data, algorithmidentifier, (ASN1OctetString)deroctetstring);
/*     */     
/* 250 */     ASN1Set set = null;
/* 251 */     EnvelopedData env = new EnvelopedData(null, (ASN1Set)derset, encryptedcontentinfo, set);
/* 252 */     ContentInfo contentinfo = new ContentInfo(PKCSObjectIdentifiers.envelopedData, (ASN1Encodable)env);
/*     */     
/* 254 */     return contentinfo.toASN1Primitive();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private KeyTransRecipientInfo computeRecipientInfo(X509Certificate x509certificate, byte[] abyte0) throws GeneralSecurityException, IOException {
/* 261 */     ASN1InputStream asn1inputstream = new ASN1InputStream(new ByteArrayInputStream(x509certificate.getTBSCertificate()));
/*     */     
/* 263 */     TBSCertificateStructure tbscertificatestructure = TBSCertificateStructure.getInstance(asn1inputstream.readObject());
/* 264 */     AlgorithmIdentifier algorithmidentifier = tbscertificatestructure.getSubjectPublicKeyInfo().getAlgorithm();
/*     */ 
/*     */ 
/*     */     
/* 268 */     IssuerAndSerialNumber issuerandserialnumber = new IssuerAndSerialNumber(tbscertificatestructure.getIssuer(), tbscertificatestructure.getSerialNumber().getValue());
/* 269 */     Cipher cipher = Cipher.getInstance(algorithmidentifier.getAlgorithm().getId());
/*     */     try {
/* 271 */       cipher.init(1, x509certificate);
/* 272 */     } catch (InvalidKeyException e) {
/* 273 */       cipher.init(1, x509certificate.getPublicKey());
/*     */     } 
/* 275 */     DEROctetString deroctetstring = new DEROctetString(cipher.doFinal(abyte0));
/* 276 */     RecipientIdentifier recipId = new RecipientIdentifier(issuerandserialnumber);
/* 277 */     return new KeyTransRecipientInfo(recipId, algorithmidentifier, (ASN1OctetString)deroctetstring);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfPublicKeySecurityHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */