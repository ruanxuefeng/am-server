package com.am.server.user.login;

import com.am.server.common.base.pojo.vo.MessageVO;
import com.am.server.common.base.service.Message;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.JwtUtils;
import com.am.server.config.test.TestConfig;
import org.json.JSONObject;
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
import java.io.InputStream;

/**
 * 修改登录用户信息
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestLoginController$updateUserInfo {
    public static final String URL = Constant.ADMIN_ROOT + "/user/update/info";
    public static final String DEFAULT_AVATAR = "http://localhost:9527/file/avatar/1266942450736435200.jpg?1590923560835";
    @Resource(name = "message")
    protected Message<MessageVO> message;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private TestConfig testConfig;
    private MockMvc mockMvc;


    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    /**
     * 没有token
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void noToken() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "admin");
        jsonObject.put("email", "admin@am.com");
        jsonObject.put("gender", "男");
        jsonObject.put("avatar", DEFAULT_AVATAR);

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)

        )
                .andExpect(MockMvcResultMatchers.status().is(401))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("exception.noToken").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 验证邮箱格式
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void validateEmailFormat() throws Exception {

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("name", "admin")
                        .param("email", "admi")
                        .param("gender", "男")
                        .param("avatar", DEFAULT_AVATAR)
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("user.email.format").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 验证邮箱为空
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void validateEmailBlank() throws Exception {

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("name", "admin")
                        .param("gender", "男")
                        .param("avatar", DEFAULT_AVATAR)

        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("user.email.blank").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 头像为空
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void validateAvatarNull() throws Exception {

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("name", "admin")
                        .param("gender", "男")
                        .param("email", "admin@am.com")
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("user.avatar.blank").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 修改姓名
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void validateUpdateName() throws Exception {

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("name", "admin1")
                        .param("gender", "男")
                        .param("email", "admin@am.com")
                        .param("avatar", DEFAULT_AVATAR)
        )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("common.update.success").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 修改头像
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void validateUpdateAvatar() throws Exception {
        File file = new File("E:\\test1.jpg");
        InputStream inputStream = new FileInputStream(file);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("img", "test1.jpg", null, inputStream);
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(URL)
                        .file(mockMultipartFile)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("name", "admin1")
                        .param("gender", "男")
                        .param("email", "admin@am.com")
                        .param("avatar", DEFAULT_AVATAR)
        )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("common.update.success").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }
}
