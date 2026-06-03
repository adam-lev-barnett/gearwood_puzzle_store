# Corrected reference — what changed and why

These files are a **reference only**. They live outside `src/`, use the original
`edu.hield.security` package names, and are **not** compiled into your project.
They show how I'd restructure the class example to fix the two issues you spotted
(plus a few related ones). Your own project is untouched.

## The two issues you raised

### 1. Controller passed `Model` into the service
**Before:** `userService.prepareDashboardModel(model)`, `prepareProfileModel(model)`,
`prepareSettingsModel(model)` — the service imported `org.springframework.ui.Model`
and assembled the view.

**After:** the service returns plain data; the **controller** is the only thing that
calls `model.addAttribute(...)`.
- `prepare*Model(Model)` → `getCurrentUser()` returns a `User`, and
  `getSettings()` returns a `SettingsView` record.
- The service no longer imports anything from `org.springframework.ui`.
- Bonus: it stopped pushing the `Authentication` object into the view; the
  manager check is reduced to a boolean flag inside `SettingsView`.

### 2. Overlapping business logic
**Before:**
- Name/email were copied in the controller (`updateSettings`) **and** again inside
  `updateUserSettings`.
- `storeProfilePicture` set the picture and saved the user, then the controller
  set it again and called `updateUser` — 2–3 saves per action. Same in `registerUser`.
- Two identical "current user" lookups: `getCurrentUserContext()` and `getCurrentUser()`.

**After:**
- `updateProfile(form, picture)` and `register(form, picture)` each own the whole
  action and save **exactly once**.
- `storeProfilePicture(...)` is now a private helper that **returns a filename and
  nothing else** — it never saves the user, so callers can't double-save.
- One current-user lookup: `getCurrentUser()`.
- Controller does zero field-copying.

## Related fixes worth noticing

| Area | Before | After |
|---|---|---|
| Form binding | `@ModelAttribute("user") User` (entity) | `ProfileUpdateForm` / `RegistrationForm` DTOs — prevents over-posting, decouples view from entity |
| Exceptions | broad `catch (Exception)`, `printStackTrace()`, `"...: " + ex.getMessage()` flashed to the user | catch the **specific** expected case (`EmailAlreadyExistsException`) whose message is user-safe; let unexpected ones hit a global handler (no stack-trace leak — see §13) |
| Transactions | none | `@Transactional` on writes, `@Transactional(readOnly = true)` on reads |
| Manager authorization | implicit, mixed into `updateUserSettings` | explicit `reassignTeam(...)` that checks the role itself |
| Duplicate upload endpoint | separate `POST /users/{id}/upload-profile-picture` overlapped the settings save path | dropped; picture upload happens as part of `updateProfile` |

## Things intentionally left for you
- **`EmailAlreadyExistsException` / `ProfilePictureStorageException`** are illustrative
  custom exceptions — create them (extend `RuntimeException`) if you adopt this pattern.
- A **`@ControllerAdvice`** global handler is the clean home for unexpected exceptions:
  ```java
  @ControllerAdvice
  class GlobalExceptionHandler {
      @ExceptionHandler(Exception.class)
      public String handleUnexpected(Exception ex, Model model) {
          // log ex server-side; show a friendly page (no stack trace) — spec §13
          model.addAttribute("errorMessage", "Something went wrong. Please try again.");
          return "error";
      }
  }
  ```
- `AuthService.loginAndCreateJwtCookie(username, password)` is shown taking credentials
  instead of a `User` entity — adjust your `AuthService` signature if you follow this.

## The one-line takeaway
Controller = HTTP plumbing (bind, call one service method, pick a view/redirect).
Service = business logic + persistence, returns plain data. No `Model` or
`jakarta.servlet` types in a service signature; one save per business action.
