/*    */ package br.jus.cnj.pje.office.core.imp;
/*    */ 
/*    */ import br.jus.cnj.pje.office.core.IPjeResponse;
/*    */ import com.github.utils4j.IConstants;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.http.ContentType;
/*    */ import org.json.JSONObject;
/*    */ 
/*    */ public class PjeJsonTaskResponse
/*    */   extends PjeTaskResponse
/*    */ {
/*    */   private final JSONObject json;
/*    */   
/*    */   public static PjeJsonTaskResponse success(JSONObject json) {
/* 16 */     return new PjeJsonTaskResponse(json, true);
/*    */   }
/*    */   
/*    */   public static PjeJsonTaskResponse fail(JSONObject json) {
/* 20 */     return new PjeJsonTaskResponse(json, false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private PjeJsonTaskResponse(JSONObject json, boolean success) {
/* 26 */     super(success);
/* 27 */     this.json = ((JSONObject)Args.requireNonNull(json, "json is null")).put("success", success);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doProcessResponse(IPjeResponse response) throws IOException {
/* 32 */     response.write(this.json.toString().getBytes(IConstants.UTF_8), ContentType.APPLICATION_JSON.toString());
/* 33 */     response.flush();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/br/jus/cnj/pje/office/core/imp/PjeJsonTaskResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */