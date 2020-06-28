package com.am.server.user.login;

import cn.hutool.core.codec.Base64;
import com.am.server.common.constant.Constant;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * 登录方法测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestLoginController$login {
    public static final String URL = Constant.ADMIN_ROOT + "/login";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;


    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    public static final String USERNAME = "admin";
    public static final String PASSWORD = "MTIzNDU2";

    /**
     * 不输入用户名
     *
     * @throws Exception Exception
     */
    @Rollback
    @Test
    public void testNoUserName() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "");
        jsonObject.put("password", PASSWORD);
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .content(jsonObject.toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("用户名不能为空"));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 不输入密码
     *
     * @throws Exception Exception
     */
    @Rollback
    @Test
    public void testNoPassword() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", USERNAME);
        jsonObject.put("password", "");
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .content(jsonObject.toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("密码不能为空"));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 用户名不正确
     *
     * @throws Exception Exception
     */
    @Rollback
    @Test
    public void testUsernameNotExit() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", USERNAME + 1);
        jsonObject.put("password", PASSWORD);
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .content(jsonObject.toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().is(406))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("用户名或密码不正确"));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 用户名不正确
     *
     * @throws Exception Exception
     */
    @Rollback
    @Test
    public void testPasswordError() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", USERNAME);
        jsonObject.put("password", Base64.encode("111111".getBytes()));
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .content(jsonObject.toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().is(406))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("用户名或密码不正确"));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 用户名不正确
     *
     * @throws Exception Exception
     */
    @Rollback
    @Test
    public void testLoginSuccess() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", USERNAME);
        jsonObject.put("password", PASSWORD);
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .content(jsonObject.toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists());

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }
}
