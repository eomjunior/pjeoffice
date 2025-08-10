package br.jus.cnj.pje.office.task;

import java.util.Optional;

public interface IURLOutputDocument extends IOutputDocument {
  Optional<String> getUrl();
  
  Optional<String> getNome();
  
  boolean isTerAtributosAssinados();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/IURLOutputDocument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */