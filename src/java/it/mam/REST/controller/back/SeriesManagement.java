package it.mam.REST.controller.back;

import it.mam.REST.controller.RESTBaseController;
import it.mam.REST.data.model.CastMember;
import it.mam.REST.data.model.CastMemberSeries;
import it.mam.REST.data.model.Channel;
import it.mam.REST.data.model.ChannelEpisode;
import it.mam.REST.data.model.Episode;
import it.mam.REST.data.model.Genre;
import it.mam.REST.data.model.Series;
import it.univaq.f4i.iw.framework.result.FailureResult;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.RESTSecurityLayer;
import it.univaq.f4i.iw.framework.security.SecurityLayer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mirko
 */
public class SeriesManagement extends RESTBaseController{

    // prende il template di default di errore e e ci stampa il messaggio passato come parametro
    private void action_error(HttpServletRequest request, HttpServletResponse response, String message) {

        FailureResult fail = new FailureResult(getServletContext());
        fail.activate(message, request, response);
    }
      // prende tutti i generi e tutti i membri del cast e li passa al template insertSeries.ftl.html
    private void action_insert_series(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        request.setAttribute("genres", getDataLayer().getGenres());
        request.setAttribute("castMembers", getDataLayer().getCastMembers());
        result.activate("insertSeries.ftl.html", request, response);
    }
    
    private void action_save_series(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Series series = getDataLayer().createSeries();
        //Controllo che i campi siano validi
        if (checkSeriesInputData(request, response)){
        series.setName(request.getParameter("name"));
        try{
        series.setYear(SecurityLayer.checkNumeric(request.getParameter("year")));
        } catch (NumberFormatException e) {
            action_error(request, response, "Field Error");
        }
        series.setDescription(request.getParameter("description"));
        series.setImageURL(request.getParameter("imageURL"));
        series.setState(request.getParameter("state"));
        } else action_error(request, response, "Inserire i campi obbligatori!");
        
        //Mi prendo l'array dei generi dalla richiesta e lo trasformo in una lista
        String[] genres = request.getParameterValues("genres");
            List<Genre> genresList = new ArrayList();
                for (String s: genres){
                    try{
                    //prendo il genere dal DB e NON ci metto gli slash perché nel DB ce li ha già e non serve di toglierli perché non devo usarlo
                    genresList.add(getDataLayer().getGenre(SecurityLayer.checkNumeric(s)));
                    } catch (NumberFormatException e) {
                        action_error(request, response, "Internal Error");
                    }
                series.setGenres(genresList);
            }
        getDataLayer().storeSeries(RESTSecurityLayer.addSlashes(series));
        }

    private void action_insert_episode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        //Qui creo la lista delle serie che passo al template, in modo che si possa scegliere (opzionalmente)
        //la serie a cui appartiene l'episodio.
        request.setAttribute("series", getDataLayer().getSeries());      
        //Qui creo la lista dei canali che passo al template, in modo che si possa scegliere (opzionalmente)
        //il canale o i canali su cui verrà trasmesso l'episodio.
        request.setAttribute("channels", getDataLayer().getChannels());
        result.activate("insert_episode.ftl.html", request, response);
    }
    
    private void action_save_episode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Episode episode = getDataLayer().createEpisode();
        // controllare se sono stati compilati tutti i form necessari ed eliminare le possibilità di SQL injection
        if (checkEpisodeInputData(request, response)) {

            episode.setTitle(request.getParameter("title"));
            episode.setDescription((request.getParameter("description")));
            episode.setNumber(SecurityLayer.checkNumeric(request.getParameter("number")));
            episode.setSeason(SecurityLayer.checkNumeric(request.getParameter("season")));
            episode.setSeries(getDataLayer().getSeries(SecurityLayer.checkNumeric(request.getParameter("idseries"))));
            //Ricavo tutti i canali che l'utente ha scelto per l'episodio, li trasformo in lista e li setto nell'episodio
            String[] channels = request.getParameterValues("channels");
            List<Channel> channelList = new ArrayList();
            for (String c : channels) {
                try {
                    channelList.add(getDataLayer().getChannel(SecurityLayer.checkNumeric(c)));
                } catch (NumberFormatException e) {
                    action_error(request, response, "Field Error");
                }

            }
            episode.setChannels(channelList);
        } else {
            action_error(request, response, "Inserire i campi obbligatori");
        }

        getDataLayer().storeEpisode(RESTSecurityLayer.addSlashes(episode));

        //Aggiungo la data alla relazione MA È SBAGLIATO! In questo modo la data è la stessa per tutti i canali! 
        //Sistemare con i campi ricevuti nella request! E possibilmente spostare il controllo dell'if nel metodo in fondo!
        ChannelEpisode ce = getDataLayer().createChannelEpisode();
        if (request.getParameter("date") != null && request.getParameter("date").length() > 0) {
            ce.setDate((SecurityLayer.checkDate(request.getParameter("date")).getTime()));
        }
        ce.setEpisode(episode);

        for (Channel channel : episode.getChannels()) {
            ce.setChannel(channel);
            getDataLayer().storeChannelEpisode(ce);
        }

    }
    
    private void action_insert_channel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        TemplateResult result = new TemplateResult(getServletContext());
        result.activate("insert_channel.ftl.html", request, response);
    }

    private void action_save_channel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Channel channel = getDataLayer().createChannel();
        // controllare se sono stati compilati tutti i form necessari ed eliminare le possibilità di SQL injection
        if (checkChannelInputData(request, response)){
            //aggiungo gli slash prima di salvare su DB per evitare valori pericolosi
            channel.setName(request.getParameter("name"));
            channel.setType(request.getParameter("type"));
            try {
            channel.setNumber(SecurityLayer.checkNumeric(request.getParameter("number")));
             } catch (NumberFormatException e) {
            action_error(request, response, "Field Error");
        }
          } else {
            action_error(request, response, "Inserire i campi obbligatori");
        }
            
        getDataLayer().storeChannel(RESTSecurityLayer.addSlashes(channel));
    }
    
    private void action_insert_genre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        TemplateResult result = new TemplateResult(getServletContext());
        result.activate("insert_genre.ftl.html", request, response);
    }
    
    private void action_save_genre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Genre genre = getDataLayer().createGenre();
        if (request.getParameter("name") != null && request.getParameter("name").length() > 0) {
            genre.setName(request.getParameter("name"));
        }
      getDataLayer().storeGenre(RESTSecurityLayer.addSlashes(genre));
     }
    
    private void action_insert_castmember(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
        request.setAttribute("series", getDataLayer().getSeries());
        result.activate("insert_castmember.ftl.html", request, response);
    }
    
    private void action_save_castmember(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CastMember castMember = getDataLayer().createCastMember();
        CastMemberSeries cms = getDataLayer().createCastMemberSeries();
        if (checkCastMemberInputData(request, response)) {
            castMember.setName(request.getParameter("name"));
            castMember.setSurname(request.getParameter("surname"));
            try {
                castMember.setBirthDate((SecurityLayer.checkDate(request.getParameter("birthdate"))).getTime());
            } catch (NumberFormatException e) {
                action_error(request, response, "Invalid Datetime");
            }
            try {
                int gender = SecurityLayer.checkNumeric(request.getParameter("gender"));

                switch (gender) {
                    case 1:
                        castMember.setGender(CastMember.MALE);
                        break;
                    case 2:
                        castMember.setGender(CastMember.FEMALE);
                        break;
                    default:
                        action_error(request, response, "Invalid gender");
                }
            } catch (NumberFormatException e) {
                action_error(request, response, "Internal Error");
            }
            castMember.setCountry(request.getParameter("country"));
            castMember.setImageURL(request.getParameter("imageURL"));

            String[] series = request.getParameterValues("series");
            List<Series> seriesList = new ArrayList();
            if (series != null) {
                for (String s : series) {
                    try {
                        seriesList.add(RESTSecurityLayer.addSlashes(getDataLayer().getSeries(SecurityLayer.checkNumeric(s))));
                    } catch (NumberFormatException e) {
                        action_error(request, response, "Field Error");
                    }
                }
                castMember.setSeries(seriesList);
            }
            getDataLayer().storeCastMember(RESTSecurityLayer.addSlashes(castMember));

     //Salvo i dati della relazione
            String[] roles = request.getParameterValues("roles");
            cms.setCastMember(castMember);
            for (Series s : castMember.getSeries()) {
                for (String r : roles) {
                    cms.setSeries(s);
                    cms.setRole(r);
                    getDataLayer().storeCastMemberSeries(RESTSecurityLayer.addSlashes(cms));
                }
            }
        }
    }
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try{
        int sezione = SecurityLayer.checkNumeric(request.getParameter("sezione"));
        switch(sezione){
            case 1: if((request.getParameter("is")) != null) { 
                action_save_series(request, response);
                } else {
                action_insert_series(request, response);
                }
            break;
            case 2: if((request.getParameter("ie")) != null) { 
                action_save_episode(request, response);
                } else {
                action_insert_episode(request, response);
                }
            break;
            case 3: if((request.getParameter("ic")) != null) { 
                action_save_channel(request, response);
                } else {
                action_insert_channel(request, response);
                }
            break;
            case 4: if((request.getParameter("ig")) != null) { 
                action_save_genre(request, response);
                } else {
                action_insert_genre(request, response);
                }
            break;
            case 5: if((request.getParameter("icm")) != null) { 
                action_save_castmember(request, response);
                } else {
                action_insert_castmember(request, response);
                }
            break;
            default: action_error(request, response, "Field Error");
        }
        } catch (NumberFormatException ex ) {
            action_error(request, response, "Field Error");
        } catch (IOException ex) {
            action_error(request, response, "Internal Error");
        }
    }
    
    private boolean checkEpisodeInputData(HttpServletRequest request, HttpServletResponse response) {
        return (request.getParameter("title") != null && request.getParameter("title").length() > 0
                && request.getParameter("description") != null && request.getParameter("description").length() > 0
                && request.getParameter("number") != null && request.getParameter("number").length() > 0
                && request.getParameter("season") != null && request.getParameter("season").length() > 0
                && request.getParameter("idseries") != null && request.getParameter("idseries").length() > 0
                && request.getParameterValues("channels") != null && request.getParameterValues("channels").length > 0);
    }
    
    private boolean checkSeriesInputData(HttpServletRequest request, HttpServletResponse response){
        return request.getParameter("name") != null && request.getParameter("name").length() > 0
                && request.getParameter("year") != null && request.getParameter("year").length() > 0 
                && request.getParameter("description") != null && request.getParameter("description").length() > 0
                && request.getParameter("image_URL") != null && request.getParameter("image_URL").length() > 0
                && request.getParameter("state") != null && request.getParameter("state").length() > 0
                && request.getParameterValues("genres") != null && request.getParameterValues("genres").length > 0;
                 
    }
    
     private boolean checkChannelInputData(HttpServletRequest request, HttpServletResponse response){
        return (request.getParameter("name") != null && request.getParameter("name").length() > 0
                && request.getParameter("type") != null && request.getParameter("type").length() > 0)
                && request.getParameter("number") != null && request.getParameter("number").length() > 0;
    }
     
     private boolean checkCastMemberInputData(HttpServletRequest request, HttpServletResponse response) {
        return request.getParameter("name") != null && request.getParameter("name").length() > 0
                && request.getParameter("surname") != null && request.getParameter("surname").length() > 0
                && request.getParameter("birthdate") != null && request.getParameter("birthdate").length() > 0
                && request.getParameter("gender") != null && request.getParameter("gender").length() > 0
                && request.getParameter("country") != null && request.getParameter("country").length() > 0
                && request.getParameter("imageURL") != null && request.getParameter("imageURL").length() > 0
                && request.getParameterValues("series") != null && request.getParameterValues("series").length > 0
                && request.getParameterValues("roles") != null && request.getParameterValues("roles").length > 0;

    }
     
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}