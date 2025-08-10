/*    */ package org.apache.tools.ant.taskdefs.optional.depend;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
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
/*    */ public interface ClassFileIterator
/*    */   extends Iterable<ClassFile>
/*    */ {
/*    */   ClassFile getNextClassFile();
/*    */   
/*    */   default Iterator<ClassFile> iterator() {
/* 39 */     return new Iterator<ClassFile>()
/*    */       {
/*    */         
/* 42 */         ClassFile next = ClassFileIterator.this.getNextClassFile();
/*    */ 
/*    */ 
/*    */         
/*    */         public boolean hasNext() {
/* 47 */           return (this.next != null);
/*    */         }
/*    */ 
/*    */         
/*    */         public ClassFile next() {
/* 52 */           if (this.next == null) {
/* 53 */             throw new NoSuchElementException();
/*    */           }
/*    */           try {
/* 56 */             return this.next;
/*    */           } finally {
/* 58 */             this.next = ClassFileIterator.this.getNextClassFile();
/*    */           } 
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/depend/ClassFileIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */