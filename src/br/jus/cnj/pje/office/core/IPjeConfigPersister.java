package br.jus.cnj.pje.office.core;

import com.github.signer4j.IAuthStrategy;
import com.github.signer4j.IConfigPersister;
import java.util.Optional;
import java.util.function.Consumer;

public interface IPjeConfigPersister extends IConfigPersister {
  void loadServerAccess(Consumer<IPjeServerAccess> paramConsumer);
  
  Optional<String> authStrategy();
  
  void save(IPjeServerAccess... paramVarArgs);
  
  void save(IAuthStrategy paramIAuthStrategy);
  
  void overwrite(IPjeServerAccess... paramVarArgs);
  
  void delete(IPjeServerAccess paramIPjeServerAccess);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjeConfigPersister.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */