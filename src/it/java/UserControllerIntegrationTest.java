import com.example.UserService.UserServiceApplication;
import com.example.UserService.controller.UserController;
import com.example.UserService.dto.UserDTO;
import com.example.UserService.model.Address;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = UserServiceApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration-test.properties")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserController userController;

    private ObjectMapper objectMapper;
    private ObjectWriter objectWriter;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
    }

    @Test
    public void getUsersEmptyList() throws Exception{
        mvc.perform(get("/users"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void getUsersSuccessfully() throws Exception{
        Address address = new Address("Romania", "Cluj", "Cluj-Napoca", "Strada Dorobantilor", 33, "123456");
        UserDTO userDTO = new UserDTO("RobertTest", "admin", address, null);
        userController.addUser(userDTO);
        mvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void addUserSuccessfully() throws Exception {
        Address address = new Address("Romania", "Cluj", "Cluj-Napoca", "Strada Dorobantilor", 33, "123456");
        UserDTO userDTO = new UserDTO("Simina", "admin", address, null);
        String requestJson = objectWriter.writeValueAsString(userDTO);
        mvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void addUserThrowsConstraintViolationException() throws Exception {
        UserDTO userDTO = new UserDTO();
        String requestJson = objectWriter.writeValueAsString(userDTO);
        mvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addUserThrowsDataIntegrityViolationException() throws Exception {
        Address address = new Address("Romania2", "Cluj", "Cluj-Napoca", "Strada Dorobantilor", 33, "123456");
        UserDTO userDTO = new UserDTO("Simina2", "admin", address, null);
        String requestJson = objectWriter.writeValueAsString(userDTO);
        userController.addUser(userDTO);
        mvc.perform(post("/").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username must be unique!"));
    }

    @Test
    public void updateUserSuccessfully() throws Exception {
        Address address = new Address("RomaniaUpdate", "Cluj", "Cluj-Napoca", "Strada Dorobantilor", 33, "123456");
        UserDTO userDTO = new UserDTO("SiminaUpdate", "admin", address, null);
        userController.addUser(userDTO);
        UserDTO userDTO1 = new UserDTO(null, null, address, null);
        String requestJson = objectWriter.writeValueAsString(userDTO1);
        mvc.perform(
                put("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .param("username", "SiminaUpdate"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUserThrowConstraintViolationException() throws Exception {
        Address address = new Address("RomaniaThrow", "Cluj", "Cluj-Napoca", "Strada Dorobantilor", 33, "123456");
        Address address2 = new Address();
        UserDTO userDTO = new UserDTO("SiminaThrow", "admin", address, null);
        userController.addUser(userDTO);
        UserDTO userDTO1 = new UserDTO(null, null, address2, null);
        String requestJson = objectWriter.writeValueAsString(userDTO1);
        mvc.perform(
                put("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .param("username", "SiminaThrow"))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void deleteUserSuccessfully() throws Exception{
        Address address = new Address("Romania", "Cluj", "Cluj-Napoca", "Strada Dorobantilor", 33, "123456");
        UserDTO userDTO = new UserDTO("Serban","1234",address,null);
        userController.addUser(userDTO);
        mvc.perform(delete("/").param("username","Serban"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUserFail() throws Exception{
        Address address = new Address("Romania", "Cluj", "Cluj-Napoca", "Strada Dorobantilor", 33, "123456");
        UserDTO userDTO = new UserDTO("Serban2","1234",address,null);
        userController.addUser(userDTO);
        mvc.perform(delete("/").param("username","Serban3"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findUserByUsernameSuccessfully() throws Exception{
        mvc.perform(get("/").param("username","Simina"))
                .andExpect(status().isOk());
    }

    @Test
    public void findUserByIDSuccessfully() throws Exception{
        mvc.perform(get("/").param("id","1"))
                .andExpect(status().isOk());
    }

    @Test
    public void findUserByUsernameFail() throws Exception{
        mvc.perform(get("/").param("username","Robert"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findUserByIDFail() throws Exception{
        mvc.perform(get("/").param("id","5"))
                .andExpect(status().isNotFound());
    }
}
