package edu.barnett.gearwood_puzzle_store.dtos;

import java.io.Serializable;

/**
 * An "Add to Cart" action attempted by an anonymous shopper. We can't persist a
 * cart line without a user, so the intent is held in the HttpSession across the
 * login hop and replayed once the user authenticates. Serializable so it can
 * live in a session safely.
 */
public record PendingCartAdd(String productCode, int quantity) implements Serializable {
}
