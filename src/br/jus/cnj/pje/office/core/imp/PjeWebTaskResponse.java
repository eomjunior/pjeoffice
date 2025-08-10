/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*     */ import com.github.taskresolver4j.ITaskResponse;
/*     */ import com.github.utils4j.imp.Base64;
/*     */ import java.io.IOException;
/*     */ import org.apache.hc.core5.http.ContentType;
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
/*     */ public abstract class PjeWebTaskResponse
/*     */   extends PjeTaskResponse
/*     */ {
/*     */   public static PjeWebTaskResponse success() {
/*  41 */     return success(false);
/*     */   }
/*     */   
/*     */   public static PjeWebTaskResponse fail() {
/*  45 */     return fail(false);
/*     */   }
/*     */   
/*     */   public static PjeWebTaskResponse success(boolean json) {
/*  49 */     return json ? JSON_SUCCESS : IMAGE_SUCCESS;
/*     */   }
/*     */   
/*     */   public static PjeWebTaskResponse fail(boolean json) {
/*  53 */     return json ? JSON_FAIL : IMAGE_FAIL;
/*     */   }
/*     */ 
/*     */   
/*  57 */   private static final PjeWebTaskResponse JSON_SUCCESS = new PjeWebTaskResponse(true, "eyJzdWNjZXNzIjogdHJ1ZX0=")
/*     */     {
/*     */       protected void doProcessResponse(IPjeResponse response) throws IOException {
/*  60 */         response.write(this.content, ContentType.APPLICATION_JSON.toString());
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  65 */   private static final PjeWebTaskResponse JSON_FAIL = new PjeWebTaskResponse(false, "eyJzdWNjZXNzIjogZmFsc2V9")
/*     */     {
/*     */       protected void doProcessResponse(IPjeResponse response) throws IOException {
/*  68 */         response.write(this.content, ContentType.APPLICATION_JSON.toString());
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  73 */   private static final PjeWebTaskResponse IMAGE_SUCCESS = new PjeWebTaskResponse(true, "R0lGODlhAQABAPAAAEz/AAAAACH5BAAAAAAALAAAAAABAAEAAAICRAEAOw==")
/*     */     {
/*     */       protected void doProcessResponse(IPjeResponse response) throws IOException {
/*  76 */         response.write(this.content, ContentType.IMAGE_GIF.toString());
/*     */       }
/*     */ 
/*     */       
/*     */       public final PjeWebTaskResponse asJson() {
/*  81 */         return PjeWebTaskResponse.JSON_SUCCESS;
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  86 */   private static final PjeWebTaskResponse IMAGE_FAIL = new PjeWebTaskResponse(false, "iVBORw0KGgoAAAANSUhEUgAAAAIAAAABCAYAAAD0In+KAAAABHNCSVQICAgIfAhkiAAAABFJREFUCJlj/M/A8J+BgYEBAA0FAgD+6nhnAAAAAElFTkSuQmCC")
/*     */     {
/*     */       protected void doProcessResponse(IPjeResponse response) throws IOException {
/*  89 */         response.write(this.content, ContentType.IMAGE_PNG.toString());
/*     */       }
/*     */ 
/*     */       
/*     */       public PjeWebTaskResponse asJson() {
/*  94 */         return PjeWebTaskResponse.JSON_FAIL;
/*     */       }
/*     */     };
/*     */   
/*  98 */   public static final PjeTaskResponse NOTHING_SUCCESS = new PjeTaskResponse(true)
/*     */     {
/*     */       public PjeWebTaskResponse asJson() {
/* 101 */         return PjeWebTaskResponse.JSON_SUCCESS;
/*     */       }
/*     */     };
/*     */   
/* 105 */   public static final PjeTaskResponse NOTHING_FAIL = new PjeTaskResponse(false)
/*     */     {
/*     */       public PjeWebTaskResponse asJson() {
/* 108 */         return PjeWebTaskResponse.JSON_FAIL;
/*     */       }
/*     */     };
/*     */   
/*     */   protected final byte[] content;
/*     */   
/*     */   private PjeWebTaskResponse(boolean success, String base64Content) {
/* 115 */     super(success);
/* 116 */     this.content = Base64.base64Decode(base64Content);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeWebTaskResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */