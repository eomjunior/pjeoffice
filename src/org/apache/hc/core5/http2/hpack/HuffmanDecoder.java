/*     */ package org.apache.hc.core5.http2.hpack;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.hc.core5.util.ByteArrayBuffer;
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
/*     */ final class HuffmanDecoder
/*     */ {
/*     */   private final HuffmanNode root;
/*     */   
/*     */   HuffmanDecoder(int[] codes, byte[] lengths) {
/*  43 */     this.root = buildTree(codes, lengths);
/*     */   }
/*     */   
/*     */   void decode(ByteArrayBuffer out, ByteBuffer src) throws HPackException {
/*  47 */     HuffmanNode node = this.root;
/*  48 */     int current = 0;
/*  49 */     int bits = 0;
/*  50 */     while (src.hasRemaining()) {
/*  51 */       int b = src.get() & 0xFF;
/*  52 */       current = current << 8 | b;
/*  53 */       bits += 8;
/*  54 */       while (bits >= 8) {
/*  55 */         int c = current >>> bits - 8 & 0xFF;
/*  56 */         node = node.getChild(c);
/*  57 */         bits -= node.getBits();
/*  58 */         if (node.isTerminal()) {
/*  59 */           if (node.getSymbol() == 256) {
/*  60 */             throw new HPackException("EOS decoded");
/*     */           }
/*  62 */           out.append(node.getSymbol());
/*  63 */           node = this.root;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  68 */     while (bits > 0) {
/*  69 */       int c = current << 8 - bits & 0xFF;
/*  70 */       node = node.getChild(c);
/*  71 */       if (node.isTerminal() && node.getBits() <= bits) {
/*  72 */         bits -= node.getBits();
/*  73 */         out.append(node.getSymbol());
/*  74 */         node = this.root;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     int mask = (1 << bits) - 1;
/*  84 */     if ((current & mask) != mask) {
/*  85 */       throw new HPackException("Invalid padding");
/*     */     }
/*     */   }
/*     */   
/*     */   private static HuffmanNode buildTree(int[] codes, byte[] lengths) {
/*  90 */     HuffmanNode root = new HuffmanNode();
/*  91 */     for (int symbol = 0; symbol < codes.length; symbol++) {
/*     */       
/*  93 */       int code = codes[symbol];
/*  94 */       int length = lengths[symbol];
/*     */       
/*  96 */       HuffmanNode current = root;
/*  97 */       while (length > 8) {
/*  98 */         if (current.isTerminal()) {
/*  99 */           throw new IllegalStateException("Invalid Huffman code: prefix not unique");
/*     */         }
/* 101 */         length -= 8;
/* 102 */         int j = code >>> length & 0xFF;
/* 103 */         if (!current.hasChild(j)) {
/* 104 */           current.setChild(j, new HuffmanNode());
/*     */         }
/* 106 */         current = current.getChild(j);
/*     */       } 
/*     */       
/* 109 */       HuffmanNode terminal = new HuffmanNode(symbol, length);
/* 110 */       int shift = 8 - length;
/* 111 */       int start = code << shift & 0xFF;
/* 112 */       int end = 1 << shift;
/* 113 */       for (int i = start; i < start + end; i++) {
/* 114 */         current.setChild(i, terminal);
/*     */       }
/*     */     } 
/* 117 */     return root;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/hpack/HuffmanDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */