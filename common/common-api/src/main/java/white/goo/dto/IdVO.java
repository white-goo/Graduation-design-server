package white.goo.dto;

import lombok.Data;

@Data
public class IdVO {

    private String id;

    public static IdVO of(String id){
        IdVO idVO = new IdVO();
        idVO.setId(id);
        return idVO;
    }

}
