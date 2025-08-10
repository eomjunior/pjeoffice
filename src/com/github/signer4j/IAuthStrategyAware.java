package com.github.signer4j;

import com.github.signer4j.imp.AuthStrategy;
import com.github.signer4j.imp.Repository;
import io.reactivex.Observable;

public interface IAuthStrategyAware extends IsAuthStrategy {
  void setAuthStrategy(AuthStrategy paramAuthStrategy, boolean paramBoolean);
  
  Observable<Repository> newRepository();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IAuthStrategyAware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */