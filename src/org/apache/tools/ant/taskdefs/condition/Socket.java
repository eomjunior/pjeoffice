/*    */ package org.apache.tools.ant.taskdefs.condition;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.ProjectComponent;
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
/*    */ public class Socket
/*    */   extends ProjectComponent
/*    */   implements Condition
/*    */ {
/* 35 */   private String server = null;
/* 36 */   private int port = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setServer(String server) {
/* 44 */     this.server = server;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setPort(int port) {
/* 53 */     this.port = port;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean eval() throws BuildException {
/* 62 */     if (this.server == null) {
/* 63 */       throw new BuildException("No server specified in socket condition");
/*    */     }
/* 65 */     if (this.port == 0) {
/* 66 */       throw new BuildException("No port specified in socket condition");
/*    */     }
/* 68 */     log("Checking for listener at " + this.server + ":" + this.port, 3);
/*    */     
/* 70 */     try { java.net.Socket s = new java.net.Socket(this.server, this.port); 
/* 71 */       try { boolean bool = true;
/* 72 */         s.close(); return bool; } catch (Throwable throwable) { try { s.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/* 73 */     { return false; }
/*    */   
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/Socket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */