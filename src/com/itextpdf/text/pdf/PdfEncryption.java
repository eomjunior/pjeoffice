/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocWriter;
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import com.itextpdf.text.exceptions.BadPasswordException;
/*     */ import com.itextpdf.text.pdf.crypto.AESCipherCBCnoPad;
/*     */ import com.itextpdf.text.pdf.crypto.ARCFOUREncryption;
/*     */ import com.itextpdf.text.pdf.crypto.IVGenerator;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.cert.Certificate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfEncryption
/*     */ {
/*     */   public static final int STANDARD_ENCRYPTION_40 = 2;
/*     */   public static final int STANDARD_ENCRYPTION_128 = 3;
/*     */   public static final int AES_128 = 4;
/*     */   public static final int AES_256 = 5;
/*  74 */   private static final byte[] pad = new byte[] { 40, -65, 78, 94, 78, 117, -118, 65, 100, 0, 78, 86, -1, -6, 1, 8, 46, 46, 0, -74, -48, 104, 62, Byte.MIN_VALUE, 47, 12, -87, -2, 100, 83, 105, 122 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   private static final byte[] salt = new byte[] { 115, 65, 108, 84 };
/*     */ 
/*     */   
/*  85 */   private static final byte[] metadataPad = new byte[] { -1, -1, -1, -1 };
/*     */ 
/*     */ 
/*     */   
/*     */   byte[] key;
/*     */ 
/*     */   
/*     */   int keySize;
/*     */ 
/*     */   
/*  95 */   byte[] mkey = new byte[0];
/*     */ 
/*     */   
/*  98 */   byte[] ownerKey = new byte[32];
/*     */ 
/*     */   
/* 101 */   byte[] userKey = new byte[32];
/*     */ 
/*     */   
/*     */   byte[] oeKey;
/*     */ 
/*     */   
/*     */   byte[] ueKey;
/*     */   
/*     */   byte[] perms;
/*     */   
/*     */   long permissions;
/*     */   
/*     */   byte[] documentID;
/*     */   
/*     */   private int revision;
/*     */   
/*     */   private int keyLength;
/*     */   
/* 119 */   protected PdfPublicKeySecurityHandler publicKeyHandler = null;
/*     */ 
/*     */   
/* 122 */   byte[] extra = new byte[5];
/*     */ 
/*     */   
/*     */   MessageDigest md5;
/*     */   
/* 127 */   private ARCFOUREncryption arcfour = new ARCFOUREncryption();
/*     */   
/*     */   private boolean encryptMetadata;
/*     */   
/* 131 */   static long seq = System.currentTimeMillis();
/*     */   
/*     */   private boolean embeddedFilesOnly;
/*     */   
/*     */   private int cryptoMode;
/*     */   private static final int VALIDATION_SALT_OFFSET = 32;
/*     */   private static final int KEY_SALT_OFFSET = 40;
/*     */   private static final int SALT_LENGHT = 8;
/*     */   private static final int OU_LENGHT = 48;
/*     */   
/*     */   public PdfEncryption() {
/*     */     try {
/* 143 */       this.md5 = MessageDigest.getInstance("MD5");
/* 144 */     } catch (Exception e) {
/* 145 */       throw new ExceptionConverter(e);
/*     */     } 
/* 147 */     this.publicKeyHandler = new PdfPublicKeySecurityHandler();
/*     */   }
/*     */   
/*     */   public PdfEncryption(PdfEncryption enc) {
/* 151 */     this();
/* 152 */     if (enc.key != null)
/* 153 */       this.key = (byte[])enc.key.clone(); 
/* 154 */     this.keySize = enc.keySize;
/* 155 */     this.mkey = (byte[])enc.mkey.clone();
/* 156 */     this.ownerKey = (byte[])enc.ownerKey.clone();
/* 157 */     this.userKey = (byte[])enc.userKey.clone();
/* 158 */     this.permissions = enc.permissions;
/* 159 */     if (enc.documentID != null)
/* 160 */       this.documentID = (byte[])enc.documentID.clone(); 
/* 161 */     this.revision = enc.revision;
/* 162 */     this.keyLength = enc.keyLength;
/* 163 */     this.encryptMetadata = enc.encryptMetadata;
/* 164 */     this.embeddedFilesOnly = enc.embeddedFilesOnly;
/* 165 */     this.publicKeyHandler = enc.publicKeyHandler;
/*     */     
/* 167 */     if (enc.ueKey != null) {
/* 168 */       this.ueKey = (byte[])enc.ueKey.clone();
/*     */     }
/* 170 */     if (enc.oeKey != null) {
/* 171 */       this.oeKey = (byte[])enc.oeKey.clone();
/*     */     }
/* 173 */     if (enc.perms != null) {
/* 174 */       this.perms = (byte[])enc.perms.clone();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setCryptoMode(int mode, int kl) {
/* 179 */     this.cryptoMode = mode;
/* 180 */     this.encryptMetadata = ((mode & 0x8) != 8);
/* 181 */     this.embeddedFilesOnly = ((mode & 0x18) == 24);
/* 182 */     mode &= 0x7;
/* 183 */     switch (mode) {
/*     */       case 0:
/* 185 */         this.encryptMetadata = true;
/* 186 */         this.embeddedFilesOnly = false;
/* 187 */         this.keyLength = 40;
/* 188 */         this.revision = 2;
/*     */         return;
/*     */       case 1:
/* 191 */         this.embeddedFilesOnly = false;
/* 192 */         if (kl > 0) {
/* 193 */           this.keyLength = kl;
/*     */         } else {
/* 195 */           this.keyLength = 128;
/* 196 */         }  this.revision = 3;
/*     */         return;
/*     */       case 2:
/* 199 */         this.keyLength = 128;
/* 200 */         this.revision = 4;
/*     */         return;
/*     */       case 3:
/* 203 */         this.keyLength = 256;
/* 204 */         this.keySize = 32;
/* 205 */         this.revision = 5;
/*     */         return;
/*     */     } 
/* 208 */     throw new IllegalArgumentException(MessageLocalization.getComposedMessage("no.valid.encryption.mode", new Object[0]));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCryptoMode() {
/* 213 */     return this.cryptoMode;
/*     */   }
/*     */   
/*     */   public boolean isMetadataEncrypted() {
/* 217 */     return this.encryptMetadata;
/*     */   }
/*     */   
/*     */   public long getPermissions() {
/* 221 */     return this.permissions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmbeddedFilesOnly() {
/* 230 */     return this.embeddedFilesOnly;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] padPassword(byte[] userPassword) {
/* 236 */     byte[] userPad = new byte[32];
/* 237 */     if (userPassword == null) {
/* 238 */       System.arraycopy(pad, 0, userPad, 0, 32);
/*     */     } else {
/* 240 */       System.arraycopy(userPassword, 0, userPad, 0, Math.min(userPassword.length, 32));
/*     */       
/* 242 */       if (userPassword.length < 32) {
/* 243 */         System.arraycopy(pad, 0, userPad, userPassword.length, 32 - userPassword.length);
/*     */       }
/*     */     } 
/*     */     
/* 247 */     return userPad;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] computeOwnerKey(byte[] userPad, byte[] ownerPad) {
/* 253 */     byte[] ownerKey = new byte[32];
/* 254 */     byte[] digest = this.md5.digest(ownerPad);
/* 255 */     if (this.revision == 3 || this.revision == 4) {
/* 256 */       byte[] mkey = new byte[this.keyLength / 8];
/*     */       
/* 258 */       for (int k = 0; k < 50; k++) {
/* 259 */         this.md5.update(digest, 0, mkey.length);
/* 260 */         System.arraycopy(this.md5.digest(), 0, digest, 0, mkey.length);
/*     */       } 
/* 262 */       System.arraycopy(userPad, 0, ownerKey, 0, 32);
/* 263 */       for (int i = 0; i < 20; i++) {
/* 264 */         for (int j = 0; j < mkey.length; j++)
/* 265 */           mkey[j] = (byte)(digest[j] ^ i); 
/* 266 */         this.arcfour.prepareARCFOURKey(mkey);
/* 267 */         this.arcfour.encryptARCFOUR(ownerKey);
/*     */       } 
/*     */     } else {
/* 270 */       this.arcfour.prepareARCFOURKey(digest, 0, 5);
/* 271 */       this.arcfour.encryptARCFOUR(userPad, ownerKey);
/*     */     } 
/* 273 */     return ownerKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setupGlobalEncryptionKey(byte[] documentID, byte[] userPad, byte[] ownerKey, long permissions) {
/* 282 */     this.documentID = documentID;
/* 283 */     this.ownerKey = ownerKey;
/* 284 */     this.permissions = permissions;
/*     */     
/* 286 */     this.mkey = new byte[this.keyLength / 8];
/*     */ 
/*     */     
/* 289 */     this.md5.reset();
/* 290 */     this.md5.update(userPad);
/* 291 */     this.md5.update(ownerKey);
/*     */     
/* 293 */     byte[] ext = new byte[4];
/* 294 */     ext[0] = (byte)(int)permissions;
/* 295 */     ext[1] = (byte)(int)(permissions >> 8L);
/* 296 */     ext[2] = (byte)(int)(permissions >> 16L);
/* 297 */     ext[3] = (byte)(int)(permissions >> 24L);
/* 298 */     this.md5.update(ext, 0, 4);
/* 299 */     if (documentID != null)
/* 300 */       this.md5.update(documentID); 
/* 301 */     if (!this.encryptMetadata) {
/* 302 */       this.md5.update(metadataPad);
/*     */     }
/* 304 */     byte[] digest = new byte[this.mkey.length];
/* 305 */     System.arraycopy(this.md5.digest(), 0, digest, 0, this.mkey.length);
/*     */ 
/*     */     
/* 308 */     if (this.revision == 3 || this.revision == 4) {
/* 309 */       for (int k = 0; k < 50; k++) {
/* 310 */         System.arraycopy(this.md5.digest(digest), 0, digest, 0, this.mkey.length);
/*     */       }
/*     */     }
/* 313 */     System.arraycopy(digest, 0, this.mkey, 0, this.mkey.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setupUserKey() {
/* 322 */     if (this.revision == 3 || this.revision == 4) {
/* 323 */       this.md5.update(pad);
/* 324 */       byte[] digest = this.md5.digest(this.documentID);
/* 325 */       System.arraycopy(digest, 0, this.userKey, 0, 16);
/* 326 */       for (int k = 16; k < 32; k++)
/* 327 */         this.userKey[k] = 0; 
/* 328 */       for (int i = 0; i < 20; i++) {
/* 329 */         for (int j = 0; j < this.mkey.length; j++)
/* 330 */           digest[j] = (byte)(this.mkey[j] ^ i); 
/* 331 */         this.arcfour.prepareARCFOURKey(digest, 0, this.mkey.length);
/* 332 */         this.arcfour.encryptARCFOUR(this.userKey, 0, 16);
/*     */       } 
/*     */     } else {
/* 335 */       this.arcfour.prepareARCFOURKey(this.mkey);
/* 336 */       this.arcfour.encryptARCFOUR(pad, this.userKey);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setupAllKeys(byte[] userPassword, byte[] ownerPassword, int permissions) {
/* 344 */     if (ownerPassword == null || ownerPassword.length == 0)
/* 345 */       ownerPassword = this.md5.digest(createDocumentId()); 
/* 346 */     permissions |= (this.revision == 3 || this.revision == 4 || this.revision == 5) ? -3904 : -64;
/*     */     
/* 348 */     permissions &= 0xFFFFFFFC;
/* 349 */     this.permissions = permissions;
/* 350 */     if (this.revision == 5) {
/*     */       try {
/* 352 */         if (userPassword == null)
/* 353 */           userPassword = new byte[0]; 
/* 354 */         this.documentID = createDocumentId();
/* 355 */         byte[] uvs = IVGenerator.getIV(8);
/* 356 */         byte[] uks = IVGenerator.getIV(8);
/* 357 */         this.key = IVGenerator.getIV(32);
/*     */         
/* 359 */         MessageDigest md = MessageDigest.getInstance("SHA-256");
/* 360 */         md.update(userPassword, 0, Math.min(userPassword.length, 127));
/* 361 */         md.update(uvs);
/* 362 */         this.userKey = new byte[48];
/* 363 */         md.digest(this.userKey, 0, 32);
/* 364 */         System.arraycopy(uvs, 0, this.userKey, 32, 8);
/* 365 */         System.arraycopy(uks, 0, this.userKey, 40, 8);
/*     */         
/* 367 */         md.update(userPassword, 0, Math.min(userPassword.length, 127));
/* 368 */         md.update(uks);
/* 369 */         AESCipherCBCnoPad ac = new AESCipherCBCnoPad(true, md.digest());
/* 370 */         this.ueKey = ac.processBlock(this.key, 0, this.key.length);
/*     */         
/* 372 */         byte[] ovs = IVGenerator.getIV(8);
/* 373 */         byte[] oks = IVGenerator.getIV(8);
/* 374 */         md.update(ownerPassword, 0, Math.min(ownerPassword.length, 127));
/* 375 */         md.update(ovs);
/* 376 */         md.update(this.userKey);
/* 377 */         this.ownerKey = new byte[48];
/* 378 */         md.digest(this.ownerKey, 0, 32);
/* 379 */         System.arraycopy(ovs, 0, this.ownerKey, 32, 8);
/* 380 */         System.arraycopy(oks, 0, this.ownerKey, 40, 8);
/*     */         
/* 382 */         md.update(ownerPassword, 0, Math.min(ownerPassword.length, 127));
/* 383 */         md.update(oks);
/* 384 */         md.update(this.userKey);
/* 385 */         ac = new AESCipherCBCnoPad(true, md.digest());
/* 386 */         this.oeKey = ac.processBlock(this.key, 0, this.key.length);
/*     */         
/* 388 */         byte[] permsp = IVGenerator.getIV(16);
/* 389 */         permsp[0] = (byte)permissions;
/* 390 */         permsp[1] = (byte)(permissions >> 8);
/* 391 */         permsp[2] = (byte)(permissions >> 16);
/* 392 */         permsp[3] = (byte)(permissions >> 24);
/* 393 */         permsp[4] = -1;
/* 394 */         permsp[5] = -1;
/* 395 */         permsp[6] = -1;
/* 396 */         permsp[7] = -1;
/* 397 */         permsp[8] = this.encryptMetadata ? 84 : 70;
/* 398 */         permsp[9] = 97;
/* 399 */         permsp[10] = 100;
/* 400 */         permsp[11] = 98;
/* 401 */         ac = new AESCipherCBCnoPad(true, this.key);
/* 402 */         this.perms = ac.processBlock(permsp, 0, permsp.length);
/*     */       }
/* 404 */       catch (Exception ex) {
/* 405 */         throw new ExceptionConverter(ex);
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 411 */       byte[] userPad = padPassword(userPassword);
/* 412 */       byte[] ownerPad = padPassword(ownerPassword);
/*     */       
/* 414 */       this.ownerKey = computeOwnerKey(userPad, ownerPad);
/* 415 */       this.documentID = createDocumentId();
/* 416 */       setupByUserPad(this.documentID, userPad, this.ownerKey, permissions);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean readKey(PdfDictionary enc, byte[] password) throws BadPasswordException {
/*     */     try {
/* 427 */       if (password == null)
/* 428 */         password = new byte[0]; 
/* 429 */       byte[] oValue = DocWriter.getISOBytes(enc.get(PdfName.O).toString());
/* 430 */       byte[] uValue = DocWriter.getISOBytes(enc.get(PdfName.U).toString());
/* 431 */       byte[] oeValue = DocWriter.getISOBytes(enc.get(PdfName.OE).toString());
/* 432 */       byte[] ueValue = DocWriter.getISOBytes(enc.get(PdfName.UE).toString());
/* 433 */       byte[] perms = DocWriter.getISOBytes(enc.get(PdfName.PERMS).toString());
/* 434 */       PdfNumber pValue = (PdfNumber)enc.get(PdfName.P);
/*     */       
/* 436 */       this.oeKey = oeValue;
/* 437 */       this.ueKey = ueValue;
/* 438 */       this.perms = perms;
/*     */       
/* 440 */       this.ownerKey = oValue;
/* 441 */       this.userKey = uValue;
/*     */       
/* 443 */       this.permissions = pValue.longValue();
/*     */       
/* 445 */       boolean isUserPass = false;
/* 446 */       MessageDigest md = MessageDigest.getInstance("SHA-256");
/* 447 */       md.update(password, 0, Math.min(password.length, 127));
/* 448 */       md.update(oValue, 32, 8);
/* 449 */       md.update(uValue, 0, 48);
/* 450 */       byte[] hash = md.digest();
/* 451 */       boolean isOwnerPass = compareArray(hash, oValue, 32);
/* 452 */       if (isOwnerPass) {
/* 453 */         md.update(password, 0, Math.min(password.length, 127));
/* 454 */         md.update(oValue, 40, 8);
/* 455 */         md.update(uValue, 0, 48);
/* 456 */         hash = md.digest();
/* 457 */         AESCipherCBCnoPad aESCipherCBCnoPad = new AESCipherCBCnoPad(false, hash);
/* 458 */         this.key = aESCipherCBCnoPad.processBlock(oeValue, 0, oeValue.length);
/*     */       } else {
/*     */         
/* 461 */         md.update(password, 0, Math.min(password.length, 127));
/* 462 */         md.update(uValue, 32, 8);
/* 463 */         hash = md.digest();
/* 464 */         isUserPass = compareArray(hash, uValue, 32);
/* 465 */         if (!isUserPass)
/* 466 */           throw new BadPasswordException(MessageLocalization.getComposedMessage("bad.user.password", new Object[0])); 
/* 467 */         md.update(password, 0, Math.min(password.length, 127));
/* 468 */         md.update(uValue, 40, 8);
/* 469 */         hash = md.digest();
/* 470 */         AESCipherCBCnoPad aESCipherCBCnoPad = new AESCipherCBCnoPad(false, hash);
/* 471 */         this.key = aESCipherCBCnoPad.processBlock(ueValue, 0, ueValue.length);
/*     */       } 
/* 473 */       AESCipherCBCnoPad ac = new AESCipherCBCnoPad(false, this.key);
/* 474 */       byte[] decPerms = ac.processBlock(perms, 0, perms.length);
/* 475 */       if (decPerms[9] != 97 || decPerms[10] != 100 || decPerms[11] != 98)
/* 476 */         throw new BadPasswordException(MessageLocalization.getComposedMessage("bad.user.password", new Object[0])); 
/* 477 */       this.permissions = (decPerms[0] & 0xFF | (decPerms[1] & 0xFF) << 8 | (decPerms[2] & 0xFF) << 16 | (decPerms[2] & 0xFF) << 24);
/*     */       
/* 479 */       this.encryptMetadata = (decPerms[8] == 84);
/* 480 */       return isOwnerPass;
/*     */     }
/* 482 */     catch (BadPasswordException ex) {
/* 483 */       throw ex;
/*     */     }
/* 485 */     catch (Exception ex) {
/* 486 */       throw new ExceptionConverter(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean compareArray(byte[] a, byte[] b, int len) {
/* 491 */     for (int k = 0; k < len; k++) {
/* 492 */       if (a[k] != b[k]) {
/* 493 */         return false;
/*     */       }
/*     */     } 
/* 496 */     return true;
/*     */   }
/*     */   
/*     */   public static byte[] createDocumentId() {
/*     */     MessageDigest md5;
/*     */     try {
/* 502 */       md5 = MessageDigest.getInstance("MD5");
/* 503 */     } catch (Exception e) {
/* 504 */       throw new ExceptionConverter(e);
/*     */     } 
/* 506 */     long time = System.currentTimeMillis();
/* 507 */     long mem = Runtime.getRuntime().freeMemory();
/* 508 */     String s = time + "+" + mem + "+" + seq++;
/* 509 */     return md5.digest(s.getBytes());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setupByUserPassword(byte[] documentID, byte[] userPassword, byte[] ownerKey, long permissions) {
/* 516 */     setupByUserPad(documentID, padPassword(userPassword), ownerKey, permissions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setupByUserPad(byte[] documentID, byte[] userPad, byte[] ownerKey, long permissions) {
/* 524 */     setupGlobalEncryptionKey(documentID, userPad, ownerKey, permissions);
/* 525 */     setupUserKey();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setupByOwnerPassword(byte[] documentID, byte[] ownerPassword, byte[] userKey, byte[] ownerKey, long permissions) {
/* 532 */     setupByOwnerPad(documentID, padPassword(ownerPassword), userKey, ownerKey, permissions);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void setupByOwnerPad(byte[] documentID, byte[] ownerPad, byte[] userKey, byte[] ownerKey, long permissions) {
/* 538 */     byte[] userPad = computeOwnerKey(ownerKey, ownerPad);
/*     */ 
/*     */     
/* 541 */     setupGlobalEncryptionKey(documentID, userPad, ownerKey, permissions);
/*     */     
/* 543 */     setupUserKey();
/*     */   }
/*     */   
/*     */   public void setKey(byte[] key) {
/* 547 */     this.key = key;
/*     */   }
/*     */   
/*     */   public void setupByEncryptionKey(byte[] key, int keylength) {
/* 551 */     this.mkey = new byte[keylength / 8];
/* 552 */     System.arraycopy(key, 0, this.mkey, 0, this.mkey.length);
/*     */   }
/*     */   
/*     */   public void setHashKey(int number, int generation) {
/* 556 */     if (this.revision == 5)
/*     */       return; 
/* 558 */     this.md5.reset();
/* 559 */     this.extra[0] = (byte)number;
/* 560 */     this.extra[1] = (byte)(number >> 8);
/* 561 */     this.extra[2] = (byte)(number >> 16);
/* 562 */     this.extra[3] = (byte)generation;
/* 563 */     this.extra[4] = (byte)(generation >> 8);
/* 564 */     this.md5.update(this.mkey);
/* 565 */     this.md5.update(this.extra);
/* 566 */     if (this.revision == 4)
/* 567 */       this.md5.update(salt); 
/* 568 */     this.key = this.md5.digest();
/* 569 */     this.keySize = this.mkey.length + 5;
/* 570 */     if (this.keySize > 16)
/* 571 */       this.keySize = 16; 
/*     */   }
/*     */   
/*     */   public static PdfObject createInfoId(byte[] id, boolean modified) throws IOException {
/* 575 */     ByteBuffer buf = new ByteBuffer(90);
/* 576 */     if (id.length == 0)
/* 577 */       id = createDocumentId(); 
/* 578 */     buf.append('[').append('<'); int k;
/* 579 */     for (k = 0; k < id.length; k++)
/* 580 */       buf.appendHex(id[k]); 
/* 581 */     buf.append('>').append('<');
/* 582 */     if (modified)
/* 583 */       id = createDocumentId(); 
/* 584 */     for (k = 0; k < id.length; k++)
/* 585 */       buf.appendHex(id[k]); 
/* 586 */     buf.append('>').append(']');
/* 587 */     buf.close();
/* 588 */     return new PdfLiteral(buf.toByteArray());
/*     */   }
/*     */   
/*     */   public PdfDictionary getEncryptionDictionary() {
/* 592 */     PdfDictionary dic = new PdfDictionary();
/*     */     
/* 594 */     if (this.publicKeyHandler.getRecipientsSize() > 0)
/* 595 */     { PdfArray recipients = null;
/*     */       
/* 597 */       dic.put(PdfName.FILTER, PdfName.PUBSEC);
/* 598 */       dic.put(PdfName.R, new PdfNumber(this.revision));
/*     */       
/*     */       try {
/* 601 */         recipients = this.publicKeyHandler.getEncodedRecipients();
/* 602 */       } catch (Exception f) {
/* 603 */         throw new ExceptionConverter(f);
/*     */       } 
/*     */       
/* 606 */       if (this.revision == 2) {
/* 607 */         dic.put(PdfName.V, new PdfNumber(1));
/* 608 */         dic.put(PdfName.SUBFILTER, PdfName.ADBE_PKCS7_S4);
/* 609 */         dic.put(PdfName.RECIPIENTS, recipients);
/* 610 */       } else if (this.revision == 3 && this.encryptMetadata) {
/* 611 */         dic.put(PdfName.V, new PdfNumber(2));
/* 612 */         dic.put(PdfName.LENGTH, new PdfNumber(128));
/* 613 */         dic.put(PdfName.SUBFILTER, PdfName.ADBE_PKCS7_S4);
/* 614 */         dic.put(PdfName.RECIPIENTS, recipients);
/*     */       } else {
/* 616 */         if (this.revision == 5) {
/* 617 */           dic.put(PdfName.R, new PdfNumber(5));
/* 618 */           dic.put(PdfName.V, new PdfNumber(5));
/*     */         } else {
/*     */           
/* 621 */           dic.put(PdfName.R, new PdfNumber(4));
/* 622 */           dic.put(PdfName.V, new PdfNumber(4));
/*     */         } 
/* 624 */         dic.put(PdfName.SUBFILTER, PdfName.ADBE_PKCS7_S5);
/*     */         
/* 626 */         PdfDictionary stdcf = new PdfDictionary();
/* 627 */         stdcf.put(PdfName.RECIPIENTS, recipients);
/* 628 */         if (!this.encryptMetadata)
/* 629 */           stdcf.put(PdfName.ENCRYPTMETADATA, PdfBoolean.PDFFALSE); 
/* 630 */         if (this.revision == 4) {
/* 631 */           stdcf.put(PdfName.CFM, PdfName.AESV2);
/* 632 */           stdcf.put(PdfName.LENGTH, new PdfNumber(128));
/*     */         }
/* 634 */         else if (this.revision == 5) {
/* 635 */           stdcf.put(PdfName.CFM, PdfName.AESV3);
/* 636 */           stdcf.put(PdfName.LENGTH, new PdfNumber(256));
/*     */         } else {
/*     */           
/* 639 */           stdcf.put(PdfName.CFM, PdfName.V2);
/* 640 */         }  PdfDictionary cf = new PdfDictionary();
/* 641 */         cf.put(PdfName.DEFAULTCRYPTFILTER, stdcf);
/* 642 */         dic.put(PdfName.CF, cf);
/* 643 */         if (this.embeddedFilesOnly) {
/* 644 */           dic.put(PdfName.EFF, PdfName.DEFAULTCRYPTFILTER);
/* 645 */           dic.put(PdfName.STRF, PdfName.IDENTITY);
/* 646 */           dic.put(PdfName.STMF, PdfName.IDENTITY);
/*     */         } else {
/*     */           
/* 649 */           dic.put(PdfName.STRF, PdfName.DEFAULTCRYPTFILTER);
/* 650 */           dic.put(PdfName.STMF, PdfName.DEFAULTCRYPTFILTER);
/*     */         } 
/*     */       } 
/*     */       
/* 654 */       MessageDigest md = null;
/* 655 */       byte[] encodedRecipient = null;
/*     */       
/*     */       try {
/* 658 */         if (this.revision == 5) {
/* 659 */           md = MessageDigest.getInstance("SHA-256");
/*     */         } else {
/* 661 */           md = MessageDigest.getInstance("SHA-1");
/* 662 */         }  md.update(this.publicKeyHandler.getSeed());
/* 663 */         for (int i = 0; i < this.publicKeyHandler.getRecipientsSize(); i++) {
/* 664 */           encodedRecipient = this.publicKeyHandler.getEncodedRecipient(i);
/* 665 */           md.update(encodedRecipient);
/*     */         } 
/* 667 */         if (!this.encryptMetadata) {
/* 668 */           md.update(new byte[] { -1, -1, -1, -1 });
/*     */         }
/* 670 */       } catch (Exception f) {
/* 671 */         throw new ExceptionConverter(f);
/*     */       } 
/*     */       
/* 674 */       byte[] mdResult = md.digest();
/*     */       
/* 676 */       if (this.revision == 5) {
/* 677 */         this.key = mdResult;
/*     */       } else {
/* 679 */         setupByEncryptionKey(mdResult, this.keyLength);
/*     */       }  }
/* 681 */     else { dic.put(PdfName.FILTER, PdfName.STANDARD);
/* 682 */       dic.put(PdfName.O, new PdfLiteral(
/* 683 */             StringUtils.escapeString(this.ownerKey)));
/* 684 */       dic.put(PdfName.U, new PdfLiteral(
/* 685 */             StringUtils.escapeString(this.userKey)));
/* 686 */       dic.put(PdfName.P, new PdfNumber(this.permissions));
/* 687 */       dic.put(PdfName.R, new PdfNumber(this.revision));
/*     */       
/* 689 */       if (this.revision == 2) {
/* 690 */         dic.put(PdfName.V, new PdfNumber(1));
/* 691 */       } else if (this.revision == 3 && this.encryptMetadata) {
/* 692 */         dic.put(PdfName.V, new PdfNumber(2));
/* 693 */         dic.put(PdfName.LENGTH, new PdfNumber(128));
/*     */       
/*     */       }
/* 696 */       else if (this.revision == 5) {
/* 697 */         if (!this.encryptMetadata)
/* 698 */           dic.put(PdfName.ENCRYPTMETADATA, PdfBoolean.PDFFALSE); 
/* 699 */         dic.put(PdfName.OE, new PdfLiteral(
/* 700 */               StringUtils.escapeString(this.oeKey)));
/* 701 */         dic.put(PdfName.UE, new PdfLiteral(
/* 702 */               StringUtils.escapeString(this.ueKey)));
/* 703 */         dic.put(PdfName.PERMS, new PdfLiteral(
/* 704 */               StringUtils.escapeString(this.perms)));
/* 705 */         dic.put(PdfName.V, new PdfNumber(this.revision));
/* 706 */         dic.put(PdfName.LENGTH, new PdfNumber(256));
/* 707 */         PdfDictionary stdcf = new PdfDictionary();
/* 708 */         stdcf.put(PdfName.LENGTH, new PdfNumber(32));
/* 709 */         if (this.embeddedFilesOnly) {
/* 710 */           stdcf.put(PdfName.AUTHEVENT, PdfName.EFOPEN);
/* 711 */           dic.put(PdfName.EFF, PdfName.STDCF);
/* 712 */           dic.put(PdfName.STRF, PdfName.IDENTITY);
/* 713 */           dic.put(PdfName.STMF, PdfName.IDENTITY);
/*     */         } else {
/*     */           
/* 716 */           stdcf.put(PdfName.AUTHEVENT, PdfName.DOCOPEN);
/* 717 */           dic.put(PdfName.STRF, PdfName.STDCF);
/* 718 */           dic.put(PdfName.STMF, PdfName.STDCF);
/*     */         } 
/* 720 */         stdcf.put(PdfName.CFM, PdfName.AESV3);
/* 721 */         PdfDictionary cf = new PdfDictionary();
/* 722 */         cf.put(PdfName.STDCF, stdcf);
/* 723 */         dic.put(PdfName.CF, cf);
/*     */       } else {
/*     */         
/* 726 */         if (!this.encryptMetadata)
/* 727 */           dic.put(PdfName.ENCRYPTMETADATA, PdfBoolean.PDFFALSE); 
/* 728 */         dic.put(PdfName.R, new PdfNumber(4));
/* 729 */         dic.put(PdfName.V, new PdfNumber(4));
/* 730 */         dic.put(PdfName.LENGTH, new PdfNumber(128));
/* 731 */         PdfDictionary stdcf = new PdfDictionary();
/* 732 */         stdcf.put(PdfName.LENGTH, new PdfNumber(16));
/* 733 */         if (this.embeddedFilesOnly) {
/* 734 */           stdcf.put(PdfName.AUTHEVENT, PdfName.EFOPEN);
/* 735 */           dic.put(PdfName.EFF, PdfName.STDCF);
/* 736 */           dic.put(PdfName.STRF, PdfName.IDENTITY);
/* 737 */           dic.put(PdfName.STMF, PdfName.IDENTITY);
/*     */         } else {
/*     */           
/* 740 */           stdcf.put(PdfName.AUTHEVENT, PdfName.DOCOPEN);
/* 741 */           dic.put(PdfName.STRF, PdfName.STDCF);
/* 742 */           dic.put(PdfName.STMF, PdfName.STDCF);
/*     */         } 
/* 744 */         if (this.revision == 4) {
/* 745 */           stdcf.put(PdfName.CFM, PdfName.AESV2);
/*     */         } else {
/* 747 */           stdcf.put(PdfName.CFM, PdfName.V2);
/* 748 */         }  PdfDictionary cf = new PdfDictionary();
/* 749 */         cf.put(PdfName.STDCF, stdcf);
/* 750 */         dic.put(PdfName.CF, cf);
/*     */       }  }
/*     */ 
/*     */     
/* 754 */     return dic;
/*     */   }
/*     */   
/*     */   public PdfObject getFileID(boolean modified) throws IOException {
/* 758 */     return createInfoId(this.documentID, modified);
/*     */   }
/*     */   
/*     */   public OutputStreamEncryption getEncryptionStream(OutputStream os) {
/* 762 */     return new OutputStreamEncryption(os, this.key, 0, this.keySize, this.revision);
/*     */   }
/*     */   
/*     */   public int calculateStreamSize(int n) {
/* 766 */     if (this.revision == 4 || this.revision == 5) {
/* 767 */       return (n & 0x7FFFFFF0) + 32;
/*     */     }
/* 769 */     return n;
/*     */   }
/*     */   
/*     */   public byte[] encryptByteArray(byte[] b) {
/*     */     try {
/* 774 */       ByteArrayOutputStream ba = new ByteArrayOutputStream();
/* 775 */       OutputStreamEncryption os2 = getEncryptionStream(ba);
/* 776 */       os2.write(b);
/* 777 */       os2.finish();
/* 778 */       return ba.toByteArray();
/* 779 */     } catch (IOException ex) {
/* 780 */       throw new ExceptionConverter(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public StandardDecryption getDecryptor() {
/* 785 */     return new StandardDecryption(this.key, 0, this.keySize, this.revision);
/*     */   }
/*     */   
/*     */   public byte[] decryptByteArray(byte[] b) {
/*     */     try {
/* 790 */       ByteArrayOutputStream ba = new ByteArrayOutputStream();
/* 791 */       StandardDecryption dec = getDecryptor();
/* 792 */       byte[] b2 = dec.update(b, 0, b.length);
/* 793 */       if (b2 != null)
/* 794 */         ba.write(b2); 
/* 795 */       b2 = dec.finish();
/* 796 */       if (b2 != null)
/* 797 */         ba.write(b2); 
/* 798 */       return ba.toByteArray();
/* 799 */     } catch (IOException ex) {
/* 800 */       throw new ExceptionConverter(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addRecipient(Certificate cert, int permission) {
/* 805 */     this.documentID = createDocumentId();
/* 806 */     this.publicKeyHandler.addRecipient(new PdfPublicKeyRecipient(cert, permission));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] computeUserPassword(byte[] ownerPassword) {
/* 817 */     byte[] userPad = null;
/* 818 */     if (this.publicKeyHandler.getRecipientsSize() == 0 && 2 <= this.revision && this.revision <= 4) {
/*     */       
/* 820 */       userPad = computeOwnerKey(this.ownerKey, padPassword(ownerPassword));
/* 821 */       for (int i = 0; i < userPad.length; ) {
/* 822 */         boolean match = true;
/* 823 */         for (int j = 0; j < userPad.length - i; j++) {
/* 824 */           if (userPad[i + j] != pad[j]) {
/* 825 */             match = false;
/*     */             break;
/*     */           } 
/*     */         } 
/* 829 */         if (!match) { i++; continue; }
/* 830 */          byte[] userPassword = new byte[i];
/* 831 */         System.arraycopy(userPad, 0, userPassword, 0, i);
/* 832 */         return userPassword;
/*     */       } 
/*     */     } 
/* 835 */     return userPad;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfEncryption.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */