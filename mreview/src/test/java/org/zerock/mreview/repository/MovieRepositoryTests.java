package org.zerock.mreview.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.mreview.entity.Movie;
import org.zerock.mreview.entity.MovieImage;

import javax.transaction.Transactional;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class MovieRepositoryTests {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieImageRepository imageRepository;


//  @Commit
//   @Transactional
//    @Test
//    // 영화와 영화 이미지 삽입 추가 테스트 코드
//    public void insertMovies() {
//
//        IntStream.rangeClosed(1,100).forEach(i -> {
//            Movie movie = Movie.builder().title("Movie....." + i).build();
//
//            System.out.println("------------------------------------------");
//
//            // 영화와 영화 이미지는 같은 시점에 insert 되어야 하므로
//            // 우선 save()
//            movieRepository.save(movie);
//
//            int count = (int)(Math.random() * 5) + 1; //1,2,3,4
//
//            for(int j=0 ; j<count ; j++) {
//                MovieImage movieImage = MovieImage.builder()
//                        .uuid(UUID.randomUUID().toString())
//                        .movie(movie)
//                        .imgName("test" + j + ".jpg").build();
//
//                imageRepository.save(movieImage);
//            }
//
//            System.out.println("==============================================");
//
//        });
//    }


//    @Test
//    public void testListPage() {
//        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
//
//        Page<Object[]> result = movieRepository.getListPage(pageRequest);
//
//        for(Object[] objects : result.getContent()) {
//            System.out.println(Arrays.toString(objects));
//        }
//    }

    @Test
    public void testGetMovieWithAll() {
        List<Object[]> result = movieRepository.getMovieWithAll(94L);

        System.out.println(result);

        for(Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
    }
}
