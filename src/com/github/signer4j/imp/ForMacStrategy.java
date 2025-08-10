/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.utils4j.imp.Jvms;
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
/*    */ class ForMacStrategy
/*    */   extends UnixStrategy
/*    */ {
/*    */   public ForMacStrategy() {
/* 37 */     add("libbit4xpki.dylib");
/* 38 */     add("libbit4ipki.dylib");
/* 39 */     add("libbit4opki.dylib");
/* 40 */     add("libASEP11.dylib");
/* 41 */     add("libOcsCryptoki.dylib");
/* 42 */     add("libgclib.dylib");
/*    */     
/* 44 */     add("libwdpkcs.dylib");
/* 45 */     add("libsfntpkcs11.dylib");
/* 46 */     add("libeTPkcs11.dylib");
/* 47 */     add("libeTpkcs11.dylib");
/* 48 */     add("libeTPkcss11.dylib");
/* 49 */     add("libetpkcs11.dylib");
/* 50 */     add("libaetpkss.dylib");
/* 51 */     add("libbanrisulpkcs11.so");
/* 52 */     add("libdesktopID_Provider.dylib");
/* 53 */     add("opensc-pkcs11.so");
/* 54 */     add("libcastle.1.0.0.dylib");
/* 55 */     load("/usr/local/ngsrv/libepsng_p11.so.1.2.2");
/* 56 */     load("/Applications/NeoID Desktop.app/Contents/Java/tools/macos/libneoidp11.dylib");
/* 57 */     load("/Applications/WatchKey USB Token Admin Tool.app/Contents/MacOS/lib/libWDP11_BR_GOV.dylib");
/* 58 */     load("/Applications/tokenadmin.app/Contents/Frameworks/libaetpkss.dylib");
/* 59 */     load("/Library/Frameworks/eToken.framework/Versions/Current/libeToken.dylib");
/* 60 */     load("/Library/Frameworks/eToken.framework/Versions/A/libeToken.dylib");
/* 61 */     load("/Library/Frameworks/eToken.framework/Versions/4.55.41/libeToken.dylib");
/* 62 */     load("/Library/Application Support/CSSI/libcmP11.dylib");
/* 63 */     load("/usr/local/AWP/lib/libOcsCryptok.dylib");
/*    */ 
/*    */     
/* 66 */     if (Jvms.isArm()) {
/* 67 */       load("/Applications/Assistente Desktop birdID.app/Contents/Resources/extraResources/osx/arm/vault-pkcs11-arm.dylib");
/*    */     } else {
/* 69 */       load("/Applications/Assistente Desktop birdID.app/Contents/Resources/extraResources/osx/x64/vault-pkcs11.dylib");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/ForMacStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */