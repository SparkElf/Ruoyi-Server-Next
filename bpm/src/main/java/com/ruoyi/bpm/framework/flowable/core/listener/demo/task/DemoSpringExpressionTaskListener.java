package com.ruoyi.bpm.framework.flowable.core.listener.demo.task;

import lombok.extern.slf4j.Slf4j;
import org.flowable.task.service.delegate.DelegateTask;

/**
 * 类型为 expression 的 TaskListener 监听器示例
 *
 * @author 芋道源码
 */
@Slf4j
public class DemoSpringExpressionTaskListener {

    public void notify(DelegateTask delegateTask) {
        log.info("[execute][task({}) 被调用]", delegateTask.getId());
    }

}