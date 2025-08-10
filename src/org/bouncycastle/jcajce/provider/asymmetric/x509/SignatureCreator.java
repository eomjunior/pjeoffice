package org.bouncycastle.jcajce.provider.asymmetric.x509;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;

interface SignatureCreator {
  Signature createSignature(String paramString) throws NoSuchAlgorithmException, NoSuchProviderException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/provider/asymmetric/x509/SignatureCreator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */