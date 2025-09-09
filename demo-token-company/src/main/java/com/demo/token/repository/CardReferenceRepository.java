package com.demo.token.repository;

import com.demo.token.entity.CardReference;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardReferenceRepository extends JpaRepository<CardReference, Long> {

  Optional<CardReference> findByCiAndCardRefId(String ci, String cardRefId);
}
