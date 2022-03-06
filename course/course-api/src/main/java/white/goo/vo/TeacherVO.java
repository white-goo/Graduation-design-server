package white.goo.vo;

import lombok.Data;
import white.goo.dto.BaseVO;

@Data
public class TeacherVO extends BaseVO {

    private String name;

    private Boolean isDelete = false;

}
