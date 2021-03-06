/*
 * Copyright 2017-2018, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.operator.topic;

import io.vertx.core.Handler;

/**
 * ZooKeeper watcher for child znodes of {@code /configs/topics},
 * calling {@link TopicOperator#onTopicConfigChanged(LogContext, TopicName, Handler)}
 * for changed children.
 */
class TopicConfigsWatcher extends ZkWatcher {

    private static final String CONFIGS_ZNODE = "/config/topics";

    TopicConfigsWatcher(TopicOperator topicOperator) {
        super(topicOperator, CONFIGS_ZNODE);
    }

    @Override
    protected void notifyOperator(String child) {
        LogContext logContext = LogContext.zkWatch(CONFIGS_ZNODE, "=" + child);
        log.info("{}: Config change {}: topic {}", logContext);
        topicOperator.onTopicConfigChanged(logContext, new TopicName(child), ar2 -> {
            log.info("{} Reconciliation result due to topic config change on topic {}: {}", logContext, child, ar2);
        });
    }
}
