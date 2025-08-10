package com.github.signer4j;

import com.github.signer4j.imp.Repository;
import io.reactivex.Observable;

public interface ITokenAccess<T extends IToken> extends ITokenSupplier<T>, ICertificateAcessor {
  Observable<IStatusMonitor> newToken();
  
  void logout();
  
  boolean isAuthenticated();
  
  IAuthStrategy getAuthStrategy();
  
  void setAuthStrategy(IAuthStrategy paramIAuthStrategy);
  
  Repository getRepository();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/ITokenAccess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */