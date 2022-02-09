package white.goo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BaseVO {

    private Long id;
    private Date createTime;
    private Date editTime;
    private Long creator;
    private Long editor;


}
