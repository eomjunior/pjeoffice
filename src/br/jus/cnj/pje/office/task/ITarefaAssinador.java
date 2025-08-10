package br.jus.cnj.pje.office.task;

import com.github.signer4j.ISignatureAlgorithm;
import com.github.signer4j.ISignatureType;
import java.util.List;
import java.util.Optional;

public interface ITarefaAssinador {
  List<IURLOutputDocument> getArquivos();
  
  Optional<String> getEnviarPara();
  
  Optional<IPjeSignMode> getModo();
  
  Optional<ISignatureType> getTipoAssinatura();
  
  @Deprecated
  boolean isDeslogarKeyStore();
  
  Optional<ISignatureAlgorithm> getAlgoritmoHash();
  
  Optional<IStandardSignature> getPadraoAssinatura();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/ITarefaAssinador.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */