package br.jus.cnj.pje.office.core;

import br.jus.cnj.pje.office.core.imp.PjeTaskResponse;
import com.github.utils4j.ISocketCodec;
import java.io.Closeable;

public interface IPjeSocketCodec<T> extends ISocketCodec<T, PjeTaskResponse>, Closeable {
  boolean isClosed();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjeSocketCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */