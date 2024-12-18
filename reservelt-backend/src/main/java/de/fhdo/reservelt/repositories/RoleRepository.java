package de.fhdo.reservelt.repositories;

import de.fhdo.reservelt.domain.Role;
import de.fhdo.reservelt.domain.enums.RoleName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByRoleName(RoleName roleName);
}
