package org.bouncycastle.jcajce;

import java.util.Collection;
import org.bouncycastle.util.Selector;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.StoreException;

public interface PKIXCertStore<T extends java.security.cert.Certificate> extends Store<T> {
  Collection<T> getMatches(Selector<T> paramSelector) throws StoreException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/jcajce/PKIXCertStore.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */