/*     */ package br.jus.cnj.pje.office.shell;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import java.util.Properties;
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
/*     */ enum Task
/*     */ {
/*  36 */   CNJ_ASSINADOR("cnj.assinador")
/*     */   {
/*     */     public void echo(String[] args, Properties output) {
/*  39 */       output.put("enviarPara", Strings.at(args, 2));
/*  40 */       output.put("modo", Strings.at(args, 3));
/*  41 */       output.put("padraoAssinatura", Strings.at(args, 4));
/*  42 */       output.put("tipoAssinatura", Strings.at(args, 5));
/*  43 */       output.put("algoritmoHash", Strings.at(args, 6));
/*     */     }
/*     */   },
/*  46 */   PDF_JOIN("pdf.join"),
/*  47 */   PDF_SPLIT_BY_SIZE("pdf.split_by_size")
/*     */   {
/*     */     public void echo(String[] args, Properties output) {
/*  50 */       output.put("tamanho", Strings.at(args, 2));
/*     */     }
/*     */   },
/*  53 */   PDF_SPLIT_BY_PARITY("pdf.split_by_parity")
/*     */   {
/*     */     public void echo(String[] args, Properties output) {
/*  56 */       output.put("paridade", Strings.at(args, 2));
/*     */     }
/*     */   },
/*  59 */   PDF_SPLIT_BY_COUNT("pdf.split_by_count")
/*     */   {
/*     */     public void echo(String[] args, Properties output) {
/*  62 */       output.put("totalPaginas", Strings.at(args, 2));
/*     */     }
/*     */   },
/*  65 */   PDF_SPLIT_BY_PATES("pdf.split_by_pages"),
/*  66 */   VIDEO_SPLIT_BY_DURATION("video.split_by_duration")
/*     */   {
/*     */     public void echo(String[] args, Properties output) {
/*  69 */       output.put("duracao", Strings.at(args, 2));
/*     */     }
/*     */   },
/*  72 */   VIDEO_SPLIT_BY_SIZE("video.split_by_size")
/*     */   {
/*     */     public void echo(String[] args, Properties output) {
/*  75 */       output.put("tamanho", Strings.at(args, 2));
/*     */     }
/*     */   },
/*  78 */   VIDEO_SPLIT_BY_SLICE("video.split_by_slice"),
/*  79 */   VIDEO_EXTRACT_AUDIO("video.extract_audio") {
/*     */     public void echo(String[] args, Properties output) {
/*  81 */       output.put("tipo", Strings.at(args, 2));
/*     */     }
/*     */   },
/*  84 */   VIDEO_CONVERT_WEBM("video.convert_webm"),
/*  85 */   VIDEO_OPTIMIZE("video.optimize");
/*     */   
/*     */   public static Optional<Task> from(String at) {
/*  88 */     for (Task task : values()) {
/*  89 */       if (task.getId().equalsIgnoreCase(at))
/*  90 */         return Optional.of(task); 
/*     */     } 
/*  92 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private String id;
/*     */   
/*     */   Task(String id) {
/*  98 */     this.id = id.toLowerCase();
/*     */   }
/*     */   
/*     */   public final String getId() {
/* 102 */     return this.id;
/*     */   }
/*     */   
/*     */   public void echo(String[] args, Properties p) {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/shell/Task.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */