package br.jus.cnj.pje.office.core;

import br.jus.cnj.pje.office.core.imp.PjeClientException;
import br.jus.cnj.pje.office.core.imp.PjeTaskResponse;
import br.jus.cnj.pje.office.task.IOutputDocument;
import br.jus.cnj.pje.office.task.IPjeEndpoint;
import br.jus.cnj.pje.office.task.ISSOPayload;
import br.jus.cnj.pje.office.task.ISignableURLDocument;
import com.github.signer4j.ISignedData;
import com.github.utils4j.IContentType;
import com.github.utils4j.IDownloadStatus;

public interface IPjeClient {
  void download(IPjeEndpoint paramIPjeEndpoint, IDownloadStatus paramIDownloadStatus) throws PjeClientException, InterruptedException;
  
  PjeTaskResponse send(IPjeEndpoint paramIPjeEndpoint, Object paramObject) throws PjeClientException, InterruptedException;
  
  PjeTaskResponse send(IPjeEndpoint paramIPjeEndpoint, ISSOPayload paramISSOPayload) throws PjeClientException, InterruptedException;
  
  PjeTaskResponse send(IPjeEndpoint paramIPjeEndpoint, ISignedData paramISignedData) throws PjeClientException, InterruptedException;
  
  PjeTaskResponse send(IPjeEndpoint paramIPjeEndpoint, String paramString) throws PjeClientException, InterruptedException;
  
  PjeTaskResponse send(IPjeEndpoint paramIPjeEndpoint, ISignedData paramISignedData, IOutputDocument paramIOutputDocument) throws PjeClientException, InterruptedException;
  
  PjeTaskResponse send(IPjeEndpoint paramIPjeEndpoint, ISignableURLDocument paramISignableURLDocument, IContentType paramIContentType) throws PjeClientException, InterruptedException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/IPjeClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */