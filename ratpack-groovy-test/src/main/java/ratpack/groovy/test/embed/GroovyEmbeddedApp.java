/*
 * Copyright 2013 the original author or authors.
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

package ratpack.groovy.test.embed;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import ratpack.groovy.Groovy;
import ratpack.groovy.handling.GroovyChain;
import ratpack.groovy.handling.GroovyContext;
import ratpack.groovy.internal.ClosureUtil;
import ratpack.groovy.server.GroovyRatpackServerSpec;
import ratpack.groovy.test.embed.internal.DefaultGroovyEmbeddedApp;
import ratpack.server.ServerConfig;
import ratpack.server.ServerConfigBuilder;
import ratpack.test.embed.EmbeddedApp;
import ratpack.test.embed.EmbeddedBaseDir;
import ratpack.test.http.TestHttpClient;

/**
 * A more Groovy version of {@link EmbeddedApp}.
 *
 * <pre class="tested">
 * import ratpack.groovy.test.embed.GroovyEmbeddedApp
 *
 * GroovyEmbeddedApp.of {
 *   handlers {
 *     get {
 *       render "root"
 *     }
 *   }
 * } test {
 *   assert getText() == "root"
 * }
 * </pre>
 *
 * @see EmbeddedBaseDir
 * @see EmbeddedApp
 */
public interface GroovyEmbeddedApp extends EmbeddedApp {

  static GroovyEmbeddedApp from(EmbeddedApp embeddedApp) {
    return embeddedApp instanceof GroovyEmbeddedApp ? (GroovyEmbeddedApp) embeddedApp : new DefaultGroovyEmbeddedApp(embeddedApp);
  }

  static GroovyEmbeddedApp of(@DelegatesTo(value = GroovyRatpackServerSpec.class, strategy = Closure.DELEGATE_FIRST) Closure<?> definition) throws Exception {
    return from(EmbeddedApp.of(s -> ClosureUtil.configureDelegateFirst(GroovyRatpackServerSpec.from(s), definition)));
  }

  static GroovyEmbeddedApp fromServer(ServerConfigBuilder serverConfig, @DelegatesTo(value = GroovyRatpackServerSpec.class, strategy = Closure.DELEGATE_FIRST) Closure<?> definition) {
    return from(EmbeddedApp.fromServer(serverConfig.build(), s -> ClosureUtil.configureDelegateFirst(GroovyRatpackServerSpec.from(s), definition)));
  }

  static GroovyEmbeddedApp fromServer(ServerConfig serverConfig, @DelegatesTo(value = GroovyRatpackServerSpec.class, strategy = Closure.DELEGATE_FIRST) Closure<?> definition) {
    return from(EmbeddedApp.fromServer(serverConfig, s -> ClosureUtil.configureDelegateFirst(GroovyRatpackServerSpec.from(s), definition)));
  }

  static GroovyEmbeddedApp fromHandler(@DelegatesTo(value = GroovyContext.class, strategy = Closure.DELEGATE_FIRST) Closure<?> handler) {
    return from(EmbeddedApp.fromHandler(Groovy.groovyHandler(handler)));
  }

  static GroovyEmbeddedApp fromHandlers(@DelegatesTo(value = GroovyChain.class, strategy = Closure.DELEGATE_FIRST) Closure<?> handlers) {
    return from(EmbeddedApp.fromHandlers(Groovy.chainAction(handlers)));
  }

  default void test(@DelegatesTo(value = TestHttpClient.class, strategy = Closure.DELEGATE_FIRST) Closure<?> test) throws Exception {
    test(ClosureUtil.delegatingAction(TestHttpClient.class, test));
  }

}
