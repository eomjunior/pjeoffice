package br.jus.cnj.pje.office.core;

import com.github.utils4j.IStringDumpable;
import java.util.Optional;

public interface IPjeHttpExchangeRequest extends IPjeRequest, IStringDumpable {
  Optional<String> getParameter(String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjeHttpExchangeRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */