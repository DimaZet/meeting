package ru.party.gateway.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.party.gateway.dto.RoleTO;
import ru.party.gateway.exception.NotFoundException;
import ru.party.gateway.service.RoleService;
import ru.party.gateway.transformer.UserTransformer;

@Controller
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    private final UserTransformer userTransformer = new UserTransformer();

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleTO> createRole(@RequestParam(name = "name") String name) {
        return ResponseEntity.ok(
                userTransformer.transform(roleService.createRole(name)));
    }

    @GetMapping
    public ResponseEntity<List<RoleTO>> getAll() {
        return ResponseEntity.ok(
                roleService.getAll().stream()
                        .map(userTransformer::transform)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{name}")
    public ResponseEntity<RoleTO> getByRoleName(@PathVariable(name = "name") String name)
            throws NotFoundException {
        return ResponseEntity.ok(
                userTransformer.transform(roleService.findByName(name)));
    }
}
