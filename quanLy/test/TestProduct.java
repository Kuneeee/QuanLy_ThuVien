import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import entity.HangHoa;

public class TestProduct {

    @Test
    void testHangHoaCreation() {
        HangHoa hangHoa = new HangHoa();
        hangHoa.setHanghoaID("HH001");
        hangHoa.setTenHangHoa("Test Product");
        hangHoa.setSoLuongHangHoa(10);
        hangHoa.setGiaNhap(new BigDecimal("100000"));
        
        assertEquals("HH001", hangHoa.getHanghoaID());
        assertEquals("Test Product", hangHoa.getTenHangHoa());
        assertEquals(10, hangHoa.getSoLuongHangHoa());
        assertEquals(new BigDecimal("100000"), hangHoa.getGiaNhap());
    }

    @Test
    void testHangHoaWithConstructor() {
        HangHoa hangHoa = new HangHoa("HH002", "Laptop", 5, 
                                      java.time.LocalDateTime.now(), 
                                      new BigDecimal("15000000"), 
                                      "Điện tử");
        
        assertEquals("HH002", hangHoa.getHanghoaID());
        assertEquals("Laptop", hangHoa.getTenHangHoa());
        assertEquals(5, hangHoa.getSoLuongHangHoa());
        assertEquals(new BigDecimal("15000000"), hangHoa.getGiaNhap());
        assertEquals("Điện tử", hangHoa.getLoaiHangHoa());
    }

    @Test
    void testHangHoaValidation() {
        HangHoa hangHoa = new HangHoa();
        hangHoa.setHanghoaID("HH003");
        hangHoa.setTenHangHoa("iPhone");
        hangHoa.setSoLuongHangHoa(0);
        hangHoa.setGiaNhap(new BigDecimal("25000000"));
        
        assertNotNull(hangHoa.getHanghoaID());
        assertNotNull(hangHoa.getTenHangHoa());
        assertTrue(hangHoa.getSoLuongHangHoa() >= 0);
        assertTrue(hangHoa.getGiaNhap().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testHangHoaToString() {
        HangHoa hangHoa = new HangHoa("HH004", "Samsung Galaxy", 20, 
                                      java.time.LocalDateTime.now(), 
                                      new BigDecimal("18000000"), 
                                      "Điện tử");
        String result = hangHoa.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("HH004"));
        assertTrue(result.contains("Samsung Galaxy"));
    }
}
