package com.closer.xt.web.domain.repository;

import com.closer.xt.pojo.Invite;
import com.closer.xt.web.dao.InviteMapper;
import com.closer.xt.web.domain.InviteDomain;
import com.closer.xt.web.model.params.InviteParams;
import org.springframework.beans.factory.annotation.Autowired;

public class InviteDomainRepository {
    @Autowired
    private InviteMapper inviteMapper;

    public InviteDomain createDomain(InviteParams inviteParams) {
        return new InviteDomain(this,inviteParams);
    }

    public void save(Invite invite) {
        this.inviteMapper.insert(invite);
    }
}
