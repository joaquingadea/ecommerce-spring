package com.api.ecommerce.users.api;

import com.api.ecommerce.users.application.IAppUserService;
import com.api.ecommerce.users.application.IPermissionService;
import com.api.ecommerce.users.application.IRoleService;
import com.api.ecommerce.users.domain.Permission;
import com.api.ecommerce.users.domain.Role;
import com.api.ecommerce.users.dto.response.UserIdUsernameDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    private IAppUserService appUserService;
    private IRoleService roleService;
    private IPermissionService permissionService;

    public AdminUserController(IAppUserService appUserService, IRoleService roleService, IPermissionService permissionService) {
        this.appUserService = appUserService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @GetMapping("/customer-list")
    public ResponseEntity<Page<UserIdUsernameDTO>> getCustomers(@PageableDefault(size = 10,page = 0) Pageable pageable){
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize());
        return ResponseEntity.status(HttpStatus.OK)
                .body(appUserService.findAllIdAndUsername(pageRequest));
    }


    @GetMapping("/get-roles")
    public ResponseEntity<List<Role>> getRoles(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(roleService.findAll());
    }
    @PostMapping("/create-role")
    public ResponseEntity<Role> createRole(@RequestBody Role role){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roleService.create(role));
    }
    @GetMapping("/get-permissions")
    public ResponseEntity<List<Permission>> getPermissions(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(permissionService.findAll());
    }
    @PostMapping("/create-permission")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(permissionService.create(permission));
    }
}
