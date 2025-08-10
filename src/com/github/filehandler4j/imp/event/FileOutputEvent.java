/*    */ package com.github.filehandler4j.imp.event;
/*    */ 
/*    */ import com.github.filehandler4j.IFileOutputEvent;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import java.io.File;
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
/*    */ public class FileOutputEvent
/*    */   implements IFileOutputEvent
/*    */ {
/*    */   private final String message;
/*    */   private final File output;
/*    */   
/*    */   public FileOutputEvent(String message, File output) {
/* 41 */     this.message = message;
/* 42 */     this.output = (File)Args.requireNonNull(output, "output is null");
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getMessage() {
/* 47 */     return this.message;
/*    */   }
/*    */ 
/*    */   
/*    */   public final File getOutput() {
/* 52 */     return this.output;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return getMessage() + " file: " + this.output.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/filehandler4j/imp/event/FileOutputEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */