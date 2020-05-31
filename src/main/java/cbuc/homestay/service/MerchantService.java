package cbuc.homestay.service;

import cbuc.homestay.CommonEnum.LevelEnum;
import cbuc.homestay.CommonEnum.StatusEnum;
import cbuc.homestay.bean.Merchant;
import cbuc.homestay.bean.MerchantExample;
import cbuc.homestay.evt.UserEvt;
import cbuc.homestay.mapper.MerchantMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @Explain: 商家处理器
 * @Author: Cbuc
 * @Version: 1.0
 * @Date: 2020/1/2
 */
@Service
public class MerchantService {
    @Autowired
    private MerchantMapper merchantMapper;

    public Merchant queryDetail(UserEvt userEvt) {
        MerchantExample merchantExample = new MerchantExample();
        MerchantExample.Criteria criteria = merchantExample.createCriteria();
//        if (StringUtils.isNotBlank(userEvt.getMaccount())) {
//            criteria.andMaccountEqualTo(userEvt.getMaccount());
//        }
//        if (StringUtils.isNotBlank(userEvt.getMpwd())) {
//            criteria.andMpwdEqualTo(userEvt.getMpwd());
//        }
        if (StringUtils.isNotBlank(userEvt.getMphone())) {
            criteria.andMphoneEqualTo(userEvt.getMphone());
        }
//        criteria.andStatusNotEqualTo(StatusEnum.D.getValue());
        List<Merchant> merchants = merchantMapper.selectByExample(merchantExample);
        return CollectionUtils.isEmpty(merchants) ? null : merchants.get(0);
    }

    public Merchant queryDetail(Long id) {
        return merchantMapper.selectByPrimaryKey(id);
    }

    public int doEdit(UserEvt userEvt) {
        Merchant merchant = new Merchant();
        if (userEvt.getMphone()!=null){
            merchant.setMphone(userEvt.getMphone());
        }
        if (userEvt.getMpwd()!=null){
            merchant.setMpwd(userEvt.getMpwd());
        }
        if (userEvt.getStatus()!=null){
            merchant.setStatus(userEvt.getStatus());
        }
//        merchant.setUpdateTime(new Date());
        MerchantExample merchantExample = new MerchantExample();
        MerchantExample.Criteria criteria = merchantExample.createCriteria();
        if (StringUtils.isNotBlank(userEvt.getMphone())) {
            criteria.andMphoneEqualTo(userEvt.getMphone());
        }
//        if (StringUtils.isNotBlank(userEvt.getStatus())) {
//            merchant.setStatus(userEvt.getStatus());
//        }
        return merchantMapper.updateByExampleSelective(merchant,merchantExample);
    }

    public int doAdd(Merchant merchant) {
        return merchantMapper.insertSelective(merchant);
    }

    public List<Merchant> queryList(String title) {
        MerchantExample merchantExample = new MerchantExample();
        MerchantExample.Criteria criteria = merchantExample.createCriteria();
        if (StringUtils.isNotBlank(title)) {
            criteria.andMnameLike("%" + title + "%");
        }
        criteria.andMlevelNotEqualTo(LevelEnum.ADMIN.getValue());
        merchantExample.setOrderByClause("id desc");
        return merchantMapper.selectByExample(merchantExample);
    }

    public Merchant getMerchant(Long id) {
        MerchantExample merchantExample = new MerchantExample();
        merchantExample.createCriteria().andAuditIdEqualTo(id);
        List<Merchant> merchantList = merchantMapper.selectByExample(merchantExample);
        return merchantList.size() > 0 ? merchantList.get(0) : null;
    }
}
