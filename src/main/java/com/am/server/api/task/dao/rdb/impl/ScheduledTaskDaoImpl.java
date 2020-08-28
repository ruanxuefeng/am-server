package com.am.server.api.task.dao.rdb.impl;

import com.am.server.api.task.dao.rdb.ScheduledTaskDao;
import com.am.server.api.task.enumerate.ScheduledTaskStatus;
import com.am.server.api.task.pojo.ao.ScheduledTaskListAo;
import com.am.server.api.task.pojo.po.ScheduledTaskDo;
import com.am.server.api.task.repository.ScheduledTaskRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author 阮雪峰
 * @date 2020年5月17日17:58:31
 */
@Service
public class ScheduledTaskDaoImpl implements ScheduledTaskDao {
    private final ScheduledTaskRepository scheduledTaskRepository;

    public ScheduledTaskDaoImpl(ScheduledTaskRepository scheduledTaskRepository) {
        this.scheduledTaskRepository = scheduledTaskRepository;
    }

    @Override
    public List<ScheduledTaskDo> findAllByStatus(ScheduledTaskStatus status) {
        return scheduledTaskRepository.findAllByStatus(status);
    }

    @Override
    public void save(ScheduledTaskDo scheduledTask) {
        scheduledTaskRepository.save(scheduledTask);
    }

    @Override
    public Page<ScheduledTaskDo> findAll(ScheduledTaskListAo scheduledTaskListAo) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                .withIgnoreNullValues()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());

        Example<ScheduledTaskDo> example = Example.of(new ScheduledTaskDo().setName(scheduledTaskListAo.getName()), matcher);

        return scheduledTaskRepository.findAll(example, PageRequest.of(scheduledTaskListAo.getPage() - 1, scheduledTaskListAo.getPageSize()));
    }

    @Override
    public Optional<ScheduledTaskDo> findById(Long id) {
        return scheduledTaskRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        scheduledTaskRepository.deleteById(id);
    }

    @Override
    public void update(ScheduledTaskDo scheduledTask) {
        scheduledTaskRepository.saveAndFlush(scheduledTask);
    }
}
