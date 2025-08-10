/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ForLinuxStrategy
/*    */   extends UnixStrategy
/*    */ {
/*    */   public ForLinuxStrategy() {
/* 36 */     add("libcoolkeypk11.so");
/* 37 */     add("libbit4opki.so");
/* 38 */     add("libbit4ipki.so");
/* 39 */     add("libbit4xpki.so");
/* 40 */     add("libASEP11.so");
/* 41 */     add("stPKCS11.so");
/* 42 */     add("libsiecap11.so");
/*    */     
/* 44 */     add("libaetpkss.so");
/* 45 */     add("libgpkcs11.so");
/* 46 */     add("libgpkcs11.so.2");
/* 47 */     add("libepsng_p11.so");
/* 48 */     add("libepsng_p11.so.1");
/* 49 */     add("libneoidp11.so");
/* 50 */     add("opensc/opensc­pkcs11.so");
/* 51 */     add("libeTPkcs11.so");
/* 52 */     add("libeTpkcs11.so");
/* 53 */     add("libeToken.so");
/* 54 */     add("libeToken.so.4");
/* 55 */     add("libcmP11.so");
/* 56 */     add("libwdpkcs.so");
/* 57 */     add("libaetpkss.so.3");
/* 58 */     add("libaetpkss.so.3.0");
/* 59 */     add("opensc-pkcs11.so");
/* 60 */     add("pkcs11/opensc-pkcs11.so");
/* 61 */     add("libscmccid.so");
/* 62 */     add("watchdata/ICP/lib/libwdpkcs_icp.so");
/* 63 */     add("watchdata/lib/libwdpkcs.so");
/* 64 */     add("x86_64-linux-gnu/opensc-pkcs11.so");
/* 65 */     add("x86_64-linux-gnu/pkcs11/opensc-pkcs11.so");
/*    */     
/* 67 */     load("/usr/local/ngsrv/libepsng_p11.so.1.2.2");
/* 68 */     load("/usr/local/ngsrv/libepsng_p11.so.1");
/* 69 */     load("/usr/local/AWP/lib/libOcsCryptoki.so");
/* 70 */     load("/usr/lib64/libeToken.so");
/* 71 */     load("/usr/local/lib64/libwdpkcs.so");
/* 72 */     load("/opt/watchdata/lib64/libwdpkcs.so");
/* 73 */     load("/opt/ePass2003-Castle-20141128/i386/redist/libcastle.so.1.0.0");
/* 74 */     load("/opt/Assistente Desktop birdID/resources/extraResources/linux/x64/vault-pkcs11.so");
/*    */ 
/*    */ 
/*    */     
/* 78 */     load("/usr/lib64/libeTPkcs11.so");
/* 79 */     load("/usr/lib64/libaetpkss.so.3.0.x");
/* 80 */     load("/usr/lib64/libaetpkss.so");
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 84 */     (new ForLinuxStrategy()).queriedPaths().forEach(s -> System.out.println("<tr>\n  <td>" + s + "</td>\n</tr>"));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/ForLinuxStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */