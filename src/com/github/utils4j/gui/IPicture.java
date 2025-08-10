/*    */ package com.github.utils4j.gui;
/*    */ 
/*    */ import com.github.utils4j.imp.Throwables;
/*    */ import java.awt.Image;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import javax.imageio.ImageIO;
/*    */ import javax.swing.ImageIcon;
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
/*    */ public interface IPicture
/*    */ {
/*    */   String path();
/*    */   
/*    */   default InputStream asStream() {
/* 45 */     return getClass().getResourceAsStream(path());
/*    */   }
/*    */   
/*    */   default Image asImage() {
/* 49 */     return Toolkit.getDefaultToolkit().createImage(getClass().getResource(path()));
/*    */   }
/*    */   
/*    */   default ImageIcon asIcon() {
/* 53 */     return new ImageIcon(getClass().getResource(path()));
/*    */   }
/*    */   
/*    */   default BufferedImage asBuffer() {
/* 57 */     return (BufferedImage)Throwables.call(() -> ImageIO.read(asStream()), null);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/gui/IPicture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */