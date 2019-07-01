package org.yohe.learning.quartz.service;

import org.yohe.learning.quartz.domain.ScheduleJob;

/**
 * @author yuhang.zhang
 * @project knowledge-repository
 * @create 2019-03-19 下午2:14
 * @desc
 **/
public interface ScheduleHandler {

    /**
     * 加载quartz任务
     * @param scheduleJob
     */
    void scheduleJob(ScheduleJob scheduleJob);

    /**
     * 删除quartz任务
     * @param jobId
     * @param jobGroup
     */
    void deleteScheduleJob(String jobId, String jobGroup);
}
