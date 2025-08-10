package io.reactivex.internal.fuseable;

public interface QueueFuseable<T> extends SimpleQueue<T> {
  public static final int NONE = 0;
  
  public static final int SYNC = 1;
  
  public static final int ASYNC = 2;
  
  public static final int ANY = 3;
  
  public static final int BOUNDARY = 4;
  
  int requestFusion(int paramInt);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/fuseable/QueueFuseable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */