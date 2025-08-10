package br.jus.cnj.pje.office.core;

import com.github.utils4j.IStringDumpable;
import com.github.utils4j.echo.IEchoNotifier;

public interface IPjeEchoNotifier extends IEchoNotifier {
  void echoN2S(IStringDumpable paramIStringDumpable);
  
  void echoS2B(IStringDumpable paramIStringDumpable);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjeEchoNotifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */