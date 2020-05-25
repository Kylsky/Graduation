package cbuc.homestay.controller.RoomCenter;

import cbuc.homestay.base.Result;
import cbuc.homestay.bean.Image;
import cbuc.homestay.bean.Merchant;
import cbuc.homestay.bean.RoomInfo;
import cbuc.homestay.evt.RoomInfoEvt;
import cbuc.homestay.mapper.PropertyMapper;
import cbuc.homestay.service.ImageService;
import cbuc.homestay.service.RoomInfoService;
import cbuc.homestay.utils.QiniuCloudUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Explain: 商户端房源控制器
 * @Author: Cbuc
 * @Version: 1.0
 * @Date: 2020/2/13
 */
@Slf4j
@Controller
@RequestMapping("merchant")
@Api(value = "商户端房源控制器", description = "用来管理房源相关业务")
public class MerchantRoomController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RoomInfoService roomInfoService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PropertyMapper propertyMapper;

    @ApiOperation("跳转房源管理界面")
    @GetMapping("/toRoom")
    public String toRoom() {
        return "merchant/room";
    }

    @ApiOperation("根据城市名查找房源")
    @ResponseBody
    @GetMapping("/getRoomByCity")
    public Object getByCity(@RequestParam(value = "current", defaultValue = "1") Integer pn,
                            @RequestParam(value = "size", defaultValue = "10") Integer size,
                            @RequestParam(value = "sort", defaultValue = "id") String sort,
                            @RequestParam(value = "order", defaultValue = "desc") String order,
                            String city,Integer manCount,HttpSession session){
        try {
            Merchant merchant = (Merchant) session.getAttribute("LOGIN_MERCHANT");
            PageHelper.startPage(pn, size, sort + " " + order);     //pn:页码  10：页大小
            RoomInfo room = RoomInfo.builder().mid(merchant.getId()).build();
            if (StringUtils.isNotBlank(city)) {
                room.setCity(city );
            }
            if (null!=manCount){
                room.setManCount(manCount);
            }
            List<RoomInfo> roomInfoList = roomInfoService.queryList(room);
            PageInfo pageInfo = new PageInfo(roomInfoList, 10);
            return Result.layuiTable(pageInfo.getTotal(), pageInfo.getList());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询结果异常");
            return Result.error("查询结果异常");
        }




    }



    @ApiOperation("获取房源列表")
    @ResponseBody
    @GetMapping("/roomPage")
    public Object roomPage(@RequestParam(value = "current", defaultValue = "1") Integer pn,
                           @RequestParam(value = "size", defaultValue = "10") Integer size,
                           @RequestParam(value = "sort", defaultValue = "id") String sort,
                           @RequestParam(value = "order", defaultValue = "desc") String order,
                           String content, HttpSession session) {
        try {
            Merchant merchant = (Merchant) session.getAttribute("LOGIN_MERCHANT");
            PageHelper.startPage(pn, size, sort + " " + order);     //pn:页码  10：页大小
            RoomInfo room = RoomInfo.builder().mid(merchant.getId()).build();
            if (StringUtils.isNotBlank(content)) {
                room.setTitle(content);
            }
            List<RoomInfo> roomInfoList = roomInfoService.queryList(room);
            PageInfo pageInfo = new PageInfo(roomInfoList, 10);
            return Result.layuiTable(pageInfo.getTotal(), pageInfo.getList());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询结果异常");
            return Result.error("查询结果异常");
        }
    }

    @RequestMapping("/toOpeRoom")
    public String toOpeRoom(String type, Long rid, Model model) {
        RoomInfo roomInfo = roomInfoService.queryDetail(rid);
        RoomInfoEvt roomInfoEvt = new RoomInfoEvt();
        BeanUtils.copyProperties(roomInfo, roomInfoEvt);
        model.addAttribute("ri", roomInfoEvt);
        return "merchant/opeRoom";
    }

    @ResponseBody
    @RequestMapping("/doSaveRoom")
    public Object doSaveRoom(@RequestParam("myFiles") MultipartFile[] myFiles, @RequestParam("roomInfo") String roomInfo, HttpSession session) {
        Long rid;
        try {
            Merchant merchant = (Merchant) session.getAttribute("LOGIN_MERCHANT");
            RoomInfoEvt roomInfoEvt = JSON.parseObject(roomInfo, RoomInfoEvt.class);
            rid = roomInfoService.doSaveRoomInfo(roomInfoEvt, merchant.getId(), roomInfoEvt.getId());
            for (MultipartFile myFile : myFiles) {
                byte[] bytes = myFile.getBytes();
                String imageName = UUID.randomUUID().toString();
                try {
                    String url = QiniuCloudUtil.put64image(bytes, imageName);
                    Image image = Image.builder().parentId(rid).url(url).origin("ROOM").build();
                    imageService.doAdd(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            rid = null;
            return Result.error("房源发布失败,请重新尝试");
        }
        return rid == null ? Result.error("房源发布失败,请重新尝试") : Result.success();
    }

    @ApiOperation("获取房源图片")
    @ResponseBody
    @RequestMapping("/getRoomImage")
    public Object getRoomImage(Long rid) {
        List<Image> imageList = imageService.queryList(Image.builder().parentId(rid).origin("ROOM").status("E").build());
        return Result.success(imageList);
    }

    @ApiOperation("删除房源图片")
    @ResponseBody
    @RequestMapping("/delImage")
    public Object delImage(String url) {
        String target = url.split("/")[3];
        QiniuCloudUtil.delete(target);
        int res = imageService.doDel(url);
        return res>0?Result.success():Result.error();
    }

    @RequestMapping("/houselist")
    @ResponseBody
    public Result getHouseList(){
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from wx_room");
        list.forEach(room->{
            if (room.get("rules")!=null){
                room.put("rules",room.get("rules").toString().split(","));
            }
            if (room.get("advantage")!=null){
                room.put("advantage",room.get("advantage").toString().split(","));
            }
            if (room.get("activity")!=null){
                room.put("activity",room.get("activity").toString().split(","));
            }

        });
        Map map = new HashMap();
        map.put("houselist",list);
        return Result.success(map);

    }

    @RequestMapping("getAllRoomDetail")
    @ResponseBody
    public Result getAllRoomDetail(){
        Map<String,Object> result = new HashMap<>();
        List<Map<String,Object>> roomList = jdbcTemplate.queryForList("select * from wx_room_detail");
        Object[] resultArr = new Object[roomList.size()];
        roomList.forEach(room->{
            Integer roomId = (Integer) room.get("room_id");
            //处理数组
            String bedTypeList = room.get("bedTypeList").toString();
            if (StringUtils.isNotBlank(bedTypeList)){
                room.put("bedTypeList",bedTypeList.split(","));
            }
            //根据roomId获取图片
            List<Map<String,Object>> imgs = jdbcTemplate.queryForList("select path from wx_room_img where room_id=?",new Object[]{roomId});
            Object[] imgArr = new Object[imgs.size()];
            for (int i = 0; i < imgs.size(); i++) {
                imgArr[i] = imgs.get(i);
            }
            room.put("ImageList",imgArr);

            //根据roomId获取detail
            List<Map<String,Object>> tags = jdbcTemplate.queryForList("select tags from wx_detail where room_id =?",new Object[]{roomId});
            room.put("room_detail",tags);

            //根据roomId获取介绍
            List<Map<String,Object>> introduces = jdbcTemplate.queryForList("select room_special,room_intduce from wx_room_introduce where room_id =?",new Object[]{roomId});
            Map<String,Object> introduce = introduces.get(0);
            room.put("introduction",introduce);
        });

        for (int i = 0; i < roomList.size(); i++) {
            resultArr[i] = roomList.get(i);
        }
        result.put("houseDetailList",resultArr);
        return Result.success(result);
    }
}