package org.bouncycastle.cert;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.TBSCertificate;
import org.bouncycastle.asn1.x509.Time;
import org.bouncycastle.asn1.x509.V3TBSCertificateGenerator;
import org.bouncycastle.operator.ContentSigner;

public class X509v3CertificateBuilder {
  private V3TBSCertificateGenerator tbsGen = new V3TBSCertificateGenerator();
  
  private ExtensionsGenerator extGenerator;
  
  public X509v3CertificateBuilder(X500Name paramX500Name1, BigInteger paramBigInteger, Date paramDate1, Date paramDate2, X500Name paramX500Name2, SubjectPublicKeyInfo paramSubjectPublicKeyInfo) {
    this(paramX500Name1, paramBigInteger, new Time(paramDate1), new Time(paramDate2), paramX500Name2, paramSubjectPublicKeyInfo);
  }
  
  public X509v3CertificateBuilder(X500Name paramX500Name1, BigInteger paramBigInteger, Date paramDate1, Date paramDate2, Locale paramLocale, X500Name paramX500Name2, SubjectPublicKeyInfo paramSubjectPublicKeyInfo) {
    this(paramX500Name1, paramBigInteger, new Time(paramDate1, paramLocale), new Time(paramDate2, paramLocale), paramX500Name2, paramSubjectPublicKeyInfo);
  }
  
  public X509v3CertificateBuilder(X500Name paramX500Name1, BigInteger paramBigInteger, Time paramTime1, Time paramTime2, X500Name paramX500Name2, SubjectPublicKeyInfo paramSubjectPublicKeyInfo) {
    this.tbsGen.setSerialNumber(new ASN1Integer(paramBigInteger));
    this.tbsGen.setIssuer(paramX500Name1);
    this.tbsGen.setStartDate(paramTime1);
    this.tbsGen.setEndDate(paramTime2);
    this.tbsGen.setSubject(paramX500Name2);
    this.tbsGen.setSubjectPublicKeyInfo(paramSubjectPublicKeyInfo);
    this.extGenerator = new ExtensionsGenerator();
  }
  
  public X509v3CertificateBuilder(X509CertificateHolder paramX509CertificateHolder) {
    this.tbsGen.setSerialNumber(new ASN1Integer(paramX509CertificateHolder.getSerialNumber()));
    this.tbsGen.setIssuer(paramX509CertificateHolder.getIssuer());
    this.tbsGen.setStartDate(new Time(paramX509CertificateHolder.getNotBefore()));
    this.tbsGen.setEndDate(new Time(paramX509CertificateHolder.getNotAfter()));
    this.tbsGen.setSubject(paramX509CertificateHolder.getSubject());
    this.tbsGen.setSubjectPublicKeyInfo(paramX509CertificateHolder.getSubjectPublicKeyInfo());
    this.extGenerator = new ExtensionsGenerator();
    Extensions extensions = paramX509CertificateHolder.getExtensions();
    Enumeration<ASN1ObjectIdentifier> enumeration = extensions.oids();
    while (enumeration.hasMoreElements())
      this.extGenerator.addExtension(extensions.getExtension(enumeration.nextElement())); 
  }
  
  public boolean hasExtension(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
    return (doGetExtension(paramASN1ObjectIdentifier) != null);
  }
  
  public Extension getExtension(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
    return doGetExtension(paramASN1ObjectIdentifier);
  }
  
  private Extension doGetExtension(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
    Extensions extensions = this.extGenerator.generate();
    return extensions.getExtension(paramASN1ObjectIdentifier);
  }
  
  public X509v3CertificateBuilder setSubjectUniqueID(boolean[] paramArrayOfboolean) {
    this.tbsGen.setSubjectUniqueID(booleanToBitString(paramArrayOfboolean));
    return this;
  }
  
  public X509v3CertificateBuilder setIssuerUniqueID(boolean[] paramArrayOfboolean) {
    this.tbsGen.setIssuerUniqueID(booleanToBitString(paramArrayOfboolean));
    return this;
  }
  
  public X509v3CertificateBuilder addExtension(ASN1ObjectIdentifier paramASN1ObjectIdentifier, boolean paramBoolean, ASN1Encodable paramASN1Encodable) throws CertIOException {
    try {
      this.extGenerator.addExtension(paramASN1ObjectIdentifier, paramBoolean, paramASN1Encodable);
    } catch (IOException iOException) {
      throw new CertIOException("cannot encode extension: " + iOException.getMessage(), iOException);
    } 
    return this;
  }
  
  public X509v3CertificateBuilder addExtension(Extension paramExtension) throws CertIOException {
    this.extGenerator.addExtension(paramExtension);
    return this;
  }
  
  public X509v3CertificateBuilder addExtension(ASN1ObjectIdentifier paramASN1ObjectIdentifier, boolean paramBoolean, byte[] paramArrayOfbyte) throws CertIOException {
    this.extGenerator.addExtension(paramASN1ObjectIdentifier, paramBoolean, paramArrayOfbyte);
    return this;
  }
  
  public X509v3CertificateBuilder replaceExtension(ASN1ObjectIdentifier paramASN1ObjectIdentifier, boolean paramBoolean, ASN1Encodable paramASN1Encodable) throws CertIOException {
    try {
      this.extGenerator = CertUtils.doReplaceExtension(this.extGenerator, new Extension(paramASN1ObjectIdentifier, paramBoolean, paramASN1Encodable.toASN1Primitive().getEncoded("DER")));
    } catch (IOException iOException) {
      throw new CertIOException("cannot encode extension: " + iOException.getMessage(), iOException);
    } 
    return this;
  }
  
  public X509v3CertificateBuilder replaceExtension(Extension paramExtension) throws CertIOException {
    this.extGenerator = CertUtils.doReplaceExtension(this.extGenerator, paramExtension);
    return this;
  }
  
  public X509v3CertificateBuilder replaceExtension(ASN1ObjectIdentifier paramASN1ObjectIdentifier, boolean paramBoolean, byte[] paramArrayOfbyte) throws CertIOException {
    this.extGenerator = CertUtils.doReplaceExtension(this.extGenerator, new Extension(paramASN1ObjectIdentifier, paramBoolean, paramArrayOfbyte));
    return this;
  }
  
  public X509v3CertificateBuilder removeExtension(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
    this.extGenerator = CertUtils.doRemoveExtension(this.extGenerator, paramASN1ObjectIdentifier);
    return this;
  }
  
  public X509v3CertificateBuilder copyAndAddExtension(ASN1ObjectIdentifier paramASN1ObjectIdentifier, boolean paramBoolean, X509CertificateHolder paramX509CertificateHolder) {
    Certificate certificate = paramX509CertificateHolder.toASN1Structure();
    Extension extension = certificate.getTBSCertificate().getExtensions().getExtension(paramASN1ObjectIdentifier);
    if (extension == null)
      throw new NullPointerException("extension " + paramASN1ObjectIdentifier + " not present"); 
    this.extGenerator.addExtension(paramASN1ObjectIdentifier, paramBoolean, extension.getExtnValue().getOctets());
    return this;
  }
  
  public X509CertificateHolder build(ContentSigner paramContentSigner) {
    this.tbsGen.setSignature(paramContentSigner.getAlgorithmIdentifier());
    if (!this.extGenerator.isEmpty())
      this.tbsGen.setExtensions(this.extGenerator.generate()); 
    try {
      TBSCertificate tBSCertificate = this.tbsGen.generateTBSCertificate();
      return new X509CertificateHolder(generateStructure(tBSCertificate, paramContentSigner.getAlgorithmIdentifier(), generateSig(paramContentSigner, (ASN1Object)tBSCertificate)));
    } catch (IOException iOException) {
      throw new IllegalArgumentException("cannot produce certificate signature");
    } 
  }
  
  private static byte[] generateSig(ContentSigner paramContentSigner, ASN1Object paramASN1Object) throws IOException {
    OutputStream outputStream = paramContentSigner.getOutputStream();
    paramASN1Object.encodeTo(outputStream, "DER");
    outputStream.close();
    return paramContentSigner.getSignature();
  }
  
  private static Certificate generateStructure(TBSCertificate paramTBSCertificate, AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfbyte) {
    ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
    aSN1EncodableVector.add((ASN1Encodable)paramTBSCertificate);
    aSN1EncodableVector.add((ASN1Encodable)paramAlgorithmIdentifier);
    aSN1EncodableVector.add((ASN1Encodable)new DERBitString(paramArrayOfbyte));
    return Certificate.getInstance(new DERSequence(aSN1EncodableVector));
  }
  
  static DERBitString booleanToBitString(boolean[] paramArrayOfboolean) {
    byte[] arrayOfByte = new byte[(paramArrayOfboolean.length + 7) / 8];
    int i;
    for (i = 0; i != paramArrayOfboolean.length; i++)
      arrayOfByte[i / 8] = (byte)(arrayOfByte[i / 8] | (paramArrayOfboolean[i] ? (1 << 7 - i % 8) : 0)); 
    i = paramArrayOfboolean.length % 8;
    return (i == 0) ? new DERBitString(arrayOfByte) : new DERBitString(arrayOfByte, 8 - i);
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cert/X509v3CertificateBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */