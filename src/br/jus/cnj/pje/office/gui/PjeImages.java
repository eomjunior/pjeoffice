/*    */ package br.jus.cnj.pje.office.gui;
/*    */ 
/*    */ import com.github.utils4j.gui.IPicture;
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
/*    */ public enum PjeImages
/*    */   implements IPicture
/*    */ {
/* 33 */   PJE_ICON_DEFAULT("/images/pje-icon-default.png"),
/*    */   
/* 35 */   PJE_ICON_DEFAULT_ONLINE("/images/pje-icon-default-online.png"),
/*    */   
/* 37 */   PJE_ICON_DARK_ONLINE("/images/pje-icon-dark-online.png"),
/*    */   
/* 39 */   PJE_ICON_LIGHT_ONLINE("/images/pje-icon-light-online.png"),
/*    */   
/* 41 */   PJE_ICON_DARK_OFFLINE("/images/pje-icon-dark-offline.png"),
/*    */   
/* 43 */   PJE_ICON_LIGHT_OFFLINE("/images/pje-icon-light-offline.png"),
/*    */   
/* 45 */   PJE_ICON_LIGHT("/images/pje-icon-light.png"),
/*    */   
/* 47 */   PJE_ICON_DARK("/images/pje-icon-dark.png"),
/*    */   
/* 49 */   PJE_SERVER("/images/server.png"),
/*    */   
/* 51 */   PJE_ICON_TRAY("/images/pje-icon-16.png"),
/*    */   
/* 53 */   PJE_ICON_PJE_FEATHER("/images/pje-icon-pje-feather.png"),
/*    */   
/* 55 */   PJE_ICON_TRAY_WARNING("/images/pje-icon-warning.png"),
/*    */   
/* 57 */   PJE_ICON_TRAY_WARNING_ONLINE("/images/pje-icon-warning-online.png"),
/*    */   
/* 59 */   PJE_ICON_SPLASH("/images/pje-icon-splash.gif"),
/*    */   
/* 61 */   PJE_RUNAS_ADMIN("/images/pje-runas-admin.gif"),
/*    */   
/* 63 */   PJE_ICON_TRAY_FEATHER("/images/pje-icon-tray-feather.png"),
/*    */   
/* 65 */   PJE_ICON_TRAY_ONLINE("/images/pje-icon-16-online.png"),
/*    */   
/* 67 */   SIGN2DB("/images/sign2db.png"),
/*    */   
/* 69 */   BROWSER2SIGN("/images/browser2sign.png"),
/*    */   
/* 71 */   BROWSER2DB("/images/browser2db.png");
/*    */   
/*    */   final String path;
/*    */   
/*    */   PjeImages(String path) {
/* 76 */     this.path = path;
/*    */   }
/*    */ 
/*    */   
/*    */   public String path() {
/* 81 */     return this.path;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/gui/PjeImages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */