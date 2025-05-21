package tr.com.my_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tr.com.my_app.model.PinConfig;
import tr.com.my_app.service.MotorService;
import tr.com.my_app.service.PinConfigService;

import java.util.Locale;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PinConfigControllerTest {

    private MockMvc mockMvc;
    private PinConfigService pinConfigService;
    private MotorService motorService;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        pinConfigService = Mockito.mock(PinConfigService.class);
        motorService = Mockito.mock(MotorService.class);
        PinConfigController controller = new PinConfigController();
        controller.pinConfigService = pinConfigService;
        controller.motorService = motorService;
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testUploadFail_MissingParams() throws Exception {
        Map<String, Object> payload = Map.of("komut", "8,7,6,5");

        mockMvc.perform(post("/configlar/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Eksik parametre"));
    }

    @Test
    public void testDeleteSuccess() throws Exception {
        Mockito.when(pinConfigService.deletePinConfig(20L)).thenReturn(true);

        mockMvc.perform(post("/configlar/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("id", 20))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    public void testDeleteFail() throws Exception {
        Mockito.when(pinConfigService.deletePinConfig(999L)).thenReturn(false); // Gerçek dışı bir ID

        mockMvc.perform(post("/configlar/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("id", 999))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Silme başarısız"));
    }


    @Test
    public void testEditFormRedirectIfNotFound() throws Exception {
        Mockito.when(pinConfigService.getPinConfig(10L)).thenReturn(null);

        mockMvc.perform(get("/configlar/edit?id=10").locale(Locale.ENGLISH))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/configlar"));
    }

    @Test
    public void testEditFormReturnsView() throws Exception {
        PinConfig config = new PinConfig();
        config.setId(1L);
        Mockito.when(pinConfigService.getPinConfig(1L)).thenReturn(config);

        mockMvc.perform(get("/configlar/edit?id=1").locale(Locale.ENGLISH))
                .andExpect(status().isOk())
                .andExpect(view().name("config/pin_config_edit"))
                .andExpect(model().attributeExists("config"))
                .andExpect(model().attribute("lang", "en"));
    }
}
