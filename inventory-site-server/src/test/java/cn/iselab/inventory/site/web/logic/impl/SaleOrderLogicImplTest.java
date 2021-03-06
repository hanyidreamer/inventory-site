package cn.iselab.inventory.site.web.logic.impl;

import cn.iselab.inventory.site.Application;
import cn.iselab.inventory.site.model.Custom;
import cn.iselab.inventory.site.model.SaleOrder;
import cn.iselab.inventory.site.model.SaleOrderItem;
import cn.iselab.inventory.site.service.CustomService;
import cn.iselab.inventory.site.service.SaleOrderItemService;
import cn.iselab.inventory.site.service.SaleOrderService;
import cn.iselab.inventory.site.web.data.SaleOrderItemVO;
import cn.iselab.inventory.site.web.data.SaleOrderVO;
import cn.iselab.inventory.site.web.data.wrapper.SaleOrderItemVOWrapper;
import cn.iselab.inventory.site.web.data.wrapper.SaleOrderVOWraper;
import cn.iselab.inventory.site.web.logic.SaleOrderLogic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * @Author ROKG
 * @Description
 * @Date: Created in 下午8:33 2017/12/5
 * @Modified By:
 */

@RunWith(MockitoJUnitRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Application.class)
public class SaleOrderLogicImplTest {

    @InjectMocks
    SaleOrderLogic saleOrderLogic=new SaleOrderLogicImpl();

    @Mock
    SaleOrderVOWraper saleOrderVOWraper;

    @Mock
    SaleOrderService saleOrderService;

    @Mock
    SaleOrderItemService saleOrderItemService;

    @Mock
    CustomService customService;

    @Mock
    SaleOrderItemVOWrapper itemVOWrapper;

    SaleOrder order=new SaleOrder();
    SaleOrderVO vo=new SaleOrderVO();
    SaleOrderItem item=new SaleOrderItem();
    SaleOrderItemVO itemVO=new SaleOrderItemVO();
    List<SaleOrder> orders=new ArrayList<>();
    List<SaleOrderVO> orderVOS=new ArrayList<>();
    List<SaleOrderItem> items=new ArrayList<>();
    List<SaleOrderItemVO> itemVOS=new ArrayList<>();
    Pageable pageable;
    Page<SaleOrder> orderPage;
    Page<SaleOrderVO> orderVOPage;
    Custom custom=new Custom();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        order.setId(1L);
        order.setOperator("test");
        order.setTotal(1.0);
        order.setNumber("test");

        orders.add(order);

        vo.setOperator("test");
        vo.setTotal(1.0);
        vo.setNumber("test");
        vo.setStatus(1L);
        vo.setCustomId(1L);

        orderVOS.add(vo);

        item.setOrderId(1L);
        item.setTotal(1.0);

        items.add(item);

        itemVO.setGoodId(1L);
        itemVO.setGoodModel("test");

        itemVOS.add(itemVO);

        vo.setOrderItems(itemVOS);

        custom.setName("test");

        orderPage=new PageImpl<>(orders);
        orderVOPage=new PageImpl<>(orderVOS);

        pageable = new PageRequest(0,10);
    }

    @Test
    public void should_returnSaleOrders_when_SaleOrdersExist() throws Exception {
        when(saleOrderService.getSaleOrders(anyString(),any(Pageable.class),anyBoolean())).thenReturn(orderPage);
        when(saleOrderVOWraper.wrap(any(SaleOrder.class))).thenReturn(vo);

        Page<SaleOrderVO> result=saleOrderLogic.getSaleOrders("test",pageable,true);
        Assert.assertEquals(vo.getOperator(),result.getContent().get(0).getOperator());
        Assert.assertEquals(vo.getNumber(),result.getContent().get(0).getNumber());
        Assert.assertEquals(vo.getTotal(),result.getContent().get(0).getTotal());
    }

    @Test
    public void should_returnSaleOrder_when_givenSaleOrderNumber() throws Exception {
        when(saleOrderService.getSaleOrderByNum(anyString())).thenReturn(order);
        when(saleOrderVOWraper.wrap(any(SaleOrder.class))).thenReturn(vo);

        SaleOrderVO result=saleOrderLogic.getSaleOrder("test");

        Assert.assertEquals(vo.getNumber(),result.getNumber());
        Assert.assertEquals(vo.getOperator(),result.getOperator());
        Assert.assertEquals(vo.getTotal(),result.getTotal());
    }

    @Test
    public void should_createSaleOrder_when_givenSaleOrder() throws Exception {
        when(saleOrderVOWraper.unwrap(any(SaleOrderVO.class))).thenReturn(order);
        when(customService.getCustom(anyLong())).thenReturn(custom);
        when(saleOrderService.createSaleOrder(any(SaleOrder.class))).thenReturn(order);
        when(itemVOWrapper.unwrap(any(SaleOrderItemVO.class))).thenReturn(item);

        String result=saleOrderLogic.createSaleOrder(vo);

        Mockito.verify(saleOrderItemService).createSaleItem(any(SaleOrderItem.class));
        Assert.assertEquals(order.getNumber(),result);
    }

    @Test
    public void should_updateSaleOrder_when_givenSaleOrder() throws Exception {
        when(saleOrderService.getSaleOrderByNum(anyString())).thenReturn(order);

        saleOrderLogic.updateSaleOrder(vo);

        Mockito.verify(saleOrderService).updateSaleOrder(any(SaleOrder.class));
    }

    @Test
    public void should_deleteSaleOrder_when_givenSaleOrder() throws Exception {
        when(saleOrderService.getSaleOrderByNum(anyString())).thenReturn(order);

        saleOrderLogic.deleteSaleOrder("test");

        Mockito.verify(saleOrderService).deleteSaleOrder(any(SaleOrder.class));
    }
}