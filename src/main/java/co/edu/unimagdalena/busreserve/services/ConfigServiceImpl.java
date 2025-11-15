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

    private static final String HOLD_TIME_KEY = "hold_time_minutes";
    private static final String OVERBOOKING_KEY = "overbooking_percentage";
    private static final String NO_SHOW_FEE_KEY = "no_show_fee";

    @Override
    @Transactional(readOnly = true)
    public String getValue(String key) {
        return configRepo.findByKey(key)
                .map(Config::getValue)
                .orElse(null);
    }

    @Override
    public void setValue(String key, String value) {
        Config config = configRepo.findByKey(key)
                .orElse(Config.builder().keyName(key).build());
        
        config.setValue(value);
        configRepo.save(config);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getHoldTimeMinutes() {
        String value = getValue(HOLD_TIME_KEY);
        return value != null ? Integer.parseInt(value) : 10;
    }

    @Override
    @Transactional(readOnly = true)
    public Double getOverbookingPercentage() {
        String value = getValue(OVERBOOKING_KEY);
        return value != null ? Double.parseDouble(value) : 5.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Double getNoShowFee() {
        String value = getValue(NO_SHOW_FEE_KEY);
        return value != null ? Double.parseDouble(value) : 10000.0;
    }

    @Override
    public void updateHoldTime(Integer minutes) {
        setValue(HOLD_TIME_KEY, minutes.toString());
    }

    @Override
    public void updateOverbookingPercentage(Double percentage) {
        if (percentage < 0 || percentage > 20) {
            throw new IllegalArgumentException("Overbooking percentage must be between 0 and 20");
        }
        setValue(OVERBOOKING_KEY, percentage.toString());
    }

    @Override
    public void updateNoShowFee(Double fee) {
        if (fee < 0) {
            throw new IllegalArgumentException("No-show fee cannot be negative");
        }
        setValue(NO_SHOW_FEE_KEY, fee.toString());
    }
}
