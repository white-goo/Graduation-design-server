package white.goo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    private String username;
    private String password;
    @TableField("roleIds")
    private String roleIds;
}
