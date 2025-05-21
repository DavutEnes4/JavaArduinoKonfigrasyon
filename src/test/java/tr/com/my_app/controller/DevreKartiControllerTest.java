package tr.com.my_app.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tr.com.my_app.config.WebConfig;
import tr.com.my_app.model.DevreKarti;
import tr.com.my_app.service.DevreKartiService;
import tr.com.my_app.service.PinConfigService;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig(classes = WebConfig.class)
@WebAppConfiguration
public class DevreKartiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DevreKartiService devreKartiService;

    @Mock
    private PinConfigService pinConfigService;

    @InjectMocks
    private DevreKartiController devreKartiController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(devreKartiController).build();
    }

    @Test
    void kartlarListesiSayfasiYukleniyorMu() throws Exception {
        when(devreKartiService.getKartlar(0, 10, null)).thenReturn(List.of());
        when(devreKartiService.getTotalCount(null)).thenReturn(0L);

        mockMvc.perform(get("/kartlar"))
                .andExpect(status().isOk())
                .andExpect(view().name("devrekarti/kartlar_listesi"))
                .andExpect(model().attributeExists("kartlar"))
                .andExpect(model().attributeExists("totalCount"))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("pageSize", 10));
    }

    @Test
    void kartConfigSayfasiYukleniyorMu() throws Exception {
        DevreKarti kart = new DevreKarti();
        kart.setId(1L);
        kart.setAdi("Test Kart");

        when(devreKartiService.getKartDetay(1L)).thenReturn(kart);

        mockMvc.perform(get("/kart/1/config"))
                .andExpect(status().isOk())
                .andExpect(view().name("devrekarti/kart_config"))
                .andExpect(model().attributeExists("kart"))
                .andExpect(model().attribute("kart", hasProperty("adi", is("Test Kart"))));
    }

    @Test
    void kartConfigBasariylaKaydediliyor() throws Exception {
        when(pinConfigService.saveOrUpdate(null, "Yeni Pin", "1,0,1", 1L)).thenReturn(true);

        mockMvc.perform(post("/kart/1/config/save")
                        .param("isim", "Yeni Pin")
                        .param("pinValues", "1,0,1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Konfigürasyon kaydedildi")));
    }

    @Test
    void kartConfigKayit_PinEksik() throws Exception {
        mockMvc.perform(post("/kart/1/config/save")
                        .param("isim", "Geçerli İsim")
                        .param("pinValues", ""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("En az bir pin seçmelisiniz")));
    }
}
