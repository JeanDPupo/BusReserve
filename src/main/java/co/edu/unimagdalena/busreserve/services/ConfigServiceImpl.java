package co.edu.unimagdalena.busreserve.services;

import co.edu.unimagdalena.busreserve.domine.entities.Config;
import co.edu.unimagdalena.busreserve.domine.repositories.ConfigRepository;
import co.edu.unimagdalena.busreserve.exception.NotFoundException;
import co.edu.unimagdalena.busreserve.services.interfaces.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ConfigServiceImpl implements ConfigService {

    private final ConfigRepository configRepo;

    @Override
    @Transactional(readOnly = true)
    public String getValue(String key) {
        return configRepo.findByKeyName(key)
                .map(Config::getValue)
                .orElse(null);
    }

    @Override
    public void setValue(String key, String value) {
        Config config = configRepo.findByKeyName(key)
                .orElseGet(() -> {
                    Config c = new Config();
                    c.setKeyName(key);
                    return c;
                });

        config.setValue(value);
        configRepo.save(config);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getHoldTimeMinutes() {
        String val = getValue("hold_time_minutes");
        return val != null ? Integer.parseInt(val) : 10;
    }

    @Override
    @Transactional(readOnly = true)
    public Double getOverbookingPercentage() {
        String val = getValue("overbooking_percentage");
        return val != null ? Double.parseDouble(val) : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Double getNoShowFee() {
        String val = getValue("no_show_fee");
        return val != null ? Double.parseDouble(val) : 0.0;
    }

    @Override
    public void updateHoldTime(Integer minutes) {
        setValue("hold_time_minutes", minutes.toString());
    }

    @Override
    public void updateOverbookingPercentage(Double percentage) {
        setValue("overbooking_percentage", percentage.toString());
    }

    @Override
    public void updateNoShowFee(Double fee) {
        setValue("no_show_fee", fee.toString());
    }
}