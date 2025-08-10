/*    */ package org.apache.tools.ant.input;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.file.Files;
/*    */ import java.nio.file.Paths;
/*    */ import java.util.Properties;
/*    */ import org.apache.tools.ant.BuildException;
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
/*    */ public class PropertyFileInputHandler
/*    */   implements InputHandler
/*    */ {
/* 35 */   private Properties props = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final String FILE_NAME_KEY = "ant.input.properties";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handleInput(InputRequest request) throws BuildException {
/* 56 */     readProps();
/*    */     
/* 58 */     Object o = this.props.get(request.getPrompt());
/* 59 */     if (o == null) {
/* 60 */       throw new BuildException("Unable to find input for '" + request
/* 61 */           .getPrompt() + "'");
/*    */     }
/* 63 */     request.setInput(o.toString());
/* 64 */     if (!request.isInputValid()) {
/* 65 */       throw new BuildException("Found invalid input " + o + " for '" + request
/* 66 */           .getPrompt() + "'");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private synchronized void readProps() throws BuildException {
/* 74 */     if (this.props == null) {
/* 75 */       String propsFile = System.getProperty("ant.input.properties");
/* 76 */       if (propsFile == null) {
/* 77 */         throw new BuildException("System property ant.input.properties for PropertyFileInputHandler not set");
/*    */       }
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 83 */       this.props = new Properties();
/*    */       
/*    */       try {
/* 86 */         this.props.load(Files.newInputStream(Paths.get(propsFile, new String[0]), new java.nio.file.OpenOption[0]));
/* 87 */       } catch (IOException e) {
/* 88 */         throw new BuildException("Couldn't load " + propsFile, e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/input/PropertyFileInputHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */