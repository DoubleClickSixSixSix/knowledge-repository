package org.yohe.learning.quartz.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yohe.learning.quartz.ScheduleHandler;
import org.yohe.learning.quartz.domain.ScheduleJob;

/**
 * @author yuhang.zhang
 * @project knowledge-repository
 * @create 2019-03-19 下午2:19
 * @desc quartz调度处理类
 **/
@Slf4j
@Component
public class ScheduleHandlerImpl implements ScheduleHandler {

    @Autowired
    private Scheduler scheduler;

    /**
     * 加载quartz任务
     * @param scheduleJob
     */
    @Override
    public void scheduleJob(ScheduleJob scheduleJob) {
        if (scheduleJob == null) {
            return;
        }
        log.info("加载调度任务：{}", JSON.toJSONString(scheduleJob));
        if (getJobTrigger(scheduleJob.getJobId(), scheduleJob.getJobGroup()) == null) {
            addScheduleJob(scheduleJob);
        } else {
            deleteScheduleJob(scheduleJob.getJobId(), scheduleJob.getJobGroup());
            addScheduleJob(scheduleJob);
        }
    }

    /**
     * 添加任务调度
     * @param scheduleJob
     */
    private void addScheduleJob(ScheduleJob scheduleJob) {
        try {
            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(scheduleJob.getJobId(), scheduleJob.getJobGroup())
                    .withSchedule(CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression()))
                    .build();
            JobDetail jobDetail = JobBuilder.newJob(scheduleJob.getJobExecuteClass())
                    .withIdentity(scheduleJob.getJobId(), scheduleJob.getJobGroup())
                    .usingJobData(new JobDataMap(scheduleJob.getJobData()))
                    .build();
            scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (SchedulerException e) {
            log.error("加载调度任务失败：{}", JSON.toJSONString(scheduleJob), e);
        }
    }

    /**
     * 删除任务调度
     * @param jobId
     * @param jobGroup
     */
    @Override
    public void deleteScheduleJob(String jobId, String jobGroup) {
        if (StringUtils.isEmpty(jobId) || StringUtils.isEmpty(jobGroup)) {
            return;
        }
        log.info("删除调度任务：jobId={}, jobGroup={}", jobId, jobGroup);
        if (getJobTrigger(jobId, jobGroup) != null) {
            JobKey jobKey = new JobKey(jobId, jobGroup);
            try {
                scheduler.deleteJob(jobKey);
            } catch (SchedulerException e) {
                log.error("删除调度任务失败：jobId={}, jobGroup={}", jobId, jobGroup, e);
            }
        }
    }

    /**
     * 获取jobTrigger
     * @param jobId
     * @param jobGroup
     * @return
     */
    private Trigger getJobTrigger(String jobId, String jobGroup) {
        if (StringUtils.isEmpty(jobId) || StringUtils.isEmpty(jobGroup)) {
            return null;
        }
        try {
            TriggerKey triggerKey = new TriggerKey(jobId, jobGroup);
            return scheduler.getTrigger(triggerKey);
        } catch (Exception e) {
            log.error("获取triggerKey失败，jobId={}, jobGroup={}", jobId, jobGroup, e);
            return null;
        }
    }
}
