package io.reactivex;

import io.reactivex.annotations.NonNull;

public interface SingleTransformer<Upstream, Downstream> {
  @NonNull
  SingleSource<Downstream> apply(@NonNull Single<Upstream> paramSingle);
}


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/SingleTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */