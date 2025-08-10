/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import com.github.utils4j.IConstants;
/*    */ import org.apache.hc.core5.http.ContentType;
/*    */ 
/*    */ 
/*    */ public class ContentTypes
/*    */ {
/*  9 */   public static final ContentType TEXT_HTML = ContentType.create("text/html", IConstants.DEFAULT_CHARSET);
/* 10 */   public static final ContentType IMAGE_ICO = ContentType.create("image/x-icon");
/* 11 */   public static final ContentType TEXT_JS = ContentType.create("text/javascript", IConstants.DEFAULT_CHARSET);
/* 12 */   public static final ContentType TEXT_CSS = ContentType.create("text/css", IConstants.DEFAULT_CHARSET);
/*    */ 
/*    */ 
/*    */   
/*    */   public static String fromExtension(String fileName) {
/* 17 */     String name = Strings.trim(fileName).toLowerCase();
/* 18 */     if (name.endsWith(".html"))
/* 19 */       return TEXT_HTML.toString(); 
/* 20 */     if (name.endsWith(".xml"))
/* 21 */       return ContentType.APPLICATION_XML.toString(); 
/* 22 */     if (name.endsWith(".ico"))
/* 23 */       return IMAGE_ICO.toString(); 
/* 24 */     if (name.endsWith(".js"))
/* 25 */       return TEXT_JS.toString(); 
/* 26 */     if (name.endsWith(".css"))
/* 27 */       return TEXT_CSS.toString(); 
/* 28 */     if (name.endsWith(".json") || name.endsWith(".map"))
/* 29 */       return ContentType.APPLICATION_JSON.toString(); 
/* 30 */     if (name.endsWith(".pdf"))
/* 31 */       return ContentType.APPLICATION_PDF.toString(); 
/* 32 */     if (name.endsWith(".bmp"))
/* 33 */       return ContentType.IMAGE_BMP.toString(); 
/* 34 */     if (name.endsWith(".gif"))
/* 35 */       return ContentType.IMAGE_GIF.toString(); 
/* 36 */     if (name.endsWith(".jpeg"))
/* 37 */       return ContentType.IMAGE_JPEG.toString(); 
/* 38 */     if (name.endsWith(".png"))
/* 39 */       return ContentType.IMAGE_PNG.toString(); 
/* 40 */     if (name.endsWith(".svg"))
/* 41 */       return ContentType.IMAGE_SVG.toString(); 
/* 42 */     if (name.endsWith(".tiff")) {
/* 43 */       return ContentType.IMAGE_TIFF.toString();
/*    */     }
/* 45 */     return ContentType.APPLICATION_OCTET_STREAM.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/ContentTypes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */