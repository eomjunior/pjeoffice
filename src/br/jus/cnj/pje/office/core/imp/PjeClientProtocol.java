/*     */ package br.jus.cnj.pje.office.core.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.core.IPjeClient;
/*     */ import br.jus.cnj.pje.office.core.IPjeClientAcessor;
/*     */ import br.jus.cnj.pje.office.core.IPjeClientBuilder;
/*     */ import br.jus.cnj.pje.office.core.IPjeJsonCodec;
/*     */ import br.jus.cnj.pje.office.core.IPjeWebCodec;
/*     */ import com.github.utils4j.ICanceller;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import com.github.utils4j.imp.Throwables;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public enum PjeClientProtocol
/*     */   implements IPjeClientAcessor
/*     */ {
/*  47 */   STDIO("stdio")
/*     */   {
/*     */     protected IPjeClientBuilder createBuilder(Supplier<ICanceller> canceller) {
/*  50 */       return new PjeStdioClientBuilder(canceller, () -> PjeJsonGlobal.STDIO_HTTP);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Function<String, PjeTaskResponse> success() {
/*  55 */       return o -> new PjeStdioTaskResponse(o);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Function<Throwable, PjeTaskResponse> fail() {
/*  60 */       return t -> new PjeStdioTaskResponse(Throwables.rootMessage(t), false);
/*     */     }
/*     */   },
/*  63 */   FILEWATCH("filewatch")
/*     */   {
/*     */     protected IPjeClientBuilder createBuilder(Supplier<ICanceller> canceller) {
/*  66 */       return new PjeFileWatchClientBuilder(canceller, () -> PjeJsonGlobal.FILEWATCH_HTTP);
/*     */     }
/*     */   },
/*  69 */   CLIP("clip")
/*     */   {
/*     */     protected IPjeClientBuilder createBuilder(Supplier<ICanceller> canceller) {
/*  72 */       return new PjeClipClientBuilder(canceller, () -> PjeJsonGlobal.CLIP_HTTP);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Function<String, PjeTaskResponse> success() {
/*  77 */       return o -> new PjeClipTaskResponse(o);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Function<Throwable, PjeTaskResponse> fail() {
/*  82 */       return t -> new PjeClipTaskResponse(Throwables.rootMessage(t), false);
/*     */     }
/*     */   },
/*  85 */   HTTP("http")
/*     */   {
/*     */     protected IPjeClientBuilder createBuilder(Supplier<ICanceller> canceller) {
/*  88 */       return new PjeClientWebBuilder(canceller, () -> PjeWebGlobal.HTTP_CODEC);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Function<String, PjeTaskResponse> success() {
/*  93 */       return o -> PjeWebTaskResponse.success();
/*     */     }
/*     */ 
/*     */     
/*     */     protected Function<Throwable, PjeTaskResponse> fail() {
/*  98 */       return t -> PjeWebTaskResponse.fail();
/*     */     }
/*     */   },
/* 101 */   HTTPS("https")
/*     */   {
/*     */     protected IPjeClientBuilder createBuilder(Supplier<ICanceller> canceller) {
/* 104 */       return new PjeClientWebBuilder(canceller, () -> PjeWebGlobal.HTTPS_CODEC);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Function<Throwable, PjeTaskResponse> fail() {
/* 109 */       return HTTP.fail();
/*     */     }
/*     */ 
/*     */     
/*     */     protected Function<String, PjeTaskResponse> success() {
/* 114 */       return HTTP.success();
/*     */     }
/*     */   },
/* 117 */   NOTHING("nothing")
/*     */   {
/*     */     protected IPjeClientBuilder createBuilder(Supplier<ICanceller> canceller) {
/* 120 */       throw new RuntimeException("Unsupported client for 'nothing' protocol");
/*     */     } };
/*     */   private static final Logger LOGGER;
/*     */   
/*     */   public static void open() {
/* 125 */     PjeWebGlobal.open();
/* 126 */     PjeJsonGlobal.open();
/*     */   }
/*     */   private IPjeClient client; private final String protocol;
/*     */   public static void closeAll() {
/* 130 */     PjeWebGlobal.closeAll();
/* 131 */     PjeJsonGlobal.closeAll();
/*     */   }
/*     */   static {
/* 134 */     LOGGER = LoggerFactory.getLogger(PjeClientProtocol.class);
/*     */   }
/*     */   private static PjeClientProtocol from(String uri) {
/* 137 */     for (PjeClientProtocol mode : values()) {
/* 138 */       if (uri.startsWith(mode.protocol + ":"))
/* 139 */         return mode; 
/*     */     } 
/* 141 */     return NOTHING;
/*     */   }
/*     */   
/*     */   public static void evict() {
/* 145 */     for (PjeClientProtocol protocol : values()) {
/* 146 */       protocol.recycle();
/* 147 */       LOGGER.info("client " + protocol.protocol + " closed");
/*     */     } 
/*     */   }
/*     */   
/*     */   public static IPjeClient clientFrom(String address, Supplier<ICanceller> canceller) {
/* 152 */     Args.requireNonNull(canceller, "canceller is null");
/* 153 */     return from(Strings.trim(address).toLowerCase()).getClient(canceller);
/*     */   }
/*     */   
/*     */   public static Function<Throwable, PjeTaskResponse> failFrom(String uri) {
/* 157 */     return from(Strings.trim(uri).toLowerCase()).fail();
/*     */   }
/*     */   
/*     */   public static Function<String, PjeTaskResponse> successFrom(String uri) {
/* 161 */     return from(Strings.trim(uri).toLowerCase()).success();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   PjeClientProtocol(String name) {
/* 169 */     this.protocol = name;
/*     */   }
/*     */   
/*     */   protected Function<String, PjeTaskResponse> success() {
/* 173 */     return o -> PjeWebTaskResponse.NOTHING_SUCCESS;
/*     */   }
/*     */   
/*     */   protected Function<Throwable, PjeTaskResponse> fail() {
/* 177 */     return t -> PjeWebTaskResponse.NOTHING_FAIL;
/*     */   }
/*     */   
/*     */   protected void recycle() {
/* 181 */     this.client = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized IPjeClient getClient(Supplier<ICanceller> canceller) {
/* 186 */     if (this.client == null) {
/* 187 */       this.client = createBuilder(canceller).build();
/*     */     }
/* 189 */     return this.client;
/*     */   }
/*     */   
/*     */   protected abstract IPjeClientBuilder createBuilder(Supplier<ICanceller> paramSupplier);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeClientProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */