/*     */ package com.github.signer4j.cert.oid;
/*     */ 
/*     */ import com.github.utils4j.IConstants;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import org.bouncycastle.asn1.ASN1InputStream;
/*     */ import org.bouncycastle.asn1.ASN1ObjectIdentifier;
/*     */ import org.bouncycastle.asn1.ASN1OctetString;
/*     */ import org.bouncycastle.asn1.ASN1String;
/*     */ import org.bouncycastle.asn1.ASN1TaggedObject;
/*     */ import org.bouncycastle.asn1.DLSequence;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.x509.OtherName;
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
/*     */ class OIDFactory
/*     */ {
/* 382 */   private static final String PACKAGE = OIDFactory.class.getPackage().getName() + ".OID_";
/*     */ 
/*     */ 
/*     */   
/*     */   private static String toClass(Object id) {
/* 387 */     return PACKAGE + id.toString().replaceAll("[.]", "_");
/*     */   }
/*     */   
/*     */   private static OIDBasic create(ASN1ObjectIdentifier oid, String content) throws Exception {
/* 391 */     return create(oid.getId(), content);
/*     */   }
/*     */   
/*     */   public static OIDBasic create(DerValue der) throws IOException, Exception {
/* 395 */     OtherName otherName = new OtherName(der);
/* 396 */     return create(otherName.getOID(), (new String(otherName.getNameValue())).substring(6));
/*     */   }
/*     */   
/*     */   public static OIDBasic create(byte[] data) throws IOException, Exception {
/* 400 */     try (ASN1InputStream is = new ASN1InputStream(data)) {
/* 401 */       DLSequence sequence = (DLSequence)is.readObject();
/* 402 */       ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier)sequence.getObjectAt(OIDReader.HEADER.intValue());
/* 403 */       ASN1TaggedObject tag = (ASN1TaggedObject)sequence.getObjectAt(OIDReader.CONTENT.intValue());
/* 404 */       ASN1TaggedObject next = (ASN1TaggedObject)tag.getObject();
/* 405 */       return create(oid, getContent(next));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String getContent(ASN1TaggedObject tag) {
/* 410 */     Object content = tag.getObject();
/* 411 */     if (content instanceof ASN1String)
/* 412 */       return ((ASN1String)content).getString(); 
/* 413 */     if (content instanceof ASN1OctetString)
/* 414 */       return new String(((ASN1OctetString)content).getOctets(), IConstants.ISO_8859_1); 
/* 415 */     throw new RuntimeException("Unabled to read content from ASN1TaggedObject tag");
/*     */   }
/*     */   
/*     */   private static OIDBasic create(Object oid, String content) throws Exception {
/*     */     try {
/* 420 */       Class<?> clazz = Class.forName(toClass(oid));
/* 421 */       Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] { String.class });
/* 422 */       constructor.setAccessible(true);
/* 423 */       OIDBasic instance = (OIDBasic)constructor.newInstance(new Object[] { content });
/* 424 */       instance.setup();
/* 425 */       return instance;
/* 426 */     } catch (InstantiationException e) {
/* 427 */       throw new Exception("Unabled to instantiate class for OID " + oid, e);
/* 428 */     } catch (IllegalAccessException e) {
/* 429 */       throw new Exception("Unabled to access class for OID " + oid, e);
/* 430 */     } catch (ClassNotFoundException e) {
/* 431 */       throw new Exception("Unabled to found class for OID " + oid, e);
/* 432 */     } catch (Exception e) {
/* 433 */       e.printStackTrace();
/* 434 */       throw e;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/oid/OIDFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */