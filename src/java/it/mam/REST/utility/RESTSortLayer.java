package it.mam.REST.utility;

import it.mam.REST.data.model.CastMember;
import it.mam.REST.data.model.Episode;
import it.mam.REST.data.model.News;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.UserSeries;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Mirko
 */
public class RESTSortLayer {

    public static final long SECOND_IN_MILLISECONDS = 1000;
    public static final long MINUTE_IN_MILLISECONDS = 60 * RESTSortLayer.SECOND_IN_MILLISECONDS;
    public static final long HOUR_IN_MILLISECONDS = 60 * RESTSortLayer.MINUTE_IN_MILLISECONDS;
    public static final long DAY_IN_MILLISECONDS = 24 * RESTSortLayer.HOUR_IN_MILLISECONDS;

    // SERIES
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
                return (getMediumRating((Series) o1) < getMediumRating((Series) o2) ? 1
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

    public static List<Series> trendify(List<Series> seriesList) {
        Date comparisonDate = new Date();
        comparisonDate.setTime(comparisonDate.getTime() - (RESTSortLayer.DAY_IN_MILLISECONDS * 30));
        seriesList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Series s1 = (Series) o1;
                int s1TrendyIndex = 0;
                Series s2 = (Series) o2;
                int s2TrendyIndex = 0;
                for (UserSeries us : s1.getUserSeries()) {
                    if (us.getAddDate().after(comparisonDate)) {
                        s1TrendyIndex++;
                    }
                }
                for (UserSeries us : s2.getUserSeries()) {
                    if (us.getAddDate().after(comparisonDate)) {
                        s2TrendyIndex++;
                    }
                }
                return s1TrendyIndex < s2TrendyIndex ? 1 : s1TrendyIndex == s2TrendyIndex ? 0 : -1;
            }
        });
        return seriesList;
    }

    public static List<List<Series>> sortByListSize(List<List<Series>> listOfList) {
        listOfList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((List<Series>) o1).size() < ((List<Series>) o2).size() ? 1 : ((List<Series>) o1).size() == ((List<Series>) o2).size() ? 0 : -1;
            }
        });
        return listOfList;
    }

    public static int getMediumRating(Series s) {
        List<UserSeries> usList = s.getUserSeries();
        int count = 0;
        int ratingsum = 0;
        for (UserSeries us : usList) {
            ratingsum += Integer.parseInt(us.getRating());
            count++;
        }
        if (count == 0) {
            return 0;
        }
        return Math.floorDiv(ratingsum, count);
    }

    // NEWS
    public static List<News> sortNewsByNumberOfComments(List<News> newsList) {
        newsList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((News) o1).getComments().size() < ((News) o2).getComments().size() ? 1 : ((News) o1).getComments().size() == ((News) o2).getComments().size() ? 0 : -1;
            }
        });
        return newsList;
    }

    public static List<News> sortNewsByNumberOfLike(List<News> newsList) {
        newsList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((News) o1).getLikes() < ((News) o2).getLikes() ? 1 : ((News) o1).getLikes() == ((News) o2).getLikes() ? 0 : -1;
            }
        });
        return newsList;
    }

    public static List<News> sortNewsByDate(List<News> newsList) {
        newsList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((News) o2).getDate().compareTo(((News) o1).getDate());
            }
        });
        return newsList;
    }

    //CASTMEMBERS
    public static List<CastMember> sortCastMemberBySurname(List<CastMember> castMemberList) {
        castMemberList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((CastMember) o1).getSurname().compareToIgnoreCase(((CastMember) o2).getSurname());
            }
        });
        return castMemberList;
    }

    //EPISODES
    public static List<Episode> sortEpisodeBySeriesAndNumber(List<Episode> episodeList) {
        episodeList.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Episode) o1).getSeries().getName().compareToIgnoreCase(((Episode) o2).getSeries().getName());
            }
        });
        return episodeList;
    }

}
