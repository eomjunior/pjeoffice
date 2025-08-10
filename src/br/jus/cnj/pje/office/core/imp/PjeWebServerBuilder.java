/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeRequestHandler;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import com.github.utils4j.imp.CustomThreadFactory;
/*    */ import com.github.utils4j.imp.Services;
/*    */ import com.sun.net.httpserver.Filter;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Executors;
/*    */ import java.util.concurrent.ThreadFactory;
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
/*    */ class PjeWebServerBuilder
/*    */   implements IPjeWebServerSetup
/*    */ {
/* 45 */   private int port = 8800;
/*    */   
/* 47 */   private final List<Filter> filters = new ArrayList<>();
/*    */   
/* 49 */   private final List<IPjeRequestHandler> handlers = new ArrayList<>();
/*    */   
/* 51 */   private final ExecutorService executor = Executors.newCachedThreadPool((ThreadFactory)new CustomThreadFactory("http(s)-request"));
/*    */ 
/*    */   
/*    */   public int getPort() {
/* 55 */     return this.port;
/*    */   }
/*    */ 
/*    */   
/*    */   public ExecutorService getExecutor() {
/* 60 */     return this.executor;
/*    */   }
/*    */ 
/*    */   
/*    */   public Filter[] getFilters() {
/* 65 */     return this.filters.<Filter>toArray(new Filter[this.filters.size()]);
/*    */   }
/*    */ 
/*    */   
/*    */   public IPjeRequestHandler[] getHandlers() {
/* 70 */     return this.handlers.<IPjeRequestHandler>toArray(new IPjeRequestHandler[this.filters.size()]);
/*    */   }
/*    */   
/*    */   PjeWebServerBuilder usingPort(int port) {
/* 74 */     this.port = Args.requirePositive(port, "port can't must be positive");
/* 75 */     return this;
/*    */   }
/*    */   
/*    */   PjeWebServerBuilder usingFilter(Filter filter) {
/* 79 */     Args.requireNonNull(filter, "filter is null");
/* 80 */     this.filters.add(filter);
/* 81 */     return this;
/*    */   }
/*    */   
/*    */   PjeWebServerBuilder usingHandler(IPjeRequestHandler handler) {
/* 85 */     this.handlers.add(Args.requireNonNull(handler, "handler is null"));
/* 86 */     return this;
/*    */   }
/*    */   
/*    */   public void shutdown() {
/* 90 */     Services.shutdownNow(this.executor, 2L, 3);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeWebServerBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */