package br.jus.cnj.pje.office.task;

import java.util.List;
import java.util.Optional;

public interface ITarefaAssinadorHash {
  boolean isModoTeste();
  
  @Deprecated
  boolean isDeslogarKeyStore();
  
  Optional<String> getAlgoritmoAssinatura();
  
  Optional<String> getUploadUrl();
  
  List<IHashedOutputDocument> getArquivos();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/ITarefaAssinadorHash.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */