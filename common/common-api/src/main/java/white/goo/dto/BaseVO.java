package white.goo.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class BaseVO implements Serializable {

    private String id;
    private Date createTime;
    private Date editTime;
    private String creator;
    private String editor;


}
