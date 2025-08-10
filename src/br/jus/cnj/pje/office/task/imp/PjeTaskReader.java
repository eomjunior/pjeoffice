/*     */ package br.jus.cnj.pje.office.task.imp;
/*     */ 
/*     */ import br.jus.cnj.pje.office.task.IJsonTranslator;
/*     */ import com.github.taskresolver4j.IRequestReader;
/*     */ import com.github.taskresolver4j.NotImplementedReader;
/*     */ import com.github.utils4j.IConstants;
/*     */ import com.github.utils4j.imp.Media;
/*     */ import com.github.utils4j.imp.NotImplementedException;
/*     */ import com.github.utils4j.imp.Params;
/*     */ import com.github.utils4j.imp.Randomizer;
/*     */ import java.io.File;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
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
/*     */ public enum PjeTaskReader
/*     */ {
/*     */   private static final PjeTaskReader[] VALUES;
/*     */   private final String id;
/*     */   private final Media extension;
/*  49 */   CNJ_ASSINADOR("cnj.assinador", (IRequestReader<Params>)TarefaAssinadorReader.INSTANCE),
/*     */   
/*  51 */   CNJ_ASSINADOR_HASH("cnj.assinadorHash", (IRequestReader<Params>)TarefaAssinadorHashReader.INSTANCE),
/*     */   
/*  53 */   CNJ_AUTENTICADOR("cnj.autenticador", (IRequestReader<Params>)TarefaAutenticadorReader.INSTANCE),
/*     */   
/*  55 */   CNJ_ASSINADOR_BASE64("cnj.assinadorBase64", (IRequestReader<Params>)TarefaAssinadorBase64Reader.INSTANCE),
/*     */   
/*  57 */   CNJ_CERTCHAIN("cnj.certchain", (IRequestReader<Params>)TarefaCertChainReader.INSTANCE),
/*     */   
/*  59 */   SSO_AUTENTICADOR("sso.autenticador", (IRequestReader<Params>)TarefaAutenticadorSSOReader.INSTANCE),
/*     */ 
/*     */ 
/*     */   
/*  63 */   UTIL_IMPRESSOR("util.impressor", (IRequestReader<Params>)TarefaImpressaoReader.INSTANCE),
/*     */   
/*  65 */   UTIL_DOWNLOADER("util.downloader", (IRequestReader<Params>)TarefaDownloadReader.INSTANCE),
/*     */   
/*  67 */   UTIL_TEST("util.test", (IRequestReader<Params>)TarefaTesteReader.INSTANCE),
/*     */ 
/*     */ 
/*     */   
/*  71 */   PDF_JOIN("pdf.join", (IRequestReader<Params>)TarefaPdfJuncaoReader.INSTANCE, Media.PDF),
/*     */   
/*  73 */   PDF_SPLIT_BY_SIZE("pdf.split_by_size", (IRequestReader<Params>)TarefaPdfDivisaoTamanhoReader.INSTANCE, Media.PDF),
/*     */   
/*  75 */   PDF_SPLIT_BY_PARITY("pdf.split_by_parity", (IRequestReader<Params>)TarefaPdfDivisaoParidadeReader.INSTANCE, Media.PDF),
/*     */   
/*  77 */   PDF_SPLIT_BY_COUNT("pdf.split_by_count", (IRequestReader<Params>)TarefaPdfDivisaoContagemReader.INSTANCE, Media.PDF),
/*     */   
/*  79 */   PDF_SPLIT_BY_PAGES("pdf.split_by_pages", (IRequestReader<Params>)TarefaPdfDivisaoPaginasReader.INSTANCE, Media.PDF),
/*     */ 
/*     */ 
/*     */   
/*  83 */   VIDEO_SPLIT_BY_DURATION("video.split_by_duration", (IRequestReader<Params>)TarefaVideoDivisaoDuracaoReader.INSTANCE, Media.MP4),
/*     */   
/*  85 */   VIDEO_SPLIT_BY_SIZE("video.split_by_size", (IRequestReader<Params>)TarefaVideoDivisaoTamanhoReader.INSTANCE, Media.MP4),
/*     */   
/*  87 */   VIDEO_SPLIT_BY_SLICE("video.split_by_slice", (IRequestReader<Params>)TarefaVideoDivisaoSliceReader.INSTANCE, Media.MP4),
/*     */   
/*  89 */   VIDEO_EXTRACT_AUDIO("video.extract_audio", (IRequestReader<Params>)TarefaVideoExtracaoAudioReader.INSTANCE, Media.MP4),
/*     */   
/*  91 */   VIDEO_CONVERT_WEBM("video.convert_webm", (IRequestReader<Params>)TarefaVideoConversaoWebmReader.INSTANCE, Media.MP4),
/*     */   
/*  93 */   VIDEO_OPTIMIZE("video.optimize", (IRequestReader<Params>)TarefaVideoOtimizacaoReader.INSTANCE, Media.MP4);
/*     */   private final IRequestReader<Params> reader;
/*     */   
/*     */   static {
/*  97 */     VALUES = values();
/*     */   }
/*     */   public static Optional<PjeTaskReader> task(String id) {
/* 100 */     return Stream.<PjeTaskReader>of(VALUES).filter(r -> r.getId().equalsIgnoreCase(id)).findFirst();
/*     */   }
/*     */   
/*     */   static IRequestReader<Params> from(String taskId) {
/* 104 */     return (IRequestReader<Params>)task(taskId).map(PjeTaskReader::get).orElse(NotImplementedReader.INSTANCE);
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
/*     */   
/*     */   PjeTaskReader(String id, IRequestReader<Params> reader, Media extension) {
/* 118 */     this.id = id.toLowerCase();
/* 119 */     this.reader = reader;
/* 120 */     this.extension = extension;
/*     */   }
/*     */   
/*     */   public final String getId() {
/* 124 */     return this.id;
/*     */   }
/*     */   
/*     */   final IRequestReader<Params> get() {
/* 128 */     return this.reader;
/*     */   }
/*     */   
/*     */   public final String toUri(Params input) throws Exception {
/* 132 */     return "?r=" + URLEncoder.encode(toJson(input), IConstants.UTF_8.toString()) + "&u=" + Randomizer.nocache();
/*     */   }
/*     */   
/*     */   public final boolean accept(File f) {
/* 136 */     return (f != null && (Media.ANY == this.extension || f.getName().toLowerCase().endsWith(this.extension.getExtension(true))));
/*     */   }
/*     */   
/*     */   final String toJson(Params input) throws Exception {
/* 140 */     if (this.reader instanceof IJsonTranslator)
/* 141 */       return ((IJsonTranslator)this.reader).toJson(input); 
/* 142 */     throw new NotImplementedException();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/task/imp/PjeTaskReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */