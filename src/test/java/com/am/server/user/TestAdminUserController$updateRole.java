package com.am.server.user;

import com.am.server.api.role.repository.RoleRepository;
import com.am.server.common.base.pojo.po.BaseDo;
import com.am.server.common.base.pojo.vo.MessageVO;
import com.am.server.common.base.service.Message;
import com.am.server.common.constant.Constant;
import com.am.server.common.util.JwtUtils;
import com.am.server.config.test.TestConfig;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理新增测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestAdminUserController$updateRole {
    public static final String URL = Constant.ADMIN_ROOT + "/user/update/role";

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestConfig testConfig;

    @Resource(name = "message")
    private Message<MessageVO> message;

    @Autowired
    private RoleRepository roleRepository;

    private MockMvc mockMvc;

    private List<Long> roleIdList;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        roleIdList = roleRepository.findAll()
                .stream()
                .map(BaseDo::getId)
                .collect(Collectors.toList());
    }

    /**
     * 没有token
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void noToken() throws Exception {
        JSONObject param = new JSONObject();
        param.put("id", testConfig.getUid());
        param.put("roleIdList", new JSONArray(roleIdList));

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(param.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(401))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("exception.noToken").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 没有用户主键
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void noUserId() throws Exception {
        JSONObject param = new JSONObject();
        param.put("roleIdList", new JSONArray(roleIdList));

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .content(param.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("common.operate.primaryKey.null").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 用户主键为空字符串
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void userIdBlank() throws Exception {
        JSONObject param = new JSONObject();
        param.put("id", "");
        param.put("roleIdList", new JSONArray(roleIdList));

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .content(param.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("common.operate.primaryKey.null").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 用户主键不是数字类型
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void userIdNotNumber() throws Exception {
        String parameterName = "com.am.server.api.user.pojo.ao.UpdateRoleAo[\"id\"]";
        String parameterType = "Long";
        String actualParameterType = "String";
        String actualParameterValue = "asd";
        MessageVO messageVO = message.get("exception.json.parameter.type");
        messageVO.setMessage(
                String.format(messageVO.getMessage(),
                        parameterName,
                        parameterType,
                        actualParameterType,
                        actualParameterValue)
        );

        JSONObject param = new JSONObject();
        param.put("id", "asd");
        param.put("roleIdList", new JSONArray(roleIdList));

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .content(param.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(messageVO.getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 没有角色主键数组
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void roleIdListNull() throws Exception {
        JSONObject param = new JSONObject();
        param.put("id", testConfig.getUid());

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .content(param.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("common.update.success").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 角色主键数组为空
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void roleIdListEmpty() throws Exception {
        JSONObject param = new JSONObject();
        param.put("id", testConfig.getUid());
        param.put("roleIdList", new JSONArray());
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .content(param.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("common.update.success").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 角色主键数组为非数字数组
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void roleIdListNotLongList() throws Exception {
        String parameterName = "com.am.server.api.user.pojo.ao.UpdateRoleAo[\"roleIdList\"]->java.util.ArrayList[0]";
        String parameterType = "Long";
        String actualParameterType = "String";
        String actualParameterValue = "aaa";
        MessageVO messageVO = message.get("exception.json.parameter.type");
        messageVO.setMessage(
                String.format(messageVO.getMessage(),
                        parameterName,
                        parameterType,
                        actualParameterType,
                        actualParameterValue)
        );

        JSONObject param = new JSONObject();
        param.put("id", testConfig.getUid());
        param.put("roleIdList", new JSONArray(Arrays.asList("aaa", "bbb")));
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .content(param.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(messageVO.getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

    /**
     * test
     *
     * @throws Exception Exception
     */
    @Transactional
    @Test
    public void test() throws Exception {
        JSONObject param = new JSONObject();
        param.put("id", testConfig.getUid());
        param.put("roleIdList", new JSONArray(roleIdList));
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(Constant.TOKEN, JwtUtils.sign(testConfig.getUid()))
                        .content(param.toString())
        )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(message.get("common.update.success").getMessage()));

        resultActions.andReturn().getResponse()
                .setCharacterEncoding("UTF-8");

        resultActions.andDo(MockMvcResultHandlers.print());
    }

}
