-- PostgreSQL Seed data for Neon Database with correct schema

-- Hàng Hóa (Products) - Insert into hang_hoa table
INSERT INTO hang_hoa(hanghoa_id, ten_hang_hoa, so_luong_hang_hoa, gia_nhap, loai_hang_hoa, nha_san_xuat, ngay_nhap)
VALUES
 ('HH001', 'Bánh quy Oreo', 100, 15000, 'Đồ ăn vặt', 'Mondelez', CURRENT_TIMESTAMP),
 ('HH002', 'Kẹo dẻo Haribo', 200, 12000, 'Đồ ăn vặt', 'Haribo', CURRENT_TIMESTAMP),
 ('HH003', 'Bánh tráng nướng', 50, 8000, 'Đồ ăn vặt', 'Homemade', CURRENT_TIMESTAMP),
 ('HH004', 'Trà sữa trân châu', 30, 20000, 'Đồ uống', 'Tea House', CURRENT_TIMESTAMP),
 ('HH005', 'Bánh mì nướng muối ớt', 25, 10000, 'Đồ ăn vặt', 'Saigon Bakery', CURRENT_TIMESTAMP),
 ('HH006', 'Chè thái', 20, 15000, 'Đồ uống', 'Chè Bà Ba', CURRENT_TIMESTAMP)
ON CONFLICT (hanghoa_id) DO NOTHING;

-- Khách Hàng (Customers) - Insert into khach_hang table  
INSERT INTO khach_hang(khach_hang_id, ten_khach_hang, so_dien_thoai, email, dia_chi, loai_khach_hang, tong_da_mua, so_don_hang, tong_chi_tieu, diem_thuong, ngay_tham_gia)
VALUES
 ('KH001', 'Chị Bảo', '0355696858', 'giabaomc0903@gmail.com', 'Thôn 6 Hải Tiến, xã Hải Ninh, tỉnh Quảng Ninh', 'VIP', 500000, 25, 500000, 50, CURRENT_DATE),
 ('KH002', 'Anh Long', '0866198289', 'dangduclong100@gmail.com', 'Thôn 4 Hải Tiến, xã Hải Ninh, tỉnh Quảng Ninh', 'Thường', 150000, 8, 150000, 15, CURRENT_DATE),
 ('KH003', 'Em Hương', '0923456789', 'huong123@gmail.com', '789 Hai Bà Trưng, Quận 1, TP.HCM', 'Mới', 0, 0, 0, 0, CURRENT_DATE)
ON CONFLICT (khach_hang_id) DO NOTHING;
