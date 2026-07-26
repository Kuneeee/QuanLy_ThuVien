-- Seed data for local library database

MERGE INTO doc_gia (
    doc_gia_id, ten_doc_gia, so_dien_thoai, email, dia_chi, loai_doc_gia,
    tai_khoan, mat_khau, quyen_han,
    tong_tien_muon, so_lan_muon, tong_tien_phat, diem_tich_luy, ngay_tham_gia, ngay_tao
) KEY(doc_gia_id) VALUES
('DG001', 'Nguyễn Văn An', '0123456789', 'an@example.com', 'Hà Nội', 'Thường', 'an.nguyen', '123456', 'Sinh viên', 0, 0, 0, 0, CURRENT_DATE, CURRENT_TIMESTAMP),
('DG002', 'Trần Thị Bích', '0987654321', 'bich@example.com', 'Đà Nẵng', 'VIP', 'bich.tran', '123456', 'Giáo viên', 0, 0, 0, 0, CURRENT_DATE, CURRENT_TIMESTAMP),
('DG003', 'Lê Minh Cường', '0369852147', 'cuong@example.com', 'TP.HCM', 'Thường', 'cuong.le', '123456', 'Sinh viên', 0, 0, 0, 0, CURRENT_DATE, CURRENT_TIMESTAMP);

MERGE INTO tai_lieu (
    tai_lieu_id, ten_tai_lieu, so_luong_ton, tac_gia, nha_xuat_ban, nam_xuat_ban,
    ngay_nhap, gia_nhap, the_loai, vi_tri
) KEY(tai_lieu_id) VALUES
('TL001', 'Lập trình Java căn bản', 10, 'Nguyễn Văn A', 'NXB Giáo dục', 2022, CURRENT_TIMESTAMP, 120000, 'Khoa học', 'Kệ CNTT A1'),
('TL002', 'Cơ sở dữ liệu', 5, 'Trần Thị B', 'NXB Khoa học', 2021, CURRENT_TIMESTAMP, 150000, 'Khoa học', 'Kệ CNTT A2'),
('TL003', 'Những câu chuyện cười', 8, 'Lê Văn C', 'NXB Văn học', 2023, CURRENT_TIMESTAMP, 90000, 'Văn học', 'Kệ Văn học B1');

MERGE INTO muon_tra (
    ma_phieu_muon, tai_lieu_id, ten_tai_lieu, ten_doc_gia, doc_gia,
    ngay_muon, ngay_hen_tra, ngay_tra, trang_thai, so_ngay_gia_han, ghi_chu, gia_muon, so_luong_muon, tong_tien, gia_nhap
) KEY(ma_phieu_muon) VALUES
('PM001', 'TL001', 'Lập trình Java căn bản', 'Nguyễn Văn An', 'Nguyễn Văn An', CURRENT_TIMESTAMP, DATEADD('DAY', 14, CURRENT_TIMESTAMP), NULL, 'Đang mượn', 0, 'Mượn đầu tiên', 120000, 1, 120000, 120000);

MERGE INTO nhap_tai_lieu (
    tai_lieu_id, ten_tai_lieu, so_luong_nhap, gia_nhap, ngay_nhap,
    nha_cung_cap, nguoi_nhap, ghi_chu, trang_thai, ma_phieu_nhap
) KEY(ma_phieu_nhap) VALUES
('TL001', 'Lập trình Java căn bản', 10, 120000, CURRENT_TIMESTAMP, 'Nhà cung cấp A', 'Quản trị', 'Nhập mới', 'Đã nhập', 'PN001');
