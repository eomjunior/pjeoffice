package com.github.signer4j;

import com.github.signer4j.imp.SwitchRepositoryException;
import com.github.signer4j.imp.exception.Signer4JException;

public interface ICertificateChooser {
  IChoice choose() throws Signer4JException, SwitchRepositoryException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/ICertificateChooser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */