/*     */ package com.github.utils4j.gui.imp;
/*     */ 
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.io.File;
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import javax.swing.TransferHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MediaTransferHandler
/*     */   extends TransferHandler
/*     */ {
/*  46 */   private final DataFlavor uriListFlavor = newDataFlavor("text/uri-list;class=java.lang.String");
/*  47 */   private final DataFlavor javaUrlFlavor = newDataFlavor("application/x-java-url;class=java.net.URL");
/*  48 */   private final DataFlavor javaFileListFlavor = DataFlavor.javaFileListFlavor;
/*  49 */   private final DataFlavor stringFlavor = DataFlavor.stringFlavor;
/*     */ 
/*     */   
/*     */   private DataFlavor newDataFlavor(String mimeType) {
/*     */     try {
/*  54 */       return new DataFlavor(mimeType);
/*  55 */     } catch (ClassNotFoundException e) {
/*  56 */       throw new RuntimeException();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean canImport(TransferHandler.TransferSupport support) {
/*  62 */     return (getDataFlavor(support) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean importData(TransferHandler.TransferSupport support) {
/*  67 */     DataFlavor flavor = getDataFlavor(support);
/*  68 */     if (flavor != null) {
/*     */       try {
/*  70 */         Object transferData = support.getTransferable().getTransferData(flavor);
/*  71 */         if (transferData instanceof String) {
/*  72 */           String value = (String)transferData;
/*  73 */           String[] uris = value.split("\\r\\n");
/*  74 */           if (uris.length > 0) {
/*  75 */             onMediaDropped(uris);
/*     */           }
/*  77 */           return true;
/*  78 */         }  if (transferData instanceof URL) {
/*  79 */           URL value = (URL)transferData;
/*  80 */           String uri = value.toExternalForm();
/*  81 */           onMediaDropped(new String[] { uri });
/*  82 */         } else if (transferData instanceof List) {
/*  83 */           List<?> value = (List)transferData;
/*  84 */           if (value.size() > 0) {
/*  85 */             File file = (File)value.get(0);
/*  86 */             String uri = file.getAbsolutePath();
/*  87 */             onMediaDropped(new String[] { uri });
/*     */           }
/*     */         
/*     */         } 
/*  91 */       } catch (Exception e) {
/*  92 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*  95 */     return false;
/*     */   }
/*     */   
/*     */   private DataFlavor getDataFlavor(TransferHandler.TransferSupport support) {
/*  99 */     if (support.isDataFlavorSupported(this.uriListFlavor)) {
/* 100 */       return this.uriListFlavor;
/*     */     }
/* 102 */     if (support.isDataFlavorSupported(this.javaUrlFlavor)) {
/* 103 */       return this.javaUrlFlavor;
/*     */     }
/* 105 */     if (support.isDataFlavorSupported(this.javaFileListFlavor)) {
/* 106 */       return this.javaFileListFlavor;
/*     */     }
/* 108 */     if (support.isDataFlavorSupported(this.stringFlavor)) {
/* 109 */       return this.stringFlavor;
/*     */     }
/* 111 */     return null;
/*     */   }
/*     */   
/*     */   protected abstract void onMediaDropped(String[] paramArrayOfString);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/imp/MediaTransferHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */