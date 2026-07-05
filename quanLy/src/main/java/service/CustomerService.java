package service;

import entity.KhachHang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.CustomerRepositoryJPA;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepositoryJPA customerRepository;
    
    // CREATE - Tạo mới khách hàng
    public KhachHang createCustomer(KhachHang khachHang) {
        if (khachHang.getKhachHangId() == null || khachHang.getKhachHangId().isEmpty()) {
            khachHang.setKhachHangId(taoMaKhachHangTuDong());
        }
        return customerRepository.save(khachHang);
    }
    
    // READ - Lấy tất cả khách hàng
    public List<KhachHang> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    // READ - Lấy khách hàng theo ID
    public Optional<KhachHang> getCustomerById(String id) {
        return customerRepository.findById(id);
    }
    
    // UPDATE - Cập nhật khách hàng
    public KhachHang updateCustomer(String id, KhachHang khachHangDetails) {
        return customerRepository.findById(id)
                .map(khachHang -> {
                    khachHang.setTenKhachHang(khachHangDetails.getTenKhachHang());
                    khachHang.setSoDienThoai(khachHangDetails.getSoDienThoai());
                    khachHang.setEmail(khachHangDetails.getEmail());
                    khachHang.setDiaChi(khachHangDetails.getDiaChi());
                    khachHang.setLoaiKhachHang(khachHangDetails.getLoaiKhachHang());
                    return customerRepository.save(khachHang);
                })
                .orElse(null);
    }
    
    // DELETE - Xóa khách hàng
    public boolean deleteCustomer(String id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // UTILITY - Tạo mã tự động
    private String taoMaKhachHangTuDong() {
        List<KhachHang> allCustomers = customerRepository.findAll();
        int maxNumber = 0;
        
        for (KhachHang kh : allCustomers) {
            String id = kh.getKhachHangId();
            if (id != null && id.startsWith("KH") && id.length() > 2) {
                try {
                    int number = Integer.parseInt(id.substring(2));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid formats
                }
            }
        }
        
        return "KH" + String.format("%03d", maxNumber + 1);
    }
}
