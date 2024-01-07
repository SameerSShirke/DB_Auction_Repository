package com.db.repository;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.db.entity.BidEntity;

@Repository
public interface BidRepository extends JpaRepository<BidEntity, Long> {

	@Query(value = "Update BIDS set BID_PRICE =:bidPrice WHERE PRODUCT_ID =:productId ",  nativeQuery = true)
	void updateBid(@NotNull Double bidPrice, @NotNull Long productId);

	@Query(value = "SELECT * FROM BIDS WHERE PRODUCT_ID =:productId and PRODUCT_STATUS =:productStatus ORDER BY BID_PRICE DESC LIMIT 1",  nativeQuery = true)
	BidEntity getAllBidsByProductId(@NotNull Long productId, @NotNull String productStatus);

	
	@Query(value = "SELECT * FROM BIDS WHERE PRODUCT_ID =:productId and PRODUCT_STATUS =:productStatus",  nativeQuery = true)
	Optional<BidEntity> getAllAvailableProducts(@NotNull Long productId, @NotNull String productStatus);

	@Query(value = "SELECT * FROM BIDS WHERE PRODUCT_ID =:productId and BIDDER =:buyerId ",  nativeQuery = true)
	Optional<BidEntity> findByProductId(@NotNull Long productId, @NotNull Long buyerId);

}
 