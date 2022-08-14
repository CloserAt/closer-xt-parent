package com.closer.xt.web.domain;

import com.closer.xt.pojo.Invite;
import com.closer.xt.web.domain.repository.InviteDomainRepository;
import com.closer.xt.web.model.params.InviteParams;

public class InviteDomain {
    private InviteDomainRepository inviteDomainRepository;
    private InviteParams inviteParams;
    public InviteDomain(InviteDomainRepository inviteDomainRepository, InviteParams inviteParams) {
        this.inviteDomainRepository = inviteDomainRepository;
        this.inviteParams = inviteParams;
    }

    public void save(Invite invite) {
        inviteDomainRepository.save(invite);
    }
}
