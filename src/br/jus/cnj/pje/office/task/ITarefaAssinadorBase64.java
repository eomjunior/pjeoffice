package br.jus.cnj.pje.office.task;

import java.util.List;
import java.util.Optional;

public interface ITarefaAssinadorBase64 {
  Optional<String> getAlgoritmoAssinatura();
  
  Optional<String> getUploadUrl();
  
  List<IInputDocument64> getArquivos();
  
  @Deprecated
  boolean isDeslogarKeyStore();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/ITarefaAssinadorBase64.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */