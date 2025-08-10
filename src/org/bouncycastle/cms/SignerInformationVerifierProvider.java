package org.bouncycastle.cms;

import org.bouncycastle.operator.OperatorCreationException;

public interface SignerInformationVerifierProvider {
  SignerInformationVerifier get(SignerId paramSignerId) throws OperatorCreationException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cms/SignerInformationVerifierProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */