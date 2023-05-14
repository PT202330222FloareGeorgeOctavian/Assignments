package model;

import java.util.Objects;

public record Bill(int client_id, int product_id, int total) {
}
