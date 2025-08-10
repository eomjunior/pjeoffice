package br.jus.cnj.pje.office.task;

import com.github.signer4j.ISignedData;
import java.util.Optional;

public interface ISignableURLDocument extends IURLOutputDocument {
  String getSignatureFieldName();
  
  Optional<ISignedData> getSignedData();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/ISignableURLDocument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */