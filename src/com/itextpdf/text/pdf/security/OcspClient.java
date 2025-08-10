package com.itextpdf.text.pdf.security;

import java.security.cert.X509Certificate;

public interface OcspClient {
  byte[] getEncoded(X509Certificate paramX509Certificate1, X509Certificate paramX509Certificate2, String paramString);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/OcspClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */