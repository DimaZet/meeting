package ru.party.meeting.service;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.party.meeting.model.Role;
import ru.party.meeting.repository.RoleRepository;

@Service
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Role createRole(String name) {
        Role role = findByName(name);
        return (role == null) ? roleRepository.save(new Role(name)) : role;
    }

    public List<Role> getAll() {
        return roleRepository.findAll();
    }
}
