package org.bouncycastle.cert.crmf;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface PKMACValuesCalculator {
  void setup(AlgorithmIdentifier paramAlgorithmIdentifier1, AlgorithmIdentifier paramAlgorithmIdentifier2) throws CRMFException;
  
  byte[] calculateDigest(byte[] paramArrayOfbyte) throws CRMFException;
  
  byte[] calculateMac(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) throws CRMFException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cert/crmf/PKMACValuesCalculator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */