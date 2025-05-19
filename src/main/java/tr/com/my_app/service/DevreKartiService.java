package tr.com.my_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.com.my_app.dao.DevreKartiDao;
import tr.com.my_app.model.DevreKarti;
import tr.com.my_app.model.Pin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class DevreKartiService {

    @Autowired
    private DevreKartiDao devreKartiDao;

    public List<DevreKarti> getKartlar(int start, int limit, String query) {
        return devreKartiDao.loadDevrekarti(start, limit, query);
    }

    public Long getTotalCount(String query) {
        return devreKartiDao.getTotalCount(query);
    }

    public DevreKarti getKartDetay(Long kartId) {
        return devreKartiDao.loadWithPins(kartId);
    }
}
