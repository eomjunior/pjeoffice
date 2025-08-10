package br.jus.cnj.pje.office.core;

import br.jus.cnj.pje.office.task.IPayload;

public interface IPjeSecurityGrantor {
  boolean isPermitted(String paramString);
  
  boolean isPermitted(IPayload paramIPayload, StringBuilder paramStringBuilder);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjeSecurityGrantor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */