/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeJsonCodec;
/*     */ import br.jus.cnj.pje.office.core.IPjeWebCodec;
/*     */ import com.github.utils4j.IConstants;
/*     */ import com.github.utils4j.IDownloadStatus;
/*     */ import com.github.utils4j.IGetCodec;
/*     */ import com.github.utils4j.IResultChecker;
/*     */ import com.github.utils4j.imp.States;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import com.github.utils4j.imp.function.IProvider;
/*     */ import java.io.IOException;
/*     */ import org.apache.hc.client5.http.classic.methods.HttpGet;
/*     */ import org.json.JSONObject;
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
/*     */ public enum PjeJsonGlobal
/*     */   implements IPjeJsonCodec
/*     */ {
/*  47 */   STDIO_HTTP(PjeWebGlobal.HTTP_CODEC)
/*     */   {
/*     */     protected void setup(IPjeWebCodec webCodec) {
/*  50 */       this.codec = new PjeStdioClient.PjeStdioJsonCodec(IConstants.DEFAULT_CHARSET, () -> webCodec);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   FILEWATCH_HTTP(PjeWebGlobal.HTTP_CODEC)
/*     */   {
/*     */     protected void setup(IPjeWebCodec webCodec) {
/*  64 */       this.codec = new PjeFileWatchClient.PjeFileJsonCodec(IConstants.DEFAULT_CHARSET, () -> webCodec);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   CLIP_HTTP(PjeWebGlobal.HTTP_CODEC)
/*     */   {
/*     */     protected void setup(IPjeWebCodec webCodec) {
/*  78 */       this.codec = new PjeClipClient.PjeClipJsonCodec(IConstants.DEFAULT_CHARSET, () -> webCodec);
/*     */     }
/*     */   };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected IPjeJsonCodec codec;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void open() {}
/*     */ 
/*     */ 
/*     */   
/*     */   PjeJsonGlobal(IPjeWebCodec codec) {
/*  95 */     setup(codec);
/*     */   }
/*     */   
/*     */   public static void closeAll() {
/*  99 */     for (PjeJsonGlobal v : values()) {
/* 100 */       Throwables.quietly(v::close);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/* 108 */     return this.codec.isClosed();
/*     */   }
/*     */ 
/*     */   
/*     */   public PjeTaskResponse post(IProvider<JSONObject> provider, IResultChecker checker) throws Exception {
/* 113 */     States.requireFalse(isClosed(), "o codec já foi fechado");
/* 114 */     return (PjeTaskResponse)this.codec.post(provider, checker);
/*     */   }
/*     */ 
/*     */   
/*     */   public void get(IProvider<HttpGet> provider, IDownloadStatus status) throws Exception {
/* 119 */     States.requireFalse(isClosed(), "o codec já foi fechado");
/* 120 */     this.codec.get(provider, status);
/*     */   }
/*     */ 
/*     */   
/*     */   public String get(IProvider<HttpGet> provider) throws Exception {
/* 125 */     States.requireFalse(isClosed(), "o codec já foi fechado");
/* 126 */     return this.codec.get(provider);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 131 */     this.codec.close();
/*     */   }
/*     */   
/*     */   protected abstract void setup(IPjeWebCodec paramIPjeWebCodec);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeJsonGlobal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */