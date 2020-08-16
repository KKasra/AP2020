package DB.components.heroes;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class HeroPower implements Serializable {
    @Column(name = "HERO_POWER_NAME")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HeroPower(String name) {
        this.name = name;
    }
    private HeroPower(){}
}
