package it.mam.REST.utility;

import it.mam.REST.data.model.ChannelEpisode;
import it.mam.REST.data.model.Episode;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;
import it.mam.REST.data.model.UserSeries;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author alex
 */
public class RESTUtility {

    public static String stringToMD5(String string) {
        String result = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(string.getBytes(Charset.forName("UTF-8")), 0, string.length());
            result = new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            //
        }
        return result;
    }

    public static void checkNotifications(User user, HttpServletRequest request, HttpServletResponse response) {
        if (!user.getNotificationStatus()) {
            return;
        }
        int count = 0;
        boolean trovato;
        Date now = new Date();
        for (UserSeries us : user.getUserSeries()) {
            if (us.getAnticipationNotification() == null) {
                continue;
            }
            Series s = us.getSeries();
            trovato = false;
            for (Episode e : s.getEpisodes()) {
                if (trovato) {
                    break;
                }
                for (ChannelEpisode ce : e.getChannelEpisode()) {
                    if (us.getEpisode() + 1 == e.getNumber() && us.getSeason() == e.getSeason()
                            && (now.getTime() >= ce.getDate().getTime() - us.getAnticipationNotification().getTime() - RESTSortLayer.HOUR_IN_MILLISECONDS)
                            && now.before(ce.getDate())) {
                        count++;
                        trovato = true;
                    }
                }
            }
        }
        request.setAttribute("notifyCount", count);
    }

}
