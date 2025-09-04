package ru.otus.hw.services.acl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AclServiceWrapperServiceImpl implements AclServiceWrapperService {

    private final MutableAclService mutableAclService;


    @Override
    public void createPermission(Object object) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Sid owner = new PrincipalSid(authentication);
        final Sid userRole = new GrantedAuthoritySid("ROLE_USER");
        ObjectIdentity oid = new ObjectIdentityImpl(object);
        final Sid admin = new GrantedAuthoritySid("ROLE_ADMIN");

        MutableAcl acl = mutableAclService.createAcl(oid);

        acl.insertAce(acl.getEntries().size(), BasePermission.READ, userRole, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, owner, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, admin, true);
        mutableAclService.updateAcl(acl);
    }

    @Override
    public void deletePermissions(Object object) {
        ObjectIdentity oid = new ObjectIdentityImpl(object);
        mutableAclService.deleteAcl(oid, true);
    }


}
