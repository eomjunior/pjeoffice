/*     */ package org.apache.tools.ant.listener;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Properties;
/*     */ import org.apache.tools.ant.DefaultLogger;
/*     */ import org.apache.tools.ant.util.FileUtils;
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
/*     */ public class AnsiColorLogger
/*     */   extends DefaultLogger
/*     */ {
/*     */   private static final int ATTR_DIM = 2;
/*     */   private static final int FG_RED = 31;
/*     */   private static final int FG_GREEN = 32;
/*     */   private static final int FG_BLUE = 34;
/*     */   private static final int FG_MAGENTA = 35;
/*     */   private static final int FG_CYAN = 36;
/*     */   private static final String PREFIX = "\033[";
/*     */   private static final String SUFFIX = "m";
/*     */   private static final char SEPARATOR = ';';
/*     */   private static final String END_COLOR = "\033[m";
/* 138 */   private String errColor = "\033[2;31m";
/*     */   
/* 140 */   private String warnColor = "\033[2;35m";
/*     */   
/* 142 */   private String infoColor = "\033[2;36m";
/*     */   
/* 144 */   private String verboseColor = "\033[2;32m";
/*     */   
/* 146 */   private String debugColor = "\033[2;34m";
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean colorsSet = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setColors() {
/* 156 */     String userColorFile = System.getProperty("ant.logger.defaults");
/* 157 */     String systemColorFile = "/org/apache/tools/ant/listener/defaults.properties";
/*     */ 
/*     */     
/* 160 */     InputStream in = null;
/*     */     
/*     */     try {
/* 163 */       Properties prop = new Properties();
/*     */       
/* 165 */       if (userColorFile != null) {
/* 166 */         in = Files.newInputStream(Paths.get(userColorFile, new String[0]), new java.nio.file.OpenOption[0]);
/*     */       } else {
/* 168 */         in = getClass().getResourceAsStream(systemColorFile);
/*     */       } 
/*     */       
/* 171 */       if (in != null) {
/* 172 */         prop.load(in);
/*     */       }
/*     */       
/* 175 */       String errC = prop.getProperty("AnsiColorLogger.ERROR_COLOR");
/* 176 */       String warn = prop.getProperty("AnsiColorLogger.WARNING_COLOR");
/* 177 */       String info = prop.getProperty("AnsiColorLogger.INFO_COLOR");
/* 178 */       String verbose = prop.getProperty("AnsiColorLogger.VERBOSE_COLOR");
/* 179 */       String debug = prop.getProperty("AnsiColorLogger.DEBUG_COLOR");
/* 180 */       if (errC != null) {
/* 181 */         this.errColor = "\033[" + errC + "m";
/*     */       }
/* 183 */       if (warn != null) {
/* 184 */         this.warnColor = "\033[" + warn + "m";
/*     */       }
/* 186 */       if (info != null) {
/* 187 */         this.infoColor = "\033[" + info + "m";
/*     */       }
/* 189 */       if (verbose != null) {
/* 190 */         this.verboseColor = "\033[" + verbose + "m";
/*     */       }
/* 192 */       if (debug != null) {
/* 193 */         this.debugColor = "\033[" + debug + "m";
/*     */       }
/* 195 */     } catch (IOException iOException) {
/*     */     
/*     */     } finally {
/* 198 */       FileUtils.close(in);
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
/*     */   protected void printMessage(String message, PrintStream stream, int priority) {
/* 210 */     if (message != null && stream != null) {
/* 211 */       if (!this.colorsSet) {
/* 212 */         setColors();
/* 213 */         this.colorsSet = true;
/*     */       } 
/*     */       
/* 216 */       StringBuilder msg = new StringBuilder(message);
/* 217 */       switch (priority) {
/*     */         case 0:
/* 219 */           msg.insert(0, this.errColor);
/* 220 */           msg.append("\033[m");
/*     */           break;
/*     */         case 1:
/* 223 */           msg.insert(0, this.warnColor);
/* 224 */           msg.append("\033[m");
/*     */           break;
/*     */         case 2:
/* 227 */           msg.insert(0, this.infoColor);
/* 228 */           msg.append("\033[m");
/*     */           break;
/*     */         case 3:
/* 231 */           msg.insert(0, this.verboseColor);
/* 232 */           msg.append("\033[m");
/*     */           break;
/*     */ 
/*     */         
/*     */         default:
/* 237 */           msg.insert(0, this.debugColor);
/* 238 */           msg.append("\033[m");
/*     */           break;
/*     */       } 
/* 241 */       String strmessage = msg.toString();
/* 242 */       stream.println(strmessage);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/listener/AnsiColorLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */