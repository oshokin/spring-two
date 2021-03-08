package ru.oshokin.store;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PermissionsTests {

    @Autowired
    private MockMvc mockMVC;

    //попытка залогиниться по заведомо несуществующим
    //(какая там область значений UID'а?) учетным данным
    @Test
    public void credentialsCheckOnLogin() throws Exception {
        mockMVC.perform(formLogin("/authenticateTheUser")
                .user("3e20386e-7709-41ee-8ed8-64e26f8dcada")
                .password("739be519-1931-4c7e-adb9-95df2aebca92"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    //только админ может добавлять продукты
    @Test
    public void justAdminCanAddProducts() throws Exception {
        mockMVC.perform(get("/products/edit/0"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

}
