/*      */ package com.itextpdf.text.pdf.security;
/*      */ 
/*      */ import com.itextpdf.text.ExceptionConverter;
/*      */ import com.itextpdf.text.error_messages.MessageLocalization;
/*      */ import com.itextpdf.text.pdf.PdfName;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.math.BigInteger;
/*      */ import java.security.GeneralSecurityException;
/*      */ import java.security.InvalidKeyException;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.security.NoSuchProviderException;
/*      */ import java.security.PrivateKey;
/*      */ import java.security.PublicKey;
/*      */ import java.security.Signature;
/*      */ import java.security.SignatureException;
/*      */ import java.security.cert.CRL;
/*      */ import java.security.cert.Certificate;
/*      */ import java.security.cert.CertificateFactory;
/*      */ import java.security.cert.X509CRL;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.HashSet;
/*      */ import java.util.Set;
/*      */ import org.bouncycastle.asn1.ASN1Encodable;
/*      */ import org.bouncycastle.asn1.ASN1EncodableVector;
/*      */ import org.bouncycastle.asn1.ASN1Enumerated;
/*      */ import org.bouncycastle.asn1.ASN1InputStream;
/*      */ import org.bouncycastle.asn1.ASN1Integer;
/*      */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*      */ import org.bouncycastle.asn1.ASN1OctetString;
/*      */ import org.bouncycastle.asn1.ASN1OutputStream;
/*      */ import org.bouncycastle.asn1.ASN1Primitive;
/*      */ import org.bouncycastle.asn1.ASN1Sequence;
/*      */ import org.bouncycastle.asn1.ASN1Set;
/*      */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*      */ import org.bouncycastle.asn1.DERNull;
/*      */ import org.bouncycastle.asn1.DEROctetString;
/*      */ import org.bouncycastle.asn1.DERSequence;
/*      */ import org.bouncycastle.asn1.DERSet;
/*      */ import org.bouncycastle.asn1.DERTaggedObject;
/*      */ import org.bouncycastle.asn1.cms.Attribute;
/*      */ import org.bouncycastle.asn1.cms.AttributeTable;
/*      */ import org.bouncycastle.asn1.cms.ContentInfo;
/*      */ import org.bouncycastle.asn1.esf.SignaturePolicyIdentifier;
/*      */ import org.bouncycastle.asn1.ess.ESSCertID;
/*      */ import org.bouncycastle.asn1.ess.ESSCertIDv2;
/*      */ import org.bouncycastle.asn1.ess.SigningCertificate;
/*      */ import org.bouncycastle.asn1.ess.SigningCertificateV2;
/*      */ import org.bouncycastle.asn1.ocsp.BasicOCSPResponse;
/*      */ import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
/*      */ import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
/*      */ import org.bouncycastle.asn1.tsp.MessageImprint;
/*      */ import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
/*      */ import org.bouncycastle.cert.X509CertificateHolder;
/*      */ import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
/*      */ import org.bouncycastle.cert.ocsp.BasicOCSPResp;
/*      */ import org.bouncycastle.cert.ocsp.CertificateID;
/*      */ import org.bouncycastle.cert.ocsp.SingleResp;
/*      */ import org.bouncycastle.jce.X509Principal;
/*      */ import org.bouncycastle.jce.provider.X509CertParser;
/*      */ import org.bouncycastle.operator.DigestCalculator;
/*      */ import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
/*      */ import org.bouncycastle.tsp.TimeStampToken;
/*      */ import org.bouncycastle.tsp.TimeStampTokenInfo;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PdfPKCS7
/*      */ {
/*      */   private String provider;
/*      */   private SignaturePolicyIdentifier signaturePolicyIdentifier;
/*      */   private String signName;
/*      */   private String reason;
/*      */   private String location;
/*      */   private Calendar signDate;
/*      */   private int version;
/*      */   private int signerversion;
/*      */   private String digestAlgorithmOid;
/*      */   private MessageDigest messageDigest;
/*      */   private Set<String> digestalgos;
/*      */   private byte[] digestAttr;
/*      */   private PdfName filterSubtype;
/*      */   private String digestEncryptionAlgorithmOid;
/*      */   private ExternalDigest interfaceDigest;
/*      */   private byte[] externalDigest;
/*      */   private byte[] externalRSAdata;
/*      */   private Signature sig;
/*      */   private byte[] digest;
/*      */   private byte[] RSAdata;
/*      */   private byte[] sigAttr;
/*      */   private byte[] sigAttrDer;
/*      */   private MessageDigest encContDigest;
/*      */   private boolean verified;
/*      */   private boolean verifyResult;
/*      */   private Collection<Certificate> certs;
/*      */   private Collection<Certificate> signCerts;
/*      */   private X509Certificate signCert;
/*      */   private Collection<CRL> crls;
/*      */   private BasicOCSPResp basicResp;
/*      */   private boolean isTsp;
/*      */   private boolean isCades;
/*      */   private TimeStampToken timeStampToken;
/*      */   
/*      */   public PdfPKCS7(PrivateKey privKey, Certificate[] certChain, String hashAlgorithm, String provider, ExternalDigest interfaceDigest, boolean hasRSAdata) throws InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException {
/*  530 */     this.version = 1;
/*      */ 
/*      */     
/*  533 */     this.signerversion = 1; this.provider = provider; this.interfaceDigest = interfaceDigest; this.digestAlgorithmOid = DigestAlgorithms.getAllowedDigests(hashAlgorithm); if (this.digestAlgorithmOid == null) throw new NoSuchAlgorithmException(MessageLocalization.getComposedMessage("unknown.hash.algorithm.1", new Object[] { hashAlgorithm }));  this.signCert = (X509Certificate)certChain[0]; this.certs = new ArrayList<Certificate>(); for (Certificate element : certChain) this.certs.add(element);  this.digestalgos = new HashSet<String>(); this.digestalgos.add(this.digestAlgorithmOid); if (privKey != null) { this.digestEncryptionAlgorithmOid = privKey.getAlgorithm(); if (this.digestEncryptionAlgorithmOid.equals("RSA")) { this.digestEncryptionAlgorithmOid = "1.2.840.113549.1.1.1"; } else if (this.digestEncryptionAlgorithmOid.equals("DSA")) { this.digestEncryptionAlgorithmOid = "1.2.840.10040.4.1"; } else { throw new NoSuchAlgorithmException(MessageLocalization.getComposedMessage("unknown.key.algorithm.1", new Object[] { this.digestEncryptionAlgorithmOid })); }  }  if (hasRSAdata) { this.RSAdata = new byte[0]; this.messageDigest = DigestAlgorithms.getMessageDigest(getHashAlgorithm(), provider); }  if (privKey != null) this.sig = initSignature(privKey);  } public PdfPKCS7(byte[] contentsKey, byte[] certsKey, String provider) { this.version = 1; this.signerversion = 1; try { this.provider = provider; X509CertParser cr = new X509CertParser(); cr.engineInit(new ByteArrayInputStream(certsKey)); this.certs = cr.engineReadAll(); this.signCerts = this.certs; this.signCert = this.certs.iterator().next(); this.crls = new ArrayList<CRL>(); ASN1InputStream in = new ASN1InputStream(new ByteArrayInputStream(contentsKey)); this.digest = ((ASN1OctetString)in.readObject()).getOctets(); if (provider == null) { this.sig = Signature.getInstance("SHA1withRSA"); } else { this.sig = Signature.getInstance("SHA1withRSA", provider); }  this.sig.initVerify(this.signCert.getPublicKey()); this.digestAlgorithmOid = "1.2.840.10040.4.3"; this.digestEncryptionAlgorithmOid = "1.3.36.3.3.1.2"; } catch (Exception e) { throw new ExceptionConverter(e); }  } public PdfPKCS7(byte[] contentsKey, PdfName filterSubtype, String provider) { this.version = 1; this.signerversion = 1; this.filterSubtype = filterSubtype; this.isTsp = PdfName.ETSI_RFC3161.equals(filterSubtype); this.isCades = PdfName.ETSI_CADES_DETACHED.equals(filterSubtype); try { ASN1Primitive pkcs; this.provider = provider; ASN1InputStream din = new ASN1InputStream(new ByteArrayInputStream(contentsKey)); try { pkcs = din.readObject(); } catch (IOException iOException) { throw new IllegalArgumentException(MessageLocalization.getComposedMessage("can.t.decode.pkcs7signeddata.object", new Object[0])); }  if (!(pkcs instanceof ASN1Sequence)) throw new IllegalArgumentException(MessageLocalization.getComposedMessage("not.a.valid.pkcs.7.object.not.a.sequence", new Object[0]));  ASN1Sequence signedData = (ASN1Sequence)pkcs; ASN1ObjectIdentifier objId = (ASN1ObjectIdentifier)signedData.getObjectAt(0); if (!objId.getId().equals("1.2.840.113549.1.7.2")) throw new IllegalArgumentException(MessageLocalization.getComposedMessage("not.a.valid.pkcs.7.object.not.signed.data", new Object[0]));  ASN1Sequence content = (ASN1Sequence)((ASN1TaggedObject)signedData.getObjectAt(1)).getObject(); this.version = ((ASN1Integer)content.getObjectAt(0)).getValue().intValue(); this.digestalgos = new HashSet<String>(); Enumeration<ASN1Sequence> e = ((ASN1Set)content.getObjectAt(1)).getObjects(); while (e.hasMoreElements()) { ASN1Sequence s = e.nextElement(); ASN1ObjectIdentifier o = (ASN1ObjectIdentifier)s.getObjectAt(0); this.digestalgos.add(o.getId()); }  ASN1Sequence rsaData = (ASN1Sequence)content.getObjectAt(2); if (rsaData.size() > 1) { ASN1OctetString rsaDataContent = (ASN1OctetString)((ASN1TaggedObject)rsaData.getObjectAt(1)).getObject(); this.RSAdata = rsaDataContent.getOctets(); }  int next = 3; while (content.getObjectAt(next) instanceof ASN1TaggedObject) next++;  X509CertParser cr = new X509CertParser(); cr.engineInit(new ByteArrayInputStream(contentsKey)); this.certs = cr.engineReadAll(); ASN1Set signerInfos = (ASN1Set)content.getObjectAt(next); if (signerInfos.size() != 1) throw new IllegalArgumentException(MessageLocalization.getComposedMessage("this.pkcs.7.object.has.multiple.signerinfos.only.one.is.supported.at.this.time", new Object[0]));  ASN1Sequence signerInfo = (ASN1Sequence)signerInfos.getObjectAt(0); this.signerversion = ((ASN1Integer)signerInfo.getObjectAt(0)).getValue().intValue(); ASN1Sequence issuerAndSerialNumber = (ASN1Sequence)signerInfo.getObjectAt(1); X509Principal issuer = new X509Principal(issuerAndSerialNumber.getObjectAt(0).toASN1Primitive().getEncoded()); BigInteger serialNumber = ((ASN1Integer)issuerAndSerialNumber.getObjectAt(1)).getValue(); for (Certificate element : this.certs) { X509Certificate cert = (X509Certificate)element; if (cert.getIssuerDN().equals(issuer) && serialNumber.equals(cert.getSerialNumber())) { this.signCert = cert; break; }  }  if (this.signCert == null)
/*      */         throw new IllegalArgumentException(MessageLocalization.getComposedMessage("can.t.find.signing.certificate.with.serial.1", new Object[] { issuer.getName() + " / " + serialNumber.toString(16) }));  signCertificateChain(); this.digestAlgorithmOid = ((ASN1ObjectIdentifier)((ASN1Sequence)signerInfo.getObjectAt(2)).getObjectAt(0)).getId(); next = 3; boolean foundCades = false; if (signerInfo.getObjectAt(next) instanceof ASN1TaggedObject) { ASN1TaggedObject tagsig = (ASN1TaggedObject)signerInfo.getObjectAt(next); ASN1Set sseq = ASN1Set.getInstance(tagsig, false); this.sigAttr = sseq.getEncoded(); this.sigAttrDer = sseq.getEncoded("DER"); for (int k = 0; k < sseq.size(); k++) { ASN1Sequence seq2 = (ASN1Sequence)sseq.getObjectAt(k); String idSeq2 = ((ASN1ObjectIdentifier)seq2.getObjectAt(0)).getId(); if (idSeq2.equals("1.2.840.113549.1.9.4")) { ASN1Set set = (ASN1Set)seq2.getObjectAt(1); this.digestAttr = ((ASN1OctetString)set.getObjectAt(0)).getOctets(); } else if (idSeq2.equals("1.2.840.113583.1.1.8")) { ASN1Set setout = (ASN1Set)seq2.getObjectAt(1); ASN1Sequence seqout = (ASN1Sequence)setout.getObjectAt(0); for (int j = 0; j < seqout.size(); j++) { ASN1TaggedObject tg = (ASN1TaggedObject)seqout.getObjectAt(j); if (tg.getTagNo() == 0) { ASN1Sequence seqin = (ASN1Sequence)tg.getObject(); findCRL(seqin); }  if (tg.getTagNo() == 1) { ASN1Sequence seqin = (ASN1Sequence)tg.getObject(); findOcsp(seqin); }  }  } else if (this.isCades && idSeq2.equals("1.2.840.113549.1.9.16.2.12")) { ASN1Set setout = (ASN1Set)seq2.getObjectAt(1); ASN1Sequence seqout = (ASN1Sequence)setout.getObjectAt(0); SigningCertificate sv2 = SigningCertificate.getInstance(seqout); ESSCertID[] cerv2m = sv2.getCerts(); ESSCertID cerv2 = cerv2m[0]; byte[] enc2 = this.signCert.getEncoded(); MessageDigest m2 = (new BouncyCastleDigest()).getMessageDigest("SHA-1"); byte[] signCertHash = m2.digest(enc2); byte[] hs2 = cerv2.getCertHash(); if (!Arrays.equals(signCertHash, hs2))
/*      */               throw new IllegalArgumentException("Signing certificate doesn't match the ESS information.");  foundCades = true; } else if (this.isCades && idSeq2.equals("1.2.840.113549.1.9.16.2.47")) { ASN1Set setout = (ASN1Set)seq2.getObjectAt(1); ASN1Sequence seqout = (ASN1Sequence)setout.getObjectAt(0); SigningCertificateV2 sv2 = SigningCertificateV2.getInstance(seqout); ESSCertIDv2[] cerv2m = sv2.getCerts(); ESSCertIDv2 cerv2 = cerv2m[0]; AlgorithmIdentifier ai2 = cerv2.getHashAlgorithm(); byte[] enc2 = this.signCert.getEncoded(); MessageDigest m2 = (new BouncyCastleDigest()).getMessageDigest(DigestAlgorithms.getDigest(ai2.getAlgorithm().getId())); byte[] signCertHash = m2.digest(enc2); byte[] hs2 = cerv2.getCertHash(); if (!Arrays.equals(signCertHash, hs2))
/*      */               throw new IllegalArgumentException("Signing certificate doesn't match the ESS information.");  foundCades = true; }  }  if (this.digestAttr == null)
/*      */           throw new IllegalArgumentException(MessageLocalization.getComposedMessage("authenticated.attribute.is.missing.the.digest", new Object[0]));  next++; }  if (this.isCades && !foundCades)
/*      */         throw new IllegalArgumentException("CAdES ESS information missing.");  this.digestEncryptionAlgorithmOid = ((ASN1ObjectIdentifier)((ASN1Sequence)signerInfo.getObjectAt(next++)).getObjectAt(0)).getId(); this.digest = ((ASN1OctetString)signerInfo.getObjectAt(next++)).getOctets(); if (next < signerInfo.size() && signerInfo.getObjectAt(next) instanceof ASN1TaggedObject) { ASN1TaggedObject taggedObject = (ASN1TaggedObject)signerInfo.getObjectAt(next); ASN1Set unat = ASN1Set.getInstance(taggedObject, false); AttributeTable attble = new AttributeTable(unat); Attribute ts = attble.get(PKCSObjectIdentifiers.id_aa_signatureTimeStampToken); if (ts != null && ts.getAttrValues().size() > 0) { ASN1Set attributeValues = ts.getAttrValues(); ASN1Sequence tokenSequence = ASN1Sequence.getInstance(attributeValues.getObjectAt(0)); ContentInfo contentInfo = ContentInfo.getInstance(tokenSequence); this.timeStampToken = new TimeStampToken(contentInfo); }  }  if (this.isTsp) { ContentInfo contentInfoTsp = ContentInfo.getInstance(signedData); this.timeStampToken = new TimeStampToken(contentInfoTsp); TimeStampTokenInfo info = this.timeStampToken.getTimeStampInfo(); String algOID = info.getMessageImprintAlgOID().getId(); this.messageDigest = DigestAlgorithms.getMessageDigestFromOid(algOID, null); } else { if (this.RSAdata != null || this.digestAttr != null) { if (PdfName.ADBE_PKCS7_SHA1.equals(getFilterSubtype())) { this.messageDigest = DigestAlgorithms.getMessageDigest("SHA1", provider); } else { this.messageDigest = DigestAlgorithms.getMessageDigest(getHashAlgorithm(), provider); }  this.encContDigest = DigestAlgorithms.getMessageDigest(getHashAlgorithm(), provider); }  this.sig = initSignature(this.signCert.getPublicKey()); }  } catch (Exception e) { throw new ExceptionConverter(e); }  }
/*      */   public void setSignaturePolicy(SignaturePolicyInfo signaturePolicy) { this.signaturePolicyIdentifier = signaturePolicy.toSignaturePolicyIdentifier(); }
/*  540 */   public void setSignaturePolicy(SignaturePolicyIdentifier signaturePolicy) { this.signaturePolicyIdentifier = signaturePolicy; } public int getVersion() { return this.version; }
/*      */   public String getSignName() { return this.signName; }
/*      */   public void setSignName(String signName) { this.signName = signName; }
/*      */   public String getReason() { return this.reason; }
/*      */   public void setReason(String reason) { this.reason = reason; }
/*      */   public String getLocation() { return this.location; }
/*      */   public void setLocation(String location) { this.location = location; }
/*      */   public Calendar getSignDate() { Calendar dt = getTimeStampDate(); if (dt == null) return this.signDate;  return dt; }
/*  548 */   public void setSignDate(Calendar signDate) { this.signDate = signDate; } public int getSigningInfoVersion() { return this.signerversion; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDigestAlgorithmOid() {
/*  571 */     return this.digestAlgorithmOid;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getHashAlgorithm() {
/*  579 */     return DigestAlgorithms.getDigest(this.digestAlgorithmOid);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDigestEncryptionAlgorithmOid() {
/*  591 */     return this.digestEncryptionAlgorithmOid;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDigestAlgorithm() {
/*  599 */     return getHashAlgorithm() + "with" + getEncryptionAlgorithm();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExternalDigest(byte[] digest, byte[] RSAdata, String digestEncryptionAlgorithm) {
/*  624 */     this.externalDigest = digest;
/*  625 */     this.externalRSAdata = RSAdata;
/*  626 */     if (digestEncryptionAlgorithm != null) {
/*  627 */       if (digestEncryptionAlgorithm.equals("RSA")) {
/*  628 */         this.digestEncryptionAlgorithmOid = "1.2.840.113549.1.1.1";
/*      */       }
/*  630 */       else if (digestEncryptionAlgorithm.equals("DSA")) {
/*  631 */         this.digestEncryptionAlgorithmOid = "1.2.840.10040.4.1";
/*      */       }
/*  633 */       else if (digestEncryptionAlgorithm.equals("ECDSA")) {
/*  634 */         this.digestEncryptionAlgorithmOid = "1.2.840.10045.2.1";
/*      */       } else {
/*      */         
/*  637 */         throw new ExceptionConverter(new NoSuchAlgorithmException(MessageLocalization.getComposedMessage("unknown.key.algorithm.1", new Object[] { digestEncryptionAlgorithm })));
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Signature initSignature(PrivateKey key) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
/*      */     Signature signature;
/*  656 */     if (this.provider == null) {
/*  657 */       signature = Signature.getInstance(getDigestAlgorithm());
/*      */     } else {
/*  659 */       signature = Signature.getInstance(getDigestAlgorithm(), this.provider);
/*  660 */     }  signature.initSign(key);
/*  661 */     return signature;
/*      */   }
/*      */   private Signature initSignature(PublicKey key) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
/*      */     Signature signature;
/*  665 */     String digestAlgorithm = getDigestAlgorithm();
/*  666 */     if (PdfName.ADBE_X509_RSA_SHA1.equals(getFilterSubtype())) {
/*  667 */       digestAlgorithm = "SHA1withRSA";
/*      */     }
/*  669 */     if (this.provider == null) {
/*  670 */       signature = Signature.getInstance(digestAlgorithm);
/*      */     } else {
/*  672 */       signature = Signature.getInstance(digestAlgorithm, this.provider);
/*      */     } 
/*  674 */     signature.initVerify(key);
/*  675 */     return signature;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void update(byte[] buf, int off, int len) throws SignatureException {
/*  687 */     if (this.RSAdata != null || this.digestAttr != null || this.isTsp) {
/*  688 */       this.messageDigest.update(buf, off, len);
/*      */     } else {
/*  690 */       this.sig.update(buf, off, len);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getEncodedPKCS1() {
/*      */     try {
/*  701 */       if (this.externalDigest != null) {
/*  702 */         this.digest = this.externalDigest;
/*      */       } else {
/*  704 */         this.digest = this.sig.sign();
/*  705 */       }  ByteArrayOutputStream bOut = new ByteArrayOutputStream();
/*      */       
/*  707 */       ASN1OutputStream dout = ASN1OutputStream.create(bOut);
/*  708 */       dout.writeObject((ASN1Primitive)new DEROctetString(this.digest));
/*  709 */       dout.close();
/*      */       
/*  711 */       return bOut.toByteArray();
/*      */     }
/*  713 */     catch (Exception e) {
/*  714 */       throw new ExceptionConverter(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getEncodedPKCS7() {
/*  725 */     return getEncodedPKCS7(null, null, null, null, MakeSignature.CryptoStandard.CMS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getEncodedPKCS7(byte[] secondDigest) {
/*  735 */     return getEncodedPKCS7(secondDigest, null, null, null, MakeSignature.CryptoStandard.CMS);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getEncodedPKCS7(byte[] secondDigest, TSAClient tsaClient, byte[] ocsp, Collection<byte[]> crlBytes, MakeSignature.CryptoStandard sigtype) {
/*      */     try {
/*  749 */       if (this.externalDigest != null) {
/*  750 */         this.digest = this.externalDigest;
/*  751 */         if (this.RSAdata != null) {
/*  752 */           this.RSAdata = this.externalRSAdata;
/*      */         }
/*  754 */       } else if (this.externalRSAdata != null && this.RSAdata != null) {
/*  755 */         this.RSAdata = this.externalRSAdata;
/*  756 */         this.sig.update(this.RSAdata);
/*  757 */         this.digest = this.sig.sign();
/*      */       } else {
/*      */         
/*  760 */         if (this.RSAdata != null) {
/*  761 */           this.RSAdata = this.messageDigest.digest();
/*  762 */           this.sig.update(this.RSAdata);
/*      */         } 
/*  764 */         this.digest = this.sig.sign();
/*      */       } 
/*      */ 
/*      */       
/*  768 */       ASN1EncodableVector digestAlgorithms = new ASN1EncodableVector();
/*  769 */       for (String element : this.digestalgos) {
/*  770 */         ASN1EncodableVector algos = new ASN1EncodableVector();
/*  771 */         algos.add((ASN1Encodable)new ASN1ObjectIdentifier(element));
/*  772 */         algos.add((ASN1Encodable)DERNull.INSTANCE);
/*  773 */         digestAlgorithms.add((ASN1Encodable)new DERSequence(algos));
/*      */       } 
/*      */ 
/*      */       
/*  777 */       ASN1EncodableVector v = new ASN1EncodableVector();
/*  778 */       v.add((ASN1Encodable)new ASN1ObjectIdentifier("1.2.840.113549.1.7.1"));
/*  779 */       if (this.RSAdata != null)
/*  780 */         v.add((ASN1Encodable)new DERTaggedObject(0, (ASN1Encodable)new DEROctetString(this.RSAdata))); 
/*  781 */       DERSequence contentinfo = new DERSequence(v);
/*      */ 
/*      */ 
/*      */       
/*  785 */       v = new ASN1EncodableVector();
/*  786 */       for (Certificate element : this.certs) {
/*  787 */         ASN1InputStream tempstream = new ASN1InputStream(new ByteArrayInputStream(((X509Certificate)element).getEncoded()));
/*  788 */         v.add((ASN1Encodable)tempstream.readObject());
/*      */       } 
/*      */       
/*  791 */       DERSet dercertificates = new DERSet(v);
/*      */ 
/*      */ 
/*      */       
/*  795 */       ASN1EncodableVector signerinfo = new ASN1EncodableVector();
/*      */ 
/*      */ 
/*      */       
/*  799 */       signerinfo.add((ASN1Encodable)new ASN1Integer(this.signerversion));
/*      */       
/*  801 */       v = new ASN1EncodableVector();
/*  802 */       v.add((ASN1Encodable)CertificateInfo.getIssuer(this.signCert.getTBSCertificate()));
/*  803 */       v.add((ASN1Encodable)new ASN1Integer(this.signCert.getSerialNumber()));
/*  804 */       signerinfo.add((ASN1Encodable)new DERSequence(v));
/*      */ 
/*      */       
/*  807 */       v = new ASN1EncodableVector();
/*  808 */       v.add((ASN1Encodable)new ASN1ObjectIdentifier(this.digestAlgorithmOid));
/*  809 */       v.add((ASN1Encodable)DERNull.INSTANCE);
/*  810 */       signerinfo.add((ASN1Encodable)new DERSequence(v));
/*      */ 
/*      */       
/*  813 */       if (secondDigest != null) {
/*  814 */         signerinfo.add((ASN1Encodable)new DERTaggedObject(false, 0, (ASN1Encodable)getAuthenticatedAttributeSet(secondDigest, ocsp, crlBytes, sigtype)));
/*      */       }
/*      */       
/*  817 */       v = new ASN1EncodableVector();
/*  818 */       v.add((ASN1Encodable)new ASN1ObjectIdentifier(this.digestEncryptionAlgorithmOid));
/*  819 */       v.add((ASN1Encodable)DERNull.INSTANCE);
/*  820 */       signerinfo.add((ASN1Encodable)new DERSequence(v));
/*      */ 
/*      */       
/*  823 */       signerinfo.add((ASN1Encodable)new DEROctetString(this.digest));
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  828 */       if (tsaClient != null) {
/*  829 */         byte[] tsImprint = tsaClient.getMessageDigest().digest(this.digest);
/*  830 */         byte[] tsToken = tsaClient.getTimeStampToken(tsImprint);
/*  831 */         if (tsToken != null) {
/*  832 */           ASN1EncodableVector unauthAttributes = buildUnauthenticatedAttributes(tsToken);
/*  833 */           if (unauthAttributes != null) {
/*  834 */             signerinfo.add((ASN1Encodable)new DERTaggedObject(false, 1, (ASN1Encodable)new DERSet(unauthAttributes)));
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  840 */       ASN1EncodableVector body = new ASN1EncodableVector();
/*  841 */       body.add((ASN1Encodable)new ASN1Integer(this.version));
/*  842 */       body.add((ASN1Encodable)new DERSet(digestAlgorithms));
/*  843 */       body.add((ASN1Encodable)contentinfo);
/*  844 */       body.add((ASN1Encodable)new DERTaggedObject(false, 0, (ASN1Encodable)dercertificates));
/*      */ 
/*      */       
/*  847 */       body.add((ASN1Encodable)new DERSet((ASN1Encodable)new DERSequence(signerinfo)));
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  852 */       ASN1EncodableVector whole = new ASN1EncodableVector();
/*  853 */       whole.add((ASN1Encodable)new ASN1ObjectIdentifier("1.2.840.113549.1.7.2"));
/*  854 */       whole.add((ASN1Encodable)new DERTaggedObject(0, (ASN1Encodable)new DERSequence(body)));
/*      */       
/*  856 */       ByteArrayOutputStream bOut = new ByteArrayOutputStream();
/*      */       
/*  858 */       ASN1OutputStream dout = ASN1OutputStream.create(bOut);
/*  859 */       dout.writeObject((ASN1Primitive)new DERSequence(whole));
/*  860 */       dout.close();
/*      */       
/*  862 */       return bOut.toByteArray();
/*      */     }
/*  864 */     catch (Exception e) {
/*  865 */       throw new ExceptionConverter(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ASN1EncodableVector buildUnauthenticatedAttributes(byte[] timeStampToken) throws IOException {
/*  879 */     if (timeStampToken == null) {
/*  880 */       return null;
/*      */     }
/*      */     
/*  883 */     String ID_TIME_STAMP_TOKEN = "1.2.840.113549.1.9.16.2.14";
/*      */     
/*  885 */     ASN1InputStream tempstream = new ASN1InputStream(new ByteArrayInputStream(timeStampToken));
/*  886 */     ASN1EncodableVector unauthAttributes = new ASN1EncodableVector();
/*      */     
/*  888 */     ASN1EncodableVector v = new ASN1EncodableVector();
/*  889 */     v.add((ASN1Encodable)new ASN1ObjectIdentifier(ID_TIME_STAMP_TOKEN));
/*  890 */     ASN1Sequence seq = (ASN1Sequence)tempstream.readObject();
/*  891 */     v.add((ASN1Encodable)new DERSet((ASN1Encodable)seq));
/*      */     
/*  893 */     unauthAttributes.add((ASN1Encodable)new DERSequence(v));
/*  894 */     return unauthAttributes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getAuthenticatedAttributeBytes(byte[] secondDigest, byte[] ocsp, Collection<byte[]> crlBytes, MakeSignature.CryptoStandard sigtype) {
/*      */     try {
/*  927 */       return getAuthenticatedAttributeSet(secondDigest, ocsp, crlBytes, sigtype).getEncoded("DER");
/*      */     }
/*  929 */     catch (Exception e) {
/*  930 */       throw new ExceptionConverter(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private DERSet getAuthenticatedAttributeSet(byte[] secondDigest, byte[] ocsp, Collection<byte[]> crlBytes, MakeSignature.CryptoStandard sigtype) {
/*      */     try {
/*  943 */       ASN1EncodableVector attribute = new ASN1EncodableVector();
/*  944 */       ASN1EncodableVector v = new ASN1EncodableVector();
/*  945 */       v.add((ASN1Encodable)new ASN1ObjectIdentifier("1.2.840.113549.1.9.3"));
/*  946 */       v.add((ASN1Encodable)new DERSet((ASN1Encodable)new ASN1ObjectIdentifier("1.2.840.113549.1.7.1")));
/*  947 */       attribute.add((ASN1Encodable)new DERSequence(v));
/*  948 */       v = new ASN1EncodableVector();
/*  949 */       v.add((ASN1Encodable)new ASN1ObjectIdentifier("1.2.840.113549.1.9.4"));
/*  950 */       v.add((ASN1Encodable)new DERSet((ASN1Encodable)new DEROctetString(secondDigest)));
/*  951 */       attribute.add((ASN1Encodable)new DERSequence(v));
/*  952 */       boolean haveCrl = false;
/*  953 */       if (crlBytes != null) {
/*  954 */         for (byte[] bCrl : crlBytes) {
/*  955 */           if (bCrl != null) {
/*  956 */             haveCrl = true;
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       }
/*  961 */       if (ocsp != null || haveCrl) {
/*  962 */         v = new ASN1EncodableVector();
/*  963 */         v.add((ASN1Encodable)new ASN1ObjectIdentifier("1.2.840.113583.1.1.8"));
/*      */         
/*  965 */         ASN1EncodableVector revocationV = new ASN1EncodableVector();
/*      */         
/*  967 */         if (haveCrl) {
/*  968 */           ASN1EncodableVector v2 = new ASN1EncodableVector();
/*  969 */           for (byte[] bCrl : crlBytes) {
/*  970 */             if (bCrl == null)
/*      */               continue; 
/*  972 */             ASN1InputStream t = new ASN1InputStream(new ByteArrayInputStream(bCrl));
/*  973 */             v2.add((ASN1Encodable)t.readObject());
/*      */           } 
/*  975 */           revocationV.add((ASN1Encodable)new DERTaggedObject(true, 0, (ASN1Encodable)new DERSequence(v2)));
/*      */         } 
/*      */         
/*  978 */         if (ocsp != null) {
/*  979 */           DEROctetString doctet = new DEROctetString(ocsp);
/*  980 */           ASN1EncodableVector vo1 = new ASN1EncodableVector();
/*  981 */           ASN1EncodableVector v2 = new ASN1EncodableVector();
/*  982 */           v2.add((ASN1Encodable)OCSPObjectIdentifiers.id_pkix_ocsp_basic);
/*  983 */           v2.add((ASN1Encodable)doctet);
/*  984 */           ASN1Enumerated den = new ASN1Enumerated(0);
/*  985 */           ASN1EncodableVector v3 = new ASN1EncodableVector();
/*  986 */           v3.add((ASN1Encodable)den);
/*  987 */           v3.add((ASN1Encodable)new DERTaggedObject(true, 0, (ASN1Encodable)new DERSequence(v2)));
/*  988 */           vo1.add((ASN1Encodable)new DERSequence(v3));
/*  989 */           revocationV.add((ASN1Encodable)new DERTaggedObject(true, 1, (ASN1Encodable)new DERSequence(vo1)));
/*      */         } 
/*      */         
/*  992 */         v.add((ASN1Encodable)new DERSet((ASN1Encodable)new DERSequence(revocationV)));
/*  993 */         attribute.add((ASN1Encodable)new DERSequence(v));
/*      */       } 
/*  995 */       if (sigtype == MakeSignature.CryptoStandard.CADES) {
/*  996 */         v = new ASN1EncodableVector();
/*  997 */         v.add((ASN1Encodable)new ASN1ObjectIdentifier("1.2.840.113549.1.9.16.2.47"));
/*      */         
/*  999 */         ASN1EncodableVector aaV2 = new ASN1EncodableVector();
/* 1000 */         String sha256Oid = DigestAlgorithms.getAllowedDigests("SHA-256");
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1005 */         if (!sha256Oid.equals(this.digestAlgorithmOid)) {
/* 1006 */           AlgorithmIdentifier algoId = new AlgorithmIdentifier(new ASN1ObjectIdentifier(this.digestAlgorithmOid));
/* 1007 */           aaV2.add((ASN1Encodable)algoId);
/*      */         } 
/*      */         
/* 1010 */         MessageDigest md = this.interfaceDigest.getMessageDigest(getHashAlgorithm());
/* 1011 */         byte[] dig = md.digest(this.signCert.getEncoded());
/* 1012 */         aaV2.add((ASN1Encodable)new DEROctetString(dig));
/*      */         
/* 1014 */         v.add((ASN1Encodable)new DERSet((ASN1Encodable)new DERSequence((ASN1Encodable)new DERSequence((ASN1Encodable)new DERSequence(aaV2)))));
/* 1015 */         attribute.add((ASN1Encodable)new DERSequence(v));
/*      */       } 
/*      */       
/* 1018 */       if (this.signaturePolicyIdentifier != null) {
/* 1019 */         attribute.add((ASN1Encodable)new Attribute(PKCSObjectIdentifiers.id_aa_ets_sigPolicyId, (ASN1Set)new DERSet((ASN1Encodable)this.signaturePolicyIdentifier)));
/*      */       }
/*      */       
/* 1022 */       return new DERSet(attribute);
/*      */     }
/* 1024 */     catch (Exception e) {
/* 1025 */       throw new ExceptionConverter(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean verify() throws GeneralSecurityException {
/* 1057 */     if (this.verified)
/* 1058 */       return this.verifyResult; 
/* 1059 */     if (this.isTsp) {
/* 1060 */       TimeStampTokenInfo info = this.timeStampToken.getTimeStampInfo();
/* 1061 */       MessageImprint imprint = info.toASN1Structure().getMessageImprint();
/* 1062 */       byte[] md = this.messageDigest.digest();
/* 1063 */       byte[] imphashed = imprint.getHashedMessage();
/* 1064 */       this.verifyResult = Arrays.equals(md, imphashed);
/*      */     
/*      */     }
/* 1067 */     else if (this.sigAttr != null || this.sigAttrDer != null) {
/* 1068 */       byte[] msgDigestBytes = this.messageDigest.digest();
/* 1069 */       boolean verifyRSAdata = true;
/*      */       
/* 1071 */       boolean encContDigestCompare = false;
/* 1072 */       if (this.RSAdata != null) {
/* 1073 */         verifyRSAdata = Arrays.equals(msgDigestBytes, this.RSAdata);
/* 1074 */         this.encContDigest.update(this.RSAdata);
/* 1075 */         encContDigestCompare = Arrays.equals(this.encContDigest.digest(), this.digestAttr);
/*      */       } 
/* 1077 */       boolean absentEncContDigestCompare = Arrays.equals(msgDigestBytes, this.digestAttr);
/* 1078 */       boolean concludingDigestCompare = (absentEncContDigestCompare || encContDigestCompare);
/* 1079 */       boolean sigVerify = (verifySigAttributes(this.sigAttr) || verifySigAttributes(this.sigAttrDer));
/* 1080 */       this.verifyResult = (concludingDigestCompare && sigVerify && verifyRSAdata);
/*      */     } else {
/*      */       
/* 1083 */       if (this.RSAdata != null)
/* 1084 */         this.sig.update(this.messageDigest.digest()); 
/* 1085 */       this.verifyResult = this.sig.verify(this.digest);
/*      */     } 
/*      */     
/* 1088 */     this.verified = true;
/* 1089 */     return this.verifyResult;
/*      */   }
/*      */   
/*      */   private boolean verifySigAttributes(byte[] attr) throws GeneralSecurityException {
/* 1093 */     Signature signature = initSignature(this.signCert.getPublicKey());
/* 1094 */     signature.update(attr);
/* 1095 */     return signature.verify(this.digest);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean verifyTimestampImprint() throws GeneralSecurityException {
/* 1105 */     if (this.timeStampToken == null)
/* 1106 */       return false; 
/* 1107 */     TimeStampTokenInfo info = this.timeStampToken.getTimeStampInfo();
/* 1108 */     MessageImprint imprint = info.toASN1Structure().getMessageImprint();
/* 1109 */     String algOID = info.getMessageImprintAlgOID().getId();
/* 1110 */     byte[] md = (new BouncyCastleDigest()).getMessageDigest(DigestAlgorithms.getDigest(algOID)).digest(this.digest);
/* 1111 */     byte[] imphashed = imprint.getHashedMessage();
/* 1112 */     boolean res = Arrays.equals(md, imphashed);
/* 1113 */     return res;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Certificate[] getCertificates() {
/* 1133 */     return this.certs.<Certificate>toArray((Certificate[])new X509Certificate[this.certs.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Certificate[] getSignCertificateChain() {
/* 1144 */     return this.signCerts.<Certificate>toArray((Certificate[])new X509Certificate[this.signCerts.size()]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public X509Certificate getSigningCertificate() {
/* 1152 */     return this.signCert;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void signCertificateChain() {
/* 1161 */     ArrayList<Certificate> cc = new ArrayList<Certificate>();
/* 1162 */     cc.add(this.signCert);
/* 1163 */     ArrayList<Certificate> oc = new ArrayList<Certificate>(this.certs);
/* 1164 */     for (int k = 0; k < oc.size(); k++) {
/* 1165 */       if (this.signCert.equals(oc.get(k))) {
/* 1166 */         oc.remove(k);
/* 1167 */         k--;
/*      */       } 
/*      */     } 
/*      */     
/* 1171 */     boolean found = true;
/* 1172 */     while (found) {
/* 1173 */       X509Certificate v = (X509Certificate)cc.get(cc.size() - 1);
/* 1174 */       found = false;
/* 1175 */       for (int i = 0; i < oc.size(); i++) {
/* 1176 */         X509Certificate issuer = (X509Certificate)oc.get(i);
/*      */         try {
/* 1178 */           if (this.provider == null) {
/* 1179 */             v.verify(issuer.getPublicKey());
/*      */           } else {
/* 1181 */             v.verify(issuer.getPublicKey(), this.provider);
/* 1182 */           }  found = true;
/* 1183 */           cc.add(oc.get(i));
/* 1184 */           oc.remove(i);
/*      */           
/*      */           break;
/* 1187 */         } catch (Exception exception) {}
/*      */       } 
/*      */     } 
/*      */     
/* 1191 */     this.signCerts = cc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<CRL> getCRLs() {
/* 1203 */     return this.crls;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void findCRL(ASN1Sequence seq) {
/*      */     try {
/* 1211 */       this.crls = new ArrayList<CRL>();
/* 1212 */       for (int k = 0; k < seq.size(); k++) {
/* 1213 */         ByteArrayInputStream ar = new ByteArrayInputStream(seq.getObjectAt(k).toASN1Primitive().getEncoded("DER"));
/* 1214 */         CertificateFactory cf = CertificateFactory.getInstance("X.509");
/* 1215 */         X509CRL crl = (X509CRL)cf.generateCRL(ar);
/* 1216 */         this.crls.add(crl);
/*      */       }
/*      */     
/* 1219 */     } catch (Exception exception) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BasicOCSPResp getOcsp() {
/* 1235 */     return this.basicResp;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRevocationValid() {
/* 1244 */     if (this.basicResp == null)
/* 1245 */       return false; 
/* 1246 */     if (this.signCerts.size() < 2)
/* 1247 */       return false; 
/*      */     try {
/* 1249 */       X509Certificate[] cs = (X509Certificate[])getSignCertificateChain();
/* 1250 */       SingleResp sr = this.basicResp.getResponses()[0];
/* 1251 */       CertificateID cid = sr.getCertID();
/* 1252 */       DigestCalculator digestalg = (new JcaDigestCalculatorProviderBuilder()).build().get(new AlgorithmIdentifier(cid.getHashAlgOID(), (ASN1Encodable)DERNull.INSTANCE));
/* 1253 */       X509Certificate sigcer = getSigningCertificate();
/* 1254 */       X509Certificate isscer = cs[1];
/*      */       
/* 1256 */       CertificateID tis = new CertificateID(digestalg, (X509CertificateHolder)new JcaX509CertificateHolder(isscer), sigcer.getSerialNumber());
/* 1257 */       return tis.equals(cid);
/*      */     }
/* 1259 */     catch (Exception exception) {
/*      */       
/* 1261 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void findOcsp(ASN1Sequence seq) throws IOException {
/* 1270 */     this.basicResp = null;
/* 1271 */     boolean ret = false;
/*      */     
/* 1273 */     while (!(seq.getObjectAt(0) instanceof ASN1ObjectIdentifier) || 
/* 1274 */       !((ASN1ObjectIdentifier)seq.getObjectAt(0)).getId().equals(OCSPObjectIdentifiers.id_pkix_ocsp_basic.getId())) {
/*      */ 
/*      */       
/* 1277 */       ret = true;
/* 1278 */       for (int k = 0; k < seq.size(); k++) {
/* 1279 */         if (seq.getObjectAt(k) instanceof ASN1Sequence) {
/* 1280 */           seq = (ASN1Sequence)seq.getObjectAt(0);
/* 1281 */           ret = false;
/*      */           break;
/*      */         } 
/* 1284 */         if (seq.getObjectAt(k) instanceof ASN1TaggedObject) {
/* 1285 */           ASN1TaggedObject tag = (ASN1TaggedObject)seq.getObjectAt(k);
/* 1286 */           if (tag.getObject() instanceof ASN1Sequence) {
/* 1287 */             seq = (ASN1Sequence)tag.getObject();
/* 1288 */             ret = false;
/*      */             
/*      */             break;
/*      */           } 
/*      */           return;
/*      */         } 
/*      */       } 
/* 1295 */       if (ret)
/*      */         return; 
/*      */     } 
/* 1298 */     ASN1OctetString os = (ASN1OctetString)seq.getObjectAt(1);
/* 1299 */     ASN1InputStream inp = new ASN1InputStream(os.getOctets());
/* 1300 */     BasicOCSPResponse resp = BasicOCSPResponse.getInstance(inp.readObject());
/* 1301 */     this.basicResp = new BasicOCSPResp(resp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isTsp() {
/* 1320 */     return this.isTsp;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TimeStampToken getTimeStampToken() {
/* 1329 */     return this.timeStampToken;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Calendar getTimeStampDate() {
/* 1338 */     if (this.timeStampToken == null)
/* 1339 */       return null; 
/* 1340 */     Calendar cal = new GregorianCalendar();
/* 1341 */     Date date = this.timeStampToken.getTimeStampInfo().getGenTime();
/* 1342 */     cal.setTime(date);
/* 1343 */     return cal;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PdfName getFilterSubtype() {
/* 1350 */     return this.filterSubtype;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getEncryptionAlgorithm() {
/* 1358 */     String encryptAlgo = EncryptionAlgorithms.getAlgorithm(this.digestEncryptionAlgorithmOid);
/* 1359 */     if (encryptAlgo == null)
/* 1360 */       encryptAlgo = this.digestEncryptionAlgorithmOid; 
/* 1361 */     return encryptAlgo;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/PdfPKCS7.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */