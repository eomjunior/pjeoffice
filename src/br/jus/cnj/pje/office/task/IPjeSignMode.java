package br.jus.cnj.pje.office.task;

import br.jus.cnj.pje.office.core.IPjeResponse;
import com.github.taskresolver4j.ITask;
import com.github.utils4j.imp.Params;

public interface IPjeSignMode {
  ITask<IPjeResponse> getTask(Params paramParams, ITarefaAssinador paramITarefaAssinador);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/IPjeSignMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */