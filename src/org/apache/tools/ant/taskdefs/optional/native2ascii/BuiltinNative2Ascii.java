/*    */ package org.apache.tools.ant.taskdefs.optional.native2ascii;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.File;
/*    */ import java.io.FileReader;
/*    */ import java.io.FileWriter;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.io.Writer;
/*    */ import java.nio.file.Files;
/*    */ import java.util.Iterator;
/*    */ import java.util.function.UnaryOperator;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.taskdefs.optional.Native2Ascii;
/*    */ import org.apache.tools.ant.util.Native2AsciiUtils;
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
/*    */ public class BuiltinNative2Ascii
/*    */   implements Native2AsciiAdapter
/*    */ {
/*    */   static final String IMPLEMENTATION_NAME = "builtin";
/*    */   
/*    */   public final boolean convert(Native2Ascii args, File srcFile, File destFile) throws BuildException {
/* 48 */     boolean reverse = args.getReverse();
/* 49 */     String encoding = args.getEncoding(); 
/* 50 */     try { BufferedReader input = getReader(srcFile, encoding, reverse); 
/* 51 */       try { Writer output = getWriter(destFile, encoding, reverse);
/*    */         
/* 53 */         try { translate(input, output, reverse ? Native2AsciiUtils::ascii2native : 
/* 54 */               Native2AsciiUtils::native2ascii);
/* 55 */           boolean bool = true;
/* 56 */           if (output != null) output.close();  if (input != null) input.close();  return bool; } catch (Throwable throwable) { if (output != null) try { output.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (input != null) try { input.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ex)
/* 57 */     { throw new BuildException("Exception trying to translate data", ex); }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   private BufferedReader getReader(File srcFile, String encoding, boolean reverse) throws IOException {
/* 63 */     if (reverse || encoding == null) {
/* 64 */       return new BufferedReader(new FileReader(srcFile));
/*    */     }
/* 66 */     return new BufferedReader(new InputStreamReader(
/* 67 */           Files.newInputStream(srcFile.toPath(), new java.nio.file.OpenOption[0]), encoding));
/*    */   }
/*    */ 
/*    */   
/*    */   private Writer getWriter(File destFile, String encoding, boolean reverse) throws IOException {
/* 72 */     if (!reverse) {
/* 73 */       encoding = "ASCII";
/*    */     }
/* 75 */     if (encoding == null) {
/* 76 */       return new BufferedWriter(new FileWriter(destFile));
/*    */     }
/* 78 */     return new BufferedWriter(new OutputStreamWriter(
/* 79 */           Files.newOutputStream(destFile.toPath(), new java.nio.file.OpenOption[0]), encoding));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void translate(BufferedReader input, Writer output, UnaryOperator<String> translation) throws IOException {
/*    */     for (String line : () -> input.lines().map(translation).iterator()) {
/* 87 */       output.write(String.format("%s%n", new Object[] { line }));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/native2ascii/BuiltinNative2Ascii.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */