package com.vvic.jst;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * <p>Description：
 *
 * @author CenHuaYou
 * @version 1.1.0
 * @since 2019-01-15 18:47
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BaseTests {

	protected MockMvc mvc;
	@Autowired
	protected WebApplicationContext wac;



	@Before()
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象

	}

	public MockMvc getMvc() {
		return mvc;
	}

	public void setMvc(MockMvc mvc) {
		this.mvc = mvc;
	}


	@Test
	public void testDoGet(){
		System.out.println(getMvc());
	}
}
