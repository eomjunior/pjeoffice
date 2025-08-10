package org.bouncycastle.jcajce.provider.asymmetric.x509;

import java.security.cert.CRLException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.IssuingDistributionPoint;
import org.bouncycastle.jcajce.util.JcaJceHelper;

class X509CRLObject extends X509CRLImpl {
  private final Object cacheLock = new Object();
  
  private X509CRLInternal internalCRLValue;
  
  private volatile boolean hashValueSet;
  
  private volatile int hashValue;
  
  X509CRLObject(JcaJceHelper paramJcaJceHelper, CertificateList paramCertificateList) throws CRLException {
    super(paramJcaJceHelper, paramCertificateList, createSigAlgName(paramCertificateList), createSigAlgParams(paramCertificateList), isIndirectCRL(paramCertificateList));
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject instanceof X509CRLObject) {
      X509CRLObject x509CRLObject = (X509CRLObject)paramObject;
      if (this.hashValueSet && x509CRLObject.hashValueSet) {
        if (this.hashValue != x509CRLObject.hashValue)
          return false; 
      } else if (null == this.internalCRLValue || null == x509CRLObject.internalCRLValue) {
        DERBitString dERBitString = this.c.getSignature();
        if (null != dERBitString && !dERBitString.equals((ASN1Primitive)x509CRLObject.c.getSignature()))
          return false; 
      } 
    } 
    return getInternalCRL().equals(paramObject);
  }
  
  public int hashCode() {
    if (!this.hashValueSet) {
      this.hashValue = getInternalCRL().hashCode();
      this.hashValueSet = true;
    } 
    return this.hashValue;
  }
  
  private X509CRLInternal getInternalCRL() {
    byte[] arrayOfByte;
    synchronized (this.cacheLock) {
      if (null != this.internalCRLValue)
        return this.internalCRLValue; 
    } 
    try {
      arrayOfByte = getEncoded();
    } catch (CRLException cRLException) {
      arrayOfByte = null;
    } 
    X509CRLInternal x509CRLInternal = new X509CRLInternal(this.bcHelper, this.c, this.sigAlgName, this.sigAlgParams, this.isIndirect, arrayOfByte);
    synchronized (this.cacheLock) {
      if (null == this.internalCRLValue)
        this.internalCRLValue = x509CRLInternal; 
      return this.internalCRLValue;
    } 
  }
  
  private static String createSigAlgName(CertificateList paramCertificateList) throws CRLException {
    try {
      return X509SignatureUtil.getSignatureName(paramCertificateList.getSignatureAlgorithm());
    } catch (Exception exception) {
      throw new CRLException("CRL contents invalid: " + exception);
    } 
  }
  
  private static byte[] createSigAlgParams(CertificateList paramCertificateList) throws CRLException {
    try {
      ASN1Encodable aSN1Encodable = paramCertificateList.getSignatureAlgorithm().getParameters();
      return (null == aSN1Encodable) ? null : aSN1Encodable.toASN1Primitive().getEncoded("DER");
    } catch (Exception exception) {
      throw new CRLException("CRL contents invalid: " + exception);
    } 
  }
  
  private static boolean isIndirectCRL(CertificateList paramCertificateList) throws CRLException {
    try {
      byte[] arrayOfByte = getExtensionOctets(paramCertificateList, Extension.issuingDistributionPoint.getId());
      return (null == arrayOfByte) ? false : IssuingDistributionPoint.getInstance(arrayOfByte).isIndirectCRL();
    } catch (Exception exception) {
      throw new ExtCRLException("Exception reading IssuingDistributionPoint", exception);
    } 
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/provider/asymmetric/x509/X509CRLObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */