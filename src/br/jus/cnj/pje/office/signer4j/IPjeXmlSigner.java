package br.jus.cnj.pje.office.signer4j;

import com.github.signer4j.IByteProcessor;
import com.github.signer4j.ISignedData;
import com.github.signer4j.imp.exception.Signer4JException;
import java.io.InputStream;

public interface IPjeXmlSigner extends IByteProcessor {
  ISignedData process(InputStream paramInputStream) throws Signer4JException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/signer4j/IPjeXmlSigner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */