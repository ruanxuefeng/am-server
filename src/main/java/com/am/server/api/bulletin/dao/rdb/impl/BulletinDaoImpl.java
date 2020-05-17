package com.am.server.api.bulletin.dao.rdb.impl;

import com.am.server.api.bulletin.dao.rdb.BulletinDao;
import com.am.server.api.bulletin.pojo.Status;
import com.am.server.api.bulletin.pojo.ao.BulletinListAo;
import com.am.server.api.bulletin.pojo.po.BulletinDo;
import com.am.server.api.bulletin.repository.BulletinRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author 阮雪峰
 * @date 2020年5月17日18:33:08
 */
@Service
public class BulletinDaoImpl implements BulletinDao {
    private final BulletinRepository bulletinRepository;

    public BulletinDaoImpl(BulletinRepository bulletinRepository) {
        this.bulletinRepository = bulletinRepository;
    }

    @Override
    public Page<BulletinDo> findAll(BulletinListAo bulletinAo) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                .withMatcher("content", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<BulletinDo> example = Example.of(new BulletinDo().setContent(bulletinAo.getContent()), matcher);

        return bulletinRepository.findAll(example, PageRequest.of(bulletinAo.getPage() - 1, bulletinAo.getPageSize()));
    }

    @Override
    public List<BulletinDo> findByStatusOrderByIdDesc(Status status) {
        return bulletinRepository.findByStatusOrderByIdDesc(status);
    }

    @Override
    public void save(BulletinDo bulletinDo) {
        bulletinRepository.save(bulletinDo);
    }

    @Override
    public Optional<BulletinDo> findById(Long id) {
        return bulletinRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        bulletinRepository.deleteById(id);
    }
}
