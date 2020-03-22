package cbuc.homestay.service;

import cbuc.homestay.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Explain: 数据报表处理器
 * @Author: Cbuc
 * @Version: 1.0
 * @Date: 2020/1/14
 */
@Service
public class BaseService {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomInfoService roomInfoService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private BulletinService bulletinService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private OrderService orderService;

    public Map<String, Object> getTotalData(Long mId) {
        Map<String, Object> dataMap = new HashMap<>();
        Message message = new Message();
        RoomInfo roomInfo = new RoomInfo();
        News news = new News();
        Order order = new Order();
        if (mId != null) {
            message.setReceiveId(mId);
            message.setReceiveType("MERCHANT");
            message.setIfMerchant(true);
            roomInfo.setMid(mId);
            news.setPublishId(mId);
            order.setMid(mId);
        }
        int msgTotal = messageService.queryList(message).size();
        int merTotal = merchantService.queryList(null).size();
        int userTotal = userService.queryList().size();
        int roomTotal = roomInfoService.queryList(roomInfo).size();
        int newsTotal = newsService.queryList(news).size();
        int commentTotal = commentService.queryList(new Comment()).size();
        int orderTotal = orderService.queryList(order).size();
        int bulletinTotal = bulletinService.queryList(new Bulletin()).size();
        dataMap.put("msgTotal", msgTotal);
        dataMap.put("merTotal", merTotal);
        dataMap.put("userTotal", userTotal);
        dataMap.put("roomTotal", roomTotal);
        dataMap.put("newsTotal", newsTotal);
        dataMap.put("commentTotal", commentTotal);
        dataMap.put("orderTotal", orderTotal);
        dataMap.put("bulletinTotal", bulletinTotal);
        return dataMap;
    }

    public Map<String, Object> getLastData(Long mid,String type) {
        Comment comment = commentService.queryLast(mid);
        if (!Objects.isNull(comment)) {
            User user = userService.queryDetail(comment.getCommentor());
            comment.setPublishName(user.getUname());
        }
        Order order = orderService.queryLast(mid);
        if (!Objects.isNull(order)) {
            RoomInfo roomInfo = roomInfoService.queryDetail(order.getRid());
            order.setRoomInfo(roomInfo);
        }
        Message message = messageService.queryLast(mid,type);
        if (!Objects.isNull(message)) {
            Merchant merchant = merchantService.queryDetail(message.getSendId());
            message.setSendName(merchant.getMname());
        }
        Map<String, Object> lastMap = new HashMap<>();
        lastMap.put("lastComment", comment);
        lastMap.put("lastOrder", order);
        lastMap.put("lastMessage", message);
        return lastMap;
    }

    public List<Map<String, Object>> querySalesData(Long mid, String Time) {
        String year = Time.split("-")[0];
        String month = Time.split("-")[1];
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, Integer.parseInt(year));
        a.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        String beginTime = Time + "-01";
        String endTime = Time + "-" + maxDate;
        List<Map<String, Object>> datas = orderService.querySalesData(mid,beginTime, endTime);
        int index = 0;
        if (datas.isEmpty()) {
            for (int i = 1; i <= maxDate; i++) {
                String date = month + "-" + (i < 10 ? "0" + i : i);
                Map<String, Object> data = new HashMap<>();
                data.put("name", date);
                data.put("value", 0);
                datas.add(data);
            }
        }
        for (int i = 1; i <= maxDate; i++) {
            String date = month + (i < 10 ? "-0" + i : "-" + i);
            for (Map<String, Object> map : datas) {
                if (i < Integer.parseInt(map.get("name").toString().split("-")[1])) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("name", date);
                    data.put("value", 0);
                    datas.add(index, data);
                    index++;
                    break;
                } else if (i == Integer.parseInt(map.get("name").toString().split("-")[1])) {
                    index++;
                    break;
                }
            }
        }
        int max = Integer.parseInt(datas.get(datas.size() - 1).get("name").toString().split("-")[1]);
        for (int i = max + 1; i <= maxDate; i++) {
            String date = month + "-" + (i < 10 ? "0" + i : i);
            Map<String, Object> data = new HashMap<>();
            data.put("name", date);
            data.put("value", 0);
            datas.add(data);
        }
        return datas;
    }
}
