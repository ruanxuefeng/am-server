package com.am.server.user.login;

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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

/**
 * 获取登录用户信息方法测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestLoginController$info {

    public static final String URL = Constant.ADMIN_ROOT + "/user/info";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestConfig testConfig;

    @Resource(name = "message")
    protected Message<MessageVO> message;

    private MockMvc mockMvc;


    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    /**
     * 获取登录用户信息,没有token
     *
     * @throws Exception Exception
     */
    @Test
    public void testNoToken() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(URL)
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
     * 获取登录用户信息
     *
     * @throws Exception Exception
     */
    @Test
    public void test() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
        )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.roles").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.permissions").exists());

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }
}
