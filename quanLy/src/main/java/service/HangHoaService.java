package service;

import entity.HangHoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.HangHoaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HangHoaService {
    
    @Autowired
    private HangHoaRepository hangHoaRepository;
    
    // CREATE - Tạo mới hàng hóa
    public HangHoa createHangHoa(HangHoa hangHoa) {
        if (hangHoa.getHanghoaID() == null || hangHoa.getHanghoaID().isEmpty()) {
            hangHoa.setHanghoaID(taoMaHangHoaTuDong());
        }
        // Set ngày nhập hiện tại nếu chưa có
        if (hangHoa.getNgayNhap() == null) {
            hangHoa.setNgayNhap(LocalDateTime.now());
        }
        return hangHoaRepository.save(hangHoa);
    }
    
    // READ - Lấy tất cả hàng hóa
    public List<HangHoa> getAllHangHoa() {
        return hangHoaRepository.findAll();
    }
    
    // READ - Lấy hàng hóa theo ID
    public Optional<HangHoa> getHangHoaById(String id) {
        return hangHoaRepository.findById(id);
    }
    
    // UPDATE - Cập nhật hàng hóa
    public HangHoa updateHangHoa(String id, HangHoa hangHoaDetails) {
        return hangHoaRepository.findById(id)
                .map(hangHoa -> {
                    hangHoa.setTenHangHoa(hangHoaDetails.getTenHangHoa());
                    hangHoa.setLoaiHangHoa(hangHoaDetails.getLoaiHangHoa());
                    hangHoa.setSoLuongHangHoa(hangHoaDetails.getSoLuongHangHoa());
                    hangHoa.setGiaNhap(hangHoaDetails.getGiaNhap());
                    hangHoa.setNhaSanXuat(hangHoaDetails.getNhaSanXuat());
                    hangHoa.setNamSanXuat(hangHoaDetails.getNamSanXuat());
                    return hangHoaRepository.save(hangHoa);
                })
                .orElse(null);
    }
    
    // DELETE - Xóa hàng hóa
    public boolean deleteHangHoa(String id) {
        if (hangHoaRepository.existsById(id)) {
            hangHoaRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // UTILITY - Tạo mã tự động
    private String taoMaHangHoaTuDong() {
        return hangHoaRepository.findAll().stream()
                .filter(h -> h.getHanghoaID() != null && !h.getHanghoaID().isEmpty())
                .map(h -> h.getHanghoaID())
                .filter(id -> id.startsWith("HH"))
                .map(id -> {
                    try {
                        return Integer.parseInt(id.substring(2));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max(Integer::compareTo)
                .map(max -> "HH" + String.format("%03d", max + 1))
                .orElse("HH001");
    }
    
    // BUSINESS - Tính tổng giá trị kho
    public BigDecimal tinhTongGiaTriKho() {
        return hangHoaRepository.findAll().stream()
                .map(h -> h.getGiaNhap().multiply(new BigDecimal(h.getSoLuongHangHoa())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
