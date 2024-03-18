package com.zclcs.platform.system.handler.dict;

import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.platform.system.service.DictItemService;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DictTextQueryHandler implements Handler<RoutingContext> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final DictItemService dictItemService;

    public DictTextQueryHandler(DictItemService dictItemService) {
        this.dictItemService = dictItemService;
    }

    @Override
    public void handle(RoutingContext ctx) {
        String dictName = ctx.request().getParam("dictName");
        String value = ctx.request().getParam("value");
        dictItemService.findDictCacheByDictNameAndValue(dictName, value)
                .onComplete(dictItems -> {
                    RoutingContextUtil.success(ctx, dictItems);
                }, e -> {
                    log.error("获取字典失败", e);
                    RoutingContextUtil.error(ctx, "获取字典失败");
                })
        ;
    }
}
