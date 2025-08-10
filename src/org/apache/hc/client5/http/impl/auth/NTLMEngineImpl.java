/*      */ package org.apache.hc.client5.http.impl.auth;
/*      */ 
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.security.GeneralSecurityException;
/*      */ import java.security.Key;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.security.SecureRandom;
/*      */ import java.security.cert.Certificate;
/*      */ import java.security.cert.CertificateEncodingException;
/*      */ import java.util.Arrays;
/*      */ import java.util.Locale;
/*      */ import java.util.Random;
/*      */ import javax.crypto.Cipher;
/*      */ import javax.crypto.spec.SecretKeySpec;
/*      */ import org.apache.hc.client5.http.utils.Base64;
/*      */ import org.apache.hc.client5.http.utils.ByteArrayBuilder;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ final class NTLMEngineImpl
/*      */   implements NTLMEngine
/*      */ {
/*   55 */   private static final Charset UNICODE_LITTLE_UNMARKED = Charset.forName("UnicodeLittleUnmarked");
/*      */   
/*   57 */   private static final Charset DEFAULT_CHARSET = StandardCharsets.US_ASCII;
/*      */   
/*      */   static final int FLAG_REQUEST_UNICODE_ENCODING = 1;
/*      */   
/*      */   static final int FLAG_REQUEST_OEM_ENCODING = 2;
/*      */   
/*      */   static final int FLAG_REQUEST_TARGET = 4;
/*      */   
/*      */   static final int FLAG_REQUEST_SIGN = 16;
/*      */   
/*      */   static final int FLAG_REQUEST_SEAL = 32;
/*      */   
/*      */   static final int FLAG_REQUEST_LAN_MANAGER_KEY = 128;
/*      */   
/*      */   static final int FLAG_REQUEST_NTLMv1 = 512;
/*      */   
/*      */   static final int FLAG_DOMAIN_PRESENT = 4096;
/*      */   
/*      */   static final int FLAG_WORKSTATION_PRESENT = 8192;
/*      */   
/*      */   static final int FLAG_REQUEST_ALWAYS_SIGN = 32768;
/*      */   
/*      */   static final int FLAG_REQUEST_NTLM2_SESSION = 524288;
/*      */   static final int FLAG_REQUEST_VERSION = 33554432;
/*      */   static final int FLAG_TARGETINFO_PRESENT = 8388608;
/*      */   static final int FLAG_REQUEST_128BIT_KEY_EXCH = 536870912;
/*      */   static final int FLAG_REQUEST_EXPLICIT_KEY_EXCH = 1073741824;
/*      */   static final int FLAG_REQUEST_56BIT_ENCRYPTION = -2147483648;
/*      */   static final int MSV_AV_EOL = 0;
/*      */   static final int MSV_AV_NB_COMPUTER_NAME = 1;
/*      */   static final int MSV_AV_NB_DOMAIN_NAME = 2;
/*      */   static final int MSV_AV_DNS_COMPUTER_NAME = 3;
/*      */   static final int MSV_AV_DNS_DOMAIN_NAME = 4;
/*      */   static final int MSV_AV_DNS_TREE_NAME = 5;
/*      */   static final int MSV_AV_FLAGS = 6;
/*      */   static final int MSV_AV_TIMESTAMP = 7;
/*      */   static final int MSV_AV_SINGLE_HOST = 8;
/*      */   static final int MSV_AV_TARGET_NAME = 9;
/*      */   static final int MSV_AV_CHANNEL_BINDINGS = 10;
/*      */   static final int MSV_AV_FLAGS_ACCOUNT_AUTH_CONSTAINED = 1;
/*      */   static final int MSV_AV_FLAGS_MIC = 2;
/*      */   static final int MSV_AV_FLAGS_UNTRUSTED_TARGET_SPN = 4;
/*      */   private static final SecureRandom RND_GEN;
/*      */   
/*      */   static {
/*  102 */     SecureRandom rnd = null;
/*      */     try {
/*  104 */       rnd = SecureRandom.getInstance("SHA1PRNG");
/*  105 */     } catch (Exception exception) {}
/*      */ 
/*      */     
/*  108 */     RND_GEN = rnd;
/*      */   }
/*      */ 
/*      */   
/*  112 */   private static final byte[] SIGNATURE = getNullTerminatedAsciiString("NTLMSSP");
/*      */ 
/*      */ 
/*      */   
/*  116 */   private static final byte[] SIGN_MAGIC_SERVER = getNullTerminatedAsciiString("session key to server-to-client signing key magic constant");
/*      */   
/*  118 */   private static final byte[] SIGN_MAGIC_CLIENT = getNullTerminatedAsciiString("session key to client-to-server signing key magic constant");
/*      */   
/*  120 */   private static final byte[] SEAL_MAGIC_SERVER = getNullTerminatedAsciiString("session key to server-to-client sealing key magic constant");
/*      */   
/*  122 */   private static final byte[] SEAL_MAGIC_CLIENT = getNullTerminatedAsciiString("session key to client-to-server sealing key magic constant");
/*      */ 
/*      */ 
/*      */   
/*  126 */   private static final byte[] MAGIC_TLS_SERVER_ENDPOINT = "tls-server-end-point:".getBytes(StandardCharsets.US_ASCII);
/*      */ 
/*      */   
/*      */   private static byte[] getNullTerminatedAsciiString(String source) {
/*  130 */     byte[] bytesWithoutNull = source.getBytes(StandardCharsets.US_ASCII);
/*  131 */     byte[] target = new byte[bytesWithoutNull.length + 1];
/*  132 */     System.arraycopy(bytesWithoutNull, 0, target, 0, bytesWithoutNull.length);
/*  133 */     target[bytesWithoutNull.length] = 0;
/*  134 */     return target;
/*      */   }
/*      */   
/*  137 */   private static final String TYPE_1_MESSAGE = (new Type1Message()).getResponse();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String getResponseFor(String message, String username, char[] password, String host, String domain) throws NTLMEngineException {
/*      */     String response;
/*  161 */     if (message == null || message.trim().equals("")) {
/*  162 */       response = getType1Message(host, domain);
/*      */     } else {
/*  164 */       Type2Message t2m = new Type2Message(message);
/*  165 */       response = getType3Message(username, password, host, domain, t2m.getChallenge(), t2m
/*  166 */           .getFlags(), t2m.getTarget(), t2m.getTargetInfo());
/*      */     } 
/*  168 */     return response;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String getResponseFor(String message, String username, char[] password, String host, String domain, Certificate peerServerCertificate) throws NTLMEngineException {
/*      */     String response;
/*  190 */     if (message == null || message.trim().equals("")) {
/*  191 */       response = (new Type1Message(host, domain)).getResponse();
/*      */     } else {
/*  193 */       Type1Message t1m = new Type1Message(host, domain);
/*  194 */       Type2Message t2m = new Type2Message(message);
/*  195 */       response = getType3Message(username, password, host, domain, t2m.getChallenge(), t2m
/*  196 */           .getFlags(), t2m.getTarget(), t2m.getTargetInfo(), peerServerCertificate, t1m
/*  197 */           .getBytes(), t2m.getBytes());
/*      */     } 
/*  199 */     return response;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String getType1Message(String host, String domain) {
/*  216 */     return TYPE_1_MESSAGE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String getType3Message(String user, char[] password, String host, String domain, byte[] nonce, int type2Flags, String target, byte[] targetInformation) throws NTLMEngineException {
/*  242 */     return (new Type3Message(domain, host, user, password, nonce, type2Flags, target, targetInformation))
/*  243 */       .getResponse();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String getType3Message(String user, char[] password, String host, String domain, byte[] nonce, int type2Flags, String target, byte[] targetInformation, Certificate peerServerCertificate, byte[] type1Message, byte[] type2Message) throws NTLMEngineException {
/*  268 */     return (new Type3Message(domain, host, user, password, nonce, type2Flags, target, targetInformation, peerServerCertificate, type1Message, type2Message))
/*  269 */       .getResponse();
/*      */   }
/*      */   
/*      */   private static int readULong(byte[] src, int index) {
/*  273 */     if (src.length < index + 4) {
/*  274 */       return 0;
/*      */     }
/*  276 */     return src[index] & 0xFF | (src[index + 1] & 0xFF) << 8 | (src[index + 2] & 0xFF) << 16 | (src[index + 3] & 0xFF) << 24;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int readUShort(byte[] src, int index) {
/*  281 */     if (src.length < index + 2) {
/*  282 */       return 0;
/*      */     }
/*  284 */     return src[index] & 0xFF | (src[index + 1] & 0xFF) << 8;
/*      */   }
/*      */   
/*      */   private static byte[] readSecurityBuffer(byte[] src, int index) {
/*  288 */     int length = readUShort(src, index);
/*  289 */     int offset = readULong(src, index + 4);
/*  290 */     if (src.length < offset + length) {
/*  291 */       return new byte[length];
/*      */     }
/*  293 */     byte[] buffer = new byte[length];
/*  294 */     System.arraycopy(src, offset, buffer, 0, length);
/*  295 */     return buffer;
/*      */   }
/*      */ 
/*      */   
/*      */   private static byte[] makeRandomChallenge(Random random) {
/*  300 */     byte[] rval = new byte[8];
/*  301 */     synchronized (random) {
/*  302 */       random.nextBytes(rval);
/*      */     } 
/*  304 */     return rval;
/*      */   }
/*      */ 
/*      */   
/*      */   private static byte[] makeSecondaryKey(Random random) {
/*  309 */     byte[] rval = new byte[16];
/*  310 */     synchronized (random) {
/*  311 */       random.nextBytes(rval);
/*      */     } 
/*  313 */     return rval;
/*      */   }
/*      */ 
/*      */   
/*      */   static class CipherGen
/*      */   {
/*      */     final Random random;
/*      */     
/*      */     final long currentTime;
/*      */     
/*      */     final String domain;
/*      */     
/*      */     final String user;
/*      */     
/*      */     final char[] password;
/*      */     
/*      */     final byte[] challenge;
/*      */     
/*      */     final String target;
/*      */     
/*      */     final byte[] targetInformation;
/*      */     
/*      */     byte[] clientChallenge;
/*      */     byte[] clientChallenge2;
/*      */     byte[] secondaryKey;
/*      */     byte[] timestamp;
/*      */     byte[] lmHash;
/*      */     byte[] lmResponse;
/*      */     byte[] ntlmHash;
/*      */     byte[] ntlmResponse;
/*      */     byte[] ntlmv2Hash;
/*      */     byte[] lmv2Hash;
/*      */     byte[] lmv2Response;
/*      */     byte[] ntlmv2Blob;
/*      */     byte[] ntlmv2Response;
/*      */     byte[] ntlm2SessionResponse;
/*      */     byte[] lm2SessionResponse;
/*      */     byte[] lmUserSessionKey;
/*      */     byte[] ntlmUserSessionKey;
/*      */     byte[] ntlmv2UserSessionKey;
/*      */     byte[] ntlm2SessionResponseUserSessionKey;
/*      */     byte[] lanManagerSessionKey;
/*      */     
/*      */     public CipherGen(Random random, long currentTime, String domain, String user, char[] password, byte[] challenge, String target, byte[] targetInformation, byte[] clientChallenge, byte[] clientChallenge2, byte[] secondaryKey, byte[] timestamp) {
/*  357 */       this.random = random;
/*  358 */       this.currentTime = currentTime;
/*      */       
/*  360 */       this.domain = domain;
/*  361 */       this.target = target;
/*  362 */       this.user = user;
/*  363 */       this.password = password;
/*  364 */       this.challenge = challenge;
/*  365 */       this.targetInformation = targetInformation;
/*  366 */       this.clientChallenge = clientChallenge;
/*  367 */       this.clientChallenge2 = clientChallenge2;
/*  368 */       this.secondaryKey = secondaryKey;
/*  369 */       this.timestamp = timestamp;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public CipherGen(Random random, long currentTime, String domain, String user, char[] password, byte[] challenge, String target, byte[] targetInformation) {
/*  379 */       this(random, currentTime, domain, user, password, challenge, target, targetInformation, null, null, null, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] getClientChallenge() {
/*  384 */       if (this.clientChallenge == null) {
/*  385 */         this.clientChallenge = NTLMEngineImpl.makeRandomChallenge(this.random);
/*      */       }
/*  387 */       return this.clientChallenge;
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] getClientChallenge2() {
/*  392 */       if (this.clientChallenge2 == null) {
/*  393 */         this.clientChallenge2 = NTLMEngineImpl.makeRandomChallenge(this.random);
/*      */       }
/*  395 */       return this.clientChallenge2;
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] getSecondaryKey() {
/*  400 */       if (this.secondaryKey == null) {
/*  401 */         this.secondaryKey = NTLMEngineImpl.makeSecondaryKey(this.random);
/*      */       }
/*  403 */       return this.secondaryKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLMHash() throws NTLMEngineException {
/*  409 */       if (this.lmHash == null) {
/*  410 */         this.lmHash = NTLMEngineImpl.lmHash(this.password);
/*      */       }
/*  412 */       return this.lmHash;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLMResponse() throws NTLMEngineException {
/*  418 */       if (this.lmResponse == null) {
/*  419 */         this.lmResponse = NTLMEngineImpl.lmResponse(getLMHash(), this.challenge);
/*      */       }
/*  421 */       return this.lmResponse;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMHash() throws NTLMEngineException {
/*  427 */       if (this.ntlmHash == null) {
/*  428 */         this.ntlmHash = NTLMEngineImpl.ntlmHash(this.password);
/*      */       }
/*  430 */       return this.ntlmHash;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMResponse() throws NTLMEngineException {
/*  436 */       if (this.ntlmResponse == null) {
/*  437 */         this.ntlmResponse = NTLMEngineImpl.lmResponse(getNTLMHash(), this.challenge);
/*      */       }
/*  439 */       return this.ntlmResponse;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLMv2Hash() throws NTLMEngineException {
/*  445 */       if (this.lmv2Hash == null) {
/*  446 */         this.lmv2Hash = NTLMEngineImpl.lmv2Hash(this.domain, this.user, getNTLMHash());
/*      */       }
/*  448 */       return this.lmv2Hash;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMv2Hash() throws NTLMEngineException {
/*  454 */       if (this.ntlmv2Hash == null) {
/*  455 */         this.ntlmv2Hash = NTLMEngineImpl.ntlmv2Hash(this.domain, this.user, getNTLMHash());
/*      */       }
/*  457 */       return this.ntlmv2Hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] getTimestamp() {
/*  462 */       if (this.timestamp == null) {
/*  463 */         long time = this.currentTime;
/*  464 */         time += 11644473600000L;
/*  465 */         time *= 10000L;
/*      */         
/*  467 */         this.timestamp = new byte[8];
/*  468 */         for (int i = 0; i < 8; i++) {
/*  469 */           this.timestamp[i] = (byte)(int)time;
/*  470 */           time >>>= 8L;
/*      */         } 
/*      */       } 
/*  473 */       return this.timestamp;
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] getNTLMv2Blob() {
/*  478 */       if (this.ntlmv2Blob == null) {
/*  479 */         this.ntlmv2Blob = NTLMEngineImpl.createBlob(getClientChallenge2(), this.targetInformation, getTimestamp());
/*      */       }
/*  481 */       return this.ntlmv2Blob;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMv2Response() throws NTLMEngineException {
/*  487 */       if (this.ntlmv2Response == null) {
/*  488 */         this.ntlmv2Response = NTLMEngineImpl.lmv2Response(getNTLMv2Hash(), this.challenge, getNTLMv2Blob());
/*      */       }
/*  490 */       return this.ntlmv2Response;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLMv2Response() throws NTLMEngineException {
/*  496 */       if (this.lmv2Response == null) {
/*  497 */         this.lmv2Response = NTLMEngineImpl.lmv2Response(getLMv2Hash(), this.challenge, getClientChallenge());
/*      */       }
/*  499 */       return this.lmv2Response;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLM2SessionResponse() throws NTLMEngineException {
/*  505 */       if (this.ntlm2SessionResponse == null) {
/*  506 */         this.ntlm2SessionResponse = NTLMEngineImpl.ntlm2SessionResponse(getNTLMHash(), this.challenge, getClientChallenge());
/*      */       }
/*  508 */       return this.ntlm2SessionResponse;
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] getLM2SessionResponse() {
/*  513 */       if (this.lm2SessionResponse == null) {
/*  514 */         byte[] clntChallenge = getClientChallenge();
/*  515 */         this.lm2SessionResponse = new byte[24];
/*  516 */         System.arraycopy(clntChallenge, 0, this.lm2SessionResponse, 0, clntChallenge.length);
/*  517 */         Arrays.fill(this.lm2SessionResponse, clntChallenge.length, this.lm2SessionResponse.length, (byte)0);
/*      */       } 
/*  519 */       return this.lm2SessionResponse;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLMUserSessionKey() throws NTLMEngineException {
/*  525 */       if (this.lmUserSessionKey == null) {
/*  526 */         this.lmUserSessionKey = new byte[16];
/*  527 */         System.arraycopy(getLMHash(), 0, this.lmUserSessionKey, 0, 8);
/*  528 */         Arrays.fill(this.lmUserSessionKey, 8, 16, (byte)0);
/*      */       } 
/*  530 */       return this.lmUserSessionKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMUserSessionKey() throws NTLMEngineException {
/*  536 */       if (this.ntlmUserSessionKey == null) {
/*  537 */         NTLMEngineImpl.MD4 md4 = new NTLMEngineImpl.MD4();
/*  538 */         md4.update(getNTLMHash());
/*  539 */         this.ntlmUserSessionKey = md4.getOutput();
/*      */       } 
/*  541 */       return this.ntlmUserSessionKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLMv2UserSessionKey() throws NTLMEngineException {
/*  547 */       if (this.ntlmv2UserSessionKey == null) {
/*  548 */         byte[] ntlmv2hash = getNTLMv2Hash();
/*  549 */         byte[] truncatedResponse = new byte[16];
/*  550 */         System.arraycopy(getNTLMv2Response(), 0, truncatedResponse, 0, 16);
/*  551 */         this.ntlmv2UserSessionKey = NTLMEngineImpl.hmacMD5(truncatedResponse, ntlmv2hash);
/*      */       } 
/*  553 */       return this.ntlmv2UserSessionKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getNTLM2SessionResponseUserSessionKey() throws NTLMEngineException {
/*  559 */       if (this.ntlm2SessionResponseUserSessionKey == null) {
/*  560 */         byte[] ntlm2SessionResponseNonce = getLM2SessionResponse();
/*  561 */         byte[] sessionNonce = new byte[this.challenge.length + ntlm2SessionResponseNonce.length];
/*  562 */         System.arraycopy(this.challenge, 0, sessionNonce, 0, this.challenge.length);
/*  563 */         System.arraycopy(ntlm2SessionResponseNonce, 0, sessionNonce, this.challenge.length, ntlm2SessionResponseNonce.length);
/*  564 */         this.ntlm2SessionResponseUserSessionKey = NTLMEngineImpl.hmacMD5(sessionNonce, getNTLMUserSessionKey());
/*      */       } 
/*  566 */       return this.ntlm2SessionResponseUserSessionKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getLanManagerSessionKey() throws NTLMEngineException {
/*  572 */       if (this.lanManagerSessionKey == null) {
/*      */         try {
/*  574 */           byte[] keyBytes = new byte[14];
/*  575 */           System.arraycopy(getLMHash(), 0, keyBytes, 0, 8);
/*  576 */           Arrays.fill(keyBytes, 8, keyBytes.length, (byte)-67);
/*  577 */           Key lowKey = NTLMEngineImpl.createDESKey(keyBytes, 0);
/*  578 */           Key highKey = NTLMEngineImpl.createDESKey(keyBytes, 7);
/*  579 */           byte[] truncatedResponse = new byte[8];
/*  580 */           System.arraycopy(getLMResponse(), 0, truncatedResponse, 0, truncatedResponse.length);
/*  581 */           Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
/*  582 */           des.init(1, lowKey);
/*  583 */           byte[] lowPart = des.doFinal(truncatedResponse);
/*  584 */           des = Cipher.getInstance("DES/ECB/NoPadding");
/*  585 */           des.init(1, highKey);
/*  586 */           byte[] highPart = des.doFinal(truncatedResponse);
/*  587 */           this.lanManagerSessionKey = new byte[16];
/*  588 */           System.arraycopy(lowPart, 0, this.lanManagerSessionKey, 0, lowPart.length);
/*  589 */           System.arraycopy(highPart, 0, this.lanManagerSessionKey, lowPart.length, highPart.length);
/*  590 */         } catch (Exception e) {
/*  591 */           throw new NTLMEngineException(e.getMessage(), e);
/*      */         } 
/*      */       }
/*  594 */       return this.lanManagerSessionKey;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static byte[] hmacMD5(byte[] value, byte[] key) {
/*  600 */     HMACMD5 hmacMD5 = new HMACMD5(key);
/*  601 */     hmacMD5.update(value);
/*  602 */     return hmacMD5.getOutput();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] RC4(byte[] value, byte[] key) throws NTLMEngineException {
/*      */     try {
/*  609 */       Cipher rc4 = Cipher.getInstance("RC4");
/*  610 */       rc4.init(1, new SecretKeySpec(key, "RC4"));
/*  611 */       return rc4.doFinal(value);
/*  612 */     } catch (Exception e) {
/*  613 */       throw new NTLMEngineException(e.getMessage(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] ntlm2SessionResponse(byte[] ntlmHash, byte[] challenge, byte[] clientChallenge) throws NTLMEngineException {
/*      */     try {
/*  628 */       MessageDigest md5 = getMD5();
/*  629 */       md5.update(challenge);
/*  630 */       md5.update(clientChallenge);
/*  631 */       byte[] digest = md5.digest();
/*      */       
/*  633 */       byte[] sessionHash = new byte[8];
/*  634 */       System.arraycopy(digest, 0, sessionHash, 0, 8);
/*  635 */       return lmResponse(ntlmHash, sessionHash);
/*  636 */     } catch (Exception e) {
/*  637 */       if (e instanceof NTLMEngineException) {
/*  638 */         throw (NTLMEngineException)e;
/*      */       }
/*  640 */       throw new NTLMEngineException(e.getMessage(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] lmHash(char[] password) throws NTLMEngineException {
/*      */     try {
/*  655 */       char[] tmp = new char[password.length];
/*  656 */       for (int i = 0; i < password.length; i++) {
/*  657 */         tmp[i] = Character.toUpperCase(password[i]);
/*      */       }
/*  659 */       byte[] oemPassword = (new ByteArrayBuilder()).append(tmp).toByteArray();
/*  660 */       int length = Math.min(oemPassword.length, 14);
/*  661 */       byte[] keyBytes = new byte[14];
/*  662 */       System.arraycopy(oemPassword, 0, keyBytes, 0, length);
/*  663 */       Key lowKey = createDESKey(keyBytes, 0);
/*  664 */       Key highKey = createDESKey(keyBytes, 7);
/*  665 */       byte[] magicConstant = "KGS!@#$%".getBytes(StandardCharsets.US_ASCII);
/*  666 */       Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
/*  667 */       des.init(1, lowKey);
/*  668 */       byte[] lowHash = des.doFinal(magicConstant);
/*  669 */       des.init(1, highKey);
/*  670 */       byte[] highHash = des.doFinal(magicConstant);
/*  671 */       byte[] lmHash = new byte[16];
/*  672 */       System.arraycopy(lowHash, 0, lmHash, 0, 8);
/*  673 */       System.arraycopy(highHash, 0, lmHash, 8, 8);
/*  674 */       return lmHash;
/*  675 */     } catch (Exception e) {
/*  676 */       throw new NTLMEngineException(e.getMessage(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] ntlmHash(char[] password) throws NTLMEngineException {
/*  691 */     byte[] unicodePassword = (new ByteArrayBuilder()).charset(UNICODE_LITTLE_UNMARKED).append(password).toByteArray();
/*  692 */     MD4 md4 = new MD4();
/*  693 */     md4.update(unicodePassword);
/*  694 */     return md4.getOutput();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] lmv2Hash(String domain, String user, byte[] ntlmHash) throws NTLMEngineException {
/*  705 */     HMACMD5 hmacMD5 = new HMACMD5(ntlmHash);
/*      */     
/*  707 */     hmacMD5.update(user.toUpperCase(Locale.ROOT).getBytes(UNICODE_LITTLE_UNMARKED));
/*  708 */     if (domain != null) {
/*  709 */       hmacMD5.update(domain.toUpperCase(Locale.ROOT).getBytes(UNICODE_LITTLE_UNMARKED));
/*      */     }
/*  711 */     return hmacMD5.getOutput();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] ntlmv2Hash(String domain, String user, byte[] ntlmHash) throws NTLMEngineException {
/*  722 */     HMACMD5 hmacMD5 = new HMACMD5(ntlmHash);
/*      */     
/*  724 */     hmacMD5.update(user.toUpperCase(Locale.ROOT).getBytes(UNICODE_LITTLE_UNMARKED));
/*  725 */     if (domain != null) {
/*  726 */       hmacMD5.update(domain.getBytes(UNICODE_LITTLE_UNMARKED));
/*      */     }
/*  728 */     return hmacMD5.getOutput();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] lmResponse(byte[] hash, byte[] challenge) throws NTLMEngineException {
/*      */     try {
/*  743 */       byte[] keyBytes = new byte[21];
/*  744 */       System.arraycopy(hash, 0, keyBytes, 0, 16);
/*  745 */       Key lowKey = createDESKey(keyBytes, 0);
/*  746 */       Key middleKey = createDESKey(keyBytes, 7);
/*  747 */       Key highKey = createDESKey(keyBytes, 14);
/*  748 */       Cipher des = Cipher.getInstance("DES/ECB/NoPadding");
/*  749 */       des.init(1, lowKey);
/*  750 */       byte[] lowResponse = des.doFinal(challenge);
/*  751 */       des.init(1, middleKey);
/*  752 */       byte[] middleResponse = des.doFinal(challenge);
/*  753 */       des.init(1, highKey);
/*  754 */       byte[] highResponse = des.doFinal(challenge);
/*  755 */       byte[] lmResponse = new byte[24];
/*  756 */       System.arraycopy(lowResponse, 0, lmResponse, 0, 8);
/*  757 */       System.arraycopy(middleResponse, 0, lmResponse, 8, 8);
/*  758 */       System.arraycopy(highResponse, 0, lmResponse, 16, 8);
/*  759 */       return lmResponse;
/*  760 */     } catch (Exception e) {
/*  761 */       throw new NTLMEngineException(e.getMessage(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] lmv2Response(byte[] hash, byte[] challenge, byte[] clientData) {
/*  780 */     HMACMD5 hmacMD5 = new HMACMD5(hash);
/*  781 */     hmacMD5.update(challenge);
/*  782 */     hmacMD5.update(clientData);
/*  783 */     byte[] mac = hmacMD5.getOutput();
/*  784 */     byte[] lmv2Response = new byte[mac.length + clientData.length];
/*  785 */     System.arraycopy(mac, 0, lmv2Response, 0, mac.length);
/*  786 */     System.arraycopy(clientData, 0, lmv2Response, mac.length, clientData.length);
/*  787 */     return lmv2Response;
/*      */   }
/*      */   
/*      */   enum Mode
/*      */   {
/*  792 */     CLIENT, SERVER;
/*      */   }
/*      */ 
/*      */   
/*      */   static class Handle
/*      */   {
/*      */     private final byte[] signingKey;
/*      */     
/*      */     private byte[] sealingKey;
/*      */     private final Cipher rc4;
/*      */     final NTLMEngineImpl.Mode mode;
/*      */     private final boolean isConnection;
/*      */     int sequenceNumber;
/*      */     
/*      */     Handle(byte[] exportedSessionKey, NTLMEngineImpl.Mode mode, boolean isConnection) throws NTLMEngineException {
/*  807 */       this.isConnection = isConnection;
/*  808 */       this.mode = mode;
/*      */       
/*      */       try {
/*  811 */         MessageDigest signMd5 = NTLMEngineImpl.getMD5();
/*  812 */         MessageDigest sealMd5 = NTLMEngineImpl.getMD5();
/*  813 */         signMd5.update(exportedSessionKey);
/*  814 */         sealMd5.update(exportedSessionKey);
/*  815 */         if (mode == NTLMEngineImpl.Mode.CLIENT) {
/*      */           
/*  817 */           signMd5.update(NTLMEngineImpl.SIGN_MAGIC_CLIENT);
/*  818 */           sealMd5.update(NTLMEngineImpl.SEAL_MAGIC_CLIENT);
/*      */         }
/*      */         else {
/*      */           
/*  822 */           signMd5.update(NTLMEngineImpl.SIGN_MAGIC_SERVER);
/*  823 */           sealMd5.update(NTLMEngineImpl.SEAL_MAGIC_SERVER);
/*      */         } 
/*  825 */         this.signingKey = signMd5.digest();
/*  826 */         this.sealingKey = sealMd5.digest();
/*      */       }
/*  828 */       catch (Exception e) {
/*      */         
/*  830 */         throw new NTLMEngineException(e.getMessage(), e);
/*      */       } 
/*  832 */       this.rc4 = initCipher();
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] getSigningKey() {
/*  837 */       return this.signingKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getSealingKey() {
/*  843 */       return this.sealingKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private Cipher initCipher() throws NTLMEngineException {
/*      */       Cipher cipher;
/*      */       try {
/*  851 */         cipher = Cipher.getInstance("RC4");
/*  852 */         if (this.mode == NTLMEngineImpl.Mode.CLIENT)
/*      */         {
/*  854 */           cipher.init(1, new SecretKeySpec(this.sealingKey, "RC4"));
/*      */         }
/*      */         else
/*      */         {
/*  858 */           cipher.init(2, new SecretKeySpec(this.sealingKey, "RC4"));
/*      */         }
/*      */       
/*  861 */       } catch (Exception e) {
/*      */         
/*  863 */         throw new NTLMEngineException(e.getMessage(), e);
/*      */       } 
/*  865 */       return cipher;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void advanceMessageSequence() throws NTLMEngineException {
/*  871 */       if (!this.isConnection) {
/*      */         
/*  873 */         MessageDigest sealMd5 = NTLMEngineImpl.getMD5();
/*  874 */         sealMd5.update(this.sealingKey);
/*  875 */         byte[] seqNumBytes = new byte[4];
/*  876 */         NTLMEngineImpl.writeULong(seqNumBytes, this.sequenceNumber, 0);
/*  877 */         sealMd5.update(seqNumBytes);
/*  878 */         this.sealingKey = sealMd5.digest();
/*  879 */         initCipher();
/*      */       } 
/*  881 */       this.sequenceNumber++;
/*      */     }
/*      */ 
/*      */     
/*      */     private byte[] encrypt(byte[] data) {
/*  886 */       return this.rc4.update(data);
/*      */     }
/*      */ 
/*      */     
/*      */     private byte[] decrypt(byte[] data) {
/*  891 */       return this.rc4.update(data);
/*      */     }
/*      */ 
/*      */     
/*      */     private byte[] computeSignature(byte[] message) {
/*  896 */       byte[] sig = new byte[16];
/*      */ 
/*      */       
/*  899 */       sig[0] = 1;
/*  900 */       sig[1] = 0;
/*  901 */       sig[2] = 0;
/*  902 */       sig[3] = 0;
/*      */ 
/*      */       
/*  905 */       NTLMEngineImpl.HMACMD5 hmacMD5 = new NTLMEngineImpl.HMACMD5(this.signingKey);
/*  906 */       hmacMD5.update(NTLMEngineImpl.encodeLong(this.sequenceNumber));
/*  907 */       hmacMD5.update(message);
/*  908 */       byte[] hmac = hmacMD5.getOutput();
/*  909 */       byte[] trimmedHmac = new byte[8];
/*  910 */       System.arraycopy(hmac, 0, trimmedHmac, 0, 8);
/*  911 */       byte[] encryptedHmac = encrypt(trimmedHmac);
/*  912 */       System.arraycopy(encryptedHmac, 0, sig, 4, 8);
/*      */ 
/*      */       
/*  915 */       NTLMEngineImpl.encodeLong(sig, 12, this.sequenceNumber);
/*      */       
/*  917 */       return sig;
/*      */     }
/*      */ 
/*      */     
/*      */     private boolean validateSignature(byte[] signature, byte[] message) {
/*  922 */       byte[] computedSignature = computeSignature(message);
/*      */ 
/*      */ 
/*      */       
/*  926 */       return Arrays.equals(signature, computedSignature);
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] signAndEncryptMessage(byte[] cleartextMessage) throws NTLMEngineException {
/*  931 */       byte[] encryptedMessage = encrypt(cleartextMessage);
/*  932 */       byte[] signature = computeSignature(cleartextMessage);
/*  933 */       byte[] outMessage = new byte[signature.length + encryptedMessage.length];
/*  934 */       System.arraycopy(signature, 0, outMessage, 0, signature.length);
/*  935 */       System.arraycopy(encryptedMessage, 0, outMessage, signature.length, encryptedMessage.length);
/*  936 */       advanceMessageSequence();
/*  937 */       return outMessage;
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] decryptAndVerifySignedMessage(byte[] inMessage) throws NTLMEngineException {
/*  942 */       byte[] signature = new byte[16];
/*  943 */       System.arraycopy(inMessage, 0, signature, 0, signature.length);
/*  944 */       byte[] encryptedMessage = new byte[inMessage.length - 16];
/*  945 */       System.arraycopy(inMessage, 16, encryptedMessage, 0, encryptedMessage.length);
/*  946 */       byte[] cleartextMessage = decrypt(encryptedMessage);
/*  947 */       if (!validateSignature(signature, cleartextMessage))
/*      */       {
/*  949 */         throw new NTLMEngineException("Wrong signature");
/*      */       }
/*  951 */       advanceMessageSequence();
/*  952 */       return cleartextMessage;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] encodeLong(int value) {
/*  959 */     byte[] enc = new byte[4];
/*  960 */     encodeLong(enc, 0, value);
/*  961 */     return enc;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void encodeLong(byte[] buf, int offset, int value) {
/*  966 */     buf[offset + 0] = (byte)(value & 0xFF);
/*  967 */     buf[offset + 1] = (byte)(value >> 8 & 0xFF);
/*  968 */     buf[offset + 2] = (byte)(value >> 16 & 0xFF);
/*  969 */     buf[offset + 3] = (byte)(value >> 24 & 0xFF);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] createBlob(byte[] clientChallenge, byte[] targetInformation, byte[] timestamp) {
/*  984 */     byte[] blobSignature = { 1, 1, 0, 0 };
/*  985 */     byte[] reserved = { 0, 0, 0, 0 };
/*  986 */     byte[] unknown1 = { 0, 0, 0, 0 };
/*  987 */     byte[] unknown2 = { 0, 0, 0, 0 };
/*  988 */     byte[] blob = new byte[blobSignature.length + reserved.length + timestamp.length + 8 + unknown1.length + targetInformation.length + unknown2.length];
/*      */     
/*  990 */     int offset = 0;
/*  991 */     System.arraycopy(blobSignature, 0, blob, offset, blobSignature.length);
/*  992 */     offset += blobSignature.length;
/*  993 */     System.arraycopy(reserved, 0, blob, offset, reserved.length);
/*  994 */     offset += reserved.length;
/*  995 */     System.arraycopy(timestamp, 0, blob, offset, timestamp.length);
/*  996 */     offset += timestamp.length;
/*  997 */     System.arraycopy(clientChallenge, 0, blob, offset, 8);
/*  998 */     offset += 8;
/*  999 */     System.arraycopy(unknown1, 0, blob, offset, unknown1.length);
/* 1000 */     offset += unknown1.length;
/* 1001 */     System.arraycopy(targetInformation, 0, blob, offset, targetInformation.length);
/* 1002 */     offset += targetInformation.length;
/* 1003 */     System.arraycopy(unknown2, 0, blob, offset, unknown2.length);
/* 1004 */     offset += unknown2.length;
/* 1005 */     return blob;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Key createDESKey(byte[] bytes, int offset) {
/* 1021 */     byte[] keyBytes = new byte[7];
/* 1022 */     System.arraycopy(bytes, offset, keyBytes, 0, 7);
/* 1023 */     byte[] material = new byte[8];
/* 1024 */     material[0] = keyBytes[0];
/* 1025 */     material[1] = (byte)(keyBytes[0] << 7 | (keyBytes[1] & 0xFF) >>> 1);
/* 1026 */     material[2] = (byte)(keyBytes[1] << 6 | (keyBytes[2] & 0xFF) >>> 2);
/* 1027 */     material[3] = (byte)(keyBytes[2] << 5 | (keyBytes[3] & 0xFF) >>> 3);
/* 1028 */     material[4] = (byte)(keyBytes[3] << 4 | (keyBytes[4] & 0xFF) >>> 4);
/* 1029 */     material[5] = (byte)(keyBytes[4] << 3 | (keyBytes[5] & 0xFF) >>> 5);
/* 1030 */     material[6] = (byte)(keyBytes[5] << 2 | (keyBytes[6] & 0xFF) >>> 6);
/* 1031 */     material[7] = (byte)(keyBytes[6] << 1);
/* 1032 */     oddParity(material);
/* 1033 */     return new SecretKeySpec(material, "DES");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void oddParity(byte[] bytes) {
/* 1043 */     for (int i = 0; i < bytes.length; i++) {
/* 1044 */       byte b = bytes[i];
/* 1045 */       boolean needsParity = (((b >>> 7 ^ b >>> 6 ^ b >>> 5 ^ b >>> 4 ^ b >>> 3 ^ b >>> 2 ^ b >>> 1) & 0x1) == 0);
/*      */       
/* 1047 */       if (needsParity) {
/* 1048 */         bytes[i] = (byte)(bytes[i] | 0x1);
/*      */       } else {
/* 1050 */         bytes[i] = (byte)(bytes[i] & 0xFFFFFFFE);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Charset getCharset(int flags) throws NTLMEngineException {
/* 1062 */     if ((flags & 0x1) == 0) {
/* 1063 */       return DEFAULT_CHARSET;
/*      */     }
/* 1065 */     return UNICODE_LITTLE_UNMARKED;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class NTLMMessage
/*      */   {
/*      */     byte[] messageContents;
/*      */ 
/*      */     
/*      */     int currentOutputPosition;
/*      */ 
/*      */     
/*      */     NTLMMessage() {}
/*      */ 
/*      */     
/*      */     NTLMMessage(String messageBody, int expectedType) throws NTLMEngineException {
/* 1082 */       this(Base64.decodeBase64(messageBody.getBytes(NTLMEngineImpl.DEFAULT_CHARSET)), expectedType);
/*      */     }
/*      */ 
/*      */     
/*      */     NTLMMessage(byte[] message, int expectedType) throws NTLMEngineException {
/* 1087 */       this.messageContents = message;
/*      */       
/* 1089 */       if (this.messageContents.length < NTLMEngineImpl.SIGNATURE.length) {
/* 1090 */         throw new NTLMEngineException("NTLM message decoding error - packet too short");
/*      */       }
/* 1092 */       int i = 0;
/* 1093 */       while (i < NTLMEngineImpl.SIGNATURE.length) {
/* 1094 */         if (this.messageContents[i] != NTLMEngineImpl.SIGNATURE[i]) {
/* 1095 */           throw new NTLMEngineException("NTLM message expected - instead got unrecognized bytes");
/*      */         }
/*      */         
/* 1098 */         i++;
/*      */       } 
/*      */ 
/*      */       
/* 1102 */       int type = readULong(NTLMEngineImpl.SIGNATURE.length);
/* 1103 */       if (type != expectedType) {
/* 1104 */         throw new NTLMEngineException("NTLM type " + expectedType + " message expected - instead got type " + type);
/*      */       }
/*      */ 
/*      */       
/* 1108 */       this.currentOutputPosition = this.messageContents.length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int getPreambleLength() {
/* 1116 */       return NTLMEngineImpl.SIGNATURE.length + 4;
/*      */     }
/*      */ 
/*      */     
/*      */     int getMessageLength() {
/* 1121 */       return this.currentOutputPosition;
/*      */     }
/*      */ 
/*      */     
/*      */     byte readByte(int position) throws NTLMEngineException {
/* 1126 */       if (this.messageContents.length < position + 1) {
/* 1127 */         throw new NTLMEngineException("NTLM: Message too short");
/*      */       }
/* 1129 */       return this.messageContents[position];
/*      */     }
/*      */ 
/*      */     
/*      */     void readBytes(byte[] buffer, int position) throws NTLMEngineException {
/* 1134 */       if (this.messageContents.length < position + buffer.length) {
/* 1135 */         throw new NTLMEngineException("NTLM: Message too short");
/*      */       }
/* 1137 */       System.arraycopy(this.messageContents, position, buffer, 0, buffer.length);
/*      */     }
/*      */ 
/*      */     
/*      */     int readUShort(int position) {
/* 1142 */       return NTLMEngineImpl.readUShort(this.messageContents, position);
/*      */     }
/*      */ 
/*      */     
/*      */     int readULong(int position) {
/* 1147 */       return NTLMEngineImpl.readULong(this.messageContents, position);
/*      */     }
/*      */ 
/*      */     
/*      */     byte[] readSecurityBuffer(int position) {
/* 1152 */       return NTLMEngineImpl.readSecurityBuffer(this.messageContents, position);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void prepareResponse(int maxlength, int messageType) {
/* 1164 */       this.messageContents = new byte[maxlength];
/* 1165 */       this.currentOutputPosition = 0;
/* 1166 */       addBytes(NTLMEngineImpl.SIGNATURE);
/* 1167 */       addULong(messageType);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void addByte(byte b) {
/* 1177 */       this.messageContents[this.currentOutputPosition] = b;
/* 1178 */       this.currentOutputPosition++;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void addBytes(byte[] bytes) {
/* 1188 */       if (bytes == null) {
/*      */         return;
/*      */       }
/* 1191 */       for (byte b : bytes) {
/* 1192 */         this.messageContents[this.currentOutputPosition] = b;
/* 1193 */         this.currentOutputPosition++;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     void addUShort(int value) {
/* 1199 */       addByte((byte)(value & 0xFF));
/* 1200 */       addByte((byte)(value >> 8 & 0xFF));
/*      */     }
/*      */ 
/*      */     
/*      */     void addULong(int value) {
/* 1205 */       addByte((byte)(value & 0xFF));
/* 1206 */       addByte((byte)(value >> 8 & 0xFF));
/* 1207 */       addByte((byte)(value >> 16 & 0xFF));
/* 1208 */       addByte((byte)(value >> 24 & 0xFF));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getResponse() {
/* 1218 */       return new String(Base64.encodeBase64(getBytes()), StandardCharsets.US_ASCII);
/*      */     }
/*      */     
/*      */     public byte[] getBytes() {
/* 1222 */       if (this.messageContents == null) {
/* 1223 */         buildMessage();
/*      */       }
/* 1225 */       if (this.messageContents.length > this.currentOutputPosition) {
/* 1226 */         byte[] tmp = new byte[this.currentOutputPosition];
/* 1227 */         System.arraycopy(this.messageContents, 0, tmp, 0, this.currentOutputPosition);
/* 1228 */         this.messageContents = tmp;
/*      */       } 
/* 1230 */       return this.messageContents;
/*      */     }
/*      */     
/*      */     void buildMessage() {
/* 1234 */       throw new RuntimeException("Message builder not implemented for " + getClass().getName());
/*      */     }
/*      */   }
/*      */   
/*      */   static class Type1Message
/*      */     extends NTLMMessage
/*      */   {
/*      */     private final byte[] hostBytes;
/*      */     private final byte[] domainBytes;
/*      */     private final int flags;
/*      */     
/*      */     Type1Message(String domain, String host) {
/* 1246 */       this(domain, host, (Integer)null);
/*      */     }
/*      */ 
/*      */     
/*      */     Type1Message(String domain, String host, Integer flags) {
/* 1251 */       this.flags = (flags == null) ? getDefaultFlags() : flags.intValue();
/*      */ 
/*      */       
/* 1254 */       String unqualifiedHost = host;
/* 1255 */       String unqualifiedDomain = domain;
/*      */       
/* 1257 */       this
/* 1258 */         .hostBytes = (unqualifiedHost != null) ? unqualifiedHost.getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED) : null;
/* 1259 */       this
/* 1260 */         .domainBytes = (unqualifiedDomain != null) ? unqualifiedDomain.toUpperCase(Locale.ROOT).getBytes(NTLMEngineImpl.UNICODE_LITTLE_UNMARKED) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     Type1Message() {
/* 1265 */       this.hostBytes = null;
/* 1266 */       this.domainBytes = null;
/* 1267 */       this.flags = getDefaultFlags();
/*      */     }
/*      */     
/*      */     private int getDefaultFlags() {
/* 1271 */       return -1576500735;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void buildMessage() {
/* 1303 */       int domainBytesLength = 0;
/* 1304 */       if (this.domainBytes != null) {
/* 1305 */         domainBytesLength = this.domainBytes.length;
/*      */       }
/* 1307 */       int hostBytesLength = 0;
/* 1308 */       if (this.hostBytes != null) {
/* 1309 */         hostBytesLength = this.hostBytes.length;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1314 */       int finalLength = 40 + hostBytesLength + domainBytesLength;
/*      */ 
/*      */ 
/*      */       
/* 1318 */       prepareResponse(finalLength, 1);
/*      */ 
/*      */       
/* 1321 */       addULong(this.flags);
/*      */ 
/*      */       
/* 1324 */       addUShort(domainBytesLength);
/* 1325 */       addUShort(domainBytesLength);
/*      */ 
/*      */       
/* 1328 */       addULong(hostBytesLength + 32 + 8);
/*      */ 
/*      */       
/* 1331 */       addUShort(hostBytesLength);
/* 1332 */       addUShort(hostBytesLength);
/*      */ 
/*      */       
/* 1335 */       addULong(40);
/*      */ 
/*      */       
/* 1338 */       addUShort(261);
/*      */       
/* 1340 */       addULong(2600);
/*      */       
/* 1342 */       addUShort(3840);
/*      */ 
/*      */       
/* 1345 */       if (this.hostBytes != null) {
/* 1346 */         addBytes(this.hostBytes);
/*      */       }
/*      */       
/* 1349 */       if (this.domainBytes != null) {
/* 1350 */         addBytes(this.domainBytes);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   static class Type2Message
/*      */     extends NTLMMessage
/*      */   {
/*      */     final byte[] challenge;
/*      */     String target;
/*      */     byte[] targetInfo;
/*      */     final int flags;
/*      */     
/*      */     Type2Message(String messageBody) throws NTLMEngineException {
/* 1364 */       this(Base64.decodeBase64(messageBody.getBytes(NTLMEngineImpl.DEFAULT_CHARSET)));
/*      */     }
/*      */     
/*      */     Type2Message(byte[] message) throws NTLMEngineException {
/* 1368 */       super(message, 2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1385 */       this.challenge = new byte[8];
/* 1386 */       readBytes(this.challenge, 24);
/*      */       
/* 1388 */       this.flags = readULong(20);
/*      */ 
/*      */       
/* 1391 */       this.target = null;
/*      */ 
/*      */ 
/*      */       
/* 1395 */       if (getMessageLength() >= 20) {
/* 1396 */         byte[] bytes = readSecurityBuffer(12);
/* 1397 */         if (bytes.length != 0) {
/* 1398 */           this.target = new String(bytes, NTLMEngineImpl.getCharset(this.flags));
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1403 */       this.targetInfo = null;
/*      */       
/* 1405 */       if (getMessageLength() >= 48) {
/* 1406 */         byte[] bytes = readSecurityBuffer(40);
/* 1407 */         if (bytes.length != 0) {
/* 1408 */           this.targetInfo = bytes;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     byte[] getChallenge() {
/* 1415 */       return this.challenge;
/*      */     }
/*      */ 
/*      */     
/*      */     String getTarget() {
/* 1420 */       return this.target;
/*      */     }
/*      */ 
/*      */     
/*      */     byte[] getTargetInfo() {
/* 1425 */       return this.targetInfo;
/*      */     }
/*      */ 
/*      */     
/*      */     int getFlags() {
/* 1430 */       return this.flags;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class Type3Message
/*      */     extends NTLMMessage
/*      */   {
/*      */     final byte[] type1Message;
/*      */ 
/*      */     
/*      */     final byte[] type2Message;
/*      */ 
/*      */     
/*      */     final int type2Flags;
/*      */     
/*      */     final byte[] domainBytes;
/*      */     
/*      */     final byte[] hostBytes;
/*      */     
/*      */     final byte[] userBytes;
/*      */     
/*      */     byte[] lmResp;
/*      */     
/*      */     byte[] ntResp;
/*      */     
/*      */     final byte[] sessionKey;
/*      */     
/*      */     final byte[] exportedSessionKey;
/*      */     
/*      */     final boolean computeMic;
/*      */ 
/*      */     
/*      */     Type3Message(String domain, String host, String user, char[] password, byte[] nonce, int type2Flags, String target, byte[] targetInformation) throws NTLMEngineException {
/* 1465 */       this(domain, host, user, password, nonce, type2Flags, target, targetInformation, (Certificate)null, (byte[])null, (byte[])null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Type3Message(Random random, long currentTime, String domain, String host, String user, char[] password, byte[] nonce, int type2Flags, String target, byte[] targetInformation) throws NTLMEngineException {
/* 1480 */       this(random, currentTime, domain, host, user, password, nonce, type2Flags, target, targetInformation, (Certificate)null, (byte[])null, (byte[])null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Type3Message(String domain, String host, String user, char[] password, byte[] nonce, int type2Flags, String target, byte[] targetInformation, Certificate peerServerCertificate, byte[] type1Message, byte[] type2Message) throws NTLMEngineException {
/* 1496 */       this(NTLMEngineImpl.RND_GEN, System.currentTimeMillis(), domain, host, user, password, nonce, type2Flags, target, targetInformation, peerServerCertificate, type1Message, type2Message);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Type3Message(Random random, long currentTime, String domain, String host, String user, char[] password, byte[] nonce, int type2Flags, String target, byte[] targetInformation, Certificate peerServerCertificate, byte[] type1Message, byte[] type2Message) throws NTLMEngineException {
/*      */       byte[] userSessionKey;
/* 1514 */       if (random == null) {
/* 1515 */         throw new NTLMEngineException("Random generator not available");
/*      */       }
/*      */ 
/*      */       
/* 1519 */       this.type2Flags = type2Flags;
/* 1520 */       this.type1Message = type1Message;
/* 1521 */       this.type2Message = type2Message;
/*      */ 
/*      */       
/* 1524 */       String unqualifiedHost = host;
/*      */       
/* 1526 */       String unqualifiedDomain = domain;
/*      */       
/* 1528 */       byte[] responseTargetInformation = targetInformation;
/* 1529 */       if (peerServerCertificate != null) {
/* 1530 */         responseTargetInformation = addGssMicAvsToTargetInfo(targetInformation, peerServerCertificate);
/* 1531 */         this.computeMic = true;
/*      */       } else {
/* 1533 */         this.computeMic = false;
/*      */       } 
/*      */ 
/*      */       
/* 1537 */       NTLMEngineImpl.CipherGen gen = new NTLMEngineImpl.CipherGen(random, currentTime, unqualifiedDomain, user, password, nonce, target, responseTargetInformation);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1551 */         if ((type2Flags & 0x800000) != 0 && targetInformation != null && target != null) {
/*      */ 
/*      */           
/* 1554 */           this.ntResp = gen.getNTLMv2Response();
/* 1555 */           this.lmResp = gen.getLMv2Response();
/* 1556 */           if ((type2Flags & 0x80) != 0) {
/* 1557 */             userSessionKey = gen.getLanManagerSessionKey();
/*      */           } else {
/* 1559 */             userSessionKey = gen.getNTLMv2UserSessionKey();
/*      */           }
/*      */         
/*      */         }
/* 1563 */         else if ((type2Flags & 0x80000) != 0) {
/*      */           
/* 1565 */           this.ntResp = gen.getNTLM2SessionResponse();
/* 1566 */           this.lmResp = gen.getLM2SessionResponse();
/* 1567 */           if ((type2Flags & 0x80) != 0) {
/* 1568 */             userSessionKey = gen.getLanManagerSessionKey();
/*      */           } else {
/* 1570 */             userSessionKey = gen.getNTLM2SessionResponseUserSessionKey();
/*      */           } 
/*      */         } else {
/* 1573 */           this.ntResp = gen.getNTLMResponse();
/* 1574 */           this.lmResp = gen.getLMResponse();
/* 1575 */           if ((type2Flags & 0x80) != 0) {
/* 1576 */             userSessionKey = gen.getLanManagerSessionKey();
/*      */           } else {
/* 1578 */             userSessionKey = gen.getNTLMUserSessionKey();
/*      */           }
/*      */         
/*      */         } 
/* 1582 */       } catch (NTLMEngineException e) {
/*      */ 
/*      */         
/* 1585 */         this.ntResp = new byte[0];
/* 1586 */         this.lmResp = gen.getLMResponse();
/* 1587 */         if ((type2Flags & 0x80) != 0) {
/* 1588 */           userSessionKey = gen.getLanManagerSessionKey();
/*      */         } else {
/* 1590 */           userSessionKey = gen.getLMUserSessionKey();
/*      */         } 
/*      */       } 
/*      */       
/* 1594 */       if ((type2Flags & 0x10) != 0) {
/* 1595 */         if ((type2Flags & 0x40000000) != 0) {
/* 1596 */           this.exportedSessionKey = gen.getSecondaryKey();
/* 1597 */           this.sessionKey = NTLMEngineImpl.RC4(this.exportedSessionKey, userSessionKey);
/*      */         } else {
/* 1599 */           this.sessionKey = userSessionKey;
/* 1600 */           this.exportedSessionKey = this.sessionKey;
/*      */         } 
/*      */       } else {
/* 1603 */         if (this.computeMic) {
/* 1604 */           throw new NTLMEngineException("Cannot sign/seal: no exported session key");
/*      */         }
/* 1606 */         this.sessionKey = null;
/* 1607 */         this.exportedSessionKey = null;
/*      */       } 
/* 1609 */       Charset charset = NTLMEngineImpl.getCharset(type2Flags);
/* 1610 */       this.hostBytes = (unqualifiedHost != null) ? unqualifiedHost.getBytes(charset) : null;
/* 1611 */       this
/* 1612 */         .domainBytes = (unqualifiedDomain != null) ? unqualifiedDomain.toUpperCase(Locale.ROOT).getBytes(charset) : null;
/* 1613 */       this.userBytes = user.getBytes(charset);
/*      */     }
/*      */     
/*      */     public byte[] getEncryptedRandomSessionKey() {
/* 1617 */       return this.sessionKey;
/*      */     }
/*      */     
/*      */     public byte[] getExportedSessionKey() {
/* 1621 */       return this.exportedSessionKey;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     void buildMessage() {
/* 1627 */       int sessionKeyLen, ntRespLen = this.ntResp.length;
/* 1628 */       int lmRespLen = this.lmResp.length;
/*      */       
/* 1630 */       int domainLen = (this.domainBytes != null) ? this.domainBytes.length : 0;
/* 1631 */       int hostLen = (this.hostBytes != null) ? this.hostBytes.length : 0;
/* 1632 */       int userLen = this.userBytes.length;
/*      */       
/* 1634 */       if (this.sessionKey != null) {
/* 1635 */         sessionKeyLen = this.sessionKey.length;
/*      */       } else {
/* 1637 */         sessionKeyLen = 0;
/*      */       } 
/*      */ 
/*      */       
/* 1641 */       int lmRespOffset = 72 + (this.computeMic ? 16 : 0);
/*      */       
/* 1643 */       int ntRespOffset = lmRespOffset + lmRespLen;
/* 1644 */       int domainOffset = ntRespOffset + ntRespLen;
/* 1645 */       int userOffset = domainOffset + domainLen;
/* 1646 */       int hostOffset = userOffset + userLen;
/* 1647 */       int sessionKeyOffset = hostOffset + hostLen;
/* 1648 */       int finalLength = sessionKeyOffset + sessionKeyLen;
/*      */ 
/*      */       
/* 1651 */       prepareResponse(finalLength, 3);
/*      */ 
/*      */       
/* 1654 */       addUShort(lmRespLen);
/* 1655 */       addUShort(lmRespLen);
/*      */ 
/*      */       
/* 1658 */       addULong(lmRespOffset);
/*      */ 
/*      */       
/* 1661 */       addUShort(ntRespLen);
/* 1662 */       addUShort(ntRespLen);
/*      */ 
/*      */       
/* 1665 */       addULong(ntRespOffset);
/*      */ 
/*      */       
/* 1668 */       addUShort(domainLen);
/* 1669 */       addUShort(domainLen);
/*      */ 
/*      */       
/* 1672 */       addULong(domainOffset);
/*      */ 
/*      */       
/* 1675 */       addUShort(userLen);
/* 1676 */       addUShort(userLen);
/*      */ 
/*      */       
/* 1679 */       addULong(userOffset);
/*      */ 
/*      */       
/* 1682 */       addUShort(hostLen);
/* 1683 */       addUShort(hostLen);
/*      */ 
/*      */       
/* 1686 */       addULong(hostOffset);
/*      */ 
/*      */       
/* 1689 */       addUShort(sessionKeyLen);
/* 1690 */       addUShort(sessionKeyLen);
/*      */ 
/*      */       
/* 1693 */       addULong(sessionKeyOffset);
/*      */ 
/*      */       
/* 1696 */       addULong(this.type2Flags);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1727 */       addUShort(261);
/*      */       
/* 1729 */       addULong(2600);
/*      */       
/* 1731 */       addUShort(3840);
/*      */       
/* 1733 */       int micPosition = -1;
/* 1734 */       if (this.computeMic) {
/* 1735 */         micPosition = this.currentOutputPosition;
/* 1736 */         this.currentOutputPosition += 16;
/*      */       } 
/*      */ 
/*      */       
/* 1740 */       addBytes(this.lmResp);
/* 1741 */       addBytes(this.ntResp);
/* 1742 */       addBytes(this.domainBytes);
/* 1743 */       addBytes(this.userBytes);
/* 1744 */       addBytes(this.hostBytes);
/* 1745 */       if (this.sessionKey != null) {
/* 1746 */         addBytes(this.sessionKey);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1751 */       if (this.computeMic) {
/*      */         
/* 1753 */         NTLMEngineImpl.HMACMD5 hmacMD5 = new NTLMEngineImpl.HMACMD5(this.exportedSessionKey);
/* 1754 */         hmacMD5.update(this.type1Message);
/* 1755 */         hmacMD5.update(this.type2Message);
/* 1756 */         hmacMD5.update(this.messageContents);
/* 1757 */         byte[] mic = hmacMD5.getOutput();
/* 1758 */         System.arraycopy(mic, 0, this.messageContents, micPosition, mic.length);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private byte[] addGssMicAvsToTargetInfo(byte[] originalTargetInfo, Certificate peerServerCertificate) throws NTLMEngineException {
/* 1769 */       byte[] channelBindingsHash, newTargetInfo = new byte[originalTargetInfo.length + 8 + 20];
/* 1770 */       int appendLength = originalTargetInfo.length - 4;
/* 1771 */       System.arraycopy(originalTargetInfo, 0, newTargetInfo, 0, appendLength);
/* 1772 */       NTLMEngineImpl.writeUShort(newTargetInfo, 6, appendLength);
/* 1773 */       NTLMEngineImpl.writeUShort(newTargetInfo, 4, appendLength + 2);
/* 1774 */       NTLMEngineImpl.writeULong(newTargetInfo, 2, appendLength + 4);
/* 1775 */       NTLMEngineImpl.writeUShort(newTargetInfo, 10, appendLength + 8);
/* 1776 */       NTLMEngineImpl.writeUShort(newTargetInfo, 16, appendLength + 10);
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1781 */         byte[] certBytes = peerServerCertificate.getEncoded();
/* 1782 */         MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
/* 1783 */         byte[] certHashBytes = sha256.digest(certBytes);
/* 1784 */         byte[] channelBindingStruct = new byte[20 + NTLMEngineImpl.MAGIC_TLS_SERVER_ENDPOINT.length + certHashBytes.length];
/*      */         
/* 1786 */         NTLMEngineImpl.writeULong(channelBindingStruct, 53, 16);
/* 1787 */         System.arraycopy(NTLMEngineImpl.MAGIC_TLS_SERVER_ENDPOINT, 0, channelBindingStruct, 20, NTLMEngineImpl
/* 1788 */             .MAGIC_TLS_SERVER_ENDPOINT.length);
/* 1789 */         System.arraycopy(certHashBytes, 0, channelBindingStruct, 20 + NTLMEngineImpl.MAGIC_TLS_SERVER_ENDPOINT.length, certHashBytes.length);
/*      */         
/* 1791 */         MessageDigest md5 = NTLMEngineImpl.getMD5();
/* 1792 */         channelBindingsHash = md5.digest(channelBindingStruct);
/*      */       }
/* 1794 */       catch (CertificateEncodingException|NoSuchAlgorithmException e) {
/*      */         
/* 1796 */         throw new NTLMEngineException(e.getMessage(), e);
/*      */       } 
/*      */       
/* 1799 */       System.arraycopy(channelBindingsHash, 0, newTargetInfo, appendLength + 12, 16);
/* 1800 */       return newTargetInfo;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static void writeUShort(byte[] buffer, int value, int offset) {
/* 1806 */     buffer[offset] = (byte)(value & 0xFF);
/* 1807 */     buffer[offset + 1] = (byte)(value >> 8 & 0xFF);
/*      */   }
/*      */   
/*      */   static void writeULong(byte[] buffer, int value, int offset) {
/* 1811 */     buffer[offset] = (byte)(value & 0xFF);
/* 1812 */     buffer[offset + 1] = (byte)(value >> 8 & 0xFF);
/* 1813 */     buffer[offset + 2] = (byte)(value >> 16 & 0xFF);
/* 1814 */     buffer[offset + 3] = (byte)(value >> 24 & 0xFF);
/*      */   }
/*      */   
/*      */   static int F(int x, int y, int z) {
/* 1818 */     return x & y | (x ^ 0xFFFFFFFF) & z;
/*      */   }
/*      */   
/*      */   static int G(int x, int y, int z) {
/* 1822 */     return x & y | x & z | y & z;
/*      */   }
/*      */   
/*      */   static int H(int x, int y, int z) {
/* 1826 */     return x ^ y ^ z;
/*      */   }
/*      */   
/*      */   static int rotintlft(int val, int numbits) {
/* 1830 */     return val << numbits | val >>> 32 - numbits;
/*      */   }
/*      */   
/*      */   static MessageDigest getMD5() {
/*      */     try {
/* 1835 */       return MessageDigest.getInstance("MD5");
/* 1836 */     } catch (NoSuchAlgorithmException ex) {
/* 1837 */       throw new RuntimeException("MD5 message digest doesn't seem to exist - fatal error: " + ex.getMessage(), ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class MD4
/*      */   {
/* 1849 */     int A = 1732584193;
/* 1850 */     int B = -271733879;
/* 1851 */     int C = -1732584194;
/* 1852 */     int D = 271733878;
/*      */     long count;
/* 1854 */     final byte[] dataBuffer = new byte[64];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void update(byte[] input) {
/* 1863 */       int curBufferPos = (int)(this.count & 0x3FL);
/* 1864 */       int inputIndex = 0;
/* 1865 */       while (input.length - inputIndex + curBufferPos >= this.dataBuffer.length) {
/*      */ 
/*      */ 
/*      */         
/* 1869 */         int transferAmt = this.dataBuffer.length - curBufferPos;
/* 1870 */         System.arraycopy(input, inputIndex, this.dataBuffer, curBufferPos, transferAmt);
/* 1871 */         this.count += transferAmt;
/* 1872 */         curBufferPos = 0;
/* 1873 */         inputIndex += transferAmt;
/* 1874 */         processBuffer();
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1879 */       if (inputIndex < input.length) {
/* 1880 */         int transferAmt = input.length - inputIndex;
/* 1881 */         System.arraycopy(input, inputIndex, this.dataBuffer, curBufferPos, transferAmt);
/* 1882 */         this.count += transferAmt;
/* 1883 */         curBufferPos += transferAmt;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     byte[] getOutput() {
/* 1890 */       int bufferIndex = (int)(this.count & 0x3FL);
/* 1891 */       int padLen = (bufferIndex < 56) ? (56 - bufferIndex) : (120 - bufferIndex);
/* 1892 */       byte[] postBytes = new byte[padLen + 8];
/*      */ 
/*      */       
/* 1895 */       postBytes[0] = Byte.MIN_VALUE;
/*      */       
/* 1897 */       for (int i = 0; i < 8; i++) {
/* 1898 */         postBytes[padLen + i] = (byte)(int)(this.count * 8L >>> 8 * i);
/*      */       }
/*      */ 
/*      */       
/* 1902 */       update(postBytes);
/*      */ 
/*      */       
/* 1905 */       byte[] result = new byte[16];
/* 1906 */       NTLMEngineImpl.writeULong(result, this.A, 0);
/* 1907 */       NTLMEngineImpl.writeULong(result, this.B, 4);
/* 1908 */       NTLMEngineImpl.writeULong(result, this.C, 8);
/* 1909 */       NTLMEngineImpl.writeULong(result, this.D, 12);
/* 1910 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     void processBuffer() {
/* 1915 */       int[] d = new int[16];
/*      */       
/* 1917 */       for (int i = 0; i < 16; i++) {
/* 1918 */         d[i] = (this.dataBuffer[i * 4] & 0xFF) + ((this.dataBuffer[i * 4 + 1] & 0xFF) << 8) + ((this.dataBuffer[i * 4 + 2] & 0xFF) << 16) + ((this.dataBuffer[i * 4 + 3] & 0xFF) << 24);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1924 */       int AA = this.A;
/* 1925 */       int BB = this.B;
/* 1926 */       int CC = this.C;
/* 1927 */       int DD = this.D;
/* 1928 */       round1(d);
/* 1929 */       round2(d);
/* 1930 */       round3(d);
/* 1931 */       this.A += AA;
/* 1932 */       this.B += BB;
/* 1933 */       this.C += CC;
/* 1934 */       this.D += DD;
/*      */     }
/*      */ 
/*      */     
/*      */     void round1(int[] d) {
/* 1939 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[0], 3);
/* 1940 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[1], 7);
/* 1941 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[2], 11);
/* 1942 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[3], 19);
/*      */       
/* 1944 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[4], 3);
/* 1945 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[5], 7);
/* 1946 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[6], 11);
/* 1947 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[7], 19);
/*      */       
/* 1949 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[8], 3);
/* 1950 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[9], 7);
/* 1951 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[10], 11);
/* 1952 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[11], 19);
/*      */       
/* 1954 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + d[12], 3);
/* 1955 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + d[13], 7);
/* 1956 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + d[14], 11);
/* 1957 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + d[15], 19);
/*      */     }
/*      */     
/*      */     void round2(int[] d) {
/* 1961 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[0] + 1518500249, 3);
/* 1962 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[4] + 1518500249, 5);
/* 1963 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[8] + 1518500249, 9);
/* 1964 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[12] + 1518500249, 13);
/*      */       
/* 1966 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[1] + 1518500249, 3);
/* 1967 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[5] + 1518500249, 5);
/* 1968 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[9] + 1518500249, 9);
/* 1969 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[13] + 1518500249, 13);
/*      */       
/* 1971 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[2] + 1518500249, 3);
/* 1972 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[6] + 1518500249, 5);
/* 1973 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[10] + 1518500249, 9);
/* 1974 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[14] + 1518500249, 13);
/*      */       
/* 1976 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + d[3] + 1518500249, 3);
/* 1977 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + d[7] + 1518500249, 5);
/* 1978 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + d[11] + 1518500249, 9);
/* 1979 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + d[15] + 1518500249, 13);
/*      */     }
/*      */ 
/*      */     
/*      */     void round3(int[] d) {
/* 1984 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[0] + 1859775393, 3);
/* 1985 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[8] + 1859775393, 9);
/* 1986 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[4] + 1859775393, 11);
/* 1987 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[12] + 1859775393, 15);
/*      */       
/* 1989 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[2] + 1859775393, 3);
/* 1990 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[10] + 1859775393, 9);
/* 1991 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[6] + 1859775393, 11);
/* 1992 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[14] + 1859775393, 15);
/*      */       
/* 1994 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[1] + 1859775393, 3);
/* 1995 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[9] + 1859775393, 9);
/* 1996 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[5] + 1859775393, 11);
/* 1997 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[13] + 1859775393, 15);
/*      */       
/* 1999 */       this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + d[3] + 1859775393, 3);
/* 2000 */       this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + d[11] + 1859775393, 9);
/* 2001 */       this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + d[7] + 1859775393, 11);
/* 2002 */       this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + d[15] + 1859775393, 15);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class HMACMD5
/*      */   {
/*      */     final byte[] ipad;
/*      */     
/*      */     final byte[] opad;
/*      */     
/*      */     final MessageDigest md5;
/*      */ 
/*      */     
/*      */     HMACMD5(byte[] input) {
/* 2018 */       byte[] key = input;
/* 2019 */       this.md5 = NTLMEngineImpl.getMD5();
/*      */ 
/*      */       
/* 2022 */       this.ipad = new byte[64];
/* 2023 */       this.opad = new byte[64];
/*      */       
/* 2025 */       int keyLength = key.length;
/* 2026 */       if (keyLength > 64) {
/*      */         
/* 2028 */         this.md5.update(key);
/* 2029 */         key = this.md5.digest();
/* 2030 */         keyLength = key.length;
/*      */       } 
/* 2032 */       int i = 0;
/* 2033 */       while (i < keyLength) {
/* 2034 */         this.ipad[i] = (byte)(key[i] ^ 0x36);
/* 2035 */         this.opad[i] = (byte)(key[i] ^ 0x5C);
/* 2036 */         i++;
/*      */       } 
/* 2038 */       while (i < 64) {
/* 2039 */         this.ipad[i] = 54;
/* 2040 */         this.opad[i] = 92;
/* 2041 */         i++;
/*      */       } 
/*      */ 
/*      */       
/* 2045 */       this.md5.reset();
/* 2046 */       this.md5.update(this.ipad);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     byte[] getOutput() {
/* 2052 */       byte[] digest = this.md5.digest();
/* 2053 */       this.md5.update(this.opad);
/* 2054 */       return this.md5.digest(digest);
/*      */     }
/*      */ 
/*      */     
/*      */     void update(byte[] input) {
/* 2059 */       this.md5.update(input);
/*      */     }
/*      */ 
/*      */     
/*      */     void update(byte[] input, int offset, int length) {
/* 2064 */       this.md5.update(input, offset, length);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String generateType1Msg(String domain, String workstation) throws NTLMEngineException {
/* 2073 */     return getType1Message(workstation, domain);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String generateType3Msg(String username, char[] password, String domain, String workstation, String challenge) throws NTLMEngineException {
/* 2083 */     Type2Message t2m = new Type2Message(challenge);
/* 2084 */     return getType3Message(username, password, workstation, domain, t2m
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2089 */         .getChallenge(), t2m
/* 2090 */         .getFlags(), t2m
/* 2091 */         .getTarget(), t2m
/* 2092 */         .getTargetInfo());
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/NTLMEngineImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */