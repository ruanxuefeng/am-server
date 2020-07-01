package com.am.server.user;

import cn.hutool.core.util.RandomUtil;
import com.am.server.common.base.enumerate.Gender;
import com.am.server.common.base.pojo.vo.MessageVO;
import com.am.server.common.base.service.Message;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.JwtUtils;
import com.am.server.config.test.TestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 用户管理列表测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestAdminUserController$update {
    public static final String URL = Constant.ADMIN_ROOT + "/user/update";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestConfig testConfig;

    @Resource(name = "message")
    private Message<MessageVO> message;

    private MockMvc mockMvc;

    private MockMultipartFile mockMultipartFile;

    @Before
    public void init() throws IOException {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        File file = new File("E:\\test1.jpg");
        InputStream inputStream = new FileInputStream(file);
        mockMultipartFile = new MockMultipartFile("img", "test1.jpg", null, inputStream);
    }

    /**
     * 没有token
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void noToken() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", testConfig.getUid())
                        .param("username", "admin1")
                        .param("name", "admin1")
                        .param("email", "admin1@am.com")
                        .param("gender", Gender.MALE.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(401))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("exception.noToken").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }
    // 1、用户名验证

    /**
     * 用户名为空
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void usernameBlank() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .file(mockMultipartFile)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("id", testConfig.getUid())
                        .param("username", "")
                        .param("name", "admin")
                        .param("email", "admin@am.com")
                        .param("gender", Gender.MALE.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("user.username.blank").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 用户名重复
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void usernameDuplicate() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .file(mockMultipartFile)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("id", testConfig.getUid())
                        .param("username", "test")
                        .param("name", "admin")
                        .param("email", "admin@am.com")
                        .param("gender", Gender.MALE.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("user.username.exist").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 用户名过长
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void usernameLong() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .file(mockMultipartFile)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("id", testConfig.getUid())
                        .param("username", RandomUtil.randomString(66))
                        .param("name", "admin")
                        .param("email", "admin@am.com")
                        .param("gender", Gender.MALE.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("user.username.long").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }
    // 2、邮箱验证

    /**
     * 邮箱为空
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void emailBlank() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .file(mockMultipartFile)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("id", testConfig.getUid())
                        .param("username", "admin")
                        .param("name", "admin")
//                        .param("email", "")
                        .param("gender", Gender.MALE.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("user.email.blank").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 邮箱格式不正确
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void emailFormat1() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .file(mockMultipartFile)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("id", testConfig.getUid())
                        .param("username", "admin")
                        .param("name", "admin")
                        .param("email", "admin")
                        .param("gender", Gender.MALE.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("user.email.format").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 邮箱格式不正确
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void emailFormat2() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .file(mockMultipartFile)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("id", testConfig.getUid())
                        .param("username", "admin")
                        .param("name", "admin")
                        .param("email", "admin@am")
                        .param("gender", Gender.MALE.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("user.email.format").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 邮箱格式不正确
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void emailFormat3() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .file(mockMultipartFile)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("id", testConfig.getUid())
                        .param("username", "admin")
                        .param("name", "admin")
                        .param("email", "admin.com")
                        .param("gender", Gender.MALE.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("user.email.format").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 邮箱已被使用
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void emailExist() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .file(mockMultipartFile)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("id", testConfig.getUid())
                        .param("username", "admin")
                        .param("name", "admin")
                        .param("email", "test@am.com")
                        .param("gender", Gender.MALE.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("user.email.exist").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }
    // 3、姓名验证

    /**
     * 姓名为空
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void nameBlank() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .file(mockMultipartFile)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("id", testConfig.getUid())
                        .param("username", "admin")
                        .param("name", "")
                        .param("email", "admin@am.com")
                        .param("gender", Gender.MALE.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("user.name.blank").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 姓名长度
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void nameLong() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .file(mockMultipartFile)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("id", testConfig.getUid())
                        .param("username", "admin")
                        .param("name", RandomUtil.randomString(66))
                        .param("email", "admin@am.com")
                        .param("gender", Gender.MALE.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("user.name.long").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }
    // 4、头像验证

    /**
     * 头像为空
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void avatarNull() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("id", testConfig.getUid())
                        .param("username", "admin")
                        .param("name", "admin")
                        .param("email", "admin@am.com")
                        .param("gender", Gender.MALE.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("user.avatar.blank").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    // 5、性别验证

    /**
     * 空
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void genderBlank() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .file(mockMultipartFile)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("id", testConfig.getUid())
                        .param("username", "admin")
                        .param("name", "admin")
                        .param("email", "admin@am.com")
                        .param("gender", "")
        )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("common.save.success").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 非枚举字段
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void genderError() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .file(mockMultipartFile)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("id", testConfig.getUid())
                        .param("username", "admin")
                        .param("name", RandomUtil.randomString(66))
                        .param("email", "admin@am.com")
                        .param("gender", "未知")
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("common.default.message").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }
    // 6、正常数据

    /**
     * 正常数据
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void test() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .file(mockMultipartFile)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("id", testConfig.getUid())
                        .param("username", "admin")
                        .param("name", "admin")
                        .param("email", "admin@am.com")
                        .param("gender", Gender.MALE.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("common.save.success").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }
}
