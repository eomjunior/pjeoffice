package br.jus.cnj.pje.office.core;

import br.jus.cnj.pje.office.IBootable;
import com.github.signer4j.IAuthStrategyAware;
import com.github.signer4j.IStatusMonitor;
import com.github.signer4j.imp.AuthStrategy;
import com.github.utils4j.imp.Media;
import io.reactivex.Observable;
import java.io.File;
import java.util.List;
import java.util.function.Function;

public interface IPjeOffice extends IBootable, IAuthStrategyAware {
  void showCertificates();
  
  void showAuthorizedServers();
  
  void showAbout();
  
  void showHelp();
  
  void setDevMode(boolean paramBoolean);
  
  @Deprecated
  void setUnsafe(boolean paramBoolean);
  
  void setAuthStrategy(AuthStrategy paramAuthStrategy, boolean paramBoolean);
  
  void kill();
  
  @Deprecated
  boolean isUnsafe();
  
  boolean isDevMode();
  
  void update(boolean paramBoolean);
  
  void selectTo(String paramString, Media paramMedia, Function<List<File>, List<File>> paramFunction);
  
  Observable<IStatusMonitor> newToken();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjeOffice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */