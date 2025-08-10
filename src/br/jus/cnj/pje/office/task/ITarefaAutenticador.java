package br.jus.cnj.pje.office.task;

import java.util.Optional;

public interface ITarefaAutenticador {
  Optional<String> getAlgoritmoAssinatura();
  
  Optional<String> getEnviarPara();
  
  Optional<String> getMensagem();
  
  Optional<String> getToken();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/ITarefaAutenticador.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */