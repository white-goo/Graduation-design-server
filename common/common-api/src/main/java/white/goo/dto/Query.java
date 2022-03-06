package white.goo.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class Query<T> extends Page<T> {

    private LinkedHashMap<String,Object> conditions;

}
