package ru.party.meeting.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.party.meeting.exception.NotFoundException;
import ru.party.meeting.model.Role;
import ru.party.meeting.repository.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(String name) throws NotFoundException {
        return roleRepository.findByName(name).orElseThrow(NotFoundException::new);
    }

    public Role createRole(String name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(new Role(name)));
    }

    public List<Role> getAll() {
        return roleRepository.findAll();
    }
}
