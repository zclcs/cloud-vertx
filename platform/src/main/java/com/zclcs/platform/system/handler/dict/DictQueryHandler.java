package com.zclcs.platform.system.handler.dict;

import com.zclcs.cloud.lib.web.utils.RoutingContextUtil;
import com.zclcs.common.core.utils.StringsUtil;
import com.zclcs.platform.system.service.DictItemService;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DictQueryHandler implements Handler<RoutingContext> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final DictItemService dictItemService;

    public DictQueryHandler(DictItemService dictItemService) {
        this.dictItemService = dictItemService;
    }

    @Override
    public void handle(RoutingContext ctx) {
        String dictName = ctx.request().getParam("dictName");
        String parentValue = ctx.request().getParam("parentValue");
        if (StringsUtil.isNotBlank(parentValue)) {
            dictItemService.findDictCacheByDictNameAndParentValue(dictName, parentValue)
                    .onComplete(dictItems -> {
                        RoutingContextUtil.success(ctx, dictItems);
                    }, e -> {
                        log.error("获取字典失败", e);
                        RoutingContextUtil.error(ctx, "获取字典失败");
                    })
            ;
        } else {
            dictItemService.findDictCacheByDictName(dictName)
                    .onComplete(dictItems -> {
                        RoutingContextUtil.success(ctx, dictItems);
                    }, e -> {
                        log.error("获取字典失败", e);
                        RoutingContextUtil.error(ctx, "获取字典失败");
                    })
            ;
        }
    }
}
