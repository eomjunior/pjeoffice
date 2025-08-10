/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.DocumentException;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.PrivateKey;
/*     */ import java.util.HashMap;
/*     */ import org.bouncycastle.cms.CMSException;
/*     */ import org.bouncycastle.cms.Recipient;
/*     */ import org.bouncycastle.cms.RecipientInformation;
/*     */ import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
/*     */ import org.bouncycastle.cms.jcajce.JceKeyTransRecipient;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PdfEncryptor
/*     */ {
/*     */   public static void encrypt(PdfReader reader, OutputStream os, byte[] userPassword, byte[] ownerPassword, int permissions, boolean strength128Bits) throws DocumentException, IOException {
/*  83 */     PdfStamper stamper = new PdfStamper(reader, os);
/*  84 */     stamper.setEncryption(userPassword, ownerPassword, permissions, strength128Bits);
/*  85 */     stamper.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void encrypt(PdfReader reader, OutputStream os, byte[] userPassword, byte[] ownerPassword, int permissions, boolean strength128Bits, HashMap<String, String> newInfo) throws DocumentException, IOException {
/* 109 */     PdfStamper stamper = new PdfStamper(reader, os);
/* 110 */     stamper.setEncryption(userPassword, ownerPassword, permissions, strength128Bits);
/* 111 */     stamper.setMoreInfo(newInfo);
/* 112 */     stamper.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void encrypt(PdfReader reader, OutputStream os, boolean strength, String userPassword, String ownerPassword, int permissions) throws DocumentException, IOException {
/* 131 */     PdfStamper stamper = new PdfStamper(reader, os);
/* 132 */     stamper.setEncryption(strength, userPassword, ownerPassword, permissions);
/* 133 */     stamper.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void encrypt(PdfReader reader, OutputStream os, boolean strength, String userPassword, String ownerPassword, int permissions, HashMap<String, String> newInfo) throws DocumentException, IOException {
/* 157 */     PdfStamper stamper = new PdfStamper(reader, os);
/* 158 */     stamper.setEncryption(strength, userPassword, ownerPassword, permissions);
/* 159 */     stamper.setMoreInfo(newInfo);
/* 160 */     stamper.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void encrypt(PdfReader reader, OutputStream os, int type, String userPassword, String ownerPassword, int permissions, HashMap<String, String> newInfo) throws DocumentException, IOException {
/* 186 */     PdfStamper stamper = new PdfStamper(reader, os);
/* 187 */     stamper.setEncryption(type, userPassword, ownerPassword, permissions);
/* 188 */     stamper.setMoreInfo(newInfo);
/* 189 */     stamper.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void encrypt(PdfReader reader, OutputStream os, int type, String userPassword, String ownerPassword, int permissions) throws DocumentException, IOException {
/* 211 */     PdfStamper stamper = new PdfStamper(reader, os);
/* 212 */     stamper.setEncryption(type, userPassword, ownerPassword, permissions);
/* 213 */     stamper.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getPermissionsVerbose(int permissions) {
/* 222 */     StringBuffer buf = new StringBuffer("Allowed:");
/* 223 */     if ((0x804 & permissions) == 2052) buf.append(" Printing"); 
/* 224 */     if ((0x8 & permissions) == 8) buf.append(" Modify contents"); 
/* 225 */     if ((0x10 & permissions) == 16) buf.append(" Copy"); 
/* 226 */     if ((0x20 & permissions) == 32) buf.append(" Modify annotations"); 
/* 227 */     if ((0x100 & permissions) == 256) buf.append(" Fill in"); 
/* 228 */     if ((0x200 & permissions) == 512) buf.append(" Screen readers"); 
/* 229 */     if ((0x400 & permissions) == 1024) buf.append(" Assembly"); 
/* 230 */     if ((0x4 & permissions) == 4) buf.append(" Degraded printing"); 
/* 231 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isPrintingAllowed(int permissions) {
/* 242 */     return ((0x804 & permissions) == 2052);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isModifyContentsAllowed(int permissions) {
/* 253 */     return ((0x8 & permissions) == 8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isCopyAllowed(int permissions) {
/* 264 */     return ((0x10 & permissions) == 16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isModifyAnnotationsAllowed(int permissions) {
/* 275 */     return ((0x20 & permissions) == 32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFillInAllowed(int permissions) {
/* 286 */     return ((0x100 & permissions) == 256);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isScreenReadersAllowed(int permissions) {
/* 297 */     return ((0x200 & permissions) == 512);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAssemblyAllowed(int permissions) {
/* 308 */     return ((0x400 & permissions) == 1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isDegradedPrintingAllowed(int permissions) {
/* 319 */     return ((0x4 & permissions) == 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getContent(RecipientInformation recipientInfo, PrivateKey certificateKey, String certificateKeyProvider) throws CMSException {
/* 331 */     JceKeyTransRecipient jceKeyTransRecipient = (new JceKeyTransEnvelopedRecipient(certificateKey)).setProvider(certificateKeyProvider);
/* 332 */     return recipientInfo.getContent((Recipient)jceKeyTransRecipient);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfEncryptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */