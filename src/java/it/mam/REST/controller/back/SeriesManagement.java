package it.mam.REST.controller.back;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.CastMember;
import it.mam.REST.data.model.CastMemberSeries;
import it.mam.REST.data.model.Channel;
import it.mam.REST.data.model.ChannelEpisode;
import it.mam.REST.data.model.Episode;
import it.mam.REST.data.model.Genre;
import it.mam.REST.data.model.Group;
import it.mam.REST.data.model.Series;
import it.mam.REST.data.model.User;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.RESTSecurityLayer;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mirko
 */
public class SeriesManagement extends RESTBaseController {

    // Creates the default error template and prints the message just received on it
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }

    /* =============================== INSERT - SAVE =======================================*/
    
    // Activates the insert series template
    private void action_insert_series(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
        }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("backContent_tpl", "insertSeries.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }
    
    // Receives all the necessary data to insert a series
    private void action_save_series(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            Series series = getDataLayer().createSeries();
            //Controllo che i campi siano validi
            if (checkSeriesInputData(request, response)) {
                series.setName(request.getParameter("seriesName"));
                series.setYear(SecurityLayer.checkNumeric(request.getParameter("seriesYear")));
                series.setDescription(request.getParameter("seriesDescription"));
                series.setImageURL(request.getParameter("seriesImageURL"));
                series.setState(request.getParameter("state"));
            } else {
                action_error(request, response, "Uno dei campi è vuoto!");
                return;
            }
            /*
             //Mi prendo l'array dei generi dalla richiesta e lo trasformo in una lista
             String[] genres = request.getParameterValues("genres");
             List<Genre> genresList = new ArrayList();
             for (String s: genres){
             //prendo il genere dal DB e NON ci metto gli slash perché nel DB ce li ha già e non serve di toglierli perché non devo usarlo
             genresList.add(getDataLayer().getGenre(SecurityLayer.checkNumeric(s)));
             series.setGenres(genresList);
             }
             */
            System.err.println(series);
            getDataLayer().storeSeries(RESTSecurityLayer.addSlashes(series));
            request.setAttribute("success", "Serie inserita correttamente!");
            action_insert_series(request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id or year is not a number
              action_error(request, response, "Riprova di nuovo!");
    }

    }

    // Activates the insert episode template
    private void action_insert_episode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("user", user);
            request.setAttribute("series", getDataLayer().getSeries());
            request.setAttribute("channels", getDataLayer().getChannels());
            request.setAttribute("backContent_tpl", "insertEpisode.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Receives all the necessary data to insert an episode
    private void action_save_episode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            Episode episode = getDataLayer().createEpisode(); 
            if (checkEpisodeInputData(request, response)) {

                episode.setTitle(request.getParameter("episodeTitle"));
                episode.setDescription((request.getParameter("episodeDescription")));
                episode.setNumber(SecurityLayer.checkNumeric(request.getParameter("episodeNumber")));
                episode.setSeason(SecurityLayer.checkNumeric(request.getParameter("episodeSeason")));
                episode.setSeries(getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("series"))));
                /*
                 //Ricavo tutti i canali che l'utente ha scelto per l'episodio, li trasformo in lista e li setto nell'episodio
                 String[] channels = request.getParameterValues("channels");
                 List<Channel> channelList = new ArrayList();
                 for (String c : channels) {
                 channelList.add(getDataLayer().getChannel(SecurityLayer.checkNumeric(c)));
                 }
                 episode.setChannels(channelList);
                 */
            } else {
                action_error(request, response, "Uno dei campi è vuoto!");
                return;
            }

            getDataLayer().storeEpisode(RESTSecurityLayer.addSlashes(episode));
                request.setAttribute("success", "Episodio inserito correttamente!");
                action_insert_episode(request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id or episode number or episode season is not a number
              action_error(request, response, "Riprova di nuovo!");
    }

    }

    // Activates the insert channel template
    private void action_insert_channel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("backContent_tpl", "insertChannel.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
          } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //Comment id or series id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Receives all the necessary data to insert a channel
    private void action_save_channel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            Channel channel = getDataLayer().createChannel();
            // controllare se sono stati compilati tutti i form necessari ed eliminare le possibilità di SQL injection
            if (checkChannelInputData(request, response)) {
                //aggiungo gli slash prima di salvare su DB per evitare valori pericolosi
                channel.setName(request.getParameter("channelName"));
                switch (SecurityLayer.checkNumeric(request.getParameter("type"))) {
                    case 1:
                        channel.setType(Channel.FREE);
                        break;
                    case 2:
                        channel.setType(Channel.PAY);
                        break;
                    default:
                        action_error(request, response, "Invalid type");
                        return;
                }
                channel.setNumber(SecurityLayer.checkNumeric(request.getParameter("channelNumber")));
            } else {
                request.setAttribute("error", "Uno dei campi è vuoto!");
                action_insert_channel(request, response);
                return;
            }

            getDataLayer().storeChannel(RESTSecurityLayer.addSlashes(channel));
            request.setAttribute("success", "Canale inserito correttamente!");
            action_insert_channel(request, response);
                } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    //Activates the insert genre template
    private void action_insert_genre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("backContent_tpl", "insertGenre.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Receives all the necessary data to insert a genre
    private void action_save_genre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            Genre genre = getDataLayer().createGenre();
            if (request.getParameter("genreName") != null && request.getParameter("genreName").length() > 0) {
                genre.setName(request.getParameter("genreName"));
            }
            getDataLayer().storeGenre(RESTSecurityLayer.addSlashes(genre));
            request.setAttribute("success", "Genere inserito correttamente!");
            action_insert_genre(request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }
   
    //Activates the insert castmember template
    private void action_insert_castmember(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("series", getDataLayer().getSeries());
            request.setAttribute("backContent_tpl", "insertCastMember.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Receives all the necessary data to insert a castmember
    private void action_save_castmember(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            CastMember castMember = getDataLayer().createCastMember();
            //CastMemberSeries cms = getDataLayer().createCastMemberSeries();
            if (checkCastMemberInputData(request, response)) {
                castMember.setName(request.getParameter("castMemberName"));
                castMember.setSurname(request.getParameter("castMemberSurname"));
                castMember.setCountry(request.getParameter("castMemberCountry"));
                castMember.setImageURL(request.getParameter("castMemberImageURL"));
                castMember.setBirthDate((SecurityLayer.checkDate(request.getParameter("castMemberBirthDate"))).getTime());
                int gender = SecurityLayer.checkNumeric(request.getParameter("gender"));

                switch (gender) {
                    case 1:
                        castMember.setGender(CastMember.MALE);
                        break;
                    case 2:
                        castMember.setGender(CastMember.FEMALE);
                        break;
                    default:
                        action_error(request, response, "Riprova di nuovo!");
                        return;
                }

//                String[] series = request.getParameterValues("series");
//                List<Series> seriesList = new ArrayList();
//                if (series != null) {
//                    for (String s : series) {
//                        seriesList.add(RESTSecurityLayer.addSlashes(getDataLayer().getSeries(SecurityLayer.checkNumeric(s))));
//                    }
//                    castMember.setSeries(seriesList);
//                }
                getDataLayer().storeCastMember(RESTSecurityLayer.addSlashes(castMember));

                //Salvo i dati della relazione
//                String[] roles = request.getParameterValues("roles");
//                cms.setCastMember(castMember);
//                for (Series s : castMember.getSeries()) {
//                    for (String r : roles) {
//                        cms.setSeries(s);
//                        cms.setRole(r);
//                        getDataLayer().storeCastMemberSeries(RESTSecurityLayer.addSlashes(cms));
//                    }
//                }
            } else {
                request.setAttribute("error", "Uno dei campi è vuoto!");
                action_insert_castmember(request, response);
                return;
            }
                request.setAttribute("success", "Membro del cast inserito correttamente!");
                action_insert_castmember(request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id or gender is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Activates the insert castmember-series template
    private void action_insert_castmemberSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("series", getDataLayer().getSeries());
            request.setAttribute("castmembers", getDataLayer().getCastMembers());
            request.setAttribute("backContent_tpl", "insertCastMemberSeries.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
            } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Receives all the necessary data to link a castmember to a series
    private void action_save_castmemberSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("user", user);
            if (checkCastMemberSeriesInputData(request, response)) {
                CastMemberSeries cms = getDataLayer().createCastMemberSeries();
                cms.setCastMember(getDataLayer().getCastMember(SecurityLayer.checkNumeric(request.getParameter("castMember"))));
                cms.setSeries(getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("series"))));
                cms.setRole(request.getParameter("role"));
                getDataLayer().storeCastMemberSeries(cms);
            } else {
            //Error: empty field
            request.setAttribute("error", "Uno dei campi è vuoto!");
                action_insert_castmemberSeries(request, response);
                return;
            }
                request.setAttribute("success", "Membro del cast e serie associati correttamente!");
                action_insert_castmemberSeries(request, response);
            } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id or castmember id or series id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }

    }

    // Activates the insert channel-episode template
    private void action_insert_channelEpisode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("channels", getDataLayer().getChannels());
            request.setAttribute("episodes", getDataLayer().getEpisodes());
            request.setAttribute("backContent_tpl", "insertChannelEpisode.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Receives all the necessary data to link an episode to a channel
    private void action_save_channelEpisode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            if (checkChannelEpisodeInputData(request, response)) {
                ChannelEpisode ce = getDataLayer().createChannelEpisode();
                ce.setChannel(getDataLayer().getChannel(SecurityLayer.checkNumeric(request.getParameter("channel"))));
                ce.setEpisode(getDataLayer().getEpisode(SecurityLayer.checkNumeric(request.getParameter("episode"))));
                Calendar c = Calendar.getInstance();
                c.clear();
                c.set(0, 0, 0, 0, 0, 0);
                c.setTimeInMillis((SecurityLayer.checkDate(request.getParameter("date")).getTimeInMillis() + SecurityLayer.checkTime(request.getParameter("time"))));
                ce.setDate(c.getTime());
                getDataLayer().storeChannelEpisode(ce);
            } else {
                request.setAttribute("error", "Uno dei campi è vuoto!");
                action_insert_channelEpisode(request, response);
                return;
            }
                request.setAttribute("success", "Canale ed episodio associati correttamente!");
                action_insert_channelEpisode(request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id or channel id or episode id is not a number or date is not valid or time is not valid
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Activates the insert genre-series template
    private void action_insert_genreSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("genres", getDataLayer().getGenres());
            request.setAttribute("series", getDataLayer().getSeries());
            request.setAttribute("backContent_tpl", "insertGenreSeries.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Receive all the necessary data to link a series to a genre
    private void action_save_genreSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            if (checkGenreSeriesInputData(request, response)) {
                Series s = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("series")));
                s.addGenre(getDataLayer().getGenre(SecurityLayer.checkNumeric(request.getParameter("genre"))));
                getDataLayer().storeSeries(s);
            } else {
                request.setAttribute("error", "Uno dei campi è vuoto!");
                action_insert_genreSeries(request, response);
                return;
            }
                request.setAttribute("success", "Genere e serie associati correttamente!");
                action_insert_genreSeries(request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id or series id or genre id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    /* ================================== REMOVE - DELETE ===================================*/
    
    // Activates the remove series template
    private void action_remove_series(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
        }
            request.setAttribute("user", user);
            request.setAttribute("series", getDataLayer().getSeries());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("backContent_tpl", "removeSeries.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }
    
    // Receives all the necessary data to delete series
    private void action_delete_series(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            if (request.getParameterValues("series") == null|| request.getParameterValues("series").length <= 0){
            request.setAttribute("error", "Uno dei campi è vuoto!");
            action_remove_series(request, response);
            return;
            }
            String[] series = request.getParameterValues("series");
            for(String s: series) {
            getDataLayer().removeSeries(getDataLayer().getSeries(SecurityLayer.checkNumeric(s)));
            }
            request.setAttribute("success", "Rimozione serie completata!");
            action_remove_series(request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id or series id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }

    }
    
    // Activates the remove episode template
    private void action_remove_episode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("user", user);
            request.setAttribute("series", getDataLayer().getSeries());
            request.setAttribute("episodes", getDataLayer().getEpisodes());
            request.setAttribute("backContent_tpl", "removeEpisode.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Receives all the necessary data to delete episodes
    private void action_delete_episode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            if (request.getParameterValues("episodes") == null|| request.getParameterValues("episodes").length <= 0){
            request.setAttribute("error", "Uno dei campi è vuoto!");
            action_remove_episode(request, response);
            return;
             }
             String[] episodes = request.getParameterValues("episodes");
             for(String e: episodes) {
            getDataLayer().removeEpisode(getDataLayer().getEpisode(SecurityLayer.checkNumeric(e)));
            }

                request.setAttribute("success", "Rimozione episodi completata!");
                action_remove_episode(request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id or episode number or episode season is not a number
              action_error(request, response, "Riprova di nuovo!");
    }

    }
    
    // Activates the remove channel template
    private void action_remove_channel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("channels", getDataLayer().getChannels());
            request.setAttribute("backContent_tpl", "removeChannel.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
          } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //Comment id or series id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Receives all the necessary data to delete channels
    private void action_delete_channel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
          try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            if (request.getParameterValues("channel") == null|| request.getParameterValues("channel").length <= 0){
            request.setAttribute("error", "Uno dei campi è vuoto!");
            action_remove_channel(request, response);
            return;
             }
             String[] channels = request.getParameterValues("channel");
             for(String c: channels) {
            getDataLayer().removeChannel(getDataLayer().getChannel(SecurityLayer.checkNumeric(c)));
            }
            request.setAttribute("success", "Canale inserito correttamente!");
            action_remove_channel(request, response);
                } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }
    
    //Activates the remove genre template
    private void action_remove_genre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("genres", getDataLayer().getGenres());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("backContent_tpl", "removeGenre.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Receives all the necessary data to delete genres
    private void action_delete_genre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            if (request.getParameterValues("genres") == null|| request.getParameterValues("genres").length <= 0){
            request.setAttribute("error", "Uno dei campi è vuoto!");
            action_remove_genre(request, response);
            return;
            }
            String[] genres = request.getParameterValues("genres");
            for(String g: genres) {
            getDataLayer().removeGenre(getDataLayer().getGenre(SecurityLayer.checkNumeric(g)));
        }
            request.setAttribute("success", "Rimozione generi completata!");
            action_remove_genre(request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }
   
    //Activates the remove castmember template
    private void action_remove_castmember(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("castMembers", getDataLayer().getCastMembers());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("series", getDataLayer().getSeries());
            request.setAttribute("backContent_tpl", "removeCastMember.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Receives all the necessary data to delete castmembers
    private void action_delete_castmember(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            if (request.getParameterValues("castMembers") == null|| request.getParameterValues("castMembers").length <= 0){
            request.setAttribute("error", "Uno dei campi è vuoto!");
            action_remove_castmember(request, response);
            return;
            }
            String[] castmembers = request.getParameterValues("castMembers");
            for(String cm: castmembers) {
            getDataLayer().removeCastMember(getDataLayer().getCastMember(SecurityLayer.checkNumeric(cm)));
            }
                request.setAttribute("success", "Rimozione membri del cast completata!");
                action_remove_castmember(request, response);
            } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id or gender is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Activates the remove castmember-series template
    private void action_remove_castmemberSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("series", getDataLayer().getSeries());
            request.setAttribute("castMembers", getDataLayer().getCastMembers());
            request.setAttribute("backContent_tpl", "removeCastMemberSeries.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
            } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Receives all the necessary data to destroy link between castmember and series
    private void action_delete_castmemberSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("user", user);
           if (!(checkCastMemberSeriesInputData(request, response))){
            request.setAttribute("error", "Uno dei campi è vuoto!");
            action_remove_castmemberSeries(request, response);
            return;
             }
           CastMember cm = getDataLayer().getCastMember(SecurityLayer.checkNumeric(request.getParameter("castMember")));
           Series s = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("series")));
           CastMemberSeries cms = getDataLayer().getCastMemberSeries(cm, s, request.getParameter("role"));
           if (cms == null){
               request.setAttribute("error", "Questo membro del cast e questa serie non sono associati!");
                action_remove_castmemberSeries(request, response);
                return;
           }
            getDataLayer().removeCastMemberSeries(cms);
                request.setAttribute("success", "Membro del cast e serie separati correttamente!");
                action_remove_castmemberSeries(request, response);
            } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id or castmember id or series id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }

    }

    // Activates the remove channel-episode template
    private void action_remove_channelEpisode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("channels", getDataLayer().getChannels());
            request.setAttribute("episodes", getDataLayer().getEpisodes());
            request.setAttribute("backContent_tpl", "removeChannelEpisode.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Receives all the necessary data to destroy link between episode and channel
    private void action_delete_channelEpisode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            if (!(checkChannelEpisodeInputData(request, response))){
                request.setAttribute("error", "Uno dei campi è vuoto!");
                action_remove_channelEpisode(request, response);
                return;
            }
                Channel c = getDataLayer().getChannel(SecurityLayer.checkNumeric(request.getParameter("channel")));
                Episode e = getDataLayer().getEpisode(SecurityLayer.checkNumeric(request.getParameter("episode")));
                Calendar cl = Calendar.getInstance();
                cl.clear();
                cl.set(0, 0, 0, 0, 0, 0);
                cl.setTimeInMillis((SecurityLayer.checkDate(request.getParameter("date")).getTimeInMillis() + SecurityLayer.checkTime(request.getParameter("time"))));
                ChannelEpisode ce = getDataLayer().getChannelEpisode(c, e, cl.getTime());
                if (ce == null){
               request.setAttribute("error", "Questo canale e questo episodio non sono associati!");
                action_remove_channelEpisode(request, response);
                return;
                }
                getDataLayer().removeChannelEpisode(ce);
            
                request.setAttribute("success", "Canale ed episodio separati correttamente!");
                action_remove_channelEpisode(request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id or channel id or episode id is not a number or date is not valid or time is not valid
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Activates the remove genre-series template
    private void action_remove_genreSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("where", "back");
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("genres", getDataLayer().getGenres());
            request.setAttribute("series", getDataLayer().getSeries());
            request.setAttribute("backContent_tpl", "removeGenreSeries.ftl.html");
            result.activate("../back/backOutline.ftl.html", request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }

    // Receive all the necessary data to destroy link between series and genre
    private void action_delete_genreSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            if (SecurityLayer.checkSession(request) != null) {
            User user = getDataLayer().getUser(SecurityLayer.checkNumeric((request.getSession().getAttribute("userid")).toString()));
            if (user.getGroup().getID() != Group.ADMIN) {
                action_error(request, response, "Non hai i permessi per effettuare questa operazione!");
            return;
            }
            request.setAttribute("user", user);
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            if (!(checkGenreSeriesInputData(request, response))){
                request.setAttribute("error", "Uno dei campi è vuoto!");
                action_remove_genreSeries(request, response);
                return;
            }
             Series s = getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("series")));
             Genre g = getDataLayer().getGenre(SecurityLayer.checkNumeric(request.getParameter("genre")));
             if(!(s.getGenres().contains(g))){
                request.setAttribute("error", "Questo genere e questa serie non sono associati!");
                action_remove_genreSeries(request, response);
                return;
           }
                s.removeGenre(g);
                request.setAttribute("success", "Genere e serie separati correttamente!");
                action_remove_genreSeries(request, response);
        } else {
            //User session is no longer valid
            request.setAttribute("error", "Devi essere loggato per eseguire quest'azione!");
            result.activate("logIn.ftl.html", request, response);
        }
          } catch (NumberFormatException ex) {
              //User id or series id or genre id is not a number
              action_error(request, response, "Riprova di nuovo!");
    }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            int sezione = SecurityLayer.checkNumeric(request.getParameter("sezione"));
            switch (sezione) {
                case 1:
                    request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("is")) != null) {
                        action_save_series(request, response);
                    } else {
                        action_insert_series(request, response);
                    }
                    break;
                case 2:
                    request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("ie")) != null) {
                        action_save_episode(request, response);
                    } else {
                        action_insert_episode(request, response);
                    }
                    break;
                case 3:
                    request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("ic")) != null) {
                        action_save_channel(request, response);
                    } else {
                        action_insert_channel(request, response);
                    }
                    break;
                case 4:
                    request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("ig")) != null) {
                        action_save_genre(request, response);
                    } else {
                        action_insert_genre(request, response);
                    }
                    break;
                case 5:
                    request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("icm")) != null) {
                        action_save_castmember(request, response);
                    } else {
                        action_insert_castmember(request, response);
                    }
                    break;
                case 6:
                    request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("icms")) != null) {
                        action_save_castmemberSeries(request, response);
                    } else {
                        action_insert_castmemberSeries(request, response);
                    }
                    break;
                case 7:
                    request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("ice")) != null) {
                        action_save_channelEpisode(request, response);
                    } else {
                        action_insert_channelEpisode(request, response);
                    }
                    break;
                case 8:
                    request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("igs")) != null) {
                        action_save_genreSeries(request, response);
                    } else {
                        action_insert_genreSeries(request, response);
                    }
                    break;
                 case 9:
                     request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("rs")) != null) {
                        action_delete_series(request, response);
                    } else {
                        action_remove_series(request, response);
                    }
                    break;
                  case 10:
                      request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("re")) != null) {
                        action_delete_episode(request, response);
                    } else {
                        action_remove_episode(request, response);
                    }
                    break;
                   case 11:
                       request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("rc")) != null) {
                        action_delete_channel(request, response);
                    } else {
                        action_remove_channel(request, response);
                    }
                    break;
                    case 12:
                        request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("rg")) != null) {
                        action_delete_genre(request, response);
                    } else {
                        action_remove_genre(request, response);
                    }
                    break;
                    case 13:
                        request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("rcm")) != null) {
                        action_delete_castmember(request, response);
                    } else {
                        action_remove_castmember(request, response);
                    }
                    break;
                    case 14:
                        request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("rcms")) != null) {
                        action_delete_castmemberSeries(request, response);
                    } else {
                        action_remove_castmemberSeries(request, response);
                    }
                    break;
                    case 15:
                        request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("rce")) != null) {
                        action_delete_channelEpisode(request, response);
                    } else {
                        action_remove_channelEpisode(request, response);
                    }
                    break;
                    case 16:
                        request.setAttribute("currentSection", sezione);
                    if ((request.getParameter("rgs")) != null) {
                        action_delete_genreSeries(request, response);
                    } else {
                        action_remove_genreSeries(request, response);
                    }
                    break;
                default:
                    action_error(request, response, "Riprova di nuovo!");
            }
        } catch (NumberFormatException | IOException ex) {
            action_error(request, response, "Riprova di nuovo!");
        }
    }

    // Checks if all the input fields have been filled
    private boolean checkEpisodeInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("episodeTitle") != null && request.getParameter("episodeTitle").length() > 0
                && request.getParameter("episodeDescription") != null && request.getParameter("episodeDescription").length() > 0
                && request.getParameter("episodeNumber") != null && request.getParameter("episodeNumber").length() > 0
                && request.getParameter("episodeSeason") != null && request.getParameter("episodeSeason").length() > 0
                && request.getParameter("series") != null && request.getParameter("series").length() > 0;
        //&& request.getParameterValues("channels") != null && request.getParameterValues("channels").length > 0);
    }

    // Checks if all the input fields have been filled
    private boolean checkSeriesInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("seriesName") != null && request.getParameter("seriesName").length() > 0
                && request.getParameter("seriesYear") != null && request.getParameter("seriesYear").length() > 0
                && request.getParameter("seriesDescription") != null && request.getParameter("seriesDescription").length() > 0
                && request.getParameter("seriesImageURL") != null && request.getParameter("seriesImageURL").length() > 0
                && request.getParameter("state") != null && request.getParameter("state").length() > 0;
        //&& request.getParameterValues("genres") != null && request.getParameterValues("genres").length > 0;

    }

    // Checks if all the input fields have been filled
    private boolean checkChannelInputData(HttpServletRequest request, HttpServletResponse response) {
        return (request.getParameter("channelName") != null && request.getParameter("channelName").length() > 0
                && request.getParameter("type") != null && request.getParameter("type").length() > 0)
                && request.getParameter("channelNumber") != null && request.getParameter("channelNumber").length() > 0;
    }

    // Checks if all the input fields have been filled
    private boolean checkCastMemberInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("castMemberName") != null && request.getParameter("castMemberName").length() > 0
                && request.getParameter("castMemberSurname") != null && request.getParameter("castMemberSurname").length() > 0
                && request.getParameter("castMemberBirthDate") != null && request.getParameter("castMemberBirthDate").length() > 0
                && request.getParameter("gender") != null && request.getParameter("gender").length() > 0
                && request.getParameter("castMemberCountry") != null && request.getParameter("castMemberCountry").length() > 0
                && request.getParameter("castMemberImageURL") != null && request.getParameter("castMemberImageURL").length() > 0;
        // && request.getParameterValues("series") != null && request.getParameterValues("series").length > 0
        // && request.getParameterValues("roles") != null && request.getParameterValues("roles").length > 0;

    }

    // Checks if all the input fields have been filled
    private boolean checkCastMemberSeriesInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("castMember") != null && request.getParameter("castMember").length() > 0
                && request.getParameter("series") != null && request.getParameter("series").length() > 0
                && request.getParameter("role") != null && request.getParameter("role").length() > 0;
    }

    // Checks if all the input fields have been filled
    private boolean checkChannelEpisodeInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("channel") != null && request.getParameter("channel").length() > 0
                && request.getParameter("episode") != null && request.getParameter("episode").length() > 0
                && request.getParameter("date") != null && request.getParameter("date").length() > 0
                && request.getParameter("time") != null && request.getParameter("time").length() > 0;
    }

    // Checks if all the input fields have been filled
    private boolean checkGenreSeriesInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("genre") != null && request.getParameter("genre").length() > 0
                && request.getParameter("series") != null && request.getParameter("series").length() > 0;
    }

    @Override
    public String getServletInfo() {
        return "This servlet activates all the series management templates and allow to insert series, episodes, genres, castmembers, channels "
                + "and to link them";
    }
}
