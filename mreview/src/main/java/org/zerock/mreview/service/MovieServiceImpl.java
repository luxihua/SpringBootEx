package org.zerock.mreview.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.mreview.dto.MovieDTO;
import org.zerock.mreview.dto.PageRequestDTO;
import org.zerock.mreview.dto.PageResultDTO;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;
import org.zerock.mreview.repository.MovieImageRepository;
import org.zerock.mreview.repository.MovieRepository;

import javax.transaction.TransactionScoped;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    private final MovieImageRepository imageRepository;


    // 추가된 DtoToEntity는 Map 타입으로 Movie 객체와 MovieImage 객체의 리스트를 처리하면 됨.
    @TransactionScoped
    @Override
    public Long register(MovieDTO movieDTO) {

        Map<String, Object> entityMap = dtoToEntity(movieDTO);
        Movie movie = (Movie) entityMap.get("movie");
        List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");

        movieRepository.save(movie);

        movieImageList.forEach(movieImage -> {
            imageRepository.save(movieImage);
        });

        return movie.getMno();
    }

    @Override
    public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("mno").descending());
        Page<Object[]> result = movieRepository.getListPage(pageable);

        Function<Object[], MovieDTO> fn = (arr -> entitiesToDTO(
                (Movie)arr[0] ,
                (List<MovieImage>)(Arrays.asList((MovieImage)arr[1])),
                (Double) arr[2],
                (Long)arr[3])
        );

        return new PageResultDTO<>(result,fn);

    }


    @Override
    public MovieDTO getMovie(Long mno) {
        // 특정영화를 조회하기 위해 각 테이블을 조인한 쿼리문의 결과를
        // List<Object[]> 형태로 반환
        List<Object[]> result = movieRepository.getMovieWithAll(mno);

        // Movie 엔티티는 가장 앞에 존재
        Movie movie = (Movie) result.get(0)[0];

        // 영화의 이미지 개수만큼 MovieImage객체 필요
        List<MovieImage> movieImageList = new ArrayList<>();

        // 각 영화별 이미지가 여러개라면 for문으로 movieImageList에 추가
        result.forEach(arr -> {
            MovieImage movieImage = (MovieImage) arr[1];
            movieImageList.add(movieImage);
        });

        Double avg = (Double) result.get(0)[2]; // 평균 평점 - 모든 Row가 동일한 값
        Long reviewCnt = (Long) result.get(0)[3]; // 리뷰 개수 - 모든 Row가 동일한 값

        return entitiesToDTO(movie, movieImageList, avg, reviewCnt);
    }
}
