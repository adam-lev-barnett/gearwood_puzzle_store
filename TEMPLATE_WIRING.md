# Template Wiring TODO

Tracking what each Thymeleaf template expects so the layouts can be wired to real
controller/service logic. The HTML structure exists; the items below are the
placeholders to fill in. CSS files are stubbed and left for styling.

**Conventions used in the templates**
- DTOs are records → accessed with method syntax in Thymeleaf: `${dto.field()}`.
- Optional/not-yet-wired data uses safe navigation (`${user?.firstName()}`) and
  `th:if="${#lists.isEmpty(...)}"` empty-states so pages render before wiring.
- Forms post with `name` attributes matching the field names below.

> Route note: several links assume routes that don't exist yet (see "Routes to add").
> The existing `AccountController` currently maps `/account/dashboard`, `/profile`,
> `/settings` — reconcile those with the `/account`, `/account/edit`, `/account/password`
> links below (change either the templates or the controller).

---

## home.html
- **Model:** `categories` (Category.values()), `featuredProducts` (`List<ProductSummaryDto>`).
- Reuses `fragments/productCard`.

## registration.html
- **Model:** `error` (optional String).
- **POST `/register`** fields: `firstName, lastName, email, password, confirmPassword`.
- TODO: server-side validation (password ≥ 6, passwords match, duplicate email → `error`).

## login.html  *(already built by you)*
- POST `/login` with `email, password`. Add `?error` handling message if desired (screens show "Invalid email or password").

## account.html
- **Model:** `user` (`UserDto`), `orders` (list of order-summary DTOs).
- Order summary accessors used: `orderNumber()`, `orderStatus()`, `orderDateTime()`, `totalAmount()`.
- `user.roles()` currently prints the raw set — format to a friendly role label.

## editAccount.html
- **Model:** `user` (`UserDto`) for prefill. Email shown read-only (spec §3.1 — not editable).
- **POST `/account/edit`** fields: `firstName, lastName`.

## changePassword.html
- **Model:** `error` (optional).
- **POST `/account/password`** fields: `currentPassword, newPassword, confirmPassword`.

## cart.html
- **Model:** `cartItems` (list), `cart` (summary object).
  - item accessors: `productCode(), name(), imgSrc(), category(), difficulty(), unitPrice(), quantity(), lineTotal()`.
  - summary accessors: `subtotal(), shipping(), freeShipping() (boolean), tax(), total()`.
- **POST `/cart/update`** fields: `productCode, quantity`.
- **POST `/cart/remove`** fields: `productCode`.
- TODO: build a cart-item DTO + cart-summary DTO (tax 8.25%, free shipping ≥ $75).

## checkout.html
- **Model:** `cartItems`, `cart` (same as cart page), `error` (optional, e.g. payment declined).
- **POST `/checkout`** fields:
  - shipping: `name, address, address2, city, state, zip`  (matches `ShippingInfo`)
  - payment: `cardholderName, cardNumber, expirationMonth, expirationYear, cvv`
- TODO: validate (empty cart, inactive product, missing fields), call payment processor,
  re-render with `error` on decline (do NOT clear cart on decline — spec §5.2).

## checkoutSuccess.html
- **Model:** `orderNumber` (String, e.g. `ORD-7E9F7591`).

## orderHistory.html
- **Model:** `orders` (same order-summary DTO as account.html).

## orderDetails.html  *(optional section §6.4)*
- **Model:** `order` — a full order DTO with:
  - `orderNumber(), orderDateTime(), orderStatus(), transactionId(), totalAmount()`
  - `shippingInfo()` → `name(), address(), address2(), city(), state(), zip()`
  - `items()` → each line item: `imgSrc(), productName(), quantity(), unitPrice(), lineTotal()`
- **Route:** `GET /account/orders/{orderNumber}` (linked from account.html + orderHistory.html).
- TODO: enforce that a user can only view **their own** order (spec §3.2) — 404/redirect otherwise.

## error.html
- **Model:** `errorMessage` (optional friendly String). Never render raw exceptions/stack traces (spec §13).

## productCreate.html  *(admin only)*
- **Model:** `manufacturers` (`List<ManufacturerDto>`), `difficulties`, `categories`, `error` (optional).
- **POST `/products/new`** fields: `productCode, name, manufacturer, numberOfPieces, difficulty, category, price, acquiredDate, shortDescription, longDescription`.
- NOTE: manufacturer `<select>` value is currently the manufacturer **name** (`ManufacturerDto` has no id).
  If you'd rather bind by id, add an id to `ManufacturerDto` and switch the option value.
- NOTE: `acquiredDate` is optional in the form; entity defaults it to today via `@PrePersist` if blank.
- Screens also show a **Wholesale Cost** field — intentionally omitted (not on `Product` entity / not in spec).

## productEdit.html  *(admin only)*
- **Model:** `product` (`ProductAdminDto` = summary + acquiredDate), plus `manufacturers/difficulties/categories`, `error`.
- Prefills via `${product.summary().xxx()}` and `${product.acquiredDate()}`.
- **POST `/products/{code}/edit`** — same fields as create.

---

## Routes to add (controllers/handlers)
- `GET /register` (exists) and `POST /register`
- `GET /account`, `GET /account/edit` + `POST /account/edit`
- `GET /account/password` + `POST /account/password`
- `GET /account/orders/{orderNumber}` (Order Details — optional §6.4) + a list view
- `GET /cart`, `POST /cart/update`, `POST /cart/remove`, `POST /cart/add` (from product details)
- `GET /checkout` + `POST /checkout`, `GET /checkout/success/{orderNumber}`
- `GET /products/new` + `POST /products/new`  (admin)
- `GET /products/{code}/edit` + `POST /products/{code}/edit`  (admin)
- `POST /products/{code}/activate` + `/deactivate`  (admin)

## Gaps on already-built pages (productDetails.html / productCatalog.html)
- **productDetails:** add-to-cart `<form action="">` is empty — point it at `POST /cart/add` and add
  the auth-conditional UI (spec §4.3): logged-out shows "login required" + Login button;
  logged-in shows quantity + Add to Cart. Add admin Edit / Activate-Deactivate buttons (§7.3).
  Block direct access to inactive products for non-admins (§8.1).
- **productCatalog:** show the result count "Products (N)" (§2.3); for admins show an
  "Add Product" button and an active/inactive status badge on each card (§7.3).
