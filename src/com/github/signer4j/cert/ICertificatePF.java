package com.github.signer4j.cert;

import java.time.LocalDate;
import java.util.Optional;

public interface ICertificatePF {
  Optional<String> getCPF();
  
  Optional<LocalDate> getBirthDate();
  
  Optional<String> getNis();
  
  Optional<String> getRg();
  
  Optional<String> getIssuingAgencyRg();
  
  Optional<String> getUfIssuingAgencyRg();
  
  Optional<String> getElectoralDocument();
  
  Optional<String> getSectionElectoralDocument();
  
  Optional<String> getZoneElectoralDocument();
  
  Optional<String> getCityElectoralDocument();
  
  Optional<String> getUFElectoralDocument();
  
  Optional<String> getCEI();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/ICertificatePF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */