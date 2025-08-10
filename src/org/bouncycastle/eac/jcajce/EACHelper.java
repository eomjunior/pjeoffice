package org.bouncycastle.eac.jcajce;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

interface EACHelper {
  KeyFactory createKeyFactory(String paramString) throws NoSuchProviderException, NoSuchAlgorithmException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/eac/jcajce/EACHelper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */