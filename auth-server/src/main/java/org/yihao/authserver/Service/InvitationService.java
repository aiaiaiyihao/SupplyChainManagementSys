package org.yihao.authserver.Service;

public interface InvitationService {
    Boolean inviteSupplier(String email, String name);
    Boolean inviteDriver(String email, String name);
}
