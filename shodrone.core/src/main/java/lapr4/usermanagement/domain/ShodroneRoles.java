package lapr4.usermanagement.domain;

import eapli.framework.infrastructure.authz.domain.model.Role;

public final class ShodroneRoles {

    /**
     * poweruser
     */
    public static final Role POWER_USER = Role.valueOf("POWER_USER");

    /**
     * System Administrator.
     */
    public static final Role ADMIN = Role.valueOf("ADMIN");

    /**
     * Customer Relationship Manager.
     */
    public static final Role CRM_MANAGER = Role.valueOf("CRM_MANAGER");

    /**
     * CRM Collaborator.
     */
    public static final Role CRM_COLLABORATOR = Role.valueOf("CRM_COLLABORATOR");

    /**
     * Show Designer.
     */
    public static final Role SHOW_DESIGNER = Role.valueOf("SHOW_DESIGNER");

    /**
     * Drone Tech.
     */
    public static final Role DRONE_TECH = Role.valueOf("DRONE_TECH");

    /**
     * Customer Representative.
     */
    public static final Role REPRESENTATIVE = Role.valueOf("REPRESENTATIVE");

    /**
     * Get all available system roles.
     *
     * @return list of all roles
     */
    public static Role[] allRoles() {
        return new Role[] {
                ADMIN,
                CRM_MANAGER,
                CRM_COLLABORATOR,
                SHOW_DESIGNER,
                DRONE_TECH,
                REPRESENTATIVE
        };
    }

    /**
     * Checks if a role is recognized by the system.
     *
     * @param role the role to check
     * @return {@code true} if the role is valid
     */
    public static boolean isRecognizedRole(final Role role) {
        for (Role r : allRoles()) {
            if (r.equals(role)) return true;
        }
        return false;
    }

}
