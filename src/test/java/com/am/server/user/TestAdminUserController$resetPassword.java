package com.am.server.user;

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

/**
 * 用户管理列表测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestAdminUserController$resetPassword {
    public static final String URL = Constant.ADMIN_ROOT + "/user/reset/password";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestConfig testConfig;

    @Resource(name = "message")
    private Message<MessageVO> message;

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
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", testConfig.getUid())
        )
                .andExpect(MockMvcResultMatchers.status().is(401))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("exception.noToken").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }
    // 1、不传id

    /**
     * 不传id
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void idNull() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("common.operate.primaryKey.null").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * id blank
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void idBlank() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("id", "")
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("common.operate.primaryKey.null").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }


    // 2、id不为数字

    /**
     * id不为数字
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void idNotNumber() throws Exception {
        String methodName = "resetPassword";
        String parameterName = "id";
        String parameterType = "Long";
        String actualParameterType = "String";
        String actualParameterValue = "asd";
        MessageVO messageVO = message.get("exception.parameter.type");
        messageVO.setMessage(
                String.format(messageVO.getMessage(),
                        methodName,
                        parameterName,
                        parameterType,
                        actualParameterType,
                        actualParameterValue)
        );
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("id", actualParameterValue)
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(messageVO.getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }
    // 3、id不存在

    /**
     * 此id数据不存在
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void idNotExist() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("id", "-1")
        )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 正常
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void test() throws Exception {
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .param("id", "1266919488381652992")
        )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("user.password.reset.success").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }
}
