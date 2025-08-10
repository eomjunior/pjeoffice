/*     */ package com.itextpdf.xmp;
/*     */ 
/*     */ import com.itextpdf.xmp.impl.XMPMetaImpl;
/*     */ import com.itextpdf.xmp.impl.XMPMetaParser;
/*     */ import com.itextpdf.xmp.impl.XMPSchemaRegistryImpl;
/*     */ import com.itextpdf.xmp.impl.XMPSerializerHelper;
/*     */ import com.itextpdf.xmp.options.ParseOptions;
/*     */ import com.itextpdf.xmp.options.SerializeOptions;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class XMPMetaFactory
/*     */ {
/*  53 */   private static XMPSchemaRegistry schema = (XMPSchemaRegistry)new XMPSchemaRegistryImpl();
/*     */ 
/*     */ 
/*     */   
/*  57 */   private static XMPVersionInfo versionInfo = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMPSchemaRegistry getSchemaRegistry() {
/*  70 */     return schema;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMPMeta create() {
/*  77 */     return (XMPMeta)new XMPMetaImpl();
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
/*     */   public static XMPMeta parse(InputStream in) throws XMPException {
/*  89 */     return parse(in, null);
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
/*     */   public static XMPMeta parse(InputStream in, ParseOptions options) throws XMPException {
/* 115 */     return XMPMetaParser.parse(in, options);
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
/*     */   public static XMPMeta parseFromString(String packet) throws XMPException {
/* 127 */     return parseFromString(packet, null);
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
/*     */   public static XMPMeta parseFromString(String packet, ParseOptions options) throws XMPException {
/* 141 */     return XMPMetaParser.parse(packet, options);
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
/*     */   public static XMPMeta parseFromBuffer(byte[] buffer) throws XMPException {
/* 153 */     return parseFromBuffer(buffer, null);
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
/*     */   public static XMPMeta parseFromBuffer(byte[] buffer, ParseOptions options) throws XMPException {
/* 167 */     return XMPMetaParser.parse(buffer, options);
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
/*     */   public static void serialize(XMPMeta xmp, OutputStream out) throws XMPException {
/* 179 */     serialize(xmp, out, null);
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
/*     */   public static void serialize(XMPMeta xmp, OutputStream out, SerializeOptions options) throws XMPException {
/* 192 */     assertImplementation(xmp);
/* 193 */     XMPSerializerHelper.serialize((XMPMetaImpl)xmp, out, options);
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
/*     */   public static byte[] serializeToBuffer(XMPMeta xmp, SerializeOptions options) throws XMPException {
/* 206 */     assertImplementation(xmp);
/* 207 */     return XMPSerializerHelper.serializeToBuffer((XMPMetaImpl)xmp, options);
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
/*     */   public static String serializeToString(XMPMeta xmp, SerializeOptions options) throws XMPException {
/* 221 */     assertImplementation(xmp);
/* 222 */     return XMPSerializerHelper.serializeToString((XMPMetaImpl)xmp, options);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void assertImplementation(XMPMeta xmp) {
/* 229 */     if (!(xmp instanceof XMPMetaImpl)) {
/* 230 */       throw new UnsupportedOperationException("The serializing service works onlywith the XMPMeta implementation of this library");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reset() {
/* 241 */     schema = (XMPSchemaRegistry)new XMPSchemaRegistryImpl();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized XMPVersionInfo getVersionInfo() {
/* 251 */     if (versionInfo == null) {
/*     */       try {
/* 253 */         int major = 5;
/* 254 */         int minor = 1;
/* 255 */         int micro = 0;
/* 256 */         int engBuild = 3;
/* 257 */         boolean debug = false;
/*     */ 
/*     */         
/* 260 */         String message = "Adobe XMP Core 5.1.0-jc003";
/*     */ 
/*     */         
/* 263 */         versionInfo = new XMPVersionInfo() {
/*     */             public int getMajor() {
/* 265 */               return 5;
/*     */             }
/*     */             
/*     */             public int getMinor() {
/* 269 */               return 1;
/*     */             }
/*     */             
/*     */             public int getMicro() {
/* 273 */               return 0;
/*     */             }
/*     */             
/*     */             public boolean isDebug() {
/* 277 */               return false;
/*     */             }
/*     */             
/*     */             public int getBuild() {
/* 281 */               return 3;
/*     */             }
/*     */             
/*     */             public String getMessage() {
/* 285 */               return "Adobe XMP Core 5.1.0-jc003";
/*     */             }
/*     */             
/*     */             public String toString() {
/* 289 */               return "Adobe XMP Core 5.1.0-jc003";
/*     */             }
/*     */           };
/*     */       }
/* 293 */       catch (Throwable e) {
/*     */         
/* 295 */         System.out.println(e);
/*     */       } 
/*     */     }
/* 298 */     return versionInfo;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/XMPMetaFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */