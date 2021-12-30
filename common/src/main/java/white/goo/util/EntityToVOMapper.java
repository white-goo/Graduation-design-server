package white.goo.util;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import white.goo.annotation.FK;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;

public class EntityToVOMapper {


    private EntityToVOMapper(){}

    public static EntityToVOMapper getInstance(){
        return new EntityToVOMapper();
    }

    public<V,E> void entityToVo(V vo,E entity){

        if(Objects.isNull(vo) || Objects.isNull(entity)){return;}
        try {
            BeanUtil.copyProperties(entity, vo);

            Class<?> entityClass = entity.getClass();
            Class<?> voClass = vo.getClass();
            Field[] voFields = voClass.getDeclaredFields();
            Field[] entityFields = entityClass.getDeclaredFields();
            for (Field field : entityFields) {
                field.setAccessible(true);
                FK annotation = field.getAnnotation(FK.class);
                if(Objects.nonNull(annotation)){
                    Class<?> value = annotation.value();
                    for (Field voField : voFields) {
                        voField.setAccessible(true);
                        if((value == voField.getType())){
                            voField.set(vo,DBUtil.getService(annotation.service()).getById((String) field.get(entity)));
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
