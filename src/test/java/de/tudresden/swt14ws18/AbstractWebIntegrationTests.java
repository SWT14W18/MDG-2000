package de.tudresden.swt14ws18;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Lotterie.class) //classes = Lotterie.class)
@ContextConfiguration(classes = Lotterie.class)
public class AbstractWebIntegrationTests {

        @Autowired WebApplicationContext context;
        protected MockMvc mvc;

        @Before
        public void setUp() {
                mvc = MockMvcBuilders.webAppContextSetup(context).build();
        }
}
