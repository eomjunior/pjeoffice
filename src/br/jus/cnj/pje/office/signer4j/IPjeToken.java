package br.jus.cnj.pje.office.signer4j;

import com.github.signer4j.ICertificateChooserFactory;
import com.github.signer4j.IPasswordCallbackHandler;
import com.github.signer4j.IPasswordCollector;
import com.github.signer4j.IToken;
import com.github.signer4j.ITokenCycle;
import com.github.signer4j.imp.exception.Signer4JException;

public interface IPjeToken extends ITokenCycle {
  IPjeToken login() throws Signer4JException;
  
  IPjeToken login(IPasswordCallbackHandler paramIPasswordCallbackHandler) throws Signer4JException;
  
  IPjeToken login(IPasswordCollector paramIPasswordCollector) throws Signer4JException;
  
  IPjeToken login(char[] paramArrayOfchar) throws Signer4JException;
  
  IPjeXmlSignerBuilder xmlSignerBuilder();
  
  IPjeXmlSignerBuilder xmlSignerBuilder(ICertificateChooserFactory paramICertificateChooserFactory);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/signer4j/IPjeToken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */