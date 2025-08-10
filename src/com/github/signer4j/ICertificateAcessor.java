package com.github.signer4j;

import com.github.signer4j.imp.SwitchRepositoryException;
import java.util.Optional;

public interface ICertificateAcessor {
  Optional<ICertificateListUI.ICertificateEntry> showCertificates(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) throws SwitchRepositoryException;
  
  void close();
  
  void reset();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/ICertificateAcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */