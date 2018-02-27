package com.github.msx80.runtime.loader;

import java.security.AllPermission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.ProtectionDomain;

public class SandboxSecurityPolicy extends Policy {
    
    @Override
    public PermissionCollection getPermissions(ProtectionDomain domain) {
        if (isCartridge(domain)) {
            return pluginPermissions();
        }
        else {
            return applicationPermissions();
        }        
    }

    private boolean isCartridge(ProtectionDomain domain) {
        return domain.getClassLoader() instanceof CartridgeClassLoader;
    }

    private PermissionCollection pluginPermissions() {
        Permissions permissions = new Permissions(); // No permissions
        return permissions;
    }

    private PermissionCollection applicationPermissions() {
        Permissions permissions = new Permissions();
        permissions.add(new AllPermission());
        return permissions;
    }
}