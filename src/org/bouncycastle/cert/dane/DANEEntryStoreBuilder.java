package org.bouncycastle.cert.dane;

public class DANEEntryStoreBuilder {
  private final DANEEntryFetcherFactory daneEntryFetcher;
  
  public DANEEntryStoreBuilder(DANEEntryFetcherFactory paramDANEEntryFetcherFactory) {
    this.daneEntryFetcher = paramDANEEntryFetcherFactory;
  }
  
  public DANEEntryStore build(String paramString) throws DANEException {
    return new DANEEntryStore(this.daneEntryFetcher.build(paramString).getEntries());
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cert/dane/DANEEntryStoreBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */