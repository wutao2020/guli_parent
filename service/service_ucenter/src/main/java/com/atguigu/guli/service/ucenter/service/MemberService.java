package com.atguigu.guli.service.ucenter.service;

import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.ucenter.entity.Member;
import com.atguigu.guli.service.ucenter.entity.vo.LoginVo;
import com.atguigu.guli.service.ucenter.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author wutao
 * @since 2021-09-21
 */
public interface MemberService extends IService<Member> {

    void register(RegisterVo registerVo);

    String login(LoginVo loginVo);

    /**
     * 根据openid返回用户信息
     * @param openid
     * @return
     */
    Member getByOpenid(String openid);

    MemberDto getMemberDtoByMemberId(String memberId);
}
