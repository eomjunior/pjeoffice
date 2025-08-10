/*    */ package org.apache.log4j.helpers;
/*    */ 
/*    */ import java.io.Writer;
/*    */ import org.apache.log4j.spi.ErrorHandler;
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
/*    */ public class SyslogQuietWriter
/*    */   extends QuietWriter
/*    */ {
/*    */   int syslogFacility;
/*    */   int level;
/*    */   
/*    */   public SyslogQuietWriter(Writer writer, int syslogFacility, ErrorHandler eh) {
/* 35 */     super(writer, eh);
/* 36 */     this.syslogFacility = syslogFacility;
/*    */   }
/*    */   
/*    */   public void setLevel(int level) {
/* 40 */     this.level = level;
/*    */   }
/*    */   
/*    */   public void setSyslogFacility(int syslogFacility) {
/* 44 */     this.syslogFacility = syslogFacility;
/*    */   }
/*    */   
/*    */   public void write(String string) {
/* 48 */     super.write("<" + (this.syslogFacility | this.level) + ">" + string);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/SyslogQuietWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */