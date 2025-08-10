package br.jus.cnj.pje.office.signer4j;

import com.github.signer4j.IByteProcessorBuilder;

public interface IPjeXmlSignerBuilder extends IByteProcessorBuilder<IPjeXmlSignerBuilder, IPjeXmlSigner> {
  IPjeXmlSignerBuilder usingHashPath(String paramString);
  
  IPjeXmlSignerBuilder usingAsymetricPath(String paramString);
  
  IPjeXmlSignerBuilder usingC14nTransformPath(String paramString);
  
  IPjeXmlSignerBuilder usingEnvelopedTransform(String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/signer4j/IPjeXmlSignerBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */