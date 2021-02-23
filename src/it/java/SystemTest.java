import com.example.UserService.UserServiceApplication;
import com.example.UserService.controller.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = UserServiceApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-system-test.properties")
public class SystemTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserController userController;

    private ObjectMapper objectMapper;
    private ObjectWriter objectWriter;

    @Before
    public void setUp() throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    public void addToShoppingCartSuccessfully() throws Exception {
        mvc.perform(
                get("/addToCart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "Simina")
        ).andExpect(status().isAccepted());
    }

    @Test @Ignore
    public void addToShoppingCartUsernameNotFound() throws Exception {
        mvc.perform(
                get("/addToCart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "Serban")
        ).andExpect(status().isBadRequest());
    }
}
