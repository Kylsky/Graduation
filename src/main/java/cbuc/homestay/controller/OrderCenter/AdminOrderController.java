package cbuc.homestay.controller.OrderCenter;

import cbuc.homestay.base.Result;
import cbuc.homestay.bean.*;
import cbuc.homestay.evt.UserEvt;
import cbuc.homestay.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Explain: 管理员之房源管理控制器
 * @Author: Cbuc
 * @Version: 1.0
 * @Date: 2020/1/13
 */
@Slf4j
@Controller
@RequestMapping("/admin")
@Api(value = "管理员之订单管理控制器", description = "管理订单相关内容")
public class AdminOrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RoomInfoService roomInfoService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private ImageService imageService;

    @ApiOperation("跳转房源管理界面")
    @GetMapping("/orderManage")
    public String roomManage() {
        return "admin/orderManage";
    }

    @ApiOperation("获取订单列表")
    @ResponseBody
    @GetMapping("/orderManagePage")
    public Object roomManagePage(@RequestParam(value = "current", defaultValue = "1") Integer pn,
                                 @RequestParam(value = "size", defaultValue = "10") Integer size,
                                 @RequestParam(value = "sort", defaultValue = "id") String sort,
                                 @RequestParam(value = "order", defaultValue = "desc") String order,
                                 String content) {
        try {
            PageHelper.startPage(pn, size, sort + " " + order);     //pn:页码  10：页大小
            List<Order> orderList = orderService.queryList(new Order());
            orderList.stream().forEach(rl -> {
                RoomInfo roomInfo = roomInfoService.queryDetail(rl.getRid());
                rl.setRoomInfo(roomInfo);
                User merchant = userService.queryDetail(Long.valueOf(rl.getOpenId()));
            });
            PageInfo pageInfo = new PageInfo(orderList, 10);
            return Result.layuiTable(pageInfo.getTotal(), pageInfo.getList());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询结果异常");
            return Result.error("查询结果异常");
        }
    }

    @ApiOperation("禁用/启用房源")
    @ResponseBody
    @PostMapping("/opeOrder")
    public Object opeOrder(UserEvt evt) {
        try {
            Order order = new Order();
            BeanUtils.copyProperties(evt, order);
            int res = orderService.doEdit(order);
            return res > 0 ? Result.success() : Result.error("操作失败");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("操作订单异常");
            return Result.error("操作订单异常");
        }
    }

    @ApiOperation("跳转订单详情页")
    @GetMapping("/orderDetail")
    public String orderDetail(String order_Code, Model model) {
        Order order = new Order();
        order.setOrderCode(order_Code);
        order = orderService.queryList(order).get(0);
        RoomInfo roomInfo = roomInfoService.queryDetail(order.getRid());
        order.setRoomInfo(roomInfo);
        model.addAttribute("orderInfo", order);
        return "orderDetail";
    }
}