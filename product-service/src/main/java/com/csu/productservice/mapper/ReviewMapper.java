package com.csu.productservice.mapper;

import com.csu.productservice.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ReviewMapper {
    public int insertReview(Review review);
    public List<Review> getAllReviews();
    public List<Review> getReviewsByItem(String itemid);
    public List<Review> getReviewsByUser(Long userid);
}
