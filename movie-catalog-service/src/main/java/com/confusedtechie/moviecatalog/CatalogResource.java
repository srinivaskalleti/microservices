package com.confusedtechie.moviecatalog;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.confusedtechie.moviecatalog.model.CatalogItem;
import com.confusedtechie.moviecatalog.model.Movie;
import com.confusedtechie.moviecatalog.model.UserRating;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
		UserRating userRating = restTemplate.getForObject("http://localhost:8083/ratingsdata/user/"+userId, UserRating.class);
		return userRating.getRatings().stream().map(rating -> {
			Movie movie = restTemplate.getForObject("http://localhost:8082/movies/"+rating.getMovieId(), Movie.class);
			return new CatalogItem(movie.getName(), movie.getDesc(), rating.getRating());
		}).collect(Collectors.toList());
	}
}
