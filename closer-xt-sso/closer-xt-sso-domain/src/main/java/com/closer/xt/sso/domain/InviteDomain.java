package com.closer.xt.sso.domain;

import com.closer.xt.sso.dao.data.Invite;
import com.closer.xt.sso.domain.repository.InviteDomainRepository;
import com.closer.xt.sso.model.params.InviteParams;

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
