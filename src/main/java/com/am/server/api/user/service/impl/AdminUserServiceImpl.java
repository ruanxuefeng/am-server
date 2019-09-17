package com.am.server.api.user.service.impl;

import com.am.server.api.role.pojo.po.RoleDO;
import com.am.server.api.upload.service.FileUploadService;
import com.am.server.api.user.dao.rdb.AdminUserDAO;
import com.am.server.api.user.exception.PasswordErrorException;
import com.am.server.api.user.exception.UserNotExistException;
import com.am.server.api.user.pojo.ao.*;
import com.am.server.api.user.pojo.po.AdminUserDO;
import com.am.server.api.user.pojo.vo.AdminUserListVO;
import com.am.server.api.user.pojo.vo.LoginUserInfoVO;
import com.am.server.api.user.pojo.vo.UserInfoVO;
import com.am.server.api.user.service.AdminUserService;
import com.am.server.api.user.service.UserPermissionCacheService;
import com.am.server.common.annotation.transaction.Commit;
import com.am.server.common.annotation.transaction.ReadOnly;
import com.am.server.common.base.pojo.vo.PageVO;
import com.am.server.common.base.service.CommonService;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 阮雪峰
 * @date 2018/7/24 17:23
 */
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
    /**
     * 存储头像的相对路径
     */
    private static final String AVATAR_FOLDER = "/avatar/";

    private final AdminUserDAO adminUserDAO;

    private final UserPermissionCacheService userPermissionCacheService;

    private final FileUploadService fileUploadService;

    private final CommonService commonService;

    public AdminUserServiceImpl(UserPermissionCacheService userPermissionCacheService, AdminUserDAO adminUserDAO, FileUploadService fileUploadService, CommonService commonService) {
        this.userPermissionCacheService = userPermissionCacheService;
        this.adminUserDAO = adminUserDAO;
        this.fileUploadService = fileUploadService;
        this.commonService = commonService;
    }

    @Override
    public LoginUserInfoVO login(LoginAO query) {
        Optional<AdminUserDO> optional = adminUserDAO.findByUsername(query.getUsername());
        optional.orElseThrow(UserNotExistException::new);

        return optional
                .filter(user -> {
                    //校验密码是否一样
                    String inputPassword = new String(Base64.getDecoder().decode(query.getPassword().getBytes()));
                    String userPassword = DesUtils.decrypt(user.getPassword(), user.getSalt());
                    return inputPassword.equals(userPassword);
                })
                .map(user -> new LoginUserInfoVO(JwtUtils.sign(user.getId().toString()), null))
                .orElseThrow(PasswordErrorException::new);
    }

    @ReadOnly
    @Override
    public UserInfoVO info(Long id) {
        return adminUserDAO.findById(id)
                .map(user -> {
                    List<String> roles = user.getRoles().stream().map(RoleDO::getName).collect(Collectors.toList());
                    return new UserInfoVO()
                            .setMenus(userPermissionCacheService.get(id))
                            .setAvatar(user.getAvatar())
                            .setEmail(user.getEmail())
                            .setGender(user.getGender())
                            .setName(user.getName())
                            .setRoles(roles);
                })
                .orElse(null);
    }

    @ReadOnly
    @Override
    public PageVO<AdminUserListVO> list(AdminUserListAO listQuery) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
                .withIgnoreNullValues()
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<AdminUserDO> example = Example.of(new AdminUserDO().setName(listQuery.getName()).setEmail(listQuery.getEmail()).setUsername(listQuery.getUsername()), matcher);
        Page<AdminUserDO> page = adminUserDAO.findAll(example, PageRequest.of(listQuery.getPage() - 1, listQuery.getPageSize()));
        return new PageVO<AdminUserListVO>()
                .setPageSize(listQuery.getPageSize())
                .setPage(listQuery.getPage())
                .setTotal(page.getNumber())
                .setRows(page.getContent().stream()
                        .map(adminUser -> new AdminUserListVO()
                                .setAvatar(adminUser.getAvatar())
                                .setCreatorName(Optional.ofNullable(adminUser.getCreatedBy()).map(AdminUserDO::getName).orElse(""))
                                .setEmail(adminUser.getEmail())
                                .setGender(adminUser.getGender())
                                .setName(adminUser.getName())
                                .setId(adminUser.getId())
                                .setUsername(adminUser.getUsername())
                                .setCreateTime(adminUser.getCreatedTime())
                        )
                        .collect(Collectors.toList()));
    }

    @Commit
    @Override
    public void save(SaveAdminUserAO adminUser) {
        String salt = StringUtils.getRandomStr(RANDOM_STRING_LENGTH);
        Long id = IdUtils.getId();
        AdminUserDO user = new AdminUserDO()
                .setId(id)
                .setUsername(adminUser.getUsername())
                .setName(adminUser.getName())
                .setCreatedTime(LocalDateTime.now())
                .setEmail(adminUser.getEmail())
                .setGender(adminUser.getGender())
                .setAvatar(uploadAvatar(id, adminUser.getImg()))
                .setCreatedBy(new AdminUserDO().setId(commonService.getLoginUserId()))
                .setSalt(salt)
                .setPassword(DesUtils.encrypt(Constant.INITIAL_PASSWORD, salt));

        adminUserDAO.save(user);
    }

    /**
     * 上传头像图片
     *
     * @param id     id
     * @param avatar 头像文件
     * @author 阮雪峰
     * @date 2019/2/18 14:31
     */
    private String uploadAvatar(Long id, MultipartFile avatar) {
        String key = AVATAR_FOLDER + id
                + FileUtils.getFileSuffix(Objects.requireNonNull(avatar.getOriginalFilename()));

        return fileUploadService.upload(avatar, key);
    }

    @Commit
    @Override
    public void delete(Long id) {
        adminUserDAO.deleteById(id);
        userPermissionCacheService.remove(id);
    }

    @ReadOnly
    @Override
    public Boolean isEmailExist(String email) {
        return adminUserDAO.findByEmail(email).isPresent();
    }

    @Commit
    @Override
    public void resetPassword(Long id) {
        String salt = StringUtils.getRandomStr(RANDOM_STRING_LENGTH);
        String password = DesUtils.encrypt(PASSWORD, salt);

        adminUserDAO.findById(id)
                .ifPresent(adminUser -> adminUserDAO.save(
                        adminUser.setPassword(password)
                                .setSalt(salt)
                ));
    }

    @Commit
    @Override
    public void updateRole(Long id, List<Long> roleIdList) {
        adminUserDAO.findById(id)
                .ifPresent(adminUser -> {
                    List<RoleDO> roles = roleIdList.stream().map(roleId -> new RoleDO().setId(roleId)).collect(Collectors.toList());
                    adminUserDAO.save(adminUser.setRoles(roles));
                    userPermissionCacheService.remove(id);
                });
    }

    @Commit
    @Override
    public void update(UpdateAdminUserAO user) {
        adminUserDAO.findById(user.getId())
                .ifPresent(adminUser -> {
                    if (user.getImg() != null && !user.getImg().isEmpty()) {
                        adminUser.setAvatar(uploadAvatar(user.getId(), user.getImg()));
                    }
                    adminUser.setUsername(user.getUsername())
                            .setName(user.getName())
                            .setEmail(user.getEmail())
                            .setGender(user.getGender());
                    adminUserDAO.save(adminUser);
                });
    }

    @Override
    public void update(UpdateUserInfoAO user) {
        adminUserDAO.findById(user.getId())
                .ifPresent(adminUser -> {
                    if (user.getImg() != null && !user.getImg().isEmpty()) {
                        adminUser.setAvatar(uploadAvatar(user.getId(), user.getImg()));
                    }
                    adminUser.setName(user.getName())
                            .setEmail(user.getEmail())
                            .setGender(user.getGender());
                    adminUserDAO.save(adminUser);
                });
    }

    @Override
    public AdminUserDO findById(Long id) {
        return adminUserDAO.findById(id).orElse(null);
    }

    @Override
    public List<Long> findRoleIdList(Long id) {
        return adminUserDAO.findById(id)
                .map(AdminUserDO::getRoles)
                .map(roles -> {
                    List<Long> list = new ArrayList<>(roles.size());
                    roles.forEach(role -> list.add(role.getId()));
                    return list;
                }).orElse(new ArrayList<>());
    }

    @Override
    public Boolean isUsernameExist(String username) {
        return adminUserDAO.findByUsername(username).isPresent();
    }

    @Override
    public Boolean isEmailExistWithId(String email, Long id) {
        return adminUserDAO.findByIdNotAndEmail(id, email).isPresent();
    }

    @Override
    public Boolean isUsernameExistWithId(String username, Long id) {
        return adminUserDAO.findByIdNotAndUsername(id, username).isPresent();
    }
}
