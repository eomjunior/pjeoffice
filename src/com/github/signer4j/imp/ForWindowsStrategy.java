/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.utils4j.imp.Jvms;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ForWindowsStrategy
/*     */   extends PreloadedStrategy
/*     */ {
/*     */   public ForWindowsStrategy() {
/*  36 */     String winRoot = Jvms.SYSTEM_ROOT;
/*     */     
/*  38 */     load(winRoot.concat("/System32/incryptoki2.dll"));
/*  39 */     load(winRoot.concat("/System32/bit4ipki.dll"));
/*  40 */     load(winRoot.concat("/System32/bit4opki.dll"));
/*  41 */     load(winRoot.concat("/System32/bit4xpki.dll"));
/*  42 */     load(winRoot.concat("/System32/SI_PKCS11.dll"));
/*  43 */     load(winRoot.concat("/System32/IpmPki32.dll"));
/*  44 */     load(winRoot.concat("/System32/IPMpkiLC.dll"));
/*  45 */     load(winRoot.concat("/System32/IpmPkiLU.dll"));
/*  46 */     load(winRoot.concat("/System32/bit4cpki.dll"));
/*  47 */     load(winRoot.concat("/System32/bit4tpki.dll"));
/*  48 */     load(winRoot.concat("/System32/CardOS_PKCS11.dll"));
/*  49 */     load(winRoot.concat("/System32/bit4p11.dll"));
/*  50 */     load(winRoot.concat("/System32/SSC_PKCS11.dll"));
/*  51 */     load(winRoot.concat("/System32/inp11lib.dll"));
/*  52 */     load(winRoot.concat("/System32/cvP11_M4.dll"));
/*  53 */     load(winRoot.concat("/System32/stPKCS11.dll"));
/*  54 */     load(winRoot.concat("/System32/SissP11.dll"));
/*  55 */     load(winRoot.concat("/System32/aepkcs11.dll"));
/*  56 */     load(winRoot.concat("/System32/eTokenPKCS11.dll"));
/*  57 */     load(winRoot.concat("/System32/swsc.dll"));
/*  58 */     load(winRoot.concat("/System32/aloaha_pkcs11.dll"));
/*  59 */     load(winRoot.concat("/System32/asignp11.dll"));
/*  60 */     load(winRoot.concat("/System32/cryst32.dll"));
/*  61 */     load(winRoot.concat("/System32/EP1PK111.dll"));
/*  62 */     load(winRoot.concat("/System32/ShuttleCsp11_3003.dll"));
/*  63 */     load(winRoot.concat("/System32/csspkcs11.dll"));
/*  64 */     load(winRoot.concat("/System32/ibmpkcss.dll"));
/*  65 */     load(winRoot.concat("/System32/nxpkcs11.dll"));
/*  66 */     load(winRoot.concat("/System32/micardoPKCS11.dll"));
/*  67 */     load(winRoot.concat("/System32/k1pk112.dll"));
/*  68 */     load(winRoot.concat("/System32/p11card.dll"));
/*  69 */     load(winRoot.concat("/System32/SpyPK11.dll"));
/*  70 */     load(winRoot.concat("/System32/acpkcs211.dll"));
/*  71 */     load(winRoot.concat("/System32/fort32.dll"));
/*  72 */     load(winRoot.concat("/System32/3gp11csp.dll"));
/*     */     
/*  74 */     load(winRoot.concat("/System32/ep2pk11.dll"));
/*  75 */     load(winRoot.concat("/System32/ngp11v211.dll"));
/*  76 */     load(winRoot.concat("/System32/aetpkss1.dll"));
/*  77 */     load(winRoot.concat("/System32/gclib.dll"));
/*  78 */     load(winRoot.concat("/System32/pk2priv.dll"));
/*  79 */     load(winRoot.concat("/System32/w32pk2ig.dll"));
/*  80 */     load(winRoot.concat("/System32/eTPkcs11.dll"));
/*  81 */     load(winRoot.concat("/System32/acospkcs11.dll"));
/*  82 */     load(winRoot.concat("/System32/dkck201.dll"));
/*  83 */     load(winRoot.concat("/System32/dkck232.dll"));
/*  84 */     load(winRoot.concat("/System32/cryptoki22.dll"));
/*  85 */     load(winRoot.concat("/System32/acpkcs.dll"));
/*  86 */     load(winRoot.concat("/System32/slbck.dll"));
/*  87 */     load(winRoot.concat("/System32/cmP11.dll"));
/*  88 */     load(winRoot.concat("/System32/WDPKCS.dll"));
/*  89 */     load(winRoot.concat("/System32/Watchdata/Watchdata Brazil CSP v1.0/WDPKCS.dll"));
/*  90 */     load(winRoot.concat("/System32/SerproPkcs11.dll"));
/*  91 */     load(winRoot.concat("/System32/OcsCryptoki.dll"));
/*  92 */     load(winRoot.concat("/System32/opensc-pkcs11.dll"));
/*  93 */     load(winRoot.concat("/System32/axaltocm.dll"));
/*  94 */     load(winRoot.concat("/System32/usbr38.dll"));
/*  95 */     load(winRoot.concat("/System32/cs2_pkcs11.dll"));
/*  96 */     load(winRoot.concat("/System32/CccSigIT.dll"));
/*  97 */     load(winRoot.concat("/System32/dspkcs.dll"));
/*  98 */     load(winRoot.concat("/System32/SetTokI.dll"));
/*  99 */     load(winRoot.concat("/System32/psepkcs11.dll"));
/* 100 */     load(winRoot.concat("/System32/id2cbox.dll"));
/* 101 */     load(winRoot.concat("/System32/smartp11.dll"));
/* 102 */     load(winRoot.concat("/System32/pkcs201n.dll"));
/* 103 */     load(winRoot.concat("/System32/cryptoki.dll"));
/* 104 */     load(winRoot.concat("/System32/AuCryptoki2-0.dll"));
/* 105 */     load(winRoot.concat("/System32/cknfast.dll"));
/* 106 */     load(winRoot.concat("/System32/cryst201.dll"));
/* 107 */     load(winRoot.concat("/System32/softokn3.dll"));
/* 108 */     load(winRoot.concat("/System32/iveacryptoki.dll"));
/* 109 */     load(winRoot.concat("/System32/sadaptor.dll"));
/* 110 */     load(winRoot.concat("/System32/pkcs11.dll"));
/* 111 */     load(winRoot.concat("/System32/siecap11.dll"));
/* 112 */     load(winRoot.concat("/System32/asepkcs.dll"));
/* 113 */     load(winRoot.concat("/System32/WDICP_P11_CCID_v34.dll"));
/* 114 */     load(winRoot.concat("/System32/desktopID_Provider.dll"));
/* 115 */     load(winRoot.concat("/System32/DXSafePKCS11.dll"));
/* 116 */     load(winRoot.concat("/System32/vault-pkcs11.dll"));
/* 117 */     load(winRoot.concat("/System32/eps2003csp11.dll"));
/*     */     
/* 119 */     load("C:/Program Files (x86)/Oberthur Technologies/AWP/DLLs/OcsCryptolib_P11.dll");
/* 120 */     load("C:/Program Files (x86)/Gemalto/IDGo 800 PKCS11/IDPrimePKCS11.dll");
/* 121 */     load("C:/Program Files (x86)/Oberthur Technologies/AWP/DLLs/OcsCryptoki.dll");
/* 122 */     load("C:/Program Files (x86)/CSP Banrisul Multiplo/PKCS11.dll");
/*     */     
/* 124 */     load("C:/Program Files/Gemplus/GemSafe Libraries/BIN/gclib.dll");
/* 125 */     load("C:/Program Files/Gemalto/IDGo 800 PKCS11/IDPrimePKCS1164.dll");
/* 126 */     load("C:/Program Files/Assistente Desktop birdID/resources/extraResources/windows/x64/vault-pkcs11.dll");
/* 127 */     load("C:/Program Files/Oberthur Technologies/AuthentIC Webpack/DLLs/OCSCryptoki.dll");
/* 128 */     load("C:/Program Files/Oberthur Technologies/AWP/DLLs/OcsCryptoki.dll");
/*     */     
/* 130 */     load("C:/Program Files/CSP Banrisul Multiplo/PKCS11.dll");
/*     */     
/* 132 */     load(winRoot.concat("/SysWOW64/opensc-pkcs11.dll"));
/* 133 */     load(winRoot.concat("/SysWOW64/WDICP_P11_CCID_v34.dll"));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/ForWindowsStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */