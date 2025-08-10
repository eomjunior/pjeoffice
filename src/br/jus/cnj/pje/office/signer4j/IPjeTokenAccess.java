package br.jus.cnj.pje.office.signer4j;

import br.jus.cnj.pje.office.signer4j.imp.PjeToken;
import com.github.signer4j.ITokenAccess;
import java.util.concurrent.locks.ReentrantLock;

public interface IPjeTokenAccess extends ITokenAccess<PjeToken> {
  ReentrantLock getLock();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/signer4j/IPjeTokenAccess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */