/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.pdf.security.XpathConstructor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XfaXpathConstructor
/*     */   implements XpathConstructor
/*     */ {
/*     */   private final String CONFIG = "config";
/*     */   private final String CONNECTIONSET = "connectionSet";
/*     */   private final String DATASETS = "datasets";
/*     */   private final String LOCALESET = "localeSet";
/*     */   private final String PDF = "pdf";
/*     */   private final String SOURCESET = "sourceSet";
/*     */   private final String STYLESHEET = "stylesheet";
/*     */   private final String TEMPLATE = "template";
/*     */   private final String XDC = "xdc";
/*     */   private final String XFDF = "xfdf";
/*     */   private final String XMPMETA = "xmpmeta";
/*     */   private String xpathExpression;
/*     */   
/*     */   public enum XdpPackage
/*     */   {
/*  57 */     Config,
/*  58 */     ConnectionSet,
/*  59 */     Datasets,
/*  60 */     LocaleSet,
/*  61 */     Pdf,
/*  62 */     SourceSet,
/*  63 */     Stylesheet,
/*  64 */     Template,
/*  65 */     Xdc,
/*  66 */     Xfdf,
/*  67 */     Xmpmeta;
/*     */   }
/*     */   
/*  70 */   public XfaXpathConstructor() { this.CONFIG = "config";
/*  71 */     this.CONNECTIONSET = "connectionSet";
/*  72 */     this.DATASETS = "datasets";
/*  73 */     this.LOCALESET = "localeSet";
/*  74 */     this.PDF = "pdf";
/*  75 */     this.SOURCESET = "sourceSet";
/*  76 */     this.STYLESHEET = "stylesheet";
/*  77 */     this.TEMPLATE = "template";
/*  78 */     this.XDC = "xdc";
/*  79 */     this.XFDF = "xfdf";
/*  80 */     this.XMPMETA = "xmpmeta";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  86 */     this.xpathExpression = ""; } public XfaXpathConstructor(XdpPackage xdpPackage) { String strPackage; this.CONFIG = "config"; this.CONNECTIONSET = "connectionSet"; this.DATASETS = "datasets";
/*     */     this.LOCALESET = "localeSet";
/*     */     this.PDF = "pdf";
/*     */     this.SOURCESET = "sourceSet";
/*     */     this.STYLESHEET = "stylesheet";
/*     */     this.TEMPLATE = "template";
/*     */     this.XDC = "xdc";
/*     */     this.XFDF = "xfdf";
/*     */     this.XMPMETA = "xmpmeta";
/*  95 */     switch (xdpPackage) {
/*     */       case Config:
/*  97 */         strPackage = "config";
/*     */         break;
/*     */       case ConnectionSet:
/* 100 */         strPackage = "connectionSet";
/*     */         break;
/*     */       case Datasets:
/* 103 */         strPackage = "datasets";
/*     */         break;
/*     */       case LocaleSet:
/* 106 */         strPackage = "localeSet";
/*     */         break;
/*     */       case Pdf:
/* 109 */         strPackage = "pdf";
/*     */         break;
/*     */       case SourceSet:
/* 112 */         strPackage = "sourceSet";
/*     */         break;
/*     */       case Stylesheet:
/* 115 */         strPackage = "stylesheet";
/*     */         break;
/*     */       case Template:
/* 118 */         strPackage = "template";
/*     */         break;
/*     */       case Xdc:
/* 121 */         strPackage = "xdc";
/*     */         break;
/*     */       case Xfdf:
/* 124 */         strPackage = "xfdf";
/*     */         break;
/*     */       case Xmpmeta:
/* 127 */         strPackage = "xmpmeta";
/*     */         break;
/*     */       default:
/* 130 */         this.xpathExpression = "";
/*     */         return;
/*     */     } 
/*     */     
/* 134 */     StringBuilder builder = new StringBuilder("/xdp:xdp/*[local-name()='");
/* 135 */     builder.append(strPackage);
/* 136 */     builder.append("']");
/* 137 */     this.xpathExpression = builder.toString(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getXpathExpression() {
/* 146 */     return this.xpathExpression;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/XfaXpathConstructor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */