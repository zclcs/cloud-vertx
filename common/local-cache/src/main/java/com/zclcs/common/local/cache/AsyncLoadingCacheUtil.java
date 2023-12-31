package com.zclcs.common.local.cache;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.reactivex.rxjava3.core.Maybe;
import io.vertx.core.Context;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * @author zclcs
 */
public class AsyncLoadingCacheUtil {

    public static <T, R> AsyncLoadingCache<T, R> buildAsyncLoadingCache(Context context, Function<T, Maybe<R>> future) {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(30))
                .recordStats()
                .executor(cmd -> context.runOnContext(v -> cmd.run()))
                .buildAsync((key, exec) -> CompletableFuture.supplyAsync(() ->
                        future.apply(key).toCompletionStage(), exec).thenComposeAsync(Function.identity(), exec));
    }
}
