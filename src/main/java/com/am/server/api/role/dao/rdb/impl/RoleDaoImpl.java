package com.am.server.api.role.dao.rdb.impl;

import com.am.server.api.role.dao.rdb.RoleDao;
import com.am.server.api.role.pojo.ao.RoleListAo;
import com.am.server.api.role.pojo.po.RoleDo;
import com.am.server.api.role.repository.RoleRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author 阮雪峰
 * @date 2020年5月17日18:18:35
 */
@Service
public class RoleDaoImpl implements RoleDao {
    private final RoleRepository roleRepository;

    public RoleDaoImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<RoleDo> findAll(RoleListAo roleListAo) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<RoleDo> example = Example.of(new RoleDo().setName(roleListAo.getName()), matcher);

        return roleRepository.findAll(example, PageRequest.of(roleListAo.getPage() - 1, roleListAo.getPageSize()));
    }

    @Override
    public void save(RoleDo role) {
        roleRepository.save(role);
    }

    @Override
    public Optional<RoleDo> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public List<RoleDo> findAllOrderByIdDesc() {
        return roleRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }

    @Override
    public List<RoleDo> findAllById(List<Long> roleIdList) {
        return roleRepository.findAllById(roleIdList);
    }
}
