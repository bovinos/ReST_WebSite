package it.mam.REST.utility;

import it.mam.REST.data.model.News;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.UserSeries;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Mirko
 */
public class RESTSortLayer {

    public static List<Series> sortSeriesByName(List<Series> seriesList) {
        seriesList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Series) o1).getName().compareToIgnoreCase(((Series) o2).getName());
            }
        });
        return seriesList;
    }

    public static List<Series> sortSeriesByRating(List<Series> seriesList) {
        seriesList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return (getMediumRating((Series) o1) > getMediumRating((Series) o2) ? 1
                        : getMediumRating((Series) o1) == getMediumRating((Series) o2) ? 0 : -1);
            }
        });
        return seriesList;
    }

    public static List<Series> sortSeriesByPopularity(List<Series> seriesList) {
        seriesList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return (((Series) o1).getAddCount() < ((Series) o2).getAddCount() ? 1
                        : ((Series) o1).getAddCount() == ((Series) o2).getAddCount() ? 0 : -1);
            }
        });
        return seriesList;
    }

    public static List<Series> sortSeriesByYear(List<Series> seriesList) {
        seriesList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return (((Series) o1).getYear() > ((Series) o2).getYear() ? 1
                        : ((Series) o1).getYear() == ((Series) o2).getYear() ? 0 : -1);
            }
        });
        return seriesList;
    }

    private static int getMediumRating(Series s) {
        List<UserSeries> usList = s.getUserSeries();
        int count = 0;
        int ratingsum = 0;
        for (UserSeries us : usList) {
            ratingsum += Integer.parseInt(us.getRating());
            count++;
        }
        return Math.floorDiv(ratingsum, count);
    }

    // NEWS
    public static void sortNewsByNumberOfComments(List<News> newsList) {
        newsList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((News) o1).getComments().size() > ((News) o2).getComments().size() ? 1 : ((News) o1).getComments().size() == ((News) o2).getComments().size() ? 0 : -1;
            }
        });
    }

    public static void sortNewsByNumberOfLike(List<News> newsList) {
        newsList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((News) o1).getLikes() > ((News) o2).getLikes() ? 1 : ((News) o1).getLikes() == ((News) o2).getLikes() ? 0 : -1;
            }
        });
    }

    public static void sortNewsByDate(List<News> newsList) {
        newsList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((News) o1).getDate().compareTo(((News) o1).getDate());
            }
        });
    }
}
