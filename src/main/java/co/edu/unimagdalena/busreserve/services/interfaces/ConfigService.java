package co.edu.unimagdalena.busreserve.services.interfaces;

public interface ConfigService {
    String getValue(String key);
    void setValue(String key, String value);
    Integer getHoldTimeMinutes();
    Double getOverbookingPercentage();
    Double getNoShowFee();
    void updateHoldTime(Integer minutes);
    void updateOverbookingPercentage(Double percentage);
    void updateNoShowFee(Double fee);
}
