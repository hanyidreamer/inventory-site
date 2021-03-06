package cn.iselab.inventory.site.web.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author ROKG
 * @Description
 * @Date: Created in 下午2:40 2017/12/8
 * @Modified By:
 */
@Data
public class SaleDetailVO {

    private String goodName;
    private String goodModel;
    private Long goodNum;
    private Double price;
    private Double total;
    private Long createTime;
}
