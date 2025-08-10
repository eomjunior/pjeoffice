package com.github.signer4j;

import com.github.signer4j.imp.Repository;
import java.util.Optional;
import java.util.function.Consumer;

public interface IConfigPersister {
  Optional<String> defaultCertificate();
  
  Optional<String> defaultDevice();
  
  Optional<String> defaultAlias();
  
  Repository defaultRepository();
  
  void saveA1Paths(IFilePath... paramVarArgs);
  
  void saveA3Paths(IFilePath... paramVarArgs);
  
  void loadA1Paths(Consumer<IFilePath> paramConsumer);
  
  void loadA3Paths(Consumer<IFilePath> paramConsumer);
  
  void save(String paramString);
  
  void saveRepository(Repository paramRepository);
  
  void reset();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/IConfigPersister.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */