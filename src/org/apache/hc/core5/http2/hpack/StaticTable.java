/*     */ package org.apache.hc.core5.http2.hpack;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
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
/*     */ 
/*     */ 
/*     */ final class StaticTable
/*     */ {
/*  40 */   static final HPackHeader[] STANDARD_HEADERS = new HPackHeader[] { new HPackHeader(":authority", ""), new HPackHeader(":method", "GET"), new HPackHeader(":method", "POST"), new HPackHeader(":path", "/"), new HPackHeader(":path", "/index.html"), new HPackHeader(":scheme", "http"), new HPackHeader(":scheme", "https"), new HPackHeader(":status", "200"), new HPackHeader(":status", "204"), new HPackHeader(":status", "206"), new HPackHeader(":status", "304"), new HPackHeader(":status", "400"), new HPackHeader(":status", "404"), new HPackHeader(":status", "500"), new HPackHeader("accept-charset", ""), new HPackHeader("accept-encoding", "gzip, deflate"), new HPackHeader("accept-language", ""), new HPackHeader("accept-ranges", ""), new HPackHeader("accept", ""), new HPackHeader("access-control-allow-origin", ""), new HPackHeader("age", ""), new HPackHeader("allow", ""), new HPackHeader("authorization", ""), new HPackHeader("cache-control", ""), new HPackHeader("content-disposition", ""), new HPackHeader("content-encoding", ""), new HPackHeader("content-language", ""), new HPackHeader("content-length", ""), new HPackHeader("content-location", ""), new HPackHeader("content-range", ""), new HPackHeader("content-type", ""), new HPackHeader("cookie", ""), new HPackHeader("date", ""), new HPackHeader("etag", ""), new HPackHeader("expect", ""), new HPackHeader("expires", ""), new HPackHeader("from", ""), new HPackHeader("host", ""), new HPackHeader("if-match", ""), new HPackHeader("if-modified-since", ""), new HPackHeader("if-none-match", ""), new HPackHeader("if-range", ""), new HPackHeader("if-unmodified-since", ""), new HPackHeader("last-modified", ""), new HPackHeader("link", ""), new HPackHeader("location", ""), new HPackHeader("max-forwards", ""), new HPackHeader("proxy-authenticate", ""), new HPackHeader("proxy-authorization", ""), new HPackHeader("range", ""), new HPackHeader("referer", ""), new HPackHeader("refresh", ""), new HPackHeader("retry-after", ""), new HPackHeader("server", ""), new HPackHeader("set-cookie", ""), new HPackHeader("strict-transport-security", ""), new HPackHeader("transfer-encoding", ""), new HPackHeader("user-agent", ""), new HPackHeader("vary", ""), new HPackHeader("via", ""), new HPackHeader("www-authenticate", "") };
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   static final StaticTable INSTANCE = new StaticTable(STANDARD_HEADERS);
/*     */   
/*     */   private final HPackHeader[] headers;
/*     */   private final ConcurrentMap<String, CopyOnWriteArrayList<HPackEntry>> mapByName;
/*     */   
/*     */   StaticTable(HPackHeader... headers) {
/* 110 */     this.headers = headers;
/* 111 */     this.mapByName = new ConcurrentHashMap<>();
/*     */     
/* 113 */     for (int i = 0; i < headers.length; i++) {
/* 114 */       HPackHeader header = headers[i];
/*     */       
/* 116 */       String key = header.getName();
/* 117 */       CopyOnWriteArrayList<HPackEntry> entries = this.mapByName.get(key);
/* 118 */       if (entries == null) {
/* 119 */         entries = new CopyOnWriteArrayList<>(new HPackEntry[] { new InternalEntry(header, i) });
/* 120 */         this.mapByName.put(key, entries);
/*     */       } else {
/* 122 */         entries.add(new InternalEntry(header, i));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int length() {
/* 128 */     return this.headers.length;
/*     */   }
/*     */   
/*     */   public HPackHeader get(int index) {
/* 132 */     return this.headers[index - 1];
/*     */   }
/*     */   
/*     */   public List<HPackEntry> getByName(String key) {
/* 136 */     return this.mapByName.get(key);
/*     */   }
/*     */   
/*     */   static class InternalEntry
/*     */     implements HPackEntry {
/*     */     private final HPackHeader header;
/*     */     private final int index;
/*     */     
/*     */     InternalEntry(HPackHeader header, int index) {
/* 145 */       this.header = header;
/* 146 */       this.index = index;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getIndex() {
/* 151 */       return this.index + 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public HPackHeader getHeader() {
/* 156 */       return this.header;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/hpack/StaticTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */