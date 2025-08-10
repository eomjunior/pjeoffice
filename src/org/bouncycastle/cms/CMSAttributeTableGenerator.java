package org.bouncycastle.cms;

import java.util.Map;
import org.bouncycastle.asn1.cms.AttributeTable;

public interface CMSAttributeTableGenerator {
  public static final String CONTENT_TYPE = "contentType";
  
  public static final String DIGEST = "digest";
  
  public static final String SIGNATURE = "encryptedDigest";
  
  public static final String DIGEST_ALGORITHM_IDENTIFIER = "digestAlgID";
  
  public static final String MAC_ALGORITHM_IDENTIFIER = "macAlgID";
  
  public static final String SIGNATURE_ALGORITHM_IDENTIFIER = "signatureAlgID";
  
  AttributeTable getAttributes(Map paramMap) throws CMSAttributeTableGenerationException;
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cms/CMSAttributeTableGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */