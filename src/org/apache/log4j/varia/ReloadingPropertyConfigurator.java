/*    */ package org.apache.log4j.varia;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.net.URL;
/*    */ import org.apache.log4j.PropertyConfigurator;
/*    */ import org.apache.log4j.spi.Configurator;
/*    */ import org.apache.log4j.spi.LoggerRepository;
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
/*    */ public class ReloadingPropertyConfigurator
/*    */   implements Configurator
/*    */ {
/* 29 */   PropertyConfigurator delegate = new PropertyConfigurator();
/*    */   
/*    */   public void doConfigure(InputStream inputStream, LoggerRepository repository) {}
/*    */   
/*    */   public void doConfigure(URL url, LoggerRepository repository) {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/varia/ReloadingPropertyConfigurator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */