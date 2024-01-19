package com.zclcs.cloud.core.utils;

import com.zclcs.cloud.core.bean.Tree;
import com.zclcs.cloud.core.constant.CommonCore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zclcs
 */
public class TreeUtil {

    /**
     * 构建树
     *
     * @param nodes nodes
     * @return <T> List<? extends Tree>
     */
    public static <T> List<Tree<T>> build(List<Tree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<Tree<T>> topNodes = new ArrayList<>();
        nodes.forEach(node -> {
            String parentCode = node.getParentCode();
            if (parentCode == null || CommonCore.TOP_PARENT_CODE.equals(parentCode)) {
                node.setHasParent(false);
                topNodes.add(node);
                return;
            }
            for (Tree<T> n : nodes) {
                String code = n.getCode();
                if (code != null && code.equals(parentCode)) {
                    if (n.getChildren() == null) {
                        n.initChildren();
                    }
                    n.getChildren().add(node);
                    node.setHasParent(true);
                    n.setHasChildren(true);
                    n.setHasParent(false);
                    return;
                }
            }
            if (topNodes.isEmpty()) {
                topNodes.add(node);
            }
        });
        return topNodes;
    }
}