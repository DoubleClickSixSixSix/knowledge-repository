package org.yohe.learning.quartz.domain;

import lombok.Data;
import org.quartz.Job;

import java.util.Map;

/**
 * @author yuhang.zhang
 * @project knowledge-repository
 * @create 2019-03-19 下午2:15
 * @desc 调度任务实体类，可继承
 **/
@Data
public class ScheduleJob {

    /**
     * 任务的Id
     */
    private String jobId;

    /**
     * 任务描述
     */
    private String jobName;

    /**
     * 任务组名
     */
    private String jobGroup;

    /**
     * 任务状态，0：启用；1：禁用；2：已删除
     */
    private int jobStatus;

    /**
     * 定时任务运行时间表达式
     */
    private String cronExpression;

    /**
     * 异步的执行任务类
     */
    private Class<? extends Job> jobExecuteClass;

    /**
     * 定时器携带的数据
     */
    private Map<? extends String, ?> jobData;
}
