/*    */ package org.apache.tools.ant.taskdefs.optional.native2ascii;
/*    */ 
/*    */ import java.io.File;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.ProjectComponent;
/*    */ import org.apache.tools.ant.taskdefs.optional.Native2Ascii;
/*    */ import org.apache.tools.ant.types.Commandline;
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
/*    */ public abstract class DefaultNative2Ascii
/*    */   implements Native2AsciiAdapter
/*    */ {
/*    */   public final boolean convert(Native2Ascii args, File srcFile, File destFile) throws BuildException {
/* 53 */     Commandline cmd = new Commandline();
/* 54 */     setup(cmd, args);
/* 55 */     addFiles(cmd, (ProjectComponent)args, srcFile, destFile);
/* 56 */     return run(cmd, (ProjectComponent)args);
/*    */   }
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
/*    */   protected void setup(Commandline cmd, Native2Ascii args) throws BuildException {
/* 72 */     if (args.getEncoding() != null) {
/* 73 */       cmd.createArgument().setValue("-encoding");
/* 74 */       cmd.createArgument().setValue(args.getEncoding());
/*    */     } 
/* 76 */     cmd.addArguments(args.getCurrentArgs());
/*    */   }
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
/*    */   protected void addFiles(Commandline cmd, ProjectComponent log, File src, File dest) throws BuildException {
/* 93 */     cmd.createArgument().setFile(src);
/* 94 */     cmd.createArgument().setFile(dest);
/*    */   }
/*    */   
/*    */   protected abstract boolean run(Commandline paramCommandline, ProjectComponent paramProjectComponent) throws BuildException;
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/native2ascii/DefaultNative2Ascii.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */