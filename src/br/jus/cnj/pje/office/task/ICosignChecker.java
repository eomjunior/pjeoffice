package br.jus.cnj.pje.office.task;

import br.jus.cnj.pje.office.core.imp.UnsupportedCosignException;
import java.io.File;
import java.io.IOException;

public interface ICosignChecker {
  void check(File paramFile) throws IOException, UnsupportedCosignException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/ICosignChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */