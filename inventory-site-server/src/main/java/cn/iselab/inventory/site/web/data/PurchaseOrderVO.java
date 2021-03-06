package cn.iselab.inventory.site.web.data;

import cn.iselab.inventory.site.model.GoodItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author ROKG
 * @Description
 * @Date: Created in 下午5:59 2017/12/5
 * @Modified By:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderVO {

    private String number;

    private String supplier;

    private Long customId;

    private String repository;

    private String operator;

    private String extra;

    private Double total;

    private Boolean type;

    private Long status;

    private Long createTime;

    private List<GoodItem> goodsItemVOS;

}
