/*     */ package org.apache.hc.client5.http.ssl;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateParsingException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLPeerUnverifiedException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import org.apache.hc.client5.http.psl.DomainType;
/*     */ import org.apache.hc.client5.http.psl.PublicSuffixMatcher;
/*     */ import org.apache.hc.client5.http.utils.DnsUtils;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.net.InetAddressUtils;
/*     */ import org.apache.hc.core5.util.TextUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public final class DefaultHostnameVerifier
/*     */   implements HttpClientHostnameVerifier
/*     */ {
/*     */   enum HostNameType
/*     */   {
/*  66 */     IPv4(7), IPv6(7), DNS(2);
/*     */     
/*     */     final int subjectType;
/*     */     
/*     */     HostNameType(int subjectType) {
/*  71 */       this.subjectType = subjectType;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  76 */   private static final Logger LOG = LoggerFactory.getLogger(DefaultHostnameVerifier.class);
/*     */   
/*     */   private final PublicSuffixMatcher publicSuffixMatcher;
/*     */   
/*     */   public DefaultHostnameVerifier(PublicSuffixMatcher publicSuffixMatcher) {
/*  81 */     this.publicSuffixMatcher = publicSuffixMatcher;
/*     */   }
/*     */   
/*     */   public DefaultHostnameVerifier() {
/*  85 */     this(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean verify(String host, SSLSession session) {
/*     */     try {
/*  91 */       Certificate[] certs = session.getPeerCertificates();
/*  92 */       X509Certificate x509 = (X509Certificate)certs[0];
/*  93 */       verify(host, x509);
/*  94 */       return true;
/*  95 */     } catch (SSLException ex) {
/*  96 */       if (LOG.isDebugEnabled()) {
/*  97 */         LOG.debug(ex.getMessage(), ex);
/*     */       }
/*  99 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void verify(String host, X509Certificate cert) throws SSLException {
/* 105 */     HostNameType hostType = determineHostFormat(host);
/* 106 */     switch (hostType) {
/*     */       case IPv4:
/* 108 */         matchIPAddress(host, getSubjectAltNames(cert, 7));
/*     */         return;
/*     */       case IPv6:
/* 111 */         matchIPv6Address(host, getSubjectAltNames(cert, 7));
/*     */         return;
/*     */     } 
/* 114 */     List<SubjectName> subjectAlts = getSubjectAltNames(cert, 2);
/* 115 */     if (subjectAlts.isEmpty()) {
/*     */ 
/*     */       
/* 118 */       matchCN(host, cert, this.publicSuffixMatcher);
/*     */     } else {
/* 120 */       matchDNSName(host, subjectAlts, this.publicSuffixMatcher);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void matchIPAddress(String host, List<SubjectName> subjectAlts) throws SSLException {
/* 126 */     for (int i = 0; i < subjectAlts.size(); i++) {
/* 127 */       SubjectName subjectAlt = subjectAlts.get(i);
/* 128 */       if (subjectAlt.getType() == 7 && 
/* 129 */         host.equals(subjectAlt.getValue())) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/* 134 */     throw new SSLPeerUnverifiedException("Certificate for <" + host + "> doesn't match any of the subject alternative names: " + subjectAlts);
/*     */   }
/*     */ 
/*     */   
/*     */   static void matchIPv6Address(String host, List<SubjectName> subjectAlts) throws SSLException {
/* 139 */     String normalisedHost = normaliseAddress(host);
/* 140 */     for (int i = 0; i < subjectAlts.size(); i++) {
/* 141 */       SubjectName subjectAlt = subjectAlts.get(i);
/* 142 */       if (subjectAlt.getType() == 7) {
/* 143 */         String normalizedSubjectAlt = normaliseAddress(subjectAlt.getValue());
/* 144 */         if (normalisedHost.equals(normalizedSubjectAlt)) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } 
/* 149 */     throw new SSLPeerUnverifiedException("Certificate for <" + host + "> doesn't match any of the subject alternative names: " + subjectAlts);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void matchDNSName(String host, List<SubjectName> subjectAlts, PublicSuffixMatcher publicSuffixMatcher) throws SSLException {
/* 155 */     String normalizedHost = DnsUtils.normalize(host);
/* 156 */     for (int i = 0; i < subjectAlts.size(); i++) {
/* 157 */       SubjectName subjectAlt = subjectAlts.get(i);
/* 158 */       if (subjectAlt.getType() == 2) {
/* 159 */         String normalizedSubjectAlt = DnsUtils.normalize(subjectAlt.getValue());
/* 160 */         if (matchIdentityStrict(normalizedHost, normalizedSubjectAlt, publicSuffixMatcher)) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } 
/* 165 */     throw new SSLPeerUnverifiedException("Certificate for <" + host + "> doesn't match any of the subject alternative names: " + subjectAlts);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void matchCN(String host, X509Certificate cert, PublicSuffixMatcher publicSuffixMatcher) throws SSLException {
/* 171 */     X500Principal subjectPrincipal = cert.getSubjectX500Principal();
/* 172 */     String cn = extractCN(subjectPrincipal.getName("RFC2253"));
/* 173 */     if (cn == null) {
/* 174 */       throw new SSLPeerUnverifiedException("Certificate subject for <" + host + "> doesn't contain a common name and does not have alternative names");
/*     */     }
/*     */     
/* 177 */     String normalizedHost = DnsUtils.normalize(host);
/* 178 */     String normalizedCn = DnsUtils.normalize(cn);
/* 179 */     if (!matchIdentityStrict(normalizedHost, normalizedCn, publicSuffixMatcher)) {
/* 180 */       throw new SSLPeerUnverifiedException("Certificate for <" + host + "> doesn't match common name of the certificate subject: " + cn);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean matchDomainRoot(String host, String domainRoot) {
/* 186 */     if (domainRoot == null) {
/* 187 */       return false;
/*     */     }
/* 189 */     return (host.endsWith(domainRoot) && (host.length() == domainRoot.length() || host
/* 190 */       .charAt(host.length() - domainRoot.length() - 1) == '.'));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean matchIdentity(String host, String identity, PublicSuffixMatcher publicSuffixMatcher, DomainType domainType, boolean strict) {
/* 197 */     if (publicSuffixMatcher != null && host.contains(".") && 
/* 198 */       !matchDomainRoot(host, publicSuffixMatcher.getDomainRoot(identity, domainType))) {
/* 199 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     int asteriskIdx = identity.indexOf('*');
/* 209 */     if (asteriskIdx != -1) {
/* 210 */       String prefix = identity.substring(0, asteriskIdx);
/* 211 */       String suffix = identity.substring(asteriskIdx + 1);
/* 212 */       if (!prefix.isEmpty() && !host.startsWith(prefix)) {
/* 213 */         return false;
/*     */       }
/* 215 */       if (!suffix.isEmpty() && !host.endsWith(suffix)) {
/* 216 */         return false;
/*     */       }
/*     */       
/* 219 */       if (strict) {
/* 220 */         String remainder = host.substring(prefix
/* 221 */             .length(), host.length() - suffix.length());
/* 222 */         return !remainder.contains(".");
/*     */       } 
/* 224 */       return true;
/*     */     } 
/* 226 */     return host.equalsIgnoreCase(identity);
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean matchIdentity(String host, String identity, PublicSuffixMatcher publicSuffixMatcher) {
/* 231 */     return matchIdentity(host, identity, publicSuffixMatcher, null, false);
/*     */   }
/*     */   
/*     */   static boolean matchIdentity(String host, String identity) {
/* 235 */     return matchIdentity(host, identity, null, null, false);
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean matchIdentityStrict(String host, String identity, PublicSuffixMatcher publicSuffixMatcher) {
/* 240 */     return matchIdentity(host, identity, publicSuffixMatcher, null, true);
/*     */   }
/*     */   
/*     */   static boolean matchIdentityStrict(String host, String identity) {
/* 244 */     return matchIdentity(host, identity, null, null, true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean matchIdentity(String host, String identity, PublicSuffixMatcher publicSuffixMatcher, DomainType domainType) {
/* 250 */     return matchIdentity(host, identity, publicSuffixMatcher, domainType, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean matchIdentityStrict(String host, String identity, PublicSuffixMatcher publicSuffixMatcher, DomainType domainType) {
/* 256 */     return matchIdentity(host, identity, publicSuffixMatcher, domainType, true);
/*     */   }
/*     */   
/*     */   static String extractCN(String subjectPrincipal) throws SSLException {
/* 260 */     if (subjectPrincipal == null) {
/* 261 */       return null;
/*     */     }
/* 263 */     List<NameValuePair> attributes = DistinguishedNameParser.INSTANCE.parse(subjectPrincipal);
/* 264 */     for (NameValuePair attribute : attributes) {
/* 265 */       if (TextUtils.isBlank(attribute.getName()) || attribute.getValue() == null) {
/* 266 */         throw new SSLException(subjectPrincipal + " is not a valid X500 distinguished name");
/*     */       }
/* 268 */       if (attribute.getName().equalsIgnoreCase("cn")) {
/* 269 */         return attribute.getValue();
/*     */       }
/*     */     } 
/* 272 */     return null;
/*     */   }
/*     */   
/*     */   static HostNameType determineHostFormat(String host) {
/* 276 */     if (InetAddressUtils.isIPv4Address(host)) {
/* 277 */       return HostNameType.IPv4;
/*     */     }
/* 279 */     String s = host;
/* 280 */     if (s.startsWith("[") && s.endsWith("]")) {
/* 281 */       s = host.substring(1, host.length() - 1);
/*     */     }
/* 283 */     if (InetAddressUtils.isIPv6Address(s)) {
/* 284 */       return HostNameType.IPv6;
/*     */     }
/* 286 */     return HostNameType.DNS;
/*     */   }
/*     */   
/*     */   static List<SubjectName> getSubjectAltNames(X509Certificate cert) {
/* 290 */     return getSubjectAltNames(cert, -1);
/*     */   }
/*     */   
/*     */   static List<SubjectName> getSubjectAltNames(X509Certificate cert, int subjectName) {
/*     */     try {
/* 295 */       Collection<List<?>> entries = cert.getSubjectAlternativeNames();
/* 296 */       if (entries == null) {
/* 297 */         return Collections.emptyList();
/*     */       }
/* 299 */       List<SubjectName> result = new ArrayList<>();
/* 300 */       for (List<?> entry : entries) {
/* 301 */         Integer type = (entry.size() >= 2) ? (Integer)entry.get(0) : null;
/* 302 */         if (type != null && (
/* 303 */           type.intValue() == subjectName || -1 == subjectName)) {
/* 304 */           Object o = entry.get(1);
/* 305 */           if (o instanceof String) {
/* 306 */             result.add(new SubjectName((String)o, type.intValue())); continue;
/* 307 */           }  if (o instanceof byte[]);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 313 */       return result;
/* 314 */     } catch (CertificateParsingException ignore) {
/* 315 */       return Collections.emptyList();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String normaliseAddress(String hostname) {
/* 323 */     if (hostname == null) {
/* 324 */       return hostname;
/*     */     }
/*     */     try {
/* 327 */       InetAddress inetAddress = InetAddress.getByName(hostname);
/* 328 */       return inetAddress.getHostAddress();
/* 329 */     } catch (UnknownHostException unexpected) {
/* 330 */       return hostname;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/ssl/DefaultHostnameVerifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */