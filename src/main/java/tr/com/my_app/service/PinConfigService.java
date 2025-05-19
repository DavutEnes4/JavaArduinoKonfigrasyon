package tr.com.my_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.my_app.dao.DevreKartiDao;
import tr.com.my_app.dao.PinConfigDao;

import tr.com.my_app.model.DevreKarti;
import tr.com.my_app.model.PinConfig;

import java.util.Date;
import java.util.List;

@Service
public class PinConfigService {

    @Autowired
    private PinConfigDao pinConfigDao;

    @Autowired
    private DevreKartiDao devreKartiDao;

    @Transactional(readOnly = false)
    public Boolean saveOrUpdate(Long id, String adi, String pinValues, Long devreKartiId) {
        PinConfig pinConfig = null;
        if (id != null) {
            pinConfig = (PinConfig) pinConfigDao.loadObject(PinConfig.class, id);
        } else {
            pinConfig = new PinConfig();
        }

        pinConfig.setAdi(adi);
        pinConfig.setPinValues(pinValues);;
        pinConfig.setDevreKarti((DevreKarti) devreKartiDao.loadObject(DevreKarti.class,devreKartiId));

        Date now = new Date();
        pinConfig.setOlusturulmaTarihi(now);

        return pinConfigDao.saveOrUpdate(pinConfig);
    }

    @Transactional(readOnly = false)
    public Boolean deletePinConfig(Long pinConfigId) {
        PinConfig pinConfig = (PinConfig) pinConfigDao.loadObject(PinConfig.class, pinConfigId);
        return pinConfigDao.removeObject(pinConfig);
    }

    public List<PinConfig> getPinConfigList(int start, int limit, String query) {
        return pinConfigDao.loadPinConfig(start, limit, query);
    }

    public PinConfig getPinConfig(Long pinConfigId) {
        return (PinConfig) pinConfigDao.loadObject(PinConfig.class,pinConfigId);
    }

    public Long getTotalCount(String query) {
        return pinConfigDao.getTotalCount(query);
    }
}
