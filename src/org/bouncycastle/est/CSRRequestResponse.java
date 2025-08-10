package org.bouncycastle.est;

public class CSRRequestResponse {
  private final CSRAttributesResponse attributesResponse;
  
  private final Source source;
  
  public CSRRequestResponse(CSRAttributesResponse paramCSRAttributesResponse, Source paramSource) {
    this.attributesResponse = paramCSRAttributesResponse;
    this.source = paramSource;
  }
  
  public boolean hasAttributesResponse() {
    return (this.attributesResponse != null);
  }
  
  public CSRAttributesResponse getAttributesResponse() {
    if (this.attributesResponse == null)
      throw new IllegalStateException("Response has no CSRAttributesResponse."); 
    return this.attributesResponse;
  }
  
  public Object getSession() {
    return this.source.getSession();
  }
  
  public Source getSource() {
    return this.source;
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/est/CSRRequestResponse.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */