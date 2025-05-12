package Models;

public class SystemSetting {
    private String name;
    private String value;

    public SystemSetting(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}