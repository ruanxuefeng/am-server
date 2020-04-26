package com.am.server.api.user.service.impl;

import com.am.server.api.role.dao.rdb.RoleDao;
import com.am.server.api.role.pojo.po.RoleDo;
import com.am.server.api.upload.enumerate.FileType;
import com.am.server.api.upload.service.SysFileService;
import com.am.server.api.user.dao.rdb.AdminUserDao;
import com.am.server.api.user.exception.NoPermissionAccessException;
import com.am.server.api.user.exception.PasswordErrorException;
import com.am.server.api.user.exception.UserNotExistException;
import com.am.server.api.user.pojo.ao.*;
import com.am.server.api.user.pojo.po.AdminUserDo;
import com.am.server.api.user.pojo.vo.AdminUserListVo;
import com.am.server.api.user.pojo.vo.LoginUserInfoVo;
import com.am.server.api.user.pojo.vo.UserInfoVo;
import com.am.server.api.user.service.AdminUserService;
import com.am.server.api.user.service.UserPermissionCacheService;
import com.am.server.common.annotation.transaction.Commit;
import com.am.server.common.annotation.transaction.ReadOnly;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.base.service.CommonService;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.DesUtils;
import com.am.server.common.util.JwtUtils;
import com.am.server.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 阮雪峰
 * @date 2018/7/24 17:23
 */
@Slf4j
@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService {

    /**
     * 初始密码
     */
    private static final String PASSWORD = "123456";

    /**
     * 密码加密key的长度，
     */
    private static final int RANDOM_STRING_LENGTH = 512;

    private final AdminUserDao adminuserDao;

    private final UserPermissionCacheService userPermissionCacheService;

    private final SysFileService sysFileService;

    private final CommonService commonService;

    private final RoleDao roleDao;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public AdminUserServiceImpl(UserPermissionCacheService userPermissionCacheService, AdminUserDao adminuserDao, SysFileService sysFileService, CommonService commonService, RoleDao roleDao, SimpMessagingTemplate simpMessagingTemplate) {
        this.userPermissionCacheService = userPermissionCacheService;
        this.adminuserDao = adminuserDao;
        this.sysFileService = sysFileService;
        this.commonService = commonService;
        this.roleDao = roleDao;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public LoginUserInfoVo login(LoginAo query) {
        Optional<AdminUserDo> optional = adminuserDao.findByUsername(query.getUsername());
        optional.orElseThrow(UserNotExistException::new);

        return optional
                .filter(user -> {
                    //校验密码是否一样
                    String inputPassword = new String(Base64.decodeBase64(query.getPassword()));
                    try {
                        String userPassword = DesUtils.decrypt(user.getPassword(), user.getSalt());
                        return inputPassword.equals(userPassword);
                    } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
                        log.error("解密失败，密文：{}，秘钥：{}", user.getPassword(), user.getSalt());
                        log.error("", e);
                    }
                    return false;
                })
                .map(user -> {
                    Set<String> permissions = userPermissionCacheService.get(user.getId());
                    if (permissions != null && permissions.size() > 0) {
                        return new LoginUserInfoVo(JwtUtils.sign(user.getId().toString()));
                    }else {
                        throw new NoPermissionAccessException();
                    }

                })
                .orElseThrow(PasswordErrorException::new);
    }

    @ReadOnly
    @Override
    public UserInfoVo info(Long id) {
        return adminuserDao.findById(id)
                .map(user -> {
                    List<String> roles = user.getRoles().stream().map(RoleDo::getName).collect(Collectors.toList());
                    String avatar = StringUtils.isEmpty(user.getAvatar()) ? "" : user.getAvatar().getUrl();
                    return new UserInfoVo()
                            .setId(user.getId())
                            .setPermissions(userPermissionCacheService.get(id))
                            .setAvatar(avatar)
                            .setEmail(user.getEmail())
                            .setGender(user.getGender())
                            .setName(user.getName())
                            .setRoles(roles);
                })
                .orElseThrow(UserNotExistException::new);
    }

    @ReadOnly
    @Override
    public PageVO<AdminUserListVo> list(AdminUserListAo listQuery) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                .withIgnoreNullValues()
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<AdminUserDo> example = Example.of(new AdminUserDo().setName(listQuery.getName()).setEmail(listQuery.getEmail()).setUsername(listQuery.getUsername()), matcher);
        Page<AdminUserDo> page = adminuserDao.findAll(example, PageRequest.of(listQuery.getPage() - 1, listQuery.getPageSize()));
        return new PageVO<AdminUserListVo>()
                .setPageSize(listQuery.getPageSize())
                .setPage(listQuery.getPage())
                .setTotal(page.getNumber())
                .setRows(page.getContent().stream()
                        .map(adminUser -> {
                                    String avatar = StringUtils.isEmpty(adminUser.getAvatar()) ? "" : adminUser.getAvatar().getUrl();
                                    return new AdminUserListVo()
                                            .setAvatar(avatar)
                                            .setCreatorName(Optional.ofNullable(adminUser.getCreatedBy()).map(AdminUserDo::getName).orElse(""))
                                            .setEmail(adminUser.getEmail())
                                            .setGender(adminUser.getGender())
                                            .setName(adminUser.getName())
                                            .setId(adminUser.getId())
                                            .setUsername(adminUser.getUsername())
                                            .setCreateTime(adminUser.getCreatedTime());
                                }
                        )
                        .collect(Collectors.toList()));
    }

    @Commit
    @Override
    public void save(SaveAdminUserAo adminUser) {
        String salt = StringUtils.getRandomStr(RANDOM_STRING_LENGTH);
        try {
            AdminUserDo user = new AdminUserDo()
                    .setUsername(adminUser.getUsername())
                    .setName(adminUser.getName())
                    .setEmail(adminUser.getEmail())
                    .setGender(adminUser.getGender())
                    .setAvatar(sysFileService.save(adminUser.getImg(), FileType.avatar))
                    .setSalt(salt)
                    .setPassword(DesUtils.encrypt(Constant.INITIAL_PASSWORD, salt));
            commonService.beforeSave(user);
            adminuserDao.save(user);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            log.error("解密失败，密文：{}，秘钥：{}", Constant.INITIAL_PASSWORD, salt);
            log.error("", e);
        }

    }

    @Commit
    @Override
    public void delete(Long id) {
        adminuserDao.deleteById(id);
        userPermissionCacheService.remove(id);
    }

    @ReadOnly
    @Override
    public Boolean isEmailExist(String email) {
        return adminuserDao.findByEmail(email).isPresent();
    }

    @Commit
    @Override
    public void resetPassword(Long id) {
        String salt = StringUtils.getRandomStr(RANDOM_STRING_LENGTH);
        try {
            String password = DesUtils.encrypt(PASSWORD, salt);

            adminuserDao.findById(id)
                    .ifPresent(adminUser -> {
                        commonService.beforeSave(adminUser);
                        adminuserDao.save(adminUser.setPassword(password).setSalt(salt));
                    });
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            log.error("加密异常，Text：{}，秘钥：{}", PASSWORD, salt);
        }
    }

    @Commit
    @Override
    public void updateRole(Long id, List<Long> roleIdList) {
        adminuserDao.findById(id)
                .ifPresent(adminUser -> {
                    commonService.beforeSave(adminUser);
                    if (!roleIdList.isEmpty()) {
                        adminUser.setRoles(roleDao.findAllById(roleIdList));
                    }else {
                        adminUser.setRoles(Collections.emptyList());
                    }
                    adminuserDao.save(adminUser);
                    userPermissionCacheService.remove(id);
                    simpMessagingTemplate.convertAndSend("/topic/permission/" + id, true);
                });
    }

    @Commit
    @Override
    public void update(UpdateAdminUserAo user) {
        adminuserDao.findById(user.getId())
                .ifPresent(adminUser -> {
                    if (user.getImg() != null && !user.getImg().isEmpty()) {
                        sysFileService.updateFileContent(user.getImg(), adminUser.getAvatar());
                    }
                    adminUser.setUsername(user.getUsername())
                            .setName(user.getName())
                            .setEmail(user.getEmail())
                            .setGender(user.getGender());

                    commonService.beforeSave(adminUser);
                    adminuserDao.save(adminUser);
                });
    }

    @Override
    public void update(UpdateUserInfoAo user) {
        adminuserDao.findById(user.getId())
                .ifPresent(adminUser -> {
                    if (user.getImg() != null && !user.getImg().isEmpty()) {
                        sysFileService.updateFileContent(user.getImg(), adminUser.getAvatar());
                    }
                    adminUser.setName(user.getName())
                            .setEmail(user.getEmail())
                            .setGender(user.getGender());

                    commonService.beforeSave(adminUser);
                    adminuserDao.save(adminUser);
                });
    }


    @Override
    public List<Long> findRoleIdList(Long id) {
        return adminuserDao.findById(id)
                .map(AdminUserDo::getRoles)
                .map(roles -> {
                    List<Long> list = new ArrayList<>(roles.size());
                    roles.forEach(role -> list.add(role.getId()));
                    return list;
                }).orElse(new ArrayList<>());
    }

    @Override
    public Boolean isUsernameExist(String username) {
        return adminuserDao.findByUsername(username).isPresent();
    }

    @Override
    public Boolean isEmailExistWithId(String email, Long id) {
        return adminuserDao.findByIdNotAndEmail(id, email).isPresent();
    }

    @Override
    public Boolean isUsernameExistWithId(String username, Long id) {
        return adminuserDao.findByIdNotAndUsername(id, username).isPresent();
    }
}
