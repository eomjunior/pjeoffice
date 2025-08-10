package com.github.signer4j.cert;

import java.time.LocalDate;
import java.util.Optional;

public interface ICertificatePJ {
  Optional<String> getResponsibleName();
  
  Optional<String> getResponsibleCPF();
  
  Optional<String> getCNPJ();
  
  Optional<LocalDate> getBirthDate();
  
  Optional<String> getBusinessName();
  
  Optional<String> getNis();
  
  Optional<String> getRg();
  
  Optional<String> getIssuingAgencyRg();
  
  Optional<String> getUfIssuingAgencyRg();
  
  Optional<String> getCEI();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/ICertificatePJ.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */