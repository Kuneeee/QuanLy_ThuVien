package service;

import entity.MuonTra;
import entity.ThongBaoMau;
import entity.ViPhamPhong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import repository.MuonTraRepository;
import repository.ThongBaoMauRepository;
import repository.ViPhamPhongRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ThongBaoThongKeService {

    @Autowired
    private ThongBaoMauRepository thongBaoMauRepository;

    @Autowired
    private MuonTraRepository muonTraRepository;

    @Autowired
    private ViPhamPhongRepository viPhamPhongRepository;

    public List<ThongBaoMau> getAllMau() {
        return thongBaoMauRepository.findAll(Sort.by(Sort.Direction.DESC, "ngayCapNhat"));
    }

    public Optional<ThongBaoMau> getMauById(Long id) {
        return thongBaoMauRepository.findById(id);
    }

    public ThongBaoMau saveMau(ThongBaoMau form) {
        if (form.getLoaiThongBao() == null || form.getLoaiThongBao().isBlank()) {
            throw new IllegalArgumentException("Loại thông báo không được để trống");
        }
        if (form.getTieuDe() == null || form.getTieuDe().isBlank()) {
            throw new IllegalArgumentException("Tiêu đề không được để trống");
        }
        if (form.getNoiDung() == null || form.getNoiDung().isBlank()) {
            throw new IllegalArgumentException("Nội dung không được để trống");
        }

        LocalDateTime now = LocalDateTime.now();
        ThongBaoMau entity = form.getId() != null
                ? thongBaoMauRepository.findById(form.getId()).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy mẫu thông báo"))
                : new ThongBaoMau();

        if (entity.getNgayTao() == null) {
            entity.setNgayTao(now);
        }
        entity.setMaMau(form.getMaMau() == null || form.getMaMau().isBlank() ? taoMaMauTuDong(form.getLoaiThongBao()) : form.getMaMau().trim());
        entity.setLoaiThongBao(form.getLoaiThongBao().trim());
        entity.setTieuDe(form.getTieuDe().trim());
        entity.setNoiDung(form.getNoiDung().trim());
        entity.setDoiTuong(form.getDoiTuong() == null ? "Toàn thư viện" : form.getDoiTuong().trim());
        entity.setKenhGui(form.getKenhGui() == null || form.getKenhGui().isBlank() ? "Trong hệ thống" : form.getKenhGui().trim());
        entity.setTrangThai(form.getTrangThai() == null || form.getTrangThai().isBlank() ? "Nháp" : form.getTrangThai().trim());
        entity.setNgayGui("Đã gửi".equalsIgnoreCase(entity.getTrangThai()) ? now : form.getNgayGui());
        entity.setNgayCapNhat(now);

        return thongBaoMauRepository.save(entity);
    }

    public void deleteMau(Long id) {
        if (id != null && thongBaoMauRepository.existsById(id)) {
            thongBaoMauRepository.deleteById(id);
        }
    }

    public List<MuonTra> getMuonTraTheoKy(String ky, String namHoc) {
        return filterMuonTra(ky, namHoc).stream()
                .sorted(Comparator.comparing(MuonTra::getNgayBan, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .collect(Collectors.toList());
    }

    public List<ViPhamPhong> getViPhamTheoKy(String ky, String namHoc) {
        return filterViPham(ky, namHoc).stream()
                .sorted(Comparator.comparing(ViPhamPhong::getNgayGhiNhan, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .collect(Collectors.toList());
    }

    public Map<String, Long> thongKeTrangThaiMuonTra(List<MuonTra> list) {
        Map<String, Long> counts = new LinkedHashMap<>();
        counts.put("Đang mượn", 0L);
        counts.put("Đã trả", 0L);
        counts.put("Đặt trước", 0L);
        counts.put("Khác", 0L);
        for (MuonTra item : list) {
            String key = item.getTrangThai();
            if (key == null || key.isBlank()) {
                key = "Khác";
            } else if (!counts.containsKey(key)) {
                key = "Khác";
            }
            counts.put(key, counts.get(key) + 1);
        }
        return counts;
    }

    public Map<String, Long> thongKeViPhamNguoiDung(List<ViPhamPhong> list) {
        return list.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getTenDocGia() + " (" + item.getMaDocGia() + ")",
                        LinkedHashMap::new,
                        Collectors.counting()));
    }

    public long demMuonDangMuon(List<MuonTra> list) {
        return list.stream().filter(item -> "Đang mượn".equalsIgnoreCase(safeText(item.getTrangThai()))).count();
    }

    public long demMuonDaTra(List<MuonTra> list) {
        return list.stream().filter(item -> item.getNgayTra() != null || "Đã trả".equalsIgnoreCase(safeText(item.getTrangThai()))).count();
    }

    public long demMuonQuaHan(List<MuonTra> list) {
        return list.stream().filter(item -> item.tinhSoNgayTre() > 0).count();
    }

    public long demViPham(List<ViPhamPhong> list) {
        return list.size();
    }

    public Map<String, Long> thongKeLoaiThongBao(List<ThongBaoMau> list) {
        return list.stream().collect(Collectors.groupingBy(
                item -> safeText(item.getLoaiThongBao()),
                LinkedHashMap::new,
                Collectors.counting()));
    }

    public String xuatBaoCaoCsv(String ky, String namHoc) {
        List<MuonTra> muonTraList = getMuonTraTheoKy(ky, namHoc);
        List<ViPhamPhong> viPhamList = getViPhamTheoKy(ky, namHoc);
        List<ThongBaoMau> mauList = getAllMau();
        Map<String, Long> trangThaiMap = thongKeTrangThaiMuonTra(muonTraList);
        Map<String, Long> viPhamMap = thongKeViPhamNguoiDung(viPhamList);

        StringBuilder builder = new StringBuilder();
        builder.append("Bao cao thong ke,Ky,").append(resolveKyLabel(ky)).append("\n");
        builder.append("Nam hoc,").append(resolveNamHoc(namHoc)).append("\n");
        builder.append("Tong mau thong bao,").append(mauList.size()).append("\n");
        builder.append("Tong phieu muon tra,").append(muonTraList.size()).append("\n");
        builder.append("Tong vi pham,").append(viPhamList.size()).append("\n\n");
        builder.append("Trang thai muon tra,So luong\n");
        for (Map.Entry<String, Long> entry : trangThaiMap.entrySet()) {
            builder.append(entry.getKey()).append(',').append(entry.getValue()).append('\n');
        }
        builder.append("\nVi pham nguoi dung,So luong\n");
        for (Map.Entry<String, Long> entry : viPhamMap.entrySet()) {
            builder.append(escapeCsv(entry.getKey())).append(',').append(entry.getValue()).append('\n');
        }
        builder.append("\nDanh sach mau thong bao,Loai,Tieu de,Trang thai\n");
        for (ThongBaoMau mau : mauList) {
            builder.append(escapeCsv(mau.getMaMau())).append(',')
                    .append(escapeCsv(mau.getLoaiThongBao())).append(',')
                    .append(escapeCsv(mau.getTieuDe())).append(',')
                    .append(escapeCsv(mau.getTrangThai())).append('\n');
        }
        return builder.toString();
    }

    public String resolveNamHoc(String namHoc) {
        if (namHoc != null && namHoc.matches("\\d{4}-\\d{4}")) {
            return namHoc;
        }
        LocalDate now = LocalDate.now();
        int startYear = now.getMonthValue() >= 9 ? now.getYear() : now.getYear() - 1;
        return startYear + "-" + (startYear + 1);
    }

    public String resolveKyLabel(String ky) {
        if (ky == null || ky.isBlank() || "CA_NAM".equalsIgnoreCase(ky)) {
            return "Cả năm";
        }
        if ("HK1".equalsIgnoreCase(ky)) {
            return "Học kỳ 1";
        }
        if ("HK2".equalsIgnoreCase(ky)) {
            return "Học kỳ 2";
        }
        return ky;
    }

    private List<MuonTra> filterMuonTra(String ky, String namHoc) {
        Range range = resolveRange(ky, namHoc);
        return muonTraRepository.findAll().stream()
                .filter(item -> item.getNgayBan() != null)
                .filter(item -> !item.getNgayBan().isBefore(range.start) && !item.getNgayBan().isAfter(range.end))
                .collect(Collectors.toList());
    }

    private List<ViPhamPhong> filterViPham(String ky, String namHoc) {
        Range range = resolveRange(ky, namHoc);
        return viPhamPhongRepository.findAll().stream()
                .filter(item -> item.getNgayGhiNhan() != null)
                .filter(item -> !item.getNgayGhiNhan().isBefore(range.start) && !item.getNgayGhiNhan().isAfter(range.end))
                .collect(Collectors.toList());
    }

    private Range resolveRange(String ky, String namHoc) {
        String resolvedNamHoc = resolveNamHoc(namHoc);
        int startYear = Integer.parseInt(resolvedNamHoc.substring(0, 4));
        LocalDateTime start = LocalDate.of(startYear, Month.SEPTEMBER, 1).atStartOfDay();
        LocalDateTime hk1End = LocalDate.of(startYear, Month.DECEMBER, 31).atTime(23, 59, 59);
        LocalDateTime hk2Start = LocalDate.of(startYear + 1, Month.JANUARY, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(startYear + 1, Month.AUGUST, 31).atTime(23, 59, 59);

        if ("HK1".equalsIgnoreCase(ky)) {
            return new Range(start, hk1End);
        }
        if ("HK2".equalsIgnoreCase(ky)) {
            return new Range(hk2Start, end);
        }
        return new Range(start, end);
    }

    private String taoMaMauTuDong(String loaiThongBao) {
        String prefix = "TB";
        if (loaiThongBao != null) {
            String normalized = loaiThongBao.toLowerCase();
            if (normalized.contains("báo cáo") || normalized.contains("bao cao")) {
                prefix = "BC";
            } else if (normalized.contains("hạn trả") || normalized.contains("han tra")) {
                prefix = "HT";
            } else if (normalized.contains("sự kiện") || normalized.contains("su kien")) {
                prefix = "SK";
            }
        }
        String finalPrefix = prefix;
        int max = thongBaoMauRepository.findAll().stream()
                .map(ThongBaoMau::getMaMau)
                .filter(code -> code != null && code.startsWith(finalPrefix))
                .map(code -> code.substring(finalPrefix.length()))
                .mapToInt(this::parseSafeInt)
                .max()
                .orElse(0);
        return finalPrefix + String.format("%03d", max + 1);
    }

    private int parseSafeInt(String value) {
        try {
            return Integer.parseInt(value.replaceAll("[^0-9]", ""));
        } catch (Exception ex) {
            return 0;
        }
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        return '"' + value.replace("\"", "\"\"") + '"';
    }

    private String safeText(String value) {
        return value == null || value.isBlank() ? "Khác" : value.trim();
    }

    private static class Range {
        private final LocalDateTime start;
        private final LocalDateTime end;

        private Range(LocalDateTime start, LocalDateTime end) {
            this.start = start;
            this.end = end;
        }
    }
}