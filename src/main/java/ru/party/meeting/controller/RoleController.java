package ru.party.meeting.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.party.meeting.dto.RoleTO;
import ru.party.meeting.service.RoleService;
import ru.party.meeting.transformer.UserTransformer;

@Controller
@RequestMapping(path = "/api")
public class RoleController {

    private final RoleService roleService;
    private final UserTransformer userTransformer = new UserTransformer();

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping(path = "/roles")
    public ResponseEntity<RoleTO> createRole(@RequestParam(name = "name") String name) {
        return ResponseEntity.ok(
                userTransformer.transform(roleService.createRole(name)));
    }

    @GetMapping(path = "/roles")
    public ResponseEntity<List<RoleTO>> findRoleByName(@RequestParam(required = false, name = "name") String name) {
        if (name == null) {
            return ResponseEntity.ok(
                    roleService.getAll().stream()
                            .map(userTransformer::transform)
                            .collect(Collectors.toList()));
        }
        return ResponseEntity.ok(
                List.of(userTransformer.transform(roleService.findByName(name))));
    }
}
