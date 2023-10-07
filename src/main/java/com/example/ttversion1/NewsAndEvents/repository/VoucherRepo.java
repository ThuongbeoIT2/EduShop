package com.example.ttversion1.NewsAndEvents.repository;

import com.example.ttversion1.NewsAndEvents.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepo extends JpaRepository<Voucher,Integer> {
    @Query("select o from voucher o where  o.user.email=:email order by o.discount asc ")
    List<Voucher> GetVoucher(String email);
    @Query("select o from voucher o where  o.code=:code and o.user.email=:email order by o.discount asc ")
    Optional<Voucher> GetVoucherByCode(String code,String email);
}
