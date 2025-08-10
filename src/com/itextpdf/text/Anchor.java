/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ public class Anchor
/*     */   extends Phrase
/*     */ {
/*     */   private static final long serialVersionUID = -852278536049236911L;
/*  77 */   protected String name = null;
/*     */ 
/*     */   
/*  80 */   protected String reference = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Anchor() {
/*  88 */     super(16.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Anchor(float leading) {
/*  98 */     super(leading);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Anchor(Chunk chunk) {
/* 107 */     super(chunk);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Anchor(String string) {
/* 116 */     super(string);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Anchor(String string, Font font) {
/* 127 */     super(string, font);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Anchor(float leading, Chunk chunk) {
/* 138 */     super(leading, chunk);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Anchor(float leading, String string) {
/* 149 */     super(leading, string);
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
/*     */   public Anchor(float leading, String string, Font font) {
/* 161 */     super(leading, string, font);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Anchor(Phrase phrase) {
/* 170 */     super(phrase);
/* 171 */     if (phrase instanceof Anchor) {
/* 172 */       Anchor a = (Anchor)phrase;
/* 173 */       setName(a.name);
/* 174 */       setReference(a.reference);
/*     */     } 
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
/*     */   
/*     */   public boolean process(ElementListener listener) {
/*     */     try {
/* 191 */       Iterator<Chunk> i = getChunks().iterator();
/* 192 */       boolean localDestination = (this.reference != null && this.reference.startsWith("#"));
/* 193 */       boolean notGotoOK = true;
/* 194 */       while (i.hasNext()) {
/* 195 */         Chunk chunk = i.next();
/* 196 */         if (this.name != null && notGotoOK && !chunk.isEmpty()) {
/* 197 */           chunk.setLocalDestination(this.name);
/* 198 */           notGotoOK = false;
/*     */         } 
/* 200 */         if (localDestination) {
/* 201 */           chunk.setLocalGoto(this.reference.substring(1));
/*     */         }
/* 203 */         listener.add(chunk);
/*     */       } 
/* 205 */       return true;
/*     */     }
/* 207 */     catch (DocumentException de) {
/* 208 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Chunk> getChunks() {
/* 219 */     boolean localDestination = (this.reference != null && this.reference.startsWith("#"));
/* 220 */     boolean notGotoOK = true;
/* 221 */     List<Chunk> tmp = new ArrayList<Chunk>();
/* 222 */     Iterator<Element> i = iterator();
/*     */     
/* 224 */     while (i.hasNext()) {
/* 225 */       Element element = i.next();
/* 226 */       if (element instanceof Chunk) {
/* 227 */         Chunk chunk = (Chunk)element;
/* 228 */         notGotoOK = applyAnchor(chunk, notGotoOK, localDestination);
/* 229 */         tmp.add(chunk);
/*     */         continue;
/*     */       } 
/* 232 */       for (Chunk c : element.getChunks()) {
/* 233 */         notGotoOK = applyAnchor(c, notGotoOK, localDestination);
/* 234 */         tmp.add(c);
/*     */       } 
/*     */     } 
/*     */     
/* 238 */     return tmp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean applyAnchor(Chunk chunk, boolean notGotoOK, boolean localDestination) {
/* 249 */     if (this.name != null && notGotoOK && !chunk.isEmpty()) {
/* 250 */       chunk.setLocalDestination(this.name);
/* 251 */       notGotoOK = false;
/*     */     } 
/* 253 */     if (localDestination) {
/* 254 */       chunk.setLocalGoto(this.reference.substring(1));
/*     */     }
/* 256 */     else if (this.reference != null) {
/* 257 */       chunk.setAnchor(this.reference);
/* 258 */     }  return notGotoOK;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int type() {
/* 268 */     return 17;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 279 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReference(String reference) {
/* 288 */     this.reference = reference;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 299 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReference() {
/* 308 */     return this.reference;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getUrl() {
/*     */     try {
/* 318 */       return new URL(this.reference);
/*     */     }
/* 320 */     catch (MalformedURLException mue) {
/* 321 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/Anchor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */