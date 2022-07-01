package plus.meo.fanta.fantaserivce.ctr.vo;

import java.io.Serializable;

public class CurrentStoreChildrenVo implements Serializable {
    private int level;
    private String name;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
