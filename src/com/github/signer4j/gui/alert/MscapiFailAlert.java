/*    */ package com.github.signer4j.gui.alert;
/*    */ 
/*    */ import com.github.signer4j.gui.utils.Images;
/*    */ import com.github.signer4j.imp.Config;
/*    */ import com.github.utils4j.gui.imp.GifAlert;
/*    */ import com.github.utils4j.gui.imp.SwingTools;
/*    */ import com.github.utils4j.imp.Jvms;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*    */ public final class MscapiFailAlert
/*    */   extends GifAlert
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 42 */   private static final AtomicBoolean VISIBLE = new AtomicBoolean(false);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static final String MESSAGE_FORMAT_ALERT = "O PJeOffice Pro detectou falhas no seu token/smartcard quando integrado ao sistema<br>Windows, obrigando-o a cancelar a operação desejada. Este problema tem como causa<br>comum a utilização de versão de driver incompatível com o PJe ou com seu dispositivo.<br><br>Você pode contornar este problema <b>removendo o driver</b> e reinstalando a versão correta<br>(versões anteriores dele) ou mesmo optando por configurar o assinador com integração<br>[PJeOffice] ao invés de [Windows].";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void showMessage() {
/* 54 */     if (Jvms.isWindows() && !VISIBLE.getAndSet(true)) {
/* 55 */       SwingTools.invokeLater(() -> (new MscapiFailAlert("O PJeOffice Pro detectou falhas no seu token/smartcard quando integrado ao sistema<br>Windows, obrigando-o a cancelar a operação desejada. Este problema tem como causa<br>comum a utilização de versão de driver incompatível com o PJe ou com seu dispositivo.<br><br>Você pode contornar este problema <b>removendo o driver</b> e reinstalando a versão correta<br>(versões anteriores dele) ou mesmo optando por configurar o assinador com integração<br>[PJeOffice] ao invés de [Windows].")).display());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public final void dispose() {
/* 61 */     super.dispose();
/* 62 */     VISIBLE.set(false);
/*    */   }
/*    */   
/*    */   private MscapiFailAlert(String message) {
/* 66 */     super("Atenção!", message, Config.getIcon(), Images.NATIVE_INTEGRATION.asIcon());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/gui/alert/MscapiFailAlert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */