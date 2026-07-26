package service;

import entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.*;

import javax.sql.DataSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
public class HeThongService {

    @Autowired
    private CauHinhHeThongRepository cauHinhHeThongRepository;

    @Autowired
    private NhatKyHoatDongRepository nhatKyHoatDongRepository;

    @Autowired
    private PhienDangNhapRepository phienDangNhapRepository;

    @Autowired
    private SaoLuuDuLieuRepository saoLuuDuLieuRepository;

    @Autowired
    private DataSource dataSource;

    private static final String BACKUP_DIR = "data/backup";

    // ===== Cấu hình hệ thống =====

    public List<CauHinhHeThong> getAllCauHinh() {
        return cauHinhHeThongRepository.findAll();
    }

    public Optional<CauHinhHeThong> getCauHinhById(Long id) {
        return cauHinhHeThongRepository.findById(id);
    }

    public List<CauHinhHeThong> getCauHinhByNhom(String nhom) {
        return cauHinhHeThongRepository.findByNhomOrderByMaAsc(nhom);
    }

    public Optional<CauHinhHeThong> getCauHinhByMa(String ma) {
        return cauHinhHeThongRepository.findByMa(ma);
    }

    public CauHinhHeThong saveCauHinh(CauHinhHeThong cauHinh) {
        cauHinh.setNgayCapNhat(LocalDateTime.now());
        if (cauHinh.getNgayTao() == null) {
            cauHinh.setNgayTao(LocalDateTime.now());
        }
        return cauHinhHeThongRepository.save(cauHinh);
    }

    public String getCauHinhValue(String ma) {
        return cauHinhHeThongRepository.findByMa(ma)
                .map(CauHinhHeThong::getGiaTri)
                .orElse(null);
    }

    // ===== Nhật ký hoạt động =====

    public List<NhatKyHoatDong> getAllNhatKy() {
        return nhatKyHoatDongRepository.findAllByOrderByThoiGianDesc();
    }

    public List<NhatKyHoatDong> getNhatKyByLoai(String loai) {
        return nhatKyHoatDongRepository.findByLoaiOrderByThoiGianDesc(loai);
    }

    public void ghiNhatKy(String loai, String hanhDong, String nguoiThucHien, String diaChiIp, String chiTiet) {
        NhatKyHoatDong log = new NhatKyHoatDong(loai, hanhDong, nguoiThucHien, diaChiIp, chiTiet, "THANH_CONG");
        nhatKyHoatDongRepository.save(log);
    }

    // ===== Phiên đăng nhập =====

    public List<PhienDangNhap> getAllPhienDangNhap() {
        return phienDangNhapRepository.findAllByOrderByThoiGianDangNhapDesc();
    }

    public List<PhienDangNhap> getPhienDangNhapActive() {
        return phienDangNhapRepository.findByTrangThaiOrderByThoiGianDangNhapDesc("ACTIVE");
    }

    public void savePhienDangNhap(PhienDangNhap phien) {
        phienDangNhapRepository.save(phien);
    }

    public void huyPhienDangNhap(Long id) {
        phienDangNhapRepository.findById(id).ifPresent(phien -> {
            phien.setTrangThai("LOGGED_OUT");
            phienDangNhapRepository.save(phien);
        });
    }

    // ===== Sao lưu & phục hồi =====

    private static final List<String> ALL_TABLES = List.of(
            "tai_lieu", "doc_gia", "muon_tra", "nhap_tai_lieu",
            "dat_phong", "lich_trong_phong", "vi_pham_phong", "thong_bao_mau",
            "cau_hinh_he_thong", "nhat_ky_hoat_dong", "phien_dang_nhap"
    );

    private static final List<String> ID_TABLES = List.of(
            "cau_hinh_he_thong", "nhat_ky_hoat_dong", "phien_dang_nhap"
    );

    public List<SaoLuuDuLieu> getAllSaoLuu() {
        return saoLuuDuLieuRepository.findAllByOrderByNgayTaoDesc();
    }

    public String taoSaoLuu(String loai) {
        try {
            File backupDir = new File(BACKUP_DIR);
            if (!backupDir.exists()) backupDir.mkdirs();

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "backup_" + timestamp + ".zip";
            String filePath = BACKUP_DIR + "/" + fileName;

            StringBuilder sqlDump = new StringBuilder();
            sqlDump.append("-- Backup created: ").append(timestamp).append("\n\n");

            try (Connection conn = dataSource.getConnection()) {
                DatabaseMetaData meta = conn.getMetaData();
                Set<String> existingTables = new HashSet<>();
                try (ResultSet rs = meta.getTables(null, null, "%", new String[]{"TABLE"})) {
                    while (rs.next()) existingTables.add(rs.getString("TABLE_NAME").toLowerCase());
                }

                for (String table : ALL_TABLES) {
                    if (!existingTables.contains(table)) continue;
                    exportTable(conn, table, sqlDump);
                }
            }

            try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(filePath))) {
                zos.putNextEntry(new ZipEntry("backup.sql"));
                zos.write(sqlDump.toString().getBytes());
                zos.closeEntry();
            }

            File backupFile = new File(filePath);
            long size = backupFile.length();

            SaoLuuDuLieu saoLuu = new SaoLuuDuLieu(
                    fileName, size, loai, "HOAN_TAT",
                    loai.equals("AUTO") ? "Sao lưu tự động" : "Sao lưu thủ công"
            );
            saoLuuDuLieuRepository.save(saoLuu);

            return fileName;
        } catch (Exception e) {
            SaoLuuDuLieu saoLuu = new SaoLuuDuLieu(null, 0L, loai, "THAT_BAI", "Lỗi: " + e.getMessage());
            saoLuuDuLieuRepository.save(saoLuu);
            throw new RuntimeException("Sao lưu thất bại: " + e.getMessage());
        }
    }

    private void exportTable(Connection conn, String tableName, StringBuilder sql) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();

            List<String> colNames = new ArrayList<>();
            for (int i = 1; i <= colCount; i++) {
                colNames.add(rsmd.getColumnName(i));
            }

            boolean hasRows = false;
            while (rs.next()) {
                if (!hasRows) {
                    sql.append("DELETE FROM ").append(tableName).append(";\n");
                    hasRows = true;
                }
                sql.append("INSERT INTO ").append(tableName).append(" (");
                sql.append(String.join(", ", colNames));
                sql.append(") VALUES (");
                for (int i = 1; i <= colCount; i++) {
                    if (i > 1) sql.append(", ");
                    Object val = rs.getObject(i);
                    if (val == null) {
                        sql.append("NULL");
                    } else if (val instanceof Number || val instanceof Boolean) {
                        sql.append(val);
                    } else if (val instanceof Timestamp) {
                        sql.append("'").append(val.toString()).append("'");
                    } else if (val instanceof java.sql.Date) {
                        sql.append("'").append(val.toString()).append("'");
                    } else {
                        String s = val.toString().replace("'", "''");
                        sql.append("'").append(s).append("'");
                    }
                }
                sql.append(");\n");
            }
            if (hasRows) sql.append("\n");
        }
    }

    public void xoaSaoLuu(Long id) {
        saoLuuDuLieuRepository.findById(id).ifPresent(sl -> {
            try {
                Path filePath = Paths.get(BACKUP_DIR, sl.getTenFile());
                Files.deleteIfExists(filePath);
            } catch (IOException ignored) {}
            saoLuuDuLieuRepository.delete(sl);
        });
    }

    public void khoiPhucDuLieu(Long id) {
        SaoLuuDuLieu sl = saoLuuDuLieuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản sao lưu"));

        Path zipPath = Paths.get(BACKUP_DIR, sl.getTenFile());
        if (!Files.exists(zipPath)) {
            throw new RuntimeException("File sao lưu không tồn tại: " + sl.getTenFile());
        }

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath.toFile()))) {
            ZipEntry entry = zis.getNextEntry();
            boolean found = false;
            while (entry != null) {
                if (entry.getName().equals("backup.sql")) {
                    found = true;
                    break;
                }
                entry = zis.getNextEntry();
            }
            if (!found) throw new RuntimeException("Không tìm thấy file backup.sql trong bản sao lưu");

            byte[] buffer = new byte[(int) entry.getSize()];
            zis.read(buffer, 0, buffer.length);
            String sql = new String(buffer);

            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {
                conn.setAutoCommit(false);
                try {
                    for (String statement : sql.split(";\n")) {
                        String s = statement.trim();
                        if (!s.isEmpty() && !s.startsWith("--")) {
                            stmt.execute(s);
                        }
                    }
                    conn.commit();
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Khôi phục thất bại: " + e.getMessage());
        }
    }
}
