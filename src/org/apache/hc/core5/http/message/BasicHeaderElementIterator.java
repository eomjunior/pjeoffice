/*    */ package org.apache.hc.core5.http.message;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import org.apache.hc.core5.http.Header;
/*    */ import org.apache.hc.core5.http.HeaderElement;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BasicHeaderElementIterator
/*    */   extends AbstractHeaderElementIterator<HeaderElement>
/*    */ {
/*    */   private final HeaderValueParser parser;
/*    */   
/*    */   public BasicHeaderElementIterator(Iterator<Header> headerIterator, HeaderValueParser parser) {
/* 51 */     super(headerIterator);
/* 52 */     this.parser = (HeaderValueParser)Args.notNull(parser, "Parser");
/*    */   }
/*    */   
/*    */   public BasicHeaderElementIterator(Iterator<Header> headerIterator) {
/* 56 */     this(headerIterator, BasicHeaderValueParser.INSTANCE);
/*    */   }
/*    */ 
/*    */   
/*    */   HeaderElement parseHeaderElement(CharSequence buf, ParserCursor cursor) {
/* 61 */     HeaderElement e = this.parser.parseHeaderElement(buf, cursor);
/* 62 */     if (!e.getName().isEmpty() || e.getValue() != null) {
/* 63 */       return e;
/*    */     }
/* 65 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/BasicHeaderElementIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */