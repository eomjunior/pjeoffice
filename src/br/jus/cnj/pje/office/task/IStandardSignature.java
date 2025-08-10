package br.jus.cnj.pje.office.task;

import br.jus.cnj.pje.office.signer4j.IPjeToken;
import com.github.signer4j.IByteProcessorBuilder;
import com.github.taskresolver4j.exception.TaskException;
import com.github.utils4j.IContentType;

public interface IStandardSignature extends IContentType {
  IByteProcessorBuilder<?, ?> processorBuilder(IPjeToken paramIPjeToken, ITarefaAssinador paramITarefaAssinador);
  
  IStandardSignature check(ITarefaAssinador paramITarefaAssinador) throws TaskException;
  
  ICosignChecker cosignChecker();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/IStandardSignature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */