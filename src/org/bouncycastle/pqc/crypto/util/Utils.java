package org.bouncycastle.pqc.crypto.util;

import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.digests.SHAKEDigest;
import org.bouncycastle.pqc.asn1.PQCObjectIdentifiers;
import org.bouncycastle.pqc.asn1.SPHINCS256KeyParams;
import org.bouncycastle.util.Integers;

class Utils {
  static final AlgorithmIdentifier AlgID_qTESLA_p_I = new AlgorithmIdentifier(PQCObjectIdentifiers.qTESLA_p_I);
  
  static final AlgorithmIdentifier AlgID_qTESLA_p_III = new AlgorithmIdentifier(PQCObjectIdentifiers.qTESLA_p_III);
  
  static final AlgorithmIdentifier SPHINCS_SHA3_256 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha3_256);
  
  static final AlgorithmIdentifier SPHINCS_SHA512_256 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha512_256);
  
  static final AlgorithmIdentifier XMSS_SHA256 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256);
  
  static final AlgorithmIdentifier XMSS_SHA512 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha512);
  
  static final AlgorithmIdentifier XMSS_SHAKE128 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_shake128);
  
  static final AlgorithmIdentifier XMSS_SHAKE256 = new AlgorithmIdentifier(NISTObjectIdentifiers.id_shake256);
  
  static final Map categories = new HashMap<Object, Object>();
  
  static int qTeslaLookupSecurityCategory(AlgorithmIdentifier paramAlgorithmIdentifier) {
    return ((Integer)categories.get(paramAlgorithmIdentifier.getAlgorithm())).intValue();
  }
  
  static AlgorithmIdentifier qTeslaLookupAlgID(int paramInt) {
    switch (paramInt) {
      case 5:
        return AlgID_qTESLA_p_I;
      case 6:
        return AlgID_qTESLA_p_III;
    } 
    throw new IllegalArgumentException("unknown security category: " + paramInt);
  }
  
  static AlgorithmIdentifier sphincs256LookupTreeAlgID(String paramString) {
    if (paramString.equals("SHA3-256"))
      return SPHINCS_SHA3_256; 
    if (paramString.equals("SHA-512/256"))
      return SPHINCS_SHA512_256; 
    throw new IllegalArgumentException("unknown tree digest: " + paramString);
  }
  
  static AlgorithmIdentifier xmssLookupTreeAlgID(String paramString) {
    if (paramString.equals("SHA-256"))
      return XMSS_SHA256; 
    if (paramString.equals("SHA-512"))
      return XMSS_SHA512; 
    if (paramString.equals("SHAKE128"))
      return XMSS_SHAKE128; 
    if (paramString.equals("SHAKE256"))
      return XMSS_SHAKE256; 
    throw new IllegalArgumentException("unknown tree digest: " + paramString);
  }
  
  static String sphincs256LookupTreeAlgName(SPHINCS256KeyParams paramSPHINCS256KeyParams) {
    AlgorithmIdentifier algorithmIdentifier = paramSPHINCS256KeyParams.getTreeDigest();
    if (algorithmIdentifier.getAlgorithm().equals((ASN1Primitive)SPHINCS_SHA3_256.getAlgorithm()))
      return "SHA3-256"; 
    if (algorithmIdentifier.getAlgorithm().equals((ASN1Primitive)SPHINCS_SHA512_256.getAlgorithm()))
      return "SHA-512/256"; 
    throw new IllegalArgumentException("unknown tree digest: " + algorithmIdentifier.getAlgorithm());
  }
  
  static Digest getDigest(ASN1ObjectIdentifier paramASN1ObjectIdentifier) {
    if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_sha256))
      return (Digest)new SHA256Digest(); 
    if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_sha512))
      return (Digest)new SHA512Digest(); 
    if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_shake128))
      return (Digest)new SHAKEDigest(128); 
    if (paramASN1ObjectIdentifier.equals((ASN1Primitive)NISTObjectIdentifiers.id_shake256))
      return (Digest)new SHAKEDigest(256); 
    throw new IllegalArgumentException("unrecognized digest OID: " + paramASN1ObjectIdentifier);
  }
  
  static {
    categories.put(PQCObjectIdentifiers.qTESLA_p_I, Integers.valueOf(5));
    categories.put(PQCObjectIdentifiers.qTESLA_p_III, Integers.valueOf(6));
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/pqc/crypto/util/Utils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */