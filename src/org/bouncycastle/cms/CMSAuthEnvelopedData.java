package org.bouncycastle.cms;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.AuthEnvelopedData;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.EncryptedContentInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Encodable;

public class CMSAuthEnvelopedData implements Encodable {
  RecipientInformationStore recipientInfoStore;
  
  ContentInfo contentInfo;
  
  private OriginatorInformation originatorInfo;
  
  private AlgorithmIdentifier authEncAlg;
  
  private ASN1Set authAttrs;
  
  private byte[] mac;
  
  private ASN1Set unauthAttrs;
  
  public CMSAuthEnvelopedData(byte[] paramArrayOfbyte) throws CMSException {
    this(CMSUtils.readContentInfo(paramArrayOfbyte));
  }
  
  public CMSAuthEnvelopedData(InputStream paramInputStream) throws CMSException {
    this(CMSUtils.readContentInfo(paramInputStream));
  }
  
  public CMSAuthEnvelopedData(ContentInfo paramContentInfo) throws CMSException {
    this.contentInfo = paramContentInfo;
    AuthEnvelopedData authEnvelopedData = AuthEnvelopedData.getInstance(paramContentInfo.getContent());
    if (authEnvelopedData.getOriginatorInfo() != null)
      this.originatorInfo = new OriginatorInformation(authEnvelopedData.getOriginatorInfo()); 
    ASN1Set aSN1Set = authEnvelopedData.getRecipientInfos();
    final EncryptedContentInfo authEncInfo = authEnvelopedData.getAuthEncryptedContentInfo();
    this.authEncAlg = encryptedContentInfo.getContentEncryptionAlgorithm();
    CMSSecureReadable cMSSecureReadable = new CMSSecureReadable() {
        public InputStream getInputStream() throws IOException, CMSException {
          return new ByteArrayInputStream(authEncInfo.getEncryptedContent().getOctets());
        }
      };
    this.authAttrs = authEnvelopedData.getAuthAttrs();
    this.mac = authEnvelopedData.getMac().getOctets();
    this.unauthAttrs = authEnvelopedData.getUnauthAttrs();
    if (this.authAttrs != null) {
      this.recipientInfoStore = CMSEnvelopedHelper.buildRecipientInformationStore(aSN1Set, this.authEncAlg, cMSSecureReadable, new AuthAttributesProvider() {
            public ASN1Set getAuthAttributes() {
              return CMSAuthEnvelopedData.this.authAttrs;
            }
            
            public boolean isAead() {
              return true;
            }
          });
    } else {
      this.recipientInfoStore = CMSEnvelopedHelper.buildRecipientInformationStore(aSN1Set, this.authEncAlg, cMSSecureReadable);
    } 
  }
  
  public OriginatorInformation getOriginatorInfo() {
    return this.originatorInfo;
  }
  
  public RecipientInformationStore getRecipientInfos() {
    return this.recipientInfoStore;
  }
  
  public AttributeTable getAuthAttrs() {
    return (this.authAttrs == null) ? null : new AttributeTable(this.authAttrs);
  }
  
  public AttributeTable getUnauthAttrs() {
    return (this.unauthAttrs == null) ? null : new AttributeTable(this.unauthAttrs);
  }
  
  public byte[] getMac() {
    return Arrays.clone(this.mac);
  }
  
  public ContentInfo toASN1Structure() {
    return this.contentInfo;
  }
  
  public byte[] getEncoded() throws IOException {
    return this.contentInfo.getEncoded();
  }
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/bouncycastle/cms/CMSAuthEnvelopedData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */