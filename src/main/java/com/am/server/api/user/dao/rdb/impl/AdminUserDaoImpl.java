package com.am.server.api.user.dao.rdb.impl;

import com.am.server.api.user.dao.rdb.AdminUserDao;
import com.am.server.api.user.pojo.ao.AdminUserListAo;
import com.am.server.api.user.pojo.po.AdminUserDo;
import com.am.server.api.user.repository.AdminUserRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author 阮雪峰
 * @date 2020年5月17日17:43:21
 */
@Service
public class AdminUserDaoImpl implements AdminUserDao {

    private final AdminUserRepository adminUserRepository;

    public AdminUserDaoImpl(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public Optional<AdminUserDo> findByEmail(String email) {
        return adminUserRepository.findByEmail(email);
    }

    @Override
    public Optional<AdminUserDo> findByIdNotAndEmail(Long id, String email) {
        return adminUserRepository.findByIdNotAndEmail(id, email);
    }

    @Override
    public Optional<AdminUserDo> findByUsername(String username) {
        return adminUserRepository.findByUsername(username);
    }

    @Override
    public Optional<AdminUserDo> findByIdNotAndUsername(Long id, String username) {
        return adminUserRepository.findByIdNotAndUsername(id, username);
    }

    @Override
    public Optional<AdminUserDo> findById(Long id) {
        return adminUserRepository.findById(id);
    }

    @Override
    public Page<AdminUserDo> findAll(AdminUserListAo listQuery) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                .withIgnoreNullValues()
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<AdminUserDo> example = Example.of(new AdminUserDo().setName(listQuery.getName()).setEmail(listQuery.getEmail()).setUsername(listQuery.getUsername()), matcher);
        return adminUserRepository.findAll(example, PageRequest.of(listQuery.getPage() - 1, listQuery.getPageSize()));
    }

    @Override
    public void save(AdminUserDo user) {
        adminUserRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        adminUserRepository.deleteById(id);
    }
}
