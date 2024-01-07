package com.db.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.db.entity.BidEntity;
import com.db.entity.ProductsEntity;
import com.db.entity.UserEntity;
import com.db.exception.AuctionResponse;
import com.db.model.Bid;
import com.db.model.EndAuction;
import com.db.repository.BidRepository;
import com.db.repository.ProductRepository;
import com.db.repository.UserRepository;

import javassist.NotFoundException;

@Service
public class BidService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BidRepository bidRepository;

	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	private Calendar cal = Calendar.getInstance();
	private static Logger log = LogManager.getLogger("LOGGER_WITH_JSON_LAYOUT");
	String responseMsg = "";
	AuctionResponse responseJSON;

	public AuctionResponse registerBid(@Valid Bid bid) throws Exception{

		try {
			Optional<ProductsEntity> productEntity = Optional.ofNullable(productRepository.findById(bid.getProductId())
					.orElseThrow(() -> new NotFoundException("Product with id " + bid.getProductId() + " not found")));

			Optional<BidEntity> bidEntityDetails = bidRepository.findByProductId(bid.getProductId(), bid.getBuyerId());

			if (bidEntityDetails.isPresent()) {
				if (bidEntityDetails.get().getBuyerId() == bid.getBuyerId()) {
					responseMsg = "Exception Occured: BidService -> registerBid() : Can not palce multiple bid by same buyer for same product...";
					throw new Exception("Can not palce multiple bid by same buyer for same product...");
				} else
					responseJSON = placeBid(productEntity, bid);

			} else {
				responseJSON = placeBid(productEntity, bid);
			}
		} catch (Exception e) {
			log.info("Exception Occured: BidService -> registerBid() : " + e.getMessage());
			responseMsg = "Exception Occured: BidService -> registerBid() : " + e.getMessage();
			responseJSON = new AuctionResponse(responseMsg, HttpStatus.BAD_REQUEST);
		}

		return responseJSON;
	}

	private AuctionResponse placeBid(Optional<ProductsEntity> productEntity, Bid bid)  throws Exception{

		if ("AVAILABLE".equalsIgnoreCase(productEntity.get().getProductStatus())
				&& bid.getBidPrice() > productEntity.get().getAuctionPrice()) {

			BidEntity bidEntity = new BidEntity();

			bidEntity.setBidPrice(bid.getBidPrice());
			bidEntity.setBidTime(dateFormat.format(cal.getTime()));
			bidEntity.setBuyerId(bid.getBuyerId());
			bidEntity.setProductId(bid.getProductId());
			bidEntity.setProductStatus("AVAILABLE");

			bidRepository.save(bidEntity);
			responseMsg = "Bid Places Successfully";
		} else {
			responseMsg = "Product Already Sold Out, Can not place a bid..";
		}
		responseJSON = new AuctionResponse(responseMsg, HttpStatus.OK);
		return responseJSON;
	}

	public AuctionResponse updateBid(@Valid Bid bid) throws Exception{

		try {
			Optional<ProductsEntity> bidDetails = Optional.ofNullable(productRepository.findById(bid.getProductId())
					.orElseThrow(() -> new NotFoundException("Product with id " + bid.getProductId() + " not found")));

			if ("AVAILABLE".equalsIgnoreCase(bidDetails.get().getProductStatus())) {

				if (bidDetails.isPresent()) {
					if (bidDetails.get().getAuctionPrice() < bid.getBidPrice()) {
						bidRepository.updateBid(bid.getBidPrice(), bid.getProductId());
						responseMsg = "Bid Updated Successfully";
					} else
						return new AuctionResponse("Biding price can NOT be less then Auction Price",
								HttpStatus.BAD_REQUEST);
				}

			} else {
				return new AuctionResponse("Product Already Sold Out, Can not update the bid..",
						HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			log.info("Exception Occured: BidService -> updateBid() : " + e.getMessage());
			responseMsg = "Exception Occured: BidService -> updateBid() : " + e.getMessage();
			return new AuctionResponse(responseMsg, HttpStatus.BAD_REQUEST);
		}

		responseJSON = new AuctionResponse(responseMsg, HttpStatus.OK);
		return responseJSON;
	}

	public AuctionResponse deleteBid(@Valid Bid bid) throws Exception{
		try {
			Optional<ProductsEntity> bidDetails = Optional.ofNullable(productRepository.findById(bid.getProductId())
					.orElseThrow(() -> new NotFoundException("Product with id " + bid.getProductId() + " not found")));

			if (bidDetails.isPresent()) {
				bidRepository.deleteById(bid.getBidId());
				responseMsg = "Bid Deleted Successfully";
			}

		} catch (Exception e) {
			log.info("Exception Occured: BidService -> updateBid() : " + e.getMessage());
			responseMsg = "Exception Occured: BidService -> updateBid() : " + e.getMessage();
			return new AuctionResponse(responseMsg, HttpStatus.BAD_REQUEST);
		}

		responseJSON = new AuctionResponse(responseMsg, HttpStatus.OK);
		return responseJSON;

	}

	public AuctionResponse endAuction(EndAuction endAuction) throws Exception{

		try {
			Optional<ProductsEntity> productEntity = Optional.ofNullable(
					productRepository.findById(endAuction.getProductId()).orElseThrow(() -> new NotFoundException(
							"Product with id " + endAuction.getProductId() + " not found")));

			BidEntity bidEntity = bidRepository.getAllBidsByProductId(endAuction.getProductId(),
					productEntity.get().getProductStatus());
			bidEntity.setSoldPrice(bidEntity.getBidPrice());
			bidEntity.setProductStatus("SOLD");
			productEntity.get().setProductStatus("SOLD");

			Optional<UserEntity> user = userRepository.findById(bidEntity.getBuyerId());
			responseMsg = "Product Sold to highest bidder : \n Id: " + user.get().getUserId() + "\n Name: "
					+ user.get().getUserName();
			productRepository.save(productEntity.get());
			bidRepository.save(bidEntity);

		} catch (Exception e) {
			log.info("Exception Occured: BidService -> endAuction() : " + e.getMessage());
			responseMsg = "Exception Occured: BidService -> endAuction() : " + e.getMessage();
			return new AuctionResponse(responseMsg, HttpStatus.BAD_REQUEST);
		}

		responseJSON = new AuctionResponse(responseMsg, HttpStatus.OK);
		return responseJSON;
	}

}
