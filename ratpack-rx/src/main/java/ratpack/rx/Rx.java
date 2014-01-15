/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ratpack.rx;

import rx.Observable;

import java.util.concurrent.Callable;

public abstract class Rx {

  private static RxBackground rxBackground;

  public static void setRxBackground(RxBackground rxBackground) {
    Rx.rxBackground = rxBackground;
  }

  public static <T> Observable<T> rxBackground(Callable<T> callable) {
    if (rxBackground == null) {
      throw new IllegalStateException("Not initialized. Did you register the RxModule?");
    } else {
      return rxBackground.exec(callable);
    }
  }

}