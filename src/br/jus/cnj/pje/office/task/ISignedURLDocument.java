package br.jus.cnj.pje.office.task;

import br.jus.cnj.pje.office.core.imp.UnsupportedCosignException;
import com.github.signer4j.IByteProcessor;
import com.github.signer4j.imp.exception.Signer4JException;
import java.io.IOException;

public interface ISignedURLDocument extends ISignableURLDocument {
  void sign(IByteProcessor paramIByteProcessor, ICosignChecker paramICosignChecker) throws Signer4JException, IOException, UnsupportedCosignException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/ISignedURLDocument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */