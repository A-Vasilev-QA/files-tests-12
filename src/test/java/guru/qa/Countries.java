package guru.qa;

import com.google.gson.annotations.SerializedName;

public class Countries {
    @SerializedName("name")
    private String name;
    @SerializedName("isoCode")
    private String isoCode;

    public Countries(String name, String isoCode) {
        this.name = name;
        this.isoCode = isoCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }
}
