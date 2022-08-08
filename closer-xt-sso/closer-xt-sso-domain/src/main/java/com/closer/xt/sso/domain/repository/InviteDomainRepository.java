package com.closer.xt.sso.domain.repository;

import com.closer.xt.sso.dao.InviteMapper;
import com.closer.xt.sso.dao.data.Invite;
import com.closer.xt.sso.domain.InviteDomain;
import com.closer.xt.sso.model.params.InviteParams;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class InviteDomainRepository {
    @Resource
    private InviteMapper inviteMapper;

    public InviteDomain createDomain(InviteParams inviteParams) {
        return new InviteDomain(this,inviteParams);
    }

    public void save(Invite invite) {
        inviteMapper.insert(invite);
    }
}
