package edu.hield.security.dtos;

import edu.hield.security.entities.User;

import java.util.List;

/**
 * The data the settings page needs, assembled by the service and RETURNED to the
 * controller. The service no longer touches Spring's {@code Model} — that's the
 * controller's job. This object is plain data, so the service stays usable from
 * tests, REST endpoints, jobs, etc.
 *
 * (For an even cleaner separation you'd expose UserDto instead of the User entity
 *  here; kept as User to stay close to the original example.)
 */
public record SettingsView(
        User user,
        boolean manager,
        List<User> currentEmployees,
        List<User> availableUsers
) {}
