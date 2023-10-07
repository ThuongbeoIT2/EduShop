package com.example.ttversion1.shareds;


import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Constants {
    public static final String SEND_EMAIL_SUCCESS = "Đã gửi mã đến Email: ";
    public static final String RESEND_EMAIL_SUCCESS = "Đã gửi lại mã đến Email:";
    public static final String LOGIN_SUCCESS = "Đăng nhập thành công";
    public static final String LOGIN_FAIL_USERNAME = "Tên đăng nhập không tồn tại";
    public static final String LOGIN_FAIL_PASSWORD = "Mật khẩu không đúng";
    public static final String NOT_FOUND = "Tìm kiếm thất bại";
    public static final String USER_NOT_FOUND = "Người dùng với email tren không tồn tại";
    public static final String IMG_NOT_FOUND = "Tệp ảnh trống rỗng";
    public static final String ROLE_NOT_FOUND = "Không tồn tại phân cấp đuợc yêu cầu";
    public static final String ACCOUNT_EXIST = " Tài khoản gắn với email đuợc yêu cầu đã tồn tại";
    public static final String ACCOUNT_NOT_EXIST = " Tài khoản gắn với email của bạn không tồn tại";
    public static final String LOGIN = "Mời nhập mã OTP.";
    public static final String ALREADY_EXIST = "Đối tượng đã tồn tại";
    public static final String EXIST_EMAIL = "Tên đăng nhập hoặc mật khẩu không đúng";
    public static final String NOT_IMPLEMENT = "Không thể thực hiện tác vụ";
    public static final String UPDATE_SUCCESS = "Cập nhật thành công đối tượng";
    public static final String DELETE_SUCCESS = "Xóa thành công đối tượng";
    public static final String INSERT_SUCCESS = "Thêm thành công đối tượng";
    public static final String OK = "Lấy ra thành công";
     public static Date getCurrentDay() {
        Date currentDate = new Date();
        return new Date(currentDate.getTime());
    }
    public static String generateVoucherCode() {
        // Tạo một UUID mới
        UUID voucherUUID = UUID.randomUUID();

        // Chuyển UUID thành chuỗi và loại bỏ các ký tự gạch ngang
        String uuidStr = voucherUUID.toString().replaceAll("-", "");

        // Tạo một chuỗi ngẫu nhiên gồm 6 ký tự số
        String randomChars = generateRandomChars(6);

        // Kết hợp UUID và ký tự ngẫu nhiên
        String voucherCode = "MP" + uuidStr.substring(0, 6) + randomChars;

        return voucherCode;
    }
    private static String generateRandomChars(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            builder.append(chars.charAt(index));
        }

        return builder.toString();
    }
    // Tạo Otp 6 số
    public static String generateTempPwd(int length) {
        String numbers = "012345678";
        char otp[] = new char[length];
        Random getOtpNum = new Random();
        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(getOtpNum.nextInt(numbers.length()));
        }
        String optCode = "";
        for (int i = 0; i < otp.length; i++) {
            optCode += otp[i];
        }
        return optCode;
    }
}