package org.bouncycastle.cert.jcajce;

import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameStyle;
import org.bouncycastle.jcajce.interfaces.BCX509Certificate;

public class JcaX500NameUtil {
  public static X500Name getIssuer(X509Certificate paramX509Certificate) {
    return (paramX509Certificate instanceof BCX509Certificate) ? notNull(((BCX509Certificate)paramX509Certificate).getIssuerX500Name()) : getX500Name(paramX509Certificate.getIssuerX500Principal());
  }
  
  public static X500Name getIssuer(X500NameStyle paramX500NameStyle, X509Certificate paramX509Certificate) {
    return (paramX509Certificate instanceof BCX509Certificate) ? X500Name.getInstance(paramX500NameStyle, notNull(((BCX509Certificate)paramX509Certificate).getIssuerX500Name())) : getX500Name(paramX500NameStyle, paramX509Certificate.getIssuerX500Principal());
  }
  
  public static X500Name getSubject(X509Certificate paramX509Certificate) {
    return (paramX509Certificate instanceof BCX509Certificate) ? notNull(((BCX509Certificate)paramX509Certificate).getSubjectX500Name()) : getX500Name(paramX509Certificate.getSubjectX500Principal());
  }
  
  public static X500Name getSubject(X500NameStyle paramX500NameStyle, X509Certificate paramX509Certificate) {
    return (paramX509Certificate instanceof BCX509Certificate) ? X500Name.getInstance(paramX500NameStyle, notNull(((BCX509Certificate)paramX509Certificate).getSubjectX500Name())) : getX500Name(paramX500NameStyle, paramX509Certificate.getSubjectX500Principal());
  }
  
  public static X500Name getX500Name(X500Principal paramX500Principal) {
    return X500Name.getInstance(getEncoded(paramX500Principal));
  }
  
  public static X500Name getX500Name(X500NameStyle paramX500NameStyle, X500Principal paramX500Principal) {
    return X500Name.getInstance(paramX500NameStyle, getEncoded(paramX500Principal));
  }
  
  private static X500Name notNull(X500Name paramX500Name) {
    if (null == paramX500Name)
      throw new IllegalStateException(); 
    return paramX500Name;
  }
  
  private static X500Principal notNull(X500Principal paramX500Principal) {
    if (null == paramX500Principal)
      throw new IllegalStateException(); 
    return paramX500Principal;
  }
  
  private static byte[] getEncoded(X500Principal paramX500Principal) {
    return notNull(paramX500Principal).getEncoded();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cert/jcajce/JcaX500NameUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */