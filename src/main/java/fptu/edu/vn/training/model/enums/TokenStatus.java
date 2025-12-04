package fptu.edu.vn.training.model.enums;

public enum TokenStatus {
    PENDING,    // đang chờ xác thực
    ACTIVE,     // đang hiệu lực
    USED,       // đã sử dụng
    EXPIRED,    // hết hạn
    REVOKED     // bị thu hồi / vô hiệu
}
