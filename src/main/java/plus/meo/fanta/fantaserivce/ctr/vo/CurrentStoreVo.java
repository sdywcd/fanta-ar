package plus.meo.fanta.fantaserivce.ctr.vo;

import java.io.Serializable;
import java.util.List;

public class CurrentStoreVo implements Serializable {
    private int level;

    List<CurrentStoreChildrenVo>children;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<CurrentStoreChildrenVo> getChildren() {
        return children;
    }

    public void setChildren(List<CurrentStoreChildrenVo> children) {
        this.children = children;
    }
}
