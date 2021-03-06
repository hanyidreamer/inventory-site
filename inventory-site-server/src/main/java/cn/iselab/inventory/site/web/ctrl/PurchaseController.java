package cn.iselab.inventory.site.web.ctrl;

import cn.iselab.inventory.site.common.constanst.UrlConstants;
import cn.iselab.inventory.site.common.web.ErrorResult;
import cn.iselab.inventory.site.common.web.ResponseMessage;
import cn.iselab.inventory.site.common.web.StatusCode;
import cn.iselab.inventory.site.common.web.SuccessResult;
import cn.iselab.inventory.site.web.data.PurchaseOrderVO;
import cn.iselab.inventory.site.web.exception.HttpBadRequestException;
import cn.iselab.inventory.site.web.logic.PurchaseLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @Author ROKG
 * @Description
 * @Date: Created in 下午5:57 2017/12/5
 * @Modified By:
 */
@RestController
public class PurchaseController extends BaseController{

    @Autowired
    PurchaseLogic purchaseLogic;

    @RequestMapping(value = UrlConstants.API+"purchases",method = RequestMethod.GET)
    public Map<String,Object> getPurchases(@RequestParam(value = "keyword",required = false)String keyword,
                                          @RequestParam(value = "sortBy")String sortBy,
                                          @RequestParam(value = "type")Boolean type,
                                          HttpServletRequest request){
        String activePage = request.getHeader("activePage");
        String rowsOnPage = request.getHeader("rowsOnPage");
        if(activePage == null || rowsOnPage == null) {
            throw new IllegalArgumentException("缺少分页信息");
        }
        Sort sortById = new Sort(Sort.Direction.DESC, sortBy);
        Pageable pageable = new PageRequest(Integer.parseInt(activePage) - 1, Integer.parseInt(rowsOnPage),sortById);
        Page<PurchaseOrderVO> orderVOS=purchaseLogic.getPurchases(keyword,pageable,type);
        return SuccessResult.ok(ResponseMessage.ITEM_RESULT,orderVOS);
    }

    @RequestMapping(value = UrlConstants.API_PURCHASE,method = RequestMethod.GET)
    public Map<String,Object> getPurchase(@RequestParam(name = "number")String number){
        try {
            PurchaseOrderVO orderVO=purchaseLogic.getPurchase(number);
            return SuccessResult.ok(ResponseMessage.ITEM_RESULT,orderVO);
        }catch (HttpBadRequestException e){
            return new ErrorResult(StatusCode.PURCHASE_NOT_EXISTS);
        }
    }

    @RequestMapping(value = UrlConstants.API_PURCHASE,method = RequestMethod.POST)
    public Map<String,Object> createPurchase(@RequestBody @NotNull PurchaseOrderVO orderVO){
        String number= purchaseLogic.createPurchase(orderVO);
        SuccessResult successResult = new SuccessResult();
        successResult.put(ResponseMessage.ID_RESULT, number);
        return successResult;
    }

    @RequestMapping(value = UrlConstants.API_PURCHASE,method = RequestMethod.PUT)
    public Map<String,Object> updatePurchase(@RequestBody @NotNull PurchaseOrderVO orderVO){
        try {
            purchaseLogic.updatePurchase(orderVO);
            return SuccessResult.ok();
        }catch (HttpBadRequestException e){
            return new ErrorResult(StatusCode.PURCHASE_NOT_EXISTS);
        }
    }

    @RequestMapping(value = UrlConstants.API_PURCHASE,method = RequestMethod.DELETE)
    public Map<String,Object> deletePurchase(@RequestParam(name = "number")String number){
        try {
            purchaseLogic.deletePurchase(number);
            return SuccessResult.ok();
        }catch (HttpBadRequestException e){
            return new ErrorResult(StatusCode.PURCHASE_NOT_EXISTS);
        }
    }
}
