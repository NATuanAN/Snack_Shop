# TÀI LIỆU TESTING

## Tổng quan
- Hệ thống test sử dụng JUnit 5 + Mockito (Spring Boot starter test).
- Tập trung vào unit test cho Service layer.
- Có 1 test khởi động Spring context: SnackBeApplicationTests.
- Jacoco được bật khi chay `mvn test`, tạo report ở `target/site/jacoco/index.html`.

## Cấu trúc thư mục
- `src/test/java/com/Snack_BE/config`
  - `BaseTestConfig`: cung cấp `PasswordEncoder` cho test.
  - `TestDataHelper`: hàng số test và helper.
  - `RedisServiceTest`: test `tryReserve`/`updateStock`.
- `src/test/java/com/Snack_BE/Service`
  - `UserServiceTest`: login/register/oauth, `getAllUser`.
  - `ProductServiceTest`: lấy danh sách sản phẩm.
  - `ShopServiceTest`: lấy danh sách shop.
  - `ReviewServiceTest`: lấy danh sách review.
  - `PromotionServiceTest`: lấy danh sách khuyến mãi.
  - `SystemReportServiceTest`: lấy danh sách report.
  - `OrderItemServiceTest`: lấy order items va map DTO.
  - `OrderServiceTest`: tạo đơn, validate, Redis fallback, Kafka event, get order by user.
  - `MoMoServiceTest`: validation `createPayment`, xử lý IPN update status.
  - `WishListServiceTest`: lấy danh sách wishlist.
- `src/test/java/com/Snack_BE/SnackBeApplicationTests`: `contextLoads`.

## Cách chạy test
### Chạy tất cả
```bash
mvn test
```

### Chạy 1 class (Ví dụ: UserServiceTest)
```bash
mvn -Dtest=UserServiceTest test
```

### Bỏ qua test Spring context load nếu cần
```bash
mvn -Dtest=!SnackBeApplicationTests test
```

## Lưu ý
- Phần lớn test dùng Mockito nên không cần database/redis/kafka thật.
- Test tạo đơn dùng KafkaTemplate và RedisService đều là mock.
- Nếu không bỏ qua test Spring context load cần chạy Docker để có thể truy cập sever database/redis/kafka.
