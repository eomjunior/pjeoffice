/*     */ package org.apache.tools.ant.types.resources;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Stack;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.types.DataType;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.util.ConcatResourceInputStream;
/*     */ import org.apache.tools.ant.util.LineTokenizer;
/*     */ import org.apache.tools.ant.util.Tokenizer;
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
/*     */ public class Tokens
/*     */   extends BaseResourceCollectionWrapper
/*     */ {
/*     */   private Tokenizer tokenizer;
/*     */   private String encoding;
/*     */   
/*     */   protected synchronized Collection<Resource> getCollection() {
/*  53 */     ResourceCollection rc = getResourceCollection();
/*  54 */     if (rc.isEmpty()) {
/*  55 */       return Collections.emptySet();
/*     */     }
/*  57 */     if (this.tokenizer == null)
/*  58 */       this.tokenizer = (Tokenizer)new LineTokenizer(); 
/*     */     
/*  60 */     try { ConcatResourceInputStream cat = new ConcatResourceInputStream(rc);
/*     */ 
/*     */       
/*  63 */       try { InputStreamReader rdr = new InputStreamReader((InputStream)cat, (this.encoding == null) ? Charset.defaultCharset() : Charset.forName(this.encoding)); 
/*  64 */         try { cat.setManagingComponent((ProjectComponent)this);
/*  65 */           List<Resource> result = new ArrayList<>();
/*  66 */           for (String s = this.tokenizer.getToken(rdr); s != null; 
/*  67 */             s = this.tokenizer.getToken(rdr)) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*  72 */             StringResource resource = new StringResource(s);
/*  73 */             resource.setProject(getProject());
/*  74 */             result.add(resource);
/*     */           } 
/*  76 */           List<Resource> list1 = result;
/*  77 */           rdr.close(); cat.close(); return list1; } catch (Throwable throwable) { try { rdr.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Throwable throwable) { try { cat.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/*  78 */     { throw new BuildException("Error reading tokens", e); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setEncoding(String encoding) {
/*  87 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void add(Tokenizer tokenizer) {
/*  96 */     if (isReference()) {
/*  97 */       throw noChildrenAllowed();
/*     */     }
/*  99 */     if (this.tokenizer != null) {
/* 100 */       throw new BuildException("Only one nested tokenizer allowed.");
/*     */     }
/* 102 */     this.tokenizer = tokenizer;
/* 103 */     setChecked(false);
/*     */   }
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
/*     */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
/* 116 */     if (isChecked()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 121 */     super.dieOnCircularReference(stk, p);
/*     */     
/* 123 */     if (!isReference()) {
/* 124 */       if (this.tokenizer instanceof DataType) {
/* 125 */         pushAndInvokeCircularReferenceCheck((DataType)this.tokenizer, stk, p);
/*     */       }
/*     */       
/* 128 */       setChecked(true);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/Tokens.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */