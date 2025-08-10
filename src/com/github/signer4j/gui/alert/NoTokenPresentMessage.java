/*    */ package com.github.signer4j.gui.alert;
/*    */ 
/*    */ import com.github.utils4j.imp.Jvms;
/*    */ import javax.swing.JOptionPane;
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
/*    */ abstract class NoTokenPresentMessage
/*    */ {
/* 36 */   protected static final String MESSAGE_MAIN = "Não foi encontrado(a) um(a) certificado/chave privada operacional.\nCausas comuns:\n\n1) Se seu certificado é do tipo A3, tenha certeza de que o dispositivo\nCORRETO (token/smartcard) esteja CONECTADO no seu computador\ne de que esteja operacional.\n\n2) Tenha a certeza de que o  driver do seu dispositivo  tenha sido ins-\ntalado corretamente e de que tenha selecionado o certificado correto\nno menu 'Configuração de certificado'.\n\n3) Tenha a certeza de que o certificado digital escolhido tenha uma\nchave privada associada a ele (consulte o software de administração\ndo seu dispositivo).\n\n" + (
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 46 */     Jvms.isWindows() ? "4) Se seu certificado é do tipo A1 e foi instalado no REPOSITÓRIO DO\nWINDOWS, tenha a certeza de que durante sua importação NÃO tenha\nsido habilitada a proteção de chaves privadas fortes. Sendo este o\ncaso, refaça sua instalação/importação desmarcando aquela proteção.\n" : "");
/*    */ 
/*    */ 
/*    */   
/*    */   protected static final String MESSAGE_RETRY = "\n\nTentar identificar o certificado novamente?";
/*    */ 
/*    */ 
/*    */   
/* 54 */   protected static final String MESSAGE_FORMAT = MESSAGE_MAIN + "\n\nTentar identificar o certificado novamente?";
/*    */   protected JOptionPane jop;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/gui/alert/NoTokenPresentMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */