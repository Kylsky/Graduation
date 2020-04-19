<!--管理员之房源详情界面-->

<!DOCTYPE html>
<html lang="en">
<#assign base=request.contextPath />
<head>
    <meta charset="UTF-8">
    <title>宿派后台管理系统</title>
    <!--Base-->
    <script src="${base}/js/jquery-1.11.2.min.js" type="application/javascript"></script>
    <script src="${base}/js/base.js"></script>
    <!--Layui-->
    <script src="${base}/plugins/layui/layui.all.js" type="application/javascript"></script>
    <link rel="stylesheet" href="${base}/plugins/layui/css/layui.css">
</head>
<body>
<div class="layui-fluid layui-anim layui-anim-scale" style="padding: 30px;">
    <div class="layui-row layui-card">
        <div class="layui-card-body" style="padding-top: 25px;">
            <from class="layui-form">
                <blockquote class="layui-elem-quote layui-text" style="width: 300px">
                    订单信息
                </blockquote>
                <div class="layui-form-item">
                    <label class="layui-form-label">订单号:</label>
                    <div class="layui-input-inline">
                        <input disabled class="layui-input" value="${orderInfo.orderCode!}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">订单金额:</label>
                    <div class="layui-input-inline">
                        <input disabled class="layui-input" value="${orderInfo.price!}">
                    </div>
                    ￥
                </div>
                <div class="layui-form-item" style="display: inline-block">
                    <label class="layui-form-label">租住期限(日)：</label>
                    <div class="layui-input-inline" style="width: 70px;">
                        <input type="text" name="price" lay-verify="title" autocomplete="off" placeholder="xxx"
                               class="layui-input" value="${orderInfo.dayCount!}">
                    </div>
                </div>
                <div class="layui-form-item" style="display: inline-block">
                    <label class="layui-form-label">支付时间：</label>
                    <div class="layui-input-inline" style="width: 240px;">
                        <span>${(orderInfo.payTime?string("yyyy-MM-dd"))!}</span>
                    </div>
                </div>
                <div class="layui-form-item" style="display: inline-block">
                    <label class="layui-form-label">订单状态：</label>
                    <div class="layui-input-inline" style="width: 70px;">
                        <span>${(orderInfo.status=='1')?string('正常','关闭')}</span>
                    </div>
                </div>





                <blockquote class="layui-elem-quote layui-text" style="width: 300px">
                    房间信息
                </blockquote>


                <div class="layui-form-item">
                    <label class="layui-form-label">房间标题:</label>
                    <div class="layui-input-inline">
                        <input disabled class="layui-input" value="${orderInfo.roomInfo.title!}">
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">房间描述:</label>
                    <div class="layui-input-inline" style="width: 450px;">
                        <textarea disabled class="layui-textarea"
                                  style="cursor: not-allowed;background-color: #f4f4f4;">${orderInfo.roomInfo.des!}</textarea>
                    </div>
                </div>
                <div class="layui-form-item" style="display: inline-block">
                    <label class="layui-form-label">房间价格：</label>
                    <div class="layui-input-inline" style="width: 70px;">
                        <input type="text" name="price" lay-verify="title" autocomplete="off" placeholder="xxx"
                               class="layui-input" value="${orderInfo.roomInfo.price!}">
                    </div>
                    ￥/天
                </div>
                <div class="layui-form-item" style="display: inline-block">
                    <label class="layui-form-label">房间类型：</label>
                    <span>${orderInfo.roomInfo.type}</span>
                </div>

                <blockquote class="layui-elem-quote layui-text" style="width: 300px">
                    客户信息
                </blockquote>

                <div class="layui-form-item" style="display: inline-block">
                    <label class="layui-form-label">姓名：</label>
                    <div class="layui-input-inline" style="width: 70px;">
                        <input type="text" name="price" lay-verify="title" autocomplete="off" placeholder="xxx"
                               class="layui-input" value="${orderInfo.name!}">
                    </div>
                </div>
                <div class="layui-form-item" style="display: inline-block">
                    <label class="layui-form-label">联系方式：</label>
                    <div class="layui-input-inline" style="width: 120px;">
                        <input type="text" name="price" lay-verify="title" autocomplete="off" placeholder="xxx"
                               class="layui-input" value="${orderInfo.phone!}">
                    </div>
                </div>
                <div class="layui-form-item" style="display: inline-block">
                    <label class="layui-form-label">身份证号：</label>
                    <div class="layui-input-inline" style="width: 200px;">
                        <input type="text" name="price" lay-verify="title" autocomplete="off" placeholder="xxx"
                               class="layui-input" value="${orderInfo.cardno!}">
                    </div>
                </div>
                <#--                <div class="layui-form-item" style="display: inline-block">-->
                <#--                    <label class="layui-form-label">卧室数量：</label>-->
                <#--                    <div class="layui-input-inline" style="width: 70px;">-->
                <#--                        <input disabled value="${roomInfo.propertyInfo.bedRoomCount!}" class="layui-input">-->
                <#--                    </div>-->
                <#--                </div>-->
                <#--                <div class="layui-form-item" style="display: inline-block">-->
                <#--                    <label class="layui-form-label">床位数量：</label>-->
                <#--                    <div class="layui-input-inline" style="width: 70px;">-->
                <#--                        <input disabled value="${roomInfo.propertyInfo.bedCount!}" class="layui-input">-->
                <#--                    </div>-->
                <#--                </div>-->
                <#--                <div class="layui-form-item" style="display: inline-block">-->
                <#--                    <label class="layui-form-label">卫生间数量：</label>-->
                <#--                    <div class="layui-input-inline" style="width: 70px;">-->
                <#--                        <input disabled value="${roomInfo.propertyInfo.bathRoomCount!}" class="layui-input">-->
                <#--                    </div>-->
                <#--                </div>-->
                <#--                <div class="layui-form-item" style="display: inline-block">-->
                <#--                    <label class="layui-form-label">最多可住：</label>-->
                <#--                    <div class="layui-input-inline" style="width: 70px;">-->
                <#--                        <input disabled value="${roomInfo.propertyInfo.peopleCount!}" class="layui-input">-->
                <#--                    </div>-->
                <#--                </div>-->
            </from>
        </div>
    </div>
</div>
</body>
<script>
    $("body").on("click", ".layer-photos-demo img", function (e) {
        layer.photos({
            photos: ".layer-photos-demo"
            , anim: 5
        });
    });
</script>
<style>
    .layui-form-item {
        margin-bottom: 40px;
    }

    .layui-form-item label {
        font-size: 20px;
        font-family: 'kaiti';
        color: #6bb6b5;
        width: 200px;
    }

    .layui-form-item input {
        cursor: not-allowed;
        background-color: #f4f4f4;
    }

    .layui-form-item span {
        font-size: 23px;
        font-family: 'kaiti';
        font-weight: bold;
        letter-spacing: 5px;
        color: #77724c;
    }
</style>
</html>
