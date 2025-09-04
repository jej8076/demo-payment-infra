package com.demo.token.repository;

import com.demo.token.entity.CardReference;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CardReferenceRepository extends JpaRepository<CardReference, Long> {
    Optional<CardReference> findByCardRefId(String cardRefId);
}
