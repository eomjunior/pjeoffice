/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.pdf.PRStream;
/*     */ import com.itextpdf.text.pdf.PdfDictionary;
/*     */ import com.itextpdf.text.pdf.PdfIndirectReference;
/*     */ import com.itextpdf.text.pdf.PdfObject;
/*     */ import com.itextpdf.text.pdf.PdfReader;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ImageRenderInfo
/*     */ {
/*     */   private final GraphicsState gs;
/*     */   private final PdfIndirectReference ref;
/*     */   private final InlineImageInfo inlineImageInfo;
/*     */   private final PdfDictionary colorSpaceDictionary;
/*  70 */   private PdfImageObject imageObject = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Collection<MarkedContentInfo> markedContentInfos;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ImageRenderInfo(GraphicsState gs, PdfIndirectReference ref, PdfDictionary colorSpaceDictionary, Collection<MarkedContentInfo> markedContentInfo) {
/*  80 */     this.gs = gs;
/*  81 */     this.ref = ref;
/*  82 */     this.inlineImageInfo = null;
/*  83 */     this.colorSpaceDictionary = colorSpaceDictionary;
/*  84 */     this.markedContentInfos = new ArrayList<MarkedContentInfo>();
/*  85 */     if (markedContentInfo != null) {
/*  86 */       this.markedContentInfos.addAll(markedContentInfo);
/*     */     }
/*     */   }
/*     */   
/*     */   private ImageRenderInfo(GraphicsState gs, InlineImageInfo inlineImageInfo, PdfDictionary colorSpaceDictionary, Collection<MarkedContentInfo> markedContentInfo) {
/*  91 */     this.gs = gs;
/*  92 */     this.ref = null;
/*  93 */     this.inlineImageInfo = inlineImageInfo;
/*  94 */     this.colorSpaceDictionary = colorSpaceDictionary;
/*  95 */     this.markedContentInfos = new ArrayList<MarkedContentInfo>();
/*  96 */     if (markedContentInfo != null) {
/*  97 */       this.markedContentInfos.addAll(markedContentInfo);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ImageRenderInfo createForXObject(GraphicsState gs, PdfIndirectReference ref, PdfDictionary colorSpaceDictionary) {
/* 109 */     return new ImageRenderInfo(gs, ref, colorSpaceDictionary, null);
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
/*     */   public static ImageRenderInfo createForXObject(GraphicsState gs, PdfIndirectReference ref, PdfDictionary colorSpaceDictionary, Collection<MarkedContentInfo> markedContentInfo) {
/* 122 */     return new ImageRenderInfo(gs, ref, colorSpaceDictionary, markedContentInfo);
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
/*     */   protected static ImageRenderInfo createForEmbeddedImage(GraphicsState gs, InlineImageInfo inlineImageInfo, PdfDictionary colorSpaceDictionary, Collection<MarkedContentInfo> markedContentInfo) {
/* 135 */     ImageRenderInfo renderInfo = new ImageRenderInfo(gs, inlineImageInfo, colorSpaceDictionary, markedContentInfo);
/* 136 */     return renderInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfImageObject getImage() throws IOException {
/* 146 */     prepareImageObject();
/* 147 */     return this.imageObject;
/*     */   }
/*     */   
/*     */   private void prepareImageObject() throws IOException {
/* 151 */     if (this.imageObject != null) {
/*     */       return;
/*     */     }
/* 154 */     if (this.ref != null) {
/* 155 */       PRStream stream = (PRStream)PdfReader.getPdfObject((PdfObject)this.ref);
/* 156 */       this.imageObject = new PdfImageObject(stream, this.colorSpaceDictionary);
/* 157 */     } else if (this.inlineImageInfo != null) {
/* 158 */       this.imageObject = new PdfImageObject(this.inlineImageInfo.getImageDictionary(), this.inlineImageInfo.getSamples(), this.colorSpaceDictionary);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector getStartPoint() {
/* 166 */     return (new Vector(0.0F, 0.0F, 1.0F)).cross(this.gs.ctm);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Matrix getImageCTM() {
/* 174 */     return this.gs.ctm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getArea() {
/* 183 */     return this.gs.ctm.getDeterminant();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfIndirectReference getRef() {
/* 191 */     return this.ref;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getCurrentFillColor() {
/* 199 */     return this.gs.fillColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasMcid(int mcid) {
/* 210 */     return hasMcid(mcid, false);
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
/*     */   public boolean hasMcid(int mcid, boolean checkTheTopmostLevelOnly) {
/* 222 */     if (checkTheTopmostLevelOnly) {
/* 223 */       if (this.markedContentInfos instanceof ArrayList) {
/* 224 */         Integer infoMcid = getMcid();
/* 225 */         return (infoMcid != null) ? ((infoMcid.intValue() == mcid)) : false;
/*     */       } 
/*     */     } else {
/* 228 */       for (MarkedContentInfo info : this.markedContentInfos) {
/* 229 */         if (info.hasMcid() && 
/* 230 */           info.getMcid() == mcid)
/* 231 */           return true; 
/*     */       } 
/*     */     } 
/* 234 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getMcid() {
/* 242 */     if (this.markedContentInfos instanceof ArrayList) {
/* 243 */       ArrayList<MarkedContentInfo> mci = (ArrayList<MarkedContentInfo>)this.markedContentInfos;
/* 244 */       MarkedContentInfo info = (mci.size() > 0) ? mci.get(mci.size() - 1) : null;
/* 245 */       return (info != null && info.hasMcid()) ? Integer.valueOf(info.getMcid()) : null;
/*     */     } 
/* 247 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/ImageRenderInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */