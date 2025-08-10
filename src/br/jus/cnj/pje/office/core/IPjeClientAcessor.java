package br.jus.cnj.pje.office.core;

import com.github.utils4j.ICanceller;
import java.util.function.Supplier;

public interface IPjeClientAcessor {
  IPjeClient getClient(Supplier<ICanceller> paramSupplier);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjeClientAcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */