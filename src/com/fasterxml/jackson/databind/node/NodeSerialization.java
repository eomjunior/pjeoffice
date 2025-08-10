/*    */ package com.fasterxml.jackson.databind.node;
/*    */ 
/*    */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*    */ import java.io.Externalizable;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInput;
/*    */ import java.io.ObjectOutput;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class NodeSerialization
/*    */   implements Serializable, Externalizable
/*    */ {
/*    */   protected static final int LONGEST_EAGER_ALLOC = 100000;
/*    */   private static final long serialVersionUID = 1L;
/*    */   public byte[] json;
/*    */   
/*    */   public NodeSerialization() {}
/*    */   
/*    */   public NodeSerialization(byte[] b) {
/* 26 */     this.json = b;
/*    */   }
/*    */   protected Object readResolve() {
/*    */     try {
/* 30 */       return InternalNodeMapper.bytesToNode(this.json);
/* 31 */     } catch (IOException e) {
/* 32 */       throw new IllegalArgumentException("Failed to JDK deserialize `JsonNode` value: " + e.getMessage(), e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static NodeSerialization from(Object o) {
/*    */     try {
/* 38 */       return new NodeSerialization(InternalNodeMapper.valueToBytes(o));
/* 39 */     } catch (IOException e) {
/* 40 */       throw new IllegalArgumentException("Failed to JDK serialize `" + o.getClass().getSimpleName() + "` value: " + e.getMessage(), e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeExternal(ObjectOutput out) throws IOException {
/* 46 */     out.writeInt(this.json.length);
/* 47 */     out.write(this.json);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readExternal(ObjectInput in) throws IOException {
/* 52 */     int len = in.readInt();
/* 53 */     this.json = _read(in, len);
/*    */   }
/*    */ 
/*    */   
/*    */   private byte[] _read(ObjectInput in, int expLen) throws IOException {
/* 58 */     if (expLen <= 100000) {
/* 59 */       byte[] result = new byte[expLen];
/* 60 */       in.readFully(result, 0, expLen);
/* 61 */       return result;
/*    */     } 
/*    */ 
/*    */     
/* 65 */     ByteArrayBuilder bb = new ByteArrayBuilder(100000); try {
/* 66 */       byte[] buffer = bb.resetAndGetFirstSegment();
/* 67 */       int outOffset = 0;
/*    */       while (true) {
/* 69 */         int toRead = Math.min(buffer.length - outOffset, expLen);
/* 70 */         in.readFully(buffer, 0, toRead);
/* 71 */         expLen -= toRead;
/* 72 */         outOffset += toRead;
/*    */         
/* 74 */         if (expLen == 0) {
/* 75 */           byte[] arrayOfByte = bb.completeAndCoalesce(outOffset);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 83 */           bb.close();
/*    */           return arrayOfByte;
/*    */         } 
/*    */         if (outOffset == buffer.length) {
/*    */           buffer = bb.finishCurrentSegment();
/*    */           outOffset = 0;
/*    */         } 
/*    */       } 
/*    */     } catch (Throwable throwable) {
/*    */       try {
/*    */         bb.close();
/*    */       } catch (Throwable throwable1) {
/*    */         throwable.addSuppressed(throwable1);
/*    */       } 
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/fasterxml/jackson/databind/node/NodeSerialization.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */