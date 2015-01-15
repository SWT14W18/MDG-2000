package de.tudresden.swt14ws18;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * 
 * Dieses Grundgerüst dient als Startgerüst für WebIntegrationtests, also ControllerTests! ========================= !!!!!!NICHT FUNKTIONSFÄHIG!!!
 * =========================
 * 
 * aber auch nocht vom Tutor gefordert
 * 
 * @author Reinhard_2
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Lotterie.class)
// classes = Lotterie.class)
@ContextConfiguration(classes = Lotterie.class)
public abstract class AbstractWebIntegrationTests {

    @Autowired
    WebApplicationContext context;
    protected MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
}
