package white.goo.entity;

import lombok.Data;

@Data
public class Teacher extends BaseEntity {

    /**
     * 教师名称
     */
    private String name;

    /**
     * 是否删除
     */
    private Boolean isDelete = false;

}
