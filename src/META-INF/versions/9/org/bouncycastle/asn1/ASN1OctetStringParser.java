package META-INF.versions.9.org.bouncycastle.asn1;

import java.io.InputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.InMemoryRepresentable;

public interface ASN1OctetStringParser extends ASN1Encodable, InMemoryRepresentable {
  InputStream getOctetStream();
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/asn1/ASN1OctetStringParser.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */