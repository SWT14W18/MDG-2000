package de.tudresden.swt14ws18.controllerTests;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;

import de.tudresden.swt14ws18.AbstractWebIntegrationTests;
import de.tudresden.swt14ws18.controller.CustomerController;
import de.tudresden.swt14ws18.controller.LotterieController;

public class CustomerControllerTest extends AbstractWebIntegrationTests{
    
    @Autowired LotterieController controller;

    @Test
    public void sampleMvcIntegrationTest() throws Exception {

          mvc.perform(get("/")).andExpect(status().isOk()); 
                       
    }
    
    @Test
    public void sampleControllerIntegrationTest() {

            ModelMap model = new ExtendedModelMap();

            String returnedView = controller.Toindex(model); //gameoverview(model);  //profil(model); 

            assertThat(returnedView, is("index"));

            //Iterable<Object> object = (Iterable<Object>) model.get("totoGameTypes");
           // assertThat(object, is(notNullValue()));
    }

}
